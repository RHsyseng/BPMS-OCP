package com.redhat.bpms.examples;

import com.redhat.bpms.examples.mortgage.Application;
import com.redhat.bpms.examples.service.AppraiserRestService;
import org.kie.server.api.model.instance.TaskSummary;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/***
 * @author jary
 * @since Nov/03/2016
 */
@Path("/appraiser")
@RequestScoped
public class AppraiserController {

    @Inject
    private AppraiserRestService appraiserRestService;

    @GET
    @Path("/tasks")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaskSummary> listTasks() {

        return appraiserRestService.listTasks();
    }

    @POST
    @Path("/claimTask")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status claimTask(Long taskId) {

        return appraiserRestService.claimTask(taskId);
    }

    @POST
    @Path("/releaseTask")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status releaseTask(Long taskId) {

        return appraiserRestService.releaseTask(taskId);
    }

    @POST
    @Path("/startTask")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> startTask(Long taskId) {

        return appraiserRestService.startTask(taskId);
    }

    @POST
    @Path("/stopTask")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status stopTask(Long taskId) {

        return appraiserRestService.stopTask(taskId);
    }

    @POST
    @Path("/completeTask/{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status completeTask(@PathParam("taskId") Long taskId, Application application) {

        return appraiserRestService.completeTask(taskId, application);
    }
}
