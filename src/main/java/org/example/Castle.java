package org.example;

class Castle implements  Chessmen{
	public boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack) {
		if((chessDesk[rawEndPos] == 0 ) || (chessDesk[rawEndPos] % 2 != isBlack)){
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			if((startPos.line == endPos.line)||(startPos.column == endPos.column))
				return true;
		}
		return false;
	}
}
