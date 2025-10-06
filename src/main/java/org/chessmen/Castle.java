package org.chessmen;
public class Castle implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(Position startPos, Position endPos) {
		if((startPos.x == endPos.y)||(startPos.y == endPos.y))
			return true;
		return false;
	}  
}
