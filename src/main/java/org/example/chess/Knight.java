package org.example.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения коня
 */
public class Knight implements Chessmen {
    /**
     * Первая компонента Г-образного сдвига коня
     */
    private static final int FIRST_PART_OF_SHIFT = 1;
    /**
     * Вторая компонента Г-образного сдвига коня
     */
    private static final int SECOND_PART_OF_SHIFT = 2;

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
            if ((Math.abs(finish.row() - start.row()) == 2 &&
                    Math.abs(finish.column() - start.column()) == 1) ||
                    (Math.abs(finish.row() - start.row()) == 1 &&
                            Math.abs(finish.column() - start.column()) == 2)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<>();
        if (isShiftAvailable(FIRST_PART_OF_SHIFT, SECOND_PART_OF_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() + FIRST_PART_OF_SHIFT, 
        			start.column() + SECOND_PART_OF_SHIFT
        	));
        }
        if (isShiftAvailable(FIRST_PART_OF_SHIFT, -SECOND_PART_OF_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() + FIRST_PART_OF_SHIFT, 
        			start.column() - SECOND_PART_OF_SHIFT
        	));
        }
        if (isShiftAvailable(-FIRST_PART_OF_SHIFT, SECOND_PART_OF_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() - FIRST_PART_OF_SHIFT, 
        			start.column() + SECOND_PART_OF_SHIFT
        	));
        }
        if (isShiftAvailable(-FIRST_PART_OF_SHIFT, -SECOND_PART_OF_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() - FIRST_PART_OF_SHIFT, 
        			start.column() - SECOND_PART_OF_SHIFT
        	));
        }
        if (isShiftAvailable(SECOND_PART_OF_SHIFT, FIRST_PART_OF_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() + SECOND_PART_OF_SHIFT, 
        			start.column() + FIRST_PART_OF_SHIFT
        	));
        }
        if (isShiftAvailable(SECOND_PART_OF_SHIFT, -FIRST_PART_OF_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() + SECOND_PART_OF_SHIFT, 
        			start.column() - FIRST_PART_OF_SHIFT
        	));
        }
        if (isShiftAvailable(-SECOND_PART_OF_SHIFT, FIRST_PART_OF_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() - SECOND_PART_OF_SHIFT, 
        			start.column() + FIRST_PART_OF_SHIFT
        	));
        }
        if (isShiftAvailable(-SECOND_PART_OF_SHIFT, -FIRST_PART_OF_SHIFT, 
        		start.row(), start.column(), board)) {
        	possibleMoves.add(new PositionOnBoard(
        			start.row() - SECOND_PART_OF_SHIFT, 
        			start.column() - FIRST_PART_OF_SHIFT
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
    	return board[finishRow][finishColumn] * board[startRow][startColumn] < 0;
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