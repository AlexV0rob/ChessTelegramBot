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
        assertEquals(false ,  pawn.CheckMove(8,63,board,true));
        assertEquals(true ,  pawn.CheckMove(8,16,board,true));
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
        assertEquals(false, casltle.CheckMove(0,1,board,true));
        assertEquals(true, casltle.CheckMove(0,16,board,true));
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
        assertEquals(false , bishop.CheckMove(2,9,board,true));
        assertEquals(true , bishop.CheckMove(2,11,board,true));
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
        assertEquals(false , king.CheckMove(4,11,board,true));
        assertEquals(true , king.CheckMove(3,12,board,true));
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
        assertEquals(false , knight.CheckMove(1,11,board,true));
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
        assertEquals(false , queen.CheckMove(3,11,board,true));
        assertEquals(true , queen.CheckMove(3,12,board,true));
    }  
}
