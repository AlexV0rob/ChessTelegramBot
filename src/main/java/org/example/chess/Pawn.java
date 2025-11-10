package org.example.chess;

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
        if (Math.abs(finish.row() - start.row()) == TWIN_MOVE_SHIFT &&
                finish.row() <= MAX_SIDE_VALUE &&
                board[(finish.row() + start.row()) / 2][finish.column()] == 0 &&
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
        int isWhite = board[startRow][startColumn] < 0 ? 1 : -1;
        if (startRow + SINGLE_MOVE_SHIFT * isWhite <= MAX_SIDE_VALUE &&
                startRow + SINGLE_MOVE_SHIFT * isWhite >= MIN_SIDE_VALUE &&
                board[startRow + SINGLE_MOVE_SHIFT * isWhite][startColumn] == 0) {
            possibleMoves.add(new PositionOnBoard(startRow + SINGLE_MOVE_SHIFT * isWhite, startColumn));
        }
        if ((WHITE_PAWN_START_ROW == startRow ||
                BLACK_PAWN_START_ROW == startRow) &&
                startRow + TWIN_MOVE_SHIFT * isWhite <= MAX_SIDE_VALUE &&
                startRow + TWIN_MOVE_SHIFT * isWhite >= MIN_SIDE_VALUE &&
                isWayFree(start, new PositionOnBoard(startRow + TWIN_MOVE_SHIFT * isWhite, startColumn), board) &&
                board[startRow + TWIN_MOVE_SHIFT * isWhite][startColumn] == 0) {
            possibleMoves.add(new PositionOnBoard(startRow + TWIN_MOVE_SHIFT * isWhite, startColumn));
        }

        if (startRow + SINGLE_MOVE_SHIFT * isWhite <= MAX_SIDE_VALUE &&
                startRow + SINGLE_MOVE_SHIFT * isWhite >= MIN_SIDE_VALUE &&
                isWayFree(start, new PositionOnBoard(startRow + TWIN_MOVE_SHIFT * isWhite, startColumn), board) &&
                board[startRow + 1][startColumn] == 0) {
            if (startColumn + HORIZONTAL_MOVE_SHIFT <= MAX_SIDE_VALUE &&
                    board[startRow][startColumn]
                            * board[startRow + SINGLE_MOVE_SHIFT * isWhite][startColumn + HORIZONTAL_MOVE_SHIFT] < 0) {
                possibleMoves.add(new PositionOnBoard(startRow + SINGLE_MOVE_SHIFT * isWhite,
                        startColumn + HORIZONTAL_MOVE_SHIFT));
            }
            if (startColumn - HORIZONTAL_MOVE_SHIFT >= MIN_SIDE_VALUE &&
                    board[startRow][startColumn]
                            * board[startRow + SINGLE_MOVE_SHIFT * isWhite][startColumn - HORIZONTAL_MOVE_SHIFT] < 0) {
                possibleMoves.add(new PositionOnBoard(startRow + SINGLE_MOVE_SHIFT * isWhite,
                        startColumn - HORIZONTAL_MOVE_SHIFT));
            }
        }
        return possibleMoves;
    }
}