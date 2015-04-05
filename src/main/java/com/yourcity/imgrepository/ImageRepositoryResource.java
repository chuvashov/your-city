package com.yourcity.imgrepository;

import com.yourcity.service.ImageProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by Andrey on 05.04.2015.
 */
@Path("/")
public class ImageRepositoryResource {

    @GET
    @Path("images-repository")
    @Produces("text/image")
    public Response getImage(@QueryParam("path") String path) {
        if (path == null || path.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        byte[] img;
        try {
            img = ImageProvider.getImageByPath(path);
        } catch (IOException e) {
            return Response.serverError().build();
        }
        if (img == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(img).build();
    }
}
