package org.example.movement;

import org.example.chess.PositionOnBoard;

/**
 * Класс-шаблон для DiagonalMovement и VerticalAndHorizontalMovement
 */
public abstract class BaseMovement implements Movement {
    /**
     * Минимальная размерность игрового поля
     */
    private final static int MIN_SIDE_VALUE = 0;
    /**
     * Максимальная размерность игрового поля
     */
    private final static int MAX_SIDE_VALUE = 7;

    /**
     * Проверить, что координата находится в границах
     */
    private boolean isInsideBorders(int pos) {
        return pos <= MAX_SIDE_VALUE && pos >= MIN_SIDE_VALUE;
    }

    /**
     * Проверить, что в точке назначения пустое поле
     */
    private boolean isPositionEmpty(int row, int column, byte[][] board) {
        return board[row][column] == 0;
    }

    /**
     * Расположение позиций друг относительно друга
     */
    private enum PositionRelatives {
        /**
         * Выше
         */
        GREATER,
        /**
         * Ниже
         */
        LESS,
        /**
         * Эквивалентны
         */
        EQUAL
    }

    /**
     * Проверить, что в точке назначения фигура противника
     */
    protected boolean isPositionEnemy(int startRow, int startColumn,
                                      int finishRow, int finishColumn, byte[][] board) {
        return board[finishRow][finishColumn] * board[startRow][startColumn] < 0;
    }

    /**
     * Проверить, что сдвинуться можно
     */
    protected boolean isShiftAvailable(int verticalShift, int horizontalShift,
                                       int row, int column, byte[][] board) {
        if (isInsideBorders(row + verticalShift) && isInsideBorders(column + horizontalShift)) {
            if (isPositionEnemy(row, column, row + verticalShift,
                    column + horizontalShift, board)
                    || isPositionEmpty(row + verticalShift, column + horizontalShift, board)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Определить отношения по этой координате между стартовой и конечной позициями
     */
    private PositionRelatives relativesBetweenStartAndFinish(
            int startCoordinate, int finishCoordinate) {
        if (startCoordinate < finishCoordinate) {
            return PositionRelatives.GREATER;
        }
        if (startCoordinate > finishCoordinate) {
            return PositionRelatives.LESS;
        }
        return PositionRelatives.EQUAL;
    }

    /**
     * Определить следующую позицию по координате при данном отношении
     */
    private int nextPosition(int currentPos, PositionRelatives relation) {
        if (relation.equals(PositionRelatives.GREATER)) {
            return currentPos + 1;
        } else if (relation.equals(PositionRelatives.LESS)) {
            return currentPos - 1;
        }
        return currentPos;
    }

    /**
     * Проверка отсутствия препятствий на пути из стартовой позиции в конечную
     */
    public boolean isWayFree(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        PositionRelatives verticalRelatives =
                relativesBetweenStartAndFinish(start.row(), finish.row());
        PositionRelatives horizontalRelatives =
                relativesBetweenStartAndFinish(start.column(), finish.column());
        int currentRow = nextPosition(start.row(), verticalRelatives);
        int currentColumn = nextPosition(start.column(), horizontalRelatives);
        while (isInsideBorders(currentRow) && isInsideBorders(currentColumn) &&
                isPositionEmpty(currentRow, currentColumn, board) &&
                (currentRow != finish.row() || currentColumn != finish.column())) {
            currentRow = nextPosition(currentRow, verticalRelatives);
            currentColumn = nextPosition(currentColumn, horizontalRelatives);
        }
        return (currentRow == finish.row() && currentColumn == finish.column());
    }
}
