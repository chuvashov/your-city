package com.yourcity.authentification;

import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Created by Andrey on 05.04.2015.
 */
@RequestScoped
@PicketLink
public class Authenticator extends BaseAuthenticator {

    @Inject
    private DefaultLoginCredentials credentials;

    @Override
    public void authenticate() {
        if (this.credentials.getCredential() == null) {
            return;
        }

        if (isUsernamePasswordCredential()) {
            String userId = this.credentials.getUserId();
            Password password = (Password) this.credentials.getCredential();

            if (userId.equals("jane") && String.valueOf(password.getValue()).equals("abcd1234")) {
                successfulAuthentication();
            }
        } else if (isCustomCredential()) {
            TokenCredential customCredential = (TokenCredential) this.credentials.getCredential();

            if (customCredential.getToken() != null && customCredential.getToken().equals("valid_token")) {
                successfulAuthentication();
            }
        }
    }

    private boolean isUsernamePasswordCredential() {
        return Password.class.equals(credentials.getCredential().getClass()) && credentials.getUserId() != null;
    }

    private boolean isCustomCredential() {
        return TokenCredential.class.equals(credentials.getCredential().getClass());
    }

    private User getDefaultUser() {
        return new User("jane");
    }

    private void successfulAuthentication() {
        setStatus(AuthenticationStatus.SUCCESS);
        setAccount(getDefaultUser());
    }
}
