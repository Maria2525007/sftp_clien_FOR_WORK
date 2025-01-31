package org.example;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SFTPClientTest {
    private SFTPClient sftpClient;

    @BeforeClass
    public void setUp() {
        // Подключение к SFTP-серверу с корректными данными
        sftpClient = new SFTPClient("localhost", 22, "sftp_user", "test123"); // Замените на реальные данные
    }

    @AfterClass
    public void tearDown() {
        if (sftpClient != null) {
            sftpClient.disconnect();
        }
    }

    @Test
    public void testSuccessfulConnection() {
        // Проверка успешного подключения
        assertTrue(sftpClient.isConnected(), "Подключение к SFTP-серверу должно быть успешным.");
    }

    @Test(dependsOnMethods = "testSuccessfulConnection")
    public void testReadFile() {
        // Проверка чтения файла
        String content = sftpClient.readFile();
        assertNotNull(content, "Файл должен быть успешно прочитан.");
    }

    @Test(dependsOnMethods = "testReadFile")
    public void testWriteFile() {
        // Проверка записи файла
        String testContent = "{\"addresses\":[]}";
        boolean result = sftpClient.writeFile(testContent);
        assertTrue(result, "Файл должен быть успешно записан.");
    }

    @Test
    public void testFailedConnection() {
        // Проверка ошибки подключения (неверные данные)
        SFTPClient invalidClient = new SFTPClient("invalid.host", 22, "wrongUser", "wrongPassword");
        assertFalse(invalidClient.isConnected(), "Подключение с неверными данными должно завершиться ошибкой.");
    }

    @Test(dependsOnMethods = "testWriteFile")
    public void testReadNonExistentFile() {
        // Проверка чтения несуществующего файла domains.json
        SFTPClient tempClient = new SFTPClient("localhost", 22, "user", "password"); // Замените на реальные данные
        String content = tempClient.readFile();
        assertNull(content, "Чтение несуществующего файла должно вернуть null.");
    }
}