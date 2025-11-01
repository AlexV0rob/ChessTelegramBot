package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Проверка преобразователя пользовательского ввода
 */
public class UserInputConverterTest {
	/**
	 * 
	 */
	private final UserInputConverter converter = new UserInputConverter();
	
	/**
	 * Проверка выделения команды из строки
	 */
	@Test
	void commandGetterTest() {
		Assertions.assertEquals("/command", converter
				.convertFromTelegramMessage("/command and something else", false));
		Assertions.assertEquals("/unknown", converter
				.convertFromTelegramMessage("something else and /command", false));
	}
	
	/**
	 * Проверка запуска одиночной игры
	 */
	@Test
	void newGameTest() {
		Assertions.assertEquals("/newsinglegame", converter
				.convertFromTelegramMessage("Начать игру на этом устройстве", false));
	}
	
	/**
	 * Проверка ввода во время игры
	 */
	@Test
	void inGameInputTest() {
		Assertions.assertEquals("Pe2e4", converter
				.convertFromTelegramMessage("Pe2e4", true));
		Assertions.assertEquals("something", converter
				.convertFromTelegramMessage("something", true));
	}
	
	/**
	 * Проверка неизвестного текстового ввода
	 */
	@Test
	void unknownInputTest() {
		Assertions.assertEquals("/unknown", converter
				.convertFromTelegramMessage("Just some input", false));
	}
	
	/**
	 * Проверка callback запроса в игре
	 */
	@Test
	void inGameCallbackTest() {
		Assertions.assertEquals("callback_e2", converter
				.convertFromTelegramCallback("e2", true));
	}
	
	/**
	 * Проверка callback запроса вне игры
	 */
	@Test
	void noGameCallbcakTest() {
		Assertions.assertEquals("/quit", converter
				.convertFromTelegramCallback("e2", false));
		
	}
}
