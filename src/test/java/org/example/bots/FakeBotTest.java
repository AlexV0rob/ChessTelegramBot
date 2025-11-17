package org.example.bots;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Iterator;
import java.util.List;

/**
 * Проверка фальшивого аккумулирующего бота
 */
public class FakeBotTest {
	/**
	 * Фальшивый бот
	 */
	private final FakeBot fakeBot = new FakeBot();
	
	/**
	 * Проверить очистку сообщений
	 */
	@Test
	public void clearMessageTest() {
		Iterator<String> messagesFirstIterator = 
				List.of("message 1", "message 2").iterator();
		Iterator<String> messagesSecondIterator = 
				List.of("message 3").iterator();
		fakeBot.sendMessages(1, messagesFirstIterator);
		fakeBot.sendMessages(2, messagesSecondIterator);
		fakeBot.clearMessages();
		Assertions.assertEquals(
				null, 
				fakeBot.getAccumulatedMessages(1));
		Assertions.assertEquals(
				null, 
				fakeBot.getAccumulatedMessages(2));
	}
	
	/**
	 * Проверить отправку сообщений
	 */
	@Test
	public void sendMessageTest() {
		fakeBot.clearMessages();
		Iterator<String> messagesFirstIterator = 
				List.of("message 1", "message 2").iterator();
		Iterator<String> messagesSecondIterator = 
				List.of("message 3").iterator();
		fakeBot.sendMessages(1, messagesFirstIterator);
		fakeBot.sendMessages(2, messagesSecondIterator);
		Assertions.assertIterableEquals(
				List.of("message 1", "message 2"), 
				fakeBot.getAccumulatedMessages(1));
		Assertions.assertIterableEquals(
				List.of("message 3"), 
				fakeBot.getAccumulatedMessages(2));
	}
	
	/**
	 * Проверить изменение сообщений
	 */
	@Test
	public void editMessageTest() {
		fakeBot.clearMessages();
		Iterator<String> messagesIterator = List.of("message 1", "message 2").iterator();
		fakeBot.sendMessages(1, messagesIterator);
		fakeBot.editMessage(1, 0, "message 3", false);
		Assertions.assertEquals(
				"message 3", 
				fakeBot.getAccumulatedMessages(1).getLast());
	}
}
