package org.example;


/**
 * Класс для реализации логики перемещения слона
 */
public class Bishop implements Chessmen {
	/**
	 * Сдвиг влево по горизонтали
	 */
	final static int LEFT_SHIFT = 7;
	/**
	 * Сдвиг вправо по горизонтали
	 */
	final static int RIGHT_SHIFT = 9;
	/**
	 * константа, соотвествующая длинне массива доски
	 */
	private final static int DESK_LENGTH = 64;
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
			if ((rawEndPos - rawStartPos) % LEFT_SHIFT == 0) {
				for (int i = rawStartPos + LEFT_SHIFT; i < rawEndPos; i += LEFT_SHIFT)
					if (chessDesk[i] != 0)
						return false;
			} else {
				for (int i = rawStartPos + RIGHT_SHIFT; i < rawEndPos; i += RIGHT_SHIFT)
					if (chessDesk[i] != 0)
						return false;
			}
		} 
		else {
			if ((rawStartPos - rawEndPos) % 7 == 0) {
				for (int i = rawStartPos - LEFT_SHIFT; i > rawEndPos; i -= LEFT_SHIFT)
					if (chessDesk[i] != 0)
						return false;
			} 
			else{
				for (int i = rawEndPos - RIGHT_SHIFT; i < rawStartPos; i -= RIGHT_SHIFT)
					if (chessDesk[i] != 0)
						return false;
			}
		}
		return true;
	}
	public int[] everyRightMove(int rawStartPos, byte[] chessDesk, boolean isWhite)
	{
		int[] rightMoves = new int[28];
		for(int i = 0; i < 28; ++i)
			rightMoves[i] = -1;
		//текущая позиция в массиве
		int curPosInArr = 0;
		for(int i = 7 ; i < LAST_INDEX_IN_DESK; i += LEFT_SHIFT)
		{
			if((chessDesk[rawStartPos + i] != 0) && ((chessDesk[rawStartPos + i] > 0) == isWhite))
					break;
			if (( rawStartPos - i > 0)&&(chessDesk[rawStartPos - i] != 0) && ((chessDesk[rawStartPos - i] > 0) == isWhite))
				break;
			if(rawStartPos + i < DESK_LENGTH)
				rightMoves[curPosInArr++] = rawStartPos + i ;
			if(rawStartPos - i < 0)
				rightMoves[curPosInArr++] = rawStartPos - i ;	
		}
		for(int i = 9 ; i < LAST_INDEX_IN_DESK; i += RIGHT_SHIFT)
		{
			if((chessDesk[rawStartPos + i] != 0) && ((chessDesk[rawStartPos + i] > 0) == isWhite))
					break;
			if (( rawStartPos - i > 0)&&(chessDesk[rawStartPos - i] != 0) && ((chessDesk[rawStartPos - i] > 0) == isWhite))
				break;
			if(rawStartPos + i < DESK_LENGTH)
				rightMoves[curPosInArr++] = rawStartPos + i ;
			if(rawStartPos - i < 0)
				rightMoves[curPosInArr++] = rawStartPos - i ;	
		}
		return rightMoves;
	}
}