package com.tsqc.util;

import com.tsqc.WebUrlMethods;

public class Config {

    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = WebUrlMethods.base_url+"camupload.php";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Tailor Invoice";
    public static final String QR = "Tailor QR Invoice";
}