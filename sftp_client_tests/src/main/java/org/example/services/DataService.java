package org.example.services;

import org.example.client.SFTPClient;

public class DataService {
    private final SFTPClient sftpClient;

    public DataService(SFTPClient sftpClient) {
        this.sftpClient = sftpClient;
    }

    public String readFile(String remotePath) {
        return sftpClient.readFile(remotePath);
    }

    public boolean writeFile(String remotePath, String content) {
        return sftpClient.writeFile(remotePath, content);
    }
}