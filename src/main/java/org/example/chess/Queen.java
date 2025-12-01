package org.example.chess;


import java.util.List;

/**
 * Класс для реализации логики перемещения Королевы
 */
public class Queen implements Chessmen {
    /**
     * ладья для проверки вертикальных и горизонтальных ходов
     */
    private final Chessmen rook;
    /**
     * слон для проверки диагональных ходов
     */
    private final Chessmen bishop;

    /**
     * Конструктор класса
     */
    public Queen(int minSideValue, int maxSideValue) {
        rook = new Rook(minSideValue, maxSideValue);
        bishop = new Bishop(minSideValue, maxSideValue);
    }

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish,
    		boolean isWhite, byte[][] board) {
        boolean result = rook.checkMove(start, finish, isWhite, board)
                || bishop.checkMove(start, finish, isWhite, board);
        return result;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = rook.allPossibleMoves(start, board);
        possibleMoves.addAll(bishop.allPossibleMoves(start, board));
        return possibleMoves;
    }
}