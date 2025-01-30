package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите адрес SFTP-сервера: ");
        String host = scanner.nextLine();
        System.out.print("Введите порт: ");
        int port = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        SFTPClient sftpClient = new SFTPClient(host, port, username, password);
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
            int choice = Integer.parseInt(scanner.nextLine());

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
                    System.out.println("Неверный выбор.");
            }
        }

        sftpClient.disconnect();
    }
}