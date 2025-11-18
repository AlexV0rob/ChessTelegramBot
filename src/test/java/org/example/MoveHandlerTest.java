package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.chess.GameHandler;
import org.example.chess.PositionOnBoard;
import org.example.states.GameState;

/**
 * Проверка обработчика хода и частей хода
 */
public class MoveHandlerTest {
	/**
	 * Обработчик хода и частей хода
	 */
	private final MoveHandler moveHandler = new MoveHandler();
	
	/**
	 * Обработчик игры
	 */
	private final GameHandler gameHandler = new GameHandler();
	
	/**
	 * Доска, на которой идёт проверка 
	 */
	private final static byte[][] START_BOARD = {
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, -1, -1, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	/**
	 * Проверить обработку хода
	 */
	@Test
	public void moveInputTest() {
		GameState gameStateReal = new GameState(START_BOARD, 8, true);
		GameState gameStateExpected = new GameState(START_BOARD, 8, true);
		GameHandler.MoveProperty moveReal = 
				moveHandler.processMove("", "e2", "e4", gameStateReal);
		GameHandler.MoveProperty moveExpected = gameHandler.processMove(
				0, new PositionOnBoard(1, 4), 
				new PositionOnBoard(3, 4), gameStateExpected);
		Assertions.assertArrayEquals(
				gameStateExpected.getBoard(), gameStateReal.getBoard());
		Assertions.assertEquals(
				gameStateExpected.isWhiteToMove(), gameStateReal.isWhiteToMove());
		Assertions.assertEquals(moveExpected, moveReal);
	}
}
