package org.example.movement;

import org.example.chess.PositionOnBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Тестирование методов класса ChessmenMovementTest
 */
public class ChessmenMovementTest {
    private final ChessmenMovement chessmenMovement = new ChessmenMovement();

    /**
     * Тестирование правильности вычисления доступных диагональных позиций
     */
    @Test
    public void allDiagonalMovesTest() {
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
        List<PositionOnBoard> expectedPositions = List.of(
                new PositionOnBoard(1, 3),
                new PositionOnBoard(2, 4),
                new PositionOnBoard(3, 5),
                new PositionOnBoard(4, 6),
                new PositionOnBoard(5, 7),
                new PositionOnBoard(1, 1),
                new PositionOnBoard(2, 0));
        List<PositionOnBoard> actualPositions = chessmenMovement.allDiagonalMoves(
                new PositionOnBoard(0, 2), board);
        Assertions.assertIterableEquals(expectedPositions, actualPositions);
    }

    /**
     * Тестирование правильности вычисления доступных Вертикальных и Горизонтальных позиций
     */
    @Test
    public void allVerticalAndHorizontalMovesTets() {
        byte[][] board = new byte[][]{
                {-2, 0, 0, 0, 0, 0, 0, 0},
                {-0, 0, -1, 0, -1, -1, -1, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 1, 1, 1},
                {2, 3, 4, 5, 6, 4, 3, 2}
        };
        List<PositionOnBoard> expectedPositions = List.of(
                new PositionOnBoard(1, 0),
                new PositionOnBoard(2, 0),
                new PositionOnBoard(3, 0),
                new PositionOnBoard(4, 0),
                new PositionOnBoard(5, 0),
                new PositionOnBoard(6, 0),
                new PositionOnBoard(0, 1),
                new PositionOnBoard(0, 2),
                new PositionOnBoard(0, 3),
                new PositionOnBoard(0, 4),
                new PositionOnBoard(0, 5),
                new PositionOnBoard(0, 6),
                new PositionOnBoard(0, 7));
        List<PositionOnBoard> actualPositions = chessmenMovement.allVerticalAndHorizontalMoves(
                new PositionOnBoard(0, 0), board);
        Assertions.assertIterableEquals(expectedPositions, actualPositions);
    }

    /**
     * Тестирование проверки отсутствия препятствий на пути
     */
    @Test
    public void isWayFreeTets() {
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
        Assertions.assertFalse(chessmenMovement.isWayFree(new PositionOnBoard(0, 0),
                new PositionOnBoard(0, 2), board));

        Assertions.assertTrue(chessmenMovement.isWayFree(new PositionOnBoard(0, 2),
                new PositionOnBoard(1, 1), board));
    }
}
