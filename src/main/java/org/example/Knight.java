package org.example;

/**
 * Класс для реализации логики перемещения коня
 */
class Knight implements Chessmen {
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
				if ((rawStartPos + shift < 64) && (rawStartPos + shift == rawEndPos))
					return true;
				if ((rawStartPos + shift > 0) && (rawStartPos + shift == rawEndPos))
					return true;
			}
		}
		return false;
	}
}