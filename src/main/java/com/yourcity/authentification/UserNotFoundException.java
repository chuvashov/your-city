package com.yourcity.authentification;

/**
 * Created by Andrey on 06.04.2015.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found");
    }
}
