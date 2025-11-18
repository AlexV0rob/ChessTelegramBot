package org.example;

import org.example.auxiliary.IdentifiedButton;
import org.example.auxiliary.SimpleButton;
import org.example.bots.Bot;
import org.example.bots.TelegramBot;
import org.example.chess.GameHandler;
import org.example.chess.PositionOnBoard;
import org.example.states.MoveState;
import org.example.states.GameState;
import org.example.states.LobbyState;
import org.example.states.UserState;


import java.util.ArrayList;
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
     * Создатель кнопок
     */
    private final ButtonsCreator buttonsCreator = new ButtonsCreator();
    
    /**
     * Переводчик игры для вывода текста пользователю
     */
    private final GameTranslator gameTranslator = new GameTranslator();
    
    /**
     * Обработчик игры
     */
    private final GameHandler gameHandler = new GameHandler();
    
    /**
     * Конвертер частей хода
     */
    private final MovePartsConverter movePartsConverter = new MovePartsConverter();
    
    /**
     * Конвертер текстов кнопок
     */
    private final MenuButtonsConverter menuButtonsConverter = new MenuButtonsConverter();
    
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
     * Скомпилированное регулярное выражение команды
     */
    private final static Pattern COMMAND_PATTERN =
            Pattern.compile("^/([\\w]+)(?: ([\\wа-яА-ЯёЁ]+))?");
    /**
     * Скомпилированное регулярное выражение, соответствующее полностью
     * введённому ходу в текстовом виде
     */
    private final static Pattern NOTATION_PATTERN =
            Pattern.compile("^([prbnqkPRBNQK]??)([a-hA-H][1-8])([a-hA-H][1-8])$");
    /**
     * Скомпилированное регулярное выражение, соответствующее части хода
     * в виде callback запроса
     */
    private final static Pattern MOVE_PART_PATTERN =
            Pattern.compile("^__((?:[prbnqkPRBNQK])|(?:[a-hA-H][1-8]))__$");
    
    /**
     * Скомпилированное регулярное выражение, соответствующее кнопке выбора матча
     */
    private final static Pattern LOBBY_BUTTON_PATTERN =
            Pattern.compile("^__([\\wа-яА-ЯёЁ]+)__$");
    
    /**
     * Сообщение команды /start
     */
    private final static String START_MESSAGE = """
            Здравствуй, путник! Я бот о шахматах. Сейчас я умею:
             - запускать игру на одном устройстве
            
            Пока что я могу только это, но список возможностей 
            """ + """
            будет пополняться с течением разработки. 
            Отправь /help для большей информации.
            """;
    /**
     * Сообщение команды /help
     */
    private final static String HELP_MESSAGE = """
            Сейчас я могу:
             - запускать игру на одном устройстве
            
            Доступные команды:
            /start - перезапускает бота
            /help - позволяет это сообщение
            /newsinglegame - начинает новую игру на одном устройстве
            
            Скоро будет больше возможностей.
            """;
    /**
     * Ответ на неизветную команду
     */
    private final static String UNKNOWN_MESSAGE = "Неизвестная команда";
    /**
     * Сообщение в меню
     */
    private final static String MENU_MESSAGE = "Чем займёмся?";
    /**
     * Сообщение о начале игры
     */
    private final static String GAME_STARTED = "Игра началась";

    /**
	 * Пригласительное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Сообщение о ходе оппонента
	 */
	private final static String NOT_YOUR_MOVE = "Сейчас ходит противник.";

    /**
     * Сообщение о ходе противника
     */
    private final static String OPPONENTS_MOVE =
            "Вы не можете сейчас ходить. Дождитесь хода противника.";
	
	/**
	 * Сообщение о досрочном завершении матча
	 */
	private final static String SURRENDERED = "Ваш противник вышел. Матч завершён.";
	
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
	 * Сообщение об успешном создании матча
	 */
	private final static String GAME_CREATED = """
			Матч %s создан и доступен для других игроков.
			Ожидайте присоединения противника
			""";
	
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
        long secondChatId = 0;
        UserState secondUserState = null;
        String lobbyId = currentUserState.getCurrentLobbyId();
        LobbyState lobbyState = games.get(lobbyId);
        if (lobbyState != null && lobbyState.getAnotherPlayerId(chatId) != 0) {
        	secondChatId = lobbyState.getAnotherPlayerId(chatId);
        	secondUserState = users.get(secondChatId);
        }
        List<String> responseMessagesFirst = new ArrayList<String>();
        List<String> responseMessagesSecond = new ArrayList<String>();
        Matcher command = COMMAND_PATTERN.matcher(userInput);
        if (command.find()) {
        	processCommand(chatId, secondChatId, currentUserState, secondUserState, 
        			lobbyId, command.group(1), command.group(2), 
        			responseMessagesFirst, responseMessagesSecond);
        } else {
            switch (currentUserState.getUserState()) {
                case UserState.UserStatus.MAINMENU -> {
                	String commandEquivalent = menuButtonsConverter.getMenuCommand(userInput);
                	processCommand(chatId, secondChatId, currentUserState, secondUserState, 
                			lobbyId, commandEquivalent, "", 
                			responseMessagesFirst, responseMessagesSecond);
                }
                case UserState.UserStatus.AWAITING -> {
                	String commandEquivalent = menuButtonsConverter.getMenuCommand(userInput);
                	processCommand(chatId, secondChatId, currentUserState, secondUserState, 
                			lobbyId, commandEquivalent, "", 
                			responseMessagesFirst, responseMessagesSecond);
                }
                case UserState.UserStatus.CREATING -> {
                	processCommand(chatId, secondChatId, currentUserState, secondUserState, 
                			lobbyId, "creategame", userInput, 
                			responseMessagesFirst, responseMessagesSecond);
                }
                case UserState.UserStatus.CHOOSING -> {
                	Matcher callbackMatch = LOBBY_BUTTON_PATTERN.matcher(userInput);
                	if (callbackMatch.find()) {
                    	processCommand(chatId, secondChatId, currentUserState, secondUserState, 
                    			lobbyId, "joingame", callbackMatch.group(1), 
                    			responseMessagesFirst, responseMessagesSecond);
                	} else {
                		processCommand(chatId, secondChatId, currentUserState, secondUserState, 
                    			lobbyId, "joingame", userInput, 
                    			responseMessagesFirst, responseMessagesSecond);
            		}
                }
                case UserState.UserStatus.INGAME -> {
                    if (lobbyState != null) {
                    	processGame(chatId, secondChatId, currentUserState, 
                    			secondUserState, lobbyState, lobbyId, userInput, 
                    			responseMessagesFirst, responseMessagesSecond);
                    }
                }
                default -> {
                	processCommand(chatId, secondChatId, currentUserState, 
                			secondUserState, lobbyId, "start", "", 
                			responseMessagesFirst, responseMessagesSecond);
                }
            }
        }
        if (messages.get(chatId) != null && messages.get(chatId) >= 0) {
            String messageToEdit = responseMessagesFirst.removeFirst();
        	editMessageOf(chatId, messages.get(chatId), messageToEdit, 
        			!responseMessagesFirst.isEmpty(), bot);
        }
        if (!responseMessagesFirst.isEmpty()) {
        	sendMessagesTo(chatId, responseMessagesFirst.iterator(), bot);
        }
        long otherChatId = secondChatId;
        if (otherChatId == 0 && games.containsKey(currentUserState.getCurrentLobbyId())) {
        	otherChatId = games.get(currentUserState.getCurrentLobbyId()).getAnotherPlayerId(chatId);
        }
        if (otherChatId > 0 && otherChatId != chatId) {
        	UserState.MessengerType otherUserMessenger = 
        			users.get(otherChatId).getUserMessenger();
        	if (otherUserMessenger != null) {
        		switch (users.get(otherChatId).getUserMessenger()) {
        		case UserState.MessengerType.TELEGRAM -> {
        			sendMessagesTo(otherChatId, responseMessagesSecond.iterator(), tgBot);
        		}
        		} 
        	} else {
        		sendMessagesTo(otherChatId, responseMessagesSecond.iterator(), bot);
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
            case UserState.UserStatus.AWAITING:
                return buttonsCreator.getAwaitingButtons();
            case UserState.UserStatus.MAINMENU:
                return buttonsCreator.getMenuButtons();
            case UserState.UserStatus.CREATING:
            case UserState.UserStatus.CHOOSING:
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
            case UserState.UserStatus.AWAITING:
            case UserState.UserStatus.CREATING:
                return List.of();
            case UserState.UserStatus.INGAME:
                String lobbyId = currentUserState.getCurrentLobbyId();
                LobbyState lobbyState = games.get(lobbyId);
                if (lobbyState != null) {
                	if ((chatId == lobbyState.getFirstPlayerId() && 
                    		lobbyState.isFirstPlayerToMove()) || 
                    		(chatId == lobbyState.getSecondPlayerId() && 
                    		!lobbyState.isFirstPlayerToMove())) {
                    	return buttonsCreator.getGameButtons(
                    			currentUserState.getMoveState(),
                            	lobbyState.getGameState().getBoard(),
                            	lobbyState.getGameState().isWhiteToMove());
                	}
                }
                return List.of();
            case UserState.UserStatus.CHOOSING:
                List<String> listOfLobbiesID = List.copyOf(games.keySet());
                return buttonsCreator.getLobbyButtons(listOfLobbiesID);
        }
        return List.of();
    }
    
    /**
     * Реализовать ход
     */
    private GameHandler.MoveProperty makeMove(String figure, String start, 
    		String finish, GameState gameState) {
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
        		gameState);
        if (!moveProperty.equals(GameHandler.MoveProperty.IMPOSSIBLE) && 
        		!moveProperty.equals(GameHandler.MoveProperty.INVALID)) {
        	gameState.moveFigure(
					new PositionOnBoard(
							startPositionRow, 
							startPositionColumn), 
					new PositionOnBoard(
							finishPositionRow, 
							finishPositionColumn));
        	if (!moveProperty.equals(GameHandler.MoveProperty.MATE)) {
        		gameState.changeSide();
        	}
        }
        return moveProperty;
    }
    
    /**
     * Обработать команду
     */
    private void processCommand(long chatId, long secondChatId, 
    		UserState currentUserState, UserState secondUserState, 
    		String lobbyId, String command, String argument, 
    		List<String> responseMessagesFirst, List<String> responseMessagesSecond) {
    	switch (command) {
        case "start", "quit" -> {
            if (command.equals("start")) {
            	responseMessagesFirst.add(START_MESSAGE);
            }
            responseMessagesFirst.add(MENU_MESSAGE);
            currentUserState.setUserState(UserState.UserStatus.MAINMENU);
            currentUserState.resetLobbyId();
            currentUserState.getMoveState().clearMoveState();
            if (secondChatId > 0 && secondChatId != chatId) {
                secondUserState.setUserState(UserState.UserStatus.MAINMENU);
                secondUserState.resetLobbyId();
                secondUserState.getMoveState().clearMoveState();
                responseMessagesSecond.add(SURRENDERED);
                responseMessagesSecond.add(MENU_MESSAGE);
            }
            games.remove(lobbyId);
        }
        case "help" -> {
        	responseMessagesFirst.add(HELP_MESSAGE);
        }
        case "newsinglegame" -> {
        	LobbyState newLobbyState = new LobbyState(
        			chatId, LobbyState.LobbyType.SINGLEPLAYER);
        	newLobbyState.setSecondPlayerId(chatId);
        	games.put(String.valueOf(chatId), newLobbyState);
        	responseMessagesFirst.add(GAME_STARTED);
        	responseMessagesFirst.add(
        			gameTranslator.chessboardString(
        					GameHandler.MoveProperty.REGULAR, 
        					newLobbyState.getGameState().getBoard(), 
        					true, true));
        	responseMessagesFirst.add(YOUR_MOVE);
            currentUserState.setUserState(UserState.UserStatus.INGAME);
            currentUserState.resetLobbyId();
            currentUserState.setCurrentLobbyId(String.valueOf(chatId));
        }
        case "creategame" -> {
        	if (argument == null || argument.isEmpty()) {
                currentUserState.setUserState(UserState.UserStatus.CREATING);
        		responseMessagesFirst.add(MAKE_UP_NAME);
        	} else {
        		if (games.containsKey(argument)) {
                    currentUserState.setUserState(UserState.UserStatus.CREATING);            			
            		responseMessagesFirst.add(OCCUPIED);
        		} else if (argument.length() > 16) {
                    currentUserState.setUserState(UserState.UserStatus.CREATING);
            		responseMessagesFirst.add(TOO_LONG);
        		} else {
            		responseMessagesFirst.add(GAME_CREATED.formatted(argument));
            		LobbyState newLobbyState = new LobbyState(
                			chatId, LobbyState.LobbyType.MULTIPLAYER);
                	games.put(argument, newLobbyState);
        			currentUserState.setUserState(UserState.UserStatus.AWAITING);
                    currentUserState.resetLobbyId();
                    currentUserState.setCurrentLobbyId(argument);
        		}
        	}
        }
        case "joingame" -> {
        	if (argument == null || argument.isEmpty()) {
                currentUserState.setUserState(UserState.UserStatus.CHOOSING);
        		responseMessagesFirst.add(LOBBIES_LIST);
        	} else {
        		if (games.containsKey(argument) && 
        				games.get(argument).getSecondPlayerId() == 0) {
        			LobbyState otherLobbyState = games.get(argument);
        			otherLobbyState.setSecondPlayerId(chatId);
        			long otherChatId = otherLobbyState.getAnotherPlayerId(chatId);
        			UserState otherUserState = users.get(otherChatId);
        			currentUserState.setUserState(UserState.UserStatus.INGAME);
        			otherUserState.setUserState(UserState.UserStatus.INGAME);
                    currentUserState.resetLobbyId();
                    currentUserState.setCurrentLobbyId(argument);
                    responseMessagesFirst.add(GAME_STARTED);
                    responseMessagesFirst.add(
                    		gameTranslator.chessboardString(
                    				GameHandler.MoveProperty.REGULAR, 
                    				otherLobbyState.getGameState().getBoard(), 
                    				true, 
                    				!otherLobbyState.isFirstPlayerToMove()));
                    if (otherLobbyState.isFirstPlayerToMove()) {
                    	responseMessagesFirst.add(NOT_YOUR_MOVE);
                    } else {
                    	responseMessagesFirst.add(YOUR_MOVE);
                    }
                    responseMessagesSecond.add(GAME_STARTED);
                    responseMessagesSecond.add(
                    		gameTranslator.chessboardString(
                    				GameHandler.MoveProperty.REGULAR, 
                    				otherLobbyState.getGameState().getBoard(), 
                    				true, 
                    				otherLobbyState.isFirstPlayerToMove()));
                    if (otherLobbyState.isFirstPlayerToMove()) {
                    	responseMessagesSecond.add(YOUR_MOVE);
                    } else {
                    	responseMessagesSecond.add(NOT_YOUR_MOVE);
                    }
                    secondChatId = otherChatId;
                    secondUserState = otherUserState;
        		} else if (!games.containsKey(argument)) {
                    currentUserState.setUserState(UserState.UserStatus.CHOOSING);
            		responseMessagesFirst.add(LOBBY_ERROR);
        		} else {
        			currentUserState.setUserState(UserState.UserStatus.CHOOSING);
            		responseMessagesFirst.add(JOIN_ERROR);
        		}
        	}
        }
        default -> {
        	responseMessagesFirst.add(UNKNOWN_MESSAGE);
        }
    	}
    }
    
    /**
     * Обработать игру
     */
    private void processGame(long chatId, long secondChatId, 
    		UserState currentUserState, UserState secondUserState, 
    		LobbyState lobbyState, String lobbyId, String userInput, 
    		List<String> responseMessagesFirst, List<String> responseMessagesSecond) {
    	if ((chatId == lobbyState.getFirstPlayerId() && 
        		lobbyState.isFirstPlayerToMove()) || 
        		(chatId == lobbyState.getSecondPlayerId() && 
        		!lobbyState.isFirstPlayerToMove())) {
        	Matcher notationMatch = NOTATION_PATTERN.matcher(userInput);
        	Matcher callbackMatch = MOVE_PART_PATTERN.matcher(userInput);
        	MoveState currentMoveState = currentUserState.getMoveState();
        	if (callbackMatch.find()) {
        		String movePart = callbackMatch.group(1).toLowerCase();
        		if (!movePart.isEmpty()) {
        			currentMoveState.nextStatus(movePart);
        		}
        		String currentFigure = currentMoveState.getFigure();
        		String currentStartPosition = currentMoveState.getStartPosition();
        		String currentFinishPosition = currentMoveState.getFinishPosition();
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
        		responseMessagesFirst.add(moveMessage);
        	}
        	if (notationMatch.find() || currentMoveState.isMoveReady()) {
        		boolean oldSide = lobbyState.getGameState().isWhiteToMove();
        		GameHandler.MoveProperty moveProperty = null;
        		if (currentMoveState.isMoveReady()) {
        			moveProperty = makeMove(
        					currentMoveState.getFigure(), 
        					currentMoveState.getStartPosition(), 
        					currentMoveState.getFinishPosition(), 
        					lobbyState.getGameState());
        		} else {
        			moveProperty = makeMove(
        					notationMatch.group(1).toLowerCase(), 
        					notationMatch.group(2).toLowerCase(), 
        					notationMatch.group(3).toLowerCase(), 
        					lobbyState.getGameState());
        		}
        		currentMoveState.clearMoveState();
        		switch (moveProperty) {
        		case GameHandler.MoveProperty.IMPOSSIBLE,
        		GameHandler.MoveProperty.INVALID -> {
        			responseMessagesFirst.add(
        					gameTranslator.chessboardString(
        							moveProperty, 
        							lobbyState.getGameState().getBoard(), 
        							lobbyState.getGameState().isWhiteToMove(), 
        							lobbyState.getGameState().isWhiteToMove()));
        			responseMessagesFirst.add(YOUR_MOVE);
        		}
        		case GameHandler.MoveProperty.REGULAR, 
        		GameHandler.MoveProperty.CHECK -> {
        			lobbyState.changeMovingPlayer();
        			if (lobbyState.getLobbyType().equals(
        					LobbyState.LobbyType.SINGLEPLAYER)) {
        				responseMessagesFirst.add(gameTranslator.chessboardString(
        							moveProperty, 
        							lobbyState.getGameState().getBoard(), 
        							lobbyState.getGameState().isWhiteToMove(), 
        							lobbyState.getGameState().isWhiteToMove()));
        				responseMessagesFirst.add(YOUR_MOVE);
        			} else if (lobbyState.getLobbyType().equals(
        					LobbyState.LobbyType.MULTIPLAYER)) {
        				responseMessagesFirst.add(gameTranslator
        						.chessboardString(
        								GameHandler.MoveProperty.REGULAR, 
        								lobbyState.getGameState().getBoard(), 
        								lobbyState.getGameState().isWhiteToMove(), 
        								oldSide));
        				responseMessagesFirst.add(NOT_YOUR_MOVE);
        				responseMessagesSecond.add(gameTranslator
        						.chessboardString(
        								moveProperty, 
        								lobbyState.getGameState().getBoard(), 
        								lobbyState.getGameState().isWhiteToMove(), 
        								!oldSide));
        				responseMessagesSecond.add(YOUR_MOVE);
        			}
        		}
        		case GameHandler.MoveProperty.MATE -> {
        			responseMessagesFirst.add(gameTranslator.chessboardString(
        						moveProperty, 
        						lobbyState.getGameState().getBoard(), 
        						lobbyState.getGameState().isWhiteToMove(), 
        						lobbyState.getGameState().isWhiteToMove()));
        			responseMessagesFirst.add(MENU_MESSAGE);
                    currentUserState.setUserState(UserState.UserStatus.MAINMENU);
                    currentUserState.resetLobbyId();
                    currentUserState.getMoveState().clearMoveState();
        			if (lobbyState.getLobbyType().equals(
        					LobbyState.LobbyType.MULTIPLAYER)) {
        				responseMessagesSecond.add(gameTranslator
        						.chessboardString(
        								moveProperty, 
        								lobbyState.getGameState().getBoard(), 
        								lobbyState.getGameState().isWhiteToMove(), 
        								!lobbyState.getGameState().isWhiteToMove()));
            			responseMessagesSecond.add(MENU_MESSAGE);
                        secondUserState.setUserState(UserState.UserStatus.MAINMENU);
                        secondUserState.resetLobbyId();
                        secondUserState.getMoveState().clearMoveState();
        			}
                    games.remove(lobbyId);
        		}
        		}
        	}
        } else {
            responseMessagesFirst.add(OPPONENTS_MOVE);
        }
    }
    

	/**
	 * Отправить сообщения пользователю
	 */
	private void sendMessagesTo(long chatId, 
			Iterator<String> messagesTextsIterator, Bot bot) {
		long lastMessageId = bot.sendMessages(chatId, messagesTextsIterator);
		messages.put(chatId, lastMessageId);
	}

	/**
	 * Изменить сообщение
	 */
	private void editMessageOf(long chatId, long messageId, 
			String editedMessageText, boolean moreMessages, Bot bot) {
		bot.editMessage(chatId, messageId, editedMessageText, moreMessages);
	}
}
