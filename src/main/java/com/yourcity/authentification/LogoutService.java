package com.yourcity.authentification;

import org.picketlink.Identity;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by Andrey on 05.04.2015.
 */
@Path("/")
public class LogoutService {

    @Inject
    private Identity identity;

    @POST
    @Path("logout")
    public void logout() {
        if (identity.isLoggedIn()) {
            identity.logout();
        }
    }
}
