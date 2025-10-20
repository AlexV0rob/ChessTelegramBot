package org.example;


/**
 * Класс для реализации логики перемещения слона
 */
public class Bishop implements Chessmen {
	/**
	 * Сдвиг влево по горизонтали
	 */
	final static int leftShift = 7;
	/**
	 * Сдвиг вправо по горизонтали
	 */
	final static int rightShift = 9;
	/**
	 * @param rawStartPos - стартовая позиция фигуры в одномерном массиве доски
	 * @param rawEndPos - предполагаемая конечная позиция фигуры в одномерном массиве доски
	 * @param chessDesk - одномерный массив с позициями всех фигур на шахматной доске
	 * @return можно ли сходить на предполагаемую конечную позицию 
	 */
	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos, byte[]  chessDesk, boolean isWhite) {
		/**
		 * Проверяем правильность хода в два этапа: 
		 * 1) Смотрим что интересующая насклетка нас не занята или там находится шахматная фигура оппонента 
		 * 2)Проверяем что слон может так сходить
		 */

		if (((chessDesk[rawEndPos] == 0) || ((chessDesk[rawEndPos] > 0) != isWhite))
				&& isThereObstacle(rawStartPos, rawEndPos, chessDesk)) {
			Position pos = new Position();
			int[] startPos = pos.convertPosition(rawStartPos);
			int[] endPos = pos.convertPosition(rawEndPos);
			if (Math.abs(endPos[0] - startPos[0]) == Math.abs(endPos[1] - startPos[1]))
				return true;
		}
		return false;
	}

	/**
	 * Проверяем что на пути у нашего слона нету преград делением с остатком на 7
	 * проверяется приадлежность хода к левой диагонали а делением с остатком на 9
	 * проверяется приадлежность хода к правой диагонали
	 */
	private boolean isThereObstacle(int rawStartPos, int rawEndPos, byte[] chessDesk) {
		if (rawEndPos > rawStartPos) {
			if ((rawEndPos - rawStartPos) % leftShift == 0) {
				for (int i = rawStartPos + leftShift; i < rawEndPos; i += leftShift)
					if (chessDesk[i] != 0)
						return false;
			} else {
				for (int i = rawStartPos + rightShift; i < rawEndPos; i += rightShift)
					if (chessDesk[i] != 0)
						return false;
			}
		} 
		else {
			if ((rawStartPos - rawEndPos) % 7 == 0) {
				for (int i = rawStartPos - leftShift; i > rawEndPos; i -= leftShift)
					if (chessDesk[i] != 0)
						return false;
			} 
			else{
				for (int i = rawEndPos - rightShift; i < rawStartPos; i -= rightShift)
					if (chessDesk[i] != 0)
						return false;
			}
		}
		return true;
	}
}