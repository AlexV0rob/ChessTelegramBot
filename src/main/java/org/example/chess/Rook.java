package org.example.chess;

import org.example.movement.ChessmenMovement;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Ладьи
 */
public class Rook implements Chessmen {
    /**
     * Экземпляр класса chessmenMovement
     */
    private final ChessmenMovement chessmenMovement = new ChessmenMovement();

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (board[finish.row()][finish.column()] == 0 ||
                (board[finish.row()][finish.column()] < 0 != board[start.row()][start.column()] < 0)) {
            if ((Math.abs(start.row() - finish.row()) == 0 ^
                    Math.abs(start.column() - finish.column()) == 0) &&
                    chessmenMovement.isWayFree(start, finish, board)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = chessmenMovement.allDiagonalmoves(start, board);
        return possibleMoves;
    }

}