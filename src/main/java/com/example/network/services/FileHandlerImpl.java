package com.example.network.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileHandlerImpl implements FileHandler {

    private static String UPLOADED_FOLDER = "C:\\temp\\";

    @Override
    public String SingleFileUpload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException e) {
            throw e;
        }
        return "redirect:/uploadStatus";
    }

    @Override
    public void downloadImage(String imageUrl, String destinationPath) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = new BufferedInputStream(url.openStream());
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             FileOutputStream fos = new FileOutputStream(destinationPath)) {

            byte[] buf = new byte[1024];
            int n;

            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }

            byte[] response = out.toByteArray();
            fos.write(response);
        }
    }

}
