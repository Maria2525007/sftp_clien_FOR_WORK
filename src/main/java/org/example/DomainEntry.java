package org.example;

public class DomainEntry implements Comparable<DomainEntry> {
    private String domain;
    private String ip;

    public DomainEntry(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
    }

    public String getDomain() { return domain; }
    public String getIp() { return ip; }

    @Override
    public int compareTo(DomainEntry other) {
        return this.domain.compareTo(other.domain);
    }
}