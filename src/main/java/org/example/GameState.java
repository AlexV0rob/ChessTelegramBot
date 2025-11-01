package org.example;

import java.util.Arrays;
import java.util.List;

import org.example.buttons.*;

/**
 * Текущее состояние игры
 */
public class GameState {
    /**
     * Обработчик игры
     */
    public final GameHandler gameHandler = new GameHandler();
    /**
     * Накопитель и обработчик текущего хода
     */
    private final MoveConstructor currentMove;

    /**
     * Состояние доски в начале игры
     */
    private final static byte[] NEW_GAME_BOARD = {
            2, 3, 4, 5, 6, 4, 3, 2,
            1, 1, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, -1,
            -2, -3, -4, -5, -6, -4, -3, -2,
    };

    /**
     * Пустая доска при отсутствии игры
     */
    private final static byte[] EMPTY_BOARD = new byte[0];

    /**
     * Сообщения о ходе определённой стороны
     */
    private final static String[] MOVING_SIDES = {"Ход белых", "Ход чёрных"};
    /**
     * Пригласительное сообщение к ходу
     */
    private final static String YOUR_MOVE = "Ваш ход: ";
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
     * Массив названий игровых фигур
     */
    private final static String[] FIGURES = {"НЕИЗВЕСТНО", "ПЕШКА", "ЛАДЬЯ",
            "КОНЬ", "СЛОН", "ФЕРЗЬ", "КОРОЛЬ"};
    /**
     * Число клеток на доске
     */
    private final static int SQUARES_COUNT = NEW_GAME_BOARD.length;
    /**
     * Число клеток в одном ряду
     */
    private final static int SQUARES_IN_A_ROW = (int) Math.sqrt(SQUARES_COUNT);

    /**
     * Текущее состояние доски
     */
    private byte[] chessboard;
    /**
     * Ходят ли сейчас белые
     */
    private boolean whiteToMove;
    /**
     * Текущее состояние игры
     */
    private STATES currentState;

    /**
     * Состояния, в которых может находиться игра
     */
    public static enum STATES {
        /**
         * Игра не идёт
         */
        NOGAME,
        /**
         * Игра идёт
         */
        INGAME
    }

    /**
     * Варианты состояния хода
     */
    public static enum MOVE_PROPERTIES {
        /**
         * Обыкновенный ход
         */
        REGULAR,
        /**
         * Невозможный ход
         */
        IMPOSSIBLE,
        /**
         * Неверная запись хода
         */
        INVALID,
        /**
         * Ход содержит шах
         */
        CHECK,
        /**
         * Ход содержит мат
         */
        CHECKMATE
    }

    /**
     * Конструктор класса, ставит пустую доску, ход белых и состояние
     * без игры
     */
    public GameState() {
        chessboard = new byte[SQUARES_COUNT];
        Arrays.fill(chessboard, (byte) 0);
        whiteToMove = true;
        currentState = STATES.NOGAME;
        currentMove = new MoveConstructor();
    }

    /**
     * Получить текущее состояние доски в виде массива кодов
     */
    public byte[] getBoard() {
        return chessboard;
    }

    /**
     * Сейчас в состоянии игры
     */
    public boolean isInGame() {
        return currentState.equals(STATES.INGAME);
    }

    /**
     * Сейчас не в состоянии игры
     */
    public boolean isNoGame() {
        return currentState.equals(STATES.NOGAME);
    }

    /**
     * Ходят ли сейчас белые
     */
    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    /**
     * Получить список кнопок для следующей части хода
     */
    public List<IdentificatedButton> nextButtons() {
        return currentMove.nextButtonsLine(
                chessboard, whiteToMove, FIGURES, LETTERS, DIGITS, gameHandler);
    }

    /**
     * Полностью ли собран ход
     */
    public boolean isMoveReady() {
        return currentMove.getStatus().equals(MoveConstructor.STATUS.FINISH);
    }

