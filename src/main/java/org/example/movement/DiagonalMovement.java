package org.example.movement;

import org.example.chess.PositionOnBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, ответственный за перемещение шахматных фигур по диагонали
 */
public class DiagonalMovement extends BaseMovement {
    /**
     * Координата сигнальной позиции
     */
    private static final int SIGNAL_POS = -1;
    /**
     * Сдвиг по вертикали
     */
    private static final int VERTICAL_SHIFT = 1;
    /**
     * Сдвиг по горизонтали
     */
    private static final int HORIZONTAL_SHIFT = 1;

    @Override
    public List<PositionOnBoard> allPossibleMoves(PositionOnBoard start, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        possibleMoves.addAll(allPossibleMovesInDirection(start, VERTICAL_SHIFT, HORIZONTAL_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesInDirection(start, -VERTICAL_SHIFT, -HORIZONTAL_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesInDirection(start, VERTICAL_SHIFT, -HORIZONTAL_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesInDirection(start, -VERTICAL_SHIFT, HORIZONTAL_SHIFT, board));
        return possibleMoves;
    }

    /**
     * Делает список доступых ходов в заданном направлении
     */
    private List<PositionOnBoard> allPossibleMovesInDirection(PositionOnBoard startPosition, int verticalShift,
                                                              int horizontalShift, byte[][] board) {
        List<PositionOnBoard> possibleMoves = new ArrayList<PositionOnBoard>();
        int currentVerticalShift = 1;
        int currentHorizontalShift = 1;
        PositionOnBoard currentPosition = startPosition;
        while (currentPosition.row() != SIGNAL_POS &&
                currentPosition.column() != SIGNAL_POS &&
                isShiftAvailable(verticalShift * currentVerticalShift,
                        horizontalShift * currentHorizontalShift, startPosition.row(),
                        startPosition.column(), board)) {
            currentPosition = move(startPosition, verticalShift * currentVerticalShift,
                    horizontalShift * currentHorizontalShift, board);
            possibleMoves.add(currentPosition);
            if (isPositionEnemy(startPosition.row(), startPosition.column(),
                    currentPosition.row(), currentPosition.column(), board)) {
                break;
            }
            currentVerticalShift++;
            currentHorizontalShift++;
        }
        return possibleMoves;
    }

    /**
     * Функция, делающая сдвиг в заданном направлении
     */
    private PositionOnBoard move(PositionOnBoard startPosition,
                                 int verticalShift, int horizontalShift, byte[][] board) {
        if (isShiftAvailable(verticalShift, horizontalShift, startPosition.row(), startPosition.column(), board)) {
            return new PositionOnBoard(startPosition.row() + verticalShift,
                    startPosition.column() + horizontalShift);
        }
        return new PositionOnBoard(SIGNAL_POS, SIGNAL_POS);
    }
}