package org.example;

public interface Chessmen {
	boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack);
	default void Move(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack)
	{

		if(CheckMove(rawStartPos, rawEndPos ,chessDesk,isBlack))
		{
			byte tmp = chessDesk[rawStartPos] ;
			chessDesk[rawStartPos] = 0;
			chessDesk[rawEndPos] = tmp;

		}
		else
		{
			//TO DO
		}
	}
}