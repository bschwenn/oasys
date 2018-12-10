package com.oasys.controllers;

import com.oasys.service.GoogleCloudStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StorageController {
    @Autowired
    private GoogleCloudStorage googleCloudStorage;

    // Responds with URL of the image
    @PostMapping("/image")
    public String handleFileUpload(@RequestParam("file") MultipartFile image) {
        try {
            return googleCloudStorage.uploadFile(image);
        } catch (Exception e) {
            return "";
        }
    }
}
