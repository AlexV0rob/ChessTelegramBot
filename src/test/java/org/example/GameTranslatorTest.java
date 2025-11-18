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
			{0, -1, 0, 0, 0, 0, 0, 0},
			{1, 0, -2, 0, 0, 0, 0, 0},
			{0, 6, 0, -3, 0, 0, 0, 0},
			{0, 0, 5, 0, -4, 0, 0, 0},
			{0, 0, 0, 4, 0, -5, 0, 0},
			{0, 0, 0, 0, 3, 0, -6, 0},
			{0, 0, 0, 0, 0, 2, 0, -1},
			{0, 0, 0, 0, 0, 0, 1, 0}
	};
	
	/**
	 * Строка с доской с белой стороны
	 */
	private final static String BOARD_STRING_WHITE = """
Ход белых

8  [      ][      ][      ][      ][      ][      ][ BP][      ]
7  [      ][      ][      ][      ][      ][ BR][      ][WP]
6  [      ][      ][      ][      ][ BN][      ][WK][      ]
5  [      ][      ][      ][ BB][      ][WQ][      ][      ]
4  [      ][      ][ BQ][      ][WB][      ][      ][      ]
3  [      ][ BK][      ][WN][      ][      ][      ][      ]
2  [ BP][      ][WR][      ][      ][      ][      ][      ]
1  [      ][WP][      ][      ][      ][      ][      ][      ]
      A      B      C      D      E      F      G      H     \s
			""";
	/**
	 * Строка с доской с чёрной стороны
	 */
	private final static String BOARD_STRING_BLACK = """
Ход чёрных

1  [      ][      ][      ][      ][      ][      ][WP][      ]
2  [      ][      ][      ][      ][      ][WR][      ][ BP]
3  [      ][      ][      ][      ][WN][      ][ BK][      ]
4  [      ][      ][      ][WB][      ][ BQ][      ][      ]
5  [      ][      ][WQ][      ][ BB][      ][      ][      ]
6  [      ][WK][      ][ BN][      ][      ][      ][      ]
7  [WP][      ][ BR][      ][      ][      ][      ][      ]
8  [      ][ BP][      ][      ][      ][      ][      ][      ]
      H      G      F      E      D      C      B      A     \s
			""";
	
	/**
	 * Проверить обычный вывод доски
	 */
	@Test
	public void sideTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.MoveProperty.REGULAR, BOARD, true);
		Assertions.assertEquals(BOARD_STRING_WHITE, boardString);
		boardString = gameTranslator.chessboardString(
				GameHandler.MoveProperty.REGULAR, BOARD, false);
		Assertions.assertEquals(BOARD_STRING_BLACK, boardString);
	}
	
	/**
	 * Проверить вывод при невозможном ходе
	 */
	@Test
	public void impossibleMoveTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.MoveProperty.IMPOSSIBLE, BOARD, true);
		Assertions.assertEquals("""
				%sНевозможный ход! Попробуйте снова.
				""".formatted(BOARD_STRING_WHITE), boardString);
	}
	
	/**
	 * Проверить вывод при неверном ходе
	 */
	@Test
	public void invalidMoveTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.MoveProperty.INVALID, BOARD, true);
		Assertions.assertEquals("""
				%sНеверная запись хода! Попробуйте снова.
				""".formatted(BOARD_STRING_WHITE), boardString);
	}
	
	/**
	 * Проверить вывод при шахе
	 */
	@Test
	public void checkMoveTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.MoveProperty.CHECK, BOARD, true);
		Assertions.assertEquals("""
				%sШах! Ваш король под угрозой!
				""".formatted(BOARD_STRING_WHITE), boardString);
	}
	
	/**
	 * Проверить вывод при обычном ходе
	 */
	@Test
	public void mateMoveTest() {
		String boardString = gameTranslator.chessboardString(
				GameHandler.MoveProperty.MATE, BOARD, false);
		Assertions.assertEquals("""
				%sШах и мат! Партия окончена. Победили чёрные.
				""".formatted(BOARD_STRING_BLACK), boardString);
		boardString = gameTranslator.chessboardString(
				GameHandler.MoveProperty.MATE, BOARD, true);
		Assertions.assertEquals("""
				%sШах и мат! Партия окончена. Победили белые.
				""".formatted(BOARD_STRING_WHITE), boardString);
	}
}
