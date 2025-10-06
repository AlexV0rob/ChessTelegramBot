package org.chessmen;
public class Queen implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(Position startPos, Position endPos) {
		if((Math.abs(startPos.x - endPos.x)==Math.abs(startPos.y - endPos.y))||(startPos.x == endPos.y)||(startPos.y == endPos.y))
			return true;
		return false;
	}  
}