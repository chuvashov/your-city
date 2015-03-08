package com.yourcity.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by Andrey on 06.03.2015.
 */
public class ImageUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class);

    public static String getBase64Image(String imgPath) {
        Path path = Paths.get(imgPath);
        byte[] img = null;
        try {
            img = Files.readAllBytes(path);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return "";
        }
        String result = "";
        try {
            result = new String(Base64.encodeBase64(img), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static String saveBase64AvatarImageAndGetName(String base64Image, String dirPath) {
        byte[] image = Base64.decodeBase64(base64Image);
        String imageName = UUID.randomUUID().toString();
        String path = dirPath;
        if (!dirPath.endsWith("/") && !dirPath.endsWith("\\")) {
            path += "/";
        }
        try (OutputStream stream = new FileOutputStream(path + imageName + ".jpg")) {
            stream.write(image);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            imageName = null;
        }
        return imageName;
    }
}
