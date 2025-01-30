package org.example;

import java.util.*;

public class JsonHandler {
    public List<DomainEntry> parseJson(String json) {
        List<DomainEntry> entries = new ArrayList<>();
        if (json == null || json.isEmpty()) return entries;

        json = json.replaceAll("[{}\\[\\]\"]", "").trim();
        String[] pairs = json.split("domain:|ip:");
        for (int i = 1; i < pairs.length; i += 2) {
            String domain = pairs[i].trim().replace(",", "");
            String ip = pairs[i + 1].trim().replace(",", "");
            entries.add(new DomainEntry(domain, ip));
        }
        return entries;
    }

    public String toJson(List<DomainEntry> entries) {
        StringBuilder json = new StringBuilder("{\"addresses\":[");
        for (DomainEntry entry : entries) {
            json.append("{\"domain\":\"").append(entry.getDomain()).append("\",\"ip\":\"").append(entry.getIp()).append("\"},");
        }
        if (!entries.isEmpty()) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("]}");
        return json.toString();
    }
}
