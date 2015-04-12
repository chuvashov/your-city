package com.yourcity.service.model;

import org.javalite.activejdbc.Model;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Andrey on 08.02.2015.
 */
public class User extends Model {

    public User() {
    }

    public Integer getUserId() {
        return getInteger("id");
    }

    public void setName(@NotNull String name) {
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

    public void setPassword(@NotNull String password) {
        setString("password", password);
    }

    public String getPassword() {
        return getString("password");
    }

    public void setEmail(@NotNull String email) {
        setString("email", email);
    }

    public String getEmail() {
        return getString("email");
    }

    public void addRole(@NotNull String role) {
        String roles = getString("roles");
        if (roles == null) {
            roles = role.toLowerCase();
        } else {
            roles += "," + role.toLowerCase();
        }
        setString("roles", roles);
    }

    public List<String> getRoles() {
        String roles = getString("roles");
        return roles != null ? Arrays.asList(roles.split(",")) : null;
    }

    public boolean deleteRole(String role) {
        List<String> roles = getRoles();
        if (roles == null) {
            return true;
        }
        if (!roles.remove(role.toLowerCase())) {
            return false;
        }
        String resultRoles = null;
        if (!roles.isEmpty()) {
            resultRoles = roles.get(0);
            for (int i = 1; i < roles.size(); i++) {
                resultRoles += "," + roles.get(i);
            }
        }
        set("roles", resultRoles);
        return true;
    }

    @Override
    public boolean equals(Object user) {
        if (user == null || !(user instanceof User)) {
            return false;
        }
        return this.getUserId().equals(((User) user).getUserId());
    }
}
