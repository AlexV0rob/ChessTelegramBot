package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Ладьи
 */
class Rook implements Chessmen {
    /**
     * Сдвиг по горизонтали
     */
    final static int HORIZONTAL_SHIFT = 1;
    /**
     * Сдвиг по вертикали
     */
    final static int VERTICAL_SHIFT = 8;
    /**
     * константа, соотвествующая длинне массива доски
     */
    private final static int LAST_INDEX_IN_DESK = 63;
    /**
     * длина линии
     */
    static int LINE_LENGTH = 8;

    /**
     * @param rawStartPos - стартовая позиция фигуры в одномерном массиве доски
     * @param rawEndPos   - предполагаемая конечная позиция фигуры в одномерном массиве доски
     * @param chessDesk   - одномерный массив с позициями всех фигур на шахматной доске
     * @return можно ли сходить на предполагаемую конечную позицию
     */
    @Override
    public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite,
                             PositionConverter positionConverter) {
        if (chessDesk[rawEndPos] == 0 || (chessDesk[rawEndPos] > 0) != isWhite) {
            int startPosRow = positionConverter.positionRow(rawStartPos);
            int startPosColumn = positionConverter.positionColumn(rawStartPos);
            int endPosRow = positionConverter.positionRow(rawEndPos);
            int endPosColumn = positionConverter.positionColumn(rawEndPos);
            if ((Math.abs(endPosRow - startPosRow) == 0
                    ^ Math.abs(endPosColumn - startPosColumn) == 0) &&
                    isWayFree(rawStartPos, rawEndPos, chessDesk, positionConverter))
                return true;

        }
        return false;
    }

    private boolean isWayFree(int rawStartPos, int rawEndPos, byte[] chessDesk,
                              PositionConverter positionConverter) {
        boolean isEndUpperThanStart =
                positionConverter.positionRow(rawEndPos) <
                        positionConverter.positionRow(rawStartPos);
        boolean isEndLefterThanStart =
                positionConverter.positionColumn(rawEndPos) <
                        positionConverter.positionColumn(rawStartPos);
        int currentPosition = positionConverter.refreshCurrendPosition(isEndUpperThanStart,
                isEndLefterThanStart, rawStartPos);
        while (currentPosition >= 0 && chessDesk[currentPosition] == 0 &&
                currentPosition != rawEndPos) {
            currentPosition = positionConverter.refreshCurrendPosition(isEndUpperThanStart,
                    isEndLefterThanStart, currentPosition);
        }
        return currentPosition == rawEndPos;
    }

    @Override
    public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk,
                                           boolean isWhite, PositionConverter positionConverter) {
        List<Integer> somelist = new ArrayList<>();
        return somelist;
    }
}