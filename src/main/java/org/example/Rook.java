package org.example;

/**
 * Класс для реализации логики перемещения Ладьи
 */
class Rook implements Chessmen {
	/**
	 * Сдвиг по горизонтали
	 */
	private final static int HORIZONTAL_SHIFT = 1;
	/**
	 * Сдвиг по вертикали
	 */
	private final static int VERTICAL_SHIFT = 8;
	/**
	 * константа, соотвествующая длинне массива доски
	 */
	private final static int LAST_INDEX_IN_DESK = 63;
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
		if ((rawStartPos > 63 || rawStartPos < 0) || (rawEndPos > 63 || rawEndPos < 0))
			return false;
		if ((chessDesk[rawEndPos] == 0) || ((chessDesk[rawEndPos] > 0) != isWhite)) {
			Position pos = new Position();
			int[] startPos = pos.convertPosition(rawStartPos);
			int[] endPos = pos.convertPosition(rawEndPos);
			if (((startPos[0] == endPos[0]) ^ (startPos[1]== endPos[1]))
					&& isThereObstacle(rawStartPos, rawEndPos, chessDesk))
				return true;
		}
		return false;
	}

	private boolean isThereObstacle(int rawStartPos, int rawEndPos, byte[] chessDesk) {
		// если разница меньше 7, то они на одной линии
		if (Math.abs(rawEndPos - rawStartPos) <= 7) {

			if (rawEndPos > rawStartPos) {
				for (int i = rawStartPos + HORIZONTAL_SHIFT; i < rawEndPos; i += HORIZONTAL_SHIFT)
					if (chessDesk[i] != 0)
						return false;
			} else {
				for (int i = rawStartPos - HORIZONTAL_SHIFT; i > rawEndPos; i -= HORIZONTAL_SHIFT)
					if (chessDesk[i] != 0)
						return false; 
			}
		} else {
			if (rawEndPos > rawStartPos) {
				for (int i = rawStartPos + VERTICAL_SHIFT; i < rawEndPos; i += VERTICAL_SHIFT)
					if (chessDesk[i] != 0)
						return false;
			} else {
				for (int i = rawStartPos - VERTICAL_SHIFT; i > rawEndPos; i -= VERTICAL_SHIFT)
					if (chessDesk[i] != 0)
						return false;
			}
		}

		return true;
	}
	public int[] everyRightMove(int rawStartPos, byte[] chessDesk, boolean isWhite) {
		int[] rightMoves = new int[28];
		int curPos = 0;
		for (int i = 0; i < 28; ++i)
			rightMoves[i] = -1;
		for (int i = 1; i < LINE_LENGTH; i += HORIZONTAL_SHIFT) {
			if ((chessDesk[rawStartPos + i] != 0) && ((chessDesk[rawStartPos + i] > 0) == isWhite))
				break;
			if (( rawStartPos - i > 0)&&(chessDesk[rawStartPos - i] != 0) && ((chessDesk[rawStartPos - i] > 0) == isWhite))
				break;
			if ((rawStartPos + i < LAST_INDEX_IN_DESK)
					&& ((rawStartPos + i) / LINE_LENGTH == rawStartPos / LINE_LENGTH))
				rightMoves[curPos++] = rawStartPos + i;
			if ((rawStartPos - i > 0) && ((rawStartPos - i) / LINE_LENGTH == rawStartPos / LINE_LENGTH))
				rightMoves[curPos++] = rawStartPos - i;
		}
		for (int i = 1; i < LAST_INDEX_IN_DESK; i += VERTICAL_SHIFT) {
			if ((chessDesk[rawStartPos + i] != 0) && ((chessDesk[rawStartPos + i] > 0) == isWhite))
				break;
			if (( rawStartPos - i > 0)&&(chessDesk[rawStartPos - i] != 0) && ((chessDesk[rawStartPos - i] > 0) == isWhite))
				break;
			if ((rawStartPos + i < LAST_INDEX_IN_DESK))
				rightMoves[curPos++] = rawStartPos + i;
			if ((rawStartPos - i > 0))
				rightMoves[curPos++] = rawStartPos - i;
		}
		return rightMoves;
	}
}
