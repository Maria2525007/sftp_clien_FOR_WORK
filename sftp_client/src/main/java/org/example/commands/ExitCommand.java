package org.example.commands;

import java.util.Scanner;

public class ExitCommand implements Command {
    @Override
    public void execute(Scanner scanner) {
        System.out.println("Работа завершена");
        System.exit(0);
    }
}