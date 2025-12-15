package org.example.statesHandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.chess.PositionOnBoard;
import org.example.states.LobbyState;
import org.example.states.LobbyState.LobbyType;
import org.example.states.UserState;
import org.example.states.UserState.MessengerType;
import org.example.states.UserState.UserStatus;

/**
 * Хранитель и обработчик состояний пользователей, использующий память
 */
public class MemoryStatesHandler implements StatesHandler {
    /**
     * Длинна стороны доски
     */
    private final static int BOARD_SIDE_LENGTH = 7;
    /**
     * Наибольший идентификатор во внутренней системе
     */
    protected long highestId = 1;

    /**
     * Ассоциативный массив с соответствием идентификатора неизвестного и
     * мессенджера идентификатора пользователя внутренней системы
     */
    protected Map<Long, Long> unknownIds = new HashMap<Long, Long>();

    /**
     * Ассоциативный массив с соответствием идентификатора Telegram и
     * идентификатора пользователя внутренней системы
     */
    protected Map<Long, Long> telegramIds = new HashMap<Long, Long>();

    /**
     * Ассоциативный массив с соответствием идентификатора Discord и
     * идентификатора пользователя внутренней системы
     */
    protected Map<Long, Long> discordIds = new HashMap<Long, Long>();

    /**
     * Ассоциативный массив с соответствием идентификатора внутренней
     * системы и ассоциативным массивом с идентификаторами мессенджеров
     */
    protected Map<Long, Map<UserState.MessengerType, Long>> messengersIds =
            new HashMap<Long, Map<UserState.MessengerType, Long>>();

    /**
     * Ассоциативный массив с соответствием идентификатора пользователя и
     * его состояния
     */
    protected Map<Long, UserState> users = new HashMap<Long, UserState>();

    /**
     * Ассоциативный массив с соответствием идентификатора матча и его
     * состояния
     */
    protected Map<String, LobbyState> games = new HashMap<String, LobbyState>();

    /**
     * Ассоциативный массив с соответствием идентификатора пользователя и
     * идентификатора последнего отправленного ему сообщения
     */
    protected Map<Long, Long> messages = new HashMap<Long, Long>();

    /**
     * Ассоциативный массив с соответствием названия ещё не начавшегося
     * матча и идентификатора его создателя
     */
    protected Map<String, Long> names = new HashMap<String, Long>();

    @Override
    public ImmutablePair<String, Double> getUserRating(long userId) {
        return users.get(userId).getUserStatistic();
    }

    @Override
    public void setNewUserStatus(long userId, UserStatus status) {
        if (users.containsKey(userId)) {
            users.get(userId).setUserState(status);
        }
    }

