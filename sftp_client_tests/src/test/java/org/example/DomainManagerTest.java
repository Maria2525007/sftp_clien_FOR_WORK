package org.example;

import org.testng.annotations.*;

import java.io.*;
import java.util.*;

import static org.testng.Assert.*;

public class DomainManagerTest {
    private DomainManager domainManager;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;


    @BeforeClass
    public void setUp() {
        // Используем заглушку для SFTPClient с пустым JSON
        SFTPClientStub sftpClientStub = new SFTPClientStub("{\"addresses\":[]}");
        domainManager = new DomainManager(sftpClientStub);
    }

    @BeforeMethod
    public void resetData() {
        // Очищаем данные перед каждым тестом
        SFTPClientStub sftpClientStub = new SFTPClientStub("{\"addresses\":[]}");
        domainManager = new DomainManager(sftpClientStub);
    }

    @BeforeMethod
    public void setUpStreams() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterMethod
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // Вспомогательные методы

    private void addDomainEntry(String domain, String ip) {
        String input = domain + "\n" + ip;
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);
        domainManager.addDomainAddress(scanner);
    }

    private void removeDomainEntry(String input) {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);
        domainManager.removeDomainAddress(scanner);
    }

    // Позитивные сценарии

    @Test
    public void testAddDomainAddress() {
        // Ввод корректных данных
        addDomainEntry("example.com", "192.168.1.1");
        List<DomainEntry> entries = domainManager.getEntries();

        // Проверяем, что запись добавлена
        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getDomain(), "example.com");
        assertEquals(entries.get(0).getIp(), "192.168.1.1");
    }

    @Test
    public void testAddDuplicateDomain() {
        // Ввод дубликата домена
        addDomainEntry("example.com", "192.168.1.1");
        addDomainEntry("example.com", "192.168.1.2");
        List<DomainEntry> entries = domainManager.getEntries();

        // Проверяем, что дубликат не добавлен
        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getIp(), "192.168.1.1");
    }

    @Test
    public void testAddDuplicateIp() {
        // Ввод дубликата IP
        addDomainEntry("example.com", "192.168.1.1");
        addDomainEntry("example2.com", "192.168.1.1");
        List<DomainEntry> entries = domainManager.getEntries();

        // Проверяем, что дубликат не добавлен
        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getDomain(), "example.com");
    }

    @Test
    public void testGetIpByDomain() {
        // Добавляем запись
        addDomainEntry("example.com", "192.168.1.1");

        // Поиск IP по домену
        String input = "example.com";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        String ip = domainManager.getIpByDomain(scanner);

        // Проверяем, что IP найден
        assertEquals(ip, "192.168.1.1");
    }

    @Test
    public void testGetDomainByIp() {
        // Добавляем запись
        addDomainEntry("example.com", "192.168.1.1");

        // Поиск домена по IP
        String input = "192.168.1.1";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        String domain = domainManager.getDomainByIp(scanner);

        // Проверяем, что домен найден
        assertEquals(domain, "example.com");
    }

    @Test
    public void testRemoveDomainAddressByDomain() {
        // Добавляем запись
        addDomainEntry("example.com", "192.168.1.1");

        // Удаление по домену
        removeDomainEntry("example.com");
        List<DomainEntry> entries = domainManager.getEntries();

        // Проверяем, что запись удалена
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testRemoveDomainAddressByIp() {
        // Добавляем запись
        addDomainEntry("example.com", "192.168.1.1");

        // Удаление по IP
        removeDomainEntry("192.168.1.1");
        List<DomainEntry> entries = domainManager.getEntries();

        // Проверяем, что запись удалена
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testRemoveNonExistentDomainAddress() {
        // Удаление по несуществующему домену
        removeDomainEntry("nonexistent.com");
        List<DomainEntry> entries = domainManager.getEntries();

        // Проверяем, что список остался пустым
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testRemoveNonExistentIp() {
        // Удаление по несуществующему IP
        removeDomainEntry("192.168.1.99");
        List<DomainEntry> entries = domainManager.getEntries();

        // Проверяем, что список остался пустым
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testPrintDomainAddressList() {
        // Добавляем несколько записей
        addDomainEntry("test.com", "192.168.1.3");
        addDomainEntry("example.org", "192.168.1.2");

        // Перехватываем вывод в консоль
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out; // Сохраняем оригинальный System.out
        System.setOut(printStream);

        // Вызываем метод, который тестируем
        domainManager.printDomainAddressList();

        // Восстанавливаем оригинальный System.out
        System.setOut(originalOut);

        // Получаем вывод в консоль
        String consoleOutput = outputStream.toString().trim();

        // Разделяем вывод на строки
        String[] lines = consoleOutput.split(System.lineSeparator());

        // Проверяем, что вывод содержит две строки
        assertEquals(lines.length, 2);

        // Проверяем, что записи отсортированы по алфавиту по домену
        assertEquals(lines[0], "Домен: example.org, IP: 192.168.1.2");
        assertEquals(lines[1], "Домен: test.com, IP: 192.168.1.3");
    }


    // Негативные сценарии

    @Test
    public void testAddInvalidDomain() {
        // Ввод некорректного домена
        addDomainEntry("invalid_domain", "192.168.1.1");
        List<DomainEntry> entries = domainManager.getEntries();

        // Проверяем, что домен не добавлен
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testAddInvalidIp() {
        // Ввод некорректного IP
        addDomainEntry("example.com", "invalid_ip");
        List<DomainEntry> entries = domainManager.getEntries();

        // Проверяем, что IP не добавлен
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testGetIpByNonExistentDomain() {
        // Поиск IP по несуществующему домену
        String input = "nonexistent.com";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        String ip = domainManager.getIpByDomain(scanner);

        // Проверяем, что IP не найден
        assertNull(ip);
    }

    @Test
    public void testGetDomainByNonExistentIp() {
        // Поиск домена по несуществующему IP
        String input = "192.168.1.99";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        String domain = domainManager.getDomainByIp(scanner);

        // Проверяем, что домен не найден
        assertNull(domain);
    }
}