package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Тест класса Echo
 */
public class EchoHandlerTest {
	/**
	 * Префикс, добавляемый перед сообщением пользователя
	 * в режиме эхо
	 */
	private final String PREFIX = "Вы отправили: ";
	
	/**
	 * Проверка метода echoMessage
	 */
    @Test
    void echoMessageTest() {
        Assertions.assertEquals(
        		PREFIX + "TEST",        		
        		new EchoHandler().echoMessage("TEST")
        		);
    }
}
