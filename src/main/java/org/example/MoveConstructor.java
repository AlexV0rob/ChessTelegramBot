package org.example;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.example.buttons.*;

/**
 * Конструктор хода игрока
 */
public class MoveConstructor {
	/**
	 * Возможный состояния готовности хода
	 */
	public static enum STATUS {
		/**
		 * Ход не собран нисколько
		 */
		NOTHING,
		/**
		 * Известна фигура
		 */
		FIGURE,
		/**
		 * Известна фигура и начальная позиция
		 */
		START,
		/**
		 * Известна фигура, начальны=ая и конечная позиции, ход готов
		 */
		FINISH
	}
	/**
	 * Код фигуры
	 */
	private byte figure;
	/**
	 * Код начальной позиции
	 */
	private int startPosition;
	/**
	 * Код конечной позиции
	 */
	private int finishPosition;
	/**
	 * Текущее состояние готовности хода
	 */
	private STATUS currentStatus;
	
	/**
	 * Конструктор, ставит 0 в фигуру, -1 в начальную и конечную позиции и
	 * состояние отсутствия готовности хода
	 */
	public MoveConstructor() {
		figure = 0;
		startPosition = -1;
		finishPosition = -1;
		currentStatus= STATUS.NOTHING;
	}
	
	/**
	 * Очистить готовность хода, ставит 0 в фигуру, -1 в начальную и
	 * конечную позиции и состояние отсутствия готовности хода
	 */
	public void clear() {
		figure = 0;
		startPosition = -1;
		finishPosition = -1;
		currentStatus= STATUS.NOTHING;
	}
	
	/**
	 * Получить текущее состояние готовности хода
	 */
	public STATUS getStatus() {
		return currentStatus;
	}
	/**
	 * Получить код фигуры
	 */
	public byte getFigure() {
		return figure;
	}
	/**
	 * Получить код начальной позиции
	 */
	public int getStartPosition() {
		return startPosition;
	}
	/**
	 * Полуить код конечной поизиции
	 */
	public int getFinishPosition() {
		return finishPosition;
	}	
	/**
	 * Собрать текущий ход в строковом формате, понятном для MainLogic
	 */
	public String flushMove(String[] figuresSymbols, String[] lettersNames,
			String[] digitsNames, int squaresInARow) {
		String move = figuresSymbols[figure] +
				lettersNames[startPosition % squaresInARow] +
				digitsNames[startPosition / squaresInARow] +
				lettersNames[finishPosition % squaresInARow] +
				digitsNames[finishPosition / squaresInARow];
		nextStatus(-1);
		return move;
	}
	
	/**
	 * Поставить новую часть хода в соответсвующее поле  поменять статус
	 * готовности хода на следующее
	 * @param newPart в зависимости от текущего состояния готовности хода
	 * может быть фигурой, начальной или конечной позицией
	 */
	public void nextStatus(int newPart) {
		switch (currentStatus) {
		case NOTHING:
			figure = (byte) newPart;
			currentStatus = STATUS.FIGURE;
			break;
		case FIGURE:
			startPosition = newPart;
			currentStatus = STATUS.START;
			break;
		case START:
			finishPosition = newPart;
			currentStatus = STATUS.FINISH;
			break;
		case FINISH:
			clear();
			break;
		}
	}

	/**
	 * Получить список кнопок для следующей части хода 
	 */
	public List<IdentificatedButton> nextButtonsLine(byte[] chessboard,
			boolean whiteToMove, String[] figuresNames,
			String[] lettersNames, String[] digitsNames) {
		switch (currentStatus) {
		case MoveConstructor.STATUS.FINISH:
			return List.of();
		case MoveConstructor.STATUS.NOTHING:
			return figuresLeft(chessboard, whiteToMove, figuresNames);
		case MoveConstructor.STATUS.FIGURE:
			return whereIsFigure(chessboard, whiteToMove, lettersNames,
					digitsNames);
		case MoveConstructor.STATUS.START:
			return possibleMoves(chessboard, whiteToMove,
					lettersNames, digitsNames);
		default:
			return new LinkedList<IdentificatedButton>();
		}
	}
	/**
	 * Кнопки с оставшимися на доске фигурами данной стороны
	 */
	private List<IdentificatedButton> figuresLeft(byte[] chessboard,
			boolean whiteToMove, String[] figuresNames) {
		int figuresCount = figuresNames.length;
		int squaresCount = chessboard.length;
		boolean[] figures = new boolean[figuresCount];
		Arrays.fill(figures, false);
		for (int i = 0; i < squaresCount; ++i) {
			if (whiteToMove && chessboard[i] < 0 ||
					!whiteToMove && chessboard[i] > 0) {
				figures[Math.abs(chessboard[i])] = true;
			}
		}
		List<IdentificatedButton> buttons = new LinkedList<IdentificatedButton>();
		for (int i = 1; i < figuresCount; ++i) {
			if (figures[i]) {
				buttons.add(new IdentificatedButton(figuresNames[i],
						String.valueOf((char) i)));
			}
		}
		return buttons;
	}
	/**
	 * Кнопки с возможными начальными позициями для текущей фигуры 
	 */
	private List<IdentificatedButton> whereIsFigure(byte[] chessboard,
			boolean whiteToMove, String[] lettersNames, String[] digitsNames) {
		int unit = (whiteToMove ? -1 : 1);
		int squaresCount = chessboard.length;
		int squaresInARow = (int) Math.sqrt(squaresCount);
		List<IdentificatedButton> buttons = new LinkedList<IdentificatedButton>();
		for (int i = 0; i < squaresCount; ++i) {
			if (chessboard[i] == unit * figure) {
				buttons.add(new IdentificatedButton(
						lettersNames[i % squaresInARow] +
						digitsNames[i / squaresInARow],
						String.valueOf((char) i)));
			}
		}
		return buttons;
	}
	/**
	 * Кнопки с возможными конечными позициями для текущей фигуры
	 */
	private List<IdentificatedButton> possibleMoves(
			byte[] chessboard, boolean whiteToMove,
			String[] lettersNames, String[] digitsNames) {
		int squaresCount = chessboard.length;
		int squaresInARow = (int) Math.sqrt(squaresCount);

		return new LinkedList<IdentificatedButton>();
	}
}
