package org.example.chess;

import org.example.movement.ChessmenMovement;

import java.util.List;

/**
 * Класс для реализации логики перемещения слона
 */
public class Bishop implements Chessmen {
    /**
     * Экземпляр класса chessmenMovement
     */
    private final ChessmenMovement chessmenMovement = new ChessmenMovement();

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        /*
         * Проверяем правильность хода в два этапа:
         * 1) Смотрим что интересующая нас етка нас не занята или там находится
         * шахматная фигура оппонента
         * 2) Проверяем что слон может так сходить
         */
        if (board[finish.row()][finish.column()] == 0 ||
                board[finish.row()][finish.column()] * board[start.row()][start.column()] < 0) {
            if (Math.abs(start.row() - finish.row()) == Math.abs(start.column() - finish.column())
                    && chessmenMovement.isWayFree(start, finish, board)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = chessmenMovement.allDiagonalMoves(start, board);
        return possibleMoves;
    }
}
