package org.example.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Ладьи
 */
public class Rook implements Chessmen {
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
     * Максимальная размерность игрового поля
     */
    private int minSideValue;
    /**
     * Минимальная размерность игрового поля
     */
    private int maxSideValue;

    /**
     * Конструктор класса
     */
    public Rook(int minSideValue, int maxSideValue) {
        this.minSideValue = minSideValue;
        this.maxSideValue = maxSideValue;
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, 
    		boolean isWhite, byte[][] board) {
        if (board[finish.row()][finish.column()] == 0 ||
                (board[finish.row()][finish.column()] < 0 != isWhite)) {
            if ((Math.abs(start.row() - finish.row()) == 0 ^
                    Math.abs(start.column() - finish.column()) == 0) &&
                    isWayFree(start, finish, board)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверка отсутствия препядствий дляна пути из начала пути в конец
     */
    private boolean isWayFree(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        PositionRelatives verticalRelatives = PositionRelatives.EQUAL;
        PositionRelatives horizontalRelatives = PositionRelatives.EQUAL;
        if (start.row() < finish.row()) {
            verticalRelatives = PositionRelatives.GREATER;
        } else if (start.row() > finish.row()) {
            verticalRelatives = PositionRelatives.LESS;
        }
        if (start.column() < finish.column()) {
            horizontalRelatives = PositionRelatives.GREATER;
        } else if (start.column() > finish.column()) {
            horizontalRelatives = PositionRelatives.LESS;
        }
        int currentRow = start.row();
        int currentColumn = start.column();
        if (verticalRelatives.equals(PositionRelatives.GREATER)) {
            ++currentRow;
        } else if (verticalRelatives.equals(PositionRelatives.LESS)) {
            --currentRow;
        }
        if (horizontalRelatives.equals(PositionRelatives.GREATER)) {
            ++currentColumn;
        } else if (horizontalRelatives.equals(PositionRelatives.LESS)) {
            --currentColumn;
        }
        while ((currentRow >= minSideValue && currentRow <= maxSideValue) &&
                (currentColumn >= minSideValue && currentColumn <= maxSideValue) &&
                board[currentRow][currentColumn] == 0 &&
                (currentRow != finish.row() || currentColumn != finish.column())) {
            if (verticalRelatives.equals(PositionRelatives.GREATER)) {
                ++currentRow;
            } else if (verticalRelatives.equals(PositionRelatives.LESS)) {
                --currentRow;
            }
            if (horizontalRelatives.equals(PositionRelatives.GREATER)) {
                ++currentColumn;
            } else if (horizontalRelatives.equals(PositionRelatives.LESS)) {
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
        int shift = 1;
        boolean isUpFree = true;
        boolean isDownFree = true;
        boolean isLeftFree = true;
        boolean isRightFree = true;
        while (shift <= maxSideValue && (isUpFree || isDownFree || isLeftFree || isRightFree)) {
            if (isUpFree && startRow + shift <= maxSideValue
                    && board[startRow + shift][startColumn] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow + shift, startColumn));
            } else if (isUpFree && startRow + shift <= maxSideValue &&
                    board[startRow + shift][startColumn] * board[startRow][startColumn] < 0) {
                isUpFree = false;
                possibleMoves.add(new PositionOnBoard(startRow + shift, startColumn));
            } else {
                isUpFree = false;
            }
            if (isDownFree && startRow - shift >= minSideValue && board[startRow - shift][startColumn] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow - shift, startColumn));
            } else if (isDownFree && startRow - shift >= minSideValue &&
                    board[startRow - shift][startColumn] * board[startRow][startColumn] < 0) {
                isDownFree = false;
                possibleMoves.add(new PositionOnBoard(startRow - shift, startColumn));
            } else {
                isDownFree = false;
            }
            if (isRightFree && startColumn + shift <= maxSideValue && board[startRow][startColumn + shift] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow, startColumn + shift));
            } else if (isRightFree && startColumn + shift <= maxSideValue &&
                    board[startRow][startColumn + shift] * board[startRow][startColumn] < 0) {
                isRightFree = false;
                possibleMoves.add(new PositionOnBoard(startRow, startColumn + shift));
            } else {
                isRightFree = false;
            }
            if (isLeftFree && startColumn - shift >= minSideValue && board[startRow][startColumn - shift] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow, startColumn - shift));
            } else if (isLeftFree && startRow - shift >= minSideValue &&
                    board[startRow - shift][startColumn] * board[startRow][startColumn] < 0) {
                isLeftFree = false;
                possibleMoves.add(new PositionOnBoard(startRow, startColumn - shift));
            } else {
                isLeftFree = false;
            }
            shift++;
        }
        return possibleMoves;
    }
}