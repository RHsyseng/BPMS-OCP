package com.redhat.bpms.examples.service;

import com.redhat.bpms.examples.Configuration;
import com.redhat.bpms.examples.mappers.ApplicationMapper;
import com.redhat.bpms.examples.mortgage.Application;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.UserTaskServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/***
 * @author jary
 * @since Nov/11/2016
 */
@Singleton
public class ApplicantRestService extends RestClientService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicantRestService.class);

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
}
