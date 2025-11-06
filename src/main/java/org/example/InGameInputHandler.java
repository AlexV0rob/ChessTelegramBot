package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InGameInputHandler implements InputHandler {
	private final MoveHandler moveHandler = new MoveHandler();
	private final MovePartsHandler movePartsHandler = new MovePartsHandler();
	
	private final static String UNKNOWN_INPUT = "Неизвестный формат ввода хода";
	
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
            Pattern.compile("^__([prbnqkPRBNQK]|(?:[a-hA-H][1-8]))__$");
    
    @Override
    public String processInput(String userInput, UserState currentUserState) {
    	Matcher notationMatch = NOTATION_PATTERN.matcher(userInput);
    	Matcher callbackMatch = CALLBACK_PATTERN.matcher(userInput);
    	if (notationMatch.find()) {
    		return moveHandler.processMove(
    				notationMatch.group(1).toLowerCase(), 
    				notationMatch.group(2).toLowerCase(), 
    				notationMatch.group(3).toLowerCase(), 
    				currentUserState.getGameState());
    	}
    	if (callbackMatch.find()) {
    		return movePartsHandler.processMovePart(
    				userInput.toLowerCase(), 
    				currentUserState.getMoveState(), 
    				currentUserState.getGameState());
    	}
    	return UNKNOWN_INPUT;
    }
}
