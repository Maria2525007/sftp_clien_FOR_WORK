package org.example.client;

import java.util.HashMap;
import java.util.Map;

public class SFTPClientStub {
    private boolean isConnected;
    private Map<String, String> fileStorage = new HashMap<>();

    public SFTPClientStub() {
        this.isConnected = true;
        fileStorage.put("/path/file.txt", "Test file content");
    }

    public SFTPClientStub(boolean shouldConnect) {
        this.isConnected = shouldConnect;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String readFile(String remotePath) {
        if (!isConnected) return null;
        return fileStorage.get(remotePath);
    }

    public boolean writeFile(String remotePath, String content) {
        if (!isConnected) return false;
        fileStorage.put(remotePath, content);
        return true;
    }

    public void disconnect() {
        isConnected = false;
    }
}
