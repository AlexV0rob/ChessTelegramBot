package org.example;

import org.example.auxiliary.CommandResults;
import org.example.auxiliary.IdentifiedButton;
import org.example.auxiliary.MoveResults;
import org.example.auxiliary.SimpleButton;
import org.example.bots.Bot;
import org.example.bots.TelegramBot;
import org.example.inputHandlers.AwaitingInputHandler;
import org.example.inputHandlers.CommandInputHandler;
import org.example.inputHandlers.InGameInputHandler;
import org.example.inputHandlers.MainMenuInputHandler;
import org.example.states.LobbyState;
import org.example.states.UserState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private final CommandInputHandler commandHandler = new CommandInputHandler();
    /**
     * Создатель кнопок
     */
    private final ButtonsCreator buttonsCreator = new ButtonsCreator();
    /**
     * Сообщение о начале игры
     */
    private final static String GAME_BEGIN = "Игра началась";
    /**
     * Сообщение об ошибке подключения к лобби
     */
    private final static String JOIN_ERROR = "Лобби с таких идентификатором не существует ";

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
     * Ассоциативный массив с соответствием идентификатора пользователя и
     * идентификатором последнего отправленного ему сообщения
     */
    private Map<Long, Long> messages = new HashMap<Long, Long>();

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
     * Обработать ввод в соответствии с режимом пользователя
     */
    public void processInput(Bot bot, String userInput, long chatId) {
        if (bot instanceof TelegramBot && tgBot == null) {
            tgBot = (TelegramBot) bot;
        }
        if (!users.containsKey(chatId)) {
            UserState.MessengerType newUserMessenger = null;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.MessengerType.TELEGRAM;
            }
            users.put(chatId, new UserState(newUserMessenger));
        }
        UserState currentUserState = users.get(chatId);
        Matcher command = COMMAND_PATTERN.matcher(userInput);
        if (command.find()) {
            String commandText = command.group(1).toLowerCase();
            String commandArgument = command.group(2);
            processUserCommand(chatId, currentUserState, commandText, commandArgument, bot);
        } else {
            switch (currentUserState.getUserState()) {
                case UserState.UserStatus.MAINMENU -> {
                    String menuCommand = inMenuHandler.processInput(userInput);
                    processUserCommand(chatId, currentUserState, menuCommand, "", bot);
                }
                case UserState.UserStatus.AWAITING -> {
                    String menuCommand = awaitingHandler.processInput(userInput);
                    processUserCommand(chatId, currentUserState, menuCommand, "", bot);
                }
                case UserState.UserStatus.INGAME -> {
                    processGameInput(chatId, currentUserState, userInput, bot);
                }
                default -> {
                    processUserCommand(chatId, currentUserState, "start", "", bot);
                }
            }
        }
    }

    /**
     * Получить простые кнопки в соответствии с режимом пользователя
     */
    public List<SimpleButton> getCurrentSimpleButtons(Bot bot, long chatId) {
        if (!users.containsKey(chatId)) {
            UserState.MessengerType newUserMessenger = null;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.MessengerType.TELEGRAM;
            }
            users.put(chatId, new UserState(newUserMessenger));
        }
        UserState currentUserState = users.get(chatId);
        switch (currentUserState.getUserState()) {
            case UserState.UserStatus.MAINMENU:
                return buttonsCreator.getMenuButtons();
            case UserState.UserStatus.AWAITING:
                return buttonsCreator.getAwaitingButtons();
            case UserState.UserStatus.INGAME:
                return List.of();
        }
        return List.of();
    }

    /**
     * Получить идентифицированные кнопки в соответствии с режимом пользователя
     */
    public List<IdentifiedButton> getCurrentIdentifiedButtons(Bot bot, long chatId) {
        if (!users.containsKey(chatId)) {
            UserState.MessengerType newUserMessenger = null;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.MessengerType.TELEGRAM;
            }
            users.put(chatId, new UserState(newUserMessenger));
        }
        UserState currentUserState = users.get(chatId);
        switch (currentUserState.getUserState()) {
            case UserState.UserStatus.MAINMENU:
                return List.of();
            case UserState.UserStatus.AWAITING:
                return List.of();
            case UserState.UserStatus.CHOOSING:
                return buttonsCreator.getLobbyButtons(games.keySet());
            case UserState.UserStatus.INGAME:
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
        }
        return List.of();
    }

    /**
     * Обработать ввод в игре
     */
    private void processGameInput(long chatId,
                                  UserState currentUserState, String userInput, Bot bot) {
        String lobbyId = currentUserState.getCurrentLobbyId();
        if (games.containsKey(lobbyId) && games.get(lobbyId) != null) {
            LobbyState currentLobbyState = games.get(lobbyId);
            if (((chatId == currentLobbyState.getFirstPlayerId()
                    ^ currentLobbyState.getGameState().isWhiteToMove())
                    ^ currentLobbyState.isFirstPlayerWhite())
                    || currentLobbyState.getLobbyType().equals(
                    LobbyState.LobbyType.SINGLEPLAYER)) {
                MoveResults results = switch (currentLobbyState.getLobbyType()) {
                    case LobbyState.LobbyType.SINGLEPLAYER -> inGameHandler.processInputSingleGame(
                            userInput,
                            currentUserState.getMoveState(),
                            currentLobbyState.getGameState());
                    case LobbyState.LobbyType.MULTIPLAYER -> inGameHandler.processInputMultiGame(
                            userInput,
                            currentLobbyState.getGameState().isWhiteToMove(),
                            currentUserState.getMoveState(),
                            currentLobbyState.getGameState());
                };
                long secondId = currentLobbyState.getSecondPlayerId();
                UserState secondUserState = users.get(secondId);
                switch (results.thisMoveStatus()) {
                    case MoveResults.MoveStatus.GAMEOVER:
                        currentUserState.setUserState(UserState.UserStatus.MAINMENU);
                        secondUserState.setUserState(UserState.UserStatus.MAINMENU);
                        currentUserState.resetLobbyId();
                        secondUserState.resetLobbyId();
                        games.remove(lobbyId);
                    case MoveResults.MoveStatus.SUCCESS:
                        if (currentLobbyState.getLobbyType().equals(
                                LobbyState.LobbyType.MULTIPLAYER)) {
                            Bot botSecond = switch (secondUserState.getUserMessenger()) {
                                case UserState.MessengerType.TELEGRAM -> tgBot;
                                default -> bot;
                            };
                            sendMessagesTo(
                                    secondId,
                                    results.messagesTextsLists().getLast().iterator(),
                                    botSecond);
                        }
                    case MoveResults.MoveStatus.FAILURE:
                        Iterator<String> messagesTextsIterator =
                                results.messagesTextsLists().getFirst().iterator();
                        if (messages.containsKey(chatId)) {
                            String messageToEdit = messagesTextsIterator.next();
                            editMessageOf(
                                    chatId,
                                    messages.get(chatId),
                                    messageToEdit,
                                    messagesTextsIterator.hasNext(),
                                    bot);
                        }
                        sendMessagesTo(
                                chatId,
                                messagesTextsIterator,
                                bot);
                }
            } else {
                sendMessagesTo(
                        chatId,
                        List.of(OPPONENTS_MOVE).iterator(),
                        bot);
            }
        }
    }

    /**
     * Обработать команду
     */
    private void processUserCommand(long chatId, UserState currentUserState,
                                    String command, String argument, Bot bot) {
        CommandResults results = commandHandler.processCommand(command);
        List<String> messagesFirst = null;
        List<String> messagesSecond = null;
        long chatIdSecond = 0;
        switch (results.userLobbyStatus()) {
            case CommandResults.LobbyStatus.CLOSE -> {
                String lobbyId = currentUserState.getCurrentLobbyId();
                LobbyState currentLobbyState = games.get(lobbyId);
                if (currentLobbyState != null) {
                    if (currentLobbyState.getFirstPlayerId() == chatId) {
                        chatIdSecond = currentLobbyState.getSecondPlayerId();
                    } else {
                        chatIdSecond = currentLobbyState.getFirstPlayerId();
                    }
                    UserState secondUserState = users.get(chatIdSecond);
                    secondUserState.setUserState(UserState.UserStatus.MAINMENU);
                    secondUserState.resetLobbyId();
                    messagesSecond = results.messagesTextsLists().getLast();
                    games.remove(lobbyId);
                }
                currentUserState.setUserState(UserState.UserStatus.MAINMENU);
                currentUserState.resetLobbyId();
                messagesFirst = results.messagesTextsLists().getFirst();
            }
            case CommandResults.LobbyStatus.SINGLEPLAYER -> {
                LobbyState newLobby = new LobbyState(
                        chatId, LobbyState.LobbyType.SINGLEPLAYER);
                newLobby.setSecondPlayerId(chatId);
                currentUserState.setUserState(UserState.UserStatus.INGAME);
                List<List<String>> startBoards = inGameHandler.getStartingBoard(
                        newLobby.getGameState(), newLobby.isFirstPlayerWhite());
                messagesFirst = results.messagesTextsLists().getFirst();
                messagesFirst.addAll(startBoards.getFirst());
                currentUserState.setCurrentLobbyId(String.valueOf(chatId));
                games.put(String.valueOf(chatId), newLobby);
            }
            case CommandResults.LobbyStatus.JOIN -> {
                if (argument != null && argument != "") {
                    if (games.containsKey(argument)) {
                        chatIdSecond = games.get(argument).getFirstPlayerId();
                        games.get(argument).setSecondPlayerId(chatId);
                        users.get(chatId).setUserState(UserState.UserStatus.INGAME);
                        users.get(games.get(argument).getFirstPlayerId()).setUserState(UserState.UserStatus.INGAME);
                        messagesFirst = results.messagesTextsLists().getFirst();
                        messagesSecond = results.messagesTextsLists().getLast();
                        messagesFirst.add(GAME_BEGIN);
                        messagesSecond.add(GAME_BEGIN);
                        List<List<String>> listOfChessBoards =
                                inGameHandler.getStartingBoard(
                                        games.get(argument).getGameState(),
                                        games.get(argument).isFirstPlayerWhite());
                        messagesFirst.addAll(listOfChessBoards.getLast());
                        messagesSecond.addAll(listOfChessBoards.getFirst());


                    } else {
                        messagesFirst = results.messagesTextsLists().getFirst();
                        messagesFirst.add(JOIN_ERROR);
                    }
                } else {
                    currentUserState.setUserState(UserState.UserStatus.CHOOSING);
                    messagesFirst = results.messagesTextsLists().getFirst();
                }
            }
            case CommandResults.LobbyStatus.MULTIPLAYER -> {
                LobbyState newLobby = new LobbyState(
                        chatId, LobbyState.LobbyType.MULTIPLAYER);
                currentUserState.setUserState(UserState.UserStatus.AWAITING);
                messagesFirst = results.messagesTextsLists().getFirst();
                currentUserState.setCurrentLobbyId(argument);
                games.put(argument, newLobby);
            }
            case CommandResults.LobbyStatus.NOTHING -> {
                messagesFirst = results.messagesTextsLists().getFirst();
            }
        }
        if (messagesFirst != null) {
            sendMessagesTo(
                    chatId,
                    messagesFirst.iterator(),
                    bot);
        }
        if (messagesSecond != null && chatIdSecond != chatId) {
            Bot botSecond = switch (users.get(chatIdSecond).getUserMessenger()) {
                case UserState.MessengerType.TELEGRAM -> tgBot;
                default -> bot;
            };
            sendMessagesTo(
                    chatIdSecond,
                    messagesSecond.iterator(),
                    botSecond);
        }
    }

    /**
     * Отправить сообщения пользователю
     */
    private void sendMessagesTo(long chatId,
                                Iterator<String> messagesTextsIterator, Bot bot) {
        long lastMessageId = bot.sendMessages(chatId, messagesTextsIterator);
        if (lastMessageId >= 0) {
            messages.put(chatId, lastMessageId);
        }
    }

    /**
     * Изменить сообщение
     */
    private void editMessageOf(long chatId, long messageId,
                               String editedMessageText, boolean moreMessages, Bot bot) {
        bot.editMessage(chatId, messageId, editedMessageText, moreMessages);
    }
}
