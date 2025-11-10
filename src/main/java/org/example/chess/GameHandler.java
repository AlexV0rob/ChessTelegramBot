package org.example.chess;

import org.example.states.GameState;

public class GameHandler {
	public enum moveProperty {
		INVALID,
		IMPOSSIBLE,
		REGULAR,
		CHECK,
		MATE
	}
	
	private final static int MIN_SIDE_VALUE = 0;
	private final static int MAX_SIDE_VALUE = 7;
	private final static int FIGURES_COUNT = 6;
	
	private final static Chessmen[] FIGURES = {
			new Pawn(),
			new Rook(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
			new Knight(),
			new Bishop(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
			new Queen(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
			new King()
	};
	
	public moveProperty processMove(int figureCode, PositionOnBoard start, 
			PositionOnBoard finish, GameState currentGameState) {
		if ((start.row() < MIN_SIDE_VALUE || start.row() > MAX_SIDE_VALUE) ||
				(start.column() < MIN_SIDE_VALUE || start.column() > MAX_SIDE_VALUE) || 
				(finish.row() < MIN_SIDE_VALUE || finish.row() > MAX_SIDE_VALUE) ||
				(finish.column() < MIN_SIDE_VALUE || finish.column() > MAX_SIDE_VALUE) || 
				(figureCode < 0 || figureCode >= FIGURES_COUNT) || 
				(currentGameState.getBoard()[start.row()][start.column()] < 0 != 
					currentGameState.isWhiteToMove())) {
			return moveProperty.INVALID;
		}
		if (!FIGURES[figureCode].checkMove(start, finish, currentGameState.getBoard())) {
			return moveProperty.IMPOSSIBLE;
		}
		currentGameState.moveFigure(start, finish);
		if (isCheckMove()) {
			if (isMateMove()) {
				return moveProperty.MATE; 
			}
			return moveProperty.CHECK;
		}
		return moveProperty.REGULAR;
	}
	
	private boolean isCheckMove() {
		return false;
	}
	
	private boolean isMateMove() {
		return false;
	}
}
