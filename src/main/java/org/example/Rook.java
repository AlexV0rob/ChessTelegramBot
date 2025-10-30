package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Ладьи
 */
class Rook implements Chessmen {
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
        List<Integer> possibleMoves = new ArrayList<>();
        int RowStarPosition = positionConverter.positionRow(rawStartPos);
        //Позиция, движущаяся по доске вертикально вверх
        int VerticalUpPosition = rawStartPos;
        //Позиция, движущаяся по доске вертикально вниз
        int VericalDownPosition = rawStartPos;
        //Позиция, движущаяся по доске горизонтально влево
        int HorizontalLeftPosition = rawStartPos;
        //Позиция, движущаяся по доске горизонтально вправо
        int HorizontalRightPosition = rawStartPos;
        //Смещения до цикла, чтобы не задеть саму фигуру
        VerticalUpPosition = positionConverter.
                nSquaresUpFromPositionX(1, VerticalUpPosition);
        VericalDownPosition = positionConverter.
                nSquaresDownFromPositionX(1, VericalDownPosition);
        HorizontalLeftPosition = positionConverter.
                nSquaresLeftFromPositionX(1, HorizontalLeftPosition);
        HorizontalRightPosition = positionConverter.
                nSquaresRightFromPositionX(1, HorizontalRightPosition);
        while (VerticalUpPosition >= 0 || VericalDownPosition >= 0 ||
                HorizontalLeftPosition >= 0 || HorizontalRightPosition >= 0) {
            if (VerticalUpPosition >= 0 && chessDesk[VerticalUpPosition] == 0) {
                possibleMoves.add(VerticalUpPosition);
                VerticalUpPosition = positionConverter.
                        nSquaresUpFromPositionX(1, VerticalUpPosition);
            }
            if (VericalDownPosition >= 0 && chessDesk[VericalDownPosition] == 0) {
                possibleMoves.add(VericalDownPosition);
                VericalDownPosition = positionConverter.
                        nSquaresDownFromPositionX(1, VericalDownPosition);
            }
            if (HorizontalLeftPosition >= 0
                    && RowStarPosition == positionConverter.positionRow(HorizontalLeftPosition)
                    && chessDesk[HorizontalLeftPosition] == 0) {
                possibleMoves.add(HorizontalLeftPosition);
                HorizontalLeftPosition = positionConverter.
                        nSquaresUpFromPositionX(1, HorizontalLeftPosition);
            }
            if (HorizontalRightPosition >= 0 && RowStarPosition == positionConverter.positionRow(HorizontalRightPosition)
                    && chessDesk[HorizontalRightPosition] == 0) {
                possibleMoves.add(VerticalUpPosition);
                HorizontalRightPosition = positionConverter.
                        nSquaresUpFromPositionX(1, HorizontalRightPosition);
            }
        }
        return possibleMoves;
    }

}