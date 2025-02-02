package org.example.services;

import java.util.regex.Pattern;

public class ValidationService {
    private static final String IPV4_PATTERN =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final String DOMAIN_PATTERN =
            "^(localhost|((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,})$";

    public boolean isValidIp(String ip) {
        return Pattern.matches(IPV4_PATTERN, ip);
    }

    public boolean isValidDomain(String domain) {
        return Pattern.matches(DOMAIN_PATTERN, domain);
    }

    public static boolean isValidPort(String port) {
        if (port == null || port.isEmpty()) {
            return false;
        }
        try {
            int portNumber = Integer.parseInt(port);
            return portNumber >= 0 && portNumber <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}