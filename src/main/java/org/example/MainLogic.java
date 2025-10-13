package org.example;

/**
 * Выполняет обработку пользовательского ввода
 */
public class MainLogic {
	/**
	 * Константа, содержащая сообщение команды /start
	 */
	private final String START_MESSAGE = """
			Здравствуй, путник! Я бот о шахматах. Сейчас я умею:
			 - возвращать твои сообщения;
			 - показывать окно помощи;

			Пока что я могу только это, но список возможностей  
			""" + """
			будет пополняться с течением разработки. 
			Отправь /help для большей информации.
			""";
	/**
	 * Константа, содержащая сообщение команды /help
	 */
	private final String HELP_MESSAGE = """
			Сейчас я могу:
			 - возврашать твоё сообщение;
			 - показывать это окно;
				
			Доступные команды:
			/help - позволяет это сообщение
			/start - перезапускает бота

			Скоро будет больше возможностей.
			""";
	
	/**
	 * Определить тип пользовательского ввода и передать управление соответствующему методу
	 * @param userInput
	 * @return экземпляр String, содержит ответное сообщение 
	 */
	public String processInput(String userInput) {
		//Проверка ввода и вызов соответствующего метода
		switch (userInput) {
		case "/start":
			return START_MESSAGE;
		case "/help":
			return HELP_MESSAGE;
		default:
			return echoMessage(userInput);
		}
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
