package org.example;

public class Knight implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(int startPos, int endPos) {
		if(Math.sqrt((startPos.x - endPos.x)*(startPos.x - endPos.x) + (startPos.y - endPos.y)*(startPos.y - endPos.y))==5)
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