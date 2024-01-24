package com.ra.service.Impl;

import com.ra.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Service

public class UploadServiceImpl implements UploadService {
    @Value("${path-upload}")
    private String pathUpload;
    @Value("${server.port}")
    private String port;
    @Override
    public String uploadImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            FileCopyUtils.copy(file.getBytes(),new File(pathUpload+fileName));
            return "http://localhost:"+port+"/"+fileName;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
