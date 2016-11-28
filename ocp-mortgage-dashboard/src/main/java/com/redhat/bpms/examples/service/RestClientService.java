package com.redhat.bpms.examples.service;

import com.redhat.bpms.examples.Configuration;
import com.redhat.bpms.examples.mappers.ApplicationMapper;
import com.redhat.bpms.examples.mortgage.Application;
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
public class RestClientService {

    private static final Logger logger = LoggerFactory.getLogger(RestClientService.class);

    private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

    private static final String PROCESS_ID = "com.redhat.bpms.examples.mortgage.MortgageApplication";

    private String containerId;

    private KieServicesClient kieServicesClient;

    private Configuration.Users lastUser;

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

    public List<TaskSummary> listTasks() {

        List<TaskSummary> tasks = new LinkedList<>();

        try {
            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.KIESERVER)
                    .getServicesClient(UserTaskServicesClient.class);

            for (ProcessInstance process : listProcessInstances()) {

                tasks.addAll((userTaskClient.findTasksByStatusByProcessInstanceId(process.getId(), new LinkedList<>()
                        , 0, 100)));
            }

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint listTasks...", e);
        }

        return tasks;
    }

    public Response.Status startApp(Application application) {

        try {

            ProcessServicesClient processServicesClient = initClient(Configuration.Users.KIESERVER)
                    .getServicesClient(ProcessServicesClient.class);

            processServicesClient.startProcess(containerId, PROCESS_ID, ApplicationMapper.convert(application));

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint startApp...", e);
        }

        return Response.Status.OK;
    }

    public Response.Status claimTask(long taskId) {

        try {

            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.BROKER)
                    .getServicesClient(UserTaskServicesClient.class);

            userTaskClient.claimTask(containerId, taskId, Configuration.Users.BROKER.username());

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint claimTask...", e);
        }
        return Response.Status.OK;
    }

    public Response.Status releaseTask(long taskId) {

        try {

            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.BROKER)
                    .getServicesClient(UserTaskServicesClient.class);

            userTaskClient.releaseTask(containerId, taskId, Configuration.Users.BROKER.username());

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint releaseTask...", e);
        }
        return Response.Status.OK;
    }

    public Map<String, Object> startTask(long taskId) {

        Map<String, Object> inputApplication = new HashMap<>();
        try {

            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.BROKER)
                    .getServicesClient(UserTaskServicesClient.class);

            userTaskClient.startTask(containerId, taskId, Configuration.Users.BROKER.username());

            inputApplication = userTaskClient.getTaskInputContentByTaskId(containerId, taskId);

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint startTask...", e);
        }
        return inputApplication;
    }

    public Response.Status stopTask(long taskId) {

        try {

            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.BROKER)
                    .getServicesClient(UserTaskServicesClient.class);

            userTaskClient.stopTask(containerId, taskId, Configuration.Users.BROKER.username());

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint stopTask...", e);
        }
        return Response.Status.OK;
    }
}