    @Override
    public String getUserLobbyName(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId).getCurrentLobbyId();
        }
        return null;
    }

    @Override
    public void resetUserLobbyName(long userId) {
        if (users.containsKey(userId)) {
            users.get(userId).resetLobbyId();
        }
    }

    @Override
    public void createNewLobby(String lobbyName, long firstPlayerId, long secondPlayerId,
                               boolean isFirstPlayerWhite, LobbyType lobbyType,
                               byte[][] chessboard, int sideLength, boolean isWhiteToMove) {
        games.put(lobbyName, new LobbyState(firstPlayerId, secondPlayerId,
                isFirstPlayerWhite, lobbyType,
                chessboard, sideLength, isWhiteToMove)
        );
    }

    @Override
    public LobbyType getLobbyType(String lobbyName) {
        if (games.containsKey(lobbyName)) {
            return games.get(lobbyName).getLobbyType();
        }
        return null;
    }

    @Override
    public void deleteLobby(String lobbyName) {
        games.remove(lobbyName);
    }

    @Override
    public long getLobbyAnotherUserId(String lobbyName, long userId) {
        if (games.containsKey(lobbyName)) {
            return games.get(lobbyName).getAnotherPlayerId(userId);
        }
        return 0;
    }

    @Override
    public void bookLobbyName(String lobbyName, long creatorId) {
        names.put(lobbyName, creatorId);
    }

    @Override
    public void unbookLobbyName(String lobbyName) {
        names.remove(lobbyName);
    }

    @Override
    public long getLobbyCreator(String argument) {
        return names.getOrDefault(argument, (long) 0);
    }

    @Override
    public void setUserLobbyName(long userId, String lobbyName) {
        if (users.containsKey(userId)) {
            users.get(userId).setCurrentLobbyId(lobbyName);
        }
    }

    @Override
    public boolean isLobbyExisting(String lobbyName) {
        return names.containsKey(lobbyName) || games.containsKey(lobbyName);
    }

    @Override
    public boolean isLobbyAvailable(String lobbyName) {
        return names.containsKey(lobbyName);
    }

    @Override
    public void addUserNewMovePart(long userId, String movePart) {
        if (users.containsKey(userId)) {
            users.get(userId).getMoveState().nextStatus(movePart);
        }
    }

    @Override
    public String getUserMovePartFigure(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId).getMoveState().getFigure();
        }
        return "";
    }

    @Override
    public String getUserMovePartStart(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId).getMoveState().getStartPosition();
        }
        return "";
    }

    @Override
    public String getUserMovePartFinish(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId).getMoveState().getFinishPosition();
        }
        return "";
    }

    @Override
    public boolean isUserMoveReady(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId).getMoveState().isMoveReady();
        }
        return false;
    }

    @Override
    public boolean isGameWhiteToMove(String lobbyName) {
        if (games.containsKey(lobbyName)) {
            return games.get(lobbyName).getGameState().isWhiteToMove();
        }
        return false;
    }

    @Override
    public byte[][] getGameChessboard(String lobbyName) {
        if (games.containsKey(lobbyName)) {
            return games.get(lobbyName).getGameState().getBoard();
        }
        return null;
    }

    @Override
    public void changeLobbyMovingUser(String lobbyName) {
        if (games.containsKey(lobbyName)) {
            games.get(lobbyName).changeMovingPlayer();
        }
    }

    @Override
    public void moveGameFigure(String lobbyName, PositionOnBoard startPosition,
                               PositionOnBoard finishPosition) {
        if (games.containsKey(lobbyName)) {
            games.get(lobbyName).getGameState().moveFigure(startPosition, finishPosition);
        }

    }

    @Override
    public void changeGameMovingSide(String lobbyName) {
        if (games.containsKey(lobbyName)) {
            games.get(lobbyName).getGameState().changeSide();
        }
    }

    @Override
    public long addNewUser(MessengerType newUserMessenger, String userName) {
        long userId = highestId++;
        users.put(userId, new UserState(newUserMessenger, userName));
        Map<UserState.MessengerType, Long> messengers =
                new HashMap<UserState.MessengerType, Long>();
        messengersIds.put(userId, messengers);
        return userId;
    }

    @Override
    public UserStatus getUserStatus(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId).getUserStatus();
        }
        return null;
    }

    @Override
    public List<ImmutablePair<String, Double>> getBookedLobbies(long userId) {
        //TODO
        return null;
		/*
		return List.copyOf(names.keySet());
		*/
    }

    @Override
    public long getLobbyFirstPlayer(String lobbyName) {
        if (games.containsKey(lobbyName)) {
            return games.get(lobbyName).getFirstPlayerId();
        }
        return 0;
    }

    @Override
    public long getLobbySecondPlayer(String lobbyName) {
        if (games.containsKey(lobbyName)) {
            return games.get(lobbyName).getSecondPlayerId();
        }
        return 0;
    }

    @Override
    public boolean isLobbyFirstPlayerToMove(String lobbyName) {
        if (games.containsKey(lobbyName)) {
            return games.get(lobbyName).isFirstPlayerToMove();
        }
        return false;
    }

    @Override
    public void changeUserLastMessage(long userId, long lastMessageId) {
        messages.put(userId, lastMessageId);
    }

    @Override
    public long getUserMessageId(long userId) {
        return messages.getOrDefault(userId, (long) -1);
    }

    @Override
    public void resetUserMoveState(long userId) {
        if (users.containsKey(userId)) {
            users.get(userId).getMoveState().clearMoveState();
        }
    }

    @Override
    public MessengerType getUserMessenger(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId).getUserMessenger();
        }
        return null;
    }

    @Override
    public long getUserMessengerId(long userId, MessengerType userMessenger) {
        if (messengersIds.containsKey(userId) &&
                messengersIds.get(userId).containsKey(userMessenger)) {
            return messengersIds.get(userId).get(userMessenger);
        }
        return userId;
    }

    @Override
    public long getUserIdFromTelegramId(long chatId) {
        if (telegramIds.containsKey(chatId)) {
            return telegramIds.get(chatId);
        }
        return 0;
    }

    @Override
    public long getUserIdFromDiscordId(long chatId) {
        if (discordIds.containsKey(chatId)) {
            return discordIds.get(chatId);
        }
        return 0;
    }

    @Override
    public void addNewMessengerId(long userId, MessengerType newUserMessenger, long chatId) {
        if (messengersIds.containsKey(userId)) {
            messengersIds.get(userId).put(newUserMessenger, chatId);
            switch (newUserMessenger) {
                case UserState.MessengerType.TELEGRAM -> {
                    telegramIds.put(chatId, userId);
                }
                case UserState.MessengerType.DISCORD -> {
                    discordIds.put(chatId, userId);
                }
            }
        }
    }

    @Override
    public boolean isMessengerIdExisting(MessengerType messenger, long chatId) {
        switch (messenger) {
            case UserState.MessengerType.TELEGRAM -> {
                return telegramIds.containsKey(chatId);
            }
            case UserState.MessengerType.DISCORD -> {
                return discordIds.containsKey(chatId);
            }
        }
        return false;
    }

    @Override
    public List<ImmutablePair<String, Double>> getTopTenUsers() {
        return List.of();
    }

    @Override
    public void addUserLose(long secondId) {
        users.get(secondId).updatePlayedGames();
    }

    @Override
    public void addUserWin(long userId) {
        users.get(userId).updatePlayedGames();
        users.get(userId).updateWonGames();
    }

    @Override
    public int getGameSideLength(String lobbyName) {
        return BOARD_SIDE_LENGTH;
    }
}
