package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестирование класса MainLogic.
 */
class MainLogicTest {
	/**
	 * Проверка метода echoMessage
	 */
    @Test
    void echoMessageTest() {
        assertEquals(
        		"Вы отправили: TEST",
        		
        		new MainLogic().processInput("TEST")
        		);
    }
    
    /**
	 * Проверка метода startCommand
	 */
    @Test
    void startCommandTest() {
        assertEquals("Здравствуй, путник! Я бот о шахматах. Сейчас я умею:\n"
        		+ " - возвращать твои сообщения;\n"
        		+ " - показывать окно помощи;\n\n"
        		+ "Пока что я могу только это,"
        		+ " но список возможностей будет пополняться с течением разработки."
        		+ " Отправь /help для большей информации.",
        		
        		new MainLogic().processInput("/start")
        		);
    }
    
    /**
	 * Проверка метода helpCommand
	 */
    @Test
    void helpCommandTest() {
        assertEquals(
        		"Сейчас я могу:\n"
        		+ " - возврашать твоё сообщение;\n"
        		+ " - показывать это окно;\n\n"
        		+ "Доступные команды:\n"
        		+ "/help - позволяет это сообщение\n"
        		+ "/start - перезапускает бота\n\n"
        		+ "Скоро будет больше возможностей.",
        		
        		new MainLogic().processInput("/help")
        		);
    }
}
