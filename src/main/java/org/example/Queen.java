package org.example;

import java.util.List;

/**
 * Класс для реализации логики перемещения Королевы
 */
public class Queen implements Chessmen {
    /**
     * ладья для проверки вертикальных и горизонтальных ходов
     */
    private static Chessmen ROOK;
    /**
     * слон для проверки диагональных ходов
     */
    private static Chessmen BISHOP;

    public Queen(int minSideValue, int maxSideValue) {
        ROOK = new Rook(minSideValue, maxSideValue);
        BISHOP = new Bishop(minSideValue, maxSideValue);
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        boolean result = ROOK.checkMove(start, finish, board)
                || BISHOP.checkMove(start, finish, board);
        return result;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = ROOK.allPossibleMoves(start, board);
        possibleMoves.addAll(BISHOP.allPossibleMoves(start, board));
        return possibleMoves;
    }
}