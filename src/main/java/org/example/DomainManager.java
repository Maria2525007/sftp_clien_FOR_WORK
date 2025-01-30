package org.example;

import java.util.*;

public class DomainManager {
    private final SFTPClient sftpClient;
    private final JsonHandler jsonHandler;

    public DomainManager(SFTPClient sftpClient) {
        this.sftpClient = sftpClient;
        this.jsonHandler = new JsonHandler();
    }

    // Получение списка пар "домен – адрес" из файла
    public void printDomainAddressList() {
        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries == null) {
            System.out.println("Ошибка чтения данных.");
            return;
        }
        entries.sort(DomainEntry::compareTo);
        entries.forEach(entry ->
                System.out.println("Домен: " + entry.getDomain() + ", IP: " + entry.getIp()));

    }

    // Получение IP-адреса по доменному имени
    public void getIpByDomain(Scanner scanner) {
        System.out.print("Введите доменное имя: ");
        String domain = scanner.nextLine();
        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries == null) {
            System.out.println("Ошибка чтения данных.");
            return;
        }
        Optional<DomainEntry> result = entries.stream()
                .filter(entry -> entry.getDomain().equals(domain))
                .findFirst();
        if (result.isPresent()) {
            System.out.println("IP-адрес: " + result.get().getIp());
        } else {
            System.out.println("Домен не найден.");
        }
    }

    // Получение доменного имени по IP-адресу
    public void getDomainByIp(Scanner scanner) {
        System.out.print("Введите IP-адрес: ");
        String ip = scanner.nextLine().trim();
        // Валидация IP перед поиском
        if (!Validator.isValidIp(ip)) {
            System.out.println("Некорректный IP-адрес. Формат: XXX.XXX.XXX.XXX (0-255).");
            return;
        }
        //Поиск данных
        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries == null) {
            System.out.println("Ошибка чтения данных.");
            return;
        }
        Optional<DomainEntry> result = entries.stream()
                .filter(entry -> entry.getIp().equals(ip))
                .findFirst();
        if (result.isPresent()) {
            System.out.println("Домен: " + result.get().getDomain());
        } else {
            System.out.println("IP-адрес не найден.");
        }
    }

    // Добавление новой пары "домен – адрес" в файл
    public void addDomainAddress(Scanner scanner) {
        System.out.print("Введите доменное имя: ");
        String domain = scanner.nextLine().trim();
        System.out.print("Введите IP-адрес: ");
        String ip = scanner.nextLine().trim();
        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries == null) {
            System.out.println("Ошибка чтения данных.");
            return;
        }
        // Валидация IP
        if (!Validator.isValidIp(ip)) {
            System.out.println("Некорректный IP-адрес. Формат Ipv4: XXX.XXX.XXX.XXX (0-255).");
            return;
        }
        // Проверка на уникальность домена или IP
        boolean domainExists = entries.stream().anyMatch(e -> e.getDomain().equalsIgnoreCase(domain));
        boolean ipExists = entries.stream().anyMatch(e -> e.getIp().equals(ip));

        if (domainExists) {
            System.out.println("Домен '" + domain + "' уже существует.");
            return;
        }
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
        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries == null) {
            System.out.println("Ошибка чтения данных.");
            return;
        }
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
