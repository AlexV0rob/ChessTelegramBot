package org.example.chess;

import java.util.List;

/**
 * Класс для реализации логики перемещения Короля
 */
public class King implements Chessmen {
    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (board[finish.row()][finish.column()] == 0 || 
        		(board[finish.row()][finish.column()] < 0) != 
        		(board[start.row()][finish.column()] < 0)) {
            if (Math.abs(start.row() - finish.row()) <= 1 && 
            		Math.abs(start.column() - finish.column()) <= 1)
                return true;
        }
        return false;
    }

	@Override
	public List<PositionOnBoard> allPossibleMoves(PositionOnBoard positionOnBoard, byte[][] chessboard) {
		// TODO Auto-generated method stub
		return null;
	}
}