package com.yourcity;

import com.yourcity.service.DatabaseProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by Andrey on 08.02.2015.
 */
public class Server extends ResourceConfig {

    public Server() {
        //register(FreemarkerMvcFeature.class);
        register(new DynamicFeature() {
            @Override
            public void configure(ResourceInfo resourceInfo, FeatureContext context) {
                context.register(DatabaseProvider.class);
            }
        });

        packages(Server.class.getPackage().getName());

    }
}
