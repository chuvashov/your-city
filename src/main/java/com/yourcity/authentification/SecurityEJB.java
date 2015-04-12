package com.yourcity.authentification;

import com.google.gson.JsonObject;
import com.yourcity.service.DatabaseProvider;
import com.yourcity.service.model.User;
import com.yourcity.service.util.ConversionFromJsonException;
import com.yourcity.service.util.JsonUtil;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;

import static org.picketlink.idm.model.basic.BasicModel.grantRole;
import static com.yourcity.authentification.YourCityApplicationRole.ADMINISTRATOR;

/**
 * Created by Andrey on 07.04.2015.
 */
@Singleton
@Startup
public class SecurityEJB {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityEJB.class);

    @EJB
    private DatabaseProvider databaseProvider;

    @Inject
    private PartitionManager partitionManager;

    public void login(DefaultLoginCredentials credentials)
            throws UserNotFoundException {
        databaseProvider.openConnection();
        String identityLogin = credentials.getUserId();
        String identityPassword = credentials.getPassword();
        List<User> users = User.where("login = ? and password = ?", identityLogin, identityPassword);
        if (users.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = users.get(0);

        org.picketlink.idm.model.basic.User picketLinkUser = new org.picketlink.idm.model.basic.User(user.getLogin());
        IdentityManager identityManager = this.partitionManager.createIdentityManager();
        identityManager.add(picketLinkUser);
        identityManager.updateCredential(picketLinkUser, new Password(user.getPassword()));

        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();

        Role role;
        for (String roleName: user.getRoles()) {
            role = new Role(roleName);
            identityManager.add(role);
            grantRole(relationshipManager, picketLinkUser, role);
        }

        /*Identity.AuthenticationResult result = identity.login();

        if (Identity.AuthenticationResult.FAILED.equals(result)) {
            identityManager.remove(picketLinkUser);
            throw new IncorrectPasswordException();
        }*/

        databaseProvider.closeConnection();
    }

    public boolean registerUser(JsonObject userObj) throws UserWithLoginExistsException {
        databaseProvider.openConnection();
        User user = new User();
        try {
            JsonUtil.jsonToUser(userObj, user);
            if (hasLogin(user.getLogin())) {
                throw new UserWithLoginExistsException();
            }
            user.addRole(ADMINISTRATOR);
        } catch (ConversionFromJsonException e) {
            LOGGER.error("Couldn't convert user from json.", e);
            return false;
        }
        databaseProvider.openConnection();
        boolean result = user.saveIt();
        databaseProvider.closeConnection();
        return result;
    }

    public boolean hasLogin(String login) {
        databaseProvider.openConnection();
        Long count = User.count("login = ?", login);
        databaseProvider.closeConnection();
        return count > 0;
    }
}