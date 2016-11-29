package com.redhat.bpms.examples;

import com.redhat.bpms.examples.service.BrokerRestService;
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
@Path("/broker")
@RequestScoped
public class BrokerController {

    @Inject
    private BrokerRestService brokerRestService;

    @GET
    @Path("/dataCorrectionTasks")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaskSummary> listTasks() {

        return brokerRestService.listTasks();
    }

    @POST
    @Path("/claimTask")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status claimTask(Long taskId) {

        return brokerRestService.claimTask(taskId);
    }

    @POST
    @Path("/releaseTask")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status releaseTask(Long taskId) {

        return brokerRestService.releaseTask(taskId);
    }

    @POST
    @Path("/startTask")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> startTask(Long taskId) {

        return brokerRestService.startTask(taskId);
    }

    @POST
    @Path("/stopTask")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status stopTask(Long taskId) {

        return brokerRestService.stopTask(taskId);
    }
}
