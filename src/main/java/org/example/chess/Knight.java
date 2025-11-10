package org.example.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения коня
 */
public class Knight implements Chessmen {
    /**
     * Первая компонента Г-образного сдвига коня
     */
    private static final int FIRST_PART_OF_SHIFT = 1;
    /**
     * Вторая компонента Г-образного сдвига коня
     */
    private static final int SECOND_PART_OF_SHIFT = 2;
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
    public Knight(int minSideValue, int maxSideValue) {
        this.minSideValue = minSideValue;
        this.maxSideValue = maxSideValue;
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (board[finish.row()][finish.column()] == 0 ||
                (board[finish.row()][finish.column()] < 0 != board[start.row()][start.column()] < 0)) {
            if ((Math.abs(finish.row() - start.row()) == 2 &&
                    Math.abs(finish.column() - start.column()) == 1) ||
                    (Math.abs(finish.row() - start.row()) == 1 &&
                            Math.abs(finish.column() - start.column()) == 2)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<>();
        int startRow = start.row();
        int startColumn = start.column();
        if (startRow + FIRST_PART_OF_SHIFT <= maxSideValue && startColumn + SECOND_PART_OF_SHIFT <= maxSideValue &&
                (board[startRow + FIRST_PART_OF_SHIFT][startColumn + SECOND_PART_OF_SHIFT] == 0 ||
                        board[startRow + FIRST_PART_OF_SHIFT][startColumn + SECOND_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + FIRST_PART_OF_SHIFT,
                    startColumn + SECOND_PART_OF_SHIFT));
        }

        if (startRow + SECOND_PART_OF_SHIFT <= maxSideValue && startColumn + FIRST_PART_OF_SHIFT <= maxSideValue &&
                (board[startRow + SECOND_PART_OF_SHIFT][startColumn + FIRST_PART_OF_SHIFT] == 0 ||
                        board[startRow + SECOND_PART_OF_SHIFT][startColumn + FIRST_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + SECOND_PART_OF_SHIFT,
                    startColumn + FIRST_PART_OF_SHIFT));
        }

        if (startRow - FIRST_PART_OF_SHIFT >= minSideValue && startColumn - SECOND_PART_OF_SHIFT >= minSideValue &&
                (board[startRow - FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT] == 0 ||
                        board[startRow - FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - FIRST_PART_OF_SHIFT,
                    startColumn - SECOND_PART_OF_SHIFT));
        }

        if (startRow - SECOND_PART_OF_SHIFT >= minSideValue && startColumn - FIRST_PART_OF_SHIFT >= minSideValue &&
                (board[startRow - SECOND_PART_OF_SHIFT][startColumn - FIRST_PART_OF_SHIFT] == 0 ||
                        board[startRow - SECOND_PART_OF_SHIFT][startColumn - FIRST_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - SECOND_PART_OF_SHIFT,
                    startColumn - FIRST_PART_OF_SHIFT));
        }

        if (startRow - FIRST_PART_OF_SHIFT >= minSideValue && startColumn + SECOND_PART_OF_SHIFT <= maxSideValue &&
                (board[startRow - FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT] == 0 ||
                        board[startRow - FIRST_PART_OF_SHIFT][startColumn + SECOND_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - FIRST_PART_OF_SHIFT,
                    startColumn + SECOND_PART_OF_SHIFT));
        }

        if (startRow - SECOND_PART_OF_SHIFT >= minSideValue && startColumn + FIRST_PART_OF_SHIFT <= maxSideValue &&
                (board[startRow - SECOND_PART_OF_SHIFT][startColumn + FIRST_PART_OF_SHIFT] == 0 ||
                        board[startRow - SECOND_PART_OF_SHIFT][startColumn + FIRST_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - SECOND_PART_OF_SHIFT,
                    startColumn + FIRST_PART_OF_SHIFT));
        }

        if (startRow + FIRST_PART_OF_SHIFT <= maxSideValue && startColumn - SECOND_PART_OF_SHIFT >= minSideValue &&
                (board[startRow + FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT] == 0 ||
                        board[startRow + FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + FIRST_PART_OF_SHIFT,
                    startColumn - SECOND_PART_OF_SHIFT));
        }

        if (startRow + SECOND_PART_OF_SHIFT <= maxSideValue && startColumn - FIRST_PART_OF_SHIFT >= minSideValue &&
                (board[startRow + SECOND_PART_OF_SHIFT][startColumn - FIRST_PART_OF_SHIFT] == 0 ||
                        board[startRow + SECOND_PART_OF_SHIFT][startColumn - FIRST_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + SECOND_PART_OF_SHIFT,
                    startColumn - FIRST_PART_OF_SHIFT));
        }
        return possibleMoves;
    }
}