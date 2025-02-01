package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHandler {
    private static final Pattern ENTRY_PATTERN = Pattern.compile(
            "\"domain\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"ip\"\\s*:\\s*\"([^\"]+)\""
    );

    public List<DomainEntry> parseJson(String json) throws IllegalArgumentException {
        List<DomainEntry> entries = new ArrayList<>();
        Matcher matcher = getMatcher(json);
        while (matcher.find()) {
            String domain = matcher.group(1).trim();
            String ip = matcher.group(2).trim();
            entries.add(new DomainEntry(domain, ip));
        }

        // Если JSON не пустой, но entries пуст, значит, JSON некорректен
        if (!json.trim().equals("{\"addresses\":[]}") && entries.isEmpty()) {
            throw new IllegalArgumentException("Некорректный JSON: не удалось извлечь данные.");
        }

        return entries;
    }

    private static Matcher getMatcher(String json) {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("JSON не может быть пустым или null.");
        }

        // Проверяем, что JSON содержит ключ "addresses"
        if (!json.contains("\"addresses\"")) {
            throw new IllegalArgumentException("Некорректный JSON: отсутствует ключ 'addresses'.");
        }

        // Убираем лишние запятые перед закрывающей скобкой
        String fixedJson = json.replaceAll(",\\s*}", "}");

        return ENTRY_PATTERN.matcher(fixedJson);
    }

    public String toJson(List<DomainEntry> entries) {
        if (entries == null) {
            throw new IllegalArgumentException("Список entries не может быть null.");
        }

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