package com.yourcity.adminresource;

import com.google.gson.JsonArray;
import com.yourcity.model.City;
import com.yourcity.util.CityUtil;
import com.yourcity.util.JsonUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Andrey on 08.03.2015.
 */
@Path("/rest/admin/city")
@Produces("application/json")
public class AdminCityResource {

    @GET
    @Path("/all")
    public Response getAllCities() {
        JsonArray array = new JsonArray();
        for (City city : CityUtil.getCities()) {
            array.add(JsonUtil.cityToJson(city));
        }
        return Response.ok(array.toString()).build();
    }

    @POST
    @Path("/add")
    public Response addCity(@QueryParam("name") String name) {
        if (!isValidCityName(name)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        City city = new City();
        city.setCityName(name);
        city.saveIt();
        CityUtil.refreshCities();
        return Response.status(Response.Status.CREATED).build();
    }

    private boolean isValidCityName(String name) {
        return name != null && !name.isEmpty();
    }
}
