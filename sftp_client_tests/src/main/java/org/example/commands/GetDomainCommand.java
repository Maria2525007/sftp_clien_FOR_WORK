package org.example.commands;

import org.example.model.DomainEntry;
import org.example.model.DomainStorage;
import org.example.services.ValidationService;
import java.util.Optional;
import java.util.Scanner;

public class GetDomainCommand implements Command {
    private final DomainStorage storage;
    private final ValidationService validator;

    public GetDomainCommand(DomainStorage storage, ValidationService validator) {
        this.storage = storage;
        this.validator = validator;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("Введите IP-адрес: ");
        String ip = scanner.nextLine().trim();
        if (!validator.isValidIp(ip)) {
            System.out.println("Некорректный IP-адрес");
            return;
        }
        Optional<DomainEntry> entry = storage.findEntryByIp(ip);
        System.out.println(entry.map(domainEntry -> "Домен: " + domainEntry.getDomain()).orElse("IP-адрес не найден"));
    }
}