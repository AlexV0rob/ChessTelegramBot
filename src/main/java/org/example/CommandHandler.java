package org.example;

import java.util.ArrayList;

/**
 * Класс, отвечающий за обработку команд
 */
public class CommandHandler {
	/**
	 * Выполняет передачу управления на методы, обрабатывающие конкретную команду
	 * @param command содержит текст команды
	 * @return результат команды или сообщение "Неизвестная команда"
	 */
	public ArrayList<String> processCommand(String command, User curUser) {
		ArrayList<String> messages = new ArrayList<String>();
		switch (command) {
		case "/start":
			//Текст команды /start
			messages.add(startCommand());
		case "/menu":
			//Сообщение меню
			messages.add("Чем займёмся?");
			//Выйти в меню
			curUser.changeMode((byte) 0);
			break;
		case "/help":
			//Текст команды /help
			messages.add(helpCommand());
			break;
		case "/echo":
			//Сообщение о начале работы режима эхо
			messages.add("Теперь я буду повторять твои сообщения. Чтобы выйти, отправь /menu");
			//Включить режим эхо
			curUser.changeMode((byte) 1);
			break;
		case "/newsinglegame":
			//Сообщение о начале новой игры
			messages.add("Новая партия началась");
			//Начать новую одиночную игру (на одном устройстве)
			curUser.changeMode((byte) 2);
			break;
		default:
			messages.add("Неизвестная команда");
		}
		return messages;
	}
	
	/**
	 * Формирует и возвращает сообщение команды /start
	 * @return ответ на команду /start
	 */
	private String startCommand() {
		//Стандартная заголовочная строка
		String startMessage = "Здравствуй, путник! Я бот о шахматах. Сейчас я умею:\n";
		
		//Список возможностей
		startMessage += " - работать в режиме эхо;\n";
		
		//Пустая строка для лучшей читаемости
		startMessage += "\n";
		
		//Стандартная заключающая строка
		startMessage += "Пока что я могу только это,"
				+ " но список возможностей будет пополняться с течением разработки."
				+ " Отправь /help для большей информации.";
		
		return startMessage;
	}
	
	/**
	 * Создаёт и возвращает сообщение команды /help
	 * @return ответ на команду /help
	 */
	private String helpCommand() {
		//Стандартная заголовочная строка
		String helpMessage = "Сейчас я могу:\n";
		
		//Список возможностей
		helpMessage += " - возврашать твоё сообщение в режиме эхо;\n";
		helpMessage += " - показывать это окно;\n";
		helpMessage += " - осуществлять навигацию с помощью команд\n";
		
		//Пустая строка для лучшей читаемости
		helpMessage += "\n";
		
		//Стандартная строка с заголовком блока команд
		helpMessage += "Доступные команды:\n";
		
		//Список команд
		helpMessage += "/start - перезапускает бота\n";
		helpMessage += "/help - показывает это сообщение\n";
		helpMessage += "/menu - выходит в главное меню\n";
		helpMessage += "/echo - включает режим эхо\n";
		helpMessage += "/newsinglegame - начинает однопользовательскую игру\n";
		
		//Пустая строка для лучшей читаемости
		helpMessage += "\n";
		
		//Стандартная заключающая строка
		helpMessage += "Скоро будет больше возможностей.";
		
		return helpMessage;
	}
}
