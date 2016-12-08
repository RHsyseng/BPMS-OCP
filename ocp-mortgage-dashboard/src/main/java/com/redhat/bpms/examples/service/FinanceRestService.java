package com.redhat.bpms.examples.service;

import com.redhat.bpms.examples.Configuration;
import com.redhat.bpms.examples.mappers.ApplicationMapper;
import com.redhat.bpms.examples.mortgage.Application;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.api.model.instance.TaskSummary;
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
public class FinanceRestService extends RestClientService {

    private static final Logger logger = LoggerFactory.getLogger(FinanceRestService.class);

    public Response.Status claimTask(long taskId) {

        try {

            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.MANAGER)
                    .getServicesClient(UserTaskServicesClient.class);

            userTaskClient.claimTask(getContainerId(), taskId, Configuration.Users.MANAGER.username());

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint claimTask...", e);
        }
        return Response.Status.OK;
    }

    public Response.Status releaseTask(long taskId) {

        try {

            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.MANAGER)
                    .getServicesClient(UserTaskServicesClient.class);

            userTaskClient.releaseTask(getContainerId(), taskId, Configuration.Users.MANAGER.username());

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint releaseTask...", e);
        }
        return Response.Status.OK;
    }

    public Map<String, Object> startTask(long taskId) {

        Map<String, Object> inputApplication = new HashMap<>();
        try {

            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.MANAGER)
                    .getServicesClient(UserTaskServicesClient.class);

            userTaskClient.startTask(getContainerId(), taskId, Configuration.Users.MANAGER.username());

            inputApplication = userTaskClient.getTaskInputContentByTaskId(getContainerId(), taskId);

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint startTask...", e);
        }
        return inputApplication;
    }

    public Response.Status stopTask(long taskId) {

        try {

            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.MANAGER)
                    .getServicesClient(UserTaskServicesClient.class);

            userTaskClient.stopTask(getContainerId(), taskId, Configuration.Users.MANAGER.username());

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint stopTask...", e);
        }
        return Response.Status.OK;
    }

    private List<TaskSummary> filterTasks(String filterString) {

        List<TaskSummary> tasks = new LinkedList<>();

        try {
            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.KIESERVER)
                    .getServicesClient(UserTaskServicesClient.class);

            for (ProcessInstance process : listProcessInstances()) {

                for (TaskSummary task : userTaskClient.findTasksByStatusByProcessInstanceId(process.getId(),
                        new LinkedList<>(), 0, 100)) {
                    if (task.getName().equals(filterString))
                        tasks.add(task);
                }
            }

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint listTasks...", e);
        }
        return tasks;
    }

    public Response.Status completeTask(Long taskId, Boolean approved) {

        try {
            UserTaskServicesClient userTaskClient = initClient(Configuration.Users.MANAGER)
                    .getServicesClient(UserTaskServicesClient.class);

            Map<String, Object> params = new HashMap<>();
            params.put("brokerOverrideTaskOutput", approved);

            userTaskClient.completeTask(getContainerId(), taskId, Configuration.Users.MANAGER.username(), params);

        } catch (Exception e) {
            logger.error("ERROR in Rest endpoint completeTask...", e);
        }
        return Response.Status.OK;
    }

    public List<TaskSummary> listTasks() {

        return filterTasks("Financial Review");
    }
}
