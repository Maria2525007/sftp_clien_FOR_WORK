package org.example;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ValidatorTest {

    // Позитивные тесты

    @Test
    public void testValidIp() {
        // Проверка корректного IPv4
        assertTrue(Validator.isValidIp("192.168.1.1"));
        assertTrue(Validator.isValidIp("10.0.0.1"));
        assertTrue(Validator.isValidIp("255.255.255.255"));
    }

    @Test
    public void testValidDomain() {
        // Проверка корректного домена
        assertTrue(Validator.isValidDomain("example.com"));
        assertTrue(Validator.isValidDomain("sub.domain.co.uk"));
        assertTrue(Validator.isValidDomain("my-domain123.com"));
    }

    @Test
    public void testValidPort() {
        // Проверка корректного порта
        assertTrue(Validator.isValidPort("22"));
        assertTrue(Validator.isValidPort("8080"));
        assertTrue(Validator.isValidPort("65535"));
    }

    // Негативные тесты

    @Test
    public void testInvalidIp() {
        // Проверка некорректного IPv4
        assertFalse(Validator.isValidIp("256.0.0.1"));
        assertFalse(Validator.isValidIp("192.168.1"));
        assertFalse(Validator.isValidIp("abc.def.ghi.jkl"));
    }

    @Test
    public void testInvalidDomain() {
        // Проверка некорректного домена
        assertFalse(Validator.isValidDomain("-example.com"));
        assertFalse(Validator.isValidDomain("example..com"));
        assertFalse(Validator.isValidDomain("example.com/"));
    }

    @Test
    public void testInvalidPort() {
        // Проверка некорректного порта
        assertFalse(Validator.isValidPort("-1"));
        assertFalse(Validator.isValidPort("70000"));
        assertFalse(Validator.isValidPort("abc"));
    }
}