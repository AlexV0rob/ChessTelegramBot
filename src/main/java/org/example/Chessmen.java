package org.example;

import java.util.List;

/**
 * Интерфейс определяющий шахматные фигуры
 */
public interface Chessmen {
    /**
     * Проверить ход на правильность
     */
    public boolean checkMove(int rawStartPos, int rawEndPos,
                             byte[] chessDesk, boolean isWhite, PositionConverter positionConverter);

    /**
     * Все возможные ходы данной фигуры с данной позиции
     */
    public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk,
                                           boolean isWhite, PositionConverter positionConverter);
}
