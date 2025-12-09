package org.example.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Короля
 */
public class King implements Chessmen {
	/**
	 * Без сдвига
	 */
	private static final int NO_SHIFT = 0;
    /**
     * Сдвиг по вертикали
     */
    private static final int VERTICAL_SHIFT = 1;
    /**
     * Сдвиг по горизонтали
     */
    private static final int HORIZONTAL_SHIFT = 1;

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
            if (Math.abs(start.row() - finish.row()) <= 1 &&
                    Math.abs(start.column() - finish.column()) <= 1)
                return true;
        }
        return false;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        if (isShiftAvailable(VERTICAL_SHIFT, NO_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() + VERTICAL_SHIFT, 
        			start.column()
        	));
        }
        if (isShiftAvailable(-VERTICAL_SHIFT, NO_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() - VERTICAL_SHIFT, 
        			start.column()
        	));
        }
        if (isShiftAvailable(NO_SHIFT, HORIZONTAL_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row(), 
        			start.column() + HORIZONTAL_SHIFT
        	));
        }
        if (isShiftAvailable(NO_SHIFT, -HORIZONTAL_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row(), 
        			start.column() - HORIZONTAL_SHIFT
        	));
        }
        if (isShiftAvailable(VERTICAL_SHIFT, HORIZONTAL_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() + VERTICAL_SHIFT, 
        			start.column() + HORIZONTAL_SHIFT
        	));
        }
        if (isShiftAvailable(VERTICAL_SHIFT, -HORIZONTAL_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() + VERTICAL_SHIFT, 
        			start.column() - HORIZONTAL_SHIFT
        	));
        }
        if (isShiftAvailable(-VERTICAL_SHIFT, HORIZONTAL_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() - VERTICAL_SHIFT, 
        			start.column() + HORIZONTAL_SHIFT
        	));
        }
        if (isShiftAvailable(-VERTICAL_SHIFT, -HORIZONTAL_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() - VERTICAL_SHIFT, 
        			start.column() - HORIZONTAL_SHIFT
        	));
        }
        return possibleMoves;
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
    	return board[finishRow][finishColumn] < 0 != board[startRow][startColumn] < 0;
    }
    
    /**
     * Проверить, что координата находится в границах
     */
    private boolean isInsideBorders(int pos) {
    	return pos <= MAX_SIDE_VALUE && pos >= MIN_SIDE_VALUE;
    }
    
    /**
     * Проверить, что сдвинуться можно
     */
    private boolean isShiftAvailable(int verticalShift, int horizontalShift, 
    		int row, int column, byte[][] board) {
    	if (isInsideBorders(row + verticalShift) && isInsideBorders(column + verticalShift) &&
                (isPositionEnemy(row, column, row + verticalShift, column + horizontalShift, board))
                || isPositionEmpty(row, column, board)) {
            return true;
        }
    	return false;
    }   
}