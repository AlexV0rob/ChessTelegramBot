package org.example;
/**
 * Интерфейс определяющий шахматные фигуры
 */
public interface Chessmen {
	boolean  CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isWhite);	
}