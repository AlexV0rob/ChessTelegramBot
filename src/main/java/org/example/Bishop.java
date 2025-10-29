package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения слона
 */
public class Bishop implements Chessmen {
    private int rawStartPos;
    private byte[] chessDesk;
    private boolean isWhite;
    private PositionConverter positionConverter;

    @Override
    public boolean checkMove(
            int rawStartPos, int rawEndPos, byte[] chessDesk,
            boolean isWhite, PositionConverter positionConverter) {
        /*
         * Проверяем правильность хода в два этапа:
         * 1) Смотрим что интересующая насклетка нас не занята или там находится
         * шахматная фигура оппонента
         * 2) Проверяем что слон может так сходить
         */
        if (chessDesk[rawEndPos] == 0 || (chessDesk[rawEndPos] > 0) != isWhite) {
            int startPosRow = positionConverter.positionRow(rawStartPos);
            int startPosColumn = positionConverter.positionColumn(rawStartPos);
            int endPosRow = positionConverter.positionRow(rawEndPos);
            int endPosColumn = positionConverter.positionColumn(rawEndPos);
            if (Math.abs(startPosRow - endPosRow) ==
                    Math.abs(startPosColumn - endPosColumn) &&
                    isWayFree(rawStartPos, rawEndPos, chessDesk, positionConverter)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяем что на пути у нашего слона нету преград делением с остатком на 7
     * проверяется приадлежность хода к левой диагонали а делением с остатком на 9
     * проверяется приадлежность хода к правой диагонали
     */
    private boolean isWayFree(int rawStartPos, int rawEndPos,
                              byte[] chessDesk, PositionConverter positionConverter) {
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
        this.rawStartPos = rawStartPos;
        this.chessDesk = chessDesk;
        this.isWhite = isWhite;
        this.positionConverter = positionConverter;
        List<Integer> possibleMoves = new ArrayList<Integer>();

        //Позиция, движущаяся по доске вверх и влево
        int currentPositionUpAndLeft = rawStartPos;
        //Позиция, движущаяся по доске вверх и вправо
        int currentPositionUpAndRight = rawStartPos;
        //Позиция, движущаяся по доске вниз и влево
        int currentPositionDownAndLeft = rawStartPos;
        //Позиция, движущаяся по доске вниз и вправо
        int currentPositionDownAndRight = rawStartPos;

        //Смещения до цикла, чтобы не задеть саму фигуру
        currentPositionUpAndLeft = positionConverter.
                nSquaresUpFromPositionX(1, currentPositionUpAndLeft);
        currentPositionUpAndLeft = positionConverter.
                nSquaresLeftFromPositionX(1, currentPositionUpAndLeft);
        currentPositionUpAndRight = positionConverter.
                nSquaresUpFromPositionX(1, currentPositionUpAndRight);
        currentPositionUpAndRight = positionConverter.
                nSquaresRightFromPositionX(1, currentPositionUpAndRight);
        currentPositionDownAndLeft = positionConverter.
                nSquaresDownFromPositionX(1, currentPositionDownAndLeft);

        currentPositionDownAndLeft = positionConverter.
                nSquaresLeftFromPositionX(1, currentPositionDownAndLeft);
        currentPositionDownAndRight = positionConverter.
                nSquaresDownFromPositionX(1, currentPositionDownAndRight);
        currentPositionDownAndRight = positionConverter.
                nSquaresRightFromPositionX(1, currentPositionDownAndRight);
        /*
         * Для каждой позиции идёт следующая проверка:
         * Если мы ещё не вышли за пределы доски ->
         *     Если на клетке свободно -> добавляем позицию и смещаемся в этом
         *         напрвлении дальше
         *     Иначе ->
         *         Если на клетке вражеская фигура -> добавляем позицию
         *         В любом случае ставим в позицию -1, сигнализируя, что в этом
         *             направлении дальше двигаться не получится
         */
        while (currentPositionUpAndLeft >= 0 || currentPositionUpAndRight >= 0 ||
                currentPositionDownAndLeft >= 0 || currentPositionDownAndRight >= 0) {
            if (currentPositionUpAndLeft >= 0) {
                if (chessDesk[currentPositionUpAndLeft] == 0) {
                    possibleMoves.add(currentPositionUpAndLeft);
                    currentPositionUpAndLeft = positionConverter.
                            nSquaresUpFromPositionX(1, currentPositionUpAndLeft);
                    currentPositionUpAndLeft = positionConverter.
                            nSquaresLeftFromPositionX(1, currentPositionUpAndLeft);
                } else {
                    if ((chessDesk[currentPositionUpAndLeft] > 0) != isWhite) {
                        possibleMoves.add(currentPositionUpAndLeft);
                    }
                    currentPositionUpAndLeft = -1;
                }
            }
            if (currentPositionUpAndRight >= 0) {
                if (chessDesk[currentPositionUpAndRight] == 0) {
                    possibleMoves.add(currentPositionUpAndRight);
                    currentPositionUpAndRight = positionConverter.
                            nSquaresUpFromPositionX(1, currentPositionUpAndRight);
                    currentPositionUpAndRight = positionConverter.
                            nSquaresRightFromPositionX(1, currentPositionUpAndRight);
                } else {
                    if ((chessDesk[currentPositionUpAndRight] > 0) != isWhite) {
                        possibleMoves.add(currentPositionUpAndRight);
                    }
                    currentPositionUpAndRight = -1;
                }
            }
            if (currentPositionDownAndLeft >= 0) {
                if (chessDesk[currentPositionDownAndLeft] == 0) {
                    possibleMoves.add(currentPositionDownAndLeft);
                    currentPositionDownAndLeft = positionConverter.
                            nSquaresDownFromPositionX(1, currentPositionDownAndLeft);
                    currentPositionDownAndLeft = positionConverter.
                            nSquaresLeftFromPositionX(1, currentPositionDownAndLeft);
                } else {
                    if ((chessDesk[currentPositionDownAndLeft] > 0) != isWhite) {
                        possibleMoves.add(currentPositionDownAndLeft);
                    }
                    currentPositionDownAndLeft = -1;
                }
            }
            if (currentPositionDownAndRight >= 0) {
                if (chessDesk[currentPositionDownAndRight] == 0) {
                    possibleMoves.add(currentPositionDownAndRight);
                    currentPositionDownAndRight = positionConverter.
                            nSquaresDownFromPositionX(1, currentPositionDownAndRight);
                    currentPositionDownAndRight = positionConverter.
                            nSquaresRightFromPositionX(1, currentPositionDownAndRight);
                } else {
                    if ((chessDesk[currentPositionDownAndRight] > 0) != isWhite) {
                        possibleMoves.add(currentPositionDownAndRight);
                    }
                    currentPositionDownAndRight = -1;
                }
            }
        }

        return possibleMoves;
    }
}
