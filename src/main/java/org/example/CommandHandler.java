package org.example;

import org.example.states.LobbyState;
import org.example.states.UserState;
import org.example.statesHandlers.StatesHandler;

/**
 * Обработчик команд
 */
public class CommandHandler {
    /**
     * Обработчик состояний
     */
    private final StatesHandler states;

    /**
     * Конструктор, требует обработчика состояний
     */
    public CommandHandler(StatesHandler statesHandler) {
        states = statesHandler;
    }
    
    /**
     * Начальная игровая доска
     */
    private final static byte[][] START_BOARD = {
    		{-2, -3, -4, -5, -6, -4, -3, -2},
    		{-1, -1, -1, -1, -1, -1, -1, -1},
    		{0, 0, 0, 0, 0, 0, 0, 0},
    		{0, 0, 0, 0, 0, 0, 0, 0},
    		{0, 0, 0, 0, 0, 0, 0, 0},
    		{0, 0, 0, 0, 0, 0, 0, 0},
    		{1, 1, 1, 1, 1, 1, 1, 1},
    		{2, 3, 4, 5, 6, 4, 3, 2}
    };
    
    /**
     * Сторона доски
     */
    private final static int BOARD_SIDE_LENGTH = 8;
    
    /**
     * Ходят ли белые
     */
    private final static boolean IS_WHITE_TO_MOVE = true;

    /**
     * Пригласительное сообщение к вводу названия матча
     */
    private final static String MAKE_UP_NAME =
            "Придумайте название для матча (не более 16 символов):";

    /**
     * Сообщение о занятом названии матча
     */
    private final static String OCCUPIED =
            "Извините, данное название уже занято. Придумайте другое:";

    /**
     * Сообщение о слишком длинном названии матча
     */
    private final static String TOO_LONG =
            "Извините, название должно быть не более 16 символов. Придумайте другое:";

    /**
     * Сообщение со списком доступных матчей
     */
    private final static String LOBBIES_LIST = """
            Вот список доступных сейчас матчей.
            Нажмите на название или введите его, чтобы присоединиться.
            Введите /quit, чтобы выйти.
            """;

    /**
     * Сообщение о невозможности подключиться к матчу
     */
    private final static String JOIN_ERROR = "Этот матч уже начат, Вы не можете к нему подключиться";

    /**
     * Сообщение об отсутствии матча
     */
    private final static String LOBBY_ERROR = "Матча с таким идентификатором не существует";

    /**
     * Сообщение о выборе привязываемого мессенджера
     */
    private final static String MESSENGER_CHOOSE = """
            Какой мессенджер привязываем? Напиши полное название мессенджера и свой ID в нём
            """;

    /**
     * Сообщение о вводе идентификатора мессенджера
     */
    private final static String MESSENGER_ID = "Введи ID мессенджера";

    /**
     * Сообщение об ошибке привязки мессенджера
     */
    private final static String LINK_ERROR = "Этот ID уже используется, введи другой";
    
    /**
     * Сообщение о слишком большой разнице в рейтинге
     */
    private final static String RATING_ERROR = 
    		"Рейтинг этого матча слишком отличается, к нему нельзя присоединиться";

    /**
     * Обработать команду /quit
     */
    public void processQuitCommand(long userId) {
        String lobbyName = states.getUserLobbyName(userId);
        states.setNewUserStatus(userId, UserState.UserStatus.MAINMENU);
        states.resetUserLobbyName(userId);
        if (states.isLobbyExisting(lobbyName)) {
            if (states.isLobbyAvailable(lobbyName)) {
                states.unbookLobbyName(lobbyName);
            } else {
                long secondId = states.getLobbyAnotherUserId(lobbyName, userId);
                states.setNewUserStatus(secondId, UserState.UserStatus.MAINMENU);
                states.resetUserLobbyName(secondId);
                states.deleteLobby(lobbyName);
            }
        }
    }

    /**
     * Обработать команду /new_local
     */
    public void processNewLocalCommand(long userId) {
        states.setNewUserStatus(userId, UserState.UserStatus.INGAME);
        states.createNewLobby(String.valueOf(userId), userId, userId,
                true, LobbyState.LobbyType.SINGLEPLAYER, 
                START_BOARD, BOARD_SIDE_LENGTH, IS_WHITE_TO_MOVE);
        states.setUserLobbyName(userId, String.valueOf(userId));
    }

    /**
     * Обработать команду /create[ argument]
     */
    public void processCreateCommand(long userId, String argument) throws CommandException {
        states.setNewUserStatus(userId, UserState.UserStatus.CREATING);
        if (argument.isBlank()) {
            throw new CommandException(MAKE_UP_NAME);
        } else {
            if (states.isLobbyExisting(argument)) {
                throw new CommandException(OCCUPIED);
            } else if (argument.length() > 16) {
                throw new CommandException(TOO_LONG);
            } else {
                states.bookLobbyName(argument, userId);
                states.setUserLobbyName(userId, argument);
                states.setNewUserStatus(userId, UserState.UserStatus.AWAITING);
            }
        }
    }

    /**
     * Обработать команду /join[ argument]
     */
    public boolean processJoinCommand(long userId, String argument) throws CommandException {
        states.setNewUserStatus(userId, UserState.UserStatus.CHOOSING);
        if (argument.isBlank()) {
            throw new CommandException(LOBBIES_LIST);
        } else {
            if (!states.isLobbyExisting(argument)) {
                throw new CommandException(LOBBY_ERROR);
            } else if (!states.isLobbyAvailable(argument)) {
                throw new CommandException(JOIN_ERROR);
            } else {
            	double userRating = states.getUserRating(userId).getRight();
                long creatorId = states.getLobbyCreator(argument);
            	double creatorRating = states.getUserRating(creatorId).getRight();
            	if (Math.abs(userRating - creatorRating) > 0.05) {
            		throw new CommandException(RATING_ERROR);
            	} else {
            		boolean isFirstWhite = true;
                	states.setNewUserStatus(userId, UserState.UserStatus.INGAME);
                	states.setUserLobbyName(userId, argument);
                	states.unbookLobbyName(argument);
                	states.createNewLobby(argument, creatorId, userId,
                            isFirstWhite, LobbyState.LobbyType.MULTIPLAYER, 
                            START_BOARD, BOARD_SIDE_LENGTH, IS_WHITE_TO_MOVE);
                	states.setNewUserStatus(creatorId, UserState.UserStatus.INGAME);
                	states.setUserLobbyName(creatorId, argument);
                	return isFirstWhite;
            	}
            }
        }
    }

    /**
     * Обработать команду /link[ argument]
     */
    public String processLinkCommand(long userId, long chatId, 
    		UserState.MessengerType messenger) throws CommandException {
        states.setNewUserStatus(userId, UserState.UserStatus.MESSENGER_CHOOSING);
        if (chatId == 0 && messenger == null) {
            throw new CommandException(MESSENGER_CHOOSE);
        } else {
            if (chatId == 0) {
                throw new CommandException(MESSENGER_ID);
            } else if (states.isMessengerIdExisting(messenger, chatId)) {
            	throw new CommandException(LINK_ERROR);
            } else {
                if (states.isMessengerIdExisting(messenger, chatId)) {
                	throw new CommandException(LINK_ERROR);
                } else {
                    states.addNewMessengerId(userId, messenger, chatId);
                    states.setNewUserStatus(userId, UserState.UserStatus.MAINMENU);
                }
                return "Мессенджер привязан. Для взаимодействия со мной повторно напишите /start";
            }
        }
    }
}