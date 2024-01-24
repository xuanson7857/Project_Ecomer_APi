package com.ra.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface UploadService {
    String uploadImage(MultipartFile file);

}
