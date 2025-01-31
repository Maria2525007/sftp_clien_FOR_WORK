package org.example;

public class SFTPClientStub extends SFTPClient {
    private String fileContent;

    public SFTPClientStub(String fileContent) {
        super("stub", 22, "stub", "stub");
        this.fileContent = fileContent;
    }

    @Override
    public String readFile() {
        return fileContent;
    }

    @Override
    public boolean writeFile(String content) {
        this.fileContent = content;
        return true;
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}