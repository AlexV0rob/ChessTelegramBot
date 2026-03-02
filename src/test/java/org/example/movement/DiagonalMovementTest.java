package org.example.movement;

import org.example.chess.PositionOnBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Класс для тестирования DiagonalMovement
 */
public class DiagonalMovementTest {
    /**
     * Экземпляр класса DiagonalMovement
     */
    private final DiagonalMovement diagonal = new DiagonalMovement();

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
        List<PositionOnBoard> actualPositions = diagonal.allPossibleMoves(
                new PositionOnBoard(0, 2), board);
        Assertions.assertIterableEquals(expectedPositions, actualPositions);
    }
}
