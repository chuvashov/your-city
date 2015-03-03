package com.yourcity.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.model.City;
import com.yourcity.model.Museum;
import com.yourcity.service.DatabaseProvider;
import com.yourcity.service.ImageProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 22.02.2015.
 */
@Path("/museum")
@Produces("application/json")
public class MuseumResource {

    private static final String DEFAULT_MUSEUM_AVATAR = "application/images/default_museum_avatar.png";
    private static final String MUSEUM_AVATAR_DIR = "your-city-images/museum/avatars";
    private static final String MUSEUM_IMAGES_DIR = "your-city-images/museum/images";

    @GET
    @Path("/all")
    public Response getMuseumsByIndexes(@QueryParam("city") String city) {
        ArrayList<City> cities = DatabaseProvider.getCities();
        int cityIndex = indexOfCity(cities, city);
        if (cityIndex == -1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Integer cityId = cities.get(cityIndex).getCityId();
        List<Museum> museums = Museum.where("city_id = ?", cityId);
        JsonArray array = new JsonArray();
        JsonObject jsonObj;
        for (Museum museum : museums) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("name", museum.getName());
            jsonObj.addProperty("description", museum.getDescription());
            jsonObj.addProperty("image", getActiveAvatarUrl(museum.getImage()));
            jsonObj.addProperty("id", museum.getMuseumId());
            array.add(jsonObj);
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("/count")
    public Response getCount(@QueryParam("city") String city) {
        ArrayList<City> cities = DatabaseProvider.getCities();
        int cityIndex = indexOfCity(cities, city);
        if (cityIndex == -1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Integer cityId = cities.get(cityIndex).getCityId();
        Long count = Museum.count("city_id = " + cityId);
        return Response.ok(count.toString()).build();
    }

    private String getActiveAvatarUrl(String img) {
        String result = MUSEUM_AVATAR_DIR + img;
        if (!ImageProvider.isMuseumAvatarImage(img)) {
            result = DEFAULT_MUSEUM_AVATAR;
        }
        return result;
    }

    private int indexOfCity(List<City> cities, String cityName) {
        if (!cities.isEmpty()) {
            City city;
            for (int i = 0; i < cities.size(); i++) {
                city = cities.get(i);
                if (city.getCityName().equals(cityName)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
