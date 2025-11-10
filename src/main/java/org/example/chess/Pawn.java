package org.example.chess;

import java.util.List;

/**
 * Класс для реализации логики перемещения пешки
 */
public class Pawn implements Chessmen {
    /**
     * линия, с которой стартуют белые пешки
     */
    private final static int WHITE_PAWN_START_ROW = 1;
    /**
     * линия, с которой стартуют чёрные пешки
     */
    private final static int BLACK_PAWN_START_ROW = 6;

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (board[finish.row()][finish.column()] == 0 && 
        		(finish.row() - start.row() == 
        			1 * (board[start.row()][start.column()] < 0 ? 1 : -1)) &&
        		finish.column() - start.column() == 0) {
            return true;
        }
        if (board[finish.row()][finish.column()] == 0 && 
        		((WHITE_PAWN_START_ROW == start.row() && 
        		finish.row() - start.row() == 2 && 
        		board[start.row()][start.column()] < 0) || 
        		(BLACK_PAWN_START_ROW == start.row() && 
        		finish.row() - start.row() == -2 && 
        		board[start.row()][start.column()] > 0))) {
            return true;
        }    
        if (board[finish.row()][finish.column()] != 0 && 
        		((board[finish.row()][finish.column()] < 0) != 
        			(board[start.row()][start.column()] < 0)) && 
        		(finish.row() - start.row() == 
        			1 * (board[start.row()][start.column()] < 0 ? 1 : -1)) &&
        		Math.abs(finish.column() - start.column()) == 1) {
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