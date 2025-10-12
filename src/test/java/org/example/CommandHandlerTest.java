package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Тестирование работы команд
 */
public class CommandHandlerTest {
	/**
	 * Количество режимов в данный момент и соответствующее число пользователей
	 */
	int modesAmount = 3;
	private User[] users = new User[modesAmount];
	private CommandHandlerTest() {
		for (byte i = 0; i < modesAmount; ++i) {
			users[i] = new User();
		}
	}
	
	/**
	 * Проверка неизвестной команды
	 */
	@Test
    void unknownCommandTest() {
    	//Выставить всем пользователям разные режимы
    	for (byte i = 0; i < modesAmount; ++i) {
    		users[i].changeMode(i);
		}
    	
    	//Режим не должен поменяться
    	for (int i = 0; i < modesAmount; ++i) {
    		byte oldMode = users[i].getMode();
    		new CommandHandler().processCommand("/something", users[i]);
    		Assertions.assertEquals(oldMode, users[i].getMode());
    	}
    }
	
    /**
	 * Проверка команды /start (перезапуск бота)
	 */
    @Test
    void startCommandTest() {
    	//Выставить всем пользователям разные режимы
    	for (byte i = 0; i < modesAmount; ++i) {
    		users[i].changeMode(i);
		}
    	
    	//Режим должен стать 0
    	for (int i = 0; i < modesAmount; ++i) {
    		new CommandHandler().processCommand("/start", users[i]);
    		Assertions.assertEquals((byte) 0, users[i].getMode());
    	}
    }
    
    /**
	 * Проверка команды /help (показать окно помощи)
	 */
    @Test
    void helpCommandTest() {
    	//Выставить всем пользователям разные режимы
    	for (byte i = 0; i < modesAmount; ++i) {
    		users[i].changeMode(i);
		}
    	
    	//Режим не должен поменяться
    	for (int i = 0; i < modesAmount; ++i) {
    		byte oldMode = users[i].getMode();
    		new CommandHandler().processCommand("/help", users[i]);
    		Assertions.assertEquals(oldMode, users[i].getMode());
    	}
    }
    
    /**
     * Тест команды /menu (выход в меню)
     */
    @Test
    void menuCommandTest() {
    	//Выставить всем пользователям разные режимы
    	for (byte i = 0; i < modesAmount; ++i) {
    		users[i].changeMode(i);
		}
    	
    	//Режим должен стать 0
    	for (int i = 0; i < modesAmount; ++i) {
    		new CommandHandler().processCommand("/menu", users[i]);
    		Assertions.assertEquals((byte) 0, users[i].getMode());
    	}
    }
    
    /**
     * Тест команды /echo (включение режима эхо)
     */
    @Test
    void echoCommandTest() {
    	//Выставить всем пользователям разные режимы
    	for (byte i = 0; i < modesAmount; ++i) {
    		users[i].changeMode(i);
		}
    	
    	//Режим должен стать 1
    	for (int i = 0; i < modesAmount; ++i) {
			new CommandHandler().processCommand("/echo", users[i]);
    		Assertions.assertEquals((byte) 1, users[i].getMode());
    	}
    }
    
    /**
     * Тест команды /newsinglegame (новая одиночная игра)
     * (игра на одном устройстве)
     */
    @Test
    void newsinglegameCommandTest() {
    	//Выставить всем пользователям разные режимы
    	for (byte i = 0; i < modesAmount; ++i) {
    		users[i].changeMode(i);
		}
    	
    	//Режим должен стать 2
    	for (int i = 0; i < modesAmount; ++i) {
    		new CommandHandler().processCommand("/newsinglegame", users[i]);
    		Assertions.assertEquals((byte) 2, users[i].getMode());
    	}
    }
}
