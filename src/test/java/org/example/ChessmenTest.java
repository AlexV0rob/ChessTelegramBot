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
    	byte[] board = new byte[] {3, 5, 7, 9, 11, 7, 5, 3,
  			  1, 1, 1, 1, 1, 1, 1, 1,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  2, 2, 2, 2, 2, 2, 2, 2,
  			  4, 6, 8, 10, 12, 8, 6, 4};
    	Chessmen pawn = new Pawn();
    	Assertions.assertFalse( pawn.checkMove(8,63,board,true));
    	Assertions.assertTrue(pawn.checkMove(8,16,board,true));
    }
	/**
	 * Проверка ходов Слона
	 */
    @Test
    void CastleTest() {
    	byte[] board = new byte[] {
    		  3, 5, 7, 9, 11, 7, 5, 3,
  			  0, 1, 1, 1, 1, 1, 1, 1,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  2, 2, 2, 2, 2, 2, 2, 2,
  			  4, 6, 8, 10, 12, 8, 6, 4};
    	Chessmen casltle = new Castle();
    	Assertions.assertFalse( casltle.checkMove(0,1,board,false));
        Assertions.assertTrue(casltle.checkMove(0,16,board,false));
    }
	/**
	 * Проверка ходов Слона
	 */
    @Test
    void BishopTest() {
    	byte[] board = new byte[] {
    		  3, 5, 7, 9, 11, 7, 5, 3,
  			  1, 1, 1, 0, 1, 1, 1, 1,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  2, 2, 0, 2, 2, 2, 2, 2,
  			  4, 6, 8, 10, 12, 8, 6, 4};
    	Chessmen bishop = new Bishop();
    	Assertions.assertFalse(bishop.checkMove(2,9,board,false));
    	Assertions.assertTrue(bishop.checkMove(2,11,board,false));
    }
	/**
	 * Проверка ходов Короля
	 */
    @Test
    void KingTest() {
    	byte[] board = new byte[] {
    		  3, 5, 7, 9, 11, 7, 5, 3,
  			  1, 1, 0, 1, 0, 1, 1, 1,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  2, 2, 0, 2, 2, 2, 2, 2,
  			  4, 6, 8, 10, 12, 8, 6, 4};
    	Chessmen king = new King();
    	Assertions.assertFalse(king.checkMove(4,11,board,false));
    	Assertions.assertTrue(king.checkMove(3,12,board,false));
    }
	/**
	 * Проверка ходов коня
	 */
    @Test
    void KnightTest() {
    	byte[] board = new byte[] {
    		  3, 5, 7, 9, 11, 7, 5, 3,
  			  1, 1, 0, 0, 0, 1, 1, 1,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  2, 2, 0, 2, 2, 2, 2, 2,
  			  4, 6, 8, 10, 12, 8, 6, 4};
    	Chessmen knight = new Knight();
    	Assertions.assertFalse(knight.checkMove(1,17,board,false));
    	Assertions.assertTrue(knight.checkMove(1,11,board,false));
    }
	/**
	 * Проверка ходов Королевы
	 */
    @Test
    void QueenTest() {
    	byte[] board = new byte[] {
    		  3, 5, 7, 9, 11, 7, 5, 3,
  			  1, 1, 0, 1, 0, 1, 1, 1,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  2, 2, 0, 2, 2, 2, 2, 2,
  			  4, 6, 8, 10, 12, 8, 6, 4};
    	Chessmen queen = new Queen();
    	Assertions.assertFalse(queen.checkMove(3,11,board,false));
    	Assertions.assertTrue(queen.checkMove(3,12,board,false));
    }  
}
