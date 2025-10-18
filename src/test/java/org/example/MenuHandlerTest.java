package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

/**
 * Тестирование работы меню
 */
public class MenuHandlerTest {	
	/**
	 * Сообщение для включения режима эхо
	 */
	private final String ECHO = "Включить эхо-мод";
	/**
	 * Сообщение для начала новой однопользовательской игры
	 * (игры на одном устройстве)
	 */
	private final String SINGLEGAME = "Начать новую игру";
	/**
	 * Сообщение для открытия помощи
	 */
	private final String HELP = "Открыть окно помощи";
	/**
	 * Сообщение неопределённого ввода
	 */
	private final String DIDNT_UNDERSTAND = "Прости, не понял запрос";
	
	/**
	 * Проверка неопределённого ввода
	 */
	@Test
    void unknownMessageTest() {
		User user = new User();
		
    	//Режим не должен поменяться
		byte oldMode = user.getMode();
    	ArrayList<String> menuResponse = new MenuHandler()
    			.processMenu("Lorem ipsum dolor sit amet", user);
    	byte menuMode = user.getMode();
    	ArrayList<String> commandResponse = new CommandHandler().
    			processCommand("/menu", user);
    	byte commandMode = user.getMode();
    	
    	/*
    	 * Вывод и режим должны соответствовать команде /menu с добавленным
    	 * сообщением DIDNT_UNDERSTAND
    	 */
    	commandResponse.add(0, DIDNT_UNDERSTAND);
    	Assertions.assertIterableEquals(menuResponse, commandResponse);
    	Assertions.assertEquals(oldMode, menuMode);
    	Assertions.assertEquals(menuMode, commandMode);
    }
    
    /**
	 * Проверка кнопки вывода окна помощи
	 */
    @Test
    void helpMessageTest() {
    	User user = new User();
    	
    	//Режим не должен поменяться
    	byte oldMode = user.getMode();
    	ArrayList<String> menuResponse = new MenuHandler()
    			.processMenu(HELP, user);
    	byte menuMode = user.getMode();
    	ArrayList<String> commandResponse = new CommandHandler().
    			processCommand("/help", user);
    	byte commandMode = user.getMode();
    	
    	//Вывод и режим должны соответствовать команде /help
    	Assertions.assertIterableEquals(menuResponse, commandResponse);
    	Assertions.assertEquals(oldMode, menuMode);
    	Assertions.assertEquals(menuMode, commandMode);
    }
    
    /**
     * Проверка кнопки включения режима эхо
     */
    @Test
    void echoMessageTest() {
    	User user = new User();
		user.changeMode((byte) 0);
		
    	//Режим должен стать 1
    	ArrayList<String> menuResponse = new MenuHandler()
    			.processMenu(ECHO, user);
    	byte menuMode = user.getMode();
    	ArrayList<String> commandResponse = new CommandHandler().
    			processCommand("/echo", user);
    	byte commandMode = user.getMode();
    	
    	//Вывод и режим должны соответствовать команде /echo
    	Assertions.assertIterableEquals(menuResponse, commandResponse);
    	Assertions.assertEquals((byte) 1, menuMode);
    	Assertions.assertEquals(menuMode, commandMode);
    }
    
    /**
     * Проверка кнопки начала новой игры
     */
    @Test
    void newsinglegameMessageTest() {
    	User user = new User();
		user.changeMode((byte) 0);
		
    	//Режим должен стать 2
    	ArrayList<String> menuResponse = new MenuHandler()
    			.processMenu(SINGLEGAME, user);
    	byte menuMode = user.getMode();
    	ArrayList<String> commandResponse = new CommandHandler().
    			processCommand("/newsinglegame", user);
    	byte commandMode = user.getMode();
    	
    	//Вывод и режим должны соответствовать команде /newsinglegame
    	Assertions.assertIterableEquals(menuResponse, commandResponse);
    	Assertions.assertEquals((byte) 2, menuMode);
    	Assertions.assertEquals(menuMode, commandMode);
    }
}
