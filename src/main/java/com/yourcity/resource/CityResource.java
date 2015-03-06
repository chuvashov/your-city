package com.yourcity.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.model.City;
import com.yourcity.util.CityUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Andrey on 22.02.2015.
 */
@Path("/cities")
public class CityResource {

    @GET
    @Path("/all")
    @Produces("application/json")
    public Response getCitiesAsJson() {
        JsonArray jsonArray = new JsonArray();
        ArrayList<City> cities = CityUtil.getCities();
        JsonObject jsonObj;
        for (City city : cities) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("city", city.getCityName());
            jsonArray.add(jsonObj);
        }
        return Response.ok(jsonArray.toString()).build();
    }
}
