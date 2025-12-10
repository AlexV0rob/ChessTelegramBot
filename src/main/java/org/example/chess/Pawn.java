package org.example.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения пешки
 */
public class Pawn implements Chessmen {
    /**
     * Линия, с которой стартуют белые пешки
     */
    private final static int WHITE_PAWN_START_ROW = 1;
    /**
     * Линия, с которой стартуют чёрные пешки
     */
    private final static int BLACK_PAWN_START_ROW = 6;
    /**
     * Сдвиг для одинарного хода
     */
    private final static int SINGLE_MOVE_SHIFT = 1;
    /**
     * Сдвиг для двойного хода
     */
    private final static int TWIN_MOVE_SHIFT = 2;
    /**
     * Сдвиг для рубки пешкой
     */
    private final static int HORIZONTAL_MOVE_SHIFT = 1;

    /**
     * Минимальная размерность игрового поля
     */
    private final static int MIN_SIDE_VALUE = 0;
    /**
     * Максимальная размерность игрового поля
     */
    private final static int MAX_SIDE_VALUE = 7;

    /**
     * Проверка отсутствия препятствий на пути из стартовой позиции в конечную
     */
    private boolean isWayFree(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (Math.abs(finish.row() - start.row()) == TWIN_MOVE_SHIFT && 
                board[(finish.row() + start.row()) / 2][finish.column()] != 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        if (isPositionEmpty(finish.row(), finish.column(), board) 
                && isWayFree(start, finish, board) 
                && isMoveCorrectForSide(start.row(), finish.row(), 1, board[start.row()][start.column()] < 0) 
                && finish.column() == start.column()) {
            return true;
        }
        if (isPositionEmpty(finish.row(), finish.column(), board) && isWayFree(start, finish, board) && 
        		((WHITE_PAWN_START_ROW == start.row() 
        		&& isMoveCorrectForSide(start.row(), finish.row(), 2, true)) || 
        				(BLACK_PAWN_START_ROW == start.row() 
        				&& isMoveCorrectForSide(start.row(), finish.row(), 2, false)))) {
            return true;
        }
        if (!isPositionEmpty(finish.row(), finish.column(), board)
        		&& isPositionEnemy(start.row(), start.column(), finish.row(), finish.column(), board) 
                && isWayFree(start, finish, board) 
                && isMoveCorrectForSide(start.row(), finish.row(), 1, board[start.row()][start.column()] < 0) 
                && Math.abs(finish.column() - start.column()) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        int startRow = start.row();
        int startColumn = start.column();
        int isWhite = board[startRow][startColumn] < 0 ? 1 : -1;
        if (isInsideBorders(startRow + SINGLE_MOVE_SHIFT * isWhite) &&
        		isPositionEmpty(startRow + SINGLE_MOVE_SHIFT * isWhite, startColumn, board)) {
            possibleMoves.add(new PositionOnBoard(startRow + SINGLE_MOVE_SHIFT * isWhite, startColumn));
        }
        if ((WHITE_PAWN_START_ROW == startRow || BLACK_PAWN_START_ROW == startRow) &&
        		isInsideBorders(startRow + TWIN_MOVE_SHIFT * isWhite) &&
                isWayFree(start, new PositionOnBoard(startRow + TWIN_MOVE_SHIFT * isWhite, startColumn), board) &&
                isPositionEmpty(startRow + TWIN_MOVE_SHIFT * isWhite, startColumn, board)) {
            possibleMoves.add(new PositionOnBoard(startRow + TWIN_MOVE_SHIFT * isWhite, startColumn));
        }

        if (isInsideBorders(startRow + SINGLE_MOVE_SHIFT * isWhite) &&
                isWayFree(start, new PositionOnBoard(startRow + TWIN_MOVE_SHIFT * isWhite, startColumn), board) &&
                isPositionEmpty(startRow + 1, startColumn, board)) {
            if (isInsideBorders(startColumn + HORIZONTAL_MOVE_SHIFT) &&
            		isPositionEnemy(
            				startRow, startColumn, 
            				startRow + SINGLE_MOVE_SHIFT * isWhite, startColumn + HORIZONTAL_MOVE_SHIFT, 
            				board)) 
            {
                possibleMoves.add(new PositionOnBoard(startRow + SINGLE_MOVE_SHIFT * isWhite,
                        startColumn + HORIZONTAL_MOVE_SHIFT));
            }
            if (isInsideBorders(startColumn - HORIZONTAL_MOVE_SHIFT) &&
            		isPositionEnemy(
            				startRow, startColumn, 
            				startRow + SINGLE_MOVE_SHIFT * isWhite, startColumn - HORIZONTAL_MOVE_SHIFT, 
            				board)) 
            {
                possibleMoves.add(new PositionOnBoard(startRow + SINGLE_MOVE_SHIFT * isWhite,
                        startColumn - HORIZONTAL_MOVE_SHIFT));
            }
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
     * Проверить, что ход в нужную для цвета пешки сторону
     */
    private boolean isMoveCorrectForSide(int startRow, int finishRow, 
    		int moveLength, boolean isPawnWhite) {
    	return finishRow - startRow == moveLength * (isPawnWhite ? 1 : -1);
    }
}