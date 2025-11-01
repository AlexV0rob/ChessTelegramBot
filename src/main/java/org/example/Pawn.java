package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения пешки
 */
public class Pawn implements Chessmen {
    /**
     * линия, с которой стартуют белые пешки
     */
    static int WHITE_PAWN_START_ROW = 6;
    /**
     * линия, с которой стартуют чёрные пешки
     */
    static int BLACK_PAWN_START_ROW = 1;

    @Override
    public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite,
                             PositionConverter positionConverter) {
        int startPosRow = positionConverter.positionRow(rawStartPos);
        int startPosColumn = positionConverter.positionColumn(rawStartPos);
        int endPosRow = positionConverter.positionRow(rawEndPos);
        int endPosColumn = positionConverter.positionColumn(rawEndPos);
        if (chessDesk[rawEndPos] == 0 && endPosRow - startPosRow == 1 * (isWhite ? -1 : 1))
            return true;
        else if (chessDesk[rawEndPos] == 0
                && (WHITE_PAWN_START_ROW == startPosRow || BLACK_PAWN_START_ROW == startPosRow)
                && endPosRow - startPosRow == 1 * (isWhite ? -2 : 2))
            return true;
        else if ((chessDesk[rawEndPos] < 0) != isWhite &&
                endPosRow - startPosRow == 1 * (isWhite ? -1 : 1)
                && Math.abs(startPosColumn - endPosColumn) == 1)
            return true;
        return false;
    }

    @Override
    public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk, boolean isWhite,
                                           PositionConverter positionConverter) {
        List<Integer> possibleMoves = new ArrayList<>();
        int startPosRow = positionConverter.positionRow(rawStartPos);
        //единичный ход пешки
        int oneTimeMovePos = rawStartPos;
        //двойной ход со стартовой позиции
        int twoTimesMovePos = rawStartPos;
        //ход, рубящий по диагонали вправо
        int rightDiagonalPos = rawStartPos;
        //ход, рубящий по диагонали влево
        int leftDiagonalPos = rawStartPos;
        // сдвигаем на позиции наши ходы
        if (chessDesk[rawStartPos] > 0) {

            oneTimeMovePos = positionConverter.verticalMoving(1, oneTimeMovePos, PositionConverter.DIRECTION_OF_SHIFT.UP);

            twoTimesMovePos = positionConverter.verticalMoving(2, twoTimesMovePos, PositionConverter.DIRECTION_OF_SHIFT.UP);

            rightDiagonalPos = positionConverter.verticalMoving(1, rightDiagonalPos, PositionConverter.DIRECTION_OF_SHIFT.UP);
            rightDiagonalPos = positionConverter.horizontalMoving(1, rightDiagonalPos, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);

            leftDiagonalPos = positionConverter.verticalMoving(1, leftDiagonalPos, PositionConverter.DIRECTION_OF_SHIFT.UP);
            leftDiagonalPos = positionConverter.horizontalMoving(1, leftDiagonalPos, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
            if (oneTimeMovePos >= 0 && chessDesk[oneTimeMovePos] == 0)
                possibleMoves.add(oneTimeMovePos);
            if (twoTimesMovePos >= 0 && chessDesk[twoTimesMovePos] == 0 && (startPosRow == BLACK_PAWN_START_ROW))
                possibleMoves.add(twoTimesMovePos);
            if (rightDiagonalPos >= 0 && chessDesk[rightDiagonalPos] != 0 && (chessDesk[rightDiagonalPos] < 0 != isWhite))
                possibleMoves.add(rightDiagonalPos);
            if (leftDiagonalPos >= 0 && chessDesk[leftDiagonalPos] != 0 && (chessDesk[leftDiagonalPos] < 0 != isWhite))
                possibleMoves.add(leftDiagonalPos);
        } else {
            oneTimeMovePos = positionConverter.verticalMoving(1, oneTimeMovePos, PositionConverter.DIRECTION_OF_SHIFT.DOWN);

            twoTimesMovePos = positionConverter.verticalMoving(2, twoTimesMovePos, PositionConverter.DIRECTION_OF_SHIFT.DOWN);

            rightDiagonalPos = positionConverter.verticalMoving(1, rightDiagonalPos, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
            rightDiagonalPos = positionConverter.horizontalMoving(1, rightDiagonalPos, PositionConverter.DIRECTION_OF_SHIFT.RIGHT);

            leftDiagonalPos = positionConverter.verticalMoving(1, leftDiagonalPos, PositionConverter.DIRECTION_OF_SHIFT.DOWN);
            leftDiagonalPos = positionConverter.horizontalMoving(1, leftDiagonalPos, PositionConverter.DIRECTION_OF_SHIFT.LEFT);
            if (oneTimeMovePos >= 0 && chessDesk[oneTimeMovePos] == 0)
                possibleMoves.add(oneTimeMovePos);
            if (twoTimesMovePos >= 0 && chessDesk[twoTimesMovePos] == 0 &&
                    (startPosRow == WHITE_PAWN_START_ROW))
                possibleMoves.add(twoTimesMovePos);
            if (rightDiagonalPos >= 0 && chessDesk[rightDiagonalPos] != 0 && (chessDesk[rightDiagonalPos] < 0 != isWhite))
                possibleMoves.add(rightDiagonalPos);
            if (leftDiagonalPos >= 0 && chessDesk[leftDiagonalPos] != 0 && (chessDesk[leftDiagonalPos] < 0 != isWhite))
                possibleMoves.add(leftDiagonalPos);
        }
        return possibleMoves;
    }
}