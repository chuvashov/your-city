package com.yourcity.service.util;

import com.yourcity.service.model.City;
import org.javalite.activejdbc.LazyList;

import java.util.ArrayList;

/**
 * Created by Andrey on 06.03.2015.
 */
public class CityUtil {

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

    public static City getCityByName(String cityName) {
        if (cities == null) {
            refreshCities();
        }
        if (!cities.isEmpty()) {
            for (City city : cities) {
                if (city.getCityName().equals(cityName)) {
                    return city;
                }
            }
        }
        return null;
    }

    public static boolean existCityId(int id) {
        if (cities == null) {
            refreshCities();
        }
        for (City city : cities) {
            if (city.getCityId() == id) {
                return true;
            }
        }
        return false;
    }
}
