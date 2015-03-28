package com.yourcity.util;

import com.google.gson.JsonObject;
import com.yourcity.model.City;
import com.yourcity.model.Museum;
import com.yourcity.model.MuseumImage;
import com.yourcity.service.ImageProvider;

import javax.ws.rs.core.Response;
import java.util.Objects;

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
        jsonObj.addProperty("image", ImageProvider.getMuseumAvatarUrl(museum.getImage()));
        return jsonObj;
    }

    public static JsonObject cityToJson(City city) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", city.getCityName());
        jsonObj.addProperty("id", city.getCityId());
        return jsonObj;
    }

    public static JsonObject museumImageToJson(MuseumImage imageObject) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", imageObject.getMuseumImageId());
        jsonObj.addProperty("description", imageObject.getDescription());
        jsonObj.addProperty("src", ImageProvider.getMuseumImageUrl(imageObject.getSrc()));
        jsonObj.addProperty("museumId", imageObject.getMuseumImageId());
        return jsonObj;
    }

    public static void jsonToMuseum(JsonObject museumJson, Museum museum) throws MuseumConversionFromJsonException{
        Integer id;
        try {
            id = museumJson.get("id").getAsInt();
        } catch (Exception e) {
            throw new MuseumConversionFromJsonException();
        }
        if (museum.getMuseumId() != null && !museum.getMuseumId().equals(id)) {
            throw new MuseumConversionFromJsonException("ID of Museum object not equals ID from json.");
        }
        try {
            int cityId = museumJson.get("cityId").getAsInt();
            if (cityId > 0 && CityUtil.existCityId(cityId)) {
                museum.setCityId(cityId);
            }

            String name = museumJson.get("name").getAsString();
            if (isValidString(name)) {
                museum.setName(name);
            }

            String description = museumJson.get("description").getAsString();
            if (isValidString(description)) {
                museum.setDescription(description);
            }

            String email = museumJson.get("email").getAsString();
            if (isValidString(email)) {
                museum.setEmail(email);
            }

            String about = museumJson.get("about").getAsString();
            if (isValidString(about)) {
                museum.setAbout(about);
            }

            String phone = museumJson.get("phone").getAsString();
            if (isValidString(phone)) {
                museum.setPhone(phone);
            }

            String address = museumJson.get("address").getAsString();
            if (isValidString(address)) {
                museum.setAddress(address);
            }

            String image = museumJson.get("image").getAsString();
            if (isValidString(image)) {
                String imageName = ImageProvider.saveMuseumAvatarBase64AndGetName(image);
                if (imageName != null) {
                    museum.setImage(imageName);
                }
            }
        } catch (Exception e) {
            throw new MuseumConversionFromJsonException();
        }
    }

    private static boolean isValidString(String name) {
        return name != null && !name.isEmpty();
    }

    private static boolean isValidId(Integer id) {
        return id != null && id > 0;
    }
}
