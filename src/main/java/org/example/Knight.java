package org.example;

/**
 * Класс для реализации логики перемещения коня
 */
class Knight implements Chessmen {
	/**
	 * константа, соотвествующая длинне массива доски
	 */
	private final static int DESK_LENGTH = 64;
	/**
	 * @param rawStartPos - стартовая позиция фигуры в одномерном массиве доски
	 * @param rawEndPos - предполагаемая конечная позиция фигуры в одномерном массиве доски
	 * @param chessDesk - одномерный массив с позициями всех фигур на шахматной доске
	 * @return можно ли сходить на предполагаемую конечную позицию 
	 */
	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite) {

		if ((chessDesk[rawEndPos] == 0) || ((chessDesk[rawEndPos] > 0) != isWhite)) {
			// проверяем все сдвиги, соотвествующие возможным ходам коня
			byte[] shifts = { 6, 7, 10, 15, 17 };
			for (byte shift : shifts) {
				if ((rawStartPos + shift < DESK_LENGTH) && (rawStartPos + shift == rawEndPos))
					return true;
				if ((rawStartPos + shift > 0) && (rawStartPos + shift == rawEndPos))
					return true;
			}
		}
		return false;
	}

	public int[] everyRightMove(int rawStartPos, byte[] chessDesk, boolean isWhite) {
		int[] rightMoves = new int[28];
		int curPos = 0;
		for (int i = 0; i < 28; ++i)
			rightMoves[i] = -1;
		int[] shifts = { 6, 7, 10, 15, 17 };
		for (int shift : shifts) {
			if ((rawStartPos + shift < DESK_LENGTH) && (chessDesk[rawStartPos + shift] == 0)
					|| ((chessDesk[rawStartPos + shift] > 0) != isWhite))
				rightMoves[curPos++] = rawStartPos + shift;
			if ((rawStartPos - shift > 0) && (chessDesk[rawStartPos - shift] == 0)
					|| ((chessDesk[rawStartPos - shift] > 0) != isWhite))
				rightMoves[curPos++] = rawStartPos - shift;
		}
		return rightMoves;
	}
}