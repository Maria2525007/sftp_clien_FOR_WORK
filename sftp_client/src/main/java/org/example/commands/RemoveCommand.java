package org.example.commands;

import org.example.model.DomainStorage;
import org.example.services.ValidationService;
import java.util.Scanner;

public class RemoveCommand implements Command {
    private final DomainStorage storage;
    private final ValidationService validator;

    public RemoveCommand(DomainStorage storage, ValidationService validator) {
        this.storage = storage;
        this.validator = validator;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("Введите домен / IP: ");
        String input = scanner.nextLine().trim();
        boolean isIp = validator.isValidIp(input);
        boolean removed = isIp ? storage.removeEntryByIp(input) : storage.removeEntryByDomain(input);
        System.out.println(removed ? "Запись успешно удалена" : "Запись не найдена");
    }
}