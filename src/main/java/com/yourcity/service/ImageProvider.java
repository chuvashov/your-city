package com.yourcity.service;

import com.yourcity.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Andrey on 01.03.2015.
 */
public class ImageProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProvider.class);

    private static String IMAGE_DIR_PATH;
    private static final String ROOT_DIR = "your-city-images";
    private static final String WEBAPP_PREFIX = "src/main/webapp/";
    private static final String BASE64_PREFIX = "data:image/png;base64,";

    //paths
    private static final String MUSEUM_AVATAR_DIR = "/museum/avatars/";
    private static final String MUSEUM_IMAGES_DIR = "/museum/images/";
    private static List<String> paths = Arrays.asList(MUSEUM_AVATAR_DIR, MUSEUM_IMAGES_DIR);

    //defaults
    private static final String DEFAULT_MUSEUM_AVATAR = "application/images/default_museum_avatar.png";


    static {
        Properties properties = new Properties();

        try (InputStream stream = DatabaseProvider.class.getResourceAsStream("/application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        try {
            IMAGE_DIR_PATH = properties.getProperty("imageDirPath");
            LOGGER.info("ImageProvider read property from file.");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        for (String path : paths) {
            File file = new File(IMAGE_DIR_PATH + path);
            if (!file.exists() && !file.isDirectory()) {
                if (file.mkdirs()) {
                    LOGGER.info("Directory " + path + " is created.");
                } else {
                    LOGGER.info("Dictionary " + path + " is not created!!!");
                }
            }
        }
        LOGGER.info("ImageProvider is opened.");
    }

    public static String getMuseumAvatarBase64Url(String img) {
        if (isMuseumAvatarImage(img)) {
            return BASE64_PREFIX + ImageUtil.getBase64Image(ROOT_DIR + MUSEUM_AVATAR_DIR + img);
        }
        return BASE64_PREFIX + ImageUtil.getBase64Image(WEBAPP_PREFIX + DEFAULT_MUSEUM_AVATAR);
    }

    public static String getMuseumImageBase64Url(String img) {
        String url = "";
        if (isMuseumImage(img)) {
            url = BASE64_PREFIX + ImageUtil.getBase64Image(ROOT_DIR + MUSEUM_IMAGES_DIR + img);
        }
        return url;
    }

    public static boolean isMuseumAvatarImage(String imgName) {
        File imgFile = new File(IMAGE_DIR_PATH + MUSEUM_AVATAR_DIR + imgName);
        return imgFile.exists() && imgFile.isFile();
    }

    public static boolean isMuseumImage(String imgName) {
        File imgFile = new File(IMAGE_DIR_PATH + MUSEUM_IMAGES_DIR + imgName);
        return imgFile.exists() && imgFile.isFile();
    }

    public static String saveAvatarBase64ImageAndGetName(String base64Image) {
        return ImageUtil.saveBase64AvatarImageAndGetName(base64Image, ROOT_DIR + MUSEUM_AVATAR_DIR);
    }
}
