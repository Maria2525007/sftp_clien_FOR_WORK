package org.example;

import org.example.model.DomainEntry;
import org.example.utils.JsonHandler;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import java.util.*;

public class JsonHandlerTest {

    // Позитивные тесты

    @Test
    public void testParseValidJson() {
        String json = "{\"addresses\":[{\"domain\":\"example.com\",\"ip\":\"192.168.1.1\"},{\"domain\":\"sub.domain.co.uk\",\"ip\":\"10.0.0.1\"}]}";
        JsonHandler jsonHandler = new JsonHandler();

        List<DomainEntry> entries = jsonHandler.parseJson(json);
        assertEquals(entries.size(), 2);

        DomainEntry entry1 = entries.get(0);
        assertEquals(entry1.getDomain(), "example.com");
        assertEquals(entry1.getIp(), "192.168.1.1");

        DomainEntry entry2 = entries.get(1);
        assertEquals(entry2.getDomain(), "sub.domain.co.uk");
        assertEquals(entry2.getIp(), "10.0.0.1");
    }

    @Test
    public void testParseEmptyJson() {
        String json = "{\"addresses\":[]}";
        JsonHandler jsonHandler = new JsonHandler();

        List<DomainEntry> entries = jsonHandler.parseJson(json);
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testToJson() {
        List<DomainEntry> entries = new ArrayList<>();
        entries.add(new DomainEntry("example.com", "192.168.1.1"));
        entries.add(new DomainEntry("sub.domain.co.uk", "10.0.0.1"));

        JsonHandler jsonHandler = new JsonHandler();
        String json = jsonHandler.toJson(entries);

        String expectedJson = "{\"addresses\":[{\"domain\":\"example.com\",\"ip\":\"192.168.1.1\"},{\"domain\":\"sub.domain.co.uk\",\"ip\":\"10.0.0.1\"}]}";
        assertEquals(json, expectedJson);
    }

    @Test
    public void testToJsonEmptyList() {
        List<DomainEntry> entries = new ArrayList<>();

        JsonHandler jsonHandler = new JsonHandler();
        String json = jsonHandler.toJson(entries);

        String expectedJson = "{\"addresses\":[]}";
        assertEquals(json, expectedJson);
    }

    @Test
    public void testParseJsonWithExtraComma() {
        String json = "{\"addresses\":[{\"domain\":\"example.com\",\"ip\":\"192.168.1.1\"},]}";
        JsonHandler jsonHandler = new JsonHandler();
        List<DomainEntry> entries = jsonHandler.parseJson(json);

        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getDomain(), "example.com");
        assertEquals(entries.get(0).getIp(), "192.168.1.1");
    }

    // Негативные тесты

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseInvalidJson() {
        String json = "\"addresses\":\"domain\":example.com,\"ip\":\"192.168.1.1\"";
        JsonHandler jsonHandler = new JsonHandler();
        jsonHandler.parseJson(json);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseNullJson() {
        String json = null;
        JsonHandler jsonHandler = new JsonHandler();
        jsonHandler.parseJson(json);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseJsonMissingAddresses() {
        String json = "{\"domains\":[{\"domain\":\"example.com\",\"ip\":\"192.168.1.1\"}]}";
        JsonHandler jsonHandler = new JsonHandler();
        jsonHandler.parseJson(json);
    }
}