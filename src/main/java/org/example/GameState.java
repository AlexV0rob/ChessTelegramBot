package org.example;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Текущее состояние игры
 */
public class GameState {
	
	private MoveConstructor currentMove;
	
	private final static byte[] NEW_GAME_BOARD = {
			 2,  3,  4,  5,  6,  4,  3,  2,
			 1,  1,  1,  1,  1,  1,  1,  1,
			 0,  0,  0,  0,  0,  0,  0,  0,
			 0,  0,  0,  0,  0,  0,  0,  0,
			 0,  0,  0,  0,  0,  0,  0,  0,
			 0,  0,  0,  0,  0,  0,  0,  0,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-2, -3, -4, -5, -6, -4, -3, -2,
	};
	
	private final static String[] MOVING_SIDES = {"Ход белых", "Ход чёрных"};
	
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	private final static String IMPOSSIBLE_MOVE = "Невозможный ход! Попробуйте снова.";
	
	private final static String INVALID_MOVE = "Неверная запись хода! Попробуйте снова.";

	private final static String CHECK_MOVE = "Шах! Ваш король под угрозой!";
	
	private final static String CHECKMATE_MOVE = "Шах и мат! Партия окончена.";
	
	private final static String WHITE_WIN = "Победили белые.";
	
	private final static String BLACK_WIN = "Победили чёрный.";
	
	/**
	 * Массив букв доски
	 */
	private final static String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H"};
	/**
	 * Массив цифр доски
	 */
	private final static String[] DIGITS = {"8", "7", "6", "5", "4", "3", "2", "1"};
	
	/**
	 * Значки, обозначающие фигуры на доске
	 */
	private final static String[] FIGURES_SYMBOLS = {"      ", "P", "R", "N", "B", "Q", "K"};
	/**
	 * Массив игровых фигур
	 */
	private final static String[] FIGURES = {"НЕИЗВЕСТНО", "ПЕШКА", "ЛАДЬЯ",
											"КОНЬ", "СЛОН", "ФЕРЗЬ", "КОРОЛЬ"};
	
	private final static int FIGURES_COUNT = FIGURES.length;
	
	/**
	 * Состояния, в которых может находиться игра
	 */
	public static enum STATES {
		NOGAME,
		INGAME
	}
	
	public static enum MOVE_PROPERTIES {
		REGULAR,
		IMPOSSIBLE,
		INVALID,
		CHECK,
		CHECKMATE
	}
	/**
	 * Число клеток на доске
	 */
	private final static int SQUARES_COUNT = 64;
	
	private final static int SQUARES_IN_A_ROW = 8;
	
	/**
	 * Текущее состояние доски
	 */
	private byte[] chessboard;
	/**
	 * Ходят ли сейчас белые
	 */
	private boolean whiteToMove;
	
	private STATES currentState;
	
	/**
	 * Конструктор класса, ставит пустую доску и ход белых
	 */
	public GameState() {
		chessboard = new byte[SQUARES_COUNT];
		Arrays.fill(chessboard, (byte) 0);
		whiteToMove = true;
		currentState = STATES.NOGAME;
		currentMove = new MoveConstructor();
	}
	
	/**
	 * Поменять местами фигуры на доске в startPosition и finishPosition
	 * и изменить ходящую сторону
	 */
	public void changeBoard(int startPosition, int finishPosition) {
		byte tempCell = chessboard[startPosition];
		chessboard[startPosition] = chessboard[finishPosition];
		chessboard[finishPosition] = tempCell;
	}
	
	public void changeMovingSide() {
		whiteToMove = !whiteToMove;
	}
	
	public void setState(STATES newState) {
		currentState = newState;
		switch (newState) {
		case STATES.NOGAME:
			Arrays.fill(chessboard, (byte) 0);
			break;
		case STATES.INGAME:
			for (int i = 0; i < SQUARES_COUNT; ++i) {
				chessboard[i] = NEW_GAME_BOARD[i];
			}
			break;
		}
		whiteToMove = true;
	}
	
	public STATES currentState() {
		return currentState;
	}
	
	public boolean isWhiteMove() {
		return whiteToMove;
	}
	
	public void changeMoveState(int newMovePart) {
		currentMove.nextStatus(newMovePart);
	}
	
	public boolean isMoveReady() {
		return currentMove.getStatus().equals(MoveConstructor.STATUS.FINISH);
	}
	
	public String getMove() {
		return currentMove.flushMove();
	}
	
