package org.example.utils;

import org.example.model.DomainEntry;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

public class JsonParserTest {
    private final JsonParser parser = new JsonParser();

    // Позитивные тесты

    @Test
    public void testParseValidJson() throws IOException {
        String json = readResourceFile("test_domains.json");
        List<DomainEntry> entries = parser.parse(json);

        assertEquals(entries.size(), 2);
        assertEquals(entries.get(0).getDomain(), "example.com");
        assertEquals(entries.get(0).getIp(), "192.168.1.1");
        assertEquals(entries.get(1).getDomain(), "test.org");
        assertEquals(entries.get(1).getIp(), "10.0.0.1");
    }

    @Test
    public void testParseEmptyJson() throws IOException {
        String json = readResourceFile("empty_domains.json");
        List<DomainEntry> entries = parser.parse(json);
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testSerializeValidData() {
        List<DomainEntry> entries = Arrays.asList(
                new DomainEntry("example.com", "192.168.1.1"),
                new DomainEntry("test.org", "10.0.0.1")
        );

        String json = parser.serialize(entries);
        assertTrue(json.contains("\"domain\":\"example.com\""));
        assertTrue(json.contains("\"ip\":\"192.168.1.1\""));
        assertTrue(json.contains("\"domain\":\"test.org\""));
        assertTrue(json.contains("\"ip\":\"10.0.0.1\""));
    }

    @Test
    public void testSerializeEmptyData() {
        List<DomainEntry> entries = Collections.emptyList();
        String json = parser.serialize(entries);
        assertEquals(json, "{\"addresses\":[]}");
    }

    // Негативные тесты

    @Test
    public void testParseCorruptedJson() throws IOException {
        String json = readResourceFile("broken_json.json");
        List<DomainEntry> entries = parser.parse(json);
        assertEquals(entries.size(), 0);
    }

    @Test
    public void testParseInvalidStructure() {
        String json = "{\"invalid_key\":[]}";
        List<DomainEntry> entries = parser.parse(json);
        assertTrue(entries.isEmpty());
    }

    // Вспомогательный метод для чтения файлов из resources
    private String readResourceFile(String fileName) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("src/test/resources/" + fileName));
        return new String(bytes);
    }
}