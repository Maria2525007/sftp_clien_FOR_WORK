package org.example;

import java.util.regex.Pattern;

public class Validator {
    // Проверка валидности IP
    public static boolean isValidIp(String ip) {
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return Pattern.matches(ipv4Pattern, ip);
    }

    // Проверка валидности домена
    public static boolean isValidDomain(String domain) {
        String domainPattern = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";
        return Pattern.matches(domainPattern, domain);
    }

    // Проверка валидности порта
    public static boolean isValidPort(String port) {
        try {
            int portNumber = Integer.parseInt(port);
            return portNumber >= 0 && portNumber <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}