package org.example;

import org.example.chess.PositionOnBoard;

import org.example.states.GameState;
import org.example.states.MoveState;

import java.util.ArrayList;
import java.util.List;

/**
 * Обработчик хода и его частей
 */
public class MovePartsHandler {
	/**
	 * Конвертер частей хода
	 */
	private final MovePartsConverter movePartsConverter = new MovePartsConverter();
	
	/**
	 * Игровой переводчик
	 */
	private final GameTranslator gameTranslator = new GameTranslator();
	
	/**
	 * Пригластельное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Обработать ход целиком
	 */
    public List<String> processMove(String figure, String startPosition, String finishPosition, 
    		GameState currentGameState, MoveState currentMoveState) {
        int figureCode = movePartsConverter.getFigureCode(figure);
        int startPositionRow = movePartsConverter
        		.getPositionRowCode(startPosition.charAt(1));
        int startPositionColumn = movePartsConverter
        		.getPositionColumnCode(startPosition.charAt(0));
        int finishPositionRow = movePartsConverter
        		.getPositionRowCode(finishPosition.charAt(1));
        int finishPositionColumn = movePartsConverter
        		.getPositionColumnCode(finishPosition.charAt(0));
        currentMoveState.clearMoveState();
        return List.of(
        		gameTranslator.makeMove(
        				figureCode, 
        				new PositionOnBoard(startPositionRow, startPositionColumn), 
        				new PositionOnBoard(finishPositionRow, finishPositionColumn),
        				currentGameState), 
        		YOUR_MOVE); 
    }
    
    /**
     * Обработать часть хода
     */
	public List<String> processMovePart(String movePart, 
			MoveState currentMoveState, GameState currentGameState) {
		List<String> responses = new ArrayList<String>();
		if (!movePart.isEmpty()) {
			currentMoveState.nextStatus(movePart);
		}
		String currentFigure = currentMoveState.getFigure();
		String currentStartPosition = currentMoveState.getStartPosition();
		String currentFinishPosition = currentMoveState.getFinishPosition();
		String moveMessage = YOUR_MOVE;
		if (!currentFigure.isEmpty()) {
			moveMessage += movePartsConverter.getFigureName(currentFigure);
		}
		if (!currentStartPosition.isEmpty()) {
			moveMessage += " " + currentStartPosition.toUpperCase();
		}
		if (!currentFinishPosition.isEmpty()) {
			moveMessage += " " + currentFinishPosition.toUpperCase();
		}
		responses.add(moveMessage);
		if (currentMoveState.isMoveReady()) {
			responses.addAll(processMove(currentFigure, currentStartPosition, 
					currentFinishPosition, currentGameState, currentMoveState));
		}
		return responses;
	}
}
