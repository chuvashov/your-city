package com.yourcity.adminresource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.yourcity.service.model.Museum;
import com.yourcity.service.model.MuseumImage;
import com.yourcity.service.ImageProvider;
import com.yourcity.service.util.CityUtil;
import com.yourcity.service.util.JsonUtil;
import com.yourcity.service.util.ConversionFromJsonException;

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

    @POST
    @Path("museum/add")
    @Consumes("application/json")
    public Response addNewMuseum(String imageBase64, @QueryParam("name") String name, @QueryParam("description") String description
            ,@QueryParam("email") String email, @QueryParam("about") String about, @QueryParam("phone") String phone
            ,@QueryParam("address") String address, @QueryParam("cityId") Integer cityId) {
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
                String imageName = ImageProvider.saveMuseumAvatarBase64AndGetName(imageBase64);
                if (imageName != null) {
                    museum.setImage(imageName);
                }
            }
            if (museum.saveIt()) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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
        List<Museum> museums = Museum.where("id = ?", id);
        if (museums.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Museum museum = museums.get(0);
        String avatar = museum.getImage();
        if (!museum.delete()) {
            return Response.serverError().build();
        }
        ImageProvider.deleteMuseumAvatar(avatar);

        List<MuseumImage> museumImages = MuseumImage.where("museum_id = ?", id);
        for (MuseumImage museumImage : museumImages) {
            ImageProvider.deleteMuseumImage(museumImage.getSrc());
        };
        MuseumImage.delete("museum_id = ?", id);
        return Response.ok().build();
    }

    @POST
    @Path("museum/update")
    @Consumes("application/json")
    public Response updateMuseumImage(String updatedMuseumJson, @QueryParam("id") Integer id) {
        JsonParser parser = new JsonParser();
        JsonObject jsonMuseum = (JsonObject) parser.parse(updatedMuseumJson);

        if (!isValidId(id)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Museum museum;
        try {
            museum = (Museum) Museum.where("id = ?", id).get(0);
            JsonUtil.jsonToMuseum(jsonMuseum, museum);
        } catch (ConversionFromJsonException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (museum.saveIt()) {

            return Response.ok().build();
        } else {
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
        int count = MuseumImage.delete("id = ?", id);
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
    public Response updateMuseum(String updatedMuseumJson, @QueryParam("id") Integer id) {
        JsonParser parser = new JsonParser();
        JsonObject jsonMuseumImage = (JsonObject) parser.parse(updatedMuseumJson);

        MuseumImage museumImage;
        if (id == null || id < 0) {
            museumImage = new MuseumImage();
        } else if (isValidId(id)) {
            List<MuseumImage> museumImages = MuseumImage.where("id = ?", id);
            if (museumImages.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            museumImage = museumImages.get(0);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            JsonUtil.jsonToMuseumImage(jsonMuseumImage, museumImage);
            if (museumImage.saveIt()) {
                JsonArray array = new JsonArray();
                array.add(JsonUtil.museumImageToJson(museumImage));
                return Response.ok(array.toString()).build();
            } else {
                return Response.serverError().build();
            }
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
        List<MuseumImage> museumImages = MuseumImage.where("museum_id = ?", museumId);
        if (museumImages.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        JsonArray array = new JsonArray();
        for (MuseumImage museumImage : museumImages) {
            array.add(JsonUtil.museumImageToJson(museumImage));
        }
        return Response.ok(array.toString()).build();
    }

    @GET
    @Path("museum/id")
    public Response findById(@QueryParam("id") Integer id) {
        if (isValidId(id)) {
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
    @Path("museum")
    public Response find(@QueryParam("cityId") Integer cityId, @QueryParam("name") String name) {
        Response response;
        if (isValidId(cityId)) {
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
        List<Museum> museums = Museum.findBySQL("SELECT * FROM MUSEUMS;");
        if (museums.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertMuseumListToJsonArrayAsString(museums)).build();
    }

    private Response findByNameAndCityId(String name, Integer cityId) {
        List<Museum> museums = Museum.where(format("name = '%s' and city_id = '%s'", name, cityId));
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

    private boolean isValidString(String name) {
        return name != null && !name.isEmpty();
    }

    private boolean isValidId(Integer id) {
        return id != null && id > 0;
    }


}
