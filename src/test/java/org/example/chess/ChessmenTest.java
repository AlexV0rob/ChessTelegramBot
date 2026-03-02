package org.example.chess;

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
        byte[][] board = new byte[][]{
                {-2, -3, -4, -5, -6, -4, -3, -2},
                {-1, -1, -1, -1, -1, -1, -1, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {2, 3, 4, 5, 6, 4, 3, 2}
        };
        Chessmen pawn = new Pawn();
        PositionOnBoard startPosition = new PositionOnBoard(1, 0);
        PositionOnBoard finishPosition = new PositionOnBoard(1, 0);
        Assertions.assertFalse(pawn.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(0, 0);
        Assertions.assertFalse(pawn.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(2, 0);
        Assertions.assertTrue(pawn.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(3, 0);
        Assertions.assertTrue(pawn.checkMove(startPosition, finishPosition, board));
    }

    /**
     * Проверка ходов Ладьи
     */
    @Test
    void RookTest() {
        byte[][] board = new byte[][]{
                {-2, -3, -4, -5, -6, -4, -3, -2},
                {0, -1, -1, -1, -1, -1, -1, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, -2},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1},
                {2, 3, 4, 5, 6, 4, 3, 0}
        };
        Chessmen rook = new Rook();
        PositionOnBoard startPosition = new PositionOnBoard(0, 0);
        PositionOnBoard finishPosition = new PositionOnBoard(0, 0);
        Assertions.assertFalse(rook.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 1);
        Assertions.assertFalse(rook.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(2, 0);
        Assertions.assertTrue(rook.checkMove(startPosition, finishPosition, board));
        startPosition = new PositionOnBoard(3, 7);
        finishPosition = new PositionOnBoard(3, 0);
        Assertions.assertTrue(rook.checkMove(startPosition, finishPosition, board));
    }

    /**
     * Проверка ходов Слона
     */
    @Test
    void BishopTest() {
        byte[][] board = new byte[][]{
                {-2, -3, -4, -5, -6, -4, -3, -2},
                {-1, 0, -1, 0, -1, -1, -1, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 1, 1, 1},
                {2, 3, 4, 5, 6, 4, 3, 2}

        };
        Chessmen bishop = new Bishop();
        PositionOnBoard startPosition = new PositionOnBoard(0, 2);
        PositionOnBoard finishPosition = new PositionOnBoard(0, 2);
        Assertions.assertFalse(bishop.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 2);
        Assertions.assertFalse(bishop.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 1);
        Assertions.assertTrue(bishop.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 3);
        Assertions.assertTrue(bishop.checkMove(startPosition, finishPosition, board));
    }

    /**
     * Проверка ходов Короля
     */
    @Test
    void KingTest() {
        byte[][] board = new byte[][]{
                {-2, -3, -4, -5, -6, -4, -3, -2},
                {-1, -1, -1, -1, 0, 0, -1, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 0, 0, 1, 1},
                {2, 3, 4, 5, 6, 4, 3, 2}
        };
        Chessmen king = new King();
        PositionOnBoard startPosition = new PositionOnBoard(0, 4);
        PositionOnBoard finishPosition = new PositionOnBoard(0, 4);

        Assertions.assertFalse(king.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 7);
        Assertions.assertFalse(king.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 4);
        Assertions.assertTrue(king.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 5);
        Assertions.assertTrue(king.checkMove(startPosition, finishPosition, board));
    }

    /**
     * Проверка ходов коня
     */
    @Test
    void KnightTest() {
        byte[][] board = new byte[][]{
                {-2, -3, -4, -5, -6, -4, -3, -2},
                {-1, -1, -1, 0, -1, -1, -1, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 1, 1, 1, 1},
                {2, 3, 4, 5, 6, 4, 3, 2},
        };
        Chessmen knight = new Knight();
        PositionOnBoard startPosition = new PositionOnBoard(0, 1);
        PositionOnBoard finishPosition = new PositionOnBoard(0, 1);
        Assertions.assertFalse(knight.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(2, 1);
        Assertions.assertFalse(knight.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 3);
        Assertions.assertTrue(knight.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(2, 2);
        Assertions.assertTrue(knight.checkMove(startPosition, finishPosition, board));
    }

    /**
     * Проверка ходов Королевы
     */
    @Test
    void QueenTest() {
        byte[][] board = new byte[][]{
                {-2, -3, -4, -5, -6, -4, -3, -2},
                {-1, -1, -1, 0, 0, -1, -1, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 1, 1, 1},
                {2, 3, 4, 5, 6, 4, 3, 2},

        };
        Chessmen queen = new Queen();
        PositionOnBoard startPosition = new PositionOnBoard(0, 3);
        PositionOnBoard finishPosition = new PositionOnBoard(0, 3);
        Assertions.assertFalse(queen.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(0, 0);
        Assertions.assertFalse(queen.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 3);
        Assertions.assertTrue(queen.checkMove(startPosition, finishPosition, board));
        finishPosition = new PositionOnBoard(1, 4);
        Assertions.assertTrue(queen.checkMove(startPosition, finishPosition, board));
    }
}