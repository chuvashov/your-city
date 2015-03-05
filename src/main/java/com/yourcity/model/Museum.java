package com.yourcity.model;

import org.javalite.activejdbc.Model;

/**
 * Created by Andrey on 22.02.2015.
 */
public class Museum extends Model {

    public Integer getMuseumId() {
        return getInteger("id");
    }

    public void setName(String name) {
        setString("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public void setAddress(String address) {
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

    public void setCityId(Integer id) {
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

    public void setAbout(String about) {
        setString("about", about);
    }

    public String getAbout() {
        String about = getString("about");
        if (about == null) {
            about = "";
        }
        return about;
    }

}
