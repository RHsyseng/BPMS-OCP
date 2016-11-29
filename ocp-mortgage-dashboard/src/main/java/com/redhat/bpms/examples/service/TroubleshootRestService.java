package com.redhat.bpms.examples.service;

import com.redhat.bpms.examples.Configuration;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.api.model.instance.WorkItemInstance;
import org.kie.server.client.UserTaskServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/***
 * @author jary
 * @since Nov/11/2016
 */
@Singleton
public class TroubleshootRestService extends RestClientService {

    private static final Logger logger = LoggerFactory.getLogger(TroubleshootRestService.class);

    public List<WorkItemInstance> listItems() {

        List<WorkItemInstance> troubleshootItems = new LinkedList<>();
        try {

            for (WorkItemInstance item : listWorkItems()) {
                if (!item.getParameters().isEmpty() && item.getParameters().containsKey("TaskName")
                        && item.getParameters().get("TaskName").equals("Troubleshoot"))
                    troubleshootItems.add(item);
            }

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint claimTask...", e);
        }
        return troubleshootItems;
    }
}
