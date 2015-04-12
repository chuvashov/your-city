package com.yourcity.authentification;

import org.apache.http.client.protocol.ResponseContentEncoding;
import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.query.RelationshipQuery;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 06.04.2015.
 */
@Path("/")
public class AuthService {

    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;

    @Inject
    private DefaultLoginCredentials credentials;

    @EJB
    private SecurityEJB securityEJB;

    @POST
    @Path("signout")
    @Produces("application/json")
    @Consumes("application/json")
    public Response l(String s) {
        if (this.identity.isLoggedIn()) {
            //session.invalidate();
            this.identity.logout();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/authenticate")
    @Produces("application/json")
    public Response authenticate(DefaultLoginCredentials credential) {
        if (!this.identity.isLoggedIn()) {
            try {
                securityEJB.login(credential);
                this.credentials.setUserId(credential.getUserId());
                this.credentials.setPassword(credential.getPassword());
                this.identity.login();
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (this.identity.isLoggedIn()) {
            Account account = this.identity.getAccount();
            List<Role> roles = getUserRoles(account);

            AuthenticationResponse authenticationResponse = new AuthenticationResponse(account, roles);

            return Response.ok().entity(authenticationResponse).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credential").build();
    }

    private List<Role> getUserRoles(Account account) {
        RelationshipQuery<Grant> query = this.relationshipManager.createRelationshipQuery(Grant.class);

        query.setParameter(Grant.ASSIGNEE, account);

        List<Role> roles = new ArrayList<>();

        for (Grant grant: query.getResultList()) {
            roles.add(grant.getRole());
        }

        return roles;
    }

    private class AuthenticationResponse implements Serializable {

        private static final long serialVersionUID = 1297387771821869087L;

        private Account account;
        private List<Role> roles;

        public AuthenticationResponse(Account account, List<Role> roles) {
            this.account = account;
            this.roles = roles;
        }

        public Account getAccount() {
            return this.account;
        }

        public List<Role> getRoles() {
            return this.roles;
        }
    }

    /*

    public void logout(Identity identity) {
        identityManager.remove(identity.getAccount());
        identity.logout();
    }*/
}
