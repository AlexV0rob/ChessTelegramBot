package org.example;

import org.example.chess.GameHandler;
import org.example.chess.PositionOnBoard;

import org.example.states.GameState;

/**
 * Обработчик хода и его частей
 */
public class MoveHandler {
	/**
	 * Конвертер частей хода
	 */
	private final MovePartsConverter movePartsConverter = new MovePartsConverter();
    
	/**
     * Обработчик игры
     */
    private GameHandler gameHandler = new GameHandler();
	
	/**
	 * Обработать ход целиком
	 */
    public GameHandler.moveProperty processMove(
    		String figure, String startPosition, String finishPosition, 
    		GameState currentGameState) {
        int figureCode = movePartsConverter.getFigureCode(figure);
        int startPositionRow = movePartsConverter
        		.getPositionRowCode(startPosition.charAt(1));
        int startPositionColumn = movePartsConverter
        		.getPositionColumnCode(startPosition.charAt(0));
        int finishPositionRow = movePartsConverter
        		.getPositionRowCode(finishPosition.charAt(1));
        int finishPositionColumn = movePartsConverter
        		.getPositionColumnCode(finishPosition.charAt(0));
        return gameHandler.processMove(
        		figureCode - 1, 
        		new PositionOnBoard(startPositionRow, startPositionColumn), 
        		new PositionOnBoard(finishPositionRow, finishPositionColumn), 
        		currentGameState); 
    }
}
