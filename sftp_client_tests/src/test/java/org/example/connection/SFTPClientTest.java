package org.example.connection;

import org.example.client.SFTPClientStub;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SFTPClientTest {
    private SFTPClientStub sftpClient;

    @BeforeMethod
    public void setUp() {
        sftpClient = new SFTPClientStub();
    }

    @AfterMethod
    public void tearDown() {
        sftpClient.disconnect();
    }

    // Позитивные тесты

    // Тест на подключение к серверу
    @Test
    public void testSuccessfulConnection() {
        Assert.assertTrue(sftpClient.isConnected());
    }

    // Тест на чтение файла
    @Test
    public void testReadFileSuccess() {
        String content = sftpClient.readFile("/path/file.txt");
        Assert.assertNotNull(content);
        Assert.assertEquals(content, "Test file content");
    }
    // Тест на запись файла
    @Test
    public void testWriteFileSuccess() {
        boolean result = sftpClient.writeFile("/path/newfile.txt", "New content");
        Assert.assertTrue(result);
        Assert.assertEquals(sftpClient.readFile("/path/newfile.txt"), "New content");
    }

    // Негативные тесты

    // Тест на ошибку подключения
    @Test
    public void testFailedConnection() {
        sftpClient = new SFTPClientStub(false);
        Assert.assertFalse(sftpClient.isConnected());
    }

    // Тест на чтение файла после отключения от сервера
    @Test
    public void testReadFileWithoutConnection() {
        sftpClient.disconnect();
        String content = sftpClient.readFile("/path/file.txt");
        Assert.assertNull(content);
    }

    // Тест на чтение несуществующего файла
    @Test
    public void testReadFileNotFound() {
        String content = sftpClient.readFile("/path/infile.txt");
        Assert.assertNull(content);
    }
}
