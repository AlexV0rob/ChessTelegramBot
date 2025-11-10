package org.example.inputHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.GameTranslator;

import org.example.chess.PositionOnBoard;

import org.example.states.UserState;

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
	 * Игровой переводчик
	 */
	private final GameTranslator gameTranslator = new GameTranslator();
	
	/**
	 * Пригласительное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Проверить совершение хода
	 */
	@Test
	public void moveTest() {
		UserState userStateReal = new UserState();
		UserState userStateExpected = new UserState();
		List<String> real = inGameInputHandler.processInput("e2e4", userStateReal);
		List<String> expected = List.of(
				gameTranslator.makeMove(
						1, new PositionOnBoard(1, 4), new PositionOnBoard(3, 4), 
						userStateExpected.getGameState()),
				YOUR_MOVE);
		Assertions.assertIterableEquals(expected, real);
	}
	
	/**
	 * Проверить обработку части хода
	 */
	@Test
	public void movePartTest() {
		UserState userState = new UserState();
		List<String> real = inGameInputHandler.processInput("__p__", userState);
		Assertions.assertEquals(List.of(YOUR_MOVE + "ПЕШКА"), real);
	}
	
	/**
	 * Проверить неизвестный ввод
	 */
	@Test
	public void unknownInputTest() {
		UserState userState = new UserState();
		List<String> real = inGameInputHandler.processInput("something", userState);
		Assertions.assertIterableEquals(List.of("Неизвестный формат ввода хода"), real);
	}
}
