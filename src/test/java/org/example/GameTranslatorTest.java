package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.example.chess.GameHandler;

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
	private final static byte[][] BOARD = {
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	/**
	 * Проверить обычный вывод доски
	 */
	@Test
	public void sideTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.moveProperty.REGULAR, BOARD, true);
		Assertions.assertTrue(boardString.contains("Ход белых"));
		boardString = gameTranslator.chessboardString(
				GameHandler.moveProperty.REGULAR, BOARD, false);
		Assertions.assertTrue(boardString.contains("Ход чёрных"));
	}
	
	/**
	 * Проверить вывод при невозможном ходе
	 */
	@Test
	public void impossibleMoveTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.moveProperty.IMPOSSIBLE, BOARD, true);
		Assertions.assertTrue(boardString.contains(
				"Невозможный ход! Попробуйте снова."));
	}
	
	/**
	 * Проверить вывод при неверном ходе
	 */
	@Test
	public void invalidMoveTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.moveProperty.INVALID, BOARD, true);
		Assertions.assertTrue(
				boardString.contains("Неверная запись хода! Попробуйте снова."));
	}
	
	/**
	 * Проверить вывод при шахе
	 */
	@Test
	public void checkMoveTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.moveProperty.CHECK, BOARD, true);
		Assertions.assertTrue(boardString.contains(
				"Шах! Ваш король под угрозой!"));
	}
	
	/**
	 * Проверить вывод при обычном ходе
	 */
	@Test
	public void mateMoveTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.moveProperty.MATE, BOARD, false);
		Assertions.assertTrue(boardString.contains(
				"Шах и мат! Партия окончена. Победили белые."));
		boardString = gameTranslator.chessboardString(
				GameHandler.moveProperty.MATE, BOARD, true);
		Assertions.assertTrue(boardString.contains(
				"Шах и мат! Партия окончена. Победили чёрные."));
	}
}
