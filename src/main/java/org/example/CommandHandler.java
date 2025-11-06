package org.example;

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
	public String processCommand(String command, String argument, UserState currentUserState) {
		switch (command) {
		case "start":
			currentUserState.setUserState(UserState.USER_STATE.MAINMENU);
			return START_MESSAGE;
		case "quit":
			currentUserState.setUserState(UserState.USER_STATE.MAINMENU);
			return MENU_MESSAGE;
		case "help":
			return HELP_MESSAGE;
		case "newsinglegame":
			currentUserState.setUserState(UserState.USER_STATE.INGAME);
			currentUserState.resetGameState();
			return new GameTranslator().currentBoardState(currentUserState.getGameState());
		default:
			return UNKNOWN_MESSAGE;
		}
	}
}
