package org.example;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
import java.util.*;

public class JsonHandlerTest {

    // Позитивные тесты

    @Test
    public void testParseValidJson() {
        // Проверка парсинга корректного JSON
        String json = "{\"addresses\":[{\"domain\":\"example.com\",\"ip\":\"192.168.1.1\"},{\"domain\":\"sub.domain.co.uk\",\"ip\":\"10.0.0.1\"}]}";
        JsonHandler jsonHandler = new JsonHandler();

        List<DomainEntry> entries = jsonHandler.parseJson(json);
        assertEquals(entries.size(), 2, "Должно быть два элемента.");

        DomainEntry entry1 = entries.get(0);
        assertEquals(entry1.getDomain(), "example.com", "Домен должен быть example.com");
        assertEquals(entry1.getIp(), "192.168.1.1", "IP должен быть 192.168.1.1");

        DomainEntry entry2 = entries.get(1);
        assertEquals(entry2.getDomain(), "sub.domain.co.uk", "Домен должен быть sub.domain.co.uk");
        assertEquals(entry2.getIp(), "10.0.0.1", "IP должен быть 10.0.0.1");
    }

    @Test
    public void testParseEmptyJson() {
        // Проверка парсинга пустого JSON
        String json = "{\"addresses\":[]}";
        JsonHandler jsonHandler = new JsonHandler();

        List<DomainEntry> entries = jsonHandler.parseJson(json);
        assertTrue(entries.isEmpty(), "Список должен быть пустым.");
    }

    @Test
    public void testToJson() {
        // Проверка генерации JSON из списка
        List<DomainEntry> entries = new ArrayList<>();
        entries.add(new DomainEntry("example.com", "192.168.1.1"));
        entries.add(new DomainEntry("sub.domain.co.uk", "10.0.0.1"));

        JsonHandler jsonHandler = new JsonHandler();
        String json = jsonHandler.toJson(entries);

        String expectedJson = "{\"addresses\":[{\"domain\":\"example.com\",\"ip\":\"192.168.1.1\"},{\"domain\":\"sub.domain.co.uk\",\"ip\":\"10.0.0.1\"}]}";
        assertEquals(json, expectedJson, "JSON должен быть сгенерирован корректно.");
    }

    @Test
    public void testToJsonEmptyList() {
        // Проверка генерации JSON из пустого списка
        List<DomainEntry> entries = new ArrayList<>();

        JsonHandler jsonHandler = new JsonHandler();
        String json = jsonHandler.toJson(entries);

        String expectedJson = "{\"addresses\":[]}";
        assertEquals(json, expectedJson, "JSON для пустого списка должен быть пустым.");
    }

    @Test
    public void testParseJsonWithExtraComma() {

        // Проверка парсинга JSON с лишней запятой в конце
        String json = "{\"addresses\":[{\"domain\":\"example.com\",\"ip\":\"192.168.1.1\"},]}";
        JsonHandler jsonHandler = new JsonHandler();
        List<DomainEntry> entries = jsonHandler.parseJson(json);
        assertFalse(entries.isEmpty(), "Список не должен быть пустым.");

        // Проверяем, что данные были извлечены правильно
        assertEquals(entries.size(), 1, "Должен быть извлечен один элемент.");
        assertEquals(entries.get(0).getDomain(), "example.com", "Домен должен быть 'example.com'.");
        assertEquals(entries.get(0).getIp(), "192.168.1.1", "IP-адрес должен быть '192.168.1.1'.");
    }

    // Негативные тесты

    @Test
    public void testParseInvalidJson() {
        // Проверка парсинга повреждённого JSON (отсутствие кавычек)
        String json = "{\"addresses\":[{\"domain\":example.com,\"ip\":\"192.168.1.1\"}]}";
        JsonHandler jsonHandler = new JsonHandler();

        List<DomainEntry> entries = jsonHandler.parseJson(json);
        assertTrue(entries.isEmpty(), "Список должен быть пустым при поврежденном JSON.");
    }


}
