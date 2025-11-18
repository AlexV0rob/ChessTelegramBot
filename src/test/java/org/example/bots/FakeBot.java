package org.example.bots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Фальшивый бот для тестов
 */
public class FakeBot implements Bot {
	/**
	 * Ассоциативный массив, имитирующий пользователей 
	 */
	private final Map<Long, List<String>> users = new HashMap<Long, List<String>>();
	
	@Override
	public long sendMessages(long chatId, Iterator<String> messagesTextsIterator) {
		users.putIfAbsent(chatId, new ArrayList<String>());
		List<String> currentUserMessages = users.get(chatId);
		while (messagesTextsIterator.hasNext()) {
			currentUserMessages.add(messagesTextsIterator.next());
		}
		return -1;
	}
	
	@Override
	public void editMessage(long chatId, long messageId, 
			String editedMessageText, boolean moreMessages) {
		users.putIfAbsent(chatId, new ArrayList<String>());
		List<String> currentUserMessages = users.get(chatId);
		currentUserMessages.removeLast();
		currentUserMessages.add(editedMessageText);
	}
	
	/**
	 * Получить накопленные сообщения
	 */
	public List<String> getAccumulatedMessages(long chatId) {
		return users.get(chatId);
	}
	
	/**
	 * Очистить накопленные сообщения
	 */
	public void clearMessages() {
		users.clear();
	}
}