    /**
     * Собрать текущий ход
     *
     * @return ход в текстовом представлении, которое поймёт MainLogic
     */
    public String assembleMove() {
        return currentMove.flushMove(FIGURES_SYMBOLS, LETTERS, DIGITS, SQUARES_IN_A_ROW);
    }

    /**
     * Получить текущее состояние доски в виде строки для печати
     */
    public String printBoard(MOVE_PROPERTIES moveProperty) {
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
                    board += FIGURE_COLOR[1];
                } else if (chessboard[i] < 0) {
                    board += FIGURE_COLOR[0];
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
                    board += FIGURE_COLOR[1];
                } else if (chessboard[i] < 0) {
                    board += FIGURE_COLOR[0];
                }
                board += FIGURES_SYMBOLS[Math.abs(chessboard[i])] + "]";
            }
        }
        if (moveProperty.equals(MOVE_PROPERTIES.CHECKMATE)) {
            additional = CHECKMATE_MOVE + (whiteToMove ? WHITE_WIN : BLACK_WIN);
            move = "";
        } else {
            if (moveProperty.equals(MOVE_PROPERTIES.IMPOSSIBLE)) {
                additional = IMPOSSIBLE_MOVE;
            } else if (moveProperty.equals(MOVE_PROPERTIES.INVALID)) {
                additional = INVALID_MOVE;
            } else if (moveProperty.equals(MOVE_PROPERTIES.CHECK)) {
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
                        DIGITS[(SQUARES_IN_A_ROW -
                                currentMove.getStartPosition() / SQUARES_IN_A_ROW) - 1];
            }
            if (currentMove.getFinishPosition() >= 0) {
                finishPosition = LETTERS[currentMove.getFinishPosition() % SQUARES_IN_A_ROW] +
                        DIGITS[(SQUARES_IN_A_ROW -
                                currentMove.getFinishPosition() / SQUARES_IN_A_ROW) - 1];
            }
        }
        chessboardString = """
                %s
                
                %s
                
                %s
                %s %s %s %s
                """.formatted(side,
                board,
                additional,
                move, figure, startPosition, finishPosition);
        return chessboardString;
    }

    /**
     * Переставить фигуру на доски из startPosition в finishPosition,
     * в startPosition ставится 0
     */
    public void moveFigure(int startPosition, int finishPosition) {
        if (startPosition >= 0 && startPosition < SQUARES_COUNT &&
                finishPosition >= 0 && finishPosition < SQUARES_COUNT) {
            chessboard[finishPosition] = chessboard[startPosition];
            chessboard[startPosition] = 0;
        }
    }

    /**
     * Установить состояние доски из внешнего массива кодов фигур
     * (не рекомендуется для совершения ходов)
     */
    public void setBoard(byte[] newBoard) {
        int newBoardSquaresCount = newBoard.length;
        int i = 0;
        for (; i < SQUARES_COUNT && i < newBoardSquaresCount; ++i) {
            chessboard[i] = newBoard[i];
        }
        for (; i < SQUARES_COUNT; ++i) {
            chessboard[i] = 0;
        }
    }

    /**
     * Установить новое состояние игры
     */
    public void setState(STATES newState) {
        currentState = newState;
        switch (newState) {
            case STATES.NOGAME:
                setBoard(EMPTY_BOARD);
                break;
            case STATES.INGAME:
                setBoard(NEW_GAME_BOARD);
                break;
        }
        whiteToMove = true;
        currentMove.clear();
    }

    /**
     * Изменить ходящую сторону
     */
    public void changeMovingSide() {
        whiteToMove = !whiteToMove;
    }

    /**
     * Поменять состояние готовности хода
     *
     * @param newMovePart новая часть хода, в зависимости от текущего
     *                    сотояния готовности хода может быть фигурой, начальной или конечной
     *                    позицией
     */
    public void changeMoveState(int newMovePart) {
        currentMove.nextStatus(newMovePart);
    }
}
