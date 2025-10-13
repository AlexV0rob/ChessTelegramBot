package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Класс, обрабатывающий ход в формате callback запроса 
 */
public class MoveMaker {
	/**
	 * Массив букв доски
	 */
	private final String[] LETTERS = {"A", "B", "C", "D", 
										"E", "F", "G", "H"};
	/**
	 * Массив цифр доски
	 */
	private final String[] DIGITS = {"8", "7", "6", "5", 
										"4", "3", "2", "1"};
	/**
	 * Массив игровых фигур
	 */
	private final String[] FIGURES = {"НЕИЗВЕСТНО", "ПЕШКА", "ЛАДЬЯ",
									"КОНЬ", "СЛОН", "ФЕРЗЬ", "КОРОЛЬ"};
	/**
	 * Количество фигур, различая чёрные и белые
	 */
	private final int FIGURES_COUNT = FIGURES.length * 2 - 1;
	/**
	 * Значки, обозначающие фигуры на доске
	 */
	private String[] FIGURES_SYMBOLS = {"  ", "BP", "WP", "BR", "WR", "BN", "WN",
											"BB", "WB", "BQ", "WQ", "BK", "WK"};
	
	/**
	 * Определить, на какой стадии находится ход, и отдать на обработку
	 * @param callbackText
	 * @param curUser
	 * @return экземпляр ArrayList, содержит текст следующего сообщения
	 * и кнопки для него (класс MoveButton)
	 */
	public ArrayList<MoveButton> assembleMove(String callbackText, User curUser) {
		int numberOfParts = StringUtils.countMatches(callbackText, '_');
		String[] parts = StringUtils.split(callbackText, '_');
		/*
		 * Количество частей означает готовность сообщения хода
		 * 0 - запрос пуст, нужно добавить фигуры 
		 * 1 - известна фигура
		 * 2 - выбрана начальная позиция
		 * 3 - выбрана буква конечной позиции
		 * 4 - выбрана цифра конечной позиции
		 * Все остальные означают, что запрос ошибочный
		 */
		ArrayList<MoveButton> newButtons = new ArrayList<MoveButton>();
		switch(numberOfParts) {
		case 0:
			newButtons = whichFiguresLeft(curUser);
			break;
		case 1:
			//Находим все позиции фигуры выбранного типа на доске
			int curFigure = (int) parts[1].charAt(0);
			for (int i = 0; i < 64; ++i) {
				if (curUser.getBoard()[i] == curFigure) {
					newButtons.add(new MoveButton(
							callbackText + "_" + ((char) i),
							LETTERS[i % 8] + DIGITS[i / 8]));
				}
			}
			break;
		case 2:
			//Клавиатура букв
			for (int i = 0; i < 8; ++i) {
				newButtons.add(new MoveButton(
						callbackText + "_" + ((char) i),
						LETTERS[i]));
			}
			break;
		case 3:
			//Клавиатура цифр
			for (int i = 7; i >= 0; --i) {
				newButtons.add(new MoveButton(
						callbackText + "_" + ((char) i),
						DIGITS[i]));
			}
			break;
		case 4:
			int figureCode = (int) parts[1].charAt(0);
			int startPosition = (int) parts[2].charAt(0);
			int finishPosition = (int) parts[3].charAt(0) +
					(int) parts[4].charAt(0) * 8;
			boolean oldSide = curUser.doesWhitesMove();
			String move = Integer.toString(figureCode) + ' ' + 
					Integer.toString(startPosition) + ' ' +
					Integer.toString(finishPosition);
			curUser.changeSide();
			//TODO Послать ход обработчику
			if (curUser.doesWhitesMove() != oldSide) {
				newButtons = whichFiguresLeft(curUser);
				String board = printBoard(curUser);
				
				newButtons.add(0, new MoveButton("board", board));
				if (curUser.doesWhitesMove()) {
					newButtons.add(0, new MoveButton("side", "Ход белых"));
				} else {
					newButtons.add(0, new MoveButton("side", "Ход чёрных"));
				}
			}
			break;
		}
		
		//Добавление нового текста сообщения, ставится в позицию 0
		newButtons.add(0, new MoveButton("message",
				currentMessage(numberOfParts, parts)));
		return newButtons;
	}
	
	/**
	 * Проверить, какие фигуры остались на доске
	 * @param curUser
	 * @return экземпляр ArrayList, содержит MoveButton с этими фигурами
	 */
	public ArrayList<MoveButton> whichFiguresLeft(User curUser) {
		ArrayList<MoveButton> leftFigures = new ArrayList<MoveButton>();
		boolean[] figuresLeft = new boolean[FIGURES_COUNT];
		Arrays.fill(figuresLeft, false);
		boolean movingSide = curUser.doesWhitesMove();
		for (byte position : curUser.getBoard()) {
			figuresLeft[position] = true;
		}
		for (int i = 1 + (movingSide ? 1 : 0); i < FIGURES_COUNT; i += 2) {
			if (figuresLeft[i]) {
				/* 
				 * Так как фигуры идут со смещением назад, их код в таблице
				 * фигур нужно получать через прибавление 1
				 * Пример: код чёрной пешки - 1, белой пешки - 2,
				 * индекс пешки в таблице фигур - 1, чтобы его получить, к коду
				 * фигуры прибавляется единица и берётся остаток от деления на 2
				 */
				leftFigures.add(new MoveButton(
						"move_" + ((char) i),
						FIGURES[(i + 1) / 2]));
			}
		}
		return leftFigures;
	}
	
