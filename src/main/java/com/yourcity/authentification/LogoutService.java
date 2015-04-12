package com.yourcity.authentification;

import org.picketlink.Identity;
import org.picketlink.authorization.annotations.LoggedIn;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by Andrey on 10.04.2015.
 */
@Path("rest/admin")
public class LogoutService {

    @Inject
    private Identity identity;

    @POST
    @Path("/logout1")
    @Produces("application/json")
    @Consumes("application/json")
    public Response l(String s) {
        if (this.identity.isLoggedIn()) {
            //session.invalidate();
            this.identity.logout();
        }
        return Response.ok().build();
    }
}
