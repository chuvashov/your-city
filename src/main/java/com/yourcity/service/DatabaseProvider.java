package com.yourcity.service;

import com.yourcity.model.City;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Andrey on 08.02.2015.
 */
@Provider
public class DatabaseProvider implements ContainerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseProvider.class);

    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String DB_URL;
    private static String DB_DRIVER;

    static {
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

            LOGGER.info("Read properties from file. Starting database with url=" + DB_URL);
        } catch (Exception e) {
            LOGGER.error("Failed to start database", e);
        }
        openConnection();
        LOGGER.info("Database connection is opened.");
    }

    private static void openConnection() {
        if (!Base.hasConnection()) {
            Base.open(DB_DRIVER, DB_URL, DB_USER, DB_PASSWORD);
        }
    }

    private static ArrayList<City> cities = null;

    public static ArrayList<City> getCities() {
        if (cities == null || cities.isEmpty()) {
            refreshCities();
        }
        return cities;
    }

    public static void refreshCities() {
        cities = new ArrayList<>();
        LazyList<City> list = City.findAll();
        for (City city : list) {
            cities.add(city);
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        openConnection();
    }

    public static void closeConnection() {
        Base.close();
    }
}
