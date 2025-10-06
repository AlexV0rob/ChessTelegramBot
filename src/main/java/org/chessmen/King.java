package org.chessmen;
public class King implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(Position startPos, Position endPos) {
		if((Math.abs(startPos.x- endPos.x)<=1)&&(Math.abs(startPos.y- endPos.y)<=1))
			return true;
		return false;
	}  
}