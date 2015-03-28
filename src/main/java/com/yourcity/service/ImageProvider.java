package com.yourcity.service;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.UUID;

/**
 * Created by Andrey on 01.03.2015.
 */
public class ImageProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProvider.class);

    //paths
    private static final String WEBAPP_DIR = "src/main/webapp";
    private static final String IMAGES_DIR = "/your-city-images";
    private static final String MUSEUM_AVATAR_DIR = "/museum/avatars/";
    private static final String MUSEUM_IMAGES_DIR = "/museum/images/";

    //defaults
    private static final String DEFAULT_MUSEUM_AVATAR = "/application/images/default_museum_avatar.png";

    public static String getMuseumAvatarUrl(String img) {
        return isMuseumAvatarImage(img)
                ? IMAGES_DIR + MUSEUM_AVATAR_DIR + img
                : DEFAULT_MUSEUM_AVATAR;
    }

    public static String getMuseumImageUrl(String img) {
        String url = "";
        if (isMuseumImage(img)) {
            url = IMAGES_DIR + MUSEUM_IMAGES_DIR + img;
        }
        return url;
    }

    public static boolean isMuseumAvatarImage(String imgName) {
        if (imgName == null) {
            return false;
        }
        File imgFile = new File(WEBAPP_DIR + IMAGES_DIR + MUSEUM_AVATAR_DIR + imgName);
        return imgFile.exists() && imgFile.isFile();
    }

    public static boolean isMuseumImage(String imgName) {
        if (imgName == null) {
            return false;
        }
        File imgFile = new File(WEBAPP_DIR + IMAGES_DIR + MUSEUM_IMAGES_DIR + imgName);
        return imgFile.exists() && imgFile.isFile();
    }

    public static String saveMuseumAvatarBase64AndGetName(String base64Image) {
        return saveImageAndReturnName(base64Image, WEBAPP_DIR + IMAGES_DIR + MUSEUM_AVATAR_DIR);
    }

    public static boolean deleteMuseumAvatar(String img) {
        return img != null && deleteImage(WEBAPP_DIR + IMAGES_DIR + MUSEUM_AVATAR_DIR + img);
    }

    public static boolean deleteMuseumImage(String img) {
        return img != null && deleteImage(WEBAPP_DIR + IMAGES_DIR + MUSEUM_IMAGES_DIR + img);
    }

    private static boolean deleteImage(String path) {
        File img = new File(path);
        return img.exists() && img.isFile() && img.delete();
    }

    private static String saveImageAndReturnName(String base64Image, String dir) {
        if (!base64Image.startsWith("data:image/")) {
            return null;
        }
        String format;
        String image;
        if (base64Image.charAt(11) == 'j') {
            format = ".jpg";
            image = base64Image.substring(23);
        } else {
            format = ".png";
            image = base64Image.substring(22);
        }
        byte[] img = Base64.decodeBase64(image);
        String name = UUID.randomUUID().toString() + format;
        try (OutputStream stream = new FileOutputStream(dir + name)) {
            stream.write(img);
        } catch (IOException e) {
            LOGGER.error("Can't save image: ", e);
            name = null;
        }
        return name;
    }
}
