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
		Assertions.assertEquals("new_local", 
				menuButtonsConverter.getMenuCommand("Начать новую одиночную игру"));
		Assertions.assertEquals("create", 
				menuButtonsConverter.getMenuCommand("Создать многопользовательский матч"));
		Assertions.assertEquals("join", 
				menuButtonsConverter.getMenuCommand("Присоединится к существующему матчу"));
		Assertions.assertEquals("quit", 
				menuButtonsConverter.getMenuCommand("Отменить поиск соперника и удалить матч"));
		Assertions.assertEquals("unknown", 
				menuButtonsConverter.getMenuCommand("Неизвестно"));
	}
	
	/**
	 * Проверка перевода команды в кнопку
	 */
	@Test
	public void commandToButtonTest() {
		Assertions.assertEquals("Начать новую одиночную игру", 
				menuButtonsConverter.getCommandText("new_local"));
		Assertions.assertEquals("Создать многопользовательский матч", 
				menuButtonsConverter.getCommandText("create"));
		Assertions.assertEquals("Присоединится к существующему матчу", 
				menuButtonsConverter.getCommandText("join"));
		Assertions.assertEquals("Отменить поиск соперника и удалить матч", 
				menuButtonsConverter.getCommandText("quit"));
		Assertions.assertEquals("Неизвестно", 
				menuButtonsConverter.getCommandText("unknown"));
		
	}
}
