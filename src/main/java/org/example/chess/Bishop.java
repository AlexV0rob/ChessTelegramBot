package org.example.chess;

import java.util.List;

/**
 * Класс для реализации логики перемещения слона
 */
public class Bishop implements Chessmen {
	private enum positionRelatives {
		GREATER,
		LESS,
		EQUAL
	}
	
	private static int MIN_SIDE_VALUE;
	private static int MAX_SIDE_VALUE;
	
	public Bishop(int minSideValue, int maxSideValue) {
		MIN_SIDE_VALUE = minSideValue;
		MAX_SIDE_VALUE = maxSideValue;
	}
	
    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        /*
         * Проверяем правильность хода в два этапа:
         * 1) Смотрим что интересующая насклетка нас не занята или там находится
         * шахматная фигура оппонента
         * 2) Проверяем что слон может так сходить
         */
        if (board[finish.row()][finish.column()] == 0 || 
        		(board[finish.row()][finish.column()] < 0 != board[start.row()][start.column()] < 0)) {
            if (Math.abs(start.row() - finish.row()) ==
                    Math.abs(start.column() - finish.column()) &&
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
    	positionRelatives verticalRelatives = positionRelatives.EQUAL;
    	positionRelatives horizontalRelatives = positionRelatives.EQUAL;
    	if (start.row() < finish.row()) {
    		verticalRelatives = positionRelatives.GREATER;
    	} else if (start.row() > finish.row()) {
    		verticalRelatives = positionRelatives.LESS;
    	}
    	if (start.column() < finish.column()) {
    		horizontalRelatives = positionRelatives.GREATER;
    	} else if (start.column() > finish.column()) {
    		horizontalRelatives = positionRelatives.LESS;
    	}
    	int currentRow = start.row();
    	int currentColumn = start.column();
    	if (verticalRelatives.equals(positionRelatives.GREATER)) {
    		++currentRow;
    	} else if (verticalRelatives.equals(positionRelatives.LESS)) {
    		--currentRow;
    	}
    	if (horizontalRelatives.equals(positionRelatives.GREATER)) {
    		++currentColumn;
    	} else if (horizontalRelatives.equals(positionRelatives.LESS)) {
    		--currentColumn;
    	}
        while ((currentRow >= MIN_SIDE_VALUE && currentRow <= MAX_SIDE_VALUE) &&
        		(currentColumn >= MIN_SIDE_VALUE && currentColumn <= MAX_SIDE_VALUE) && 
        		board[currentRow][currentColumn] == 0 &&
                (currentRow != finish.row() || currentColumn != finish.column())) {
        	if (verticalRelatives.equals(positionRelatives.GREATER)) {
        		++currentRow;
        	} else if (verticalRelatives.equals(positionRelatives.LESS)) {
        		--currentRow;
        	}
        	if (horizontalRelatives.equals(positionRelatives.GREATER)) {
        		++currentColumn;
        	} else if (horizontalRelatives.equals(positionRelatives.LESS)) {
        		--currentColumn;
        	}
        }
        return (currentRow == finish.row() && currentColumn == finish.column());
    }

	@Override
	public List<PositionOnBoard> allPossibleMoves(PositionOnBoard positionOnBoard, byte[][] chessboard) {
		// TODO Auto-generated method stub
		return null;
	}
}
