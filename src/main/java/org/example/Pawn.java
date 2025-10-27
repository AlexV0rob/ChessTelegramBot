package org.example;

/**
 * Класс для реализации логики перемещения пешки
 */
public class Pawn implements Chessmen {
	/**
	 * @param rawStartPos - стартовая позиция фигуры в одномерном массиве доски
	 * @param rawEndPos - предполагаемая конечная позиция фигуры в одномерном массиве доски
	 * @param chessDesk - одномерный массив с позициями всех фигур на шахматной доске
	 * @return можно ли сходить на предполагаемую конечную позицию 
	 */
	/**
	 * линия, с которой стартуют белые пешки
	 */
	static int WHITE_PAWN_START_LINE = 1;
	/**
	 * линия, с которой стартуют чёрные пешки
	 */
	static int BLACK_PAWN_START_LINE = 6;
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
}
