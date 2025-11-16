package org.example.inputHandlers;

import org.example.GameTranslator;
import org.example.MovePartsConverter;
import org.example.auxiliary.MoveResults;
import org.example.MoveHandler;
import org.example.chess.GameHandler;
import org.example.states.GameState;
import org.example.states.MoveState;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Обработчик ввода в игре
 */
public class InGameInputHandler {	
	/**
	 * Обработчик частей хода
	 */
	private final MoveHandler moveHandler = new MoveHandler();
	/**
	 * Конвертер частей хода
	 */
	private final MovePartsConverter movePartsConverter = new MovePartsConverter();
	
	/**
	 * Игровой переводчик
	 */
	private final GameTranslator gameTranslator = new GameTranslator();
	
	/**
	 * Сообщщение о неизвестном формате ввода
	 */
	private final static String UNKNOWN_INPUT = "Неизвестный формат ввода хода";
	
	/**
	 * Пригласительное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Сообщение о ходе оппонента
	 */
	private final static String NOT_YOUR_MOVE = "Сейчас ходит противник.";
	
	/**
     * Скомпилированное регулярное выражение, соответствующее полностью
     * введённому ходу в текстовом виде
     */
    private final static Pattern NOTATION_PATTERN =
            Pattern.compile("^([prbnqkPRBNQK]??)([a-hA-H][1-8])([a-hA-H][1-8])$");
    /**
     * Скомпилированное регулярное выражение, соответствующее части хода
     * в виде callback запроса
     */
    private final static Pattern CALLBACK_PATTERN =
            Pattern.compile("^__((?:[prbnqkPRBNQK])|(?:[a-hA-H][1-8]))__$");
    
    /**
     * Получить начальную доску
     */
    public List<List<String>> getStartingBoard(
    		GameState startingGameState, boolean firstIsWhite) {
    	List<String> messagesFirst = null;
    	List<String> messagesSecond = null;
    	if (firstIsWhite) {
    		messagesFirst = List.of(
    				createBoardString(
    						startingGameState.getBoard(),
    						GameHandler.moveProperty.REGULAR,
    						true), 
    				YOUR_MOVE);

    		messagesSecond = List.of(
    				createBoardString(
    						startingGameState.getBoard(),
    						GameHandler.moveProperty.REGULAR,
    						false), 
    				NOT_YOUR_MOVE);
    	} else {
    		messagesFirst = List.of(
    				createBoardString(
    						startingGameState.getBoard(),
    						GameHandler.moveProperty.REGULAR,
    						false), 
    				YOUR_MOVE);

    		messagesSecond = List.of(
    				createBoardString(
    						startingGameState.getBoard(),
    						GameHandler.moveProperty.REGULAR,
    						true), 
    				NOT_YOUR_MOVE);    		
    	}
    	return List.of(messagesFirst, messagesSecond);
    }
    
    /**
     * Обработать ввод в режиме многопользовательской игры
     */
    public MoveResults processInputMultiGame(String userInput, boolean movingIsWhite,
    		MoveState currentMoveState, GameState currentGameState) {
    	Matcher notationMatch = NOTATION_PATTERN.matcher(userInput);
    	Matcher callbackMatch = CALLBACK_PATTERN.matcher(userInput);
    	List<String> responseTextsFirst = new ArrayList<String>();
    	List<String> responseTextsSecond = new ArrayList<String>();
    	MoveResults.moveStatus thisMoveStatus = MoveResults.moveStatus.FAILURE;
    	if (callbackMatch.find()) {
    		String movePart = callbackMatch.group(1).toLowerCase();
    		if (!movePart.isEmpty()) {
    			currentMoveState.nextStatus(movePart);
    		}
    		responseTextsFirst.add(createMoveString(currentMoveState));
    	}
    	if (notationMatch.find() || currentMoveState.isMoveReady()) {
        	GameHandler.moveProperty move = GameHandler.moveProperty.REGULAR;
    		if (currentMoveState.isMoveReady()) {
    			move = moveHandler.processMove(
    					currentMoveState.getFigure(), 
    					currentMoveState.getStartPosition(), 
    					currentMoveState.getFinishPosition(), 
    					currentGameState);
    		} else {
    			move = moveHandler.processMove(
    					notationMatch.group(1).toLowerCase(), 
    					notationMatch.group(2).toLowerCase(), 
    					notationMatch.group(3).toLowerCase(), 
    					currentGameState);
    		}
    		currentMoveState.clearMoveState();
    		if (move.equals(GameHandler.moveProperty.CHECK)) {
    			responseTextsFirst.add(createBoardString(
    				currentGameState.getBoard(), 
    				GameHandler.moveProperty.REGULAR, 
    				movingIsWhite));
    		} else {
    			responseTextsFirst.add(createBoardString(
    				currentGameState.getBoard(), 
    				move, 
    				movingIsWhite));
    		}
    		thisMoveStatus = switch (move) {
    			case GameHandler.moveProperty.IMPOSSIBLE, 
    			GameHandler.moveProperty.INVALID -> 
    				MoveResults.moveStatus.FAILURE;
    			case GameHandler.moveProperty.REGULAR, 
    			GameHandler.moveProperty.CHECK -> 
					MoveResults.moveStatus.SUCCESS;
    			case GameHandler.moveProperty.MATE -> 
    				MoveResults.moveStatus.GAMEOVER;
    		};
    		if (thisMoveStatus.equals(MoveResults.moveStatus.FAILURE)) {
            	responseTextsFirst.add(YOUR_MOVE);
            } else {
            	responseTextsSecond.add(createBoardString(
            			currentGameState.getBoard(), move, !movingIsWhite));
            	if (thisMoveStatus.equals(MoveResults.moveStatus.SUCCESS)) {
            		responseTextsFirst.add(NOT_YOUR_MOVE);
            		responseTextsSecond.add(YOUR_MOVE);
            	}
            }
    	}    	
    	if (responseTextsFirst.isEmpty()) {
    		responseTextsFirst.add(UNKNOWN_INPUT);
        	responseTextsFirst.add(YOUR_MOVE);
    	}
    	return new MoveResults(thisMoveStatus, 
    			List.of(responseTextsFirst, responseTextsSecond));
    }
    
