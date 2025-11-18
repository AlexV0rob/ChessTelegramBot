package org.example.bots;

import java.util.Iterator;

public interface Bot {
	/**
	 * Отправить список сообщений
	 */
	public void sendMessages(long chatId, Iterator<String> messagesTextsIterator);
}
