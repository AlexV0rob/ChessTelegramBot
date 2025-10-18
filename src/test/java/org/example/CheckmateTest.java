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
    void CheckTest() { //27
    	byte[] board = new byte[] {
    		  11, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,  			  
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 8, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  4, 6, 8, 10, 12, 0, 6, 4};
    	Chessmen knight = new Knight();
    	Chessmen bishop = new Bishop();
    	GameHandler game = new GameHandler();
        Assertions.assertTrue(game.check(board,true,27,bishop));
        Assertions.assertFalse(game.check(board,true,27,knight));
    }
    /**
     * Проверка матовой ситуации
     */
    @Test
    void CheckmateTest() { 
    	byte[] board = new byte[] {
    		  0, 0, 0, 0, 0, 0, 8,11,
  			  0, 0, 0, 0, 0, 0, 1, 1,  			  
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 8, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  4, 6, 8, 10, 12, 0, 6, 4};
    	Chessmen knight = new Knight();
    	GameHandler game = new GameHandler();
    	Assertions.assertFalse(game.IsThisMoveOnKing(board,13,true));
    	Assertions.assertTrue(game.IsThisMoveOnKing(board,7,true));
    }
}
