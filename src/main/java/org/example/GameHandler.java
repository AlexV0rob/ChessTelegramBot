package org.example;
/**
 * Класс для произведения хода у конкретного пользователя
 */
public class GameHandler{
	public byte ProgressHandler(User user, int rawStartPos, int rawEndPos, byte figureCode){
		Chessmen lastChessmen = null;
		boolean isNormalMove = false;
		if((rawStartPos > 63 || rawStartPos < 0 ) || (rawEndPos > 63 || rawEndPos < 0))
			return 0;
		byte[] curDesk = user.getBoard();
		switch((figureCode + 1) / 2)
		{
		case 1:
			Chessmen pawn = new Pawn();
			isNormalMove = pawn.checkMove(rawStartPos, rawEndPos,curDesk, user.doesWhitesMove());
			lastChessmen = pawn;
			break;
		case 2:
			Chessmen castle = new Castle();
			isNormalMove = castle.checkMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			lastChessmen = castle;
			break;


		case 3:
			Chessmen knight = new Knight();
			isNormalMove = knight.checkMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			lastChessmen = knight;
			break;

		case 4:
			Chessmen bishop = new Bishop();
			isNormalMove = bishop.checkMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			lastChessmen = bishop;
			break;
		case 5:
			Chessmen queen = new Queen();
			isNormalMove = queen.checkMove(rawStartPos, rawEndPos,curDesk, user.doesWhitesMove());
			lastChessmen = queen;
			break;

		case 6:
			Chessmen king = new King();
			isNormalMove = king.checkMove(rawStartPos, rawEndPos, curDesk, user.doesWhitesMove());
			lastChessmen = king;
			break;
		}
		if(isNormalMove)
		{
			if(IsThisMoveOnKing(curDesk, rawEndPos,user.doesWhitesMove())){
				return 3;
			}
			byte tmp = curDesk[rawStartPos] ;
			curDesk[rawStartPos] = 0;
			curDesk[rawEndPos] = tmp;
			if(check(curDesk, user.doesWhitesMove(), rawEndPos,lastChessmen)) {
				return 2;
			}

			return 1;
		}
		else
			return 0;
	}
	/**
	 * Проверка на шах
	 */
	public boolean check(byte[]curBoard, boolean isWhiteMove, int rawStartPos,Chessmen chessmen)
	{
		int i = 0 ;
		if(isWhiteMove){
			for(; i < 64; ++i)
				if(curBoard[i] == 11)
					break;
			if(chessmen.checkMove(rawStartPos, i, curBoard, true))
				return true;
		}
		else{
			for(; i < 64; ++i)
				if(curBoard[i] == 12)
					break;
			if(chessmen.checkMove(rawStartPos, i, curBoard, false))
				return true;	
		}
		return false;
	}
	/**
	 * Проверка на рубку короля
	 */
	public boolean 	IsThisMoveOnKing(byte[]curBoard,int rawPosition, boolean isWhiteMove){
		if(isWhiteMove)
			return curBoard[rawPosition] == 11;
		return curBoard[rawPosition] == 12;
	}	
	
}

