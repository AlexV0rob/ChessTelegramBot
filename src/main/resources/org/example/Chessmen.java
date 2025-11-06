package org.example;

/**
 * Интерфейс определяющий шахматные фигуры
 */
public interface Chessmen {
    /**
     * Проверить ход на правильность
     */
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish,
                             byte[][] board);
}
