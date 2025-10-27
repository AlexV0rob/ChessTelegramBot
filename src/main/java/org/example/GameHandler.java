package org.example;

/**
 * Класс для произведения хода у конкретного пользователя
 */
public class GameHandler {
	Chessmen[] figureList;
	/**
	 * констанста, соотвествующая длинне массива доски
	 */
	final static int DESK_LENGTH = 64;
	/**
	 * констанста, соотвествующая длинне массива доски
	 */
	final static int LAST_INDEX_IN_DESK = 63;
	/**
	 * констанста, соотвествующая числовому представлению белого короля
	 */
	final static int WHITE_KING = 12;
	/**
	 * констанста, соотвествующая числовому представлению чёрного короля
	 */
	final static int BLACK_KING = -12;

	public GameHandler() {
		figureList = new Chessmen[6];
		figureList[0] = new Pawn();
		figureList[1] = new Rook();
		figureList[2] = new Knight();
		figureList[3] = new Bishop();
		figureList[4] = new Queen();
		figureList[5] = new King();
	}

	/**
	 * метод, изменяющий состояние доски
	 * @return возвращает состояние хода 0 - ход не сделан, 1 - ход сделан; 2 - ход
	 * сделан и он ставит шах королю; 3 - король срублен, игра окончена
	 */
	public int progressHandler(byte[] curDesk, int rawStartPos, int rawEndPos, byte figureCode) {
		Chessmen lastChessmen = null;
		boolean isNormalMove = false;
		if ((rawStartPos > LAST_INDEX_IN_DESK || rawStartPos < 0) || (rawEndPos > LAST_INDEX_IN_DESK || rawEndPos < 0))
			return 0;

		isNormalMove = figureList[Math.abs(figureCode) - 1].checkMove(rawStartPos, rawEndPos, curDesk, rawStartPos < 0);
		lastChessmen = figureList[Math.abs(figureCode) - 1];
		if (isNormalMove) {
			if (isThisMoveOnKing(curDesk, rawEndPos, rawStartPos < 0)) {
				return 3;
			}
			if (check(curDesk, rawStartPos < 0, rawEndPos, lastChessmen)) {
				return 2;
			}

			return 1;
		} else
			return 0;
	}

	/**
	 * Проверка на шах
	 */
	public boolean check(byte[] curBoard, boolean isWhiteMove, int rawStartPos, Chessmen chessmen) {
		int i = 0;
		if (isWhiteMove) {
			while (i < DESK_LENGTH) {
				if (curBoard[i] == BLACK_KING)
					break;
				i++;
			}
			if (chessmen.checkMove(rawStartPos, i, curBoard, isWhiteMove))
				return true;
		} else {
			while (i < DESK_LENGTH) {
				if (curBoard[i] == WHITE_KING)
					break;
				i++;
			}
			if (chessmen.checkMove(rawStartPos, i, curBoard, isWhiteMove))
				return true;
		}
		return false;
	}

	/**
	 * Проверка на рубку короля
	 */
	public boolean isThisMoveOnKing(byte[] curBoard, int rawPosition, boolean isWhiteMove) {
		if (isWhiteMove)
			return curBoard[rawPosition] == -12;
		return curBoard[rawPosition] == 12;
	}
}
