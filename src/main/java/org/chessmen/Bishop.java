package org.chessmen;

public class Bishop implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(Position startPos, Position endPos) {
		 if(Math.abs(startPos.x-endPos.x)==Math.abs(startPos.y-endPos.y))
			return true;
		return false;
	}  
}