package org.example.inputHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.GameTranslator;
import org.example.MoveHandler;
import org.example.chess.GameHandler;
import org.example.states.UserState;

import java.util.ArrayList;
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
	 * Пригласительное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Проверить совершение хода
	 */
	@Test
	public void moveTest() {
		UserState userStateReal = new UserState(UserState.messengerType.FAKE);
		UserState userStateExpected = new UserState(UserState.messengerType.FAKE);
		GameHandler.moveProperty moveExpected = 
				moveHandler.processMove("", "e2", "e4", userStateExpected.getGameState());
		List<String> textsExpected = List.of(
				gameTranslator.chessboardString(
						moveExpected, 
						userStateExpected.getGameState().getBoard(), 
						userStateExpected.getGameState().isWhiteToMove()), 
				YOUR_MOVE);
		List<String> textsReal = inGameInputHandler.processInput("e2e4", userStateReal);
		Assertions.assertIterableEquals(textsExpected, textsReal);
	}
	
	/**
	 * Проверить обработку части хода
	 */
	@Test
	public void movePartsTest() {
		UserState userStateReal = new UserState(UserState.messengerType.FAKE);
		UserState userStateExpected = new UserState(UserState.messengerType.FAKE);
		List<String> real = inGameInputHandler.processInput("__p__", userStateReal);
		Assertions.assertEquals(List.of(YOUR_MOVE + "ПЕШКА"), real);
		real = inGameInputHandler.processInput("__e2__", userStateReal);
		Assertions.assertEquals(List.of(YOUR_MOVE + "ПЕШКА E2"), real);
		real = inGameInputHandler.processInput("__e4__", userStateReal);
		List<String> expected = new ArrayList<String>();
		expected.add(YOUR_MOVE + "ПЕШКА E2 E4");
		expected.addAll(inGameInputHandler.processInput("pe2e4", userStateExpected));
		Assertions.assertEquals(expected, real);
	}
	
	/**
	 * Проверить неизвестный ввод
	 */
	@Test
	public void unknownInputTest() {
		UserState userState = new UserState(UserState.messengerType.FAKE);
		List<String> real = inGameInputHandler.processInput("something", userState);
		Assertions.assertIterableEquals(List.of("Неизвестный формат ввода хода"), real);
	}
}
