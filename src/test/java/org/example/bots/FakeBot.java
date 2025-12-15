package org.example.bots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.states.UserState;

/**
 * Фальшивый бот для тестов
 */
public class FakeBot implements Bot {
	/**
	 * Ассоциативный массив, имитирующий пользователей 
	 */
	private final Map<Long, List<String>> users = new HashMap<Long, List<String>>();
	
	/**
	 * Тип мессенджера этого бота
	 */
	private final UserState.MessengerType messenger;
	
	/**
	 * Конструктор, устанавливает тип мессенджера для имитации
	 */
	public FakeBot(UserState.MessengerType mimicMessenger) {
		messenger = mimicMessenger;
	}
	
	@Override
	public long sendMessages(long chatId, List<String> messagesTexts) {
		users.putIfAbsent(chatId, new ArrayList<String>());
		List<String> currentUserMessages = users.get(chatId);
		for (int i = 0; i < messagesTexts.size(); ++i) {
			currentUserMessages.add(messagesTexts.get(i));
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
	
	@Override
	public UserState.MessengerType getBotMessengerType() {
		return messenger;
	}
}
