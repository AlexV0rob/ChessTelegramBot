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
                             byte[][] board);

	public List<PositionOnBoard> allPossibleMoves(PositionOnBoard positionOnBoard, byte[][] chessboard);
}
