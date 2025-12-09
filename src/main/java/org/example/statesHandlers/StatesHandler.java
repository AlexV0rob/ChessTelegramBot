package org.example.statesHandlers;

import java.util.List;

import org.example.chess.PositionOnBoard;
import org.example.states.LobbyState;
import org.example.states.LobbyState.LobbyType;
import org.example.states.UserState;
import org.example.states.UserState.MessengerType;
import org.example.states.UserState.UserStatus;

/**
 * Хранитель и обработчик состояний пользователей
 */
public interface StatesHandler {
	/**
	 * Установить пользователю новое состояние
	 */
	void setNewUserStatus(long userId, UserStatus status);
	
	/**
	 * Получить название матча по идентификатору пользователя
	 */
	String getUserLobbyName(long userId);

	/**
	 * Сбросить название матча в состоянии пользователя
	 */
	void resetUserLobbyName(long userId);

	/**
	 * Создать новый матч и добавить его в список идущих
	 */
	void createNewLobby(String lobbyName, long firstPlayerId, long secondPlayerId, 
			boolean isFirstPlayerWhite, LobbyType lobbyType, 
			byte[][] chessboard, int sideLength, boolean isWhiteToMove);

	/**
	 * Получить тип матча
	 */
	LobbyState.LobbyType getLobbyType(String lobbyName);

	/**
	 * Удалить матч из списка идущих
	 */
	void deleteLobby(String lobbyName);

	/**
	 * Получить идентификатор второго участника матча
	 */
	long getLobbyAnotherUserId(String lobbyName, long userId);

	/**
	 * Зарезервировать название матча
	 */
	void bookLobbyName(String lobbyName, long creatorId);

	/**
	 * Удалить матч из списка зарезервированных
	 */
	void unbookLobbyName(String lobbyName);

	/**
	 * Получить идентификатор создателя матча
	 */
	long getLobbyCreator(String lobbyName);

	/**
	 * Установить в состояние пользователя новое название матча
	 */
	void setUserLobbyName(long userId, String lobbyName);

	/**
	 * Проверить, что матч с таким названием существует, то есть 
	 * идёт или зарезервирован
	 */
	boolean isLobbyExisting(String lobbyName);

	/**
	 * Проверить, что матч даступен, то есть название зарезервировано и 
	 * матч ещё не идёт
	 */
	boolean isLobbyAvailable(String lobbyName);

	/**
	 * Добавить новую часть в хранитель состояний хода пользователя
	 */
	void addUserNewMovePart(long userId, String movePart);

	/**
	 * Получить фигуру как часть хода
	 */
	String getUserMovePartFigure(long userId);

	/**
	 * Получить начальную позицию как часть хода
	 */
	String getUserMovePartStart(long userId);

	/**
	 * Получить конечную позицию как часть хода
	 */
	String getUserMovePartFinish(long userId);

	/**
	 * Проверить, что ход пользователя полностью готов
	 */
	boolean isUserMoveReady(long userId);

	/**
	 * Проверить, что в игре ходят белые
	 */
	boolean isGameWhiteToMove(String lobbyName);

	/**
	 * Получить игровую шахматную доску
	 */
	byte[][] getGameChessboard(String lobbyName);

	/**
	 * Поменять в матче ходящую сторону
	 */
	void changeLobbyMovingUser(String lobbyName);

	/**
	 * Передвинуть в игре фигуру со стартовой позиции на конечную
	 */
	void moveGameFigure(String lobbyName, 
			PositionOnBoard startPosition, PositionOnBoard finishPosition);

	/**
	 * Поменять в игре ходящую сторону
	 */
	void changeGameMovingSide(String lobbyName);

	/**
	 * Добавить нового пользователя
	 */
	long addNewUser(MessengerType newUserMessenger);

	/**
	 * Получить статус состояния пользователя
	 */
	UserState.UserStatus getUserStatus(long userId);

	/**
	 * Получить список всех зарезервированных матчей
	 */
	List<String> getBookedLobbies();

	/**
	 * Получить идентфикатор первого игрока матча
	 */
	long getLobbyFirstPlayer(String lobbyName);

	/**
	 * Получить идентфикатор второго игрока матча
	 */
	long getLobbySecondPlayer(String lobbyName);
	
	/**
	 * Проверить, что сейчас в матче ходит первый игрок
	 */
	boolean isLobbyFirstPlayerToMove(String lobbyName);

	/**
	 * Поменять идентификатор последнего сообщения, отправленного пользователю
	 */
	void changeUserLastMessage(long userId, long lastMessageId);

	/**
	 * Получить идентификатор последнего сообщения, отправленного польззователю
	 */
	long getUserMessageId(long userId);

	/**
	 * Сбросить накопленное состояние хода пользователя
	 */
	void resetUserMoveState(long userId);

	/**
	 * Получить текущий мессенджер пользователя
	 */
	MessengerType getUserMessenger(long userId);

	/**
	 * Получить идентификатор пользователя в данном мессенджере
	 */
	long getUserMessengerId(long userId, MessengerType userMessenger);
	
	/**
	 * Получить идентификатор пользователя во внутренней системе 
	 * из идентификатора Telegram
	 */
	long getUserIdFromTelegramId(long chatId);
	
	/**
	 * Получить идентификатор пользователя во внутренней системе 
	 * из идентификатора Discord
	 */
	long getUserIdFromDiscordId(long chatId);

	/**
	 * Добавить новый идентификатор мессенджера
	 */
	void addNewMessengerId(long userId, MessengerType newUserMessenger, long chatId);

	/**
	 * Проверить, что в таком мессенджере такой идентификатор числится
	 */
	boolean isMessengerIdExisting(MessengerType messenger, long chatId);

	/**
	 * Получить длину стороны доски в игре
	 */
	int getGameSideLength(String lobbyName);
}
