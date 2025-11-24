package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.example.states.LobbyState;
import org.example.states.UserState;
import org.example.statesHandlers.FakeStatesHandler;

/**
 * Проверка обработчика команд
 */
public class CommandHandlerTest {	
	/**
	 * Хранитель состояний для проверки их изменения
	 */
	private final FakeStatesHandler states = new FakeStatesHandler();
	
	/**
	 * Обработчик команд
	 */
	private final CommandHandler commandHandler = new CommandHandler(states);
	
	/**
	 * Проверить работу команды /quit на одном пользователе
	 */
	@Test
	public void quitCommandSingleUserTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.INGAME);
		states.createNewLobby("game", 1, 1, true, LobbyState.LobbyType.SINGLEPLAYER);
		states.setUserLobbyName(1, "game");
		commandHandler.processQuitCommand(1);
		Assertions.assertEquals(UserState.UserStatus.MAINMENU, states.getUserStatus(1));
		Assertions.assertEquals("", states.getUserLobbyName(1));
		Assertions.assertFalse(states.isLobbyExisting("game"));
	}
	
	/**
	 * Проверить работу команды /quit на двух пользователях
	 */
	@Test
	public void quitCommandTwoUsersTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.INGAME);
		states.addNewUserWithStatus(2, UserState.UserStatus.INGAME);
		states.createNewLobby("game", 1, 2, true, LobbyState.LobbyType.MULTIPLAYER);
		states.setUserLobbyName(1, "game");
		states.setUserLobbyName(2, "game");
		commandHandler.processQuitCommand(1);
		Assertions.assertEquals(UserState.UserStatus.MAINMENU, states.getUserStatus(1));
		Assertions.assertEquals(UserState.UserStatus.MAINMENU, states.getUserStatus(2));
		Assertions.assertEquals("", states.getUserLobbyName(1));
		Assertions.assertEquals("", states.getUserLobbyName(2));
		Assertions.assertFalse(states.isLobbyExisting("game"));
	}
	
	/**
	 * Проверить работу команды /new_local
	 */
	@Test
	public void newLocalCommandSingleUserTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.MAINMENU);
		commandHandler.processNewLocalCommand(1);
		Assertions.assertEquals(UserState.UserStatus.INGAME, states.getUserStatus(1));
		Assertions.assertTrue(states.isLobbyExisting("1"));
		Assertions.assertFalse(states.isLobbyAvailable("1"));
		Assertions.assertEquals(LobbyState.LobbyType.SINGLEPLAYER, states.getLobbyType("1"));
		Assertions.assertEquals("1", states.getUserLobbyName(1));
		Assertions.assertEquals(1, states.getLobbyFirstPlayer("1"));
		Assertions.assertEquals(1, states.getLobbySecondPlayer("1"));
	}
	
	/**
	 * Проверить создание матча
	 */
	@Test
	public void createValidLobbyNameTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.MAINMENU);
		try {
			commandHandler.processCreateCommand(1, "game");
		} catch (CommandException e) {
			Assertions.assertFalse(true);
		}
		Assertions.assertEquals(UserState.UserStatus.AWAITING, states.getUserStatus(1));
		Assertions.assertEquals("game", states.getUserLobbyName(1));
		Assertions.assertTrue(states.isLobbyExisting("game"));
		Assertions.assertTrue(states.isLobbyAvailable("game"));
	}
	
	/**
	 * Проверить создание матча без аргумента
	 */
	@Test
	public void createNoLobbyNameExceptionTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.MAINMENU);
		CommandException exception = Assertions.assertThrows(
				CommandException.class, () ->
				commandHandler.processCreateCommand(1, ""));
		Assertions.assertEquals(
				"Придумайте название для матча (не более 16 символов):", 
				exception.getMessage());
		Assertions.assertEquals(UserState.UserStatus.CREATING, states.getUserStatus(1));
	}
	
	/**
	 * Проверить создание матча со слишком длинным названием
	 */
	@Test
	public void createTooLongLobbyNameExceptionTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.MAINMENU);
		CommandException exception = Assertions.assertThrows(
				CommandException.class, () ->
				commandHandler.processCreateCommand(1, "abcdefghijklmnopqrstuvwxyz"));
		Assertions.assertEquals(
				"Извините, название должно быть не более 16 символов. Придумайте другое:", 
				exception.getMessage());
		Assertions.assertEquals(UserState.UserStatus.CREATING, states.getUserStatus(1));
	}
	
	/**
	 * Проверить создание матча с занятым названием
	 */
	@Test
	public void createExistingLobbyNameExceptionTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.MAINMENU);
		states.addNewUserWithStatus(2, UserState.UserStatus.MAINMENU);
		try {
			commandHandler.processCreateCommand(2, "game");
		} catch (CommandException e) {
			Assertions.assertFalse(true);
		}
		CommandException exception = Assertions.assertThrows(
				CommandException.class, () ->
				commandHandler.processCreateCommand(1, "game"));
		Assertions.assertEquals(
				"Извините, данное название уже занято. Придумайте другое:", 
				exception.getMessage());
		Assertions.assertEquals(UserState.UserStatus.CREATING, states.getUserStatus(1));
	}
	
	/**
	 * Проверить присоединение к матчу
	 */
	@Test
	public void joinValidLobbyNameTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.MAINMENU);
		states.addNewUserWithStatus(2, UserState.UserStatus.MAINMENU);
		try {
			commandHandler.processCreateCommand(1, "game");
			commandHandler.processJoinCommand(2, "game");
		} catch (CommandException e) {
			Assertions.assertFalse(true);
		}
		Assertions.assertEquals(UserState.UserStatus.INGAME, states.getUserStatus(1));
		Assertions.assertEquals(UserState.UserStatus.INGAME, states.getUserStatus(2));
		Assertions.assertEquals("game", states.getUserLobbyName(1));
		Assertions.assertEquals("game", states.getUserLobbyName(2));
		Assertions.assertTrue(states.isLobbyExisting("game"));
		Assertions.assertFalse(states.isLobbyAvailable("game"));
		Assertions.assertEquals(LobbyState.LobbyType.MULTIPLAYER, states.getLobbyType("game"));
		Assertions.assertEquals(1, states.getLobbyFirstPlayer("game"));
		Assertions.assertEquals(2, states.getLobbySecondPlayer("game"));
	}
	
	/**
	 * Проверить присоединение к матчу без аргумента
	 */
	@Test
	public void joinNoLobbyNameExceptionTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.MAINMENU);
		CommandException exception = Assertions.assertThrows(
				CommandException.class, () ->
				commandHandler.processJoinCommand(1, ""));
		Assertions.assertEquals(
				"""
				Вот список доступных сейчас матчей.
				Нажмите на название или введите его, чтобы присоединиться.
				Введите /quit, чтобы выйти.
				""", 
				exception.getMessage());
		Assertions.assertEquals(UserState.UserStatus.CHOOSING, states.getUserStatus(1));
	}
	
	/**
	 * Проверить присоединение к несуществующему матчу
	 */
	@Test
	public void joinNotExistingLobbyNameExceptionTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.MAINMENU);
		CommandException exception = Assertions.assertThrows(
				CommandException.class, () ->
				commandHandler.processJoinCommand(1, "game"));
		Assertions.assertEquals(
				"Матча с таким идентификатором не существует", 
				exception.getMessage());
		Assertions.assertEquals(UserState.UserStatus.CHOOSING, states.getUserStatus(1));
	}
	
	/**
	 * Проверить присоединение к идущему матчу
	 */
	@Test
	public void joinNotAvailableLobbyNameExceptionTest() {
		states.resetAll();
		states.addNewUserWithStatus(1, UserState.UserStatus.MAINMENU);
		states.addNewUserWithStatus(2, UserState.UserStatus.MAINMENU);
		states.addNewUserWithStatus(3, UserState.UserStatus.MAINMENU);
		try {
			commandHandler.processCreateCommand(2, "game");
			commandHandler.processJoinCommand(3, "game");
		} catch (CommandException e) {
			Assertions.assertFalse(true);
		}
		CommandException exception = Assertions.assertThrows(
				CommandException.class, () ->
				commandHandler.processJoinCommand(1, "game"));
		Assertions.assertEquals(
				"Этот матч уже начат, Вы не можете к нему подключиться", 
				exception.getMessage());
		Assertions.assertEquals(UserState.UserStatus.CHOOSING, states.getUserStatus(1));
	}
}
