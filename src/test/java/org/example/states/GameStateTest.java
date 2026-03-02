package org.example.states;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.chess.PositionOnBoard;

/**
 * Проверка хранителя состояния игры
 */
public class GameStateTest {
	/**
	 * Маленькая доска, на которой идёт проверка
	 */
	private final byte[][] miniBoard = {{0, 0}, {1, 0}};
	
	/**
	 * Состояние игры
	 */
	private final GameState gameState = new GameState(miniBoard, 2, true);
	
	/**
	 * Проверить передвижение фигуры
	 */
	@Test
	public void figureMoveTest() {
		gameState.moveFigure(new PositionOnBoard(1, 0), new PositionOnBoard(0, 1));
		byte[][] expectedBoard = {{0, 1}, {0, 0}};
		Assertions.assertArrayEquals(expectedBoard, gameState.getBoard());
	}
	
	/**
	 * Проверить смену сторон
	 */
	@Test
	public void changeSideTest() {
		gameState.changeSide();
		Assertions.assertFalse(gameState.isWhiteToMove());
		gameState.changeSide();
		Assertions.assertTrue(gameState.isWhiteToMove());
	}
}
