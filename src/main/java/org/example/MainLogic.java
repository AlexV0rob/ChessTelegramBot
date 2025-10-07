package org.example;

/**
 * Выполняет обработку пользовательского ввода
 */
public class MainLogic {
	/**
	 * Определить тип пользовательского ввода и передать управление соответствующему методу
	 * @param userInput
	 * @return экземпляр String, содержит ответное сообщение 
	 */
	public String processInput(String userInput) {
		//Проверка ввода и вызов соответствующего метода
		switch (userInput) {
		case "/start":
			return startCommand();
		case "/help":
			return helpCommand();
		default:
			return echoMessage(userInput);
		}
	}
	
	/**
	 * Сформировать ответ на команду /start
	 * @return экземпляр String, содержит сообщение команды /start
	 */
	private String startCommand() {
		String startMessage = "Здравствуй, путник! Я бот о шахматах. Сейчас я умею:\n"
			+ " - возвращать твои сообщения;\n"
			+ " - показывать окно помощи;\n"
			+ "\n"

			+ "Пока что я могу только это, "
			+ "но список возможностей будет пополняться с течением разработки. "
			+ "Отправь /help для большей информации.";
		
		return startMessage;
	}
	
	/**
	 * Сформировать ответ на команду /help
	 * @return экземпляр String, содержит сообщение команды /help
	 */
	private String helpCommand() {
		String helpMessage = "Сейчас я могу:\n"
			+ " - возврашать твоё сообщение;\n"
			+ " - показывать это окно;\n"
			+ "\n"
			
			+ "Доступные команды:\n"
			+ "/help - позволяет это сообщение\n"
			+ "/start - перезапускает бота\n"
			+ "\n"

			+ "Скоро будет больше возможностей.";
		
		return helpMessage;
	}
	
	/**
	 * Вернуть ответ на любой ввод, не являющийся командой
	 * @param userMessage
	 * @return экземпляр String, содержит сообщение пользователя с префиксом "Вы отправили: "
	 */
	private String echoMessage(String userMessage) {
		return "Вы отправили: " + userMessage;
	}
}
