package org.example;

public class Bishop implements  Chessmen{
	protected int currentPosition;
	private boolean checkMove(Position startPos, Position endPos) {
		 if(Math.abs(startPos.x-endPos.x)==Math.abs(startPos.y-endPos.y))
			return true;
		return false;
	} 
	public void Move(Position startPos, Position endPos,byte[][] chessDesk)
	{
		if(checkMove(startPos, endPos,chessDesk))
		{
			byte tmp = chessDesk[startPos.x][startPos.y] ;
			chessDesk[startPos.x][startPos.y] = 0b01;
			chessDesk[endPos.x][endPos.y] = tmp;

		}
		else
		{
		}
	}
}