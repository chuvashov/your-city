package com.yourcity.authentification;

/**
 * Created by Andrey on 12.04.2015.
 */
public class UserWithLoginExistsException extends Exception {

    public UserWithLoginExistsException() {
        super("User with the login exists");
    }
}
