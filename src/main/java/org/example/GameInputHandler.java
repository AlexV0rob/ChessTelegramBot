package org.example;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.chess.GameHandler;
import org.example.chess.PositionOnBoard;
import org.example.states.LobbyState;
import org.example.states.UserState.UserStatus;
import org.example.statesHandlers.StatesHandler;;

/**
 * Обработчик игрового ввода
 */
public class GameInputHandler {
	/**
	 * Обработчик состояний
	 */
	private final StatesHandler states;
	
    /**
     * Обработчик игры
     */
    private final GameHandler gameHandler = new GameHandler();

    /**
     * Переводчик игры для вывода текста пользователю
     */
    private final GameTranslator gameTranslator = new GameTranslator();
    
    /**
	 * Пригласительное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Сообщение о ходе оппонента
	 */
	private final static String NOT_YOUR_MOVE = "Сейчас ходит противник.";
	
    /**
     * Сообщение в меню
     */
    private final static String MENU_MESSAGE = "Чем займёмся?";
    
    /**
     * Конвертер частей хода
     */
    private final MovePartsConverter movePartsConverter = new MovePartsConverter();
	public GameInputHandler(StatesHandler statesHandler) {
		states = statesHandler;
	}

	/**
	 * Обработать часть хода
	 */
	public ImmutablePair<List<String>, List<String>> processMovePart(
			long userId, String movePart) {
		ImmutablePair<List<String>, List<String>> responseMessages = 
				new ImmutablePair<>(new ArrayList<String>(), new ArrayList<String>());
		if (!movePart.isEmpty()) {
			if (movePart.equals("cancel")) {
				states.resetUserMoveState(userId);
			} else {
				states.addUserNewMovePart(userId, movePart);
			}
		}
		String currentFigure = states.getUserMovePartFigure(userId);
		String currentStartPosition = states.getUserMovePartStart(userId);
		String currentFinishPosition = states.getUserMovePartFinish(userId);
		String moveMessage = YOUR_MOVE;
		if (!currentFigure.isEmpty()) {
			moveMessage += movePartsConverter.getFigureName(currentFigure);
		}
		if (!currentStartPosition.isEmpty()) {
			moveMessage += " " + currentStartPosition.toUpperCase();
		}
		if (!currentFinishPosition.isEmpty()) {
			moveMessage += " " + currentFinishPosition.toUpperCase();
		}
		responseMessages.getKey().add(moveMessage);
		if (states.isUserMoveReady(userId)) {
			ImmutablePair<List<String>, List<String>> moveMessages = processMove(
					userId,
					currentFigure, 
					currentStartPosition, 
					currentFinishPosition);
			responseMessages.getKey().addAll(moveMessages.getKey());
			responseMessages.getValue().addAll(moveMessages.getValue());
		}
		return responseMessages;
	}

