package org.example.inputHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Проверка обрабтчика ввода в главном меню
 */
public class MainMenuInputHandlerTest {
	/**
	 * Обработчик ввода в главном меню
	 */
	private final MainMenuInputHandler mainMenuInputHandler = 
			new MainMenuInputHandler();
	
	/**
	 * Проверка запроса начала игры
	 */
	@Test
	public void startGameTest() {
		String startGameString = mainMenuInputHandler.processInput(
				"Начать игру на этом устройстве");
		Assertions.assertEquals("newsinglegame", startGameString);
	}
	
	/**
	 * Проверка неизвестного запроса
	 */
	@Test
	public void unknownQueryTest() {
		String unknownQueryString = mainMenuInputHandler.processInput("Какой-то запрос");
		Assertions.assertEquals("unknown", unknownQueryString);
	}
}
