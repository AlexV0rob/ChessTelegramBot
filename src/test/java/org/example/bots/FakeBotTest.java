package org.example.bots;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

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
		List<String> messagesFirst = List.of("message 1", "message 2");
		List<String> messagesSecond = List.of("message 3");
		fakeBot.sendMessages(1, messagesFirst);
		fakeBot.sendMessages(2, messagesSecond);
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
		List<String> messagesFirst = List.of("message 1", "message 2");
		List<String> messagesSecond = List.of("message 3");
		fakeBot.sendMessages(1, messagesFirst);
		fakeBot.sendMessages(2, messagesSecond);
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
		List<String> messages = List.of("message 1", "message 2");
		fakeBot.sendMessages(1, messages);
		fakeBot.editMessage(1, 0, "message 3", false);
		Assertions.assertEquals(
				"message 3", 
				fakeBot.getAccumulatedMessages(1).getLast());
	}
}
