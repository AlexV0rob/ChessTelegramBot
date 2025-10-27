package org.example;

/**
 * Класс для реализации логики перемещения пешки
 */
public class Pawn implements Chessmen {
	/**
	 * линия, с которой стартуют белые пешки
	 */
	private final static int WHITE_PAWN_START_LINE = 1;
	/**
	 * линия, с которой стартуют чёрные пешки
	 */
	private final static int BLACK_PAWN_START_LINE = 6;
	/**
	 * длина линии
	 */
	private final static int LINE_LENGTH = 8;
	/**
	 * @param rawStartPos - стартовая позиция фигуры в одномерном массиве доски
	 * @param rawEndPos - предполагаемая конечная позиция фигуры в одномерном массиве доски
	 * @param chessDesk - одномерный массив с позициями всех фигур на шахматной доске
	 * @return можно ли сходить на предполагаемую конечную позицию 
	 */
	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite) {
		Position pos = new Position();
		int[] startPos = pos.convertPosition(rawStartPos);
		int[] endPos = pos.convertPosition(rawEndPos);
		if (chessDesk[rawEndPos] == 0) {

			if ((Math.abs(endPos[0] - startPos[0]) == 1) && (startPos[1] == endPos[1]))
				return true;
			else if ((startPos[0] == WHITE_PAWN_START_LINE || startPos[0] == BLACK_PAWN_START_LINE)
					&& ((Math.abs(endPos[0] - startPos[0]) == 2) && (startPos[1]== endPos[1])))
				return true;
		}else if (((chessDesk[rawEndPos] > 0) != isWhite)) {
			if ((Math.abs(endPos[0] - startPos[0]) == 1) && (Math.abs(startPos[1] - endPos[1]) == 1))
				return true;
		}
		return false;
	}

	public int[] everyRightMove(int rawStartPos, byte[] chessDesk, boolean isWhite) {
		Position pos = new Position();
		int[] rightMoves = new int[28];
		for(int i = 0; i < 28; ++i)
			rightMoves[i] = -1;
		int[] startPos = pos.convertPosition(rawStartPos);
		int curPosition = 0;
		if ((chessDesk[rawStartPos + LINE_LENGTH] == 0) || ((chessDesk[rawStartPos + LINE_LENGTH] > 0) != isWhite))
			rightMoves[curPosition++] = rawStartPos + LINE_LENGTH;
		if ((startPos[0] == WHITE_PAWN_START_LINE || startPos[0] == BLACK_PAWN_START_LINE)
				&& ((chessDesk[rawStartPos + LINE_LENGTH * 2] == 0)
						|| ((chessDesk[rawStartPos + LINE_LENGTH * 2] > 0) != isWhite)))
			rightMoves[curPosition++] = rawStartPos + LINE_LENGTH;
		if (((chessDesk[rawStartPos + LINE_LENGTH - 1] > 0) != isWhite) && startPos[1] != 0)
			rightMoves[curPosition++] = rawStartPos + LINE_LENGTH - 1;
		if (((chessDesk[rawStartPos + LINE_LENGTH + 1] > 0) != isWhite) && startPos[1] != 7)
			rightMoves[curPosition++] = rawStartPos + LINE_LENGTH + 1;
		return rightMoves;
	}
}
