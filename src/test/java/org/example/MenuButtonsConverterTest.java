package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Проверка перевода кгнопок в команды и обратно
 */
public class MenuButtonsConverterTest {
	/**
	 * Преобразователь кнопок в команды
	 */
	private final MenuButtonsConverter menuButtonsConverter = new MenuButtonsConverter();
	
	/**
	 * Проверка перевода кнопки в команду
	 */
	@Test
	public void buttonToCommandTest() {
		Assertions.assertEquals("newsinglegame", 
				menuButtonsConverter.getMenuCommand("Начать игру на этом устройстве"));
	}
	
	/**
	 * Проверка перевода команды в кнопку
	 */
	@Test
	public void commandToButtonTest() {
		Assertions.assertEquals("Начать игру на этом устройстве", 
				menuButtonsConverter.getCommandText("newsinglegame"));
	}
}
