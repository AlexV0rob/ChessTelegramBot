package org.chessmen;

public class Pawn implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(Position startPos, Position endPos) {
		if((startPos.x == endPos.x)&&(endPos.y - startPos.x == 1 ))
			return true;
		return false;
	}
}
