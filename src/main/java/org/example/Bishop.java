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
        if (chessDesk[rawEndPos] == 0 || (chessDesk[rawEndPos] < 0) != isWhite) {
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
     * Проверка отсутствия препядствий дляна пути из начала пути в конец
     */
    private boolean isWayFree(int rawStartPos, int rawEndPos, byte[] chessDesk,
                              PositionConverter positionConverter) {
        PositionConverter.SHIFT_PROPERTY verticalProperty = PositionConverter.SHIFT_PROPERTY.EQUAL;
        if (positionConverter.positionRow(rawStartPos) < positionConverter.positionRow((rawEndPos))) {
            verticalProperty = PositionConverter.SHIFT_PROPERTY.GREATER;
        } else if (positionConverter.positionRow(rawStartPos) > positionConverter.positionRow((rawEndPos))) {
            verticalProperty = PositionConverter.SHIFT_PROPERTY.LESS;
        }
        PositionConverter.SHIFT_PROPERTY horizontalProperty = PositionConverter.SHIFT_PROPERTY.EQUAL;
        if (positionConverter.positionColumn(rawStartPos) < positionConverter.positionColumn((rawEndPos))) {
            horizontalProperty = PositionConverter.SHIFT_PROPERTY.GREATER;
        } else if (positionConverter.positionColumn(rawStartPos) > positionConverter.positionColumn((rawEndPos))) {
            horizontalProperty = PositionConverter.SHIFT_PROPERTY.LESS;
        }
        int currentPosition = positionConverter.refreshCurrentPosition
                (verticalProperty,
                        horizontalProperty, rawStartPos);
        while (currentPosition >= 0 && chessDesk[currentPosition] == 0 &&
                currentPosition != rawEndPos) {
            currentPosition = positionConverter.refreshCurrentPosition(verticalProperty,
                    horizontalProperty, currentPosition);
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
                verticalMoving(1, currentPositionUpAndLeft, PositionConverter.DIRECTION_OF_SHIFT.UP);
        currentPositionUpAndLeft = positionConverter.
                horizontalMoving(1, currentPositionUpAndLeft, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
        currentPositionUpAndRight = positionConverter.
                verticalMoving(1, currentPositionUpAndRight, PositionConverter.DIRECTION_OF_SHIFT.UP);
        currentPositionUpAndRight = positionConverter.
                horizontalMoving(1, currentPositionUpAndRight, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);

        currentPositionDownAndLeft = positionConverter.
                verticalMoving(1, currentPositionDownAndLeft, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
        currentPositionDownAndLeft = positionConverter.
                horizontalMoving(1, currentPositionDownAndLeft, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
        currentPositionDownAndRight = positionConverter.
                verticalMoving(1, currentPositionDownAndRight, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
        currentPositionDownAndRight = positionConverter.
                horizontalMoving(1, currentPositionDownAndRight, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
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
                            verticalMoving(1, currentPositionUpAndLeft, PositionConverter.DIRECTION_OF_SHIFT.UP);
                    currentPositionUpAndLeft = positionConverter.
                            horizontalMoving(1, currentPositionUpAndLeft, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
                } else {
                    if ((chessDesk[currentPositionUpAndLeft] < 0) != isWhite) {
                        possibleMoves.add(currentPositionUpAndLeft);
                    }
                    currentPositionUpAndLeft = -1;
                }
            }
            if (currentPositionUpAndRight >= 0) {
                if (chessDesk[currentPositionUpAndRight] == 0) {
                    possibleMoves.add(currentPositionUpAndRight);
                    currentPositionUpAndRight = positionConverter.
                            verticalMoving(1, currentPositionUpAndRight, PositionConverter.DIRECTION_OF_SHIFT.UP);
                    currentPositionUpAndRight = positionConverter.
                            horizontalMoving(1, currentPositionUpAndRight, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
                } else {
                    if ((chessDesk[currentPositionUpAndRight] < 0) != isWhite) {
                        possibleMoves.add(currentPositionUpAndRight);
                    }
                    currentPositionUpAndRight = -1;
                }
            }
            if (currentPositionDownAndLeft >= 0) {
                if (chessDesk[currentPositionDownAndLeft] == 0) {
                    possibleMoves.add(currentPositionDownAndLeft);
                    currentPositionDownAndLeft = positionConverter.
                            verticalMoving(1, currentPositionDownAndLeft, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
                    currentPositionDownAndLeft = positionConverter.
                            horizontalMoving(1, currentPositionDownAndLeft, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
                } else {
                    if ((chessDesk[currentPositionDownAndLeft] < 0) != isWhite) {
                        possibleMoves.add(currentPositionDownAndLeft);
                    }
                    currentPositionDownAndLeft = -1;
                }
            }
            if (currentPositionDownAndRight >= 0) {
                if (chessDesk[currentPositionDownAndRight] == 0) {
                    possibleMoves.add(currentPositionDownAndRight);
                    currentPositionDownAndRight = positionConverter.
                            verticalMoving(1, currentPositionDownAndRight, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
                    currentPositionDownAndRight = positionConverter.
                            horizontalMoving(1, currentPositionDownAndRight, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
                } else {
                    if ((chessDesk[currentPositionDownAndRight] < 0) != isWhite) {
                        possibleMoves.add(currentPositionDownAndRight);
                    }
                    currentPositionDownAndRight = -1;
                }
            }
        }

        return possibleMoves;
    }
}
