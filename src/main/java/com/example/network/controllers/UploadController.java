package com.example.network.controllers;

import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.services.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(path = "/")
public class UploadController {

    @Autowired
    private FileHandler fileHandler;


    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");
        String result = null;
        try {
            result = fileHandler.SingleFileUpload(file, redirectAttributes);
        } catch (IOException | AuthorizationCustomException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "upload";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response, @RequestParam String imageUrl) throws Exception {
        try {
//
            String destinationPath = "C:/temp/downloaded_image.jpg";

            fileHandler.downloadImage(imageUrl, destinationPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
