package org.example.validation;

import org.example.services.ValidationService;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ValidatorTest {

    // Позитивные тесты

    @Test
    public void testValidIp() {
        // Проверка корректного IPv4
        assertTrue(ValidationService.isValidIp("192.168.1.1"));
        assertTrue(ValidationService.isValidIp("10.0.0.1"));
        assertTrue(ValidationService.isValidIp("255.255.255.255"));
    }

    @Test
    public void testValidDomain() {
        // Проверка корректного домена
        assertTrue(ValidationService.isValidDomain("example.com"));
        assertTrue(ValidationService.isValidDomain("sub.domain.co.uk"));
        assertTrue(ValidationService.isValidDomain("my-domain123.com"));
    }

    @Test
    public void testValidPort() {
        // Проверка корректного порта
        assertTrue(ValidationService.isValidPort("22"));
        assertTrue(ValidationService.isValidPort("8080"));
        assertTrue(ValidationService.isValidPort("65535"));
    }

    // Негативные тесты

    @Test
    public void testInvalidIp() {
        // Проверка некорректного IPv4
        assertFalse(ValidationService.isValidIp("256.0.0.1"));
        assertFalse(ValidationService.isValidIp("192.168.1"));
        assertFalse(ValidationService.isValidIp("abc.def.ghi.jkl"));
    }

    @Test
    public void testInvalidDomain() {
        // Проверка некорректного домена
        assertFalse(ValidationService.isValidDomain("-example.com"));
        assertFalse(ValidationService.isValidDomain("example..com"));
        assertFalse(ValidationService.isValidDomain("example.com/"));
    }

    @Test
    public void testInvalidPort() {
        // Проверка некорректного порта
        assertFalse(ValidationService.isValidPort("-1"));
        assertFalse(ValidationService.isValidPort("70000"));
        assertFalse(ValidationService.isValidPort("abc"));
    }
}