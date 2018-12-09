package com.oasys.controllers;

import com.oasys.service.GoogleCloudStorage;
import net.bytebuddy.asm.Advice;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
