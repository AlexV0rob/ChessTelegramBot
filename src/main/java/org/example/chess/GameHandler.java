package org.example.chess;

import org.example.states.GameState;

import java.util.List;

/**
 * Класс обработки хода
 */
public class GameHandler {
    /**
     * Статус хода
     */
    public enum MoveProperty {
        /**
         * Неправильный ход
         */
        INVALID,
        /**
         * Невозможный ход
         */
        IMPOSSIBLE,
        /**
         * Обычный правильный ход
         */
        REGULAR,
        /**
         * Ход, ставящий шах
         */
        CHECK,
        /**
         * Ход, ставящий мат
         */
        MATE
    }

    /**
     * Минимальная размерность игрового поля
     */
    private final static int MIN_SIDE_VALUE = 0;
    /**
     * Максимальная размерность игрового поля
     */
    private final static int MAX_SIDE_VALUE = 7;
    /**
     * Количество классов фигур
     */
    private final static int FIGURES_COUNT = 6;
    /**
     * Список классов фигур
     */
    private final List<Chessmen> figures = List.of(
            new Pawn(), new Rook(), new Knight(), new Bishop(), new Queen(), new King()
    );
    /**
     * Обозначение чёрного короля в массиве доски
     */
    private final static int BLACK_KING = 6;
    /**
     * Обозначение белого короля в массиве доски
     */
    private final static int WHITE_KING = -6;

    /**
     * Возвращает статус хода
     */
    public MoveProperty processMove(int figureCode, PositionOnBoard start,
                                    PositionOnBoard finish, GameState currentGameState) {
        if ((start.row() < MIN_SIDE_VALUE || start.row() > MAX_SIDE_VALUE) ||
                (start.column() < MIN_SIDE_VALUE || start.column() > MAX_SIDE_VALUE) ||
                (finish.row() < MIN_SIDE_VALUE || finish.row() > MAX_SIDE_VALUE) ||
                (finish.column() < MIN_SIDE_VALUE || finish.column() > MAX_SIDE_VALUE) ||
                (figureCode < 0 || figureCode >= FIGURES_COUNT) ||
                (currentGameState.getBoard()[start.row()][start.column()] < 0 !=
                        currentGameState.isWhiteToMove())) {
            return MoveProperty.INVALID;
        }
        if (!figures.get(figureCode).checkMove(start, finish, currentGameState.getBoard())) {
            return MoveProperty.IMPOSSIBLE;
        }
        MoveProperty move = MoveProperty.REGULAR;
        if (isCheckMove(figureCode, finish, currentGameState.getBoard())) {
            move = MoveProperty.CHECK;
        }
        if (isMateMove(finish, currentGameState)) {
            move = MoveProperty.MATE;
        }
        currentGameState.moveFigure(start, finish);
        currentGameState.changeSide();
        return move;
    }

    /**
     * Проверка на шах
     */
    private boolean isCheckMove(int figureCode, PositionOnBoard curentPosition, byte[][] board) {
        int kingRow = -1, kingColumn = -1;
        if (board[curentPosition.row()][curentPosition.column()] < 0) {
            for (int i = MIN_SIDE_VALUE; i <= MAX_SIDE_VALUE && kingRow < 0; ++i) {
                for (int j = MIN_SIDE_VALUE; j <= MAX_SIDE_VALUE && kingColumn < 0; ++j) {
                    if (board[i][j] == BLACK_KING) {
                        kingRow = i;
                        kingColumn = j;
                    }
                }
            }
        } else {
            for (int i = MIN_SIDE_VALUE; i <= MAX_SIDE_VALUE && kingRow < 0; ++i) {
                for (int j = MIN_SIDE_VALUE; j <= MAX_SIDE_VALUE && kingColumn < 0; ++j) {
                    if (board[i][j] == WHITE_KING) {
                        kingRow = i;
                        kingColumn = j;
                    }
                }
            }
        }
        if (kingRow < 0 || kingColumn < 0) {
            return false;
        }
        return figures.get(figureCode).checkMove(
                curentPosition, new PositionOnBoard(kingRow, kingColumn), board);
    }

    /**
     * Проверка, что ход на короля
     */
    private boolean isMateMove(PositionOnBoard finish, GameState gameState) {
        return Math.abs(gameState.getBoard()[finish.row()][finish.column()]) == 6;
    }
}
