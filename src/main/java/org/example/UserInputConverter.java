package org.example;

/**
 * Преобразует сообщения и callback запросы Telegram в единую форму
 */
public class UserInputConverter {	
	private final static String NEW_SINGLE_GAME = "Начать игру на этом устройстве";
	
	public String convertFromMessage(String userMessage, boolean inGame) {
		if (userMessage.charAt(0) == '/') {
			return userMessage.split("\\s")[0];
		}
		if (!inGame && userMessage.equals(NEW_SINGLE_GAME)) {
			return "/newsinglegame";
		} else if (inGame) {
			return userMessage;
		}
		return "/unknown";
	}
	
	public String convertFromCallback(String callbackData, boolean inGame) {
		if (inGame) {
			return "callback_" + callbackData;
		} else {
			return "/menu";
		}
	}
}
