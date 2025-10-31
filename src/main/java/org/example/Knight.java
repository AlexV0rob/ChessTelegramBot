package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения коня
 */
class Knight implements Chessmen {
    @Override
    public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite,
                             PositionConverter positionConverter) {
        if (chessDesk[rawEndPos] == 0 || (chessDesk[rawEndPos] > 0) != isWhite) {
            int startPosRow = positionConverter.positionRow(rawStartPos);
            int startPosColumn = positionConverter.positionColumn(rawStartPos);
            int endPosRow = positionConverter.positionRow(rawEndPos);
            int endPosColumn = positionConverter.positionColumn(rawEndPos);
            if ((Math.abs(startPosRow - endPosRow) == 2 && Math.abs(endPosColumn - startPosColumn) == 1)
                    || (Math.abs(startPosRow - endPosRow) == 1 && Math.abs(endPosColumn - startPosColumn) == 2))
                return true;
        }
        return false;
    }

    @Override
    public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk,
                                           boolean isWhite, PositionConverter positionConverter) {
        List<Integer> possibleMoves = new ArrayList<>();
        int RowStarPosition = positionConverter.positionRow(rawStartPos);
        //Позиция, движущаяся по доске вертикально вверх
        int UpAndLeftPosition = rawStartPos;
        //Позиция, движущаяся по доске вертикально вниз
        int UpAndRightPosition = rawStartPos;
        //Позиция, движущаяся по доске горизонтально влево
        int DownAndLeftPosition = rawStartPos;
        //Позиция, движущаяся по доске горизонтально вправо
        int DownAndRightPosition = rawStartPos;
        //Смещения позиций
        UpAndLeftPosition = positionConverter.verticalMoving(2, UpAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.UP);
        UpAndLeftPosition = positionConverter.horizontalMoving(1, UpAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.LEFT);

        UpAndRightPosition = positionConverter.verticalMoving(2, UpAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.UP);
        UpAndRightPosition = positionConverter.horizontalMoving(1, UpAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);

        DownAndLeftPosition = positionConverter.verticalMoving(2, DownAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
        DownAndLeftPosition = positionConverter.horizontalMoving(1, DownAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.LEFT);

        DownAndRightPosition = positionConverter.verticalMoving(2, DownAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
        DownAndRightPosition = positionConverter.horizontalMoving(1, DownAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
        if (UpAndLeftPosition >= 0 && (chessDesk[UpAndLeftPosition] == 0 || chessDesk[UpAndLeftPosition] < 0 != isWhite)) {
            possibleMoves.add(UpAndLeftPosition);
        }
        if (UpAndRightPosition >= 0 && (chessDesk[UpAndRightPosition] == 0 || chessDesk[UpAndRightPosition] < 0 != isWhite)) {
            possibleMoves.add(UpAndRightPosition);
        }
        if (DownAndLeftPosition >= 0 && (chessDesk[DownAndLeftPosition] == 0 || chessDesk[DownAndLeftPosition] < 0 != isWhite)) {
            possibleMoves.add(DownAndLeftPosition);
        }
        if (DownAndRightPosition >= 0 && (chessDesk[DownAndRightPosition] == 0 || chessDesk[DownAndRightPosition] < 0 != isWhite)) {
            possibleMoves.add(DownAndRightPosition);
        }
        UpAndLeftPosition = positionConverter.verticalMoving(1, rawStartPos, PositionConverter.DIRECTION_OF_SHIFT.UP);
        UpAndLeftPosition = positionConverter.horizontalMoving(2, UpAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.LEFT);

        UpAndRightPosition = positionConverter.verticalMoving(1, rawStartPos, PositionConverter.DIRECTION_OF_SHIFT.UP);
        UpAndRightPosition = positionConverter.horizontalMoving(2, UpAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);

        DownAndLeftPosition = positionConverter.verticalMoving(1, rawStartPos, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
        DownAndLeftPosition = positionConverter.horizontalMoving(2, DownAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.LEFT);

        DownAndRightPosition = positionConverter.verticalMoving(1, rawStartPos, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
        DownAndRightPosition = positionConverter.horizontalMoving(2, DownAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
        if (UpAndLeftPosition >= 0 && (chessDesk[UpAndLeftPosition] == 0 || chessDesk[UpAndLeftPosition] < 0 != isWhite)) {
            possibleMoves.add(UpAndLeftPosition);
        }
        if (UpAndRightPosition >= 0 && (chessDesk[UpAndRightPosition] == 0 || chessDesk[UpAndRightPosition] < 0 != isWhite)) {
            possibleMoves.add(UpAndRightPosition);
        }
        if (DownAndLeftPosition >= 0 && (chessDesk[DownAndLeftPosition] == 0 || chessDesk[DownAndLeftPosition] < 0 != isWhite)) {
            possibleMoves.add(DownAndLeftPosition);
        }
        if (DownAndRightPosition >= 0 && (chessDesk[DownAndRightPosition] == 0 || chessDesk[DownAndRightPosition] < 0 != isWhite)) {
            possibleMoves.add(DownAndRightPosition);
        }
        return possibleMoves;
    }
}