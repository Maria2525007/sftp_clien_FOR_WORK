package org.example.commands;

import org.example.model.DomainEntry;
import org.example.model.DomainStorage;
import org.example.services.ValidationService;
import java.util.Scanner;

public class AddCommand implements Command {
    private final DomainStorage storage;
    private final ValidationService validator;

    public AddCommand(DomainStorage storage, ValidationService validator) {
        this.storage = storage;
        this.validator = validator;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("Введите домен: ");
        String domain = scanner.nextLine().trim();

        if (!validator.isValidDomain(domain)) {
            System.out.println("Ошибка: Некорректный формат домена (пример: example.com)");
            return;
        }

        if (storage.findEntryByDomain(domain).isPresent()) {
            System.out.println("Ошибка: Домен уже существует");
            return;
        }

        System.out.print("Введите IP: ");
        String ip = scanner.nextLine().trim();

        if (!validator.isValidIp(ip)) {
            System.out.println("Ошибка: Некорректный IPv4 (пример: 192.168.1.1)");
            return;
        }

        if (storage.findEntryByIp(ip).isPresent()) {
            System.out.println("Ошибка: IP уже существует");
            return;
        }

        storage.addEntry(new DomainEntry(domain, ip));
        System.out.println("Успешно: Запись добавлена");
    }
}