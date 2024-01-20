package com.example.network.services;

import com.example.network.exceptions.AuthorizationCustomException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

public interface FileHandler {

    String SingleFileUpload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, AuthorizationCustomException;



    void downloadImage(String imageUrl, String destinationPath) throws IOException, AuthorizationCustomException;
}
