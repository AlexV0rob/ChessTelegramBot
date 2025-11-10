package org.example.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения слона
 */
public class Bishop implements Chessmen {
    private enum positionRelatives {
        GREATER,
        LESS,
        EQUAL
    }

    private static int MIN_SIDE_VALUE;
    private static int MAX_SIDE_VALUE;

    public Bishop(int minSideValue, int maxSideValue) {
        MIN_SIDE_VALUE = minSideValue;
        MAX_SIDE_VALUE = maxSideValue;
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        /*
         * Проверяем правильность хода в два этапа:
         * 1) Смотрим что интересующая насклетка нас не занята или там находится
         * шахматная фигура оппонента
         * 2) Проверяем что слон может так сходить
         */
        if (board[finish.row()][finish.column()] == 0 ||
                (board[finish.row()][finish.column()] < 0 != board[start.row()][start.column()] < 0)) {
            if (Math.abs(start.row() - finish.row()) ==
                    Math.abs(start.column() - finish.column()) &&
                    isWayFree(start, finish, board)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверка отсутствия препятствий на пути из стартовой позиции в конечную
     */
    private boolean isWayFree(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        positionRelatives verticalRelatives = positionRelatives.EQUAL;
        positionRelatives horizontalRelatives = positionRelatives.EQUAL;
        if (start.row() < finish.row()) {
            verticalRelatives = positionRelatives.GREATER;
        } else if (start.row() > finish.row()) {
            verticalRelatives = positionRelatives.LESS;
        }
        if (start.column() < finish.column()) {
            horizontalRelatives = positionRelatives.GREATER;
        } else if (start.column() > finish.column()) {
            horizontalRelatives = positionRelatives.LESS;
        }
        int currentRow = start.row();
        int currentColumn = start.column();
        if (verticalRelatives.equals(positionRelatives.GREATER)) {
            ++currentRow;
        } else if (verticalRelatives.equals(positionRelatives.LESS)) {
            --currentRow;
        }
        if (horizontalRelatives.equals(positionRelatives.GREATER)) {
            ++currentColumn;
        } else if (horizontalRelatives.equals(positionRelatives.LESS)) {
            --currentColumn;
        }
        while ((currentRow >= MIN_SIDE_VALUE && currentRow <= MAX_SIDE_VALUE) &&
                (currentColumn >= MIN_SIDE_VALUE && currentColumn <= MAX_SIDE_VALUE) &&
                board[currentRow][currentColumn] == 0 &&
                (currentRow != finish.row() || currentColumn != finish.column())) {
            if (verticalRelatives.equals(positionRelatives.GREATER)) {
                ++currentRow;
            } else if (verticalRelatives.equals(positionRelatives.LESS)) {
                --currentRow;
            }
            if (horizontalRelatives.equals(positionRelatives.GREATER)) {
                ++currentColumn;
            } else if (horizontalRelatives.equals(positionRelatives.LESS)) {
                --currentColumn;
            }
        }
        return (currentRow == finish.row() && currentColumn == finish.column());
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        int startRow = start.row();
        int startColumn = start.column();
        int DiagonalShift = 1;
        boolean isUpAndRightFree = true;
        boolean isDownAndRightFree = true;
        boolean isUpAndLeftFree = true;
        boolean isDownAndLeftFree = true;
        while (DiagonalShift <= MAX_SIDE_VALUE && (isUpAndRightFree || isDownAndRightFree || isUpAndLeftFree
                || isDownAndLeftFree)) {
            if (isUpAndRightFree &&
                    startRow + DiagonalShift <= MAX_SIDE_VALUE &&
                    startColumn + DiagonalShift <= MAX_SIDE_VALUE &&
                    board[startRow + DiagonalShift][startColumn + DiagonalShift] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow + DiagonalShift,
                        startColumn + DiagonalShift));
            } else if (isUpAndRightFree && startRow + DiagonalShift <= MAX_SIDE_VALUE &&
                    startRow + DiagonalShift <= MAX_SIDE_VALUE &&
                    startColumn + DiagonalShift <= MAX_SIDE_VALUE &&
                    board[startRow + DiagonalShift][startColumn + DiagonalShift] * board[startRow][startColumn] < 0) {
                possibleMoves.add(new PositionOnBoard(startRow + DiagonalShift,
                        startColumn + DiagonalShift));
                isUpAndRightFree = false;
            } else {
                isUpAndRightFree = false;
            }
            if (isDownAndRightFree &&
                    startRow - DiagonalShift >= MIN_SIDE_VALUE &&
                    startColumn - DiagonalShift >= MIN_SIDE_VALUE &&
                    board[startRow - DiagonalShift][startColumn - DiagonalShift] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow - DiagonalShift,
                        startColumn - DiagonalShift));
            } else if (isDownAndRightFree &&
                    startRow - DiagonalShift >= MIN_SIDE_VALUE &&
                    startColumn - DiagonalShift >= MIN_SIDE_VALUE &&
                    board[startRow - DiagonalShift][startColumn - DiagonalShift] * board[startRow][startColumn] < 0) {
                possibleMoves.add(new PositionOnBoard(startRow - DiagonalShift,
                        startColumn - DiagonalShift));
                isDownAndRightFree = false;
            } else {
                isDownAndRightFree = false;
            }
            if (isUpAndLeftFree &&
                    startRow + DiagonalShift <= MAX_SIDE_VALUE &&
                    startColumn - DiagonalShift >= MIN_SIDE_VALUE &&
                    board[startRow + DiagonalShift][startColumn - DiagonalShift] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow + DiagonalShift,
                        startColumn - DiagonalShift));
            } else if (isUpAndLeftFree &&
                    startRow + DiagonalShift <= MAX_SIDE_VALUE &&
                    startColumn - DiagonalShift >= MIN_SIDE_VALUE &&
                    board[startRow + DiagonalShift][startColumn - DiagonalShift] * board[startRow][startColumn] < 0) {
                possibleMoves.add(new PositionOnBoard(startRow + DiagonalShift,
                        startColumn - DiagonalShift));
                isUpAndLeftFree = false;
            } else {
                isUpAndLeftFree = false;
            }
            if (isDownAndLeftFree &&
                    startRow - DiagonalShift >= MIN_SIDE_VALUE &&
                    startColumn + DiagonalShift <= MAX_SIDE_VALUE &&
                    board[startRow - DiagonalShift][startColumn + DiagonalShift] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow - DiagonalShift,
                        startColumn + DiagonalShift));
            } else if (isDownAndLeftFree &&
                    startRow - DiagonalShift >= MIN_SIDE_VALUE &&
                    startColumn + DiagonalShift <= MAX_SIDE_VALUE &&
                    board[startRow - DiagonalShift][startColumn + DiagonalShift] * board[startRow][startColumn] < 0) {
                possibleMoves.add(new PositionOnBoard(startRow - DiagonalShift,
                        startColumn + DiagonalShift));
                isDownAndLeftFree = false;
            } else {
                isDownAndLeftFree = false;
            }
            DiagonalShift++;
        }
        return possibleMoves;
    }
}
