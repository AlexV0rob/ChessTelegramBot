package org.example.movement;

import org.example.chess.PositionOnBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, ответственный за перемещение шахматных фигур по вертикали и горизонтали
 */
public class VerticalAndHorizontalMovement extends BaseMovement {

    /**
     * Без сдвига
     */
    private static final int NO_SHIFT = 0;
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
        possibleMoves.addAll(allPossibleMovesInDirection(start, VERTICAL_SHIFT, NO_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesInDirection(start, -VERTICAL_SHIFT, NO_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesInDirection(start, NO_SHIFT, HORIZONTAL_SHIFT, board));
        possibleMoves.addAll(allPossibleMovesInDirection(start, NO_SHIFT, -HORIZONTAL_SHIFT, board));
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
        while (true) {
            currentPosition = move(startPosition, verticalShift * currentVerticalShift,
                    horizontalShift * currentHorizontalShift, board);
            if (currentPosition.row() == SIGNAL_POS || currentPosition.column() == SIGNAL_POS) {
                break;
            }
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
     * Выполнить ход им вернуть сигнальную позицию, в случае если в конечную точку невозможно попасть
     */
    private PositionOnBoard move(PositionOnBoard start, int verticalShift, int horizontalShift, byte[][] board) {
        if (horizontalShift != 0) {
            if (isShiftAvailable(NO_SHIFT, horizontalShift, start.row(), start.column(), board)) {
                return new PositionOnBoard(start.row(), start.column() + horizontalShift);
            }
        } else if (verticalShift != 0) {
            if (isShiftAvailable(verticalShift, NO_SHIFT, start.row(), start.column(), board)) {
                return new PositionOnBoard(start.row() + verticalShift, start.column());
            }
        }
        return new PositionOnBoard(SIGNAL_POS, SIGNAL_POS);
    }
}
