package org.example;

import org.example.auxiliary.IdentifiedButton;
import org.example.auxiliary.SimpleButton;
import org.example.chess.Bishop;
import org.example.chess.Chessmen;
import org.example.chess.King;
import org.example.chess.Knight;
import org.example.chess.Pawn;
import org.example.chess.PositionOnBoard;
import org.example.chess.Queen;
import org.example.chess.Rook;

import org.example.states.MoveState;

import java.util.ArrayList;
import java.util.List;

/**
 * Создатель кнопок
 */
public class ButtonsCreator {
    /**
     * Конвертер частей хода
     */
    private final MovePartsConverter movePartsConverter = new MovePartsConverter();

    /**
     * Текст кнопки для начала игры в главном меню
     */
    private final static String NEW_SINGLE_GAME = "Начать игру на этом устройстве";
    
    /**
     * Текст кнопки для выхода из режима ожидания
     */
    private final static String QUIT_LOBBY = "Отменить поиск матча и удалить лобби";

    /**
     * Минимальная позиция в стороне доски
     */
    private final static int MIN_SIDE_VALUE = 0;
    /**
     * Максимальная позиция в стороне доски
     */
    private final static int MAX_SIDE_VALUE = 7;

    /**
     * Список фигур для получения всех возможных ходов
     */
    private final static Chessmen[] FIGURES = {
            new Pawn(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Rook(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Knight(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Bishop(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Queen(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new King(MIN_SIDE_VALUE, MAX_SIDE_VALUE)
    };

    /**
     * Получить список кнопок главного меню
     */
    public List<SimpleButton> getMenuButtons() {
        return List.of(new SimpleButton(NEW_SINGLE_GAME));
    }
    
    /**
     * Получить список кнопок ожидания начала матча
     */
    public List<SimpleButton> getAwaitingButtons() {
    	return List.of(new SimpleButton(QUIT_LOBBY));
    }
    
    /**
     * Получить список кнопок игры
     */
    public List<IdentifiedButton> getGameButtons(MoveState currentMoveState,
                                                 byte[][] chessboard, boolean isWhiteToMove) {
        String currentFigure = currentMoveState.getFigure();
        String currentStartPosition = currentMoveState.getStartPosition();
        String currentFinishPosition = currentMoveState.getFinishPosition();
        if (!currentFinishPosition.isEmpty()) {
            return List.of();
        }
        if (!currentStartPosition.isEmpty()) {
            int figureCode = movePartsConverter.getFigureCode(currentFigure);
            int startPositionRow = movePartsConverter
                    .getPositionRowCode(currentStartPosition.charAt(1));
            int startPositionColumn = movePartsConverter
                    .getPositionColumnCode(currentStartPosition.charAt(0));
            return findPossibleFigureMoves(chessboard, figureCode,
                    new PositionOnBoard(startPositionRow, startPositionColumn));
        }
        if (!currentFigure.isEmpty()) {
            int figureCode = movePartsConverter.getFigureCode(currentFigure);
            return findFigurePositions(chessboard, figureCode, isWhiteToMove);
        }
        return findAvailableFigures(chessboard, isWhiteToMove);
    }

    /**
     * Список кнопок со всеми доступными фигурами для хода
     */
    private List<IdentifiedButton> findAvailableFigures(byte[][] chessboard,
                                                        boolean isWhiteToMove) {
        List<IdentifiedButton> availableFigures = new ArrayList<IdentifiedButton>();
        boolean[] isFigureOnBoard = new boolean[FIGURES.length];
        for (int i = 0; i < FIGURES.length; ++i) {
            isFigureOnBoard[i] = false;
        }
        for (byte[] currentChessboardRow : chessboard) {
            for (int currentFigureCode : currentChessboardRow) {
                if (currentFigureCode != 0 && (currentFigureCode < 0) == isWhiteToMove) {
                    isFigureOnBoard[Math.abs(currentFigureCode) - 1] = true;
                }
            }
        }
        for (int i = 0; i < FIGURES.length; ++i) {
            if (isFigureOnBoard[i]) {
                String currentFigureSymbol = movePartsConverter
                        .getFigureSymbol(i + 1);
                if (!currentFigureSymbol.isEmpty()) {
                    availableFigures.add(new IdentifiedButton(
                            "__" + currentFigureSymbol + "__",
                            movePartsConverter.getFigureName(currentFigureSymbol)));
                }
            }
        }
        return availableFigures;
    }

    /**
     * Список кнопок со всеми стартовыми позициями фигуры
     */
    private List<IdentifiedButton> findFigurePositions(byte[][] chessboard,
                                                       int figureCode, boolean isWhiteToMove) {
        List<IdentifiedButton> availablePositions = new ArrayList<IdentifiedButton>();
        int figureCodeSigned = figureCode * (isWhiteToMove ? -1 : 1);
        for (int i = MIN_SIDE_VALUE; i <= MAX_SIDE_VALUE; ++i) {
            for (int j = MIN_SIDE_VALUE; j <= MAX_SIDE_VALUE; ++j) {
                if (chessboard[i][j] == figureCodeSigned) {
                    char[] positionSymbols = {
                            movePartsConverter.getPositionColumnSymbol(j),
                            movePartsConverter.getPositionRowSymbol(i)
                    };
                    String positionString = String.copyValueOf(positionSymbols);
                    availablePositions.add(new IdentifiedButton(
                            "__" + positionString + "__",
                            positionString.toUpperCase()));
                }
            }
        }
        return availablePositions;
    }

    /**
     * Список кнопок со всеми возможными ходами для фигуры из стартовой позиции
     */
    private List<IdentifiedButton> findPossibleFigureMoves(byte[][] chessboard,
                                                           int figureCode, PositionOnBoard startPosition) {
        List<PositionOnBoard> possiblePositions =
                FIGURES[figureCode - 1].allPossibleMoves(startPosition, chessboard);
        List<IdentifiedButton> possibleMoves = new ArrayList<IdentifiedButton>();
        for (PositionOnBoard currentPosition : possiblePositions) {
            char[] positionSymbols = {
                    movePartsConverter.getPositionColumnSymbol(currentPosition.column()),
                    movePartsConverter.getPositionRowSymbol(currentPosition.row())
            };
            String positionString = String.copyValueOf(positionSymbols);
            possibleMoves.add(new IdentifiedButton(
                    "__" + positionString + "__",
                    positionString.toUpperCase()));
        }
        return possibleMoves;
    }
}
