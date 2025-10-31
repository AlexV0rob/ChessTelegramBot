package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Короля
 */
class King implements Chessmen {
    @Override
    public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite,
                             PositionConverter positionConverter) {
        if (chessDesk[rawEndPos] == 0 || (chessDesk[rawEndPos] < 0) != isWhite) {
            int startPosRow = positionConverter.positionRow(rawStartPos);
            int startPosColumn = positionConverter.positionColumn(rawStartPos);
            int endPosRow = positionConverter.positionRow(rawEndPos);
            int endPosColumn = positionConverter.positionColumn(rawEndPos);
            if (Math.abs(endPosRow - startPosRow) <= 1 && Math.abs(endPosColumn - startPosColumn) <= 1)
                return true;
        }
        return false;
    }

    @Override
    public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk, boolean isWhite,
                                           PositionConverter positionConverter) {
        List<Integer> possibleMoves = new ArrayList<>();
        // Создаём 4 позиции, которые образую своеобразный крест вокруг клетки короля
        int downPos = positionConverter.verticalMoving(1, rawStartPos,
                PositionConverter.DIRECTION_OF_SHIFT.DOWN);
        int upPos = positionConverter.verticalMoving(1, rawStartPos,
                PositionConverter.DIRECTION_OF_SHIFT.UP);
        int leftPos = positionConverter.horizontalMoving(1, rawStartPos,
                PositionConverter.DIRECTION_OF_SHIFT.LEFT);
        int rightPos = positionConverter.horizontalMoving(1, rawStartPos,
                PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
        /*Проверяем верхнюю и нижнюю клетки на валидность. Если ход в клетку  не валиден по причине нахождения
         союзной фигуры, то есть смысл сдвинутся по против часовой стрелки и проверить валидность хода в новой клетке
         */
        if (downPos >= 0 && (chessDesk[downPos] == 0 || chessDesk[downPos] < 0 != isWhite)) {
            possibleMoves.add(downPos);
        }
        if (downPos >= 0) {
            downPos = positionConverter.horizontalMoving(1, downPos,
                    PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
            if (downPos >= 0 && (chessDesk[downPos] == 0 || chessDesk[downPos] < 0 != isWhite))
                possibleMoves.add(downPos);
        }

        if (upPos >= 0 && (chessDesk[upPos] == 0 || chessDesk[upPos] < 0 != isWhite)) {
            possibleMoves.add(upPos);
        }
        if (upPos >= 0) {
            upPos = positionConverter.horizontalMoving(1, upPos,
                    PositionConverter.DIRECTION_OF_SHIFT.LEFT);
            if (upPos >= 0 && (chessDesk[upPos] == 0 || chessDesk[upPos] < 0 != isWhite))
                possibleMoves.add(upPos);
        }
        /*
         Для полного прохода по всем возможных ходам короля, проверка левой и правой клетки были разделены на два
         случая: Для белого и для черного королей.
         Для белого короля левая клетка смещается вверх по массиву, а правая - вверх.
         Для Чёрного короля всё наоборот: левая вниз, а правая вверх
         */
        if (chessDesk[rawStartPos] < 0) {
            if (leftPos >= 0 && (chessDesk[leftPos] == 0 || chessDesk[leftPos] < 0 != isWhite)) {
                possibleMoves.add(leftPos);
            }
            if (leftPos >= 0) {
                leftPos = positionConverter.verticalMoving(1, leftPos,
                        PositionConverter.DIRECTION_OF_SHIFT.DOWN);
                if (leftPos >= 0 && (chessDesk[leftPos] == 0 || chessDesk[leftPos] < 0 != isWhite))
                    possibleMoves.add(leftPos);
            }
            if (rightPos >= 0 && (chessDesk[rightPos] == 0 || chessDesk[rightPos] < 0 != isWhite)) {
                possibleMoves.add(rightPos);
            }
            if (rightPos >= 0) {
                rightPos = positionConverter.verticalMoving(1, rightPos,
                        PositionConverter.DIRECTION_OF_SHIFT.UP);
                if (rightPos >= 0 && (chessDesk[rightPos] == 0 || chessDesk[rightPos] < 0 != isWhite))
                    possibleMoves.add(rightPos);
            }
        } else {
            if (leftPos >= 0 && (chessDesk[leftPos] == 0 || chessDesk[leftPos] < 0 != isWhite)) {
                possibleMoves.add(leftPos);
            }
            if (leftPos >= 0) {
                leftPos = positionConverter.verticalMoving(1, leftPos,
                        PositionConverter.DIRECTION_OF_SHIFT.UP);
                if (leftPos >= 0 && (chessDesk[leftPos] == 0 || chessDesk[leftPos] < 0 != isWhite))
                    possibleMoves.add(leftPos);
            }
            if (rightPos >= 0 && (chessDesk[rightPos] == 0 || chessDesk[rightPos] < 0 != isWhite)) {
                possibleMoves.add(rightPos);
            }
            if (rightPos >= 0) {
                rightPos = positionConverter.verticalMoving(1, rightPos,
                        PositionConverter.DIRECTION_OF_SHIFT.DOWN);
                if (rightPos >= 0 && (chessDesk[rightPos] == 0 || chessDesk[rightPos] < 0 != isWhite))
                    possibleMoves.add(rightPos);
            }
        }
        return possibleMoves;
    }
}