package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Тестирование работы команд
 */
public class CommandHandlerTest {	
	/**
	 * Тест неизвестной команды
	 */
	@Test
    void unknownCommandTest() {
		User user = new User();
		
    	//Режим не должен поменяться
    	byte oldMode = user.getMode();
    	new CommandHandler().processCommand("/lorem", user);
    	Assertions.assertEquals(oldMode, user.getMode());
    }
	
    /**
	 * Тест команды /start (перезапуск бота)
	 */
    @Test
    void startCommandTest() {
    	User user = new User();
		user.changeMode((byte) 1);
		
    	//Режим должен стать 0
    	byte oldMode = user.getMode();
    	new CommandHandler().processCommand("/start", user);
    	Assertions.assertEquals((byte) 0, user.getMode());
    }
    
    /**
	 * Тест команды /help (показать окно помощи)
	 */
    @Test
    void helpCommandTest() {
    	User user = new User();
    	
    	//Режим не должен поменяться
    	byte oldMode = user.getMode();
    	new CommandHandler().processCommand("/help", user);
    	Assertions.assertEquals(oldMode, user.getMode());
    }
    
    /**
     * Тест команды /menu (выход в меню)
     */
    @Test
    void menuCommandTest() {
    	User user = new User();
		user.changeMode((byte) 1);
		
    	//Режим должен стать 0
    	byte oldMode = user.getMode();
    	new CommandHandler().processCommand("/menu", user);
    	Assertions.assertEquals((byte) 0, user.getMode());
    }
    
    /**
     * Тест команды /echo (включение режима эхо)
     */
    @Test
    void echoCommandTest() {
    	User user = new User();
		user.changeMode((byte) 0);
    	//Режим должен стать 1
    	byte oldMode = user.getMode();
    	new CommandHandler().processCommand("/echo", user);
    	Assertions.assertEquals((byte) 1, user.getMode());
    }
    
    /**
     * Тест команды /newsinglegame (новая одиночная игра)
     * (игра на одном устройстве)
     */
    @Test
    void newsinglegameCommandTest() {
    	User user = new User();
		user.changeMode((byte) 0);
		
    	//Режим должен стать 2
    	byte oldMode = user.getMode();
    	new CommandHandler().processCommand("/newsinglegame", user);
    	Assertions.assertEquals((byte) 2, user.getMode());
    }
}
