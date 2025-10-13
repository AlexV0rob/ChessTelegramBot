package org.example;

import org.telegram.telegrambots.meta.api.objects.Update;
/**
 * Класс для произведения хода у конкретного пользователя
 */
public class GameHandler{
	public boolean ProgressHandler(Player player, int rawStartPos, int rawEndPos, byte figureCode,boolean isWhite)
	{
		boolean isNormalMove;
		switch(figureCode)
		{
		case 1:
			Chessmen pawn = new Pawn();
			isNormalMove = pawn.CheckMove(rawStartPos, rawEndPos, player.chessDesk, isWhite);
			break;
		case 2:
			Chessmen castle = new Castle();
			isNormalMove = castle.CheckMove(rawStartPos, rawEndPos, player.chessDesk, isWhite);
			break;
		case 3:
			Chessmen bishop = new Bishop();
			isNormalMove = bishop.CheckMove(rawStartPos, rawEndPos, player.chessDesk, isWhite);
			break;
		case 4:
			Chessmen knight = new Knight();
			isNormalMove = knight.CheckMove(rawStartPos, rawEndPos, player.chessDesk, isWhite);
			break;
		case 5:
			Chessmen queen = new Queen();
			isNormalMove = queen.CheckMove(rawStartPos, rawEndPos, player.chessDesk, isWhite);
			break;

		case 6:
			Chessmen king = new King();
			isNormalMove = king.CheckMove(rawStartPos, rawEndPos, player.chessDesk, isWhite);
			break;
		}
		if(isNormalMove)
		{
			byte tmp = player.chessDesk[rawStartPos] ;
			player.chessDesk[rawStartPos] = 0;
			player.chessDesk[rawEndPos] = tmp;
			return true;
		}
		else
			return false;
	}
	
}

