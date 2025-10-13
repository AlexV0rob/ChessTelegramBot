package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
        assertEquals(false ,  pawn.checkMove(8,63,board,true));
        assertEquals(true ,  pawn.checkMove(8,16,board,true));
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
        assertEquals(false, casltle.checkMove(0,1,board,false));
        assertEquals(true, casltle.checkMove(0,16,board,false));
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
        assertEquals(false , bishop.checkMove(2,9,board,false));
        assertEquals(true , bishop.checkMove(2,11,board,false));
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
        assertEquals(false , king.checkMove(4,11,board,false));
        assertEquals(true , king.checkMove(3,12,board,false));
    }
	/**
	 * Проверка ходов коня
	 */
    @Test
    void KnightTest() {
    	byte[] board = new byte[] {
    		  3, 5, 7, 9, 11, 7, 5, 3,
  			  1, 1, 0, 1, 0, 1, 1, 1,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  0, 0, 0, 0, 0, 0, 0, 0,
  			  2, 2, 0, 2, 2, 2, 2, 2,
  			  4, 6, 8, 10, 12, 8, 6, 4};
    	Chessmen knight = new Knight();
        assertEquals(false , knight.checkMove(1,17,board,false));
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
        assertEquals(false , queen.checkMove(3,11,board,false));
        assertEquals(true , queen.checkMove(3,12,board,false));
    }  
}
