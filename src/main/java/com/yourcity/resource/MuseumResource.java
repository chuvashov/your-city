package com.yourcity.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.model.City;
import com.yourcity.model.Museum;
import com.yourcity.service.DatabaseProvider;

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

    @GET
    @Path("/indexes")
    public Response getMuseumsByIndexes(@QueryParam("city") String city,
                                        @QueryParam("from") int from, @QueryParam("to") int to) {
        ArrayList<City> cities = DatabaseProvider.getCities();
        int cityIndex = indexOfCity(cities, city);
        if (cityIndex == -1 || from < 0 || to < from) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Integer cityId = cities.get(cityIndex).getCityId();
        /*List<Museum> museums = Museum.where("city_id = ?", cityId)
                .offset(from)
                .limit(to - from + 1)
                .orderBy("id");*/
        List<Museum> museums = Museum.where("city_id = ?", cityId);
        JsonArray array = new JsonArray();
        JsonObject jsonObj;
        for (Museum museum : museums) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("name", getString(museum.getName()));
            jsonObj.addProperty("address", getString(museum.getAddress()));
            jsonObj.addProperty("description", getString(museum.getDescription()));
            jsonObj.addProperty("phone", getString(museum.getPhone()));
            jsonObj.addProperty("email", getString(museum.getEmail()));
            jsonObj.addProperty("image", getString(museum.getImage()));
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

    private String getString(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj.toString();
    }


}
