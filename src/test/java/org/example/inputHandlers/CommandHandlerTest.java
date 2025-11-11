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
	 * Состояние работы пользователя
	 */
	private final UserState userState = new UserState();
	
	/**
	 * Проверить команду /start
	 */
	@Test
	public void startCommandTest() {
		userState.setUserState(UserState.USER_STATE.INGAME);
		commandHandler.processCommand("start", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.MAINMENU);
		commandHandler.processCommand("start", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.MAINMENU);
	}
	
	/**
	 * Проверить команду /quit
	 */
	@Test
	public void quitCommandTest() {
		userState.setUserState(UserState.USER_STATE.INGAME);
		commandHandler.processCommand("quit", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.MAINMENU);
		commandHandler.processCommand("quit", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.MAINMENU);
	}
	
	/**
	 * Проверить команду /help
	 */
	@Test
	public void helpCommandTest() {
		userState.setUserState(UserState.USER_STATE.MAINMENU);
		commandHandler.processCommand("help", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.MAINMENU);
		userState.setUserState(UserState.USER_STATE.INGAME);
		commandHandler.processCommand("help", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.INGAME);
	}
	
	/**
	 * Проверить команду /newsinglegame
	 */
	@Test
	public void newsinglegameCommandTest() {
		userState.setUserState(UserState.USER_STATE.MAINMENU);
		commandHandler.processCommand("newsinglegame", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.INGAME);
		commandHandler.processCommand("newsinglegame", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.INGAME);
	}
	
	/**
	 * Проверить неизвестную команду
	 */
	@Test
	public void unknownCommandTest() {
		userState.setUserState(UserState.USER_STATE.MAINMENU);
		commandHandler.processCommand("unknown", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.MAINMENU);
		userState.setUserState(UserState.USER_STATE.INGAME);
		commandHandler.processCommand("unknown", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.INGAME);
	}
}
