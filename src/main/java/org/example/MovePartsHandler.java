package org.example;

import java.util.HashMap;
import java.util.Map;

public class MovePartsHandler extends MoveHandler {
	private final static Map<String, String> FIGURES_NAMES = 
			new HashMap<String, String>();
	
	public MovePartsHandler() {
		FIGURES_NAMES.put("p", "ПЕШКА");
		FIGURES_NAMES.put("r", "ЛАДЬЯ");
		FIGURES_NAMES.put("n", "КОНЬ");
		FIGURES_NAMES.put("b", "СЛОН");
		FIGURES_NAMES.put("q", "ФЕРЗЬ");
		FIGURES_NAMES.put("k", "КОРОЛЬ");
	}
	
	public String processMovePart(String movePart, 
			MoveState currentMoveState, GameState currentGameState) {
		if (movePart.isEmpty()) {
			movePart = "p";
		}
		currentMoveState.nextStatus(movePart);
		String currentFigure = currentMoveState.getFigure();
		String currentStartPosition = currentMoveState.getStartPosition();
		String currentFinishPosition = currentMoveState.getFinishPosition();
		if (currentMoveState.isMoveReady()) {
			currentMoveState.nextStatus("");
			return processMove(currentFigure, currentStartPosition, 
					currentFinishPosition, currentGameState);
		}
		String moveMessage = "";
		if (!currentFigure.isEmpty()) {
			moveMessage += FIGURES_NAMES.get(currentFigure);
		}
		if (!currentStartPosition.isEmpty()) {
			moveMessage += currentStartPosition.toUpperCase();
		}
		if (!currentFinishPosition.isEmpty()) {
			moveMessage += currentFinishPosition.toUpperCase();
		}
		return moveMessage;
	}
}
