package com.yourcity.service.model;

import org.javalite.activejdbc.Model;

/**
 * Created by Andrey on 08.02.2015.
 */
public class User extends Model {

    public User() {
    }

    public Integer getUserId() {
        return getInteger("id");
    }

    public void setName(String name) {
        setString("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public void setLogin(String login) {
        setString("login", login);
    }

    public String getLogin() {
        return getString("login");
    }

    public void setPassword(String password) {
        setString("password", password);
    }

    public String getPassword() {
        return getString("password");
    }

    public void setEmail(String email) {
        setString("email", email);
    }

    public String getEmail() {
        return getString("email");
    }

    @Override
    public boolean equals(Object user) {
        if (user == null || !(user instanceof User)) {
            return false;
        }
        return this.getUserId().equals(((User) user).getUserId());
    }
}
