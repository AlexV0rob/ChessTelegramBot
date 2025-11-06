package org.example;

/**
 * Класс для реализации логики перемещения коня
 */
public class Knight implements Chessmen {
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
}