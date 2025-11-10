package org.example.inputHandlers;

import org.example.GameTranslator;
import org.example.MovePartsConverter;
import org.example.MoveHandler;
import org.example.chess.GameHandler;
import org.example.states.MoveState;
import org.example.states.UserState;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Обработчик ввода в игре
 */
public class InGameInputHandler implements InputHandler {
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
	 * Пригластельное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
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
    
    @Override
    public List<String> processInput(String userInput, UserState currentUserState) {
    	Matcher notationMatch = NOTATION_PATTERN.matcher(userInput);
    	Matcher callbackMatch = CALLBACK_PATTERN.matcher(userInput);
    	List<String> responses = new ArrayList<String>();
		MoveState currentMoveState = currentUserState.getMoveState();
    	if (callbackMatch.find()) {
    		String movePart = callbackMatch.group(1).toLowerCase();
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
    	}
    	if (notationMatch.find() || currentMoveState.isMoveReady()) {
        	GameHandler.moveProperty move = GameHandler.moveProperty.REGULAR;
    		if (currentMoveState.isMoveReady()) {
    			move = moveHandler.processMove(
    					currentMoveState.getFigure(), 
    					currentMoveState.getStartPosition(), 
    					currentMoveState.getFinishPosition(), 
    					currentUserState.getGameState());
    		} else {
    			move = moveHandler.processMove(
    					notationMatch.group(1).toLowerCase(), 
    					notationMatch.group(2).toLowerCase(), 
    					notationMatch.group(3).toLowerCase(), 
    					currentUserState.getGameState());
    		}
    		currentMoveState.clearMoveState();
    		responses.add(gameTranslator.chessboardString(
    				move, 
    				currentUserState.getGameState().getBoard(), 
    				currentUserState.getGameState().isWhiteToMove()));
    		if (move.equals(GameHandler.moveProperty.MATE)) {
    			currentUserState.setUserState(UserState.USER_STATE.MAINMENU);
            } else {
            	responses.add(YOUR_MOVE);
            }
    	}
    	if (responses.isEmpty()) {
    		return List.of(UNKNOWN_INPUT);
    	} else {
    		return responses;
    	}
    }
}
