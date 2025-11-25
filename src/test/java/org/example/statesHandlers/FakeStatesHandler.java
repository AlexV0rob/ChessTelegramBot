package org.example.statesHandlers;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.states.LobbyState;
import org.example.states.UserState;

/**
 * Тестовый хранитель состояний
 */
public class FakeStatesHandler extends MemoryStatesHandler {
	/**
	 * Добавить пользователя с определённым состоянием
	 */
	public long addNewUserWithStatus(UserState.UserStatus status) {
		long userId = addNewUser(null);
		setNewUserStatus(userId, status);
		return userId;
	}
	
	/**
	 * Добавить стандартную однопользовательскую игру
	 */
	public long createStandardSingleGame(String lobbyName) {
		long userId = addNewUserWithStatus(UserState.UserStatus.INGAME);
		setUserLobbyName(userId, lobbyName);
		createNewLobby(lobbyName, userId, userId, true, LobbyState.LobbyType.SINGLEPLAYER);
		return userId;
	}
	
	/**
	 * Добавить стандартную многопользовательскую игру
	 */
	public ImmutablePair<Long, Long> createStandardMultiGame(String lobbyName) {
		long userId1 = addNewUserWithStatus(UserState.UserStatus.INGAME);
		long userId2 = addNewUserWithStatus(UserState.UserStatus.INGAME);
		setUserLobbyName(userId1, lobbyName);
		setUserLobbyName(userId2, lobbyName);
		createNewLobby(lobbyName, userId1, userId2, true, LobbyState.LobbyType.MULTIPLAYER);
		return new ImmutablePair<>(userId1, userId2);
	}
	
	/**
	 * Добавить стандартную однопользовательскую игру с заданной доской
	 */
	public long createStandardSingleGameWithBoard(String lobbyName, byte[][] chessboard) {
		long userId = addNewUserWithStatus(UserState.UserStatus.INGAME);
		setUserLobbyName(1, lobbyName);
		createNewLobbyWithBoard(lobbyName, userId, userId, true, 
				LobbyState.LobbyType.SINGLEPLAYER, 
				chessboard, 8, true);
		return userId;
	}
	
	/**
	 * Добавить стандартную многопользовательскую игру с заданной доской
	 */
	public ImmutablePair<Long, Long> createStandardMultiGameWithBoard(String lobbyName, byte[][] chessboard) {
		long userId1 = addNewUserWithStatus(UserState.UserStatus.INGAME);
		long userId2 = addNewUserWithStatus(UserState.UserStatus.INGAME);
		setUserLobbyName(userId1, lobbyName);
		setUserLobbyName(userId2, lobbyName);
		createNewLobbyWithBoard(lobbyName, userId1, userId2, true, 
				LobbyState.LobbyType.MULTIPLAYER, 
				chessboard, 8, true);
		return new ImmutablePair<>(userId1, userId2);
	}
	
	/**
	 * Сбросить все данные
	 */
	public void resetAll() {
		users.clear();
		games.clear();
		names.clear();
		messages.clear();
		highestId = 1;
	}
	
	private void createNewLobbyWithBoard(String lobbyName, long firstPlayerId, 
			long secondPlayerId, boolean isFirstPlayerWhite, LobbyState.LobbyType lobbyType, 
			byte[][] chessboard, int boardSideLength, boolean isWhiteToMove) {
		games.put(lobbyName, new LobbyState(
				firstPlayerId, secondPlayerId, isFirstPlayerWhite, lobbyType, 
				chessboard, boardSideLength, isWhiteToMove));
	}
}
