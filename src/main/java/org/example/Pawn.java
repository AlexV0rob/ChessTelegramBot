package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения пешки
 */
public class Pawn implements Chessmen {
    /**
     * линия, с которой стартуют белые пешки
     */
    private final static int WHITE_PAWN_START_ROW = 1;
    /**
     * линия, с которой стартуют чёрные пешки
     */
    private final static int BLACK_PAWN_START_ROW = 6;
    /**
     * сдвиг для одинарного хода
     */
    private final static int SINGLE_MOVE_SHIFT = 1;
    /**
     * сдвиг для двойного хода
     */
    private final static int TWIN_MOVE_SHIFT = 2;
    /**
     * сдвиг для рубки пешкой
     */
    private final static int HORIZONTAL_MOVE_SHIFT = 1;

    private static int MIN_SIDE_VALUE;
    private static int MAX_SIDE_VALUE;

    public Pawn(int minSideValue, int maxSideValue) {
        MIN_SIDE_VALUE = minSideValue;
        MAX_SIDE_VALUE = maxSideValue;
    }

    /**
     * Проверка отсутствия препятствий на пути из стартовой позиции в конечную
     */
    private boolean isWayFree(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (start.row() == MAX_SIDE_VALUE) {
            return false;
        }
        if (finish.row() - start.row() == SINGLE_MOVE_SHIFT &&
                board[finish.row()][finish.column()] == 0) {
            return true;
        }
        if (finish.row() - start.row() == TWIN_MOVE_SHIFT &&
                finish.row() <= MAX_SIDE_VALUE &&
                board[finish.row()][finish.column()] == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (board[finish.row()][finish.column()] == 0 &&
                isWayFree(start, finish, board) &&
                finish.row() - start.row() == 1 * (board[start.row()][start.column()] < 0 ? 1 : -1)) {
            return true;
        }
        if (board[finish.row()][finish.column()] == 0 &&
                isWayFree(start, finish, board) &&
                ((WHITE_PAWN_START_ROW == start.row() &&
                        finish.row() - start.row() == 2 &&
                        board[start.row()][start.column()] < 0) ||
                        (BLACK_PAWN_START_ROW == start.row() &&
                                finish.row() - start.row() == -2 &&
                                board[start.row()][start.column()] > 0))) {
            return true;
        }
        if ((board[finish.row()][finish.column()] < 0) != (board[start.row()][start.column()] < 0) &&
                isWayFree(start, finish, board) &&
                finish.row() - start.row() == 1 * (board[start.row()][start.column()] < 0 ? 1 : -1) &&
                Math.abs(finish.column() - start.column()) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        int startRow = start.row();
        int startColumn = start.column();
        if (startRow + SINGLE_MOVE_SHIFT <= MAX_SIDE_VALUE && board[startRow + 1][startColumn] == 0) {
            possibleMoves.add(new PositionOnBoard(startRow + 1, startColumn));
        }
        if (startRow + TWIN_MOVE_SHIFT <= MAX_SIDE_VALUE &&
                isWayFree(start, new PositionOnBoard(startRow + TWIN_MOVE_SHIFT, startColumn), board) &&
                board[startRow + 1][startColumn] == 0) {
            possibleMoves.add(new PositionOnBoard(startRow + 1, startColumn));
        }

        if (startRow + SINGLE_MOVE_SHIFT <= MAX_SIDE_VALUE &&
                isWayFree(start, new PositionOnBoard(startRow + TWIN_MOVE_SHIFT, startColumn), board) &&
                board[startRow + 1][startColumn] == 0) {
            if (startColumn + HORIZONTAL_MOVE_SHIFT <= MAX_SIDE_VALUE &&
                    board[startRow][startColumn]
                            * board[startRow + SINGLE_MOVE_SHIFT][startColumn + HORIZONTAL_MOVE_SHIFT] < 0) {
                possibleMoves.add(new PositionOnBoard(startRow + SINGLE_MOVE_SHIFT,
                        startColumn + HORIZONTAL_MOVE_SHIFT));
            }
            if (startColumn - HORIZONTAL_MOVE_SHIFT >= MIN_SIDE_VALUE &&
                    board[startRow][startColumn]
                            * board[startRow + SINGLE_MOVE_SHIFT][startColumn - HORIZONTAL_MOVE_SHIFT] < 0) {
                possibleMoves.add(new PositionOnBoard(startRow + SINGLE_MOVE_SHIFT,
                        startColumn - HORIZONTAL_MOVE_SHIFT));
            }
        }
        return possibleMoves;
    }
}