package org.example;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.states.UserState;
import org.example.statesHandlers.FakeStatesHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Проверка обработчика игрового ввода
 */
public class GameInputHandlerTest {
	/**
	 * Хранитель состояний для проверки их изменения
	 */
	private final FakeStatesHandler states = new FakeStatesHandler();
	
	/**
	 * Обработчик ввода в игре
	 */
	private final GameInputHandler gameInputHandler = new GameInputHandler(states);
	
	/**
	 * Доска для проверки мата
	 */
	private final static byte[][] CHECKMATE_BOARD = {
			{6, 0, 0, 0, 0, 0, 0, 0},
			{-5, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	/**
	 * Проверить ввод части хода в одиночной игре
	 */
	@Test
	public void movePartSingleGameTest() {
		states.resetAll();
		long userId = states.createStandardSingleGame("game");
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMovePart(userId, "p");
		Assertions.assertIterableEquals(List.of("Ваш ход: ПЕШКА"), gameResponses.getKey());
		Assertions.assertIterableEquals(List.of(), gameResponses.getValue());
	}
	
	/**
	 * Проверить ввод части хода в многопользовательской игре
	 */
	@Test
	public void movePartMultiGameTest() {
		states.resetAll();
		ImmutablePair<Long, Long> ids = states.createStandardMultiGame("game");
		long userId = ids.getKey();
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMovePart(userId, "p");
		Assertions.assertIterableEquals(List.of("Ваш ход: ПЕШКА"), gameResponses.getKey());
		Assertions.assertIterableEquals(List.of(), gameResponses.getValue());
	}
	
	/**
	 * Проверить ввод хода по частям в одиночной игре
	 */
	@Test
	public void moveByPartsSingleGameTest() {
		states.resetAll();
		long userId = states.createStandardSingleGame("game");
		gameInputHandler.processMovePart(userId, "p");
		gameInputHandler.processMovePart(userId, "e2");
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMovePart(userId, "e4");
		Assertions.assertIterableEquals(
				List.of("Ваш ход: ПЕШКА E2 E4", 
						"""
Ход чёрных

1  [WR][WN][WB][WK][WQ][WB][WN][WR]
2  [WP][WP][WP][      ][WP][WP][WP][WP]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][WP][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
8  [ BR][ BN][ BB][ BK][ BQ][ BB][ BN][ BR]
      H      G      F      E      D      C      B      A     \s
				""", 
				"Ваш ход: "), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][WP][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][      ][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
						""", 
						"Сейчас ходит противник."), 
				gameResponses.getValue());
	}
	
	/**
	 * Проверить ввод хода по частям в многопользовательской игре
	 */
	@Test
	public void moveByPartsMultiGameTest() {
		states.resetAll();
		ImmutablePair<Long, Long> ids = states.createStandardMultiGame("game");
		long userId = ids.getKey();
		gameInputHandler.processMovePart(userId, "p");
		gameInputHandler.processMovePart(userId, "e2");
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMovePart(userId, "e4");
		Assertions.assertIterableEquals(
				List.of("Ваш ход: ПЕШКА E2 E4", 
						"""
Ход чёрных

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][WP][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][      ][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
				""", 
				"Сейчас ходит противник."), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

1  [WR][WN][WB][WK][WQ][WB][WN][WR]
2  [WP][WP][WP][      ][WP][WP][WP][WP]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][WP][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
8  [ BR][ BN][ BB][ BK][ BQ][ BB][ BN][ BR]
      H      G      F      E      D      C      B      A     \s
						""", 
						"Ваш ход: "), 
				gameResponses.getValue());
	}
	
	/**
	 * Проверить ввод хода целиком в одиночной игре
	 */
	@Test
	public void moveEntireSingleGameTest() {
		states.resetAll();
		long userId = states.createStandardSingleGame("game");
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId, "", "e2", "e4");
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

1  [WR][WN][WB][WK][WQ][WB][WN][WR]
2  [WP][WP][WP][      ][WP][WP][WP][WP]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][WP][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
8  [ BR][ BN][ BB][ BK][ BQ][ BB][ BN][ BR]
      H      G      F      E      D      C      B      A     \s
						""", 
						"Ваш ход: "), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][WP][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][      ][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
						""", 
						"Сейчас ходит противник."), 
				gameResponses.getValue());
	}
	
	/**
	 * Проверить ввод хода целиком в многопользовательской игре
	 */
	@Test
	public void moveEntireMultiGameTest() {
		states.resetAll();
		ImmutablePair<Long, Long> ids = states.createStandardMultiGame("game");
		long userId = ids.getKey();
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId, "", "e2", "e4");
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][WP][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][      ][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
						""", 
						"Сейчас ходит противник."), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

1  [WR][WN][WB][WK][WQ][WB][WN][WR]
2  [WP][WP][WP][      ][WP][WP][WP][WP]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][WP][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
8  [ BR][ BN][ BB][ BK][ BQ][ BB][ BN][ BR]
      H      G      F      E      D      C      B      A     \s
						""", 
						"Ваш ход: "), 
				gameResponses.getValue());
	}
	
	/**
	 * Проверить ввод неуспешного хода (невозможный или неверный) целиком в одиночной игре
	 */
	@Test
	public void moveFailureSingleGameTest() {
		states.resetAll();
		long userId = states.createStandardSingleGame("game");
		ImmutablePair<List<String>, List<String>> gameResponsesImpossible = 
				gameInputHandler.processMove(userId, "q", "e1", "e4");
		ImmutablePair<List<String>, List<String>> gameResponsesInvalid = 
				gameInputHandler.processMove(userId, "", "e1", "e9");
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][WP][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
Невозможный ход! Попробуйте снова.
						""", 
						"Ваш ход: "), 
				gameResponsesImpossible.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][WP][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
Неверная запись хода! Попробуйте снова.
						""", 
						"Ваш ход: "), 
				gameResponsesInvalid.getKey());
		Assertions.assertIterableEquals(
				List.of(), gameResponsesImpossible.getValue());
		Assertions.assertIterableEquals(
				List.of(), gameResponsesInvalid.getValue());
	}
	
	/**
	 * Проверить ввод неуспешного хода (невозможный или неверный) в многопользовательской игре
	 */
	@Test
	public void moveFailureMultiGameTest() {
		states.resetAll();
		ImmutablePair<Long, Long> ids = states.createStandardMultiGame("game");
		long userId = ids.getKey();
		ImmutablePair<List<String>, List<String>> gameResponsesImpossible = 
				gameInputHandler.processMove(userId, "q", "e1", "e4");
		ImmutablePair<List<String>, List<String>> gameResponsesInvalid = 
				gameInputHandler.processMove(userId, "", "e1", "e9");
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][WP][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
Невозможный ход! Попробуйте снова.
						""", 
						"Ваш ход: "), 
				gameResponsesImpossible.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][WP][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
Неверная запись хода! Попробуйте снова.
						""", 
						"Ваш ход: "), 
				gameResponsesInvalid.getKey());
		Assertions.assertIterableEquals(
				List.of(), gameResponsesImpossible.getValue());
		Assertions.assertIterableEquals(
				List.of(), gameResponsesInvalid.getValue());
	}
	
	/**
	 * Проверить ввод шахового хода в одиночной игре
	 */
	@Test
	public void moveCheckSingleGameTest() {
		states.resetAll();
		long userId = states.createStandardSingleGameWithBoard("game", CHECKMATE_BOARD);
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId, "q", "a2", "b2");
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

