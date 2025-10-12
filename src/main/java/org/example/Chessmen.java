package org.example;

public interface Chessmen {
	boolean  CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack);
	default String Move(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack){
	if(CheckMove(rawStartPos, rawEndPos ,chessDesk,isBlack))
	{
		byte tmp = chessDesk[rawStartPos] ;
		chessDesk[rawStartPos] = 0;
		chessDesk[rawEndPos] = tmp;
		return "Ход успешно совершён";

	}
		return "Ход не был совершён";
} 
}