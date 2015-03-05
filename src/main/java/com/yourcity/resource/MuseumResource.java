package com.yourcity.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.model.City;
import com.yourcity.model.Museum;
import com.yourcity.model.MuseumImage;
import com.yourcity.service.DatabaseProvider;
import com.yourcity.service.ImageProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    private static final String MUSEUM_AVATAR_DIR = "your-city-images/museum/avatars/";
    private static final String MUSEUM_IMAGES_DIR = "your-city-images/museum/images/";

    private static final String WEBAPP_PREFIX = "src/main/webapp/";
    private static final String BASE64_PREFIX = "data:image/png;base64,";

    @GET
    @Path("/info/byId")
    public Response getMuseumById(@QueryParam("id") int museumId) {
        if (museumId < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Museum> museums = Museum.where("id = ?", museumId);
        if (museums.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Museum museum = museums.get(0);
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", museum.getMuseumId());
        jsonObj.addProperty("name", museum.getName());
        jsonObj.addProperty("email", museum.getEmail());
        jsonObj.addProperty("phone", museum.getPhone());
        jsonObj.addProperty("address", museum.getAddress());
        jsonObj.addProperty("about", museum.getAbout());
        return Response.ok(jsonObj.toString()).build();
    }

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
        if (!ImageProvider.isMuseumAvatarImage(img)) {
            return BASE64_PREFIX + ImageProvider.getBase64Image(WEBAPP_PREFIX + DEFAULT_MUSEUM_AVATAR);
        }
        return BASE64_PREFIX + ImageProvider.getBase64Image(MUSEUM_AVATAR_DIR + img);
    }

    @GET
    @Path("/view")
    public Response getMuseum(@QueryParam("id") int museumId) {
        if (museumId < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Museum> museums = Museum.where("id = ?", museumId);
        if (museums.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Museum museum = museums.get(0);
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", museum.getMuseumId());
        jsonObj.addProperty("name", museum.getName());
        jsonObj.addProperty("email", museum.getEmail());
        jsonObj.addProperty("phone", museum.getPhone());
        jsonObj.addProperty("address", museum.getAddress());
        jsonObj.addProperty("about", museum.getAbout());
        jsonObj.addProperty("image", getActiveAvatarUrl(museum.getImage()));
        return Response.ok(jsonObj.toString()).build();
    }

    @GET
    @Path("/images")
    public Response getImages(@QueryParam("id") int museumId) {
        if (museumId < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<MuseumImage> images = MuseumImage.where("museum_id = ?", museumId);
        if (images.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
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
            jsonObj.addProperty("src", BASE64_PREFIX + ImageProvider.getBase64Image(MUSEUM_IMAGES_DIR + src));
            array.add(jsonObj);
        }
        return Response.ok(array.toString()).build();
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
