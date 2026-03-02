package org.example.inputHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.states.UserState;

/**
 * Проверка обработчика команд
 */
public class CommandHandlerTest {
	/**
	 * Обработчик команд
	 */
	private final CommandHandler commandHandler = new CommandHandler();
	
	/**
	 * Проверить команду /start
	 */
	@Test
	public void startCommandTest() {
		UserState userState = new UserState();
		commandHandler.processCommand("start", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.MAIN_MENU);
		commandHandler.processCommand("start", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.MAIN_MENU);
	}
	
	/**
	 * Проверить команду /quit
	 */
	@Test
	public void quitCommandTest() {
		UserState userState = new UserState();
		commandHandler.processCommand("quit", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.MAIN_MENU);
		commandHandler.processCommand("quit", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.MAIN_MENU);
	}
	
	/**
	 * Проверить команду /help
	 */
	@Test
	public void helpCommandTest() {
		UserState userState = new UserState();
		commandHandler.processCommand("help", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.MAIN_MENU);
		commandHandler.processCommand("newsinglegame", "", userState);
		commandHandler.processCommand("help", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.IN_GAME);
	}
	
	/**
	 * Проверить команду /newsinglegame
	 */
	@Test
	public void newsinglegameCommandTest() {
		UserState userState = new UserState();
		commandHandler.processCommand("newsinglegame", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.IN_GAME);
		commandHandler.processCommand("newsinglegame", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.IN_GAME);
	}
	
	/**
	 * Проверить неизвестную команду
	 */
	@Test
	public void unknownCommandTest() {
		UserState userState = new UserState();
		commandHandler.processCommand("unknown", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.MAIN_MENU);
		commandHandler.processCommand("newsinglegame", "", userState);
		commandHandler.processCommand("unknown", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.UserStatus.IN_GAME);
	}
}
