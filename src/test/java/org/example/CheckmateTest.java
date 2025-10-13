package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CheckmateTest {
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
        assertEquals(true, game.check(board,true,27,bishop));
        assertEquals(false, game.check(board,true,27,knight));
    }
    @Test
    void CheckmateTest() { 
    	byte[] board = new byte[] {
    		  0, 0, 0, 0, 0, 0, 8, 11,
  			  0, 0, 0, 0, 0, 0, 1, 1,  			  
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 8, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  4, 6, 8, 10, 12, 0, 6, 4};
    	Chessmen knight = new Knight();
    	Chessmen bishop = new Bishop();
    	GameHandler game = new GameHandler();
        assertEquals(false, game.checkmate(board,true));
    }
}
