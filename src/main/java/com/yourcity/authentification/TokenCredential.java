package com.yourcity.authentification;

import org.picketlink.idm.credential.AbstractBaseCredentials;

/**
 * Created by Andrey on 05.04.2015.
 */
public class TokenCredential extends AbstractBaseCredentials {

    private String token;

    public TokenCredential(String token) {
        this.token = token;
    }

    @Override
    public void invalidate() {
        token = null;
    }

    public String getToken() {
        return token;
    }
}
