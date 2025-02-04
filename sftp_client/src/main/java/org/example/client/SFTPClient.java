package org.example.client;

import com.jcraft.jsch.*;
import java.io.*;

public class SFTPClient {
    private ChannelSftp sftpChannel;
    private Session session;
    private boolean isConnected = false;

    public SFTPClient(String host, int port, String user, String password) {
        connect(host, port, user, password);
    }

    private void connect(String host, int port, String user, String password) {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
            isConnected = true;
            System.out.println("Подключение к SFTP-серверу успешно.");
        } catch (JSchException e) {
            if (e.getMessage().contains("Auth fail")) {
                System.err.println("Неверный логин/пароль");
            }
            isConnected = false;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String readFile(String remotePath) {
        try (InputStream is = sftpChannel.get(remotePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (Exception e) {
            return null;
        }
    }


    public boolean writeFile(String remotePath, String content) {
        try (OutputStream os = sftpChannel.put(remotePath)) {
            os.write(content.getBytes());
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка записи файла: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public void disconnect() {
        if (sftpChannel != null) sftpChannel.disconnect();
        if (session != null) session.disconnect();
    }
}