package org.example.chess;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Ладьи
 */
public class Rook implements Chessmen {
    private enum positionRelatives {
        GREATER,
        LESS,
        EQUAL
    }

    /**
     * Максимальная размерность игрового поля
     */
    private static int MIN_SIDE_VALUE;
    /**
     * Минимальная размерность игрового поля
     */
    private static int MAX_SIDE_VALUE;

    /**
     * Конструктор класса
     */
    public Rook(int minSideValue, int maxSideValue) {
        MIN_SIDE_VALUE = minSideValue;
        MAX_SIDE_VALUE = maxSideValue;
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (board[finish.row()][finish.column()] == 0 ||
                (board[finish.row()][finish.column()] < 0 != board[start.row()][start.column()] < 0)) {
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
        int shift = 1;
        boolean isUpFree = true;
        boolean isDownFree = true;
        boolean isLeftFree = true;
        boolean isRightFree = true;
        while (shift <= MAX_SIDE_VALUE && (isUpFree || isDownFree || isLeftFree || isRightFree)) {
            if (isUpFree && startRow + shift <= MAX_SIDE_VALUE
                    && board[startRow + shift][startColumn] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow + shift, startColumn));
            } else if (isUpFree && startRow + shift <= MAX_SIDE_VALUE &&
                    board[startRow + shift][startColumn] * board[startRow][startColumn] < 0) {
                isUpFree = false;
                possibleMoves.add(new PositionOnBoard(startRow + shift, startColumn));
            } else {
                isUpFree = false;
            }
            if (isDownFree && startRow - shift >= MIN_SIDE_VALUE && board[startRow - shift][startColumn] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow - shift, startColumn));
            } else if (isDownFree && startRow - shift >= MIN_SIDE_VALUE &&
                    board[startRow - shift][startColumn] * board[startRow][startColumn] < 0) {
                isDownFree = false;
                possibleMoves.add(new PositionOnBoard(startRow - shift, startColumn));
            } else {
                isDownFree = false;
            }
            if (isRightFree && startColumn + shift <= MAX_SIDE_VALUE && board[startRow][startColumn + shift] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow, startColumn + shift));
            } else if (isRightFree && startColumn + shift <= MAX_SIDE_VALUE &&
                    board[startRow][startColumn + shift] * board[startRow][startColumn] < 0) {
                isRightFree = false;
                possibleMoves.add(new PositionOnBoard(startRow, startColumn + shift));
            } else {
                isRightFree = false;
            }
            if (isLeftFree && startColumn - shift >= MIN_SIDE_VALUE && board[startRow][startColumn - shift] == 0) {
                possibleMoves.add(new PositionOnBoard(startRow, startColumn - shift));
            } else if (isLeftFree && startRow - shift >= MIN_SIDE_VALUE &&
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