	/**
	 * Получить запрос на передвижение фигуры из пользовательского ввода
	 * @param messageText
	 * @param curUser
	 * @return экземпляр ArrayList, содержит ответные сообщения (класс String)
	 */
	public ArrayList<String> fromStringToQuery(String messageText, User curUser) {
		String[] parts = StringUtils.split(messageText);
		
		int figureCode = 0, startPosition = 0, finishLetter = 0, finishDigit = 0;
		for (int i = 0; i < FIGURES_COUNT; ++i) {
			if (parts[0].equalsIgnoreCase(FIGURES[0])) {
				figureCode = i;
				break;
			}
		}
		
		for (int i = 0; i < 8; ++i) {
			if (String.valueOf(parts[1].charAt(0))
					.equalsIgnoreCase(LETTERS[i])) {
				startPosition += i * 8;
			}
		}
		for (int i = 7; i >= 0; --i) {
			if (String.valueOf(parts[1].charAt(1))
					.equalsIgnoreCase(LETTERS[i])) {
				startPosition += i;
			}
		}
		
		for (int i = 0; i < 8; ++i) {
			if (String.valueOf(parts[2].charAt(0))
					.equalsIgnoreCase(LETTERS[i])) {
				finishLetter = i;
			}
		}
		
		for (int i = 7; i >= 0; --i) {
			if (String.valueOf(parts[2].charAt(1))
					.equalsIgnoreCase(LETTERS[i])) {
				finishDigit = i;
			}
		}
		
		String query = "move_" + ((char) figureCode) + "_" + ((char) startPosition) +
				"_" + ((char) finishLetter) + "_" + ((char) finishDigit);
		ArrayList<MoveButton> buttons = assembleMove(query, curUser);
		
		String[] messages = new String[3];
        int buttonsCount = buttons.size();
        
        for (int i = 0; i < buttonsCount; ++i) {
        	if (buttons.get(i).getCallbackQuery().equals("side")) {
        		messages[0] = buttons.get(i).getButtonMessage();
        	} else if (buttons.get(i).getCallbackQuery().equals("board")) {
        		messages[1] = buttons.get(i).getButtonMessage();
        	}
        }
        messages[2] = "Ваш ход: ";
		return new ArrayList<String>(Arrays.asList(messages));
	}
	
	/**
	 * Нарисовать текущее состояние доски
	 * @param curUser
	 * @return экземпляр String, содержит текстовое представление доски
	 */
	public String printBoard(User curUser) {
		String boardString = "";
		byte[] board = curUser.getBoard();
		if (curUser.doesWhitesMove()) {
			for (int i = 0, row = 1; i < board.length; ++i, ++row) {
				boardString += "[" + FIGURES_SYMBOLS[board[i]] + "]";
				if (row == 8) {
					boardString += "\n";
					row = 0;
				}
			}
			
		} else {
			for (int i = board.length - 1, row = 1; i >= 0; --i, ++row) {
				boardString += "[" + FIGURES_SYMBOLS[board[i]] + "]";
				if (row == 8) {
					boardString += "\n";
					row = 0;
				}
			}
		}
		return boardString;
	}
	
	/**
	 * Составить новый текст сообщения из разделённого текста callback запроса
	 * @param partsCount
	 * @param callbackParts
	 * @return экземпляр String, содержит новый текст
	 */
	private String currentMessage(int partsCount, String[] callbackParts) {
		/*
		 * Текст callback запроса выглядит так:
		 * move_[фигура]_[начальная позиция]_[буква конечной позиции]_[цифра конечной позиции]
		 * Поэтому поочерёдно проходя по индексам и сравнивая его с числом частей
		 * можно определить, закончился текст запроса или нет
		 */
		String message = "Ваш ход: ";
		int partIndex = 1;
		if (partIndex <= partsCount) {
			message += FIGURES[((int) callbackParts[partIndex].charAt(0) + 1) / 2];
			++partIndex;
		}
		
		if (partIndex <= partsCount) {
			message += ' ' + LETTERS[(int) callbackParts[partIndex].charAt(0) % 8]
					+ DIGITS[(int) callbackParts[partIndex].charAt(0) / 8];
			++partIndex;
		}
		if (partIndex <= partsCount) {
			message += ' ' + LETTERS[(int) callbackParts[partIndex].charAt(0)];
			++partIndex;
		}
		if (partIndex <= partsCount) {
			message += DIGITS[(int) callbackParts[partIndex].charAt(0)];
			++partIndex;
		}
		return message;
	}
}
