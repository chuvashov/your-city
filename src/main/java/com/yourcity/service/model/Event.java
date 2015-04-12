package com.yourcity.service.model;

import org.javalite.activejdbc.Model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by Andrey on 12.04.2015.
 */
public class Event extends Model {

    public Integer getEventId() {
        return getInteger("id");
    }

    public void setCityId(@NotNull @Min(0) Integer cityId) {
        setInteger("city_id", cityId);
    }

    public Integer getCityId() {
        return getInteger("city_id");
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setImage(String image) {
        set("image", image);
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

    public void setEventType(@NotNull String type) {
        setString("event_type", type);
    }

    public String getEventType() {
        return getString("event_type");
    }

    public void setStartTime(Timestamp startTime) {
        set("start_time", startTime);
    }

    public Timestamp getStartTime() {
        return getTimestamp("start_time");
    }

    public void setFinishTime(Timestamp finishTime) {
        set("start_time", finishTime);
    }

    public Timestamp getFinishTime() {
        return getTimestamp("finish_time");
    }

    public void setTimeType(String timeType) {
        set("time_type", timeType);
    }

    public String getTimeType() {
        return getString("time_type");
    }
}
