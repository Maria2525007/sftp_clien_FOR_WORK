package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHandler {
    private static final Pattern ENTRY_PATTERN = Pattern.compile(
            "\"domain\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"ip\"\\s*:\\s*\"([^\"]+)\""
    );

    public List<DomainEntry> parseJson(String json) {
        List<DomainEntry> entries = new ArrayList<>();
        if (json == null || json.isEmpty()) return entries;

        String fixedJson = json.replaceAll(",\\s*}", "}");  // Убираем запятые перед закрывающей скобкой

        Matcher matcher = ENTRY_PATTERN.matcher(fixedJson);
        while (matcher.find()) {
            String domain = matcher.group(1).trim();
            String ip = matcher.group(2).trim();
            entries.add(new DomainEntry(domain, ip));
        }

        return entries;
    }


    public String toJson(List<DomainEntry> entries) {
        StringBuilder json = new StringBuilder("{\"addresses\":[");
        for (DomainEntry entry : entries) {
            json.append("{\"domain\":\"").append(entry.getDomain())
                    .append("\",\"ip\":\"").append(entry.getIp()).append("\"},");
        }
        if (!entries.isEmpty()) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("]}");
        return json.toString();
    }
}
