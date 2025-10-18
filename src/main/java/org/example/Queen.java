package org.example;
/**
 * Класс для реализации логики перемещения Королевы
 */
public class Queen implements  Chessmen{
	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite) {
		Chessmen castle = new Castle();
		Chessmen bishop = new Bishop();
		if((castle.checkMove(rawStartPos, rawEndPos, chessDesk, isWhite) || bishop.checkMove(rawStartPos, rawEndPos, chessDesk, isWhite))){
			Position endPos = new Position(rawEndPos);
			Position startPos = new Position(rawStartPos);
			if((Math.abs(startPos.line - endPos.line) == Math.abs(startPos.column - endPos.column))
					||(startPos.line == endPos.line)||(startPos.column == endPos.column))
				return true;
		}
		return false;
	}
}