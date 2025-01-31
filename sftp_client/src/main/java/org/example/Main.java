package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод адреса SFTP-сервера (может быть IPv4 или доменным именем)
        System.out.print("Введите адрес SFTP-сервера: ");
        String host = scanner.nextLine();

        // Ввод и валидация порта
        String portInput;
        int port;
        while (true) {
            System.out.print("Введите порт: ");
            portInput = scanner.nextLine();
            if (Validator.isValidPort(portInput)) {
                port = Integer.parseInt(portInput);
                break;
            } else {
                System.out.println("Некорректный порт. Порт должен быть числом от 0 до 65535.");
            }
        }

        // Ввод логина и пароля
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        // Подключение к SFTP-серверу
        SFTPClient sftpClient = new SFTPClient(host, port, username, password);

        // Проверка успешности подключения
        if (sftpClient.isConnected()) {
            DomainManager domainManager = new DomainManager(sftpClient);

            boolean running = true;
            while (running) {
                System.out.println("\nВыберите действие:");
                System.out.println("1. Получить список пар 'домен – адрес'");
                System.out.println("2. Получить IP-адрес по доменному имени");
                System.out.println("3. Получить доменное имя по IP-адресу");
                System.out.println("4. Добавить новую пару 'домен – адрес'");
                System.out.println("5. Удалить пару 'домен – адрес'");
                System.out.println("6. Завершить работу");
                String choiceInput = scanner.nextLine();

                try {
                    int choice = Integer.parseInt(choiceInput);
                    switch (choice) {
                        case 1:
                            domainManager.printDomainAddressList();
                            break;
                        case 2:
                            domainManager.getIpByDomain(scanner);
                            break;
                        case 3:
                            domainManager.getDomainByIp(scanner);
                            break;
                        case 4:
                            domainManager.addDomainAddress(scanner);
                            break;
                        case 5:
                            domainManager.removeDomainAddress(scanner);
                            break;
                        case 6:
                            running = false;
                            break;
                        default:
                            System.out.println("Неверный ввод. Введите число от 1 до 6.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Неверный ввод. Введите число от 1 до 6.");
                }
            }

            sftpClient.disconnect();
        }
    }
}