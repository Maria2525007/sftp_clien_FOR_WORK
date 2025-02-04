package org.example;

import org.example.client.SFTPClient;
import org.example.commands.*;
import org.example.model.DomainStorage;
import org.example.services.*;
import org.example.utils.*;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод данных подключения
        System.out.println("=== SFTP Client ===");
        System.out.print("Введите хост: ");
        String host = scanner.nextLine();

        // Ввод и валидация порта
        String portInput;
        int port;
        while (true) {
            System.out.print("Введите порт: ");
            portInput = scanner.nextLine();
            if (ValidationService.isValidPort(portInput)) {
                port = Integer.parseInt(portInput);
                break;
            } else {
                System.out.println("Некорректный порт. Порт должен быть числом от 0 до 65535.");
            }
        }

        System.out.print("Введите логин: ");
        String user = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        // Инициализация клиента
        SFTPClient sftpClient = new SFTPClient(host, port, user, password);
        if (!sftpClient.isConnected()) {
            System.out.println("Ошибка подключения к SFTP");
            return;
        }

        // Инициализация сервисов
        DataService dataService = new DataService(sftpClient);
        JsonParser jsonParser = new JsonParser();
        DomainStorage storage = new DomainStorage(dataService, jsonParser);
        ValidationService validator = new ValidationService();

        // Проверка наличия файла с данными
        String fileContent = sftpClient.readFile("domains.json");
        if (fileContent == null || fileContent.isEmpty()) {
            System.err.println("Ошибка: не удалось прочитать файл. Завершение работы.");
            sftpClient.disconnect();
            return;
        }

        // Регистрация команд
        CommandHandler handler = new CommandHandler()
                .register("1", new ListCommand(storage))
                .register("2", new GetIpCommand(storage))
                .register("3", new GetDomainCommand(storage, validator))
                .register("4", new AddCommand(storage, validator))
                .register("5", new RemoveCommand(storage, validator))
                .register("6", new ExitCommand());

        // Запуск
        new InputHandler(handler).start();
    }
}