package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Обрабатывает команды
 */
public class CommandHandler {
	/**
	 * Сообщение команды /start
	 */
	private final static String START_MESSAGE = """
			Здравствуй, путник! Я бот о шахматах. Сейчас я умею:
			 - запускать игру на одном устройстве

			Пока что я могу только это, но список возможностей 
			""" + """
			будет пополняться с течением разработки. 
			Отправь /help для большей информации.
			""";
	/**
	 * Сообщение команды /help
	 */
	private final static String HELP_MESSAGE = """
			Сейчас я могу:
			 - запускать игру на одном устройстве
				
			Доступные команды:
			/start - перезапускает бота
			/help - позволяет это сообщение
			/newsinglegame - начинает новую игру на одном устройстве

			Скоро будет больше возможностей.
			""";
	/**
	 * Ответ на неизветную команду
	 */
	private final static String UNKNOWN_MESSAGE = "Неизвестная команда";
	/**
	 * Сообщение в меню
	 */
	private final static String MENU_MESSAGE = "Чем займёмся?";
	/**
	 * Сообщение о начале игры
	 */
	private final static String GAME_STARTED = "Игра началась";
	
	/**
	 * Определить тип команды, поменять при необходимости на соответсвующий режим
	 * и отправить ответ
	 */
	public List<String> processCommand(String command, GameState currentGameState) {
		List<String> responseMessagesTexts = new ArrayList<String>();
		switch (command) {
		case "/start":
			responseMessagesTexts.add(START_MESSAGE);
		case "/quit":
			currentGameState.setState(GameState.STATES.NOGAME);
			responseMessagesTexts.add(MENU_MESSAGE);
			return responseMessagesTexts;
		case "/help":
			return List.of(HELP_MESSAGE);
		case "/newsinglegame":
			currentGameState.setState(GameState.STATES.INGAME);
			return List.of(GAME_STARTED,
					currentGameState.printBoard(GameState.MOVE_PROPERTIES.REGULAR));
		default:
			return List.of(UNKNOWN_MESSAGE);
		}
	}
}
