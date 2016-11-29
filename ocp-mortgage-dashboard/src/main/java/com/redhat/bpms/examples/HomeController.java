package com.redhat.bpms.examples;

import com.redhat.bpms.examples.service.MgmtClientService;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.api.model.instance.WorkItemInstance;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/***
 * @author jary
 * @since Nov/03/2016
 */
@Path("/home")
@RequestScoped
public class HomeController {

    @Inject
    private MgmtClientService mgmtClientService;

    @GET
    @Path("/processes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProcessDefinition> listProcesses() {

        return mgmtClientService.listProcesses();
    }

    @GET
    @Path("/running")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProcessInstance> listProcessInstances() {

        return mgmtClientService.listProcessInstances();
    }

    @GET
    @Path("/workitems")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkItemInstance> listWorkItems() {

        return mgmtClientService.listWorkItems();
    }
}
