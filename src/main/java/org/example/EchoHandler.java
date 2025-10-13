package org.example;

/**
 * Реализует функцию эхо
 */
public class EchoHandler {
	/**
	 * Префикс, с которым возвращается сообщение
	 */
	private final String PREFIX = "Вы отправили: ";
	
	/**
	 * Отправить сообщение обратно
	 * @return экзмепляр String, содержит сообщение пользователя с префиксом
	 */
	public String echoMessage(String message) {
		return PREFIX + message;
	}
}
