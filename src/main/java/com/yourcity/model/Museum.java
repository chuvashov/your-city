package com.yourcity.model;

import org.javalite.activejdbc.Model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Andrey on 22.02.2015.
 */
public class Museum extends Model {

    public Integer getMuseumId() {
        return getInteger("id");
    }

    public void setName(@NotNull String name) {
        setString("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public void setAddress(@NotNull String address) {
        setString("address", address);
    }

    public String getAddress() {
        return getString("address");
    }

    public void setPhone(String phone) {
        setString("phone", phone);
    }

    public String getPhone() {
        return getString("phone");
    }

    public void setEmail(String email) {
        setString("email", email);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setDescription(String description) {
        setString("description", description);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setCityId(@NotNull @Min(0) Integer id) {
        setInteger("city_id", id);
    }

    public Integer getCityId() {
        return getInteger("city_id");
    }

    public void setImage(String image) {
        setString("image", image);
    }

    public String getImage() {
        return getString("image");
    }

    public void setAbout(@NotNull String about) {
        setString("about", about);
    }

    public String getAbout() {
        return getString("about");
    }

}
