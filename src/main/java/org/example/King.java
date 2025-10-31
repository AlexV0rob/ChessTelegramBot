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
        if (chessDesk[rawEndPos] == 0 || (chessDesk[rawEndPos] > 0) != isWhite) {
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
        // позиция, передвигаемая против часовой снизу вверх по потенциальным ходам Короля
        int UpAndLeftPosition = rawStartPos;
        // позиция, передвигаемая по часовой свурху вниз потенциальным ходам Короля
        int DownAndRightPosition = rawStartPos;
        //сдвинем позиции на стартовые позиции
        UpAndLeftPosition = positionConverter.verticalMoving(1, UpAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.UP);
        UpAndLeftPosition = positionConverter.horizontalMoving(1, UpAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);

        DownAndRightPosition = positionConverter.verticalMoving(1, DownAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
        DownAndRightPosition = positionConverter.horizontalMoving(1, DownAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
        for (int i = 0; i < 2; ++i) {
            if (UpAndLeftPosition >= 0 &&
                    (chessDesk[UpAndLeftPosition] == 0 || (chessDesk[UpAndLeftPosition] < 0) != isWhite)) {
                possibleMoves.add(UpAndLeftPosition);
                UpAndLeftPosition = positionConverter.verticalMoving(1, UpAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.UP);

            }
            if (DownAndRightPosition >= 0 &&
                    (chessDesk[DownAndRightPosition] == 0 || (chessDesk[DownAndRightPosition] < 0) != isWhite)) {
                possibleMoves.add(DownAndRightPosition);
                DownAndRightPosition = positionConverter.verticalMoving(1, DownAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
            }
        }
        UpAndLeftPosition = positionConverter.horizontalMoving(1, UpAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
        DownAndRightPosition = positionConverter.horizontalMoving(1, DownAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);

        for (int i = 0; i < 2; ++i) {
            if (UpAndLeftPosition >= 0 &&
                    (chessDesk[UpAndLeftPosition] == 0 || (chessDesk[UpAndLeftPosition] < 0) != isWhite)) {
                possibleMoves.add(UpAndLeftPosition);
                UpAndLeftPosition = positionConverter.horizontalMoving(1, UpAndLeftPosition, PositionConverter.DIRECTION_OF_SHIFT.LEFT);

            }
            if (DownAndRightPosition >= 0 &&
                    (chessDesk[DownAndRightPosition] == 0 || (chessDesk[DownAndRightPosition] < 0) != isWhite)) {
                possibleMoves.add(DownAndRightPosition);
                DownAndRightPosition = positionConverter.horizontalMoving(1, DownAndRightPosition, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);
            }
        }
        return possibleMoves;
    }
}