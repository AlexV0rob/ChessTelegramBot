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
	public int[] everyRightMove(int rawStartPos, byte[] chessDesk, boolean isWhite) {
		int[] rightMoves = new int[28];
		int curPos = 0;
		for (int i = 0; i < 28; ++i)
			rightMoves[i] = -1;
		Chessmen rook = new Rook();
		Chessmen bishop = new Bishop();
		int[] rookMoves = rook.everyRightMove(rawStartPos, chessDesk, isWhite);
		int[] bishopMoves = bishop.everyRightMove(rawStartPos, chessDesk, isWhite);
		for(int i = 0; i < 28; ++i) {
			if(rookMoves[i] == -1)
				break;
			rightMoves[curPos++] = rookMoves[i];
		}
		for(int i = 0; i < 28; ++i) {
			if(bishopMoves[i] == -1)
				break;
			rightMoves[curPos++] = bishopMoves[i];
		}
		 return rightMoves;
	}
}