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
	void CheckTest() { // 27
		byte[] board = new byte[] { 
				-12, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 8, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				4, 6, 8, 10, 12, 0, 6, 4 };
		Chessmen Rook = new Rook();
		Chessmen bishop = new Bishop();
		GameHandler game = new GameHandler();
		Assertions.assertTrue(game.isCheck(board, true, 27, bishop));
		Assertions.assertFalse(game.isCheck(board, true, 27, Rook));
	}

	/**
	 * Проверка матовой ситуации
	 */
	@Test
	void CheckmateTest() {
		byte[] board = new byte[] {
				0, 0, 0, 0, 0, 0, 8, -12,
				0, 0, 0, 0, 0, 0, -2, -2, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 8, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				4, 6, 8, 10, 12,0, 6, 4 };
		GameHandler game = new GameHandler();
		Assertions.assertFalse(game.isThisMoveOnKing(board, 13, true));
		Assertions.assertTrue(game.isThisMoveOnKing(board, 7, true));
	}
}
