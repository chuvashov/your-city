package com.yourcity.service;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Andrey on 08.02.2015.
 */
@Singleton
@Startup
public class DatabaseProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseProvider.class);

    private String DB_USER;
    private String DB_PASSWORD;
    private String DB_URL;
    private String DB_DRIVER;

    @PostConstruct
    public void loadInitProp() {
        Properties properties = new Properties();

        try (InputStream stream = DatabaseProvider.class.getResourceAsStream("/application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        try {
            DB_DRIVER = properties.getProperty("dbDriver");
            DB_URL = properties.getProperty("dbUrl");
            DB_USER = properties.getProperty("dbUser");
            DB_PASSWORD = properties.getProperty("dbPassword");

            LOGGER.info("Read properties from file. Starting database with url = " + DB_URL);
        } catch (Exception e) {
            LOGGER.error("Failed to start database. " + e.getMessage(), e);
        }
        openConnection();
        LOGGER.info("Database connection is opened.");
    }

    public void openConnection() {
        if (!Base.hasConnection()) {
            Base.open(DB_DRIVER, DB_URL, DB_USER, DB_PASSWORD);
        }
    }

    public void closeConnection() {
        Base.close();
    }
}
