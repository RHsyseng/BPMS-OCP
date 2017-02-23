package com.redhat.bpms.examples.service;

import com.redhat.bpms.examples.Configuration;
import org.apache.commons.math3.exception.NullArgumentException;
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

    private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

    protected final String PROCESS_ID = "com.redhat.bpms.examples.mortgage.MortgageApplication";


    KieServicesClient initClient(Configuration.Users user) {

        String kieServerFQDN = System.getProperty(Configuration.KIE_API_REST_FQDN);
        if(kieServerFQDN == null || kieServerFQDN.equals("")) {

            java.util.Properties props = System.getProperties();
            props.list(System.out);

            throw new RuntimeException("initClient:  Need to specify system property: "+Configuration.KIE_API_REST_FQDN);
        }

        // Expecting something similar to the following: http://mortgage-rule-mortgage-rules.cloudapps.na.openshift.opentlc.com/kie-server/services/rest/server
        String restBaseUri = "http://"+kieServerFQDN+"/kie-server/services/rest/server";

        logger.info("initClient() using kie server URI of: "+restBaseUri);

        KieServicesClient client = null;
        try {

            KieServicesConfiguration conf = KieServicesFactory.newRestConfiguration(
                    restBaseUri,
                    user.username(),
                    user.password());

            conf.setMarshallingFormat(FORMAT);

            client = KieServicesFactory.newKieServicesClient(conf);

        } catch (Exception e) {
            logger.error("ERROR in initializing REST client...", e);
        }
        return client;
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

    List<KieContainerResource> listContainers() {

        List<KieContainerResource> containers = new LinkedList<>();
        try {
            containers = initClient(Configuration.Users.KIESERVER)
                    .listContainers().getResult().getContainers();

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

    String getContainerId() {

        String containerId = null;
        try {
                containerId = listContainers().get(0).getContainerId();

                if (containerId == null)
                    throw new NullArgumentException();

        } catch (Exception e) {
            logger.error("ERROR in resolving container Id...", e);
        }

        return containerId;
    }
}