1  [      ][      ][      ][      ][      ][      ][      ][ BK]
2  [      ][      ][      ][      ][      ][      ][WQ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][      ][      ][      ][      ]
8  [      ][      ][      ][      ][      ][      ][      ][      ]
      H      G      F      E      D      C      B      A     \s
Шах! Ваш король под угрозой!
						""",
						"Ваш ход: "), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][WQ][      ][      ][      ][      ][      ][      ]
1  [ BK][      ][      ][      ][      ][      ][      ][      ]
      A      B      C      D      E      F      G      H     \s
						""", 
						"Сейчас ходит противник."), 
				gameResponses.getValue());
	}
	
	/**
	 * Проверить ввод шахового хода в многопользовательской игре
	 */
	@Test
	public void moveCheckMultiGameTest() {
		states.resetAll();
		ImmutablePair<Long, Long> ids = states.createStandardMultiGameWithBoard("game", CHECKMATE_BOARD);
		long userId = ids.getKey();
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId, "q", "a2", "b2");
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][WQ][      ][      ][      ][      ][      ][      ]
1  [ BK][      ][      ][      ][      ][      ][      ][      ]
      A      B      C      D      E      F      G      H     \s
						""",
						"Сейчас ходит противник."), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

1  [      ][      ][      ][      ][      ][      ][      ][ BK]
2  [      ][      ][      ][      ][      ][      ][WQ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][      ][      ][      ][      ]
8  [      ][      ][      ][      ][      ][      ][      ][      ]
      H      G      F      E      D      C      B      A     \s
Шах! Ваш король под угрозой!
						""",
						"Ваш ход: "), 
				gameResponses.getValue());
	}
	
	/**
	 * Проверить ввод матового хода в одиночной игре
	 */
	@Test
	public void moveMateSingleGameTest() {
		states.resetAll();
		long userId = states.createStandardSingleGameWithBoard("game", CHECKMATE_BOARD);
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId, "q", "a2", "a1");
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
1  [WQ][      ][      ][      ][      ][      ][      ][      ]
      A      B      C      D      E      F      G      H     \s
Шах и мат! Партия окончена. Победили белые.
						""", 
						"Чем займёмся?"), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

1  [      ][      ][      ][      ][      ][      ][      ][WQ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][      ][      ][      ][      ]
8  [      ][      ][      ][      ][      ][      ][      ][      ]
      H      G      F      E      D      C      B      A     \s
Шах и мат! Партия окончена. Победили белые.
						""",
						"Чем займёмся?"), 
				gameResponses.getValue());
		Assertions.assertEquals(UserState.UserStatus.MAINMENU, states.getUserStatus(userId));
	}
	
	/**
	 * Проверить ввод матового хода в многопользовательской игре
	 */
	@Test
	public void moveMateMultiGameTest() {
		states.resetAll();
		ImmutablePair<Long, Long> ids = states.createStandardMultiGameWithBoard("game", CHECKMATE_BOARD);
		long userId1 = ids.getKey();
		long userId2 = ids.getValue();
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId1, "q", "a2", "a1");
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
1  [WQ][      ][      ][      ][      ][      ][      ][      ]
      A      B      C      D      E      F      G      H     \s
Шах и мат! Партия окончена. Победили белые.
						""",
						"Чем займёмся?"), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

1  [      ][      ][      ][      ][      ][      ][      ][WQ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][      ][      ][      ][      ]
8  [      ][      ][      ][      ][      ][      ][      ][      ]
      H      G      F      E      D      C      B      A     \s
Шах и мат! Партия окончена. Победили белые.
						""",
						"Чем займёмся?"), 
				gameResponses.getValue());
		Assertions.assertEquals(UserState.UserStatus.MAINMENU, states.getUserStatus(userId1));
		Assertions.assertEquals(UserState.UserStatus.MAINMENU, states.getUserStatus(userId2));
	}
}
