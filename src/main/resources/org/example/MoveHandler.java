package org.example;

import java.util.HashMap;
import java.util.Map;

public class MoveHandler {
	private final static Map<String, Integer> FIGURES_CODES = 
			new HashMap<String, Integer>();
	private final static Map<Character, Integer> LETTERS_CODES = 
			new HashMap<Character, Integer>();
	private final static Map<Character, Integer> DIGITS_CODES = 
			new HashMap<Character, Integer>();
	
	private final GameTranslator gameTexts = new GameTranslator();
	
	public MoveHandler() {
		FIGURES_CODES.put("", 1);
		FIGURES_CODES.put("p", 1);
		FIGURES_CODES.put("r", 2);
		FIGURES_CODES.put("n", 3);
		FIGURES_CODES.put("b", 4);
		FIGURES_CODES.put("q", 5);
		FIGURES_CODES.put("k", 6);

		LETTERS_CODES.put('a', 0);
		LETTERS_CODES.put('b', 1);
		LETTERS_CODES.put('c', 2);
		LETTERS_CODES.put('d', 3);
		LETTERS_CODES.put('e', 4);
		LETTERS_CODES.put('f', 5);
		LETTERS_CODES.put('g', 6);
		LETTERS_CODES.put('h', 7);
		
		DIGITS_CODES.put('1', 0);
		DIGITS_CODES.put('2', 1);
		DIGITS_CODES.put('3', 2);
		DIGITS_CODES.put('4', 3);
		DIGITS_CODES.put('5', 4);
		DIGITS_CODES.put('6', 5);
		DIGITS_CODES.put('7', 6);
		DIGITS_CODES.put('8', 7);	
	}
	
    public String processMove(String figure, String startPosition, String finishPosition, 
    		GameState currentGameState) {
        int figureCode = 0;
        int startPositionRow = -1;
        int startPositionColumn = -1;
        int finishPositionRow = -1;
        int finishPositionColumn = -1;
        figureCode = calculateFigureCode(figure);
        startPositionRow = calculatePositionRowCode(startPosition.charAt(1));
        startPositionColumn = calculatePositionColumnCode(startPosition.charAt(0));
        finishPositionRow = calculatePositionRowCode(finishPosition.charAt(1));
        finishPositionColumn = calculatePositionColumnCode(finishPosition.charAt(0));
        return gameTexts.makeMove(figureCode, 
        		new PositionOnBoard(startPositionRow, startPositionColumn), 
        		new PositionOnBoard(finishPositionRow, finishPositionColumn),
        		currentGameState); 
    }
    
    protected int calculateFigureCode(String figure) {
        int code;
        if (FIGURES_CODES.get(figure) == null) {
        	code = 0;
        } else {
        	code = FIGURES_CODES.get(figure);
        }
        return code;
    }
    
    protected int calculatePositionRowCode(char digit) {
        int code;
        if (DIGITS_CODES.get(digit) == null) {
        	code = -1;
        } else {
        	code = DIGITS_CODES.get(digit);
        }
        return code;
    }
    
    protected int calculatePositionColumnCode(char letter) {
        int code;
        if (LETTERS_CODES.get(letter) == null) {
        	code = -1;
        } else {
        	code = LETTERS_CODES.get(letter);
        }
        return code;
    }
}
