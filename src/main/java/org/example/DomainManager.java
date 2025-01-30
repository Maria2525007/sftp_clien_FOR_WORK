package org.example;

import java.util.*;

public class DomainManager {
    private final SFTPClient sftpClient;
    private final JsonHandler jsonHandler;

    public DomainManager(SFTPClient sftpClient) {
        this.sftpClient = sftpClient;
        this.jsonHandler = new JsonHandler();
    }

    public void printDomainAddressList() {
        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries != null) {
            entries.sort(DomainEntry::compareTo);
            entries.forEach(entry ->
                    System.out.println("Домен: " + entry.getDomain() + ", IP: " + entry.getIp()));
        }
    }

    public void getIpByDomain(Scanner scanner) {
        System.out.print("Введите доменное имя: ");
        String domain = scanner.nextLine();
        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries != null) {
            Optional<DomainEntry> result = entries.stream()
                    .filter(entry -> entry.getDomain().equals(domain))
                    .findFirst();
            if (result.isPresent()) {
                System.out.println("IP-адрес: " + result.get().getIp());
            } else {
                System.out.println("Домен не найден.");
            }
        }
    }

    public void getDomainByIp(Scanner scanner) {
        System.out.print("Введите IP-адрес: ");
        String ip = scanner.nextLine();
        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries != null) {
            Optional<DomainEntry> result = entries.stream()
                    .filter(entry -> entry.getIp().equals(ip))
                    .findFirst();
            if (result.isPresent()) {
                System.out.println("Домен: " + result.get().getDomain());
            } else {
                System.out.println("IP-адрес не найден.");
            }
        }
    }

    public void addDomainAddress(Scanner scanner) {
        System.out.print("Введите доменное имя: ");
        String domain = scanner.nextLine();
        System.out.print("Введите IP-адрес: ");
        String ip = scanner.nextLine();

        if (!Validator.isValidIp(ip)) {
            System.out.println("Некорректный IP-адрес.");
            return;
        }

        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries != null) {
            boolean domainExists = entries.stream().anyMatch(entry -> entry.getDomain().equals(domain));
            boolean ipExists = entries.stream().anyMatch(entry -> entry.getIp().equals(ip));

            if (domainExists || ipExists) {
                System.out.println("Домен или IP-адрес уже существуют.");
            } else {
                entries.add(new DomainEntry(domain, ip));
                sftpClient.writeFile(jsonHandler.toJson(entries));
                System.out.println("Пара 'домен – адрес' успешно добавлена.");
            }
        }
    }

    public void removeDomainAddress(Scanner scanner) {
        System.out.print("Введите доменное имя или IP-адрес для удаления: ");
        String input = scanner.nextLine();
        List<DomainEntry> entries = jsonHandler.parseJson(sftpClient.readFile());
        if (entries != null) {
            boolean removed = entries.removeIf(entry ->
                    entry.getDomain().equals(input) || entry.getIp().equals(input));
            if (removed) {
                sftpClient.writeFile(jsonHandler.toJson(entries));
                System.out.println("Пара 'домен – адрес' успешно удалена.");
            } else {
                System.out.println("Пара 'домен – адрес' не найдена.");
            }
        }
    }
}
