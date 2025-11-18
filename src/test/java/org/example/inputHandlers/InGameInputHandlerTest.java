package org.example.inputHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.GameTranslator;
import org.example.MoveHandler;
import org.example.auxiliary.MoveResults;
import org.example.chess.GameHandler;
import org.example.states.GameState;
import org.example.states.MoveState;

import java.util.List;

/**
 * Проверка обработчика ввода в игре
 */
public class InGameInputHandlerTest {
	/**
	 * Обработчик ввода в игре
	 */
	private final InGameInputHandler inGameInputHandler = 
			new InGameInputHandler();
	
	/**
	 * Обработчик хода и частей хода
	 */
	private final MoveHandler moveHandler = new MoveHandler();
	
	/**
	 * Игровой переводчик
	 */
	private final GameTranslator gameTranslator = new GameTranslator();
	
	/**
	 * Доска, на которой идёт проверка
	 */
	private final static byte[][] BOARD = {
			{0, 0, 0, 0, -6, 0, 0, 0},
			{0, 0, 0, 0, -1, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, -5, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{6, 0, 0, 0, 0, 0, 0, 0}
	};
	
	/**
	 * Пригласительное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Сообщение о ходе оппонента
	 */
	private final static String NOT_YOUR_MOVE = "Сейчас ходит противник.";
	
	/**
	 * Проверить вывод начальной доски
	 */
	@Test
	public void startingBoardTest() {
		GameState gameStateExpected = new GameState();
		GameState gameStateReal = new GameState();
		List<String> textsExpectedFirst = List.of(
				gameTranslator.chessboardString(
						GameHandler.MoveProperty.REGULAR, 
						gameStateExpected.getBoard(), 
						true), 
				YOUR_MOVE);
		List<String> textsExpectedSecond = List.of(
				gameTranslator.chessboardString(
						GameHandler.MoveProperty.REGULAR, 
						gameStateExpected.getBoard(), 
						false), 
				NOT_YOUR_MOVE);
		List<List<String>> textsReal= inGameInputHandler
				.getStartingBoard(gameStateReal, true);
		Assertions.assertIterableEquals(textsExpectedFirst, textsReal.getFirst());
		Assertions.assertIterableEquals(textsExpectedSecond, textsReal.getLast());
		Assertions.assertEquals(2, textsReal.size());
		textsExpectedFirst = List.of(
				gameTranslator.chessboardString(
						GameHandler.MoveProperty.REGULAR, 
						gameStateExpected.getBoard(), 
						false), 
				NOT_YOUR_MOVE);
		textsExpectedSecond = List.of(
				gameTranslator.chessboardString(
						GameHandler.MoveProperty.REGULAR, 
						gameStateExpected.getBoard(), 
						true), 
				YOUR_MOVE);
		textsReal= inGameInputHandler
				.getStartingBoard(gameStateReal, false);
		Assertions.assertIterableEquals(textsExpectedFirst, textsReal.getFirst());
		Assertions.assertIterableEquals(textsExpectedSecond, textsReal.getLast());
		Assertions.assertEquals(2, textsReal.size());
	}
	
	/**
	 * Проверить совершение хода в одиночной игре
	 */
	@Test
	public void moveSingleGameTest() {
		GameState gameStateExpected = new GameState(BOARD, 8, true);
		GameState gameStateReal = new GameState(BOARD, 8, true);
		MoveState moveStateReal = new MoveState();
		GameHandler.MoveProperty moveExpected = 
				moveHandler.processMove("", "e2", "e4", gameStateExpected);
		List<String> textsExpected = List.of(
				gameTranslator.chessboardString(
						moveExpected, 
						gameStateExpected.getBoard(), 
						gameStateExpected.isWhiteToMove()), 
				YOUR_MOVE);
		MoveResults results = inGameInputHandler
				.processInputSingleGame("e2e4", moveStateReal, gameStateReal);
		Assertions.assertIterableEquals(
				textsExpected, results.messagesTextsLists().getFirst());
		Assertions.assertEquals(1, results.messagesTextsLists().size());
	}
	
	/**
	 * Проверить обработку части хода в одиночной игре
	 */
	@Test
	public void movePartsSingleGameTest() {
		GameState gameStateExpected = new GameState(BOARD, 8, true);
		GameState gameStateReal = new GameState(BOARD, 8, true);
		MoveState moveStateReal = new MoveState();
		MoveResults results = inGameInputHandler
				.processInputSingleGame("__p__", moveStateReal, gameStateReal);
		Assertions.assertIterableEquals(List.of(YOUR_MOVE + "ПЕШКА"), 
				results.messagesTextsLists().getFirst());
		Assertions.assertEquals(1, results.messagesTextsLists().size());
		results = inGameInputHandler
				.processInputSingleGame("__e2__", moveStateReal, gameStateReal);
		Assertions.assertIterableEquals(List.of(YOUR_MOVE + "ПЕШКА E2"), 
				results.messagesTextsLists().getFirst());
		Assertions.assertEquals(1, results.messagesTextsLists().size());
		results = inGameInputHandler
				.processInputSingleGame("__e4__", moveStateReal, gameStateReal);
		GameHandler.MoveProperty moveExpected = 
				moveHandler.processMove("p", "e2", "e4", gameStateExpected);
		List<String> textsExpected = List.of(
				YOUR_MOVE + "ПЕШКА E2 E4",
				gameTranslator.chessboardString(
						moveExpected, 
						gameStateExpected.getBoard(), 
						gameStateExpected.isWhiteToMove()), 
				YOUR_MOVE);
		Assertions.assertIterableEquals(textsExpected, 
				results.messagesTextsLists().getFirst());
		Assertions.assertEquals(1, results.messagesTextsLists().size());
	}
	
	/**
	 * Проверить неизвестный ввод в одиночной игре
	 */
	@Test
	public void unknownInputSingleGameTest() {
		GameState gameStateReal = new GameState(BOARD, 8, true);
		MoveState moveStateReal = new MoveState();
		MoveResults results = inGameInputHandler
				.processInputSingleGame("something", moveStateReal, gameStateReal);
		Assertions.assertIterableEquals(
				List.of("Неизвестный формат ввода хода", YOUR_MOVE), 
				results.messagesTextsLists().getFirst());
		Assertions.assertEquals(1, results.messagesTextsLists().size());
	}
	
	/**
	 * Проверить совершение хода в многопользовательской игре
	 */
	@Test
	public void moveMultiGameTest() {
		GameState gameStateExpected = new GameState(BOARD, 8, true);
		GameState gameStateReal = new GameState(BOARD, 8, true);
		MoveState moveStateReal = new MoveState();
		GameHandler.MoveProperty moveExpected = 
				moveHandler.processMove("", "e2", "e4", gameStateExpected);
		List<String> textsExpectedFirst = List.of(
				gameTranslator.chessboardString(
						moveExpected, 
						gameStateExpected.getBoard(), 
						true), 
				NOT_YOUR_MOVE);
		List<String> textsExpectedSecond = List.of(
				gameTranslator.chessboardString(
						moveExpected, 
						gameStateExpected.getBoard(), 
						false), 
				YOUR_MOVE);
		MoveResults results = inGameInputHandler
				.processInputMultiGame("e2e4", true, moveStateReal, gameStateReal);
		Assertions.assertIterableEquals(
				textsExpectedFirst, results.messagesTextsLists().getFirst());
		Assertions.assertIterableEquals(
				textsExpectedSecond, results.messagesTextsLists().getLast());
		Assertions.assertEquals(2, results.messagesTextsLists().size());
	}
	
	/**
	 * Проверить обработку части хода в многопользовательской игре
	 */
	@Test
	public void movePartsMultiGameTest() {
		GameState gameStateExpected = new GameState(BOARD, 8, true);
		GameState gameStateReal = new GameState(BOARD, 8, true);
		MoveState moveStateReal = new MoveState();
		MoveResults results = inGameInputHandler
				.processInputMultiGame("__p__", true, moveStateReal, gameStateReal);
		Assertions.assertIterableEquals(List.of(YOUR_MOVE + "ПЕШКА"), 
				results.messagesTextsLists().getFirst());
		Assertions.assertIterableEquals(
				List.of(), results.messagesTextsLists().getLast());
		Assertions.assertEquals(2, results.messagesTextsLists().size());
		results = inGameInputHandler
				.processInputMultiGame("__e2__", true, moveStateReal, gameStateReal);
		Assertions.assertIterableEquals(List.of(YOUR_MOVE + "ПЕШКА E2"), 
				results.messagesTextsLists().getFirst());
		Assertions.assertIterableEquals(
						List.of(), results.messagesTextsLists().getLast());
		Assertions.assertEquals(2, results.messagesTextsLists().size());
		results = inGameInputHandler
				.processInputMultiGame("__e4__", true, moveStateReal, gameStateReal);
		GameHandler.MoveProperty moveExpected = 
				moveHandler.processMove("p", "e2", "e4", gameStateExpected);
		List<String> textsExpectedFirst = List.of(
				YOUR_MOVE + "ПЕШКА E2 E4",
				gameTranslator.chessboardString(
						moveExpected, 
						gameStateExpected.getBoard(), 
						true), 
				NOT_YOUR_MOVE);
		List<String> textsExpectedSecond = List.of(
				gameTranslator.chessboardString(
						moveExpected, 
						gameStateExpected.getBoard(), 
						false), 
				YOUR_MOVE);
		Assertions.assertIterableEquals(textsExpectedFirst, 
				results.messagesTextsLists().getFirst());
		Assertions.assertIterableEquals(textsExpectedSecond, 
				results.messagesTextsLists().getLast());
		Assertions.assertEquals(2, results.messagesTextsLists().size());
	}
	
	/**
	 * Проверить неизвестный ввод в многопользовательской игре
	 */
	@Test
	public void unknownInputMultiGameTest() {
		GameState gameStateReal = new GameState(BOARD, 8, true);
		MoveState moveStateReal = new MoveState();
		MoveResults results = inGameInputHandler
				.processInputMultiGame("something", true, moveStateReal, gameStateReal);
		Assertions.assertIterableEquals(
				List.of("Неизвестный формат ввода хода", YOUR_MOVE), 
				results.messagesTextsLists().getFirst());
		Assertions.assertIterableEquals(
				List.of(), results.messagesTextsLists().getLast());
		Assertions.assertEquals(2, results.messagesTextsLists().size());
	}
	
	/**
	 * Проверить неудавшийся ход
	 */
	@Test
	public void moveFailureTest() {
		GameState gameStateFirst = new GameState(BOARD, 8, true);
		GameState gameStateSecond = new GameState(BOARD, 8, true);
		MoveState moveState = new MoveState();
		MoveResults resultsSingle = inGameInputHandler
				.processInputSingleGame("e2e9", moveState, gameStateFirst);
		MoveResults resultsMulti = inGameInputHandler
				.processInputMultiGame("e2e9", true, moveState, gameStateSecond);
		Assertions.assertEquals(MoveResults.MoveStatus.FAILURE, 
				resultsSingle.thisMoveStatus());
		Assertions.assertEquals(MoveResults.MoveStatus.FAILURE, 
				resultsMulti.thisMoveStatus());
	}
	
	/**
	 * Проверить удавшийся ход
	 */
	@Test
	public void moveSuccessTest() {
		GameState gameStateFirst = new GameState(BOARD, 8, true);
		GameState gameStateSecond = new GameState(BOARD, 8, true);
		MoveState moveState = new MoveState();
		MoveResults resultsSingle = inGameInputHandler
				.processInputSingleGame("e2e4", moveState, gameStateFirst);
		MoveResults resultsMulti = inGameInputHandler
				.processInputMultiGame("e2e4", true, moveState, gameStateSecond);
		Assertions.assertEquals(MoveResults.MoveStatus.SUCCESS, 
				resultsSingle.thisMoveStatus());
		Assertions.assertEquals(MoveResults.MoveStatus.SUCCESS, 
				resultsMulti.thisMoveStatus());
	}
	
	/**
	 * Проверить матовый ход
	 */
	@Test
	public void moveMateTest() {
		GameState gameStateFirst = new GameState(BOARD, 8, true);
		GameState gameStateSecond = new GameState(BOARD, 8, true);
		MoveState moveState = new MoveState();
		MoveResults resultsSingle = inGameInputHandler
				.processInputSingleGame("qc6a8", moveState, gameStateFirst);
		MoveResults resultsMulti = inGameInputHandler
				.processInputMultiGame("qc6a8", true, moveState, gameStateSecond);
		Assertions.assertEquals(MoveResults.MoveStatus.GAMEOVER, 
				resultsSingle.thisMoveStatus());
		Assertions.assertEquals(MoveResults.MoveStatus.GAMEOVER, 
				resultsMulti.thisMoveStatus());
	}
}
