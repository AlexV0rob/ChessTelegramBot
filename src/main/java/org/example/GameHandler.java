package org.example;
/**
 * Класс для произведения хода у конкретного пользователя
 */
public class GameHandler{
	public boolean ProgressHandler(User user, int rawStartPos, int rawEndPos, byte figureCode)
	{
		boolean isNormalMove = false;
		if((rawStartPos > 63 || rawStartPos < 0 )&&(rawEndPos > 63 || rawEndPos < 0))
			return false;
		byte[] curDesk = user.getDesk();
		switch((figureCode + 1) % 2)
		{
		case 1:
			Chessmen pawn = new Pawn();
			isNormalMove = pawn.CheckMove(rawStartPos, rawEndPos,curDesk, user.doesWhitesMove());
			break;
		case 2:
			Chessmen castle = new Castle();
			isNormalMove = castle.CheckMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			break;
		case 3:
			Chessmen bishop = new Bishop();
			isNormalMove = bishop.CheckMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			break;
		case 4:
			Chessmen knight = new Knight();
			isNormalMove = knight.CheckMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			break;
		case 5:
			Chessmen queen = new Queen();
			isNormalMove = queen.CheckMove(rawStartPos, rawEndPos,curDesk, user.doesWhitesMove());
			break;

		case 6:
			Chessmen king = new King();
			isNormalMove = king.CheckMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
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

