package org.example;

/**
 * Класс для произведения хода у конкретного пользователя
 */
public class GameHandler {
	Chessmen[] figureList;
	/**
	 * констанста, соотвествующая длинне массива доски
	 */
	final static int deskLength = 64;
	/**
	 * констанста, соотвествующая длинне массива доски
	 */
	final static int lastIndexInDesk = 63;
	/**
	 * констанста, соотвествующая числовому представлению белого короля
	 */
	final static int whiteKing = 12;
	/**
	 * констанста, соотвествующая числовому представлению чёрного короля
	 */
	final static int blackKing = -12;

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
		if ((rawStartPos > lastIndexInDesk || rawStartPos < 0) || (rawEndPos > lastIndexInDesk || rawEndPos < 0))
			return 0;
		isNormalMove = figureList[Math.abs(figureCode) - 1].checkMove(rawStartPos, rawEndPos, curDesk, rawStartPos < 0);
		lastChessmen = figureList[Math.abs(figureCode) - 1];
		if (isNormalMove) {
			if (isThisMoveOnKing(curDesk, rawEndPos, rawStartPos < 0)) {
				return 3;
			}
			byte tmp = curDesk[rawStartPos];
			curDesk[rawStartPos] = 0;
			curDesk[rawEndPos] = tmp;
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
			while (i < deskLength) {
				if (curBoard[i] == blackKing)
					break;
				i++;
			}
			if (chessmen.checkMove(rawStartPos, i, curBoard, isWhiteMove))
				return true;
		} else {
			while (i < deskLength) {
				if (curBoard[i] == whiteKing)
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
