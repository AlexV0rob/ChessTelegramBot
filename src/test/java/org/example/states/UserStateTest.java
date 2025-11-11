package org.example.states;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.chess.PositionOnBoard;

import java.util.Arrays;

/**
 * Проверка хранителя пользовательского состояния
 */
public class UserStateTest {
	/**
	 * Храниель пользовательского состояния
	 */
	private final UserState userState = new UserState();
	
	/**
	 * Проверка перезапуска игры
	 */
	@Test
	public void resetGameTest() {
		GameState gameStateOld = userState.getGameState();
		gameStateOld.moveFigure(new PositionOnBoard(1, 4), new PositionOnBoard(3, 4));
		gameStateOld.changeSide();
		userState.resetGameState();
		GameState gameStateNew = userState.getGameState();
		Assertions.assertFalse(gameStateOld == gameStateNew);
		Assertions.assertFalse(gameStateOld.isWhiteToMove() == gameStateNew.isWhiteToMove());
		Assertions.assertFalse(Arrays.equals(gameStateOld.getBoard(), gameStateNew.getBoard()));
	}
}
