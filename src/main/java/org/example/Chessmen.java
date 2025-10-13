package org.example;
/**
 * Интерфейс определяющий шахматные фигуры
 */
public interface Chessmen {
	boolean  checkMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite);	
}