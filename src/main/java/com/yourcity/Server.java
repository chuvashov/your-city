package com.yourcity;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.FeatureContext;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andrey on 03.04.2015.
 */
public class Server {

    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();

    public Server() {
    }

    /*@Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }*/

}