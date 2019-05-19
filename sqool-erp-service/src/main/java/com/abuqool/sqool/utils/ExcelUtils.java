package com.abuqool.sqool.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.abuqool.sqool.service.file.impl.StorageException;

public class ExcelUtils {
    protected static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    private ExcelUtils() {
    }

    public static List<Map<String, String>> parseXlsx(MultipartFile file) {
        try {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory " + filename);
            }
            Path filePath = Paths.get("/tmp/uploads").resolve(filename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            File f = new File(filePath.toString());
            Workbook wb = new XSSFWorkbook(new FileInputStream(f));
            Sheet sheet = wb.getSheetAt(0);

            int firstRowIndex = sheet.getFirstRowNum()+1;//row 1 - label, row 2 - key
            int lastRowIndex = sheet.getLastRowNum();
            List<Map<String, String>> data = new ArrayList<>();
            Map<String, String> indexToKey = new HashMap<>();
            for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                Row row = sheet.getRow(rIndex);
                if (row != null) {
                    Map<String, String> r = new HashMap<>();
                    int firstCellIndex = row.getFirstCellNum();
                    int lastCellIndex = row.getLastCellNum();
                    for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
                        Cell cell = row.getCell(cIndex);
                        if (cell != null) {
                            String idxStr = Integer.toString(cIndex);
                            String value = SecUtils.stripXSS(cell.toString());
                            if(rIndex == firstRowIndex) {
                                //map index to key
                                indexToKey.put(idxStr, value);
                            }else {
                                String key = indexToKey.get(idxStr);
                                r.put(key, value);
                            }
                        }
                    }
                    if(rIndex > firstRowIndex) {//1st row are keys
                        data.add(r);
                    }
                }
            }
            return data;
        } catch (IOException e) {
            logger.error("Error on parsing Excel", e);
            return null;
        }
    }
}
