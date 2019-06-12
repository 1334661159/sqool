package com.abuqool.sqool.service.file.impl;

import com.abuqool.sqool.integration.qcloud.QCloudService;
import com.abuqool.sqool.service.file.StorageService;
import com.abuqool.sqool.service.mgmt.MgmtProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    private MgmtProductService productService;

    @Autowired
    private QCloudService qcloudService;

    public FileSystemStorageService() {
        this.rootLocation = Paths.get("/tmp/uploads");
    }

    @Override
    public void store(String folder, MultipartFile file) {
        Path filePath = saveFile(file);
        uploadToQCloud(folder, file.getOriginalFilename(), filePath);
    }

    @Override
    public void store(String folder, String filename, byte[] data) {
        try {
            Path filePath = saveAsFile(filename, new ByteArrayInputStream(data));
            uploadToQCloud(folder, filename, filePath);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }
    
    private Path saveFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                return saveAsFile(filename, inputStream);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    public Path saveAsFile(String filename,InputStream inputStream) throws IOException {
        if (filename.contains("..")) {
            // This is a security check
            throw new StorageException(
                    "Cannot store file with relative path outside current directory "
                            + filename);
        }
        Path filePath = this.rootLocation.resolve(filename);
        Files.copy(inputStream, filePath,
            StandardCopyOption.REPLACE_EXISTING);
        return filePath;
    }

    private void uploadToQCloud(String folder, String fileName, Path filePath) {
        try{
            File f = new File(filePath.toString());
            qcloudService.putObject(folder, fileName, f);
            f.delete();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void extractPics(MultipartFile f) {
        /*
         * input: a .zip file
         * action: extract the zip, and deal with each .jpg file
         */
        if(!f.getOriginalFilename().endsWith(".zip")) {
            return;
        }
        Path filePath = saveFile(f);
        List<String> pics = new ArrayList<>();
        try {
            ZipFile zipFile = new ZipFile(filePath.toString());
            Enumeration<?> enu = zipFile.entries();
            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

                String name = zipEntry.getName();
                if(!name.endsWith(".jpg")) {
                    continue;
                }
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n", 
                        name, size, compressedSize);

                File file = new File(name);
                if (name.endsWith("/")) {
                    file.mkdirs();
                    continue;
                }

                File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }

                InputStream is = zipFile.getInputStream(zipEntry);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = is.read(bytes)) >= 0) {
                    fos.write(bytes, 0, length);
                }
                is.close();
                fos.close();

                uploadToQCloud("", name, this.rootLocation.resolve(file.getAbsolutePath()));
                pics.add(name);
                file.delete();
            }
            zipFile.close();
            File file = new File(filePath.toString());
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String name : pics) {
            /*
             * if name matches product code, set as product's picUrl
             * else if find sku code start with current pic name, if skuCode.lenght - name.lenght == 3, set as sku's picUrl
             */
            productService.tryMatchPic(name.substring(0, name.length()-4),
                "https://cdn-1258157285.cos.ap-shanghai.myqcloud.com/abuqool/"+name);
        }
    }
}
