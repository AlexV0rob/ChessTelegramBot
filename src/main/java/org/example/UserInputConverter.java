package org.example;

/**
 * Преобразует сообщения и callback запросы Telegram в единую форму
 */
public class UserInputConverter {	
	/**
	 * Строка из меню для начала одиночной игры
	 */
	private final static String NEW_SINGLE_GAME = "Начать игру на этом устройстве";
	
	/**
	 * Преобразовать из текста сообщения Телеграм в единый формат для MainLogic
	 */
	public String convertFromTelegramMessage(String userMessage, boolean inGame) {
		if (userMessage.charAt(0) == '/') {
			return userMessage.split("\\s")[0];
		}
		if (inGame) {
			return userMessage;
		} else if (!inGame && userMessage.equals(NEW_SINGLE_GAME)) {
			return "/newsinglegame";
		}
		return "/unknown";
	}
	
	/**
	 * Преобразовать из callback запроса Телеграм в единый формат для MainLogic
	 */
	public String convertFromTelegramCallback(String callbackData, boolean inGame) {
		if (inGame) {
			return "callback_" + callbackData;
		} else {
			return "/quit";
		}
	}
}
