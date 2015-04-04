package com.yourcity.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.service.MuseumEJB;
import com.yourcity.service.model.City;
import com.yourcity.service.model.Museum;
import com.yourcity.service.model.MuseumImage;
import com.yourcity.service.ImageProvider;
import com.yourcity.service.util.CityUtil;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

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
    @Path("images")
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
