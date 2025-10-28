package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Проверка работы игрового состояния
 */
public class GameStateTest {
	/**
	 * Болванка с игровым состоянием
	 */
	private final GameState gameState = new GameState();
	
	/**
	 * Проверка вывода доски
	 */
	@Test
	void printBoardTest() {
		String stringBoard;
		stringBoard = gameState.printBoard(GameState.MOVE_PROPERTIES.INVALID);
		Assertions.assertTrue(
				stringBoard.contains("Неверная запись хода! Попробуйте снова."));
		stringBoard = gameState.printBoard(GameState.MOVE_PROPERTIES.IMPOSSIBLE);
		Assertions.assertTrue(
				stringBoard.contains("Невозможный ход! Попробуйте снова."));
		stringBoard = gameState.printBoard(GameState.MOVE_PROPERTIES.CHECK);
		Assertions.assertTrue(
				stringBoard.contains("Шах! Ваш король под угрозой!"));
		stringBoard = gameState.printBoard(GameState.MOVE_PROPERTIES.CHECKMATE);
		Assertions.assertTrue(
				stringBoard.contains("Шах и мат! Партия окончена."));
	}
	
	/**
	 * Проверка установки значения доски
	 */
	@Test
	void setBoardTest() {
		byte[] newBoard = {1, 3, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 4};
		gameState.setBoard(newBoard);
		byte[] board = gameState.getBoard();
		Assertions.assertTrue(java.util.Arrays.equals(
				new byte[] {
						1, 3, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 4, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0
				},
				board));
	}
	
	/**
	 * Проверка передвижения фигуры
	 */
	@Test
	void figureMovingTest() {
		byte[] newBoard = {1, 2};
		gameState.setBoard(newBoard);
		gameState.moveFigure(0, 1);
		byte[] board = gameState.getBoard();
		Assertions.assertTrue(java.util.Arrays.equals(
				new byte[] {
						0, 1, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0
				},
				board));
	}
	
	/**
	 * Проверка смены состояния хода и его сборки
	 */
	@Test
	void moveAssemblingTest() {
		//Пешка
		gameState.changeMoveState(1);
		//e2
		gameState.changeMoveState(52);
		//e4
		gameState.changeMoveState(36);
		Assertions.assertTrue(gameState.isMoveReady());
		Assertions.assertEquals("PE2E4", gameState.assembleMove());
	}
}
