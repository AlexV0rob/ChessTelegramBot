package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Проверка работы основного обработчика пользовательского ввода
 */
public class MainLogicTest {
	/**
	 * Основной логический обработчик ввода
	 */
	private final MainLogic mainLogic = new MainLogic();

	/**
	 * Болванка с игровым состоянием
	 */
	private final GameState gameState = new GameState();
	
	/**
	 * Проверка включения игры любым некомандным вводом
	 */
	@Test
	void nonCommandGameEnablingTest() {
		mainLogic.processUserInput("something", gameState);
		Assertions.assertTrue(gameState.isInGame());
	}
	
	/**
	 * Проверка работы текстовой формы хода
	 */
	@Test
	void textMoveTest() {
		mainLogic.processUserInput("/newsinglegame", gameState);
		String stringBoard;
		stringBoard = mainLogic.processUserInput("pe2e4", gameState).getFirst();
		Assertions.assertFalse(
				stringBoard.contains("Неверная запись хода! Попробуйте снова."));
		stringBoard = mainLogic.processUserInput("abrakadabra", gameState).getFirst();
		Assertions.assertTrue(
				stringBoard.contains("Неверная запись хода! Попробуйте снова."));
	}
	
	/**
	 * Проверка работы callback запроса
	 */
	@Test
	void callbackQueryTest() {
		mainLogic.processUserInput("/newsinglegame", gameState);
		//Пешка
		mainLogic.processUserInput("callback_" + ((char) 1), gameState);
		//e2
		mainLogic.processUserInput("callback_" + ((char) 52), gameState);
		//e4
		mainLogic.processUserInput("callback_" + ((char) 36), gameState);
		Assertions.assertTrue(gameState.isMoveReady());
		Assertions.assertEquals("PE2E4", gameState.assembleMove());
	}
}
