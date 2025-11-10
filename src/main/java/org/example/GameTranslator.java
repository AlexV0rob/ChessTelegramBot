package org.example;

import org.example.chess.GameHandler;
import org.example.chess.PositionOnBoard;

import org.example.states.GameState;

/**
 * Игровой переводчик, передаёт собщения в обрвботчик игры и обратно 
 */
public class GameTranslator {
	/**
	 * Обработчик игры
	 */
	private GameHandler gameHandler = new GameHandler();

    /**
     * Сообщения о ходе определённой стороны
     */
    private final static String[] MOVING_SIDES = {"Ход белых", "Ход чёрных"};
    /**
     * Сообщение о невозможном ходе
     */
    private final static String IMPOSSIBLE_MOVE = "Невозможный ход! Попробуйте снова.";
    /**
     * Сообщение о неправильной записи хода
     */
    private final static String INVALID_MOVE = "Неверная запись хода! Попробуйте снова.";
    /**
     * Сообщение о шахе
     */
    private final static String CHECK_MOVE = "Шах! Ваш король под угрозой!";
    /**
     * Сообщение о мате и конце партии
     */
    private final static String CHECKMATE_MOVE = "Шах и мат! Партия окончена. ";
    /**
     * сообщение о победе белых
     */
    private final static String WHITE_WIN = "Победили белые.";
    /**
     * Сообщение о победе чёрных
     */
    private final static String BLACK_WIN = "Победили чёрные.";

    /**
     * Массив букв доски
     */
    private final static String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H"};
    /**
     * Массив цифр доски
     */
    private final static String[] DIGITS = {"1", "2", "3", "4", "5", "6", "7", "8"};
    /**
     * Массив символов, обозначающих фигуры на доске
     */
    private final static String[] FIGURES_SYMBOLS = {"      ", "P", "R", "N", "B", "Q", "K"};
    /**
     * Смволы, обозначающие цвет фигуры
     */
    private final static String[] FIGURE_COLOR = {"W", " B"};
    /**
     * Число клеток в одном ряду
     */
    private final static int SQUARES_IN_A_ROW = 8;
    
    /**
     * Получить текущее состояние доски в виде строки
     */
    public String currentBoardState(GameState currentGameState) {
    	return chessboardString(GameHandler.moveProperty.REGULAR, 
    			currentGameState.getBoard(), currentGameState.isWhiteToMove());
    }
    
    /**
     * Сделать ход и получить новое состояние доски в виде строки
     */
    public String makeMove(int figureCode, PositionOnBoard start, 
    		PositionOnBoard finish, GameState currentGameState) {
    	GameHandler.moveProperty moveProperty = 
    			gameHandler.processMove(figureCode - 1, start, finish, currentGameState);
    	if (!moveProperty.equals(GameHandler.moveProperty.IMPOSSIBLE) && 
        		!moveProperty.equals(GameHandler.moveProperty.INVALID)) {
        	currentGameState.changeSide();
        }
    	return chessboardString(moveProperty, currentGameState.getBoard(), 
    			currentGameState.isWhiteToMove());
    }
    
    /**
     * Сформировать текст сообщения с состоянием доски в виде строки
     */
    private String chessboardString(GameHandler.moveProperty moveProperty, 
    		byte[][] currentChessboard, boolean isWhiteToMove) {
        String chessboardString, side, board, additional;
        board = boardString(currentChessboard, isWhiteToMove);
        if(isWhiteToMove) {
            side = MOVING_SIDES[0];        	
        } else {
            side = MOVING_SIDES[1];        	
        }
        additional = switch (moveProperty) {
        	case GameHandler.moveProperty.REGULAR -> "";
        	case GameHandler.moveProperty.IMPOSSIBLE -> "\n" + IMPOSSIBLE_MOVE;
        	case GameHandler.moveProperty.INVALID -> "\n" + INVALID_MOVE;
        	case GameHandler.moveProperty.CHECK -> "\n" + CHECK_MOVE;
        	case GameHandler.moveProperty.MATE -> 
        		"\n" + CHECKMATE_MOVE + (isWhiteToMove ? WHITE_WIN : BLACK_WIN);
        };
        if (moveProperty.equals(GameHandler.moveProperty.MATE)) {
        	//TODO
        }
        chessboardString = """
                %s
                
                %s%s
                """.formatted(side, board, additional);
        return chessboardString;
    }
    
    /**
     * Посторить доску в строковом виде
     */
    private String boardString(byte[][] currentChessboard, boolean isWhiteToMove) {
    	String boardString = "";
        if (isWhiteToMove) {
        	for (int i = SQUARES_IN_A_ROW - 1; i >= 0; --i) {
        		boardString += DIGITS[i] + "  ";
        		for (int j = 0; j < SQUARES_IN_A_ROW; ++j) {
                	boardString += "[";
                	if (currentChessboard[i][j] < 0) {
                		boardString += FIGURE_COLOR[0];
                    } else if (currentChessboard[i][j] > 0) {
                        boardString += FIGURE_COLOR[1];
                    }
                    boardString += 
                    		FIGURES_SYMBOLS[Math.abs(currentChessboard[i][j])] + "]";
                }
                boardString += "\n";
            }
            String halfSpace = " ".repeat(FIGURES_SYMBOLS[0].length() / 2 + 3);
            boardString += halfSpace;
            for (int i = 0; i < SQUARES_IN_A_ROW; ++i) {
            	boardString += LETTERS[i] + halfSpace;
            }
        } else {
            for (int i = 0; i < SQUARES_IN_A_ROW; ++i) {
            	boardString += DIGITS[i] + "  ";
                for (int j = SQUARES_IN_A_ROW - 1; j >= 0; --j) {
                	boardString += "[";
                	if (currentChessboard[i][j] < 0) {
                		boardString += FIGURE_COLOR[0];
                    } else if (currentChessboard[i][j] > 0) {
                        boardString += FIGURE_COLOR[1];
                    }
                    boardString += 
                    		FIGURES_SYMBOLS[Math.abs(currentChessboard[i][j])] + "]";
                }
                boardString += "\n";
            }
            String halfSpace = " ".repeat(FIGURES_SYMBOLS[0].length() / 2 + 3);
            boardString += halfSpace;
            for (int i = SQUARES_IN_A_ROW - 1; i >= 0; --i) {
            	boardString += LETTERS[i] + halfSpace;
            }
        }
        return boardString;
    }
}
