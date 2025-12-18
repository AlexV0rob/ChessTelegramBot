package org.example;

import org.apache.commons.lang3.tuple.ImmutablePair;
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
     * Конвертер текстов кнопок
     */
    private final MenuButtonsConverter menuButtonsConverter = new MenuButtonsConverter();

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
    private final List<Chessmen> figures = List.of(
            new Pawn(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Rook(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Knight(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Bishop(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new Queen(MIN_SIDE_VALUE, MAX_SIDE_VALUE),
            new King(MIN_SIDE_VALUE, MAX_SIDE_VALUE)
    );


    /**
     * Получить список кнопок главного меню
     */
    public List<SimpleButton> getMenuButtons() {
        return List.of(
                new SimpleButton(menuButtonsConverter.getCommandText("new_local")),
                new SimpleButton(menuButtonsConverter.getCommandText("create")),
                new SimpleButton(menuButtonsConverter.getCommandText("join")),
                new SimpleButton(menuButtonsConverter.getCommandText("leadertable")));
    }

    /**
     * Получить список кнопок ожидания начала матча
     */
    public List<SimpleButton> getAwaitingButtons() {
        return List.of(new SimpleButton(menuButtonsConverter.getCommandText("quit")));
    }

    /**
     * Получить список кнопок доступных лобби
     */
    public List<SimpleButton> getLinkButtonns() {

        return List.of(
                new SimpleButton(menuButtonsConverter.getCommandText("link")),
                new SimpleButton(menuButtonsConverter.getCommandText("new_messenger")));

    }

    /**
     * Получить список кнопок доступных лобби
     */
    public List<IdentifiedButton> getLobbyButtons(List<ImmutablePair<String, Double>> listOfLobbies) {
        List<IdentifiedButton> lobbyButtons = new ArrayList<IdentifiedButton>();
        for (ImmutablePair<String, Double> lobby : listOfLobbies) {
            lobbyButtons.add(
            		new IdentifiedButton(
            				"__" + lobby.getLeft() + "__", 
            				"%s %.4f".formatted(lobby.getLeft(), lobby.getRight())));
        }
        return lobbyButtons;
    }

    /**
     * Получить список кнопок игры
     */
    public List<IdentifiedButton> getGameButtons(String figure, String start,
                                                 String finish, byte[][] chessboard, boolean isWhiteToMove) {
        if (!finish.isEmpty()) {
            return List.of();
        }
        if (!start.isEmpty()) {
            int figureCode = movePartsConverter.getFigureCode(figure);
            int startPositionRow = movePartsConverter
                    .getPositionRowCode(start.charAt(1));
            int startPositionColumn = movePartsConverter
                    .getPositionColumnCode(start.charAt(0));
            List<IdentifiedButton> moves = findPossibleFigureMoves(chessboard, figureCode,
                    new PositionOnBoard(startPositionRow, startPositionColumn));
            if (moves.isEmpty()) {
                return List.of(new IdentifiedButton("__cancel__", "Сбросить"));
            } else {
                return moves;
            }
        }
        if (!figure.isEmpty()) {
            int figureCode = movePartsConverter.getFigureCode(figure);
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
        boolean[] isFigureOnBoard = new boolean[figures.size()];
        for (int i = 0; i < figures.size(); ++i) {
            isFigureOnBoard[i] = false;
        }
        for (byte[] currentChessboardRow : chessboard) {
            for (int currentFigureCode : currentChessboardRow) {
                if (currentFigureCode != 0 && (currentFigureCode < 0) == isWhiteToMove) {
                    isFigureOnBoard[Math.abs(currentFigureCode) - 1] = true;
                }
            }
        }
        for (int i = 0; i < figures.size(); ++i) {
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
    private List<IdentifiedButton> findPossibleFigureMoves(
            byte[][] chessboard, int figureCode, PositionOnBoard startPosition) {
        List<PositionOnBoard> possiblePositions =
                figures.get(figureCode - 1).allPossibleMoves(startPosition, chessboard);
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
