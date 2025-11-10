package org.example.chess;

import org.example.states.GameState;

import java.util.List;

public class GameHandler {
    public enum moveProperty {
        INVALID,
        IMPOSSIBLE,
        REGULAR,
        CHECK,
        MATE
    }

    private final static int MIN_SIDE_VALUE = 0;
    private final static int MAX_SIDE_VALUE = 7;
    private final static int FIGURES_COUNT = 6;

    private final Chessmen[] FIGURES = {
            new Pawn(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Rook(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Knight(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Bishop(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Queen(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new King(MIN_SIDE_VALUE, MAX_SIDE_VALUE)
    };

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
        //координаты короля

        int i = 0, j = 0;
        if (board[curentPosition.row()][curentPosition.column()] < 0) {
            for (; i < 8; ++i) {
                for (; j < 8; ++j) {
                    if (board[i][j] == 6)
                        break;
                }
            }
        } else {
            for (; i < 8; ++i) {
                for (; j < 8; ++j) {
                    if (board[i][j] == -6)
                        break;
                }
            }
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
