package org.example.inputHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Проверка обрабтчика ввода в режиме ожидания противника в матче
 */
public class AwaitingInputHandlerTest {
	/**
	 * Обработчик ввода в режиме ожидания
	 */
	private final AwaitingInputHandler awaitingInputHandler = 
			new AwaitingInputHandler();
	
	/**
	 * Проверка отмены поиска матча
	 */
	@Test
	public void quitGameTest() {
		String quitGameString = awaitingInputHandler.processInput(
				"Отменить поиск матча и удалить лобби");
		Assertions.assertEquals("quit", quitGameString);
	}
	
	/**
	 * Проверка неизвестного запроса
	 */
	@Test
	public void unknownQueryTest() {
		String unknownQueryString = awaitingInputHandler.processInput("Какой-то запрос");
		Assertions.assertEquals("unknown", unknownQueryString);
	}
}
