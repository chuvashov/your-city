package com.yourcity.model;

import org.javalite.activejdbc.Model;

import javax.validation.constraints.NotNull;

/**
 * Created by Andrey on 05.03.2015.
 */
public class MuseumImage extends Model {

    public Integer getMuseumImageId() {
        return getInteger("id");
    }

    public void setDescription(String description) {
        setString("description", description);
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
