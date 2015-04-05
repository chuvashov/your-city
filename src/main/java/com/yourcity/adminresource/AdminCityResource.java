package com.yourcity.adminresource;

import com.google.gson.JsonArray;
import com.yourcity.service.admin.CityAdminEJB;
import com.yourcity.service.model.City;
import com.yourcity.service.util.CityUtil;
import com.yourcity.service.util.JsonUtil;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Andrey on 08.03.2015.
 */
@Path("rest/admin/city")
@Produces("application/json")
public class AdminCityResource {

    @EJB
    private CityAdminEJB cityEJB;

    @GET
    @Path("all")
    public Response getAllCities() {
        JsonArray array = cityEJB.getAllCities();
        return Response.ok(array.toString()).build();
    }

    @POST
    @Path("add")
    public Response addCity(@QueryParam("name") String name) {
        if (!isValidCityName(name)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (cityEJB.addCity(name)) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }

    private boolean isValidCityName(String name) {
        return name != null && !name.isEmpty();
    }
}
