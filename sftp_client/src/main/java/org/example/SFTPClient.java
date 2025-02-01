package org.example;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.*;

public class SFTPClient {
    private static final String SFTP_FILE = "upload/domains.json";
    private ChannelSftp sftpChannel;
    private Session session;
    private boolean isConnected = false;

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
            isConnected = true;
            System.out.println("Подключение к SFTP-серверу успешно.");

        } catch (JSchException e) {
            isConnected = false;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String readFile() {
        try {
            InputStream inputStream = sftpChannel.get(SFTP_FILE);
            if (inputStream == null) {
                return null;
            }
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return null;
            }
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return null;
        }
    }



    public boolean writeFile(String content) {
        try (OutputStream outputStream = sftpChannel.put(SFTP_FILE)) {
            outputStream.write(content.getBytes());
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка записи файла: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        if (sftpChannel != null) sftpChannel.exit();
        if (session != null) session.disconnect();
        System.out.println("Отключение от SFTP-сервера.");
    }
}