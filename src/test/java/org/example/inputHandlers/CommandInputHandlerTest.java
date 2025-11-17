package org.example.inputHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.auxiliary.CommandResults;

/**
 * Проверка обработчика команд
 */
public class CommandInputHandlerTest {
	/**
	 * Обработчик команд
	 */
	private final CommandInputHandler commandHandler = new CommandInputHandler();
	
	/**
	 * Проверить команду /start
	 */
	@Test
	public void startCommandTest() {
		CommandResults results = commandHandler.processCommand("start");
		Assertions.assertEquals(CommandResults.LobbyStatus.CLOSE, 
				results.userLobbyStatus());
	}
	
	/**
	 * Проверить команду /quit
	 */
	@Test
	public void quitCommandTest() {
		CommandResults results = commandHandler.processCommand("quit");
		Assertions.assertEquals(CommandResults.LobbyStatus.CLOSE, 
				results.userLobbyStatus());
	}
	
	/**
	 * Проверить команду /help
	 */
	@Test
	public void helpCommandTest() {
		CommandResults results = commandHandler.processCommand("help");
		Assertions.assertEquals(CommandResults.LobbyStatus.NOTHING, 
				results.userLobbyStatus());
	}
	
	/**
	 * Проверить команду /newsinglegame
	 */
	@Test
	public void newsinglegameCommandTest() {
		CommandResults results = commandHandler.processCommand("newsinglegame");
		Assertions.assertEquals(CommandResults.LobbyStatus.SINGLEPLAYER, 
				results.userLobbyStatus());
	}
	
	/**
	 * Проверить неизвестную команду
	 */
	@Test
	public void unknownCommandTest() {
		CommandResults results = commandHandler.processCommand("unknown");
		Assertions.assertEquals(CommandResults.LobbyStatus.NOTHING, 
				results.userLobbyStatus());
	}
}
