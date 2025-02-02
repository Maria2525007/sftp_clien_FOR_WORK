package org.example.commands;

import org.example.model.DomainEntry;
import org.example.model.DomainStorage;
import java.util.Optional;
import java.util.Scanner;

public class GetIpCommand implements Command {
    private final DomainStorage storage;

    public GetIpCommand(DomainStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("Введите домен: ");
        String domain = scanner.nextLine().trim();
        Optional<DomainEntry> entry = storage.findEntryByDomain(domain);
        System.out.println(entry.map(domainEntry ->
                "IP-адрес: " + domainEntry.getIp()).orElse("Домен не найден."));
    }
}