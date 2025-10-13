package org.example;

import java.util.ArrayList;

/**
 * Обрабатывает команды
 */
public class CommandHandler {
	/**
	 * Сообщение команды /start
	 */
	private final String START_MESSAGE = """
			Здравствуй, путник! Я бот о шахматах. Сейчас я умею:
			 - работать в режиме эхо
			 - запускать игру 1v1 на одном устройстве

			Пока что я могу только это, но список возможностей 
			""" + """
			будет пополняться с течением разработки. 
			Отправь /help для большей информации.
			""";
	/**
	 * Сообщение команды /help
	 */
	private final String HELP_MESSAGE = """
			Сейчас я могу:
			 - работать в режиме эхо
			 - запускать игру 1v1 на одном устройстве
				
			Доступные команды:
			/start - перезапускает бота
			/help - позволяет это сообщение
			/menu - выходит в главное меню
			/echo - включает режим эхо
			/newsinglegame - начинает новую игру на одном устройстве

			Скоро будет больше возможностей.
			""";
	/**
	 * Сообщение команды /menu
	 */
	private final String MENU_MESSAGE = "Чем займёмся?";
	/**
	 * Сообщение команды /echo
	 */
	private final String ECHO_MESSAGE = """
			Я буду повторять твои сообщения.
			Чтобы выйти, отправь /menu
			""";
	/**
	 * Пригласителное сообщение для хода
	 */
	private final String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Сообщение команды /newsinglegame
	 */
	private final String SINGLEGAME_MESSAGE = "Новая партия началась";	
	/**
	 * Ответ на неизветную команду
	 */
	private final String UNKNOWN_MESSAGE = "Неизвестная команда";
	
	/**
	 * Определить тип команды, поменять на соответсвующий режим
	 * и отправить ответ
	 * @param command
	 * @return экземпляр ArrayList<String>, содержит ответные сообщения
	 */
	public ArrayList<String> processCommand(String command, User curUser) {
		/*
		 * При введении некоторых команд переключается режим работы бота
		 * Список режимов:
		 * 0 - главное меню
		 * 1 - режим эхо
		 * 2 - одиночная игра (игра на одном устройстве)
		 */
		ArrayList<String> messages = new ArrayList<String>();
		
		//Меняем режим или добавляем базовые сообщения
		switch (command) {
		case "/start":
			messages.add(START_MESSAGE);
		case "/menu":
			curUser.changeMode((byte) 0);
			break;
		case "/help":
			messages.add(HELP_MESSAGE);
			break;
		case "/echo":
			curUser.changeMode((byte) 1);
			break;
		case "/newsinglegame":
			curUser.changeMode((byte) 2);
			curUser.newBoard();
			break;
		default:
			messages.add(UNKNOWN_MESSAGE);
		}
		
		//Добавляем пригласительные сообщения режима
		switch(curUser.getMode()) {
		case 0:
			messages.add(MENU_MESSAGE);
			break;
		case 1:
			messages.add(ECHO_MESSAGE);
			break;
		case 2:
			messages.add(SINGLEGAME_MESSAGE);
			messages.add("Ход белых");
			messages.add(new MoveMaker().printBoard(curUser));
			messages.add(YOUR_MOVE);
			break;
		}
		
		return messages;
	}
}
