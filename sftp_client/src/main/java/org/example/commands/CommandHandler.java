package org.example.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler register(String key, Command command) {
        commands.put(key, command);
        return this;
    }

    public boolean execute(String key, Scanner scanner) {
        Command command = commands.get(key);
        if (command == null) {
            System.out.println("Неверная команда");
            return true;
        }
        command.execute(scanner);
        return true;
    }
}