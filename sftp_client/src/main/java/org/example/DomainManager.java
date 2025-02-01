package org.example;

import java.util.*;

public class DomainManager {
    private final SFTPClient sftpClient;
    private final JsonHandler jsonHandler;

    public DomainManager(SFTPClient sftpClient) {
        this.sftpClient = sftpClient;
        this.jsonHandler = new JsonHandler();
    }

    // Получение списка данных
    List<DomainEntry> getEntries() {
        String json = sftpClient.readFile();
        if (json == null) {
            System.out.println("Ошибка чтения файла.");
            return new ArrayList<>();
        }
        return jsonHandler.parseJson(json);
    }

    // Получение списка пар "домен – адрес" из файла
    public void printDomainAddressList() {
        List<DomainEntry> entries = getEntries();
        // Сортируем записи по доменному имени
        entries.sort(Comparator.comparing(DomainEntry::getDomain));
        entries.forEach(entry ->
                System.out.println("Домен: " + entry.getDomain() + ", IP: " + entry.getIp()));
    }

    // Получение IP-адреса по доменному имени
    public String getIpByDomain(Scanner scanner) {
        System.out.print("Введите доменное имя: ");
        String domain = scanner.nextLine();
        List<DomainEntry> entries = getEntries();
        Optional<DomainEntry> result = entries.stream()
                .filter(entry -> entry.getDomain().equals(domain))
                .findFirst();
        if (result.isPresent()) {
            return result.get().getIp();
        } else {
            System.out.println("Домен не найден.");
            return null;
        }
    }

    // Получение доменного имени по IP-адресу
    public String getDomainByIp(Scanner scanner) {
        System.out.print("Введите IP-адрес: ");
        String ip = scanner.nextLine().trim();

        // Валидация IP перед поиском
        if (!Validator.isValidIp(ip)) {
            System.out.println("Некорректный IP-адрес. Формат: XXX.XXX.XXX.XXX (0-255).");
            return null;
        }
        //Поиск данных
        List<DomainEntry> entries = getEntries();
        Optional<DomainEntry> result = entries.stream()
                .filter(entry -> entry.getIp().equals(ip))
                .findFirst();
        if (result.isPresent()) {
            return result.get().getDomain();
        } else {
            System.out.println("IP-адрес не найден.");
            return null;
        }
    }

    // Добавление новой пары "домен – адрес" в файл
    public void addDomainAddress(Scanner scanner) {
        List<DomainEntry> entries = getEntries();

        System.out.print("Введите доменное имя: ");
        String domain = scanner.nextLine().trim();
        // Валидация домена
        if (!Validator.isValidDomain(domain)) {
            System.out.println("Некорректный домен. Пример: example.com");
            return;
        }
        // Проверка на уникальность домена
        boolean domainExists = entries.stream().anyMatch(e -> e.getDomain().equalsIgnoreCase(domain));
        if (domainExists) {
            System.out.println("Домен '" + domain + "' уже существует.");
            return;
        }

        System.out.print("Введите IP-адрес: ");
        String ip = scanner.nextLine().trim();
        // Валидация IP
        if (!Validator.isValidIp(ip)) {
            System.out.println("Некорректный IP-адрес. Формат Ipv4: XXX.XXX.XXX.XXX (0-255).");
            return;
        }
        // Проверка на уникальность IP
        boolean ipExists = entries.stream().anyMatch(e -> e.getIp().equals(ip));
        if (ipExists) {
            System.out.println("IP-адрес '" + ip + "' уже существует.");
            return;
        }

        entries.add(new DomainEntry(domain, ip));
        sftpClient.writeFile(jsonHandler.toJson(entries));
        System.out.println("Пара добавлена успешно.");
    }

    //Удаление пары "домен – адрес" по доменному имени или IP
    public void removeDomainAddress(Scanner scanner) {
        System.out.print("Введите доменное имя или IP-адрес: ");
        String input = scanner.nextLine().trim();
        List<DomainEntry> entries = getEntries();
        boolean isIp = Validator.isValidIp(input);
        boolean removed = entries.removeIf(entry ->
                (isIp && entry.getIp().equals(input)) ||
                        (!isIp && entry.getDomain().equalsIgnoreCase(input))
        );
        if (removed) {
            sftpClient.writeFile(jsonHandler.toJson(entries));
            System.out.println("Пара удалена успешно.");
        } else {
            System.out.println("Пара не найдена.");
        }
    }
}
