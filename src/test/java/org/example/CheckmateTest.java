package org.example;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

/**
 * Проверка работы шаха и мата
 */
public class CheckmateTest {	
	/**
	 * Проверка шаховой ситуации
	 */
	@Test
	void CheckTest() { 
		byte[] board = new byte[] { 
				0, 0, 0, -6, 0, 0, 0, 0,
				0, 0, 5, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				2, 3, 4, 0, 6, 0, 3, 2 };
		Chessmen rook = new Rook();
		Chessmen queen = new Queen();
		GameHandler game = new GameHandler();
		Assertions.assertFalse(game.isCheck(56, board, true, rook));
		Assertions.assertTrue(game.isCheck(10, board, true, queen));
		
	}

	/**
	 * Проверка матовой ситуации
	 */
	@Test
	void CheckmateTest() {
		byte[] board = new byte[] {
				0, 0, 0, 0, 0, 0, 5, -6,
				0, 0, 0, 0, 0, 0, -1, -1, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 8, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				2, 3, 4, 0, 6,0, 3, 2 };
		GameHandler game = new GameHandler();
		Assertions.assertFalse(game.isThisMoveOnKing(board, 13, true));
		Assertions.assertTrue(game.isThisMoveOnKing(board, 7, true));
	}
}
