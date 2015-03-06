package com.yourcity.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.model.City;
import com.yourcity.model.Museum;
import com.yourcity.model.MuseumImage;
import com.yourcity.service.ImageProvider;
import com.yourcity.util.CityUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Andrey on 22.02.2015.
 */
@Path("/museum")
@Produces("application/json")
public class MuseumResource {

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
    public Response getMuseumsByIndexes(@QueryParam("city") String cityName) {
        City city = CityUtil.getCityByName(cityName);
        if (city == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Museum> museums = Museum.where("city_id = ?", city.getCityId());
        JsonArray array = new JsonArray();
        JsonObject jsonObj;
        for (Museum museum : museums) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("name", museum.getName());
            jsonObj.addProperty("description", museum.getDescription());
            jsonObj.addProperty("image", ImageProvider.getMuseumAvatarBase64Url(museum.getImage()));
            jsonObj.addProperty("id", museum.getMuseumId());
            array.add(jsonObj);
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("/count")
    public Response getCount(@QueryParam("city") String cityName) {
        City city = CityUtil.getCityByName(cityName);
        if (city == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Long count = Museum.count("city_id = " + city.getCityId());
        return Response.ok(count.toString()).build();
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
        jsonObj.addProperty("image", ImageProvider.getMuseumAvatarBase64Url(museum.getImage()));
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
            jsonObj.addProperty("src", ImageProvider.getMuseumImageBase64Url(src));
            array.add(jsonObj);
        }
        return Response.ok(array.toString()).build();
    }


}
