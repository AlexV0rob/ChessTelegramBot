package org.example;

/**
 * Класс для произведения хода у конкретного пользователя
 */
public class GameHandler {
	private Chessmen[] figureList;
	/**
	 * константа, соотвествующая длинне массива доски
	 */
	private final static int DESK_LENGTH = 64;
	/**
	 * константа, соотвествующая длинне массива доски
	 */
	private final static int LAST_INDEX_IN_DESK = 63;
	/**
	 * константа, соотвествующая числовому представлению белого короля
	 */
	private final static int WHITE_KING = -6;
	/**
	 * константа, соотвествующая числовому представлению чёрного короля
	 */
	private final static int BLACK_KING = 6;
	/**
	 * Число фигур
	 */
	private final static int CHESSMEN_COUNT = 6;

	public GameHandler() {
		figureList = new Chessmen[CHESSMEN_COUNT];
		figureList[0] = new Pawn();
		figureList[1] = new Rook();
		figureList[2] = new Knight();
		figureList[3] = new Bishop();
		figureList[4] = new Queen();
		figureList[5] = new King();
	}

	/**
	 * метод, изменяющий состояние доски
	 * @return характеристика хода
	 */
	public GameState.MOVE_PROPERTIES progressHandler(
			GameState currentGameState, int rawStartPos,
			int rawEndPos, byte figureCode) {
		Chessmen lastChessmen;
		boolean isNormalMove = false;
		if ((rawStartPos > LAST_INDEX_IN_DESK || rawStartPos < 0) ||
				(rawEndPos > LAST_INDEX_IN_DESK || rawEndPos < 0) ||
				(Math.abs(figureCode) > CHESSMEN_COUNT || figureCode == 0)) {
			return GameState.MOVE_PROPERTIES.IMPOSSIBLE;
		}
		
		byte[] curDesk = currentGameState.getBoard();

		lastChessmen = figureList[Math.abs(figureCode) - 1];
		
		isNormalMove = lastChessmen
				.checkMove(rawStartPos, rawEndPos, curDesk, rawStartPos < 0);
		if (isNormalMove) {
			if (isThisMoveOnKing(curDesk, rawEndPos, rawStartPos < 0)) {

				return GameState.MOVE_PROPERTIES.CHECKMATE;
			}
			currentGameState.moveFigure(rawStartPos, rawEndPos);
			currentGameState.changeMovingSide();
			if (isCheck(curDesk, rawStartPos < 0, rawEndPos, lastChessmen)) {
				return GameState.MOVE_PROPERTIES.CHECK;
			}

			return GameState.MOVE_PROPERTIES.REGULAR;
		} else
			return GameState.MOVE_PROPERTIES.IMPOSSIBLE;
	}

	/**
	 * Проверка на шах
	 */
	public boolean isCheck(
			byte[] curBoard, boolean isWhiteMove,
			int rawStartPos, Chessmen chessmen) {
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
			return curBoard[rawPosition] == BLACK_KING;
		return curBoard[rawPosition] == WHITE_KING;
	}
}
