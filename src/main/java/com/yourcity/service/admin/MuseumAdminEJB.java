package com.yourcity.service.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.service.DatabaseProvider;
import com.yourcity.service.ImageProvider;
import com.yourcity.service.model.Museum;
import com.yourcity.service.model.MuseumImage;
import com.yourcity.service.util.ConversionFromJsonException;
import com.yourcity.service.util.JsonUtil;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Andrey on 05.04.2015.
 */
@Stateless
public class MuseumAdminEJB {

    @EJB
    private DatabaseProvider databaseProvider;

    public boolean addNewMuseum(Integer cityId, String name, String description, String email, String about,
                                String phone, String address, String imageBase64) throws Exception {
        databaseProvider.openConnection();
        boolean result;
        Museum museum = new Museum();
        museum.setCityId(cityId);
        museum.setName(name);
        museum.setDescription(description);
        museum.setEmail(email);
        museum.setAbout(about);
        museum.setPhone(phone);
        museum.setAddress(address);
        if (isValidString(imageBase64)) {
            String imageName = ImageProvider.saveMuseumAvatarBase64AndGetName(imageBase64);
            if (imageName != null) {
                museum.setImage(imageName);
            }
        }
        result = museum.saveIt();
        databaseProvider.closeConnection();
        return result;
    }

    public boolean delete(Integer id) {
        databaseProvider.openConnection();
        List<Museum> museums = Museum.where("id = ?", id);
        if (museums.isEmpty()) {
            return true;
        }
        Museum museum = museums.get(0);
        String avatar = museum.getImage();
        boolean result = museum.delete();
        ImageProvider.deleteMuseumAvatar(avatar);

        List<MuseumImage> museumImages = MuseumImage.where("museum_id = ?", id);
        for (MuseumImage museumImage : museumImages) {
            ImageProvider.deleteMuseumImage(museumImage.getSrc());
        }
        ;
        MuseumImage.delete("museum_id = ?", id);
        databaseProvider.closeConnection();
        return result;
    }

    public boolean update(JsonObject jsonMuseum, Integer id) throws ConversionFromJsonException {
        databaseProvider.openConnection();
        List<Museum> museums = Museum.where("id = ?", id);
        if (museums.isEmpty()) {
            return false;
        }
        Museum museum = museums.get(0);
        JsonUtil.jsonToMuseum(jsonMuseum, museum);
        boolean result = museum.saveIt();
        databaseProvider.closeConnection();
        return result;
    }

    public int deleteMuseumImages(Integer id) {
        databaseProvider.openConnection();
        int count = MuseumImage.delete("id = ?", id);
        databaseProvider.closeConnection();
        return count;
    }

    public JsonArray updateOrCreateMuseumImage(JsonObject jsonMuseumImage, Integer id) throws ConversionFromJsonException {
        databaseProvider.openConnection();
        MuseumImage museumImage;
        if (id == null || id < 0) {
            museumImage = new MuseumImage();
        } else if (isValidId(id)) {
            List<MuseumImage> museumImages = MuseumImage.where("id = ?", id);
            if (museumImages.isEmpty()) {
                return null;
            }
            museumImage = museumImages.get(0);
        } else {
            return null;
        }
        JsonUtil.jsonToMuseumImage(jsonMuseumImage, museumImage);
        if (museumImage.saveIt()) {
            JsonArray array = new JsonArray();
            array.add(JsonUtil.museumImageToJson(museumImage));
            databaseProvider.closeConnection();
            return array;
        } else {
            return null;
        }
    }

    public JsonArray getMuseumImages(Integer museumId) {
        databaseProvider.openConnection();
        List<MuseumImage> museumImages = MuseumImage.where("museum_id = ?", museumId);
        if (museumImages.isEmpty()) {
            return null;
        }
        JsonArray array = new JsonArray();
        for (MuseumImage museumImage : museumImages) {
            array.add(JsonUtil.museumImageToJson(museumImage));
        }
        databaseProvider.closeConnection();
        return array;
    }

    public JsonArray findById(Integer id) {
        databaseProvider.openConnection();
        Museum museum = Museum.findById(id);
        if (museum == null) {
            return null;
        }
        JsonArray array = new JsonArray();
        array.add(JsonUtil.museumToJson(museum));
        databaseProvider.closeConnection();
        return array;
    }

    public JsonArray findAll() {
        databaseProvider.openConnection();
        List<Museum> museums = Museum.findBySQL("SELECT * FROM MUSEUMS;");
        if (museums.isEmpty()) {
            return null;
        }
        databaseProvider.closeConnection();
        return convertMuseumListToJsonArray(museums);
    }

    public JsonArray findByNameAndCityId(String name, Integer cityId) {
        databaseProvider.openConnection();
        List<Museum> museums = Museum.where(format("name = '%s' and city_id = '%s'", name, cityId));
        if (museums.isEmpty()) {
            return null;
        }
        databaseProvider.closeConnection();
        return convertMuseumListToJsonArray(museums);
    }

    public JsonArray findByName(String name) {
        databaseProvider.openConnection();
        List<Museum> museums = Museum.where("name = ?", name);
        if (museums.isEmpty()) {
            return null;
        }
        databaseProvider.closeConnection();
        return convertMuseumListToJsonArray(museums);
    }

    public JsonArray findByCityId(Integer cityId) {
        databaseProvider.openConnection();
        List<Museum> museums = Museum.where("city_id = ?", cityId);
        if (museums.isEmpty()) {
            return null;
        }
        databaseProvider.closeConnection();
        return convertMuseumListToJsonArray(museums);
    }

    private JsonArray convertMuseumListToJsonArray(List<Museum> museums) {
        JsonArray array = new JsonArray();
        for (Museum museum : museums) {
            array.add(JsonUtil.museumToJson(museum));
        }
        return array;
    }

    private boolean isValidString(String name) {
        return name != null && !name.isEmpty();
    }

    private boolean isValidId(Integer id) {
        return id != null && id > 0;
    }
}
