package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.chess.PositionOnBoard;

import org.example.states.GameState;
import org.example.states.MoveState;

import java.util.List;

/**
 * Проверка обработчика хода и частей хода
 */
public class MovePartsHandlerTest {
	/**
	 * Обработчик хода и частей хода
	 */
	private final MovePartsHandler movePartsHandler = new MovePartsHandler();
	
	/**
	 * Состояние готовности хода
	 */
	private final MoveState moveState = new MoveState();
	
	/**
	 * Игровой переводчик
	 */
	private final GameTranslator gameTranslator = new GameTranslator();
	
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
	 * Пригласительное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Проверить обработку хода по частям
	 */
	@Test
	public void normalInputPartsTest() {
		GameState gameStateReal = new GameState(START_BOARD, 8, true);
		GameState gameStateExpected = new GameState(START_BOARD, 8, true);
		List<String> figure = movePartsHandler.processMovePart(
				"p", moveState, gameStateReal);
		Assertions.assertIterableEquals(List.of(YOUR_MOVE + "ПЕШКА"), figure);
		List<String> start = movePartsHandler.processMovePart(
				"e2", moveState, gameStateReal);
		Assertions.assertIterableEquals(List.of(YOUR_MOVE + "ПЕШКА E2"), start);
		List<String> real = movePartsHandler.processMovePart(
				"e4", moveState, gameStateReal);
		List<String> expected = List.of(
				YOUR_MOVE + "ПЕШКА E2 E4",
				gameTranslator.makeMove(
						1, new PositionOnBoard(1, 4), new PositionOnBoard(3, 4), 
						gameStateExpected),
				YOUR_MOVE);
		Assertions.assertIterableEquals(expected, real);
	}
	
	/**
	 * Проверить обработку хода по частям с неверными частями
	 */
	@Test
	public void invalidInputPartsTest() {
		GameState gameStateReal = new GameState(START_BOARD, 8, true);
		GameState gameStateExpected = new GameState(START_BOARD, 8, true);
		List<String> figure = movePartsHandler.processMovePart(
				"p", moveState, gameStateReal);
		Assertions.assertIterableEquals(List.of(YOUR_MOVE + "ПЕШКА"), figure);
		List<String> start = movePartsHandler.processMovePart(
				"e2", moveState, gameStateReal);
		Assertions.assertIterableEquals(List.of(YOUR_MOVE + "ПЕШКА E2"), start);
		List<String> real = movePartsHandler.processMovePart(
				"e9", moveState, gameStateReal);
		List<String> expected = List.of(
				YOUR_MOVE + "ПЕШКА E2 E9",
				gameTranslator.makeMove(
						1, new PositionOnBoard(1, 4), new PositionOnBoard(3, -1), 
						gameStateExpected),
				YOUR_MOVE);
		Assertions.assertIterableEquals(expected, real);
	}
	
	/**
	 * Проверить обработку ввода целиком
	 */
	@Test
	public void normalInputTest() {
		GameState gameStateReal = new GameState(START_BOARD, 8, true);
		GameState gameStateExpected = new GameState(START_BOARD, 8, true);
		MoveState moveState = new MoveState();
		List<String> real = movePartsHandler
				.processMove("", "e2", "e4", gameStateReal, moveState);
		List<String> expected = List.of(
				gameTranslator.makeMove(
						1, new PositionOnBoard(1, 4), new PositionOnBoard(3, 4), 
						gameStateExpected), 
				YOUR_MOVE);
		Assertions.assertIterableEquals(expected, real);
	}
	
	/**
	 * Проверить обработку неверного ввода целиком
	 */
	@Test
	public void invalidInputTest() {
		GameState gameStateReal = new GameState(START_BOARD, 8, true);
		GameState gameStateExpected = new GameState(START_BOARD, 8, true);
		MoveState moveState = new MoveState();
		List<String> real = movePartsHandler
				.processMove("", "e2", "e9", gameStateReal, moveState);
		List<String> expected = List.of(
				gameTranslator.makeMove(
						1, new PositionOnBoard(1, 4), new PositionOnBoard(3, -1), 
						gameStateExpected),
				YOUR_MOVE);
		Assertions.assertEquals(expected, real);
	}
}
