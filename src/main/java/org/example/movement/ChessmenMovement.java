package org.example.movement;

import org.example.chess.PositionOnBoard;
import org.example.chess.Rook;

import java.util.ArrayList;
import java.util.List;

public class ChessmenMovement {
    /**
     * Без сдвига
     */
    private static final int NO_SHIFT = 0;
    /**
     * Сдвиг по вертикали
     */
    private static final int VERTICAL_SHIFT = 1;
    /**
     * Сдвиг по горизонтали
     */
    private static final int HORIZONTAL_SHIFT = 1;
    /**
     * Минимальная размерность игрового поля
     */
    private final static int MIN_SIDE_VALUE = 0;
    /**
     * Максимальная размерность игрового поля
     */
    private final static int MAX_SIDE_VALUE = 7;
    /**
     * Ход на одну клетку
     */
    private final static int SINGULAR_MOVE = 7;

    /**
     * Направление движения
     */
    public enum moveDirection {
        /**
         * Движение вверх по доске
         */
        UP,
        /**
         * Движение вниз по доске
         */
        DOWN,
        /**
         * Движение влево по доске
         */
        LEFT,
        /**
         * Движение вправо по доске
         */
        RIGHT,
        /**
         * Движение вверх по правой диагонали
         */
        UP_AND_RIGHT,
        /**
         * Движение вниз по правой диагонали
         */
        DOWN_AND_RIGHT,
        /**
         * Движение вниз по левой диагонали
         */
        UP_AND_LEFT,
        /**
         * Движение вниз по левой диагонали
         */
        DOWN_AND_LEFT
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
     * Делаем список доступых ходов в заданном направлении
     */
    public List<PositionOnBoard> allPossibleMovesAtChosenDirection(PositionOnBoard startPosition, byte[][] board,
                                                                   moveDirection direction) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        PositionOnBoard currentPosition = startPosition;
        while (isPositionEnemy(startPosition.row(), startPosition.column(), currentPosition.row(),
                currentPosition.column(), board)) {
            currentPosition = move(currentPosition, board, SINGULAR_MOVE, direction);
            if (currentPosition.row() != -1) {
                possibleMoves.add(currentPosition);
            } else {
                break;
            }
        }
        return possibleMoves;
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
    private boolean isWayFree(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
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

    /**
     * Функция делающая сдвиг в заданном направлении
     */
    public PositionOnBoard move(PositionOnBoard startPosition, byte[][] board,
                                int n, moveDirection direction) {
        switch (direction) {
            case moveDirection.UP:
                if (isShiftAvailable(VERTICAL_SHIFT * n, NO_SHIFT, startPosition.row(),
                        startPosition.column(), board)) {
                    return new PositionOnBoard(startPosition.row() + VERTICAL_SHIFT * n, startPosition.column());
                }
                return new PositionOnBoard(-1, 0);
            case moveDirection.DOWN:
                if (isShiftAvailable(-VERTICAL_SHIFT * n, NO_SHIFT, startPosition.row(),
                        startPosition.column(), board)) {
                    return new PositionOnBoard(startPosition.row() - VERTICAL_SHIFT * n, startPosition.column());
                }
                return new PositionOnBoard(-1, 0);
            case moveDirection.RIGHT:
                if (isShiftAvailable(NO_SHIFT, VERTICAL_SHIFT * n, startPosition.row(),
                        startPosition.column(), board)) {
                    return new PositionOnBoard(startPosition.row(),
                            startPosition.column() - HORIZONTAL_SHIFT * n);
                }
                return new PositionOnBoard(-1, 0);
            case moveDirection.LEFT:
                if (isShiftAvailable(NO_SHIFT, -HORIZONTAL_SHIFT * n, startPosition.row(),
                        startPosition.column(), board)) {
                    return new PositionOnBoard(startPosition.row(),
                            startPosition.column() - HORIZONTAL_SHIFT * n);
                }
                return new PositionOnBoard(-1, 0);
            case moveDirection.UP_AND_RIGHT:
                if (isShiftAvailable(VERTICAL_SHIFT * n, HORIZONTAL_SHIFT * n,
                        startPosition.row(), startPosition.column(), board)) {
                    return new PositionOnBoard(startPosition.row() + VERTICAL_SHIFT * n,
                            startPosition.column() + HORIZONTAL_SHIFT * n);
                }
                return new PositionOnBoard(-1, 0);
            case moveDirection.DOWN_AND_RIGHT:
                if (isShiftAvailable(-VERTICAL_SHIFT * n, -VERTICAL_SHIFT * n,
                        startPosition.row(), startPosition.column(), board)) {
                    return new PositionOnBoard(startPosition.row() - VERTICAL_SHIFT * n,
                            startPosition.column() - HORIZONTAL_SHIFT * n);
                }
                return new PositionOnBoard(-1, 0);
            case moveDirection.DOWN_AND_LEFT:
                if (isShiftAvailable(-VERTICAL_SHIFT * n, HORIZONTAL_SHIFT * n,
                        startPosition.row(), startPosition.column(), board)) {
                    return new PositionOnBoard(startPosition.row() - VERTICAL_SHIFT * n,
                            startPosition.column() + HORIZONTAL_SHIFT * n);
                }
                return new PositionOnBoard(-1, 0);
            case moveDirection.UP_AND_LEFT:
                if (isShiftAvailable(VERTICAL_SHIFT * n, -HORIZONTAL_SHIFT * n,
                        startPosition.row(), startPosition.column(), board)) {
                    return new PositionOnBoard(startPosition.row() + VERTICAL_SHIFT * n,
                            startPosition.column() - HORIZONTAL_SHIFT * n);
                }
                return new PositionOnBoard(-1, 0);
        }
        return null;
    }

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
     * Проверить, что в точке назначения фигура противника
     */
    private boolean isPositionEnemy(int startRow, int startColumn,
                                    int finishRow, int finishColumn, byte[][] board) {
        return board[finishRow][finishColumn] < 0 != board[startRow][startColumn] < 0;
    }

    /**
     * Проверить, что сдвинуться можно
     */
    private boolean isShiftAvailable(int verticalShift, int horizontalShift,
                                     int row, int column, byte[][] board) {
        if (isInsideBorders(row + verticalShift) && isInsideBorders(column + verticalShift) &&
                (isPositionEnemy(row, column, row + verticalShift, column + horizontalShift, board))
                || isPositionEmpty(row, column, board)) {
            return true;
        }
        return false;
    }

}
