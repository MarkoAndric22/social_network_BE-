package com.example.network.services;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

public interface FileHandler {

    String SingleFileUpload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException;



    void downloadImage(String imageUrl, String destinationPath) throws IOException;
}
