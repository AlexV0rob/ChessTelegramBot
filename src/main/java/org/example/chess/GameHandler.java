package org.example.chess;

import org.example.states.GameState;

import java.util.List;

/**
 * Класс обработки хода
 */
public class GameHandler {
    public enum moveProperty {
        INVALID,
        IMPOSSIBLE,
        REGULAR,
        CHECK,
        MATE
    }

    /**
     * Максимальная размерность игрового поля
     */
    private static int MIN_SIDE_VALUE;
    /**
     * Минимальная размерность игрового поля
     */
    private static int MAX_SIDE_VALUE;
    /**
     * Количество классов фигур
     */
    private final static int FIGURES_COUNT = 6;
    /**
     * Список классов фигур
     */
    private final Chessmen[] FIGURES = {
            new Pawn(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Rook(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Knight(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Bishop(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Queen(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new King(MIN_SIDE_VALUE, MAX_SIDE_VALUE)
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
        if ((start.row() < MIN_SIDE_VALUE || start.row() > MAX_SIDE_VALUE) ||
                (start.column() < MIN_SIDE_VALUE || start.column() > MAX_SIDE_VALUE) ||
                (finish.row() < MIN_SIDE_VALUE || finish.row() > MAX_SIDE_VALUE) ||
                (finish.column() < MIN_SIDE_VALUE || finish.column() > MAX_SIDE_VALUE) ||
                (figureCode < 0 || figureCode >= FIGURES_COUNT) ||
                (currentGameState.getBoard()[start.row()][start.column()] < 0 !=
                        currentGameState.isWhiteToMove())) {
            return moveProperty.INVALID;
        }
        if (!FIGURES[figureCode].checkMove(start, finish, currentGameState.getBoard())) {
            return moveProperty.IMPOSSIBLE;
        }
        currentGameState.moveFigure(start, finish);
        if (isCheckMove(figureCode, finish, currentGameState.getBoard())) {
            if (isMateMove(finish, currentGameState.getBoard())) {
                return moveProperty.MATE;
            }
            return moveProperty.CHECK;
        }
        return moveProperty.REGULAR;
    }

    public List<PositionOnBoard> allPossiblePositionsForFigure(int figureCode, PositionOnBoard start,
                                                               GameState currentGameState) {
        return FIGURES[figureCode].allPossibleMoves(start, currentGameState.getBoard());
    }

    /**
     * проверка на шах
     */
    private boolean isCheckMove(int figureCode, PositionOnBoard curentPosition, byte[][] board) {
        int i = 0, j = 0;
        if (board[curentPosition.row()][curentPosition.column()] < 0) {
            while (i < 8 && board[i][j] != BLACK_KING) {
                while (j < 8 && board[i][j] != BLACK_KING) {
                    j++;
                }
                j = 0;
                i++;
            }
        } else {
            while (i < 8 && board[i][j] == WHITE_KING) {
                while (j < 8 && board[i][j] == WHITE_KING) {
                    j++;
                }
                j = 0;
                i++;
            }
        }
        if (i == 8 || j == 8) {
            return false;
        }
        return FIGURES[figureCode].checkMove(curentPosition, new PositionOnBoard(i, j), board);
    }

    /**
     * проверка что ход на короля
     */
    private boolean isMateMove(PositionOnBoard finish, byte[][] board) {
        return Math.abs(board[finish.row()][finish.column()]) == 6;
    }
}
