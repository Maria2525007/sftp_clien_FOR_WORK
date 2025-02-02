package org.example.commands;

import org.example.model.DomainStorage;
import java.util.Scanner;

public class ListCommand implements Command {
    private final DomainStorage storage;

    public ListCommand(DomainStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute(Scanner scanner) {
        storage.getEntries().forEach(entry ->
                System.out.println("Домен: " + entry.getDomain() + ", IP: " + entry.getIp())
        );
    }
}