	public String printBoard(MOVE_PROPERTIES property) {
		String chessboardString, side, board = "", additional, move, figure = "", 
				startPosition = "", finishPosition = "";
		if (whiteToMove) {
			side = MOVING_SIDES[0];
			for (int i = 0, rowLoading = 0; i < SQUARES_COUNT; ++i, ++rowLoading) {
				if (rowLoading == SQUARES_IN_A_ROW) {
					board += "\n";
					rowLoading = 0;
				}
				board += "[";
				if (chessboard[i] > 0) {
					board += " B";
				} else if (chessboard[i] < 0) {
					board += "W";
				}
				board += FIGURES_SYMBOLS[Math.abs(chessboard[i])] + "]";
			}
		} else {
			side = MOVING_SIDES[1];
			for (int i = SQUARES_COUNT - 1, rowLoading = 0; i >= 0; --i, ++rowLoading) {
				if (rowLoading == SQUARES_IN_A_ROW) {
					board += "\n";
					rowLoading = 0;
				}
				board += "[";
				if (chessboard[i] > 0) {
					board += " B";
				} else if (chessboard[i] < 0) {
					board += "W";
				}
				board += FIGURES_SYMBOLS[Math.abs(chessboard[i])] + "]";
			}
		}
		if (property.equals(MOVE_PROPERTIES.CHECKMATE)) {
			additional = CHECKMATE_MOVE + (whiteToMove ? WHITE_WIN : BLACK_WIN);
			move = "";
		} else {
			if (property.equals(MOVE_PROPERTIES.IMPOSSIBLE)) {
				additional = IMPOSSIBLE_MOVE;
			} else if (property.equals(MOVE_PROPERTIES.INVALID)) {
				additional = INVALID_MOVE;
			} else if (property.equals(MOVE_PROPERTIES.CHECK)) {
				additional = CHECK_MOVE;
			} else {
				additional = "";
			}
			move = YOUR_MOVE;
			if (currentMove.getFigure() > 0) {
				figure = FIGURES[Math.abs(currentMove.getFigure())];
			}
			if (currentMove.getStartPosition() >= 0) {
				startPosition = LETTERS[currentMove.getStartPosition() % SQUARES_IN_A_ROW] +
					DIGITS[currentMove.getStartPosition() / SQUARES_IN_A_ROW];
			}
			if (currentMove.getFinishPosition() >= 0) {
				finishPosition = LETTERS[currentMove.getFinishPosition() % SQUARES_IN_A_ROW] +
					DIGITS[currentMove.getFinishPosition() / SQUARES_IN_A_ROW];
			}
		}
		chessboardString = """
				%s
				
				%s
				
				%s
				%s %s %s %s
				""".formatted(side, board, additional, move, figure,
						startPosition, finishPosition);
		return chessboardString;
	}
	
	public List<ButtonWithID> nextButtonsLine() {
		switch (currentMove.getStatus()) {
		case MoveConstructor.STATUS.NOTHING:
		case MoveConstructor.STATUS.FINISH:
			return figuresLeft();
		case MoveConstructor.STATUS.FIGURE:
			return whereIsFigure(currentMove.getFigure());
		case MoveConstructor.STATUS.START:
			return possibleMoves(currentMove.getFigure(),
					currentMove.getStartPosition());
		default:
			return new LinkedList<ButtonWithID>();
		}
	}
	
	private List<ButtonWithID> figuresLeft() {
		boolean[] figures = new boolean[FIGURES_COUNT];
		Arrays.fill(figures, false);
		for (int i = 0; i < SQUARES_COUNT; ++i) {
			if (whiteToMove && chessboard[i] < 0 ||
					!whiteToMove && chessboard[i] > 0) {
				figures[Math.abs(chessboard[i])] = true;
			}
		}
		List<ButtonWithID> buttons = new LinkedList<ButtonWithID>();
		for (int i = 1; i < FIGURES_COUNT; ++i) {
			if (figures[i]) {
				buttons.add(new ButtonWithID(FIGURES[i], String.valueOf((char) i)));
			}
		}
		return buttons;
	}
	
	private List<ButtonWithID> whereIsFigure(byte figureCode) {
		List<ButtonWithID> buttons = new LinkedList<ButtonWithID>();
		int unit = (whiteToMove ? -1 : 1);
		for (int i = 0; i < SQUARES_COUNT; ++i) {
			if (chessboard[i] == unit * figureCode) {
				buttons.add(new ButtonWithID(
						LETTERS[i % SQUARES_IN_A_ROW] +
							DIGITS[i / SQUARES_IN_A_ROW],
						String.valueOf((char) i)));
			}
		}
		return buttons;
	}
	
	private List<ButtonWithID> possibleMoves(byte figureCode, int startPositionCode) {
		//TODO возможные ходы для этой фигуры из этой позиции
		return new LinkedList<ButtonWithID>();
	}
}
