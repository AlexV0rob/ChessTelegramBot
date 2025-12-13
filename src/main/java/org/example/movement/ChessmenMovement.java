package org.example.movement;

import org.example.chess.PositionOnBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвественный за перемещение шахматных фигур
 */
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
     * Сигнальная позиция
     */
    private final static PositionOnBoard SIGNAL_POSITION = new PositionOnBoard(-1, -1);

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
     * Делает список доступых ходов в заданном направлении
     */
    public List<PositionOnBoard> allVerticalAndHorizontalMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        possibleMoves.addAll(allPossibleMovesAtDirection(start, VERTICAL_SHIFT, NO_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesAtDirection(start, -VERTICAL_SHIFT, NO_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesAtDirection(start, NO_SHIFT, HORIZONTAL_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesAtDirection(start, NO_SHIFT, -HORIZONTAL_SHIFT, board));
        return possibleMoves;
    }

    /**
     * Делает список доступых ходов в заданном направлении
     */
    public List<PositionOnBoard> allDiagonalMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        possibleMoves.addAll(allPossibleMovesAtDirection(start, VERTICAL_SHIFT, HORIZONTAL_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesAtDirection(start, -VERTICAL_SHIFT, -HORIZONTAL_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesAtDirection(start, VERTICAL_SHIFT, -HORIZONTAL_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesAtDirection(start, -VERTICAL_SHIFT, HORIZONTAL_SHIFT, board));
        return possibleMoves;

    }

    /**
     * Делает список доступых ходов в заданном направлении
     */
    private List<PositionOnBoard> allPossibleMovesAtDirection(PositionOnBoard startPosition, int verticalShift,
                                                              int horizontalShift, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        int currentVerticalShift = 1;
        int currentHorizontalShift = 1;
        PositionOnBoard currentPosition = startPosition;
        while (currentPosition.row() != SIGNAL_POSITION.row() &&
                currentPosition.column() != SIGNAL_POSITION.column() &&
                isShiftAvailable(verticalShift * currentVerticalShift,
                        horizontalShift * currentHorizontalShift, startPosition.row(),
                        startPosition.column(), board)) {
            currentPosition = move(startPosition, verticalShift * currentVerticalShift,
                    horizontalShift * currentHorizontalShift, board);
            possibleMoves.add(currentPosition);
            if (isPositionEnemy(startPosition.row(), startPosition.column(),
                    currentPosition.row(), currentPosition.column(), board)) {
                break;
            }
            currentVerticalShift++;
            currentHorizontalShift++;
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

    /**
     * Функция, делающая сдвиг в заданном направлении
     */
    private PositionOnBoard move(PositionOnBoard startPosition,
                                 int verticalShift, int horizontalShift, byte[][] board) {
        if (isShiftAvailable(verticalShift, horizontalShift, startPosition.row(), startPosition.column(), board)) {
            return new PositionOnBoard(startPosition.row() + verticalShift,
                    startPosition.column() + horizontalShift);
        }
        return SIGNAL_POSITION;
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
        return board[finishRow][finishColumn] * board[startRow][startColumn] < 0;
    }

    /**
     * Проверить, что сдвинуться можно
     */
    private boolean isShiftAvailable(int verticalShift, int horizontalShift,
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

}
