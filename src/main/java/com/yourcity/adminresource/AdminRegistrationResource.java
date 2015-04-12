package com.yourcity.adminresource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yourcity.authentification.SecurityEJB;
import com.yourcity.authentification.UserWithLoginExistsException;
import com.yourcity.service.util.ConversionFromJsonException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Andrey on 12.04.2015.
 */
@Path("rest/admin")
public class AdminRegistrationResource {

    @EJB
    private SecurityEJB securityEJB;

    @POST
    @Path("register")
    @Consumes("application/json")
    public Response createUser(String userJson) {
        if (!isValidString(userJson)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        JsonParser parser = new JsonParser();
        JsonObject user = (JsonObject) parser.parse(userJson);
        try {
            if (securityEJB.registerUser(user)) {
                return Response.ok().build();
            } else {
                return Response.serverError().build();
            }
        } catch (UserWithLoginExistsException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User with the login exists.").build();
        }
    }

    private boolean isValidString(String str) {
        return str != null && !str.isEmpty();
    }
}
