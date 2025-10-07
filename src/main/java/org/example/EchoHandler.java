package org.example;

/**
 * Класс, отвечающий за реализацию функции эхо
 */
public class EchoHandler {
	/**
	 * Реализует функционал эхо
	 * @return ответ на любой не-коммандный ввод
	 */
	public String echoMessage(String message) {
		//Берёт сообщение пользователя и возвращает с небольшим изменением
		return "Вы отправили: " + message;
	}
}
