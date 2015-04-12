package com.yourcity.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.service.MuseumEJB;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Andrey on 22.02.2015.
 */
@Path("museum")
@Produces("application/json")
public class MuseumResource {

    @EJB
    private MuseumEJB museumEJB;

    @GET
    @Path("info/byId")
    public Response getMuseumById(@QueryParam("id") int museumId) {
        if (museumId < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonObject jsonObj = museumEJB.getMuseumById(museumId);
        if (jsonObj == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(jsonObj.toString()).build();
    }

    @GET
    @Path("/all")
    public Response getMuseumsByIndexes(@QueryParam("city") String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonArray array = museumEJB.getMuseumsFromCity(cityName);
        if (array == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("view")
    public Response getMuseum(@QueryParam("id") int museumId) {
        if (museumId < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonObject jsonObj = museumEJB.getMuseumForView(museumId);
        if (jsonObj == null) {
            Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(jsonObj.toString()).build();
    }

    @GET
    @Path("/images")
    public Response getImages(@QueryParam("id") int museumId) {
        if (museumId < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonArray array = museumEJB.getMuseumImages(museumId);
        if (array == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(array.toString()).build();
    }

}
