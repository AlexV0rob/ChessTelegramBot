package org.example;
/**
 * Класс для реализации логики перемещения коня
 */
class Knight implements  Chessmen{
	public boolean checkMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite) {
		
		if((chessDesk[rawEndPos] == 0 ) || ((chessDesk[rawEndPos] % 2 != 0 ) != isWhite))
		{
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			if (((startPos.line - 2 == endPos.line) && (startPos.column + 1 == endPos.column)))
					return true;
			else if(((startPos.line - 1 == endPos.line) && (startPos.column + 2 == endPos.column)))
					return true;
			else if(((startPos.line + 1 == endPos.line) && (startPos.column + 2 == endPos.column)))
				return true;
			else if(((startPos.line + 2 == endPos.line) && (startPos.column - 1 == endPos.column)))
				return true;
			else if(((startPos.line + 2 == endPos.line) && (startPos.column - 1 == endPos.column)))
				return true;
			else if(((startPos.line + 1 == endPos.line) && (startPos.column - 2 == endPos.column)))
				return true;
			else if(((startPos.line - 1 == endPos.line) && (startPos.column - 2 == endPos.column)))
				return true;
			else if(((startPos.line - 2 == endPos.line) && (startPos.column - 1 == endPos.column)))
				return true;
		}
		return false;
	}
}