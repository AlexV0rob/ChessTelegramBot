package org.example;

import java.util.List;

/**
 * Класс для произведения хода у конкретного пользователя
 */
public class GameHandler {
    /**
     * Массив всех фигур
     */
    private final Chessmen[] figureList;
    /**
     * Число клеток на доске
     */
    private final static int DESK_LENGTH = 64;
    /**
     * Максимальная позиция на доске
     */
    private final static int LAST_INDEX_IN_DESK = DESK_LENGTH - 1;
    /**
     * Минимальная позиция на доске
     */
    private final static int FIRST_INDEX_IN_DESK = 0;

    /**
     * Длина одной линии клеток
     */
    private final static int LINE_LENGTH = (int) Math.sqrt(DESK_LENGTH);
    /**
     * Код белого короля
     */
    private final static int WHITE_KING = 6;
    /**
     * Код чёрного короля
     */
    private final static int BLACK_KING = -6;
    /**
     * Число фигур
     */
    private final static int CHESSMEN_COUNT = 6;

    public GameHandler() {
        figureList = new Chessmen[CHESSMEN_COUNT];
        figureList[0] = new Pawn();
        figureList[1] = new Rook();
        figureList[2] = new Knight();
        figureList[3] = new Bishop();
        figureList[4] = new Queen();
        figureList[5] = new King();
    }

    /**
     * Преобразователь и обработчик позиций одномерного массива
     */
    private final PositionConverter positionConverter =
            new PositionConverter(LINE_LENGTH,
                    FIRST_INDEX_IN_DESK,
                    LAST_INDEX_IN_DESK);

    /**
     * Проверить ход на правильность
     *
     * @return характеристика хода
     */
    public GameState.MOVE_PROPERTIES handleMove(byte[] curDesk,
                                                int rawStartPos, int rawEndPos, byte figureCode) {
        Chessmen lastChessmen;
        boolean isNormalMove = false;
        if ((rawStartPos > LAST_INDEX_IN_DESK || rawStartPos < 0) ||
                (rawEndPos > LAST_INDEX_IN_DESK || rawEndPos < 0) ||
                (Math.abs(figureCode) > CHESSMEN_COUNT || figureCode == 0) ||
                (curDesk[rawStartPos] != figureCode)
        ) {
            return GameState.MOVE_PROPERTIES.IMPOSSIBLE;
        }
        lastChessmen = figureList[Math.abs(figureCode) - 1];

        isNormalMove = lastChessmen
                .checkMove(rawStartPos, rawEndPos, curDesk,
                        rawStartPos < 0, positionConverter);
        if (isNormalMove) {
            if (isThisMoveOnKing(curDesk, rawEndPos, rawStartPos < 0)) {
                return GameState.MOVE_PROPERTIES.CHECKMATE;
            }
            if (isCheck(rawEndPos, curDesk, rawStartPos < 0, lastChessmen)) {
                return GameState.MOVE_PROPERTIES.CHECK;
            }
            return GameState.MOVE_PROPERTIES.REGULAR;
        } else
            return GameState.MOVE_PROPERTIES.IMPOSSIBLE;
    }

    /**
     * Является ли ход шахом
     */
    public boolean isCheck(int rawStartPos,
                           byte[] curBoard,
                           boolean isWhiteMove, Chessmen chessmen) {
        int i = 0;
        if (isWhiteMove) {
            while (i < DESK_LENGTH) {
                if (curBoard[i] == BLACK_KING)
                    break;
                i++;
            }
            if (chessmen.checkMove(
                    rawStartPos, i, curBoard, isWhiteMove, positionConverter))
                return true;
        } else {
            while (i < DESK_LENGTH) {
                if (curBoard[i] == WHITE_KING)
                    break;
                i++;
            }
            if (chessmen.checkMove(
                    rawStartPos, i, curBoard, isWhiteMove, positionConverter))
                return true;
        }
        return false;
    }

    /**
     * Рубят ли короля
     */
    public boolean isThisMoveOnKing(
            byte[] curBoard, int rawPosition, boolean isWhiteMove) {
        if (isWhiteMove)
            return curBoard[rawPosition] == BLACK_KING;
        return curBoard[rawPosition] == WHITE_KING;
    }

    /**
     * Получить список всех возможных ходов для данной фигуры из данной позиции
     */
    public List<Integer> everyPossibleRightMove(
            byte figure, int rawStartPos, byte[] chessDesk, boolean isWhiteMove) {
        return figureList[Math.abs(figure) - 1]
                .everyPossibleMove(rawStartPos, chessDesk,
                        isWhiteMove, positionConverter);
    }
}
