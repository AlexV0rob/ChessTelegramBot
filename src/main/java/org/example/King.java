package org.example;

public class King implements  Chessmen{
	protected Position currentPosition;
	public boolean checkMove(int startPos, int endPos) {
		if((Math.abs(startPos.x- endPos.x)<=1)&&(Math.abs(startPos.y- endPos.y)<=1))
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