package org.example.chess;

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
    /**
     * Максимальная размерность игрового поля
     */
    private int minSideValue;
    /**
     * Минимальная размерность игрового поля
     */
    private int maxSideValue;

    /**
     * Конструктор
     */
    public King(int minSideValue, int maxSideValue) {
        this.minSideValue = minSideValue;
        this.maxSideValue = maxSideValue;
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, 
    		boolean isWhite, byte[][] board) {
        if (board[finish.row()][finish.column()] == 0 ||
                (board[finish.row()][finish.column()] < 0) != isWhite) {
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
        if (startRow + VERTICAL_SHIFT <= maxSideValue) {
            if (!(board[startRow + VERTICAL_SHIFT][startColumn] * board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow + VERTICAL_SHIFT, startColumn));
            }
            if (startColumn + HORIZONTAL_SHIFT <= maxSideValue &&
                    !(board[startRow + VERTICAL_SHIFT][startColumn + HORIZONTAL_SHIFT] *
                            board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow + VERTICAL_SHIFT,
                        startColumn + HORIZONTAL_SHIFT));
            }
            if (startColumn - HORIZONTAL_SHIFT >= minSideValue &&
                    !(board[startRow + VERTICAL_SHIFT][startColumn - HORIZONTAL_SHIFT]
                            * board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow + VERTICAL_SHIFT,
                        startColumn - HORIZONTAL_SHIFT));
            }
        }
        if (startRow - VERTICAL_SHIFT >= minSideValue) {
            if (!(board[startRow - VERTICAL_SHIFT][startColumn] * board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow - VERTICAL_SHIFT, startColumn));
            }
            if (startColumn + HORIZONTAL_SHIFT <= maxSideValue &&
                    !(board[startRow - VERTICAL_SHIFT][startColumn + HORIZONTAL_SHIFT] *
                            board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow - VERTICAL_SHIFT,
                        startColumn + HORIZONTAL_SHIFT));
            }
            if (startColumn - HORIZONTAL_SHIFT >= minSideValue &&
                    !(board[startRow - VERTICAL_SHIFT][startColumn - HORIZONTAL_SHIFT]
                            * board[startRow][startColumn] > 0)) {
                possibleMoves.add(new PositionOnBoard(startRow - VERTICAL_SHIFT,
                        startColumn - HORIZONTAL_SHIFT));
            }
        }
        if (startColumn + HORIZONTAL_SHIFT <= maxSideValue &&
                !(board[startRow][startColumn + HORIZONTAL_SHIFT] *
                        board[startRow][startColumn] > 0)) {
            possibleMoves.add(new PositionOnBoard(startRow,
                    startColumn + HORIZONTAL_SHIFT));
        }
        if (startColumn - HORIZONTAL_SHIFT >= minSideValue &&
                !(board[startRow][startColumn - HORIZONTAL_SHIFT]
                        * board[startRow][startColumn] > 0)) {
            possibleMoves.add(new PositionOnBoard(startRow,
                    startColumn - HORIZONTAL_SHIFT));
        }
        return possibleMoves;
    }
}