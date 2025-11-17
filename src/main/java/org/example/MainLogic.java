package org.example;

import org.example.auxiliary.CommandResults;
import org.example.auxiliary.IdentifiedButton;
import org.example.auxiliary.MoveResults;
import org.example.auxiliary.SimpleButton;
import org.example.bots.Bot;
import org.example.bots.TelegramBot;
import org.example.inputHandlers.AwaitingInputHandler;
import org.example.inputHandlers.CommandHandler;
import org.example.inputHandlers.InGameInputHandler;
import org.example.inputHandlers.MainMenuInputHandler;
import org.example.states.LobbyState;
import org.example.states.UserState;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Главный логический модуль, получает идентификатор пользователя и
 * передаёт управление необходимому обработчику
 */
public class MainLogic {
    /**
     * Обработчик ввода в игре
     */
    private final InGameInputHandler inGameHandler = new InGameInputHandler();
    /**
     * Обработчик ввода в главном меню
     */
    private final MainMenuInputHandler inMenuHandler = new MainMenuInputHandler();
    /**
     * Обработчик ввода в режиме ожидания
     */
    private final AwaitingInputHandler awaitingHandler = new AwaitingInputHandler();
    /**
     * Обработчик команд
     */
    private final CommandHandler commandHandler = new CommandHandler();
    /**
     * Создатель кнопок
     */
    private final ButtonsCreator buttonsCreator = new ButtonsCreator();

    /**
     * Ассоциативный массив с соответствием идентификатора пользователя и
     * его состояния
     */
    private Map<Long, UserState> users = new HashMap<Long, UserState>();

    /**
     * Ассоциативный массив с соответствием идентификатора матча и его
     * состоянием. Если название зарезервировано, но сам матч ещё не
     * начался, то вместо состояния будет null
     */
    private Map<String, LobbyState> games = new HashMap<String, LobbyState>();

    /**
     * Экземпляр TelegramBot для отправки сообщений в Телеграм
     */
    private TelegramBot tgBot = null;

    /**
     * Скомпилированный паттерн команды
     */
    private final static Pattern COMMAND_PATTERN =
            Pattern.compile("^/([\\w]+)(?: ([\\wа-яА-ЯёЁ]+))?");

    /**
     * Сообщение о ходе противника
     */
    private final static String OPPONENTS_MOVE =
            "Вы не можете сейчас ходить. Дождитесь хода противника.";
    /**
     * Сообщение о начале игры
     */
    private final static String GAME_BEGIN = "Игра началась";

    /**
     * Сообщение об ошибке подключения к лобби
     */
    private final static String JOIN_ERROR = "Лобби с таких идентификатором не существует ";

