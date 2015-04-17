package com.yourcity.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.service.model.City;
import com.yourcity.service.util.CityUtil;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;

/**
 * Created by Andrey on 04.04.2015.
 */
@Stateless
public class CityEJB {

    @EJB
    private DatabaseProvider databaseProvider;

    public JsonArray getAllCities() {
        databaseProvider.openConnection();
        JsonArray jsonArray = new JsonArray();
        ArrayList<City> cities = CityUtil.getCities();
        JsonObject jsonObj;
        for (City city : cities) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("city", city.getCityName());
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }
}
