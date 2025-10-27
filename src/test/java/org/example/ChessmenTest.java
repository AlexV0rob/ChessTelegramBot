 package org.example;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

/**
 * Проверка работы фигур
 */
public class ChessmenTest {

	/**
	 * Проверка ходов пешки
	 */
    @Test
    void PawnTest() {
    	byte[] board = new byte[] {
  		       2,  3,  4,  5,  6,  4,  3,  2,
  		       1,  1,  1,  1,  1,  1,  1,  1,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		      -1, -1, -1, -1, -1, -1, -1, -1,
  		      -2, -3, -4, -5, -6, -4, -3, -2
    		  };
    	Chessmen pawn = new Pawn();
    	Assertions.assertFalse(pawn.checkMove(8,63,board, true));
    	Assertions.assertFalse(pawn.checkMove(8,0,board, true));
    	Assertions.assertTrue(pawn.checkMove(8,16,board,true));
    	Assertions.assertTrue(pawn.checkMove(8,24,board,true));
    }
	/**
	 * Проверка ходов Слона
	 */
    @Test
    void RookTest() {
    	byte[] board = new byte[] {
 		       2,  3,  4,  5,  6,  4,  3,  0,
 		       0,  1,  1,  1,  1,  1,  1,  1,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		       0,  0,  0,  0,  0,  0,  0,  2,
 		       0,  0,  0,  0,  0,  0,  34,  0,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		      -1, -1, -1, -1, -1, -1, -1, -1,
 		      -2, -3, -4, -5, -6, -4, -3, -2
  			  };
    	Chessmen rook = new Rook();
    	Assertions.assertFalse(rook.checkMove(0,1,board,true));
        Assertions.assertFalse(rook.checkMove(28,34,board,true));
    	Assertions.assertTrue(rook.checkMove(0,16,board,true));
        Assertions.assertTrue(rook.checkMove(28,27,board,true));
    }
	/**
	 * Проверка ходов Слона
	 */
    @Test
    void BishopTest() {
    	byte[] board = new byte[] {
 		       2,  3,  4,  5,  6,  4,  3,  2,
 		       1,  0,  1,  0,  1,  1,  1,  1,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		      -1, -1, -1, -1, -1, -1, -1, -1,
 		      -2, -3, -4, -5, -6, -4, -3, -2
  			  };
    	Chessmen bishop = new Bishop();
    	Assertions.assertFalse(bishop.checkMove(2,10,board,true));
    	Assertions.assertFalse(bishop.checkMove(2,1,board,true));
    	Assertions.assertTrue(bishop.checkMove(2,9,board,true));
    	Assertions.assertTrue(bishop.checkMove(2,11,board,true));
    }
	/**
	 * Проверка ходов Короля
	 */
    @Test
    void KingTest() {
    	byte[] board = new byte[] {
 		       2,  3,  4,  5,  6,  4,  3,  2,
 		       1,  1,  1,  1,  0,  0,  1,  1,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		       0,  0,  0,  0,  0,  0,  0,  0,
 		      -1, -1, -1, -1, -1, -1, -1, -1,
 		      -2, -3, -4, -5, -6, -4, -3, -2
  			  };
    	Chessmen king = new King();
    	Assertions.assertFalse(king.checkMove(4,11,board,true));
    	Assertions.assertFalse(king.checkMove(4,15,board,true));
    	Assertions.assertTrue(king.checkMove(4,12,board,true));
    	Assertions.assertTrue(king.checkMove(4,13,board,true));
    }
	/**
	 * Проверка ходов коня
	 */
    @Test
    void KnightTest() {
    	byte[] board = new byte[] {
  		       2,  3,  4,  5,  6,  4,  3,  2,
  		       1,  1,  1,  0,  1,  1,  1,  1,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		      -1, -1, -1, -1, -1, -1, -1, -1,
  		      -2, -3, -4, -5, -6, -4, -3, -2
  		      };
    	Chessmen knight = new Knight();
    	Assertions.assertFalse(knight.checkMove(1,0,board,true));
    	Assertions.assertFalse(knight.checkMove(1,17,board,true));
    	Assertions.assertTrue(knight.checkMove(1,11,board,true));
    	Assertions.assertTrue(knight.checkMove(1,18,board,true));
    }
	/**
	 * Проверка ходов Королевы
	 */
    @Test
    void QueenTest() {
    	byte[] board = new byte[] {
  		       2,  3,  4,  5,  6,  4,  3,  2,
  		       1,  1,  1,  0,  0,  1,  1,  1,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		       0,  0,  0,  0,  0,  0,  0,  0,
  		      -1, -1, -1, -1, -1, -1, -1, -1,
  		      -2, -3, -4, -5, -6, -4, -3, -2
  			  };
    	Chessmen queen = new Queen();
    	Assertions.assertFalse(queen.checkMove(3,9,board,true));
    	Assertions.assertFalse(queen.checkMove(3,2,board,true));
    	Assertions.assertTrue(queen.checkMove(3,12,board,true));
    	Assertions.assertTrue(queen.checkMove(3,11,board,true));
    }  
}
