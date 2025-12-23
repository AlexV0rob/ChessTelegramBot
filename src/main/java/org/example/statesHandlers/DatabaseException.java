package org.example.statesHandlers;

import java.sql.SQLException;

/**
 * Ошибка базы данных
 */
public class DatabaseException extends Exception {
    /**
     * Конструктор ошибки
     */
    public DatabaseException(String message, SQLException exception) {
        super(message, exception);
    }
}
