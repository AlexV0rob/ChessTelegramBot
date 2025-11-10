package org.example;

import java.util.List;

/**
 * Интерфейс определяющий шахматные фигуры
 */
public interface Chessmen {
    /**
     * Проверить ход на правильность
     */
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish,
                             byte[][] board);

    /**
     * Создание списка всех доступных ходов для данной функции
     */
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board);
}
