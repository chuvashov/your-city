package com.yourcity.service.model;

import org.javalite.activejdbc.Model;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 12.04.2015.
 */
public class Event extends Model {

    public enum EventType {
        CONCERT,
        EXHIBITION,
        EVENT,
        CAFE,
        CLUB,
        BAR;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public static EventType getEventType(String type) {
            switch (type.toLowerCase()) {
                case "concert": return CONCERT;
                case "exhibition": return EXHIBITION;
                case "event": return EVENT;
                case "cafe": return CAFE;
                case "club": return CLUB;
                case "bar": return BAR;
                default: throw new IllegalArgumentException();
            }
        }

        public static List<String> getTypes() {
            List<String> types = new ArrayList<>();
            for (EventType event : EventType.values()) {
                types.add(event.toString());
            }
            return types;
        }
    }

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

    public void setEventType(@NotNull EventType type) {
        setString("event_type", type.toString());
    }

    public EventType getEventType() {
        return EventType.getEventType(getString("event_type"));
    }

    public void setStartTime(String startTime) {
        set("start_time", startTime);
    }

    public String getStartTime() {
        return getString("start_time");
    }

    public void setFinishTime(String finishTime) {
        set("start_time", finishTime);
    }

    public String getFinishTime() {
        return getString("finish_time");
    }

    public void setName(@NotNull String name) {
        setString("name", name);
    }

    public String getName() {
        return getString("name");
    }
}
