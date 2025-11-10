package org.example;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения коня
 */
public class Knight implements Chessmen {
    private static final int firstPartOfShift = 1;
    private static final int secondPartOfShift = 2;

    private static int MIN_SIDE_VALUE;
    private static int MAX_SIDE_VALUE;

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
        if (startRow + firstPartOfShift <= MAX_SIDE_VALUE && startColumn + secondPartOfShift <= MAX_SIDE_VALUE &&
                (board[startRow + firstPartOfShift][startColumn + secondPartOfShift] == 0 ||
                        board[startRow + firstPartOfShift][startColumn + secondPartOfShift]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + firstPartOfShift,
                    startColumn + secondPartOfShift));
        }

        if (startRow + secondPartOfShift <= MAX_SIDE_VALUE && startColumn + firstPartOfShift <= MAX_SIDE_VALUE &&
                (board[startRow + secondPartOfShift][startColumn + firstPartOfShift] == 0 ||
                        board[startRow + secondPartOfShift][startColumn + firstPartOfShift]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + secondPartOfShift,
                    startColumn + firstPartOfShift));
        }

        if (startRow - firstPartOfShift <= MIN_SIDE_VALUE && startColumn - secondPartOfShift <= MIN_SIDE_VALUE &&
                (board[startRow - firstPartOfShift][startColumn - secondPartOfShift] == 0 ||
                        board[startRow - firstPartOfShift][startColumn - secondPartOfShift]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - firstPartOfShift,
                    startColumn - secondPartOfShift));
        }

        if (startRow - secondPartOfShift <= MIN_SIDE_VALUE && startColumn - firstPartOfShift <= MIN_SIDE_VALUE &&
                (board[startRow - secondPartOfShift][startColumn - firstPartOfShift] == 0 ||
                        board[startRow - secondPartOfShift][startColumn - firstPartOfShift]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - secondPartOfShift,
                    startColumn - firstPartOfShift));
        }

        if (startRow - firstPartOfShift <= MIN_SIDE_VALUE && startColumn + secondPartOfShift <= MAX_SIDE_VALUE &&
                (board[startRow - firstPartOfShift][startColumn - secondPartOfShift] == 0 ||
                        board[startRow - firstPartOfShift][startColumn + secondPartOfShift]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - firstPartOfShift,
                    startColumn + secondPartOfShift));
        }

        if (startRow - secondPartOfShift <= MIN_SIDE_VALUE && startColumn + firstPartOfShift <= MAX_SIDE_VALUE &&
                (board[startRow - secondPartOfShift][startColumn - firstPartOfShift] == 0 ||
                        board[startRow - secondPartOfShift][startColumn + firstPartOfShift]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow - secondPartOfShift,
                    startColumn + firstPartOfShift));
        }

        if (startRow + firstPartOfShift <= MIN_SIDE_VALUE && startColumn - secondPartOfShift <= MIN_SIDE_VALUE &&
                (board[startRow + firstPartOfShift][startColumn - secondPartOfShift] == 0 ||
                        board[startRow + firstPartOfShift][startColumn - secondPartOfShift]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + firstPartOfShift,
                    startColumn - secondPartOfShift));
        }

        if (startRow + secondPartOfShift <= MAX_SIDE_VALUE && startColumn - firstPartOfShift <= MIN_SIDE_VALUE &&
                (board[startRow + secondPartOfShift][startColumn - firstPartOfShift] == 0 ||
                        board[startRow + secondPartOfShift][startColumn - firstPartOfShift]
                                * board[startRow][startColumn] < 0)) {
            possibleMoves.add(new PositionOnBoard(startRow + secondPartOfShift,
                    startColumn - firstPartOfShift));
        }
        return possibleMoves;
    }
}