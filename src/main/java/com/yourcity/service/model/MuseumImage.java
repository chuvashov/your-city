package com.yourcity.service.model;

import org.javalite.activejdbc.Model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Andrey on 05.03.2015.
 */
public class MuseumImage extends Model {

    public Integer getMuseumImageId() {
        return getInteger("id");
    }

    public void setMuseumId(@NotNull @Min(0) Integer museumId) {
        setInteger("museum_id", museumId);
    }

    public Integer getMuseumId() {
        return getInteger("museum_id");
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public String getDescription() {
        String description = getString("description");
        if (description == null) {
            description = "";
        }
        return description;
    }

    public void setSrc(@NotNull String src) {
        setString("src", src);
    }

    public String getSrc() {
        return getString("src");
    }
}
