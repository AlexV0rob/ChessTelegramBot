package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Королевы
 */
public class Queen implements Chessmen {
    /**
     * ладья для проверки вертикальных и горизонтальных ходов
     */
    Chessmen rook = new Rook();
    /**
     * слон для проверки диагональных ходов
     */
    Chessmen bishop = new Bishop();

    @Override
    public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite,
                             PositionConverter positionConverter) {
        boolean result = rook.checkMove(rawStartPos, rawEndPos, chessDesk, isWhite, positionConverter)
                || bishop.checkMove(rawStartPos, rawEndPos, chessDesk, isWhite, positionConverter);
        return result;
    }

    @Override
    public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk,
                                           boolean isWhite, PositionConverter positionConverter) {
        List<Integer> possibleMoves = rook.everyPossibleMove(rawStartPos, chessDesk,
                isWhite, positionConverter);
        possibleMoves.addAll(bishop.everyPossibleMove(rawStartPos, chessDesk,
                isWhite, positionConverter));

        return possibleMoves;
    }
}