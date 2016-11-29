package com.redhat.bpms.examples;

import com.redhat.bpms.examples.service.TroubleshootRestService;
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
@Path("/troubleshoot")
@RequestScoped
public class TroubleshootController {

    @Inject
    private TroubleshootRestService troubleshootRestService;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkItemInstance> listItems() {

        return troubleshootRestService.listItems();
    }
}
