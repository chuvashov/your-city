package com.yourcity.adminresource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.yourcity.service.admin.MuseumAdminEJB;
import com.yourcity.service.model.Museum;
import com.yourcity.service.model.MuseumImage;
import com.yourcity.service.ImageProvider;
import com.yourcity.service.util.CityUtil;
import com.yourcity.service.util.JsonUtil;
import com.yourcity.service.util.ConversionFromJsonException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Andrey on 08.03.2015.
 */
@Path("rest/admin")
@Produces("application/json")
public class AdminMuseumResource {

    @EJB
    private MuseumAdminEJB museumEJB;

    @POST
    @Path("museum/add")
    @Consumes("application/json")
    public Response addNewMuseum(String imageBase64, @QueryParam("name") String name, @QueryParam("description") String description
            ,@QueryParam("email") String email, @QueryParam("about") String about, @QueryParam("phone") String phone
            ,@QueryParam("address") String address, @QueryParam("cityId") Integer cityId) {
        if (!isValidString(name) || !CityUtil.existCityId(cityId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            if (museumEJB.addNewMuseum(cityId, name, description, email, about, phone, address, imageBase64)) {
                return Response.ok().build();
            } else {
                return Response.serverError().build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("museum/delete")
    @Consumes("application/json")
    public Response deleteMuseum(@QueryParam("id") Integer id) {
        if (!isValidId(id)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (museumEJB.delete(id)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("museum/update")
    @Consumes("application/json")
    public Response updateMuseum(String updatedMuseumJson, @QueryParam("id") Integer id) {
        if (!isValidId(id)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonParser parser = new JsonParser();
        JsonObject jsonMuseum = (JsonObject) parser.parse(updatedMuseumJson);
        try {
            if (museumEJB.update(jsonMuseum, id)) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (ConversionFromJsonException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @POST
    @Path("museum/image/delete")
    public Response deleteMuseumImage(@QueryParam("id") Integer id) {
        if (!isValidId(id)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Response response;
        int count = museumEJB.deleteMuseumImages(id);
        if (count > 0) {
            response = Response.ok().build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    @POST
    @Path("museum/image/update")
    @Consumes("application/json")
    public Response updateMuseumImage(String updatedMuseumJson, @QueryParam("id") Integer id) {
        JsonParser parser = new JsonParser();
        JsonObject jsonMuseumImage = (JsonObject) parser.parse(updatedMuseumJson);

        try {
            JsonArray array = museumEJB.updateOrCreateMuseumImage(jsonMuseumImage, id);
            if (array == null) {
                return Response.serverError().build();
            }
            return Response.ok(array.toString()).build();
        } catch (ConversionFromJsonException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("museum/images")
    public Response getMuseumImages(@QueryParam("museumId") Integer museumId) {
        if (!isValidId(museumId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonArray array = museumEJB.getMuseumImages(museumId);
        if (array == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("museum/find/id")
    public Response findById(@QueryParam("id") Integer id) {
        if (isValidId(id)) {
            JsonArray array = museumEJB.findById(id);
            if (array == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(array.toString()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("museum/find/name/cityid")
    public Response findByNameAndCityId(@QueryParam("cityId") Integer cityId, @QueryParam("name") String name) {
        if (!isValidId(cityId) || !isValidString(name)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonArray array = museumEJB.findByNameAndCityId(name, cityId);
        if (array == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("museum/find/cityid")
    public Response findByCityId(@QueryParam("cityId") Integer cityId) {
        if (!isValidId(cityId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonArray array = museumEJB.findByCityId(cityId);
        if (array == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("museum/find/name")
    public Response findByName(@QueryParam("name") String name) {
        if (!isValidString(name)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonArray array = museumEJB.findByName(name);
        if (array == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("museum/find/all")
    public Response findAll() {
        JsonArray array = museumEJB.findAll();
        if (array == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(array.toString()).build();
    }

    private boolean isValidString(String name) {
        return name != null && !name.isEmpty();
    }

    private boolean isValidId(Integer id) {
        return id != null && id > 0;
    }


}
