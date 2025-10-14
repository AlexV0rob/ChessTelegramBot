package org.example;
/**
 * Класс для произведения хода у конкретного пользователя
 */
public class GameHandler{
	public boolean ProgressHandler(User user, int rawStartPos, int rawEndPos, byte figureCode)
	{
		boolean isNormalMove = false;
		if((rawStartPos > 63 || rawStartPos < 0 ) || (rawEndPos > 63 || rawEndPos < 0))
			return false;
		byte[] curDesk = user.getBoard();
		switch((figureCode + 1) / 2)
		{
		case 1:
			Chessmen pawn = new Pawn();
			isNormalMove = pawn.checkMove(rawStartPos, rawEndPos,curDesk, user.doesWhitesMove());
			break;
		case 2:
			Chessmen castle = new Castle();
			isNormalMove = castle.checkMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			break;


		case 3:
			Chessmen knight = new Knight();
			isNormalMove = knight.checkMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			break;

		case 4:
			Chessmen bishop = new Bishop();
			isNormalMove = bishop.checkMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			break;
		case 5:
			Chessmen queen = new Queen();
			isNormalMove = queen.checkMove(rawStartPos, rawEndPos,curDesk, user.doesWhitesMove());
			break;

		case 6:
			Chessmen king = new King();
			isNormalMove = king.checkMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			break;
		}
		if(isNormalMove)
		{
			byte tmp = curDesk[rawStartPos] ;
			curDesk[rawStartPos] = 0;
			curDesk[rawEndPos] = tmp;
			return true;
		}
		else
			return false;
	}
	
	
}