    /**
     * Обработать ввод в соответствии с режимом пользователя
     */
    public void processInput(Bot bot, String userInput, long chatId) {

        if (bot instanceof TelegramBot && tgBot == null) {
            tgBot = (TelegramBot) bot;
        }
        if (!users.containsKey(chatId)) {
            UserState.messengerType newUserMessenger = null;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.messengerType.TELEGRAM;
            }
            users.put(chatId, new UserState(newUserMessenger));
        }
        UserState currentUserState = users.get(chatId);
        Matcher command = COMMAND_PATTERN.matcher(userInput);
        if (command.find()) {
            String commandText = command.group(1).toLowerCase();
            String commandArgument = command.group(2);
            processUserCommand(chatId, currentUserState, commandText, commandArgument);
        } else {
            switch (currentUserState.getUserState()) {
                case UserState.userState.MAINMENU -> {
                    String menuCommand = inMenuHandler.processInput(userInput);
                    processUserCommand(chatId, currentUserState, menuCommand, "");
                }
                case UserState.userState.AWAITING -> {
                    String menuCommand = awaitingHandler.processInput(userInput);
                    processUserCommand(chatId, currentUserState, menuCommand, "");
                }
                case UserState.userState.INGAME -> {
                    processGameInput(chatId, currentUserState, userInput);
                }
                default -> {
                    processUserCommand(chatId, currentUserState, "start", "");
                }
            }
        }
    }

    /**
     * Получить простые кнопки в соответствии с режимом пользователя
     */
    public List<SimpleButton> getCurrentSimpleButtons(Bot bot, long chatId) {
        if (!users.containsKey(chatId)) {
            UserState.messengerType newUserMessenger = null;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.messengerType.TELEGRAM;
            }
            users.put(chatId, new UserState(newUserMessenger));
        }
        UserState currentUserState = users.get(chatId);
        switch (currentUserState.getUserState()) {
            case UserState.userState.MAINMENU:
                return buttonsCreator.getMenuButtons();
            case UserState.userState.AWAITING:
                return buttonsCreator.getAwaitingButtons();
            case UserState.userState.INGAME:
                return List.of();
        }
        return List.of();
    }

    /**
     * Получить идентифицированные кнопки в соответствии с режимом пользователя
     */
    public List<IdentifiedButton> getCurrentIdentifiedButtons(Bot bot, long chatId) {
        if (!users.containsKey(chatId)) {
            UserState.messengerType newUserMessenger = null;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.messengerType.TELEGRAM;
            }
            users.put(chatId, new UserState(newUserMessenger));
        }
        UserState currentUserState = users.get(chatId);
        switch (currentUserState.getUserState()) {
            case UserState.userState.MAINMENU:
                return List.of();
            case UserState.userState.AWAITING:
                return List.of();
            case UserState.userState.INGAME:
                String lobbyId = currentUserState.getCurrentLobbyId();
                if (games.containsKey(lobbyId) && games.get(lobbyId) != null) {
                    LobbyState currentLobbyState = games.get(lobbyId);
                    return buttonsCreator.getGameButtons(
                            currentUserState.getMoveState(),
                            currentLobbyState.getGameState().getBoard(),
                            currentLobbyState.getGameState().isWhiteToMove());
                } else {
                    return List.of();
                }
            case UserState.userState.CHOOSING:
                Set<String> setOfLobbiesID = games.keySet();
                return buttonsCreator.getLobbyButtons(setOfLobbiesID);
        }
        return List.of();
    }

    /**
     * Обработать ввод в игре
     */
    private void processGameInput(long chatId,
                                  UserState currentUserState, String userInput) {
        String lobbyId = currentUserState.getCurrentLobbyId();
        if (games.containsKey(lobbyId) && games.get(lobbyId) != null) {
            LobbyState currentLobbyState = games.get(lobbyId);
            if (((chatId == currentLobbyState.getFirstPlayerId()
                    ^ currentLobbyState.getGameState().isWhiteToMove())
                    ^ currentLobbyState.isFirstPlayerWhite())
                    || currentLobbyState.getLobbyType().equals(
                    LobbyState.lobbyType.SINGLEPLAYER)) {
                MoveResults results = switch (currentLobbyState.getLobbyType()) {
                    case LobbyState.lobbyType.SINGLEPLAYER -> inGameHandler.processInputSingleGame(
                            userInput,
                            currentUserState.getMoveState(),
                            currentLobbyState.getGameState());
                    case LobbyState.lobbyType.MULTIPLAYER -> inGameHandler.processInputMultiGame(
                            userInput,
                            currentLobbyState.getGameState().isWhiteToMove(),
                            currentUserState.getMoveState(),
                            currentLobbyState.getGameState());
                };
                long secondId = currentLobbyState.getSecondPlayerId();
                UserState secondUserState = users.get(secondId);
                switch (results.thisMoveStatus()) {
                    case MoveResults.moveStatus.GAMEOVER:
                        currentUserState.setUserState(UserState.userState.MAINMENU);
                        secondUserState.setUserState(UserState.userState.MAINMENU);
                        currentUserState.resetLobbyId();
                        secondUserState.resetLobbyId();
                        games.remove(lobbyId);
                    case MoveResults.moveStatus.SUCCESS:
                        if (currentLobbyState.getLobbyType().equals(
                                LobbyState.lobbyType.MULTIPLAYER)) {
                            sendMessagesTo(
                                    secondId,
                                    secondUserState.getUserMessenger(),
                                    results.messagesTextsLists().getLast());
                        }
                    case MoveResults.moveStatus.FAILURE:
                        sendMessagesTo(
                                chatId,
                                currentUserState.getUserMessenger(),
                                results.messagesTextsLists().getFirst());
                }
            } else {
                sendMessagesTo(
                        chatId,
                        currentUserState.getUserMessenger(),
                        List.of(OPPONENTS_MOVE));
            }
        }
    }

    /**
     * Обработать команду
     */
    private void processUserCommand(long chatId, UserState currentUserState,
                                    String command, String argument) {
        CommandResults results = commandHandler.processCommand(command);
        List<String> messagesFirst = null;
        List<String> messagesSecond = null;
        long chatIdSecond = 0;
        switch (results.userLobbyStatus()) {
            case CommandResults.lobbyStatus.CLOSE -> {
                String lobbyId = currentUserState.getCurrentLobbyId();
                LobbyState currentLobbyState = games.get(lobbyId);
                if (currentLobbyState != null) {
                    if (currentLobbyState.getFirstPlayerId() == chatId) {
                        chatIdSecond = currentLobbyState.getSecondPlayerId();
                    } else {
                        chatIdSecond = currentLobbyState.getFirstPlayerId();
                    }
                    UserState secondUserState = users.get(chatIdSecond);
                    secondUserState.setUserState(UserState.userState.MAINMENU);
                    secondUserState.resetLobbyId();
                    messagesSecond = results.messagesTextsLists().getLast();
                    games.remove(lobbyId);
                }
                currentUserState.setUserState(UserState.userState.MAINMENU);
                currentUserState.resetLobbyId();
                messagesFirst = results.messagesTextsLists().getFirst();
            }
            case CommandResults.lobbyStatus.SINGLEPLAYER -> {
                LobbyState newLobby = new LobbyState(
                        chatId, LobbyState.lobbyType.SINGLEPLAYER);
                newLobby.setSecondPlayerId(chatId);
                currentUserState.setUserState(UserState.userState.INGAME);
                List<List<String>> startBoards = inGameHandler.getStartingBoard(
                        newLobby.getGameState(), newLobby.isFirstPlayerWhite());
                messagesFirst = results.messagesTextsLists().getFirst();
                messagesFirst.addAll(startBoards.getFirst());
                currentUserState.setCurrentLobbyId(String.valueOf(chatId));
                games.put(String.valueOf(chatId), newLobby);
            }
            case CommandResults.lobbyStatus.JOIN -> {
                if (argument != null) {
                    if (games.containsKey(argument)) {
                        chatIdSecond = games.get(argument).getFirstPlayerId();
                        games.get(argument).setSecondPlayerId(chatId);
                        users.get(chatId).setUserState(UserState.userState.INGAME);
                        users.get(games.get(argument).getFirstPlayerId()).setUserState(UserState.userState.INGAME);
                        messagesFirst.add(GAME_BEGIN);
                        messagesSecond.add(GAME_BEGIN);

                    } else {
                        messagesFirst = results.messagesTextsLists().getFirst();
                        messagesFirst.add(JOIN_ERROR);
                    }
                } else {
                    currentUserState.setUserState(UserState.userState.CHOOSING);
                    messagesFirst = results.messagesTextsLists().getFirst();
                }
            }
            case CommandResults.lobbyStatus.MULTIPLAYER -> {
                LobbyState newLobby = new LobbyState(
                        chatId, LobbyState.lobbyType.MULTIPLAYER);
                currentUserState.setUserState(UserState.userState.AWAITING);
                messagesFirst = results.messagesTextsLists().getFirst();
                currentUserState.setCurrentLobbyId(argument);
                games.put(argument, newLobby);
            }
            case CommandResults.lobbyStatus.NOTHING -> {
                messagesFirst = results.messagesTextsLists().getFirst();
            }
        }
        if (messagesFirst != null) {
            sendMessagesTo(
                    chatId,
                    currentUserState.getUserMessenger(),
                    messagesFirst);
        }
        if (messagesSecond != null && chatIdSecond != chatId) {
            sendMessagesTo(
                    chatIdSecond,
                    users.get(chatIdSecond).getUserMessenger(),
                    messagesSecond);
        }
    }

    /**
     * Отправить сообщения пользователю
     */
    private void sendMessagesTo(long chatId, UserState.messengerType userMessenger,
                                List<String> messagesTexts) {
        switch (userMessenger) {
            case UserState.messengerType.TELEGRAM -> {
                tgBot.sendMessages(chatId, messagesTexts.iterator());
            }
        }
    }
}
