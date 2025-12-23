package org.example;

/**
 * Критическая ошибка, требующая остановку работы программы
 */
public class CriticalError extends RuntimeException {
    /**
     * Конструктор ошибки
     */
    public CriticalError(String message, Exception e) {
        super(message, e);
    }
}
