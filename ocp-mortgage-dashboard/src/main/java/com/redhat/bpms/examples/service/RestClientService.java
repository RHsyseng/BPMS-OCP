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
public class RestClientService {

    private static final Logger logger = LoggerFactory.getLogger(RestClientService.class);

    protected static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

    protected static final String PROCESS_ID = "com.redhat.bpms.examples.mortgage.MortgageApplication";

    protected String containerId;

    protected KieServicesClient kieServicesClient;

    protected Configuration.Users lastUser;

    protected KieServicesClient initClient(Configuration.Users user) {

        try {
            if (kieServicesClient == null || lastUser != user) {

                KieServicesConfiguration conf = KieServicesFactory.newRestConfiguration(
                        Configuration.REST_BASE_URI,
                        user.username(),
                        user.password());

                conf.setMarshallingFormat(FORMAT);

                lastUser = user;
                kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
            }
        } catch (Exception e) {
            logger.error("ERROR in initializing REST client...", e);
        }
        return kieServicesClient;
    }

    public List<ProcessInstance> listProcessInstances() {

        List<ProcessInstance> processInstances = new LinkedList<>();

        try {
            QueryServicesClient queryClient = initClient(Configuration.Users.KIESERVER)
                    .getServicesClient(QueryServicesClient.class);

            for (KieContainerResource container : listContainers()) {
                processInstances.addAll(queryClient.findProcessInstancesByContainerId(container.getContainerId(),
                        new LinkedList<>(), 0, 100));
            }

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint listProcessInstances...", e);
        }

        return processInstances;
    }

    public List<KieContainerResource> listContainers() {

        List<KieContainerResource> containers = new LinkedList<>();
        try {
            containers = initClient(Configuration.Users.KIESERVER)
                    .listContainers().getResult().getContainers();

            if (!containers.isEmpty()) {
                containerId = containers.get(0).getContainerId();
            }

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint listContainers...", e);
        }
        return containers;
    }

    public List<WorkItemInstance> listWorkItems() {

        List<WorkItemInstance> tasks = new LinkedList<>();

        try {
            ProcessServicesClient processServicesClient = initClient(Configuration.Users.KIESERVER)
                    .getServicesClient(ProcessServicesClient.class);

            for (ProcessInstance process : listProcessInstances()) {

                tasks.addAll(processServicesClient.getWorkItemByProcessInstance(process.getContainerId(), process.getId
                        ()));
            }

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint listWorkItems...", e);
        }

        return tasks;
    }
}
