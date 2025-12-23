package org.example;

/**
 * Критическая ошибка для быстрого завершения всей программы
 */
public class CommandException extends Exception {
    /**
     * Конструктор ошибки
     */
    public CommandException(String message) {
        super(message);
    }
}