    /**
     * Обработать ввод в режиме многопользовательской игры 
     */
    public MoveResults processInputSingleGame(String userInput, 
    		MoveState currentMoveState, GameState currentGameState) {
    	Matcher notationMatch = NOTATION_PATTERN.matcher(userInput);
    	Matcher callbackMatch = CALLBACK_PATTERN.matcher(userInput);
    	List<String> responseTexts = new ArrayList<String>();
    	MoveResults.moveStatus thisMoveStatus = MoveResults.moveStatus.FAILURE;
    	if (callbackMatch.find()) {
    		String movePart = callbackMatch.group(1).toLowerCase();
    		if (!movePart.isEmpty()) {
    			currentMoveState.nextStatus(movePart);
    		}
    		responseTexts.add(createMoveString(currentMoveState));
    	}
    	if (notationMatch.find() || currentMoveState.isMoveReady()) {
        	GameHandler.moveProperty move = GameHandler.moveProperty.REGULAR;
    		if (currentMoveState.isMoveReady()) {
    			move = moveHandler.processMove(
    					currentMoveState.getFigure(), 
    					currentMoveState.getStartPosition(), 
    					currentMoveState.getFinishPosition(), 
    					currentGameState);
    		} else {
    			move = moveHandler.processMove(
    					notationMatch.group(1).toLowerCase(), 
    					notationMatch.group(2).toLowerCase(), 
    					notationMatch.group(3).toLowerCase(), 
    					currentGameState);
    		}
    		currentMoveState.clearMoveState();
    		responseTexts.add(createBoardString(
    				currentGameState.getBoard(), move, currentGameState.isWhiteToMove()));
    		thisMoveStatus = switch (move) {
    			case GameHandler.moveProperty.IMPOSSIBLE, 
    			GameHandler.moveProperty.INVALID -> 
    				MoveResults.moveStatus.FAILURE;
    			case GameHandler.moveProperty.REGULAR, 
    			GameHandler.moveProperty.CHECK -> 
					MoveResults.moveStatus.SUCCESS;
    			case GameHandler.moveProperty.MATE -> 
    				MoveResults.moveStatus.GAMEOVER;
    		};
    		if (!move.equals(GameHandler.moveProperty.MATE)) {
            	responseTexts.add(YOUR_MOVE);
            }
    	}    	
    	if (responseTexts.isEmpty()) {
    		responseTexts.add(UNKNOWN_INPUT);
    	}
    	return new MoveResults(thisMoveStatus, List.of(responseTexts));
    }
    
    /**
     * Сформировать строку с состоянием готовности хода
     */
    private String createMoveString(MoveState moveState) {
    	String currentFigure = moveState.getFigure();
		String currentStartPosition = moveState.getStartPosition();
		String currentFinishPosition = moveState.getFinishPosition();
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
		return moveMessage;
    }
    
    /**
     * Сформировать строку с состоянием доски
     */
    private String createBoardString(byte[][] chessboard, 
    		GameHandler.moveProperty moveProperty, boolean whiteSide) {
    	return gameTranslator.chessboardString(moveProperty, chessboard, whiteSide);
    }
}
