package org.example;

/**
 * Класс для реализации логики перемещения Королевы
 */
public class Queen implements Chessmen {
	/**
	 * @param rawStartPos - стартовая позиция фигуры в одномерном массиве доски
	 * @param rawEndPos - предполагаемая конечная позиция фигуры в одномерном массиве доски
	 * @param chessDesk - одномерный массив с позициями всех фигур на шахматной доске
	 * @return можно ли сходить на предполагаемую конечную позицию 
	 */
	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite) {
		
		Chessmen rook = new Rook();
		Chessmen bishop = new Bishop();
		if ((rook.checkMove(rawStartPos, rawEndPos, chessDesk, isWhite)
				|| bishop.checkMove(rawStartPos, rawEndPos, chessDesk, isWhite))) {
				return true;
		}
		return false;
	}
}