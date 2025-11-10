package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.chess.PositionOnBoard;

import org.example.states.GameState;

/**
 * Проверить игрового переводчика
 */
public class GameTranslatorTest {
	/**
	 * Игровой переводчик
	 */
	private GameTranslator gameTranslator = new GameTranslator();
	
	/**
	 * Доска, на которой идёт проверка
	 */
	private final static byte[][] START_BOARD = {
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, -1, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 6, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}
		};
	
	/**
	 * Проверить обычный вывод доски
	 */
	@Test
	public void regularBoardTest() {
		GameState gameState = new GameState(START_BOARD, 8, true);
		String boardBefore = gameTranslator.currentBoardState(gameState);
		Assertions.assertTrue(gameState.isWhiteToMove());
		Assertions.assertEquals(-1, gameState.getBoard()[1][3]);
		Assertions.assertEquals(0, gameState.getBoard()[2][3]);
		Assertions.assertTrue(boardBefore.contains("Ход белых"));
	}
	
	/**
	 * Проверить вывод при невозможном ходе
	 */
	@Test
	public void impossibleMoveTest() {
		GameState gameState = new GameState(START_BOARD, 8, true);
		String impossibleMove = gameTranslator.makeMove(
				1, new PositionOnBoard(1, 3), new PositionOnBoard(2, 4), gameState);
		Assertions.assertTrue(gameState.isWhiteToMove());
		Assertions.assertEquals(-1, gameState.getBoard()[1][3]);
		Assertions.assertEquals(0, gameState.getBoard()[2][4]);
		Assertions.assertTrue(impossibleMove.contains("Ход белых"));
		Assertions.assertTrue(
				impossibleMove.contains("Невозможный ход! Попробуйте снова."));
	}
	
	/**
	 * Проверить вывод при неверном ходе
	 */
	@Test
	public void invalidMoveTest() {
		GameState gameState = new GameState(START_BOARD, 8, true);
		String invalidMove = gameTranslator.makeMove(
				1, new PositionOnBoard(1, 3), new PositionOnBoard(2, 9), gameState);
		Assertions.assertTrue(gameState.isWhiteToMove());
		Assertions.assertEquals(-1, gameState.getBoard()[1][3]);
		Assertions.assertTrue(invalidMove.contains("Ход белых"));
		Assertions.assertTrue(
				invalidMove.contains("Неверная запись хода! Попробуйте снова."));
	}
	
	/**
	 * Проверить вывод при шахе
	 */
	@Test
	public void checkMoveTest() {
		GameState gameState = new GameState(START_BOARD, 8, true);
		String checkMove = gameTranslator.makeMove(
				1, new PositionOnBoard(1, 3), new PositionOnBoard(2, 3), gameState);
		Assertions.assertFalse(gameState.isWhiteToMove());
		Assertions.assertEquals(-1, gameState.getBoard()[2][3]);
		Assertions.assertEquals(0, gameState.getBoard()[1][3]);
		Assertions.assertTrue(checkMove.contains("Ход чёрных"));
		Assertions.assertTrue(
				checkMove.contains("Шах! Ваш король под угрозой!"));
	}
	
	/**
	 * Проверить вывод при обычном ходе
	 */
	@Test
	public void normalMoveTest() {
		GameState gameState = new GameState(START_BOARD, 8, true);
		String normalMove = gameTranslator.makeMove(
				1, new PositionOnBoard(1, 3), new PositionOnBoard(3, 3), gameState);
		Assertions.assertFalse(gameState.isWhiteToMove());
		Assertions.assertEquals(-1, gameState.getBoard()[3][3]);
		Assertions.assertEquals(0, gameState.getBoard()[1][3]);
		Assertions.assertTrue(normalMove.contains("Ход чёрных"));
		Assertions.assertFalse(
				normalMove.contains("Невозможный ход! Попробуйте снова."));
		Assertions.assertFalse(
				normalMove.contains("Неверная запись хода! Попробуйте снова."));
		Assertions.assertFalse(
				normalMove.contains("Шах! Ваш король под угрозой!"));
	}
}
