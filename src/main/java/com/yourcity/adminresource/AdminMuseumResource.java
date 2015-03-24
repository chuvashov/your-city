package com.yourcity.adminresource;

import com.google.gson.JsonArray;
import com.yourcity.model.Museum;
import com.yourcity.service.ImageProvider;
import com.yourcity.util.CityUtil;
import com.yourcity.util.JsonUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Andrey on 08.03.2015.
 */
@Path("/rest/admin")
@Produces("application/json")
public class AdminMuseumResource {

    @POST
    @Path("/museum/add")
    @Consumes("application/json")
    public Response addNewMuseum(String imageBase64, @QueryParam("name") String name, @QueryParam("description") String description
            , @QueryParam("email") String email, @QueryParam("about") String about, @QueryParam("phone") String phone
            , @QueryParam("address") String address, @QueryParam("cityId") Integer cityId) {
        if (!isValidString(name) || !CityUtil.existCityId(cityId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Museum museum = new Museum();
        try {
            museum.setCityId(cityId);
            museum.setName(name);
            museum.setDescription(description);
            museum.setEmail(email);
            museum.setAbout(about);
            museum.setPhone(phone);
            museum.setAddress(address);
            if (isValidString(imageBase64)) {
                String imageName = ImageProvider.saveAvatarBase64ImageAndGetName(imageBase64);
                if (imageName != null) {
                    museum.setImage(imageName);
                }
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (museum.saveIt()) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isValidString(String name) {
        return name != null && !name.isEmpty();
    }

    @GET
    @Path("/museum/id")
    public Response findById(@QueryParam("id") Integer id) {
        if (id != null && id > 0) {
            Museum museum = Museum.findById(id);
            if (museum == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            JsonArray array = new JsonArray();
            array.add(JsonUtil.museumToJson(museum));
            return Response.ok(array.toString()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/museum")
    public Response find(@QueryParam("cityId") Integer cityId, @QueryParam("name") String name) {
        Response response;
        if (cityId != null && cityId > 0) {
            if (isValidString(name)) {
                response = findByNameAndCityId(name, cityId);
            } else {
                response = findByCityId(cityId);
            }
        } else if (isValidString(name)) {
            response = findByName(name);
        } else {
            response = findAll();
        }
        return response;
    }

    private Response findAll() {
        List<Museum> museums = Museum.findBySQL("SELECT * FROM MUSEUMS");
        if (museums.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertMuseumListToJsonArrayAsString(museums)).build();
    }

    private Response findByNameAndCityId(String name, Integer cityId) {
        List<Museum> museums = Museum.where(format("name = '%s', city_id = '%s'", name, cityId));
        if (museums.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertMuseumListToJsonArrayAsString(museums)).build();
    }

    private Response findByName(String name) {
        List<Museum> museums = Museum.where("name = ?", name);
        if (museums.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertMuseumListToJsonArrayAsString(museums)).build();
    }

    private Response findByCityId(Integer cityId) {
        List<Museum> museums = Museum.where("city_id = ?", cityId);
        if (museums.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertMuseumListToJsonArrayAsString(museums)).build();
    }

    private String convertMuseumListToJsonArrayAsString(List<Museum> museums) {
        JsonArray array = new JsonArray();
        for (Museum museum : museums) {
            array.add(JsonUtil.museumToJson(museum));
        }
        return array.toString();
    }

}
