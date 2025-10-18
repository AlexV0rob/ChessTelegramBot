package org.example;
/**
 * Интерфейс определяющий шахматные фигуры
 */
public interface Chessmen {
	/**
	 * Проверить ход на правльность
	 */
	boolean  checkMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite);	
}