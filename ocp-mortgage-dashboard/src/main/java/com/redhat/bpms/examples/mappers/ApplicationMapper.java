package com.redhat.bpms.examples.mappers;

import com.redhat.bpms.examples.mortgage.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/***
 * @author jary
 * @since Nov/10/2016
 */
public final class ApplicationMapper {

    public static Map<String, Object> convert(Application application, String payloadKey) {

        final Logger logger = LoggerFactory.getLogger(ApplicationMapper.class);

        Map<String, Object> payload = new HashMap<>();

        try {
            Map<String, Object> wrapper = new HashMap<>();
            Map<String, Object> appContent = new HashMap<>();
            Map<String, Object> applicant = new HashMap<>();
            Map<String, Object> property = new HashMap<>();

            applicant.put("name", application.getApplicant().getName());
            applicant.put("ssn", application.getApplicant().getSsn());
            applicant.put("income", application.getApplicant().getIncome());

            property.put("address", application.getProperty().getAddress());
            property.put("price", application.getProperty().getPrice());

            appContent.put("applicant", applicant);
            appContent.put("property", property);
            appContent.put("downPayment", application.getDownPayment());
            appContent.put("amortization", application.getAmortization());
            appContent.put("mortgageAmount", application.getProperty().getPrice() - application.getDownPayment());
            appContent.put("apr", application.getApr());

            if (application.getAppraisal() != null) {
                Map<String, Object> appraisal = new HashMap<>();
                appraisal.put("property", property);
                appraisal.put("value", application.getAppraisal().getValue());
                appContent.put("appraisal", appraisal);
            }

            wrapper.put("com.redhat.bpms.examples.mortgage.Application", appContent);
            payload.put(payloadKey, wrapper);

        } catch (Exception e) {
            logger.error("ERROR in mapping application to HashMap format...", e);
        }

        return payload;
    }

    public static Map<String, Object> convert(Application application) {
        return convert(application, "application");
    }
}