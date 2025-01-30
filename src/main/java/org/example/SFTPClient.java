package org.example;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.*;

public class SFTPClient {
    private static final String SFTP_FILE = "upload/domains.json";
    private ChannelSftp sftpChannel;
    private Session session;

    public SFTPClient(String host, int port, String username, String password) {
        connect(host, port, username, password);
    }

    private void connect(String host, int port, String username, String password) {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
            System.out.println("Подключение к SFTP-серверу успешно.");
        } catch (JSchException e) {
            System.err.println("Ошибка подключения к SFTP-серверу: " + e.getMessage());
        }
    }

    public String readFile() {
        try (InputStream inputStream = sftpChannel.get(SFTP_FILE)) {
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (Exception e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return null;
        }
    }

    public void writeFile(String content) {
        try (OutputStream outputStream = sftpChannel.put(SFTP_FILE)) {
            outputStream.write(content.getBytes());
        } catch (Exception e) {
            System.err.println("Ошибка записи файла: " + e.getMessage());
        }
    }

    public void disconnect() {
        if (sftpChannel != null) sftpChannel.exit();
        if (session != null) session.disconnect();
        System.out.println("Отключение от SFTP-сервера.");
    }
}