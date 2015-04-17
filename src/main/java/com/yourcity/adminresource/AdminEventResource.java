package com.yourcity.adminresource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yourcity.service.admin.EventEJB;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Andrey on 17.04.2015.
 */
@Path("rest/admin/event")
@Produces("application/json")
public class AdminEventResource {

    @EJB
    private EventEJB eventEJB;

    @GET
    @Path("types")
    public Response getEventTypes() {
        return Response.ok(eventEJB.getEventTypes().toString()).build();
    }

    @GET
    @Path("find/{type}/all")
    public Response getAllEventsByType(@PathParam("type") String type) {
        try {
            JsonArray array = eventEJB.getAllEventOfType(type);
            return Response.ok(array.toString()).build();
        } catch (IllegalArgumentException e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("find/{type}/id")
    public Response findById(@QueryParam("id") Integer id, @PathParam("type") String type) {
        if (isValidId(id)) {
            try {
                JsonArray array = eventEJB.findById(id, type);
                if (array == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                return Response.ok(array.toString()).build();
            } catch (IllegalArgumentException e) {
                return Response.serverError().build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("find/{type}/name/cityid")
    public Response findByNameAndCityId(@QueryParam("cityId") Integer cityId, @QueryParam("name") String name,
            @PathParam("type") String type) {
        if (!isValidId(cityId) || !isValidString(name)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonArray array;
        try {
             array = eventEJB.findByNameAndCityId(cityId, name, type);
        } catch (IllegalArgumentException e) {
            return Response.serverError().build();
        }
        if (array == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("find/{type}/name")
    public Response findByName(@QueryParam("name") String name, @PathParam("type") String type) {
        if (!isValidString(name)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonArray array;
        try {
            array = eventEJB.findByName(name, type);
        } catch (IllegalArgumentException e) {
            return Response.serverError().build();
        }
        if (array == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("find/{type}/cityid")
    public Response findByCityId(@QueryParam("cityId") Integer cityId, @PathParam("type") String type) {
        if (!isValidId(cityId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonArray array;
        try {
            array = eventEJB.findByCityId(cityId, type);
        } catch (IllegalArgumentException e) {
            return Response.serverError().build();
        }
        if (array == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(array.toString()).build();
    }

    @POST
    @Path("create/{type}")
    @Consumes("application/json")
    public Response createEvent(String event, @PathParam("type") String type) {
        JsonParser parser = new JsonParser();
        JsonObject eventJsonObj = (JsonObject) parser.parse(event);
        if (eventEJB.createEvent(eventJsonObj, type)) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }

    private boolean isValidId(Integer id) {
        return id != null && id > 0;
    }

    private boolean isValidString(String name) {
        return name != null && !name.isEmpty();
    }
}
