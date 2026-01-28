package org.example.chess;

import org.example.movement.VerticalAndHorizontalMovement;

import java.util.List;

/**
 * Класс для реализации логики перемещения Ладьи
 */
public class Rook implements Chessmen {
    /**
     * Экземпляр класса VerticalAndHorizontalMovement
     */
    private final VerticalAndHorizontalMovement allVerticalAndHorizontal = new VerticalAndHorizontalMovement();

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (isPositionEmpty(finish.row(), finish.column(), board) ||
                isPositionEnemy(start.row(), start.column(), finish.row(), finish.column(), board)) {
            if ((Math.abs(start.row() - finish.row()) == 0 ^
                    Math.abs(start.column() - finish.column()) == 0) &&
                    allVerticalAndHorizontal.isWayFree(start, finish, board)) {
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
        List<PositionOnBoard> possibleMoves = allVerticalAndHorizontal.allPossibleMoves(start, board);
        return possibleMoves;
    }

}