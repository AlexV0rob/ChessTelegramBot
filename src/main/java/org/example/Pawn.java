package org.example;

public class Pawn implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(int startPos, int endPos) {
		if((startPos.x == endPos.x)&&(endPos.y - startPos.x == 1 ))
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
