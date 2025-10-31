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
        if (chessDesk[rawEndPos] == 0 || (chessDesk[rawEndPos] < 0) != isWhite) {
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
        int verticalUpPosition = rawStartPos;
        //Позиция, движущаяся по доске вертикально вниз
        int verticalDownPosition = rawStartPos;
        //Позиция, движущаяся по доске горизонтально влево
        int horizontalLeftPosition = rawStartPos;
        //Позиция, движущаяся по доске горизонтально вправо
        int horizontalRightPosition = rawStartPos;
        verticalUpPosition = positionConverter.verticalMoving(1, verticalUpPosition, PositionConverter.DIRECTION_OF_SHIFT.UP);
        verticalDownPosition = positionConverter.verticalMoving(1, verticalDownPosition, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
        horizontalLeftPosition = positionConverter.horizontalMoving(1, horizontalLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
        horizontalRightPosition = positionConverter.horizontalMoving(1, horizontalRightPosition, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
        while (verticalUpPosition >= 0 || verticalDownPosition >= 0 ||
                horizontalRightPosition >= 0 || horizontalLeftPosition >= 0) {
            if (verticalUpPosition >= 0) {
                if (chessDesk[verticalUpPosition] == 0) {
                    possibleMoves.add(verticalUpPosition);
                    verticalUpPosition = positionConverter.verticalMoving(1, verticalUpPosition, PositionConverter.DIRECTION_OF_SHIFT.UP);
                } else {
                    if ((chessDesk[verticalUpPosition] < 0) != isWhite) {
                        possibleMoves.add(verticalUpPosition);
                    }
                    verticalUpPosition = -1;

                }
            }
            if (verticalDownPosition >= 0) {
                if (chessDesk[verticalDownPosition] == 0) {
                    possibleMoves.add(verticalDownPosition);
                    verticalDownPosition = positionConverter.verticalMoving(1, verticalDownPosition, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
                } else {
                    if ((chessDesk[verticalDownPosition] < 0) != isWhite) {
                        possibleMoves.add(verticalDownPosition);
                    }
                    verticalDownPosition = -1;

                }
            }
            if (horizontalLeftPosition >= 0) {
                if (chessDesk[horizontalLeftPosition] == 0) {
                    possibleMoves.add(horizontalLeftPosition);
                    horizontalLeftPosition = positionConverter.horizontalMoving(1, horizontalLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
                } else {
                    if ((chessDesk[horizontalLeftPosition] < 0) != isWhite) {
                        possibleMoves.add(horizontalLeftPosition);
                    }
                    horizontalLeftPosition = -1;

                }
            }
            if (horizontalRightPosition >= 0) {
                if (chessDesk[horizontalRightPosition] == 0) {
                    possibleMoves.add(horizontalRightPosition);
                    horizontalRightPosition = positionConverter.horizontalMoving(1, horizontalRightPosition, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
                } else {
                    if ((chessDesk[horizontalRightPosition] < 0) != isWhite) {
                        possibleMoves.add(horizontalRightPosition);
                    }
                    horizontalRightPosition = -1;
                }
            }
        }
        return possibleMoves;
    }

}