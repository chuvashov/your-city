package com.yourcity.service.model;

import org.javalite.activejdbc.Model;

/**
 * Created by Andrey on 22.02.2015.
 */
public class City extends Model {

    public Integer getCityId() {
        return getInteger("id");
    }

    public void setCityName(String name) {
        setString("city_name", name);
    }

    public String getCityName() {
        return getString("city_name");
    }
}
