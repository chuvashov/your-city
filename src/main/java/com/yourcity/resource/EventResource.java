package com.yourcity.resource;

import com.google.gson.JsonArray;
import com.yourcity.service.EventEJB;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Andrey on 18.04.2015.
 */
@Path("event")
public class EventResource {

    @EJB
    private EventEJB eventEJB;

    @GET
    @Path("{type}/all")
    @Produces("application/json")
    public Response getAllEvents(@PathParam("type") String type, @QueryParam("city") String city) {
        if (!isValidString(city)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            JsonArray array = eventEJB.getAllEvents(city, type);
            if (array == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok(array.toString()).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("{type}/view")
    @Produces("application/json")
    public Response getEventById(@PathParam("type") String type, @QueryParam("id") Integer id) {
        if (!isValidId(id)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            JsonArray array = eventEJB.getEventById(id, type);
            if (array == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok(array.toString()).build();
            }
        } catch (IllegalArgumentException e) {
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
