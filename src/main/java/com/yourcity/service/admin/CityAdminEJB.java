package com.yourcity.service.admin;

import com.google.gson.JsonArray;
import com.yourcity.service.DatabaseProvider;
import com.yourcity.service.model.City;
import com.yourcity.service.util.CityUtil;
import com.yourcity.service.util.JsonUtil;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Andrey on 05.04.2015.
 */
@Stateless
public class CityAdminEJB {

    @EJB
    private DatabaseProvider databaseProvider;

    public JsonArray getAllCities() {
        databaseProvider.openConnection();
        JsonArray array = new JsonArray();
        for (City city : CityUtil.getCities()) {
            array.add(JsonUtil.cityToJson(city));
        }
        return array;
    }

    public boolean addCity(String name) {
        databaseProvider.openConnection();
        City city = new City();
        city.setCityName(name);
        boolean saved = city.saveIt();
        if (saved) {
            CityUtil.refreshCities();
        }
        return saved;
    }

    public boolean deleteCity(Integer id) {
        databaseProvider.openConnection();
        List<City> cities = City.where("id = ?", id);
        if (cities.isEmpty()) {
            return false;
        }
        if (cities.get(0).delete()) {
            CityUtil.refreshCities();
            return true;
        } else {
            return false;
        }
    }
}
