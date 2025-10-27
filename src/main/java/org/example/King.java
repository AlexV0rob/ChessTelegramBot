package org.example;

/**
 * Класс для реализации логики перемещения Короля
 */
class King implements Chessmen {
	/**
	 * константа, соотвествующая длинне массива доски
	 */
	private final static int LAST_INDEX_IN_DESK = 63;
	/**
	 * @param rawStartPos - стартовая позиция фигуры в одномерном массиве доски
	 * @param rawEndPos - предполагаемая конечная позиция фигуры в одномерном массиве доски
	 * @param chessDesk - одномерный массив с позициями всех фигур на шахматной доске
	 * @return можно ли сходить на предполагаемую конечную позицию 
	 */
	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite) {
		if ((chessDesk[rawEndPos] == 0) || ((chessDesk[rawEndPos] > 0) != isWhite)) {
			Position pos = new Position();
			int[] startPos = pos.convertPosition(rawStartPos);
			int[] endPos = pos.convertPosition(rawEndPos);
			if ((Math.abs(startPos[0] - endPos[0]) <= 1) && (Math.abs(startPos[1] - endPos[1]) <= 1))
				return true;
		}
		return false;
	}

	public int[] everyRightMove(int rawStartPos, byte[] chessDesk, boolean isWhite) {
		int[] rightMoves = new int[28];
		int curPos = 0;
		for (int i = 0; i < 28; ++i)
			rightMoves[i] = -1;
		int[] shifts = { 1, 8, 7, 9 };
		for (int i : shifts) {
			if ((rawStartPos + i < LAST_INDEX_IN_DESK)
					&& ((chessDesk[rawStartPos + i] == 0) || ((chessDesk[rawStartPos + i] > 0) != isWhite)))
				rightMoves[curPos++] = rawStartPos + i;
			if ((rawStartPos - i > 0)
					&& ((chessDesk[rawStartPos - i] == 0) || ((chessDesk[rawStartPos - i] > 0) != isWhite)))
				rightMoves[curPos++] = rawStartPos - i;
		}
		return rightMoves;
	}
}