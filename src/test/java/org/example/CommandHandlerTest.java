package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

public class CommandHandlerTest {
	/**
	 * Количество режимов в данный момент и соответствующее число пользователей
	 */
	int modesAmount = 3;
	private User[] users = new User[modesAmount];
	private CommandHandlerTest() {
		for (byte i = 0; i < modesAmount; ++i) {
			users[i] = new User(0);
		}
	}
	
	/**
	 * Список строк команд бота
	 */
	String startCommand = "Здравствуй, путник! Я бот о шахматах. Сейчас я умею:\n"
    		+ " - работать в режиме эхо;\n\n"
    		+ "Пока что я могу только это, "
    		+ "но список возможностей будет пополняться с течением разработки. "
    		+ "Отправь /help для большей информации.";
	String menuCommand = "Чем займёмся?";
	String echoCommand = "Теперь я буду повторять твои сообщения. "
			+ "Чтобы выйти, отправь /menu";
	String helpCommand = "Сейчас я могу:\n"
			+ " - возврашать твоё сообщение в режиме эхо;\n"
			+ " - показывать это окно;\n"
			+ " - осуществлять навигацию с помощью команд\n\n"
    		+ "Доступные команды:\n"
    		+ "/start - перезапускает бота\n"
			+ "/help - показывает это сообщение\n"
			+ "/menu - выходит в главное меню\n"
			+ "/echo - включает режим эхо\n"
			+ "/newsinglegame - начинает однопользовательскую игру\n\n"
    		+ "Скоро будет больше возможностей.";
	String newsinglegameCommand = "Новая партия началась";
	String unknownCommand = "Неизвестная команда";
	
	/**
	 * Проверка неизвестной команды
	 */
	@Test
    void unknownCommandTest() {
    	//Выставить всем пользователям разные режимы
    	for (byte i = 0; i < modesAmount; ++i) {
    		users[i].changeMode(i);
		}
    	//Препдполагаемый список ответных сообщений
    	ArrayList<String> assumedResponses = new ArrayList<String>();
    	assumedResponses.add(unknownCommand);
    	
    	//Проход по всем пользователям
    	for (int i = 0; i < modesAmount; ++i) {
    		//Проверка ответных сообщений
    		byte oldMode = users[i].getMode();
    		assertEquals(
        		assumedResponses,
        		new CommandHandler().processCommand("/something", users[i])
        		);
    		//Проверка изменения режима (не должен поменяться)
    		assertEquals(oldMode, users[i].getMode());
    	}
    }
	
    /**
	 * Проверка метода startCommand
	 */
    @Test
    void startCommandTest() {
    	//Выставить всем пользователям разные режимы
    	for (byte i = 0; i < modesAmount; ++i) {
    		users[i].changeMode(i);
		}
    	//Препдполагаемый список ответных сообщений
    	ArrayList<String> assumedResponses = new ArrayList<String>();
    	assumedResponses.add(startCommand);
    	assumedResponses.add(menuCommand);
    	
    	//Проход по всем пользователям
    	for (int i = 0; i < modesAmount; ++i) {
    		//Проверка ответных сообщений
    		assertEquals(
        		assumedResponses,
        		new CommandHandler().processCommand("/start", users[i])
        		);
    		//Проверка изменения режима (должен стать 0 - выход в меню)
    		assertEquals((byte) 0, users[i].getMode());
    	}
    }
    
    /**
	 * Проверка метода helpCommand
	 */
    @Test
    void helpCommandTest() {
    	//Выставить всем пользователям разные режимы
    	for (byte i = 0; i < modesAmount; ++i) {
    		users[i].changeMode(i);
		}
    	//Препдполагаемый список ответных сообщений
    	ArrayList<String> assumedResponses = new ArrayList<String>();
    	assumedResponses.add(helpCommand);
    	
    	//Проход по всем пользователям
    	for (int i = 0; i < modesAmount; ++i) {
    		//Проверка ответных сообщений
    		byte oldMode = users[i].getMode();
    		assertEquals(
        		assumedResponses,
        		new CommandHandler().processCommand("/help", users[i])
        		);
    		//Проверка изменения режима (не должен поменяться)
    		assertEquals(oldMode, users[i].getMode());
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
    	//Препдполагаемый список ответных сообщений
    	ArrayList<String> assumedResponses = new ArrayList<String>();
    	assumedResponses.add(menuCommand);
    	
    	//Проход по всем пользователям
    	for (int i = 0; i < modesAmount; ++i) {
    		//Проверка ответных сообщений
        	assertEquals(
        			assumedResponses,
        			new CommandHandler().processCommand("/menu", users[i])
        			);
        	//Проверка изменения режима (должен стать 0 - выход в меню)
    		assertEquals((byte) 0, users[i].getMode());
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
    	//Препдполагаемый список ответных сообщений
    	ArrayList<String> assumedResponses = new ArrayList<String>();
    	assumedResponses.add(echoCommand);
    	
    	//Проход по всем пользователям
    	for (int i = 0; i < modesAmount; ++i) {
    		//Проверка ответных сообщений
        	assertEquals(
        			assumedResponses,
        			new CommandHandler().processCommand("/echo", users[i])
        			);
        	//Проверка изменения режима (должен стать 1 - режим эхо)
    		assertEquals((byte) 1, users[i].getMode());
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
    	//Препдполагаемый список ответных сообщений
    	ArrayList<String> assumedResponses = new ArrayList<String>();
    	assumedResponses.add(newsinglegameCommand);
    	
    	//Проход по всем пользователям
    	for (int i = 0; i < modesAmount; ++i) {
    		//Проверка ответных сообщений
        	assertEquals(
        			assumedResponses,
        			new CommandHandler().processCommand("/newsinglegame", users[i])
        			);
        	//Проверка изменения режима (должен стать 2 - новая одиночная игра)
    		assertEquals((byte) 2, users[i].getMode());
    	}
    }
}
