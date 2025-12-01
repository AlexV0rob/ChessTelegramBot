package org.example.chess;

import java.util.List;

/**
 * Интерфейс определяющий шахматные фигуры
 */
public interface Chessmen {
    /**
     * Проверить ход на правильность
     */
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, 
    		boolean isWhite, byte[][] board);

    /**
     * Создание списка всех доступных ходов для данной фигуры
     */
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board);
}
