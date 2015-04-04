package com.yourcity.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.service.CityEJB;
import com.yourcity.service.model.City;
import com.yourcity.service.util.CityUtil;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Andrey on 22.02.2015.
 */
@Path("cities")
public class CityResource {

    @EJB
    private CityEJB cityEJB;

    @GET
    @Path("all")
    @Produces("application/json")
    public Response getCitiesAsJson() {
        JsonArray jsonArray = cityEJB.getAllCities();
        return Response.ok(jsonArray.toString()).build();
    }
}
