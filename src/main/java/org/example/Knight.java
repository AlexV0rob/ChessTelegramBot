package org.example;
/**
 * Класс для реализации логики перемещения коня
 */
class Knight implements  Chessmen{
	public boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack) {
		
		if((chessDesk[rawEndPos] == 0 ) || (chessDesk[rawEndPos] % 2 != isBlack))
		{
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			if(Math.sqrt((startPos.line - endPos.line)*(startPos.line - endPos.line) 
					+ (startPos.column - endPos.column)*(startPos.column - endPos.column))==5)
				return true;
		}
		return false;
	}
}