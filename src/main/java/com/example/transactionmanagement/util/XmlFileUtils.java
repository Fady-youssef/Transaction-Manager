package com.example.transactionmanagement.util;

import org.springframework.web.multipart.MultipartFile;


public class XmlFileUtils {

    public static boolean isXmlFile(MultipartFile file) {
        return isXmlByContentType(file) || isXmlByFileExtension(file);
    }

    private static boolean isXmlByContentType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("text/xml") || contentType.equals("application/xml"));
    }

    private static boolean isXmlByFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null && fileName.toLowerCase().endsWith(".xml");
    }
}
