package org.example;
/**
 * Класс для реализации логики перемещения пешки
 */
public class Pawn implements  Chessmen{
	protected Position currentPosition;
	public  boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite) {
		Position endPos= new Position(rawEndPos);
		Position startPos= new Position(rawStartPos);
		if(chessDesk[rawEndPos] == 0){

			if((Math.abs(endPos.line - startPos.line) == 1) && (startPos.column == endPos.column))
				return true;
			else if((startPos.line == 1 ||  startPos.line == 6)&&
					((Math.abs(endPos.line - startPos.line) == 2) && (startPos.column == endPos.column)))
				return true;
		}
		else if(((chessDesk[rawEndPos] % 2 == 0 ) != isWhite))
		{
			if((Math.abs(endPos.line - startPos.line) == 1) && (Math.abs(startPos.column -  endPos.column) == 1))
				return true;
		}
		return false;
	} 
}
