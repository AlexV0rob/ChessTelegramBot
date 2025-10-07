package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Тестирование класса MainLogic
 */
class MainLogicTest {
	/**
	 * Проверка метода echoMessage
	 */
    @Test
    void echoMessageTest() {
        Assertions.assertEquals(
        		"Вы отправили: TEST",
        		new MainLogic().processInput("TEST"));
    }
}
