package org.example;

/**
 * Отвечает за основную логику
 */
public class MainLogic {
	/**
	 * Выполняет первичное управление сообщением от пользователя
	 * @param input содержит текст сообщения
	 * @return ответное сообщение
	 */
	public String processInput(String input) {
		switch (input) {
		//Если введена команда /start
		case "/start":
			return startCommand();
		//Если введена команда /help
		case "/help":
			return helpCommand();
		//Введён просто какой-то текст
		default:
			return echoMessage(input);
		}
	}
	
	/**
	 * Формирует и возвращает сообщение команды /start
	 * @return ответ на команду /start
	 */
	private String startCommand() {
		//Стандартная заголовочная строка
		String startMessage = "Здравствуй, путник! Я бот о шахматах. Сейчас я умею:\n";
		
		//Список возможностей
		startMessage += " - возвращать твои сообщения;\n";
		startMessage += " - показывать окно помощи;\n";
		
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
		helpMessage += " - возврашать твоё сообщение;\n";
		helpMessage += " - показывать это окно;\n";
		
		//Пустая строка для лучшей читаемости
		helpMessage += "\n";
		
		//Стандартная строка с заголовком блока команд
		helpMessage += "Доступные команды:\n";
		
		//Список команд
		helpMessage += "/help - позволяет это сообщение\n";
		helpMessage += "/start - перезапускает бота\n";
		
		//Пустая строка для лучшей читаемости
		helpMessage += "\n";
		
		//Стандартная заключающая строка
		helpMessage += "Скоро будет больше возможностей.";
		
		return helpMessage;
	}
	
	/**
	 * Реализует функционал эхо
	 * @return ответ на любой не-коммандный ввод
	 */
	private String echoMessage(String message) {
		//Берёт сообщение пользователя и возвращает с небольшим изменением
		return "Вы отправили: " + message;
	}
}
