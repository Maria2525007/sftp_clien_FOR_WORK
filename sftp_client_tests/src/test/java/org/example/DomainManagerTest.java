package org.example;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static org.testng.Assert.*;

public class DomainManagerTest {
    private DomainManager domainManager;

    @BeforeClass
    public void setUp() {
        // Используем заглушку для SFTPClient
        SFTPClientStub sftpClientStub = new SFTPClientStub("{\"addresses\":[]}");
        domainManager = new DomainManager(sftpClientStub);
    }

    @Test
    public void testAddDomainAddress() {
        // Подготовка входных данных
        String input = "example.com\n192.168.1.1";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Вызов метода
        domainManager.addDomainAddress(scanner);

        // Проверка результата
        List<DomainEntry> entries = domainManager.getEntries();
        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getDomain(), "example.com");
        assertEquals(entries.get(0).getIp(), "192.168.1.1");
    }

    @Test(dependsOnMethods = "testAddDomainAddress")
    public void testAddDuplicateDomain() {
        // Подготовка входных данных
        String input = "example.com\n192.168.1.2";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Вызов метода
        domainManager.addDomainAddress(scanner);

        // Проверка результата
        List<DomainEntry> entries = domainManager.getEntries();
        assertEquals(entries.size(), 1); // Должна остаться только одна запись
    }

    @Test(dependsOnMethods = "testAddDuplicateDomain")
    public void testGetIpByDomain() {
        // Подготовка входных данных
        String input = "example.com";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Вызов метода
        domainManager.getIpByDomain(scanner);

        // Проверка результата
        // Метод getIpByDomain выводит результат в консоль, поэтому здесь мы проверяем только отсутствие исключений
    }

    @Test(dependsOnMethods = "testGetIpByDomain")
    public void testGetDomainByIp() {
        // Подготовка входных данных
        String input = "192.168.1.1";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Вызов метода
        domainManager.getDomainByIp(scanner);

        // Проверка результата
        // Метод getDomainByIp выводит результат в консоль, поэтому здесь мы проверяем только отсутствие исключений
    }

    @Test(dependsOnMethods = "testGetDomainByIp")
    public void testRemoveDomainAddressByDomain() {
        // Подготовка входных данных
        String input = "example.com";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Вызов метода
        domainManager.removeDomainAddress(scanner);

        // Проверка результата
        List<DomainEntry> entries = domainManager.getEntries();
        assertEquals(entries.size(), 0); // Все записи должны быть удалены
    }

    @Test(dependsOnMethods = "testRemoveDomainAddressByDomain")
    public void testRemoveNonExistentDomainAddress() {
        // Подготовка входных данных
        String input = "nonexistent.com";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Вызов метода
        domainManager.removeDomainAddress(scanner);

        // Проверка результата
        List<DomainEntry> entries = domainManager.getEntries();
        assertEquals(entries.size(), 0); // Ничего не должно измениться
    }

    @Test(dependsOnMethods = "testRemoveNonExistentDomainAddress")
    public void testPrintDomainAddressList() {
        // Добавление тестовых данных
        String input = "test.com\n192.168.1.3";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);
        domainManager.addDomainAddress(scanner);

        // Вызов метода
        domainManager.printDomainAddressList();

        // Проверка результата
        // Метод printDomainAddressList выводит результат в консоль, поэтому здесь мы проверяем только отсутствие исключений
    }
}