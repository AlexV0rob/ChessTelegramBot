package org.example.chess;


import java.util.List;

/**
 * Класс для реализации логики перемещения Королевы
 * Так как ходы Королевы являются комбинацией ходов Ладьи и Слона, хранит 
 * внутри себя экземпляры этих фигур и все проверки делает через комбинирование 
 * результатов этих проверок для Ладьи и Слона
 */
public class Queen implements Chessmen {
    /**
     * Ладья для проверки вертикальных и горизонтальных ходов
     */
    private final Chessmen rook = new Rook();
    /**
     * Слон для проверки диагональных ходов
     */
    private final Chessmen bishop = new Bishop();

    @Override
    public boolean checkMove(PositionOnBoard start, PositionOnBoard finish, byte[][] board) {
        boolean result = rook.checkMove(start, finish, board)
                || bishop.checkMove(start, finish, board);
        return result;
    }

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = rook.allPossibleMoves(start, board);
        possibleMoves.addAll(bishop.allPossibleMoves(start, board));
        return possibleMoves;
    }
}