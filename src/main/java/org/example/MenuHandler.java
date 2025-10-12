package org.example;

import java.util.ArrayList;

/**
 * Обрабатывает сообщения в главном меню
 */
public class MenuHandler {
	/**
	 * Сообщение для включения режима эхо
	 */
	private final String ECHO = "Включить эхо-мод";
	/**
	 * Сообщение для начала новой однопользовательской игры
	 * (игры на одном устройстве)
	 */
	private final String SINGLEGAME = "Начать новую игру";
	/**
	 * Сообщение для открытия помощи
	 */
	private final String HELP = "Открыть окно помощи";
	/**
	 * 
	 */
	private final String DIDNT_UNDERSTAND = "Прости, не понял запрос";
	
	/**
	 * Обработать пользовательский ввод в меню и передать управление
	 * обработчику команд
	 * @param userInput
	 * @return экземпляр ArrayList<String>, содержит ответные сообщения
	 */
	public ArrayList<String> processMenu(String userInput, User curUser) {
		switch (userInput) {
		case HELP:
			return new CommandHandler().processCommand("/help", curUser);
		case ECHO:
			return new CommandHandler().processCommand("/echo", curUser);
		case SINGLEGAME:
			return new CommandHandler().processCommand("/newsinglegame", curUser);
			
		//Неизвестная команда меню
		default:
			ArrayList<String> responses = new CommandHandler()
				.processCommand("/menu", curUser);
			responses.add(0, DIDNT_UNDERSTAND);
			return responses;
		}
	}
}
