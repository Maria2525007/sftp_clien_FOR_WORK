package org.example.model;

import org.example.services.DataService;
import org.example.utils.JsonParser;

import java.util.*;

public class DomainStorage {
    private final DataService dataService;
    private final JsonParser jsonParser;
    private final List<DomainEntry> entries = new ArrayList<>();

    public DomainStorage(DataService dataService, JsonParser jsonParser) {
        this.dataService = dataService;
        this.jsonParser = jsonParser;
        loadData();
    }

    private void loadData() {
        String json = dataService.readFile("upload/domains.json");
        if (json != null && !json.trim().isEmpty()) {
            entries.clear();
            entries.addAll(jsonParser.parse(json));
            entries.sort(DomainEntry::compareTo);
        }
    }

    public void saveData() {
        String json = jsonParser.serialize(entries);
        dataService.writeFile("upload/domains.json", json);
    }

    public List<DomainEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(DomainEntry entry) {
        entries.add(entry);
        entries.sort(DomainEntry::compareTo);
        saveData();
    }

    public boolean removeEntryByDomain(String domain) {
        boolean removed = entries.removeIf(e -> e.getDomain().equalsIgnoreCase(domain));
        if (removed) saveData();
        return removed;
    }

    public boolean removeEntryByIp(String ip) {
        boolean removed = entries.removeIf(e -> e.getIp().equals(ip));
        if (removed) saveData();
        return removed;
    }

    public Optional<DomainEntry> findEntryByDomain(String domain) {
        return entries.stream()
                .filter(e -> e.getDomain().equalsIgnoreCase(domain))
                .findFirst();
    }

    public Optional<DomainEntry> findEntryByIp(String ip) {
        return entries.stream()
                .filter(e -> e.getIp().equals(ip))
                .findFirst();
    }
}