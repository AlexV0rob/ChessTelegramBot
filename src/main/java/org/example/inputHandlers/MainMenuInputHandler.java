package org.example.inputHandlers;

import java.util.List;

import org.example.states.UserState;

/**
 * Обработчик ввлода в главном меню
 */
public class MainMenuInputHandler implements InputHandler {
	/**
	 * Обработчик команд
	 */
	private final CommandHandler commandHandler = new CommandHandler();
	
	/**
	 * Сообщение о неизвестном запросе
	 */
	private final static String UNKNOWN_INPUT = "Неизвестный запрос меню"; 

	/**
	 * Текст кнопки меню для начала одиночной игры
	 */
	private final static String NEW_SINGLE_GAME = "Начать игру на этом устройстве";
	
	@Override
	public List<String> processInput(String userInput, UserState currentUserState) {
		if (userInput.equals(NEW_SINGLE_GAME)) {
			return commandHandler.processCommand("newsinglegame", "", currentUserState);
		}
		return List.of(UNKNOWN_INPUT);
	}
}
