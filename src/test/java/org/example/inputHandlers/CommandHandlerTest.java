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
		userState.setUserState(UserState.userStatus.IN_GAME);
		commandHandler.processCommand("start", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.MAIN_MENU);
		commandHandler.processCommand("start", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.MAIN_MENU);
	}
	
	/**
	 * Проверить команду /quit
	 */
	@Test
	public void quitCommandTest() {
		userState.setUserState(UserState.userStatus.IN_GAME);
		commandHandler.processCommand("quit", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.MAIN_MENU);
		commandHandler.processCommand("quit", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.MAIN_MENU);
	}
	
	/**
	 * Проверить команду /help
	 */
	@Test
	public void helpCommandTest() {
		userState.setUserState(UserState.userStatus.MAIN_MENU);
		commandHandler.processCommand("help", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.MAIN_MENU);
		userState.setUserState(UserState.userStatus.IN_GAME);
		commandHandler.processCommand("help", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.IN_GAME);
	}
	
	/**
	 * Проверить команду /newsinglegame
	 */
	@Test
	public void newsinglegameCommandTest() {
		userState.setUserState(UserState.userStatus.MAIN_MENU);
		commandHandler.processCommand("newsinglegame", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.IN_GAME);
		commandHandler.processCommand("newsinglegame", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.IN_GAME);
	}
	
	/**
	 * Проверить неизвестную команду
	 */
	@Test
	public void unknownCommandTest() {
		userState.setUserState(UserState.userStatus.MAIN_MENU);
		commandHandler.processCommand("unknown", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.MAIN_MENU);
		userState.setUserState(UserState.userStatus.IN_GAME);
		commandHandler.processCommand("unknown", "", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.IN_GAME);
	}
}
