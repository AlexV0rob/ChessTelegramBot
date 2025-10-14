package org.example;
/**
 * Интерфейс определяющий шахматные фигуры
 */
public interface Chessmen {
	/**
	 * Проверить ход на правльность
	 * @param rawStartPos
	 * @param rawEndPos
	 * @param chessDesk
	 * @param isWhite
	 * @return валиден ход, или нет
	 */
	boolean  checkMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite);	
}