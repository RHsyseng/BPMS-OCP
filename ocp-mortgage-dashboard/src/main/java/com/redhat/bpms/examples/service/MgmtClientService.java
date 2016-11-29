package com.redhat.bpms.examples.service;

import com.redhat.bpms.examples.Configuration;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.api.model.instance.WorkItemInstance;
import org.kie.server.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import java.util.LinkedList;
import java.util.List;

/***
 * @author jary
 * @since Nov/11/2016
 */
@Singleton
public class MgmtClientService extends RestClientService {

    private static final Logger logger = LoggerFactory.getLogger(MgmtClientService.class);

    public List<ProcessDefinition> listProcesses() {

        List<ProcessDefinition> processDefinitions = new LinkedList<>();

        try {
            QueryServicesClient queryClient = initClient(Configuration.Users.KIESERVER)
                    .getServicesClient(QueryServicesClient.class);

            for (KieContainerResource container : listContainers()) {
                processDefinitions.addAll(queryClient.findProcessesByContainerId(container.getContainerId(), 0, 100));
            }

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint listProcesses...", e);
        }

        return processDefinitions;
    }
}
