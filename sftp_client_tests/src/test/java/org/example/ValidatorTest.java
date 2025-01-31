package org.example;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ValidatorTest {

    // Позитивные тесты

    @Test
    public void testValidIp() {
        // Проверка корректного IPv4
        assertTrue(Validator.isValidIp("192.168.1.1"), "Корректный IPv4 должен пройти валидацию.");
        assertTrue(Validator.isValidIp("10.0.0.1"), "Корректный IPv4 должен пройти валидацию.");
        assertTrue(Validator.isValidIp("255.255.255.255"), "Корректный IPv4 должен пройти валидацию.");
    }

    @Test
    public void testValidDomain() {
        // Проверка корректного домена
        assertTrue(Validator.isValidDomain("example.com"), "Корректный домен должен пройти валидацию.");
        assertTrue(Validator.isValidDomain("sub.domain.co.uk"), "Корректный домен должен пройти валидацию.");
        assertTrue(Validator.isValidDomain("my-domain123.com"), "Корректный домен должен пройти валидацию.");
    }

    @Test
    public void testValidPort() {
        // Проверка корректного порта
        assertTrue(Validator.isValidPort("22"), "Корректный порт должен пройти валидацию.");
        assertTrue(Validator.isValidPort("8080"), "Корректный порт должен пройти валидацию.");
        assertTrue(Validator.isValidPort("65535"), "Корректный порт должен пройти валидацию.");
    }

    // Негативные тесты

    @Test
    public void testInvalidIp() {
        // Проверка некорректного IPv4
        assertFalse(Validator.isValidIp("256.0.0.1"), "Некорректный IPv4 не должен проходить валидацию.");
        assertFalse(Validator.isValidIp("192.168.1"), "Некорректный IPv4 не должен проходить валидацию.");
        assertFalse(Validator.isValidIp("abc.def.ghi.jkl"), "Некорректный IPv4 не должен проходить валидацию.");
    }

    @Test
    public void testInvalidDomain() {
        // Проверка некорректного домена
        assertFalse(Validator.isValidDomain("-example.com"), "Некорректный домен не должен проходить валидацию.");
        assertFalse(Validator.isValidDomain("example..com"), "Некорректный домен не должен проходить валидацию.");
        assertFalse(Validator.isValidDomain("example.com/"), "Некорректный домен не должен проходить валидацию.");
    }

    @Test
    public void testInvalidPort() {
        // Проверка некорректного порта
        assertFalse(Validator.isValidPort("-1"), "Некорректный порт не должен проходить валидацию.");
        assertFalse(Validator.isValidPort("70000"), "Некорректный порт не должен проходить валидацию.");
        assertFalse(Validator.isValidPort("abc"), "Некорректный порт не должен проходить валидацию.");
    }
}