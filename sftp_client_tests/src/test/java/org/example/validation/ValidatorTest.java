package org.example.validation;

import org.example.services.ValidationService;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ValidatorTest {

    // Позитивные тесты

    // Проверка корректного IPv4
    @Test
    public void testValidIp() {
        assertTrue(ValidationService.isValidIp("192.168.1.1"));
        assertTrue(ValidationService.isValidIp("10.0.0.1"));
        assertTrue(ValidationService.isValidIp("255.255.255.255"));
    }

    // Проверка корректного домена
    @Test
    public void testValidDomain() {
        assertTrue(ValidationService.isValidDomain("example.com"));
        assertTrue(ValidationService.isValidDomain("sub.domain.co.uk"));
        assertTrue(ValidationService.isValidDomain("my-domain123.com"));
    }

    // Проверка корректного порта
    @Test
    public void testValidPort() {
        assertTrue(ValidationService.isValidPort("22"));
        assertTrue(ValidationService.isValidPort("8080"));
        assertTrue(ValidationService.isValidPort("65535"));
    }

    // Негативные тесты

    // Проверка некорректного IPv4
    @Test
    public void testInvalidIp() {
        assertFalse(ValidationService.isValidIp("256.0.0.1"));
        assertFalse(ValidationService.isValidIp("192.168.1"));
        assertFalse(ValidationService.isValidIp("abc.def.ghi.jkl"));
    }

    // Проверка некорректного домена
    @Test
    public void testInvalidDomain() {
        assertFalse(ValidationService.isValidDomain("-example.com"));
        assertFalse(ValidationService.isValidDomain("example..com"));
        assertFalse(ValidationService.isValidDomain("example.com/"));
    }

    // Проверка некорректного порта
    @Test
    public void testInvalidPort() {
        assertFalse(ValidationService.isValidPort("-1"));
        assertFalse(ValidationService.isValidPort("70000"));
        assertFalse(ValidationService.isValidPort("abc"));
    }
}