package com.yourcity.service;

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
    private static final String MUSEUM_AVATAR_DIR = "/museum/avatars";
    private static final String MUSEUM_IMAGES_DIR = "/museum/images";

    private static List<String> paths = Arrays.asList(MUSEUM_AVATAR_DIR, MUSEUM_IMAGES_DIR);

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

        for (String path: paths) {
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

    public static boolean isMuseumAvatarImage(String img) {
        File imgFile = new File(IMAGE_DIR_PATH + MUSEUM_AVATAR_DIR + img);
        return imgFile.exists();
    }
}