	/**
	 * Обработать ход, введённый целиком
	 */
	public ImmutablePair<List<String>, List<String>> processMove(
			long userId, String figure, String start, String finish) {
		states.resetUserMoveState(userId);
		String lobbyName = states.getUserLobbyName(userId);
		boolean isSingle = states.getLobbyType(lobbyName)
				.equals(LobbyState.LobbyType.SINGLEPLAYER);
		List<String> responseMessagesFirst = new ArrayList<String>();
		List<String> responseMessagesSecond = new ArrayList<String>();
		boolean viewSide = states.isGameWhiteToMove(lobbyName);
		GameHandler.MoveProperty moveProperty = null;
		if (figure.isEmpty() && start.isEmpty() && finish.isEmpty()) {
			moveProperty = GameHandler.MoveProperty.START;
		} else {
			moveProperty = makeMove(figure, start, finish, lobbyName);
		}
		GameHandler.MoveProperty firstPlayerProperty = moveProperty;
		if (moveProperty.equals(GameHandler.MoveProperty.CHECK)) {
			firstPlayerProperty = GameHandler.MoveProperty.REGULAR;			
		}
		responseMessagesFirst.add(gameTranslator.chessboardString(
				firstPlayerProperty, 
				states.getGameChessboard(lobbyName),
        		states.isGameWhiteToMove(lobbyName), 
				viewSide));
		if (!moveProperty.equals(GameHandler.MoveProperty.IMPOSSIBLE) && 
				!moveProperty.equals(GameHandler.MoveProperty.INVALID)) {
			responseMessagesSecond.add(gameTranslator.chessboardString(
					moveProperty, 
					states.getGameChessboard(lobbyName),
	        		states.isGameWhiteToMove(lobbyName), 
					!viewSide));			
		}
		switch (moveProperty) {
		case GameHandler.MoveProperty.IMPOSSIBLE,
		GameHandler.MoveProperty.INVALID -> {
			responseMessagesFirst.add(YOUR_MOVE);
		}
		case GameHandler.MoveProperty.START -> {
			responseMessagesFirst.add(YOUR_MOVE);
			responseMessagesSecond.add(NOT_YOUR_MOVE);
		}
		case GameHandler.MoveProperty.REGULAR, 
		GameHandler.MoveProperty.CHECK -> {
			responseMessagesFirst.add(NOT_YOUR_MOVE);
			states.changeLobbyMovingUser(lobbyName);
			responseMessagesSecond.add(YOUR_MOVE);
		}
		case GameHandler.MoveProperty.MATE -> {
			states.setNewUserStatus(userId, UserStatus.MAINMENU);
			states.resetUserLobbyName(userId);
			long secondId = states.getLobbyAnotherUserId(lobbyName, userId);
			states.setNewUserStatus(secondId, UserStatus.MAINMENU);
			states.resetUserLobbyName(secondId);
			if (states.getLobbyType(lobbyName).equals(LobbyState.LobbyType.MULTIPLAYER)) {
				states.addUserLose(secondId);
				states.addUserWin(userId);
			}
			states.deleteLobby(lobbyName);
			responseMessagesFirst.add(MENU_MESSAGE);
			responseMessagesSecond.add(MENU_MESSAGE);
		}
		}
		boolean isSuccessfulMove = moveProperty.equals(GameHandler.MoveProperty.REGULAR)
				|| moveProperty.equals(GameHandler.MoveProperty.CHECK);
		if (isSingle && isSuccessfulMove ) {
			return new ImmutablePair<>(responseMessagesSecond, responseMessagesFirst);
		} else {
			return new ImmutablePair<>(responseMessagesFirst, responseMessagesSecond);
		}
	}

	private GameHandler.MoveProperty makeMove(String figure, String start, 
    		String finish, String lobbyName) {
		int figureCode = movePartsConverter
        		.getFigureCode(
        				figure);
        int startPositionRow = movePartsConverter
        		.getPositionRowCode(
        				start.charAt(1));
        int startPositionColumn = movePartsConverter
        		.getPositionColumnCode(
        				start.charAt(0));
        int finishPositionRow = movePartsConverter
        		.getPositionRowCode(
        				finish.charAt(1));
        int finishPositionColumn = movePartsConverter
        		.getPositionColumnCode(
        				finish.charAt(0));
        GameHandler.MoveProperty moveProperty =  gameHandler.processMove(
        		figureCode - 1, 
        		new PositionOnBoard(startPositionRow, startPositionColumn), 
        		new PositionOnBoard(finishPositionRow, finishPositionColumn), 
        		states.getGameChessboard(lobbyName),
        		states.isGameWhiteToMove(lobbyName));
        if (!moveProperty.equals(GameHandler.MoveProperty.IMPOSSIBLE) && 
        		!moveProperty.equals(GameHandler.MoveProperty.INVALID)) {
        	states.moveGameFigure(
        			lobbyName, 
					new PositionOnBoard(
							startPositionRow, 
							startPositionColumn), 
					new PositionOnBoard(
							finishPositionRow, 
							finishPositionColumn));
        	if (!moveProperty.equals(GameHandler.MoveProperty.MATE)) {
        		states.changeGameMovingSide(lobbyName);
        	}
        }
        return moveProperty;
	}
}
