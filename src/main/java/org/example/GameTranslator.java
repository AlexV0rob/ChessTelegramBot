package org.example;

import org.example.chess.GameHandler;

import java.util.List;

/**
 * Игровой переводчик, передаёт собщения в обработчик игры и обратно
 */
public class GameTranslator {
    /**
     * Сообщения о ходе определённой стороны
     */
    private final List<String> MOVING_SIDES = List.of("Ход белых", "Ход чёрных");
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
    private final List<String> LETTERS = List.of("A", "B", "C", "D", "E", "F", "G", "H");
    /**
     * Массив цифр доски
     */
    private final List<String> DIGITS = List.of("1", "2", "3", "4", "5", "6", "7", "8");
    /**
     * Массив символов, обозначающих фигуры на доске
     */
    private final List<String> FIGURES_SYMBOLS = List.of("      ", "P", "R", "N", "B", "Q", "K");
    /**
     * Символы, обозначающие цвет фигуры
     */
    private final List<String> FIGURE_COLOR = List.of("W", " B");

    /**
     * Сформировать текст сообщения с состоянием доски в виде строки
     */
    public String chessboardString(GameHandler.MoveProperty moveProperty,
                                   byte[][] currentChessboard, int sideLength, boolean isWhiteToMove,
                                   boolean forWhiteSide) {
        String chessboardString, side, board, additional;
        board = boardString(currentChessboard, sideLength, forWhiteSide);
        if (isWhiteToMove) {
            side = MOVING_SIDES.get(0);
        } else {
            side = MOVING_SIDES.get(1);
        }
        additional = switch (moveProperty) {
            case GameHandler.MoveProperty.REGULAR,
                 GameHandler.MoveProperty.START -> "";
            case GameHandler.MoveProperty.IMPOSSIBLE -> "\n" + IMPOSSIBLE_MOVE;
            case GameHandler.MoveProperty.INVALID -> "\n" + INVALID_MOVE;
            case GameHandler.MoveProperty.CHECK -> "\n" + CHECK_MOVE;
            case GameHandler.MoveProperty.MATE -> "\n" + CHECKMATE_MOVE + (isWhiteToMove ? WHITE_WIN : BLACK_WIN);
        };
        chessboardString = """
                %s
                
                %s%s
                """.formatted(side, board, additional);
        return chessboardString;
    }

    /**
     * Посторить доску в строковом виде
     */
    private String boardString(byte[][] currentChessboard, int sideLength, boolean isWhiteToMove) {
        String boardString = "";
        if (isWhiteToMove) {
            for (int i = sideLength - 1; i >= 0; --i) {
                boardString += DIGITS.get(i) + "  ";
                for (int j = 0; j < sideLength; ++j) {
                    boardString += "[";
                    if (currentChessboard[i][j] < 0) {
                        boardString += FIGURE_COLOR.get(0);
                    } else if (currentChessboard[i][j] > 0) {
                        boardString += FIGURE_COLOR.get(1);
                    }
                    boardString +=
                            FIGURES_SYMBOLS.get(Math.abs(currentChessboard[i][j])) + "]";
                }
                boardString += "\n";
            }
            String halfSpace = " ".repeat(FIGURES_SYMBOLS.get(0).length() / 2 + 3);
            boardString += halfSpace;
            for (int i = 0; i < sideLength; ++i) {
                boardString += LETTERS.get(i) + halfSpace;
            }
        } else {
            for (int i = 0; i < sideLength; ++i) {
                boardString += DIGITS.get(i) + "  ";
                for (int j = sideLength - 1; j >= 0; --j) {
                    boardString += "[";
                    if (currentChessboard[i][j] < 0) {
                        boardString += FIGURE_COLOR.get(0);
                    } else if (currentChessboard[i][j] > 0) {
                        boardString += FIGURE_COLOR.get(1);
                    }
                    boardString +=
                            FIGURES_SYMBOLS.get(Math.abs(currentChessboard[i][j])) + "]";
                }
                boardString += "\n";
            }
            String halfSpace = " ".repeat(FIGURES_SYMBOLS.get(0).length() / 2 + 3);
            boardString += halfSpace;
            for (int i = sideLength - 1; i >= 0; --i) {
                boardString += LETTERS.get(i) + halfSpace;
            }
        }
        return boardString;
    }
}
