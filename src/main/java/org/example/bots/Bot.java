package org.example.bots;

import java.util.Iterator;

/**
 * Интерфейс бота
 */
public interface Bot {
	/**
	 * Отправить список сообщений
	 */
	public long sendMessages(long chatId, Iterator<String> messagesTextsIterator);
	
	/**
	 * Редактировать сообщение по идентификатору или отправить новое, если 
	 * невозможно изменить
	 */
	public void editMessage(long chatId, long messageId, 
			String editedMessageText, boolean moreMessages);
}
