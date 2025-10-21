package org.example;

/**
 * Класс для реализации логики перемещения Ладьи
 */
class Rook implements Chessmen {
	/**
	 * Сдвиг по горизонтали
	 */
	final static int horizontalShift = 1;
	/**
	 * Сдвиг по вертикали
	 */
	final static int verticalShift = 8;
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
				for (int i = rawStartPos + horizontalShift; i < rawEndPos; i += horizontalShift)
					if (chessDesk[i] != 0)
						return false;
			} else {
				for (int i = rawStartPos - horizontalShift; i > rawEndPos; i -= horizontalShift)
					if (chessDesk[i] != 0)
						return false; 
			}
		} else {
			if (rawEndPos > rawStartPos) {
				for (int i = rawStartPos + verticalShift; i < rawEndPos; i += verticalShift)
					if (chessDesk[i] != 0)
						return false;
			} else {
				for (int i = rawStartPos - verticalShift; i > rawEndPos; i -= verticalShift)
					if (chessDesk[i] != 0)
						return false;
			}
		}

		return true;
	}
}
