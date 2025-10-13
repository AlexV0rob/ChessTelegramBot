package org.example;
/**
 * Класс для реализации логики перемещения Королевы
 */
public class Queen implements  Chessmen{
	public boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack) {
		Chessmen castle = new Castle();
		Chessmen bishop = new Bishop();
		if((chessDesk[rawEndPos] == 0 ) || (chessDesk[rawEndPos] % 2 != isBlack) 
				&& castle.CheckMove(rawStartPos,rawEndPos,chessDesk,isBlack) && bishop.CheckMove(rawStartPos,rawEndPos,chessDesk,isBlack)){
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			if((Math.abs(startPos.line - endPos.line) == Math.abs(startPos.column - endPos.column))
					||(startPos.line == endPos.line)||(startPos.column == endPos.column))
				return true;
		}
		return false;
	}
}