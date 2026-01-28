package org.example.movement;

import org.example.chess.PositionOnBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Класс для тестирования VerticalAndHorizontalMovement
 */
public class VertAndHorMovementTest {
    /**
     * Экземпляр класса VerticalAndHorizontalMovement
     */
    private final VerticalAndHorizontalMovement vAndHMovement = new VerticalAndHorizontalMovement();

    /**
     * Тестирование правильности вычисления доступных вертикальных и горизонтальных позиций
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
        List<PositionOnBoard> actualPositions = vAndHMovement.allPossibleMoves(
                new PositionOnBoard(0, 0), board);
        Assertions.assertIterableEquals(expectedPositions, actualPositions);
    }
}
