package org.example;

public class Pawn implements  Chessmen{
	protected Position currentPosition;
	public  boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack) {
		Position endPos= new Position(rawEndPos);
		Position startPos= new Position(rawStartPos);
		if(chessDesk[rawEndPos] == 0){

			if((endPos.line - startPos.line == 1) && (startPos.column == endPos.column))
				return true;
		}
		else if(chessDesk[rawEndPos] % 2 != isBlack)
		{
			if((endPos.line - startPos.line == 1) && (Math.abs(startPos.column -  endPos.column)== 0))
				return true;
		}
		return false;
	} 
}
