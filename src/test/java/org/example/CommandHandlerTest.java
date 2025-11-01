package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Проверка работы команд
 */
public class CommandHandlerTest {
	/**
	 * Болванка с игровым состоянием
	 */
	private final GameState gameState = new GameState();
	/**
	 * Экземпляр обработчика команд
	 */
	private final CommandHandler commandHandler = new CommandHandler();
	
	/**
	 * Проверка команды /start
	 */
	@Test
	void startCommandTest() {
		gameState.setState(GameState.STATES.INGAME);
		commandHandler.processCommand("/start", gameState);
		Assertions.assertTrue(gameState.isNoGame());
	}
	
	/**
	 * Проверка команды /help
	 */
	@Test
	void helpCommandTest() {
		gameState.setState(GameState.STATES.INGAME);;
		commandHandler.processCommand("/help", gameState);
		Assertions.assertTrue(gameState.isInGame());
	}
	
	/**
	 * Проверка команды /quit
	 */
	@Test
	void quitCommandTest() {
		gameState.setState(GameState.STATES.INGAME);
		commandHandler.processCommand("/quit", gameState);
		Assertions.assertTrue(gameState.isNoGame());
	}
	
	/**
	 * Проверка команды /newsinglegame
	 */
	@Test
	void newsinglegameCommandTest() {
		gameState.setState(GameState.STATES.NOGAME);
		commandHandler.processCommand("/newsinglegame", gameState);
		Assertions.assertTrue(gameState.isInGame());
	}
	/**
	 * Проверка неизвестной команды
	 */
	@Test
	void unknownCommandTest() {
		gameState.setState(GameState.STATES.INGAME);
		commandHandler.processCommand("/unknown", gameState);
		Assertions.assertTrue(gameState.isInGame());
	}
}
