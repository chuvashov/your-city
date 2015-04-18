package com.yourcity.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.service.model.City;
import com.yourcity.service.model.Event;
import com.yourcity.service.util.CityUtil;
import com.yourcity.service.util.JsonUtil;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Andrey on 18.04.2015.
 */
@Stateless
public class EventEJB {

    @EJB
    private DatabaseProvider databaseProvider;

    public JsonArray getAllEvents(String cityName, String type) {
        Event.EventType eventType = Event.EventType.getEventType(type);
        databaseProvider.openConnection();
        City city = CityUtil.getCityByName(cityName);
        List<Event> events = Event.where(format("city_id = '%s' and event_type = '%s'", city.getCityId(), eventType));
        if (events.isEmpty()) {
            return null;
        }
        return convertEventsToJsonArray(events);
    }

    public JsonArray getEventById(Integer id, String type) {
        Event.EventType eventType = Event.EventType.getEventType(type);
        databaseProvider.openConnection();
        List<Event> events = Event.where(format("id = '%s' and event_type = '%s'", id, eventType));
        if (events.isEmpty()) {
            return null;
        }
        return convertEventsToJsonArray(events);
    }

    private JsonArray convertEventsToJsonArray(List<Event> events) {
        JsonArray array = new JsonArray();
        JsonObject jsonObj;
        for (Event event : events) {
            jsonObj = JsonUtil.eventToJson(event);
            array.add(jsonObj);
        }
        return array;
    }
}
