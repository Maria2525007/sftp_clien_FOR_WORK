package org.example;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.testng.Assert.*;

public class SFTPClientTest {
    private SFTPClient sftpClient;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeClass
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
        // Подключение к SFTP-серверу с корректными данными
        sftpClient = new SFTPClient("localhost", 22, "sftp_user", "test123");
    }

    @AfterClass
    public void tearDown() {
        if (sftpClient != null) {
            sftpClient.disconnect();
        }
    }

    @Test
    public void testSuccessfulConnection() {
        assertTrue(sftpClient.isConnected(), "Подключение к SFTP-серверу должно быть успешным.");
    }

    @Test(dependsOnMethods = "testSuccessfulConnection")
    public void testReadFile() {
        String content = sftpClient.readFile();
        assertNotNull(content, "Файл должен быть успешно прочитан.");
    }

    @Test(dependsOnMethods = "testReadFile")
    public void testWriteFile() {
        String testContent = "{\"addresses\":[]}";
        boolean result = sftpClient.writeFile(testContent);
        assertTrue(result, "Файл должен быть успешно записан.");
    }

    @Test
    public void testFailedConnection() {
        SFTPClient invalidClient = new SFTPClient("some.host", 22, "wrongUser", "wrongPassword");
        assertFalse(invalidClient.isConnected(), "Подключение с неверными данными должно завершиться ошибкой.");
    }

    @Test(dependsOnMethods = "testSuccessfulConnection")
    public void testReadEmptyFile() {
        sftpClient.writeFile("");

        String content = sftpClient.readFile();
        assertEquals(content, "", "Чтение пустого файла должно вернуть пустую строку.");
    }

    @Test(dependsOnMethods = "testSuccessfulConnection")
    public void testReadFileWithContent() {
        String testContent = "{\"addresses\":[]}";
        sftpClient.writeFile(testContent);

        String content = sftpClient.readFile();
        assertNotNull(content, "Чтение файла с данными не должно возвращать null.");
        assertEquals(content, testContent, "Содержимое файла должно совпадать с записанным.");
    }

}