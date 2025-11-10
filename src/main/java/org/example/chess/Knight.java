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
    private static int MIN_SIDE_VALUE;
    /**
     * Минимальная размерность игрового поля
     */
    private static int MAX_SIDE_VALUE;

    /**
     * Конструктор
     */
    public Knight(int minSideValue, int maxSideValue) {
        MIN_SIDE_VALUE = minSideValue;
        MAX_SIDE_VALUE = maxSideValue;
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
        if (startRow + FIRST_PART_OF_SHIFT <= MAX_SIDE_VALUE && startColumn + SECOND_PART_OF_SHIFT <= MAX_SIDE_VALUE &&
                (board[startRow + FIRST_PART_OF_SHIFT][startColumn + SECOND_PART_OF_SHIFT] == 0 ||
                        board[startRow + FIRST_PART_OF_SHIFT][startColumn + SECOND_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + FIRST_PART_OF_SHIFT,
                    startColumn + SECOND_PART_OF_SHIFT));
        }

        if (startRow + SECOND_PART_OF_SHIFT <= MAX_SIDE_VALUE && startColumn + FIRST_PART_OF_SHIFT <= MAX_SIDE_VALUE &&
                (board[startRow + SECOND_PART_OF_SHIFT][startColumn + FIRST_PART_OF_SHIFT] == 0 ||
                        board[startRow + SECOND_PART_OF_SHIFT][startColumn + FIRST_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + SECOND_PART_OF_SHIFT,
                    startColumn + FIRST_PART_OF_SHIFT));
        }

        if (startRow - FIRST_PART_OF_SHIFT >= MIN_SIDE_VALUE && startColumn - SECOND_PART_OF_SHIFT >= MIN_SIDE_VALUE &&
                (board[startRow - FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT] == 0 ||
                        board[startRow - FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - FIRST_PART_OF_SHIFT,
                    startColumn - SECOND_PART_OF_SHIFT));
        }

        if (startRow - SECOND_PART_OF_SHIFT >= MIN_SIDE_VALUE && startColumn - FIRST_PART_OF_SHIFT >= MIN_SIDE_VALUE &&
                (board[startRow - SECOND_PART_OF_SHIFT][startColumn - FIRST_PART_OF_SHIFT] == 0 ||
                        board[startRow - SECOND_PART_OF_SHIFT][startColumn - FIRST_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - SECOND_PART_OF_SHIFT,
                    startColumn - FIRST_PART_OF_SHIFT));
        }

        if (startRow - FIRST_PART_OF_SHIFT >= MIN_SIDE_VALUE && startColumn + SECOND_PART_OF_SHIFT <= MAX_SIDE_VALUE &&
                (board[startRow - FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT] == 0 ||
                        board[startRow - FIRST_PART_OF_SHIFT][startColumn + SECOND_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - FIRST_PART_OF_SHIFT,
                    startColumn + SECOND_PART_OF_SHIFT));
        }

        if (startRow - SECOND_PART_OF_SHIFT >= MIN_SIDE_VALUE && startColumn + FIRST_PART_OF_SHIFT <= MAX_SIDE_VALUE &&
                (board[startRow - SECOND_PART_OF_SHIFT][startColumn + FIRST_PART_OF_SHIFT] == 0 ||
                        board[startRow - SECOND_PART_OF_SHIFT][startColumn + FIRST_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - SECOND_PART_OF_SHIFT,
                    startColumn + FIRST_PART_OF_SHIFT));
        }

        if (startRow + FIRST_PART_OF_SHIFT <= MAX_SIDE_VALUE && startColumn - SECOND_PART_OF_SHIFT >= MIN_SIDE_VALUE &&
                (board[startRow + FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT] == 0 ||
                        board[startRow + FIRST_PART_OF_SHIFT][startColumn - SECOND_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + FIRST_PART_OF_SHIFT,
                    startColumn - SECOND_PART_OF_SHIFT));
        }

        if (startRow + SECOND_PART_OF_SHIFT <= MAX_SIDE_VALUE && startColumn - FIRST_PART_OF_SHIFT >= MIN_SIDE_VALUE &&
                (board[startRow + SECOND_PART_OF_SHIFT][startColumn - FIRST_PART_OF_SHIFT] == 0 ||
                        board[startRow + SECOND_PART_OF_SHIFT][startColumn - FIRST_PART_OF_SHIFT]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + SECOND_PART_OF_SHIFT,
                    startColumn - FIRST_PART_OF_SHIFT));
        }
        return possibleMoves;
    }
}