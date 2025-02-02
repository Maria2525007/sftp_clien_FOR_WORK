package org.example.utils;

import org.example.model.DomainEntry;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public List<DomainEntry> parse(String json) {
        List<DomainEntry> entries = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) {
            return entries;
        }

        json = json.replaceAll("\\s+", "");

        int start = json.indexOf("\"addresses\":[");
        if (start == -1) return entries;
        start += 13; // Длина "\"addresses\":["

        int end = json.lastIndexOf("]");
        if (end <= start) return entries;

        String content = json.substring(start, end);

        String[] items = content.split("\\},\\{");
        for (String item : items) {
            item = item.replaceAll("[{}]", "");
            String[] pairs = item.split(",");
            String domain = null;
            String ip = null;

            for (String pair : pairs) {
                if (pair.startsWith("\"domain\":")) {
                    domain = pair.split(":")[1].replaceAll("\"", "");
                } else if (pair.startsWith("\"ip\":")) {
                    ip = pair.split(":")[1].replaceAll("\"", "");
                }
            }

            if (domain != null && ip != null) {
                entries.add(new DomainEntry(domain, ip));
            }
        }

        return entries;
    }

    public String serialize(List<DomainEntry> entries) {
        StringBuilder sb = new StringBuilder("{\"addresses\":[");
        for (DomainEntry entry : entries) {
            sb.append("{")
                    .append("\"domain\":\"").append(entry.getDomain()).append("\",")
                    .append("\"ip\":\"").append(entry.getIp()).append("\"},");
        }
        if (!entries.isEmpty()) sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        return sb.toString();
    }
}