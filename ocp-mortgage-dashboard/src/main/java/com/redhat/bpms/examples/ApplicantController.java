package com.redhat.bpms.examples;

import com.redhat.bpms.examples.mortgage.Application;
import com.redhat.bpms.examples.service.RestClientService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/***
 * @author jary
 * @since Nov/03/2016
 */
@Path("/applicant")
@RequestScoped
public class ApplicantController {

    @Inject
    private RestClientService restClientService;

    @POST
    @Path("/startApp")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status startApp(Application application) {

        return restClientService.startApp(application);
    }
}
