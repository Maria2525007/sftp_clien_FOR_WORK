package org.example.utils;

import org.example.commands.CommandHandler;
import java.util.Scanner;

public class InputHandler {
    private final CommandHandler commandHandler;

    public InputHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                System.out.print("Выберите действие: ");
                String choice = scanner.nextLine();
                running = commandHandler.execute(choice, scanner);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1. Список всех пар");
        System.out.println("2. Найти IP по домену");
        System.out.println("3. Найти домен по IP");
        System.out.println("4. Добавить новую пару");
        System.out.println("5. Удалить пару");
        System.out.println("6. Выход");
    }
}