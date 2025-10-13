package org.example;
/**
 * Класс для реализации логики перемещения Короля
 */
class King implements  Chessmen{
	public boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite) {
		if((chessDesk[rawEndPos] == 0 ) || ((chessDesk[rawEndPos] % 2 != 0 ) != isWhite))
		{
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			if((Math.abs(startPos.line- endPos.line)<=1)&&(Math.abs(startPos.column- endPos.column)<=1))
				return true;
		}
		return false;
	}
}