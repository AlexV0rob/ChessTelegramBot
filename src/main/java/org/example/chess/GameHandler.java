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
    public enum moveProperty {
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
    private int minSideValue = 0;
    /**
     * Максимальная размерность игрового поля
     */
    private int maxSideValue = 7;
    /**
     * Количество классов фигур
     */
    private final static int FIGURES_COUNT = 6;
    /**
     * Список классов фигур
     */
    private final Chessmen[] FIGURES = {
            new Pawn(minSideValue, maxSideValue),
            new Rook(minSideValue, maxSideValue),
            new Knight(minSideValue, maxSideValue),
            new Bishop(minSideValue, maxSideValue),
            new Queen(minSideValue, maxSideValue),
            new King(minSideValue, maxSideValue)
    };
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
    public moveProperty processMove(int figureCode, PositionOnBoard start,
                                    PositionOnBoard finish, GameState currentGameState) {
        if ((start.row() < minSideValue || start.row() > maxSideValue) ||
                (start.column() < minSideValue || start.column() > maxSideValue) ||
                (finish.row() < minSideValue || finish.row() > maxSideValue) ||
                (finish.column() < minSideValue || finish.column() > maxSideValue) ||
                (figureCode < 0 || figureCode >= FIGURES_COUNT) ||
                (currentGameState.getBoard()[start.row()][start.column()] < 0 !=
                        currentGameState.isWhiteToMove())) {
            return moveProperty.INVALID;
        }
        if (!FIGURES[figureCode].checkMove(start, finish, currentGameState.getBoard())) {
            return moveProperty.IMPOSSIBLE;
        }
        moveProperty move = moveProperty.REGULAR;
        if (isCheckMove(figureCode, finish, currentGameState.getBoard())) {
            move = moveProperty.CHECK;
        }
        if (isMateMove(finish, currentGameState)) {
            move = moveProperty.MATE;
        }
        currentGameState.moveFigure(start, finish);
        currentGameState.changeSide();
        return move;
    }

    /**
     * Получение всех доступных позиций для фигуры в конкретной клетке
     */
    public List<PositionOnBoard> allPossiblePositionsForFigure(int figureCode, PositionOnBoard start,
                                                               GameState currentGameState) {
        return FIGURES[figureCode].allPossibleMoves(start, currentGameState.getBoard());
    }

    /**
     * проверка на шах
     */
    private boolean isCheckMove(int figureCode, PositionOnBoard curentPosition, byte[][] board) {
        int kingRow = -1, kingColumn = -1;
        if (board[curentPosition.row()][curentPosition.column()] < 0) {
            for (int i = minSideValue; i <= maxSideValue && kingRow < 0; ++i) {
                for (int j = minSideValue; j <= maxSideValue && kingColumn < 0; ++j) {
                    if (board[i][j] == BLACK_KING) {
                        kingRow = i;
                        kingColumn = j;
                    }
                }
            }
        } else {
            for (int i = minSideValue; i <= maxSideValue && kingRow < 0; ++i) {
                for (int j = minSideValue; j <= maxSideValue && kingColumn < 0; ++j) {
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
        return FIGURES[figureCode].checkMove(
                curentPosition, new PositionOnBoard(kingRow, kingColumn), board);
    }

    /**
     * проверка что ход на короля
     */
    private boolean isMateMove(PositionOnBoard finish, GameState gameState) {
        return Math.abs(gameState.getBoard()[finish.row()][finish.column()]) == 6;
    }
}
