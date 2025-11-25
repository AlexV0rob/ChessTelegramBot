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
	public void setNewUserStatus(long userId, UserStatus status);
	
	/**
	 * Получить название матча по идентификатору пользователя
	 * @return Название или пустую строку, если не найдено
	 */
	public String getUserLobbyName(long userId);

	/**
	 * Сбросить название матча в состоянии пользователя
	 */
	public void resetUserLobbyName(long userId);

	/**
	 * Создать новый матч и добавить его в список идущих
	 */
	public void createNewLobby(String lobbyName, long firstPlayerId, 
			long secondPlayerId, boolean isFirstPlayerWhite, LobbyType lobbyType);

	/**
	 * Получить тип матча
	 */
	public LobbyState.LobbyType getLobbyType(String lobbyName);

	/**
	 * Удалить матч из списка идущих
	 */
	public void deleteLobby(String lobbyName);

	/**
	 * Получить идентификатор второго участника матча
	 */
	public long getLobbyAnotherUserId(String lobbyName, long userId);

	/**
	 * Зарезервировать название матча
	 */
	public void bookLobbyName(String lobbyName, long creatorId);

	/**
	 * Удалить матч из списка зарезервированных
	 */
	public void unbookLobbyName(String lobbyName);

	/**
	 * Получить идентификатор создателя матча
	 */
	public long getLobbyCreator(String lobbyName);

	/**
	 * Установить в состояние пользователя новое название матча
	 */
	public void setUserLobbyName(long userId, String lobbyName);

	/**
	 * Проверить, что матч с таким названием существует, то есть 
	 * идёт или зарезервирован
	 */
	public boolean isLobbyExisting(String lobbyName);

	/**
	 * Проверить, что матч даступен, то есть название зарезервировано и 
	 * матч ещё не идёт
	 */
	public boolean isLobbyAvailable(String lobbyName);

	/**
	 * Добавить новую часть в хранитель состояний хода пользователя
	 */
	public void addUserNewMovePart(long userId, String movePart);

	/**
	 * Получить фигуру как часть хода
	 */
	public String getUserMovePartFigure(long userId);

	/**
	 * Получить начальную позицию как часть хода
	 */
	public String getUserMovePartStart(long userId);

	/**
	 * Получить конечную позицию как часть хода
	 */
	public String getUserMovePartFinish(long userId);

	/**
	 * Проверить, что ход пользователя полностью готов
	 */
	public boolean isUserMoveReady(long userId);

	/**
	 * Проверить, что в игре ходят белые
	 */
	public boolean isGameWhiteToMove(String lobbyName);

	/**
	 * Получить игровую шахматную доску
	 */
	public byte[][] getGameChessboard(String lobbyName);

	/**
	 * Поменять в матче ходящую сторону
	 */
	public void changeLobbyMovingUser(String lobbyName);

	/**
	 * Передвинуть в игре фигуру со стартовой позиции на конечную
	 */
	public void moveGameFigure(String lobbyName, 
			PositionOnBoard startPosition, PositionOnBoard finishPosition);

	/**
	 * Поменять в игре ходящую сторону
	 */
	public void changeGameMovingSide(String lobbyName);

	/**
	 * Добавить нового пользователя
	 */
	public long addNewUser(MessengerType newUserMessenger);

	/**
	 * Получить статус состояния пользователя
	 */
	public UserState.UserStatus getUserStatus(long userId);

	/**
	 * Получить список всех зарезервированных матчей
	 */
	public List<String> getBookedLobbies();

	/**
	 * Получить идентфикатор первого игрока матча
	 */
	public long getLobbyFirstPlayer(String lobbyName);

	/**
	 * Получить идентфикатор второго игрока матча
	 */
	public long getLobbySecondPlayer(String lobbyName);
	
	/**
	 * Проверить, что сейчас в матче ходит первый игрок
	 */
	public boolean isLobbyFirstPlayerToMove(String lobbyName);

	/**
	 * Поменять идентификатор последнего сообщения, отправленного пользователю
	 */
	public void changeUserLastMessage(long userId, long lastMessageId);

	/**
	 * Получить идентификатор последнего сообщения, отправленного польззователю
	 */
	public long getUserMessageId(long userId);

	/**
	 * Сбросить накопленное состояние хода пользователя
	 */
	public void resetUserMoveState(long userId);

	/**
	 * Получить текущий мессенджер пользователя
	 */
	public MessengerType getUserMessenger(long userId);

	/**
	 * Получить идентификатор пользователя в данном мессенджере
	 */
	public long getUserMessengerId(long userId, MessengerType userMessenger);

	/**
	 * Получить идентификатор пользователя во внутренней системе 
	 * из идентификатора неизвестного мессенджера
	 */
	public long getUserIdFromUnknownId(long chatId);
	
	/**
	 * Получить идентификатор пользователя во внутренней системе 
	 * из идентификатора Telegram
	 */
	public long getUserIdFromTelegramId(long chatId);
	
	/**
	 * Получить идентификатор пользователя во внутренней системе 
	 * из идентификатора Discord
	 */
	public long getUserIdFromDiscordId(long chatId);

	/**
	 * Добавить новый идентификатор мессенджера
	 */
	public void addNewMessengerId(long userId, MessengerType newUserMessenger, long chatId);

	/**
	 * Проверить, что в таком мессенджере такой идентификатор числится
	 */
	public boolean isMessengerIdExisting(MessengerType messenger, long chatId);
}
