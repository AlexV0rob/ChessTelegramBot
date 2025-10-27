package org.example;

import java.util.regex.Pattern;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Выполняет обработку пользовательского ввода
 */
public class MainLogic {
	/**
	 * Обработчик команд
	 */
	private final CommandHandler commandHandler = new CommandHandler();
	/**
	 * Обработчик игры
	 */
	private final GameHandler gameHandler = new GameHandler();
	
	/**
	 * Скомпилированное регулярное выражение, соответствующее полностью 
	 * введённому ходу в текстовом виде
	 */
	private final static Pattern MOVE_PATTERN =
			Pattern.compile("^([PRBNQKprbhqk]??)([a-hA-H][1-8])([a-hA-H][1-8])$");
	/**
	 * Какая-то часть хода в виде callback запроса
	 */
	private final static Pattern CALLBACK_PATTERN =
			Pattern.compile("^callback_(.)$");
	
	/**
	 * Символы фигур в записи хода
	 */
	private final static char[] FIGURES_SYMBOLS = {'P', 'R', 'N', 'B', 'Q', 'K'};
	/**
	 * Количество фигур
	 */
	private final static int FIGURES_COUNT = FIGURES_SYMBOLS.length;
	/**
	 * Символы букв доски в записи хода
	 */
	private final static char[] LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
	/**
	 * Символы цифр доски в записи хода
	 */
	private final static char[] DIGITS = {'1', '2', '3', '4', '5', '6', '7', '8'};
	/**
	 * Длина стороны доски
	 */
	private final static int BOARD_SIDE = 8;
	
	/**
	 * Обработать пользовательский ввод
	 * @return текст ответного сообщения
	 */
	public List<String> processUserInput(String userInput, GameState currentGameState) {
		//Проверка на команду
		if (userInput.charAt(0) == '/') {
			return commandHandler.processCommand(userInput, currentGameState);
		}
		//Любой не командный ввод не в режиме игры начинает игру 
		if (currentGameState.isNoGame()) {
			currentGameState.setState(GameState.STATES.INGAME);
		} else if (currentGameState.isInGame()) {
			Matcher callbackParts = CALLBACK_PATTERN.matcher(userInput);
			Matcher moveParts = MOVE_PATTERN.matcher(userInput);
			if (callbackParts.find()) {
				char callbackInfo = callbackParts.group(1).charAt(0);
				currentGameState.changeMoveState(callbackInfo);
			} else if (moveParts.find()) {
				String figure = moveParts.group(1).toUpperCase();
				String startPosition = moveParts.group(2).toLowerCase();
				String finishPosition = moveParts.group(3).toLowerCase();
				byte figureCode = calculateFigureCode(figure);
				if (currentGameState.isWhiteToMove()) {
					figureCode *= -1;
				}
				int startPositionCode = calculatePositionCode(startPosition);
				int finishPositionCode = calculatePositionCode(finishPosition);
				//TODO return gameHandler
			} else {
				return List.of(currentGameState
						.printBoard(GameState.MOVE_PROPERTIES.INVALID));
			}
		}
		return List.of(currentGameState
				.printBoard(GameState.MOVE_PROPERTIES.REGULAR));
	}
	
	/**
	 * Получить числовой код фигуры по её символу
	 */
	private byte calculateFigureCode(String figure) {
		//Код пешки
		byte code = 1;
		//Пешка может как обозначаться P, так и не обозначаться ничем вовсе
		if (figure.isEmpty()) {
			return code;
		}
		
		char figureSymbol = figure.charAt(0);
		//Символы в массиве идут со смещением назад на 1
		while (code <= FIGURES_COUNT && FIGURES_SYMBOLS[code - 1] != figureSymbol) {
			++code;
		}
		
		return code;
	}
	/**
	 * Вычислить числовой номер позиции по коду из буквы и цифры
	 */
	private int calculatePositionCode(String position) { 
		char letter = position.charAt(0);
		char digit = position.charAt(1);
		int letterCode = BOARD_SIDE - 1, digitCode = 0;
		
		/*
		 * Доска идёт как линейный массив в обратном порядке для лучшего 
		 * внешнего представления (чёрные сверху доски - в начале массива, 
		 * белые снизу доски - в конце массива), поэтому код буквы идёт 
		 * с конца в начало
		 */
		while (letterCode >= 0 && LETTERS[letterCode] != letter) {
			--letterCode;
		}
		while (digitCode < BOARD_SIDE && DIGITS[digitCode] != digit) {
			++digitCode;
		}
		
		//Цифра означает количество рялов, буква - позицию в это ряду
		return digitCode * BOARD_SIDE + (BOARD_SIDE - letterCode - 1);
	}
}
