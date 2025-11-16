package org.example.inputHandlers;

/**
 * Обработчик ввлода в главном меню
 */
public class MainMenuInputHandler {
	/**
	 * Текст кнопки меню для начала одиночной игры
	 */
	private final static String NEW_SINGLE_GAME = "Начать игру на этом устройстве";
	
	/**
	 * Получить командный эквивалент текстовому вводу
	 */
	public String processInput(String userInput) {
		switch (userInput) {
		case NEW_SINGLE_GAME:
			return "newsinglegame";
		default:
			return "unknown";
		}
	}
}
