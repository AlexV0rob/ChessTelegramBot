package org.example.bots;

import java.util.List;

import org.example.states.UserState;

/**
 * Интерфейс бота
 */
public interface Bot {
	/**
	 * Отправить список сообщений
	 */
	long sendMessages(long chatId, List<String> messagesTexts);
	
	/**
	 * Редактировать сообщение по идентификатору или отправить новое, если 
	 * невозможно изменить
	 */
	void editMessage(long chatId, long messageId, 
			String editedMessageText, boolean moreMessages);
	
	/**
	 * Получить тип мессенджера бота
	 */
	UserState.MessengerType getBotMessengerType();
}
