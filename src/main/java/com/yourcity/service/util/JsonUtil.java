package com.yourcity.service.util;

import com.google.gson.JsonObject;
import com.yourcity.service.model.*;
import com.yourcity.service.ImageProvider;

import java.sql.Date;

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
        jsonObj.addProperty("museumId", imageObject.getMuseumId());
        return jsonObj;
    }

    public static JsonObject eventToJson(Event eventObject) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", eventObject.getEventId());
        jsonObj.addProperty("cityId", eventObject.getCityId());
        jsonObj.addProperty("name", eventObject.getName());
        jsonObj.addProperty("description", eventObject.getDescription());
        jsonObj.addProperty("image", eventObject.getImage());
        jsonObj.addProperty("about", eventObject.getAbout());
        jsonObj.addProperty("eventType", eventObject.getEventType().toString());
        jsonObj.addProperty("startTime", eventObject.getStartTime());
        jsonObj.addProperty("finishTime", eventObject.getFinishTime());
        return jsonObj;
    }

    public static void jsonToMuseum(JsonObject museumJson, Museum museum) throws ConversionFromJsonException {
        Integer id;
        try {
            id = museumJson.get("id").getAsInt();
        } catch (Exception e) {
            throw new ConversionFromJsonException();
        }
        if (museum.getMuseumId() != null && !museum.getMuseumId().equals(id)) {
            throw new ConversionFromJsonException("ID of Museum object not equals ID from json.");
        }
        try {
            Integer cityId = getIntegerFromJson(museumJson, "cityId");
            if (isValidId(cityId) && CityUtil.existCityId(cityId)) {
                museum.setCityId(cityId);
            }

            String name = getStringFromJson(museumJson, "name");
            if (isValidString(name)) {
                museum.setName(name);
            }

            String description = getStringFromJson(museumJson, "description");
            if (isValidString(description)) {
                museum.setDescription(description);
            }

            String email = getStringFromJson(museumJson, "email");
            if (isValidString(email)) {
                museum.setEmail(email);
            }

            String about = getStringFromJson(museumJson, "about");
            if (isValidString(about)) {
                museum.setAbout(about);
            }

            String phone = getStringFromJson(museumJson, "phone");
            if (isValidString(phone)) {
                museum.setPhone(phone);
            }

            String address = museumJson.get("address").getAsString();
            if (isValidString(address)) {
                museum.setAddress(address);
            }

            String image = getStringFromJson(museumJson, "image");
            if (isValidString(image)) {
                String imageName = ImageProvider.saveMuseumAvatarBase64AndGetName(image);
                if (imageName != null) {
                    museum.setImage(imageName);
                }
            }
        } catch (Exception e) {
            throw new ConversionFromJsonException();
        }
    }

    public static void jsonToMuseumImage(JsonObject museumImageJson, MuseumImage museumImage)
            throws ConversionFromJsonException{
        try {
            String description = getStringFromJson(museumImageJson, "description");
            if (isValidString(description)) {
                museumImage.setDescription(description);
            }

            Integer museumId = getIntegerFromJson(museumImageJson, "museumId");
            if (isValidId(museumId)) {
                museumImage.setMuseumId(museumId);
            }

            String image = getStringFromJson(museumImageJson, "src");
            if (isValidString(image)) {
                String imageName = ImageProvider.saveMuseumImageBase64AndGetName(image);
                if (imageName != null) {
                    museumImage.setSrc(imageName);
                }
            }
        } catch (Exception e) {
            throw new ConversionFromJsonException();
        }
    }

    public static void jsonToUser(JsonObject userJson, User user)
            throws ConversionFromJsonException{
        try {
            String name = getStringFromJson(userJson, "name");
            if (isValidString(name)) {
                user.setName(name);
            }

            String login = getStringFromJson(userJson, "login");
            if (isValidString(login)) {
                user.setLogin(login);
            }

            String password = getStringFromJson(userJson, "password");
            if (isValidString(password)) {
                user.setPassword(password);
            }

            String email = getStringFromJson(userJson, "email");
            if (isValidString(email)) {
                user.setEmail(email);
            }
        } catch (Exception e) {
            throw new ConversionFromJsonException();
        }
    }

    public static void jsonToEvent(JsonObject eventJson, Event event)
            throws ConversionFromJsonException{
        try {
            String name = getStringFromJson(eventJson, "name");
            if (isValidString(name)) {
                event.setName(name);
            }

            String description = getStringFromJson(eventJson, "description");
            if (isValidString(description)) {
                event.setDescription(description);
            }

            String about = getStringFromJson(eventJson, "about");
            if (isValidString(about)) {
                event.setAbout(about);
            }

            String image = getStringFromJson(eventJson, "image");
            if (isValidString(image)) {
                String imageName = ImageProvider.saveEventImageBase64AndGetName(image);
                if (imageName != null) {
                    event.setImage(imageName);
                }
            }

            Integer cityId = getIntegerFromJson(eventJson, "cityId");
            if (isValidId(cityId)) {
                event.setCityId(cityId);
            }

            String startTime = getStringFromJson(eventJson, "startTime");
            if (isValidString(startTime)) {
                event.setStartTime(startTime);
            }

            String finishTime = getStringFromJson(eventJson, "finishTime");
            if (isValidString(finishTime)) {
                event.setStartTime(finishTime);
            }
        } catch (Exception e) {
            throw new ConversionFromJsonException();
        }
    }

    private static boolean isValidString(String name) {
        return name != null && !name.isEmpty();
    }

    private static boolean isValidId(Integer id) {
        return id != null && id > 0;
    }

    private static Integer getIntegerFromJson(JsonObject jsonObj, String propertyName) {
        Integer result = null;
        if (!jsonObj.get(propertyName).isJsonNull()) {
            result = jsonObj.get(propertyName).getAsInt();
        };
        return result;
    }

    private static String getStringFromJson(JsonObject jsonObj, String propertyName) {
        String result = null;
        if (!jsonObj.get(propertyName).isJsonNull()) {
            result = jsonObj.get(propertyName).getAsString();
        };
        return result;
    }
}
