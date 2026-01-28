package org.example.movement;

import org.example.chess.PositionOnBoard;

import java.util.List;

public interface Movement {
    /**
     * Вычисляет список доступных ходов
     */
    List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board);
}
