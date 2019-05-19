package com.abuqool.sqool.controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.abuqool.sqool.auth.AuthService;
import com.abuqool.sqool.auth.MgmtRestfulOps;
import com.abuqool.sqool.service.file.StorageService;
import com.abuqool.sqool.service.file.impl.StorageFileNotFoundException;
import com.abuqool.sqool.service.mgmt.MgmtProductService;
import com.abuqool.sqool.utils.ExcelUtils;


@RestController
@RequestMapping("/upload")
public class FileUploadController {
    protected static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private StorageService storageService;

    @Autowired
    private MgmtProductService productService;

    @Autowired
    private AuthService authService;

    @PostMapping("/products/")
    public String batchUploadProducts(
            @RequestParam("token") String token,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if(!authService.isAdmReqAllowed(token, MgmtRestfulOps.BATCH_UPLOAD_PRODUCTS)) {
            return "Error";
        }
        List<Map<String, String>> data = ExcelUtils.parseXlsx(file);
        logger.debug("Done parsing file");
        if(data != null) {
            productService.batchSaveProducts(data);
            logger.debug("Done saving products.");
            return "Done";
        }
        logger.debug("Got null as result.");
        return "Error";
    }

    @PostMapping("/pic/")
    public String uploadPic(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam("token") String token,
            @RequestParam("folder") String folder,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if(!authService.isAdmReqAllowed(token, MgmtRestfulOps.UPLOAD_PIC)) {
            return "Error";
        }
        try {
            storageService.store(folder, file);
        }catch(Exception ex) {
            logger.error("Error on saving file {"+file.getOriginalFilename()+"}");
            throw ex;
        }
        return file.getOriginalFilename();
    }

    @PostMapping("/pics/")
    public String uploadPics(
            @RequestParam("token") String token,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if(!authService.isAdmReqAllowed(token, MgmtRestfulOps.BATCH_UPLOAD_PICS)) {
            return "Error";
        }
        try {
            storageService.extractPics(file);
        }catch(Exception ex) {
            logger.error("Error on extracting file {"+file.getOriginalFilename()+"}");
            throw ex;
        }

        return file.getOriginalFilename();
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}