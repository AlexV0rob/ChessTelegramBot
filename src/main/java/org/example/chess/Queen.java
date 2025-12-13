package org.example.chess;


import org.example.movement.ChessmenMovement;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Королевы
 */
public class Queen implements Chessmen {
    /**
     * Экземпляр класса chessmenMovement
     */
    private final ChessmenMovement chessmenMovement = new ChessmenMovement();
    /**
     * Минимальная размерность игрового поля
     */
    private final static int MIN_SIDE_VALUE = 0;
    /**
     * Максимальная размерность игрового поля
     */
    private final static int MAX_SIDE_VALUE = 7;

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {

        if (isPositionEmpty(finish.row(), finish.column(), board) ||
                isPositionEnemy(start.row(), start.column(), finish.row(), finish.column(), board)) {
            if (((Math.abs(start.row() - finish.row()) == 0 ^
                    Math.abs(start.column() - finish.column()) == 0) ||
                    Math.abs(start.row() - finish.row()) == Math.abs(start.column() - finish.column())) &&
                    chessmenMovement.isWayFree(start, finish, board)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверить, что в точке назначения пустое поле
     */
    private boolean isPositionEmpty(int row, int column, byte[][] board) {
        return board[row][column] == 0;
    }

    /**
     * Проверить, что в точке назначения фигура противника
     */
    private boolean isPositionEnemy(int startRow, int startColumn,
                                    int finishRow, int finishColumn, byte[][] board) {
        return board[finishRow][finishColumn] * board[startRow][startColumn] < 0;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        possibleMoves.addAll(chessmenMovement.allDiagonalMoves(start, board));
        possibleMoves.addAll(chessmenMovement.allVerticalAndHorizontalMoves(start, board));
        return possibleMoves;
    }
}