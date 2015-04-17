package com.yourcity.service.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yourcity.service.DatabaseProvider;
import com.yourcity.service.model.Event;
import com.yourcity.service.util.ConversionFromJsonException;
import com.yourcity.service.util.JsonUtil;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Andrey on 17.04.2015.
 */
@Stateless
public class EventEJB {

    private JsonArray eventTypesJsonArray;

    @EJB
    private DatabaseProvider databaseProvider;

    @PostConstruct
    public void initEventTypesJsonArray() {
        eventTypesJsonArray = new JsonArray();
        for (String type : Event.EventType.getTypes()) {
            JsonObject t = new JsonObject();
            t.addProperty("eventType", type);
            eventTypesJsonArray.add(t);
        }
    }

    public JsonArray getAllEventOfType(String type) throws IllegalArgumentException {
        Event.EventType eventType = Event.EventType.getEventType(type);
        databaseProvider.openConnection();
        List<Event> events = Event.where(format("event_type = '%s'", eventType));
        return convertEventsToJsonArray(events);
    }

    public JsonArray findById(Integer id, String type) throws IllegalArgumentException {
        Event.EventType eventType = Event.EventType.getEventType(type);
        databaseProvider.openConnection();
        List<Event> events = Event.where(format("id = '%s' and event_type = '%s'", id, eventType));
        if (events.isEmpty()) {
            return null;
        }
        return convertEventsToJsonArray(events);
    }

    public JsonArray findByNameAndCityId(Integer cityId, String name, String type)
            throws IllegalArgumentException {
        Event.EventType eventType = Event.EventType.getEventType(type);
        databaseProvider.openConnection();
        List<Event> events = Event.where(format("event_type = '%s' and city_id = '%s' and name = '%s'",
                eventType, cityId, name));
        return convertEventsToJsonArray(events);
    }

    public JsonArray findByName(String name, String type)
            throws IllegalArgumentException {
        Event.EventType eventType = Event.EventType.getEventType(type);
        databaseProvider.openConnection();
        List<Event> events = Event.where(format("event_type = '%s' and name = '%s'", eventType, name));
        return convertEventsToJsonArray(events);
    }

    public JsonArray findByCityId(Integer cityId, String type)
            throws IllegalArgumentException {
        Event.EventType eventType = Event.EventType.getEventType(type);
        databaseProvider.openConnection();
        List<Event> events = Event.where(format("event_type = '%s' and city_id = '%s'", eventType, cityId));
        return convertEventsToJsonArray(events);
    }

    public boolean createEvent(JsonObject jsonObj, String type) {
        Event.EventType eventType;
        try {
            eventType = Event.EventType.getEventType(type);
        } catch (IllegalArgumentException e) {
            return false;
        }
        databaseProvider.openConnection();
        Event event = new Event();
        event.setEventType(eventType);
        try {
            JsonUtil.jsonToEvent(jsonObj, event);
        } catch (ConversionFromJsonException e) {
            return false;
        }
        return event.saveIt();
    }

    public JsonArray getEventTypes() {
        return eventTypesJsonArray;
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
