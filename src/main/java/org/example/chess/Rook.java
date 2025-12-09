package org.example.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Ладьи
 */
public class Rook implements Chessmen {
    /**
     * Расположение позиций друг относительно друга
     */
    private enum PositionRelatives {
        /**
         * Выше
         */
        GREATER,
        /**
         * Ниже
         */
        LESS,
        /**
         * Эквивалентны
         */
        EQUAL
    }

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
        PositionRelatives verticalRelatives = 
        		relativesBetweenStartAndFinish(start.row(), finish.row());
        PositionRelatives horizontalRelatives = 
        		relativesBetweenStartAndFinish(start.column(), finish.column());
        int currentRow = nextPosition(start.row(), verticalRelatives);
        int currentColumn = nextPosition(start.column(), horizontalRelatives);
        while (isInsideBorders(currentRow) && isInsideBorders(currentColumn) &&
                isPositionEmpty(currentRow, currentColumn, board) &&
                (currentRow != finish.row() || currentColumn != finish.column())) {
        	currentRow = nextPosition(currentRow, verticalRelatives);
            currentColumn = nextPosition(currentColumn, horizontalRelatives);
        }
        return (currentRow == finish.row() && currentColumn == finish.column());
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        possibleMoves.addAll(directionAllMoves(start.row(), start.column(), 0, 1, board));
        possibleMoves.addAll(directionAllMoves(start.row(), start.column(), 0, -1, board));
        possibleMoves.addAll(directionAllMoves(start.row(), start.column(), 1, 0, board));
        possibleMoves.addAll(directionAllMoves(start.row(), start.column(), -1, 0, board));
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
     * Определить отношения по этой координате между стартовой и конечной позициями
     */
    private PositionRelatives relativesBetweenStartAndFinish(
    		int startCoordinate, int finishCoordinate) {
    	if (startCoordinate < finishCoordinate) {
    		return PositionRelatives.GREATER;
    	}
    	if (startCoordinate > finishCoordinate) {
    		return PositionRelatives.LESS;
    	}
    	return PositionRelatives.EQUAL;
    }
    
    /**
     * Определить следующую позицию по координате при данном отношении
     */
    private int nextPosition(int currentPos, PositionRelatives relation) {
        if (relation.equals(PositionRelatives.GREATER)) {
        	return currentPos + 1;
        } else if (relation.equals(PositionRelatives.LESS)) {
            return currentPos - 1;
        }
        return currentPos;
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
    
    /**
     * Проверить, что дальнейшее движение возможно
     */
    private boolean isFurtherShiftAvailable(int verticalShift, int horizontalShift, 
    		int row, int column, byte[][] board) {
    	if (isInsideBorders(row + verticalShift) && isInsideBorders(column + verticalShift) &&
    			isPositionEmpty(row, column, board)) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * Получить все доступные ходы в данном направлении
     */
    private List<PositionOnBoard> directionAllMoves(int row, int column, 
    		int verticalShift, int horizontalShift, byte[][] board) {
    	List<PositionOnBoard> moves = new ArrayList<PositionOnBoard>();
    	boolean isWayFree = true;
    	while (isInsideBorders(row) && isInsideBorders(column) && isWayFree) {
    		if (isShiftAvailable(verticalShift, horizontalShift, row, column, board)) {
    			moves.add(new PositionOnBoard(row + verticalShift, column + horizontalShift));
    		}
    		isWayFree = isFurtherShiftAvailable(verticalShift, horizontalShift, row, column, board);
			row += verticalShift;
			column += horizontalShift;
    	}
    	return moves;
    }
}