package org.example.inputHandlers;

/**
 * Обработчик ввода в режиме ожидания
 */
public class AwaitingInputHandler {
	/**
	 * Текст кнопки для выхода из режима ожидания
	 */
	private final static String QUIT_LOBBY = "Отменить поиск матча и удалить лобби";
	
	/**
	 * Получить командный эквивалент текстовому вводу
	 */
	public String processInput(String userInput) {
		switch (userInput) {
		case QUIT_LOBBY:
			return "quit";
		default:
			return "unknown";
		}
	}
}
