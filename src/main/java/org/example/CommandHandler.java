package org.example;

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
	
	private final static String MENU_MESSAGE = "Чем займёмся?";
	
	/**
	 * Определить тип команды, поменять при необходимости на соответсвующий режим
	 * и отправить ответ
	 */
	public String processCommand(String command, GameState currentGameState) {
		switch (command) {
		case "/start":
			currentGameState.setState(GameState.STATES.NOGAME);
			return START_MESSAGE;
		case "/help":
			return HELP_MESSAGE;
		case "/quit":
			currentGameState.setState(GameState.STATES.NOGAME);
			return MENU_MESSAGE;
		case "/newsinglegame":
			currentGameState.setState(GameState.STATES.INGAME);
			return currentGameState.printBoard(GameState.MOVE_PROPERTIES.REGULAR);
		default:
			return UNKNOWN_MESSAGE;
		}
	}
}
