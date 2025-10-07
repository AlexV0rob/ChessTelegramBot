package org.example;

public class Knight implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(Position startPos, Position endPos) {
		if(Math.sqrt((startPos.x - endPos.x)*(startPos.x - endPos.x) + (startPos.y - endPos.y)*(startPos.y - endPos.y))==5)
			return true; 
		return false;
	}  
}