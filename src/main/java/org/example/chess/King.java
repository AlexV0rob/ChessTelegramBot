package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Короля
 */
public class King implements Chessmen {
    /**
     * Сдвиг по вертикали
     */
    private static final int VERTICAL_SHIFT = 1;
    /**
     * Сдвиг по горизонтали
     */
    private static final int HORIZONTAL_SHIFT = 1;
    private static int MIN_SIDE_VALUE;
    private static int MAX_SIDE_VALUE;

    public King(int minSideValue, int maxSideValue) {
        MIN_SIDE_VALUE = minSideValue;
        MAX_SIDE_VALUE = maxSideValue;
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (board[finish.row()][finish.column()] == 0 ||
                (board[finish.row()][finish.column()] < 0) !=
                        (board[start.row()][finish.column()] < 0)) {
            if (Math.abs(start.row() - finish.row()) <= 1 &&
                    Math.abs(start.column() - finish.column()) <= 1)
                return true;
        }
        return false;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        int startRow = start.row();
        int startColumn = start.column();
        if (startRow + VERTICAL_SHIFT <= MAX_SIDE_VALUE) {
            if (!(board[startRow + VERTICAL_SHIFT][startColumn] * board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow + VERTICAL_SHIFT, startColumn));
            }
            if (startColumn + HORIZONTAL_SHIFT <= MAX_SIDE_VALUE &&
                    !(board[startRow + VERTICAL_SHIFT][startColumn + HORIZONTAL_SHIFT] *
                            board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow + VERTICAL_SHIFT,
                        startColumn + HORIZONTAL_SHIFT));
            }
            if (startColumn - HORIZONTAL_SHIFT >= MIN_SIDE_VALUE &&
                    !(board[startRow + VERTICAL_SHIFT][startColumn - HORIZONTAL_SHIFT]
                            * board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow + VERTICAL_SHIFT,
                        startColumn - HORIZONTAL_SHIFT));
            }
        }
        if (startRow - VERTICAL_SHIFT >= MIN_SIDE_VALUE) {
            if (!(board[startRow - VERTICAL_SHIFT][startColumn] * board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow - VERTICAL_SHIFT, startColumn));
            }
            if (startColumn + HORIZONTAL_SHIFT <= MAX_SIDE_VALUE &&
                    !(board[startRow - VERTICAL_SHIFT][startColumn + HORIZONTAL_SHIFT] *
                            board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow - VERTICAL_SHIFT,
                        startColumn + HORIZONTAL_SHIFT));
            }
            if (startColumn - HORIZONTAL_SHIFT >= MIN_SIDE_VALUE &&
                    !(board[startRow - VERTICAL_SHIFT][startColumn - HORIZONTAL_SHIFT]
                            * board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow - VERTICAL_SHIFT,
                        startColumn - HORIZONTAL_SHIFT));
            }
        }
        if (startColumn + HORIZONTAL_SHIFT <= MAX_SIDE_VALUE &&
                !(board[startRow][startColumn + HORIZONTAL_SHIFT] *
                        board[startRow][startColumn] > 0)) {
            possibleMoves.add(new PositionOnBoard(startRow,
                    startColumn + HORIZONTAL_SHIFT));
        }
        if (startColumn - HORIZONTAL_SHIFT >= MIN_SIDE_VALUE &&
                !(board[startRow][startColumn - HORIZONTAL_SHIFT]
                        * board[startRow][startColumn] > 0)) {
            possibleMoves.add(new PositionOnBoard(startRow,
                    startColumn - HORIZONTAL_SHIFT));
        }
        return possibleMoves;
    }
}