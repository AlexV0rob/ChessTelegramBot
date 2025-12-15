package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.states.LobbyState;
import org.example.states.UserState;
import org.example.statesHandlers.StatesHandler;
import org.example.statesHandlers.MemoryStatesHandler;

/**
 * Проверка обработчика игрового ввода
 */
public class GameInputHandlerTest {
	/**
	 * Хранитель состояний для проверки их изменения
	 */
	private StatesHandler states;
	
	/**
	 * Обработчик ввода в игре
	 */
	private GameInputHandler gameInputHandler;
	
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
	 * Доска для проверки мата
	 */
	private final static byte[][] REGULAR_BOARD = {
			{0, 0, 0, 0, -5, 0, 0, 0},
			{0, 0, 0, 0, -1, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 1, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	/**
	 * Создать однопользовательскую игру и пользователей для неё
	 */
	private long createSingleGame(String name, byte[][] board, int sideLength, boolean isWhiteToMove) {
		long userId = states.addNewUser(UserState.MessengerType.TELEGRAM);
		states.setNewUserStatus(userId, UserState.UserStatus.INGAME);
		states.setUserLobbyName(userId, name);
		states.createNewLobby(name, userId, userId, 
				true, LobbyState.LobbyType.SINGLEPLAYER, 
				board, sideLength, isWhiteToMove);
		return userId;
	}
	
	/**
	 * Создать однопользовательскую игру и пользователей для неё
	 */
	private ImmutablePair<Long, Long> createMultiGame(String name, 
			byte[][] board, int sideLength, boolean isWhiteToMove) {
		long userId1 = states.addNewUser(UserState.MessengerType.TELEGRAM);
		long userId2 = states.addNewUser(UserState.MessengerType.TELEGRAM);
		states.setNewUserStatus(userId1, UserState.UserStatus.INGAME);
		states.setNewUserStatus(userId2, UserState.UserStatus.INGAME);
		states.setUserLobbyName(userId1, name);
		states.setUserLobbyName(userId2, name);
		states.createNewLobby(name, userId1, userId2, 
				true, LobbyState.LobbyType.MULTIPLAYER, 
				board, sideLength, isWhiteToMove);
		return new ImmutablePair<>(userId1, userId2);
	}
	
	/**
	 * Сбросить состояния
	 */
	@BeforeEach
	public void ResetStates() {
		states = new MemoryStatesHandler();
		gameInputHandler = new GameInputHandler(states);
	}
	
	/**
	 * Проверить ввод части хода в одиночной игре
	 */
	@Test
	public void movePartSingleGameTest() {
		long userId = createSingleGame("game", REGULAR_BOARD, 8, true);
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
		ImmutablePair<Long, Long> ids = createMultiGame("game", REGULAR_BOARD, 8, true);
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
		long userId = createSingleGame("game", REGULAR_BOARD, 8, true);
		gameInputHandler.processMovePart(userId, "p");
		gameInputHandler.processMovePart(userId, "e2");
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMovePart(userId, "e4");
		Assertions.assertIterableEquals(
				List.of("Ваш ход: ПЕШКА E2 E4", 
						"""
Ход чёрных

1  [      ][      ][      ][WQ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][WP][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][ BP][      ][      ][      ][      ]
8  [      ][      ][      ][      ][      ][      ][      ][      ]
      H      G      F      E      D      C      B      A     \s
						""", 
						"Ваш ход: "), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][ BP][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][WP][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
1  [      ][      ][      ][      ][WQ][      ][      ][      ]
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
		ImmutablePair<Long, Long> ids = createMultiGame("game", REGULAR_BOARD, 8, true);
		long userId = ids.getKey();
		gameInputHandler.processMovePart(userId, "p");
		gameInputHandler.processMovePart(userId, "e2");
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMovePart(userId, "e4");
		Assertions.assertIterableEquals(
				List.of("Ваш ход: ПЕШКА E2 E4", 
						"""
Ход чёрных

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][ BP][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][WP][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
1  [      ][      ][      ][      ][WQ][      ][      ][      ]
      A      B      C      D      E      F      G      H     \s
						""", 
						"Сейчас ходит противник."), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

1  [      ][      ][      ][WQ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][WP][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][ BP][      ][      ][      ][      ]
8  [      ][      ][      ][      ][      ][      ][      ][      ]
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
		long userId = createSingleGame("game", REGULAR_BOARD, 8, true);
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId, "", "e2", "e4");
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

1  [      ][      ][      ][WQ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][WP][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][ BP][      ][      ][      ][      ]
8  [      ][      ][      ][      ][      ][      ][      ][      ]
      H      G      F      E      D      C      B      A     \s
						""", 
						"Ваш ход: "), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][ BP][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][WP][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
1  [      ][      ][      ][      ][WQ][      ][      ][      ]
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
		ImmutablePair<Long, Long> ids = createMultiGame("game", REGULAR_BOARD, 8, true);
		long userId = ids.getKey();
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId, "", "e2", "e4");
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][ BP][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][WP][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
1  [      ][      ][      ][      ][WQ][      ][      ][      ]
      A      B      C      D      E      F      G      H     \s
						""", 
						"Сейчас ходит противник."), 
				gameResponses.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход чёрных

1  [      ][      ][      ][WQ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][WP][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][ BP][      ][      ][      ][      ]
8  [      ][      ][      ][      ][      ][      ][      ][      ]
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
		long userId = createSingleGame("game", REGULAR_BOARD, 8, true);
		ImmutablePair<List<String>, List<String>> gameResponsesImpossible = 
				gameInputHandler.processMove(userId, "q", "e1", "e4");
		ImmutablePair<List<String>, List<String>> gameResponsesInvalid = 
				gameInputHandler.processMove(userId, "", "e1", "e9");
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][ BP][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][WP][      ][      ][      ]
1  [      ][      ][      ][      ][WQ][      ][      ][      ]
      A      B      C      D      E      F      G      H     \s
Невозможный ход! Попробуйте снова.
						""", 
						"Ваш ход: "), 
				gameResponsesImpossible.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][ BP][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][WP][      ][      ][      ]
1  [      ][      ][      ][      ][WQ][      ][      ][      ]
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
		ImmutablePair<Long, Long> ids = createMultiGame("game", REGULAR_BOARD, 8, true);
		long userId = ids.getKey();
		ImmutablePair<List<String>, List<String>> gameResponsesImpossible = 
				gameInputHandler.processMove(userId, "q", "e1", "e4");
		ImmutablePair<List<String>, List<String>> gameResponsesInvalid = 
				gameInputHandler.processMove(userId, "", "e1", "e9");
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][ BP][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][WP][      ][      ][      ]
1  [      ][      ][      ][      ][WQ][      ][      ][      ]
      A      B      C      D      E      F      G      H     \s
Невозможный ход! Попробуйте снова.
						""", 
						"Ваш ход: "), 
				gameResponsesImpossible.getKey());
		Assertions.assertIterableEquals(
				List.of("""
Ход белых

8  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [      ][      ][      ][      ][ BP][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [      ][      ][      ][      ][WP][      ][      ][      ]
1  [      ][      ][      ][      ][WQ][      ][      ][      ]
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
		long userId = createSingleGame("game", CHECKMATE_BOARD, 8, true);
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
		ImmutablePair<Long, Long> ids = createMultiGame("game", CHECKMATE_BOARD, 8, true);
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
		long userId = createSingleGame("game", CHECKMATE_BOARD, 8, true);
		double userRatingOld = states.getUserRating(userId).getRight();
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId, "q", "a2", "a1");
		double userRatingNew = states.getUserRating(userId).getRight();
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
		Assertions.assertTrue(Math.abs(userRatingOld - userRatingNew) < 1e-9);
	}
	
	/**
	 * Проверить ввод матового хода в многопользовательской игре
	 */
	@Test
	public void moveMateMultiGameTest() {
		ImmutablePair<Long, Long> ids = createMultiGame("game", CHECKMATE_BOARD, 8, true);
		long userId1 = ids.getKey();
		long userId2 = ids.getValue();
		double userRatingOld1 = states.getUserRating(userId1).getRight();
		double userRatingOld2 = states.getUserRating(userId2).getRight();
		ImmutablePair<List<String>, List<String>> gameResponses = 
				gameInputHandler.processMove(userId1, "q", "a2", "a1");
		double userRatingNew1 = states.getUserRating(userId1).getRight();
		double userRatingNew2 = states.getUserRating(userId2).getRight();
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
		Assertions.assertTrue(userRatingNew1 > userRatingOld1);
		Assertions.assertTrue(userRatingNew2 < userRatingOld2);
	}
}
