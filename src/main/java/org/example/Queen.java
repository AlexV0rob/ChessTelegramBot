package org.example;

public class Queen implements  Chessmen{
	protected Position currentPosition;
	public boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack) {
		if((chessDesk[rawEndPos] == 0 ) || (chessDesk[rawEndPos] % 2 != isBlack)){
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			if((Math.abs(startPos.line - endPos.line)==Math.abs(startPos.column - endPos.column))
					||(startPos.line == endPos.line)||(startPos.column == endPos.column))
				return true;
		}
		return false;
	}
}