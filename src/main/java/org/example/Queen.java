package org.example;
/**
 * Класс для реализации логики перемещения Королевы
 */
public class Queen implements  Chessmen{
	public boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite) {
		Chessmen castle = new Castle();
		Chessmen bishop = new Bishop();
		if((chessDesk[rawEndPos] == 0 ) || ((chessDesk[rawEndPos] % 2 != 0 ) != isWhite)
				&& castle.CheckMove(rawStartPos,rawEndPos,chessDesk, isWhite) && bishop.CheckMove(rawStartPos,rawEndPos,chessDesk,isWhite)){
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			if((Math.abs(startPos.line - endPos.line) == Math.abs(startPos.column - endPos.column))
					||(startPos.line == endPos.line)||(startPos.column == endPos.column))
				return true;
		}
		return false;
	}
}