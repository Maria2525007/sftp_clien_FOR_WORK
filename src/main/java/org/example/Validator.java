package org.example;

import java.util.regex.Pattern;

public class Validator {
    public static boolean isValidIp(String ip) {
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return Pattern.matches(ipv4Pattern, ip);
    }
    public static boolean isValidPort(String port) {
        try {
            int portNumber = Integer.parseInt(port);
            return portNumber >= 0 && portNumber <= 65535; // Диапазон допустимых портов
        } catch (NumberFormatException e) {
            return false; // Если ввод не является числом
        }
    }
}
