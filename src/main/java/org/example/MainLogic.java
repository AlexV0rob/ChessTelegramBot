package org.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Выполняет обработку пользовательского ввода
 */
public class MainLogic {
	private CommandHandler commandHandler = new CommandHandler();
	
	private GameHandler gameHandler = new GameHandler();
	
	private final static Pattern MOVE_PATTERN =
			Pattern.compile("^([PRBNQK]??)([a-h][1-8])([a-h][1-8])$");
	
	private final static Pattern CALLBACK_PATTERN =
			Pattern.compile("^callback_(.)$");
	
	private final static char[] FIGURES_SYMBOLS = {'P', 'R', 'N', 'B', 'Q', 'K'};
	
	private final static char[] LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
	
	private final static char[] DIGITS = {'1', '2', '3', '4', '5', '6', '7', '8'};
	
	private final static int BOARD_SIDE = 8;
	
	public String processUserInput(String userInput, GameState currentGameState) {
		if (userInput.charAt(0) == '/') {
			return commandHandler.processCommand(userInput, currentGameState);
		}
		if (currentGameState.currentState().equals(GameState.STATES.NOGAME)) {
			currentGameState.setState(GameState.STATES.INGAME);
		} else if (currentGameState.currentState().equals(GameState.STATES.INGAME)) {
			Matcher callbackParts = CALLBACK_PATTERN.matcher(userInput);
			Matcher moveParts = MOVE_PATTERN.matcher(userInput);
			if (callbackParts.find()) {
				char callbackInfo = callbackParts.group(1).charAt(0);
				currentGameState.changeMoveState(callbackInfo);
				if (currentGameState.isMoveReady()) {
					String move = currentGameState.getMove();
					byte figureCode = (byte) move.charAt(0);
					if (currentGameState.isWhiteMove()) {
						figureCode *= -1;
					}
					int startPositionCode = (int) move.charAt(1);
					int finishPositionCode = (int) move.charAt(2);
					//TODO return GameHandler
				}
			} else if (moveParts.find()) {
				String figure = moveParts.group(1);
				String startPosition = moveParts.group(2);
				String finishPosition = moveParts.group(3);
				byte figureCode = calculateFigureCode(figure);
				if (currentGameState.isWhiteMove()) {
					figureCode *= -1;
				}
				int startPositionCode = calculatePositionCode(startPosition);
				int finishPositionCode = calculatePositionCode(finishPosition);
				//TODO return gameHandler
			} else {
				return currentGameState.printBoard(GameState.MOVE_PROPERTIES.INVALID);
			}
		}
		return currentGameState.printBoard(GameState.MOVE_PROPERTIES.REGULAR);
	}
	
	private byte calculateFigureCode(String figure) {
		byte code = 1;
		if (figure.isEmpty()) {
			return code;
		}
		char figureSymbol = figure.charAt(0);
		while (FIGURES_SYMBOLS[code - 1] != figureSymbol) {
			++code;
		}
		return code;
	}
	
	private int calculatePositionCode(String position) {
		char letter = position.charAt(0);
		char digit = position.charAt(1);
		int letterCode = 0, digitCode = 0;
		while (LETTERS[BOARD_SIDE - letterCode - 1] != letter) {
			++letterCode;
		}
		while (DIGITS[digitCode] != digit) {
			++digitCode;
		}
		return digitCode * BOARD_SIDE + (BOARD_SIDE - letterCode - 1);
	}
}
