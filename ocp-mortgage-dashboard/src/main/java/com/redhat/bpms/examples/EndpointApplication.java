package com.redhat.bpms.examples;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import java.util.Properties;
import java.util.Set;

/***
 * @author jary
 * @since Nov/03/2016
 */
@ApplicationPath("service")
public class EndpointApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(final Set<Class<?>> resources) {
        resources.add(HomeController.class);
        resources.add(BrokerController.class);
        resources.add(ApplicantController.class);
    }
}
