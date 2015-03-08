package com.yourcity.util;

import com.google.gson.JsonObject;
import com.yourcity.model.City;
import com.yourcity.model.Museum;

/**
 * Created by Andrey on 08.03.2015.
 */
public class JsonUtil {

    public static JsonObject museumToJson(Museum museum) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", museum.getMuseumId());
        jsonObj.addProperty("name", museum.getName());
        jsonObj.addProperty("about", museum.getAbout());
        jsonObj.addProperty("address", museum.getAddress());
        jsonObj.addProperty("description", museum.getDescription());
        jsonObj.addProperty("phone", museum.getPhone());
        jsonObj.addProperty("email", museum.getEmail());
        jsonObj.addProperty("cityId", museum.getCityId());
        jsonObj.addProperty("image", museum.getImage());
        return jsonObj;
    }

    public static JsonObject cityToJson(City city) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", city.getCityName());
        jsonObj.addProperty("id", city.getCityId());
        return jsonObj;
    }
}
