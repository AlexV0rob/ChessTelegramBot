package org.example;
/**
 * Класс для реализации логики перемещения Королевы
 */
public class Queen implements Chessmen {
	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite,
			PositionConverter positionConverter) {
		Chessmen rook = new Rook();
		Chessmen bishop = new Bishop();
		boolean result = rook.checkMove(rawStartPos, rawEndPos, chessDesk, isWhite, positionConverter) 
				|| bishop.checkMove(rawStartPos, rawEndPos, chessDesk, isWhite, positionConverter);
		return result;
	}
	@Override
	public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk, boolean isWhite,
			PositionConverter positionConverter) {
        List<Integer> possibleMoves = new ArrayList<>();
		Chessmen rook = new Rook();
		Chessmen bishop = new Bishop();
		possibleMoves = rook.everyPossibleMove(rawStartPos, chessDesk, isWhite, positionConverter);
		possibleMoves.addAll(bishop.everyPossibleMove(rawStartPos, chessDesk, isWhite, positionConverter));
		
		return possibleMoves;
	}
}