package org.example.movement;

import org.example.chess.PositionOnBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BaseMovementTest {
    /**
     * Экземпляр класса DiagonalMovement
     */
    private final DiagonalMovement diagonal = new DiagonalMovement();

    /**
     * Тестирование проверки сдвига на позицию
     */
    @Test
    public void isShiftAvailableTest() {
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
        Assertions.assertFalse(diagonal.isShiftAvailable(1, 0, 0, 0, board));

        Assertions.assertTrue(diagonal.isShiftAvailable(1, 0, 1, 0, board));
    }

    /**
     * Тестирование проверки позиции на наличие шахматной фигуры оппонента
     */
    @Test
    public void isPositionEnemyTest() {
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
        Assertions.assertFalse(diagonal.isPositionEnemy(1, 0, 2, 0, board));
        Assertions.assertTrue(diagonal.isPositionEnemy(1, 0, 7, 0, board));
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
        Assertions.assertFalse(diagonal.isWayFree(new PositionOnBoard(0, 0),
                new PositionOnBoard(0, 2), board));

        Assertions.assertTrue(diagonal.isWayFree(new PositionOnBoard(0, 2),
                new PositionOnBoard(1, 1), board));
    }

}
