package org.example;

public class Queen implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(int startPos, int endPos) {
		if((Math.abs(startPos.x - endPos.x)==Math.abs(startPos.y - endPos.y))||(startPos.x == endPos.y)||(startPos.y == endPos.y))
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