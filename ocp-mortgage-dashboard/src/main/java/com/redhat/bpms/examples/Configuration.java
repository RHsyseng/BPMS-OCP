package com.redhat.bpms.examples;

import java.util.HashMap;
import java.util.Map;

/***
 * @author jary
 * @since Nov/10/2016
 */
public final class Configuration {

    public static final String REST_BASE_URI = "http://mort-dashboard.bxms.ose/kie-server/services/rest/server";

    public static enum Users {

        KIESERVER("kieserver", "kieserver1!"),

        BROKER("brokerUser", "password1!"),

        MANAGER("managerUser", "password1!"),

        APPRAISER("appraiserUser", "password1!");

        private final String username;
        private final String password;

        Users (String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String username() {
            return this.username;
        }

        public String password() {
            return this.password;
        }
    }
}
