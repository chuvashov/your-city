package com.yourcity.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.service.model.City;
import com.yourcity.service.model.Museum;
import com.yourcity.service.model.MuseumImage;
import com.yourcity.service.util.CityUtil;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Andrey on 04.04.2015.
 */
@Stateless
public class MuseumEJB {

    @EJB
    private DatabaseProvider databaseProvider;

    public JsonObject getMuseumById(int id) {
        databaseProvider.openConnection();
        List<Museum> museums = Museum.where("id = ?", id);
        if (museums.isEmpty()) {
            return null;
        }
        Museum museum = museums.get(0);
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", museum.getMuseumId());
        jsonObj.addProperty("name", museum.getName());
        jsonObj.addProperty("email", museum.getEmail());
        jsonObj.addProperty("phone", museum.getPhone());
        jsonObj.addProperty("address", museum.getAddress());
        jsonObj.addProperty("about", museum.getAbout());
        return jsonObj;
    }

    public JsonArray getMuseumsFromCity(String cityName) {
        databaseProvider.openConnection();
        City city = CityUtil.getCityByName(cityName);
        if (city == null) {
            return null;
        }
        List<Museum> museums = Museum.where("city_id = ?", city.getCityId());
        JsonArray array = new JsonArray();
        JsonObject jsonObj;
        for (Museum museum : museums) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("name", museum.getName());
            jsonObj.addProperty("description", museum.getDescription());
            jsonObj.addProperty("image", ImageProvider.getMuseumAvatarUrl(museum.getImage()));
            jsonObj.addProperty("id", museum.getMuseumId());
            array.add(jsonObj);
        }
        return array;
    }

    public JsonObject getMuseumForView(int id) {
        databaseProvider.openConnection();
        List<Museum> museums = Museum.where("id = ?", id);
        if (museums.isEmpty()) {
            return null;
        }
        Museum museum = museums.get(0);
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", museum.getMuseumId());
        jsonObj.addProperty("name", museum.getName());
        jsonObj.addProperty("email", museum.getEmail());
        jsonObj.addProperty("phone", museum.getPhone());
        jsonObj.addProperty("address", museum.getAddress());
        jsonObj.addProperty("about", museum.getAbout());
        jsonObj.addProperty("image", ImageProvider.getMuseumAvatarUrl(museum.getImage()));
        return jsonObj;
    }

    public JsonArray getMuseumImages(int id) {
        databaseProvider.openConnection();
        List<MuseumImage> images = MuseumImage.where("museum_id = ?", id);
        if (images.isEmpty()) {
            return null;
        }
        JsonArray array = new JsonArray();
        JsonObject jsonObj;
        for (MuseumImage image : images) {
            String src = image.getSrc();
            if (!ImageProvider.isMuseumImage(src)) {
                continue;
            }
            jsonObj = new JsonObject();
            jsonObj.addProperty("description", image.getDescription());
            jsonObj.addProperty("src", ImageProvider.getMuseumImageUrl(src));
            array.add(jsonObj);
        }
        return array;
    }
}
