package com.yourcity.service;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by Andrey on 01.03.2015.
 */
public class ImageProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProvider.class);

    //paths
    private static String IMAGE_REPOSITORY_DIR;
    private static final String MUSEUM_AVATAR_DIR = "/museum/avatars/";
    private static final String MUSEUM_IMAGES_DIR = "/museum/images/";
    private static final String EVENT_IMAGES_DIR = "/event/images/";

    //defaults
    private static final String IMAGE_URL_PREFIX = "/your-city/images-repository?path=";
    private static final String DEFAULT_MUSEUM_AVATAR = "/your-city/application/images/default_museum_avatar.png";
    private static final String DEFAULT_EVENT_IMAGE = "/your-city/application/images/default_event_image.png";

    static {
        Properties properties = new Properties();

        try (InputStream stream = ImageProvider.class.getResourceAsStream("/application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        try {
            IMAGE_REPOSITORY_DIR = properties.getProperty("imageRepository");

            File dir = new File(IMAGE_REPOSITORY_DIR + MUSEUM_AVATAR_DIR);
            dir.mkdirs();
            dir = new File(IMAGE_REPOSITORY_DIR + MUSEUM_IMAGES_DIR);
            dir.mkdirs();
            dir = new File(IMAGE_REPOSITORY_DIR + EVENT_IMAGES_DIR);
            dir.mkdirs();

            LOGGER.info("Read image repository location from property file. ImageRepositoryLocation = \""
                    + IMAGE_REPOSITORY_DIR + "\"");
        } catch (Exception e) {
            LOGGER.error("Failed to read image repository location.", e);
        }
    }

    public static String getMuseumAvatarUrl(String img) {
        return isMuseumAvatarImage(img)
                ? IMAGE_URL_PREFIX + MUSEUM_AVATAR_DIR + img
                : DEFAULT_MUSEUM_AVATAR;
    }

    public static String getMuseumImageUrl(String img) {
        String url = "";
        if (isMuseumImage(img)) {
            url = IMAGE_URL_PREFIX + MUSEUM_IMAGES_DIR + img;
        }
        return url;
    }

    public static String getEventImageUrl(String img) {
        return isEventImage(img)
                ? IMAGE_URL_PREFIX + EVENT_IMAGES_DIR + img
                : DEFAULT_EVENT_IMAGE;
    }

    public static boolean isMuseumAvatarImage(String imgName) {
        if (imgName == null) {
            return false;
        }
        File imgFile = new File(IMAGE_REPOSITORY_DIR + MUSEUM_AVATAR_DIR + imgName);
        return imgFile.exists() && imgFile.isFile();
    }

    public static boolean isMuseumImage(String imgName) {
        if (imgName == null) {
            return false;
        }
        File imgFile = new File(IMAGE_REPOSITORY_DIR + MUSEUM_IMAGES_DIR + imgName);
        return imgFile.exists() && imgFile.isFile();
    }

    public static boolean isEventImage(String imgName) {
        if (imgName == null) {
            return false;
        }
        File imgFile = new File(IMAGE_REPOSITORY_DIR + EVENT_IMAGES_DIR + imgName);
        return imgFile.exists() && imgFile.isFile();
    }

    public static String saveMuseumAvatarBase64AndGetName(String base64Image) {
        return saveImageAndReturnName(base64Image, IMAGE_REPOSITORY_DIR + MUSEUM_AVATAR_DIR);
    }

    public static String saveMuseumImageBase64AndGetName(String base64Image) {
        return saveImageAndReturnName(base64Image, IMAGE_REPOSITORY_DIR + MUSEUM_IMAGES_DIR);
    }

    public static String saveEventImageBase64AndGetName(String base64Image) {
        return saveImageAndReturnName(base64Image, IMAGE_REPOSITORY_DIR + EVENT_IMAGES_DIR);
    }

    public static boolean deleteMuseumAvatar(String img) {
        return img != null && deleteImage(IMAGE_REPOSITORY_DIR + MUSEUM_AVATAR_DIR + img);
    }

    public static boolean deleteMuseumImage(String img) {
        return img != null && deleteImage(IMAGE_REPOSITORY_DIR + MUSEUM_IMAGES_DIR + img);
    }

    public static boolean deleteEventImage(String img) {
        return img != null && deleteImage(IMAGE_REPOSITORY_DIR + EVENT_IMAGES_DIR + img);
    }

    public static byte[] getImageByPath(String path) throws IOException {
        ByteArrayOutputStream out = null;
        InputStream input = null;
        boolean wasException = false;
        try{
            out = new ByteArrayOutputStream();
            input = new BufferedInputStream(new FileInputStream(IMAGE_REPOSITORY_DIR + path));
            int data;
            while ((data = input.read()) != -1){
                out.write(data);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Image not found: ");
            wasException = true;
        } catch (IOException e) {
            LOGGER.error("Failed to load image: ", e);
            wasException = true;
        } finally {
            if (null != input){
                input.close();
            }
            if (null != out){
                out.close();
            }
        }
        if (wasException) {
            return null;
        }
        return out.toByteArray();
    }

    private static boolean deleteImage(String path) {
        File img = new File(path);
        return img.exists() && img.isFile() && img.delete();
    }

    private static String saveImageAndReturnName(String base64Image, String dir) {
        String format;
        String image;
        if (base64Image.startsWith("data:image/jpeg;base64,")) {
            format = ".jpg";
            image = base64Image.substring(23);
        } else if (base64Image.startsWith("data:image/png;base64,")) {
            format = ".png";
            image = base64Image.substring(22);
        } else {
            return null;
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
