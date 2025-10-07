package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест класса Echo
 */
public class EchoTest {
	/**
	 * Проверка метода echoMessage
	 */
    @Test
    void echoMessageTest() {
    	//Префикс
    	String prefix = "Вы отправили: ";
    	//Проверка, что сообщение возвращается с префиксом
        assertEquals(
        		prefix + "TEST",        		
        		new EchoHandler().echoMessage("TEST")
        		);
    }
}
