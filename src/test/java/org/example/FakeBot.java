package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.example.bots.Bot;

public class FakeBot implements Bot {
	/**
	 * Список накопленных сообщений
	 */
	private List<String> accumulatedMessages = new ArrayList<String>();
	
	@Override
	public void sendMessages(long chatId, Iterator<String> messagesTextsIterator) {
		while (messagesTextsIterator.hasNext()) {
			accumulatedMessages.add(messagesTextsIterator.next());
		}
	}
	
	/**
	 * Получить накопленные сообщения
	 */
	public List<String> getAccumulatedMessages() {
		return accumulatedMessages;
	}
	
	/**
	 * Очистить накопленные сообщения
	 */
	public void clearMessages() {
		accumulatedMessages = new ArrayList<String>();
	}
}
