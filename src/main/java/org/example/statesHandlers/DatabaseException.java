package org.example.statesHandlers;

import java.sql.SQLException;

public class DatabaseException extends Exception {
	public DatabaseException(String message, SQLException exception) {
		super(message, exception);
	}
}
