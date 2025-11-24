package org.example;

import org.example.auxiliary.IdentifiedButton;
import org.example.auxiliary.SimpleButton;
import org.example.bots.Bot;
import org.example.bots.TelegramBot;
import org.example.states.UserState;
import org.example.statesHandlers.StatesHandler;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Главный логический модуль, получает идентификатор пользователя и
 * передаёт управление необходимому обработчику
 */
public class MainLogic {
	/**
	 * Хранитель и обработчик состояний пользователей в памяти
	 */
	private final StatesHandler statesHandler;
	
	/**
	 * Обработчик команд
	 */
	private final CommandHandler commandHandler;
	
	/**
	 * Обработчик игрового ввода
	 */
	private final GameInputHandler gameInputHandler;
	
    /**
     * Создатель кнопок
     */
    private final ButtonsCreator buttonsCreator = new ButtonsCreator();
    
    /**
     * Конвертер текстов кнопок
     */
    private final MenuButtonsConverter menuButtonsConverter = new MenuButtonsConverter();

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
            Pattern.compile("^__([prbnqkPRBNQK]|(?:[a-hA-H][1-8])|cancel)__$");
    
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
             - создавать матч на одном устройстве
             - создавать онлайн матч
            
            Пока что я могу только это, но список возможностей будет пополняться с течением разработки. 
            Отправь /help для большей информации.
            """;
    
    /**
     * Сообщение команды /help
     */
    private final static String HELP_MESSAGE = """
            Сейчас я могу:
             - создавать матч на одном устройстве
             - создавать онлайн матч
            
            Доступные команды:
            /start - перезапускает бота
            /help - показывает это сообщение
            /new_local - начинает новую игру на одном устройстве
            /create <argument> - переходит в режим создания матча или сразу создаёт с названием
            /join <argument> - переходит в режим создания матча или присоединияется по названию
            /quit - выходит в главное меню
            
            Скоро будет больше возможностей.
            """;
    
    /**
     * Сообщение в меню
     */
    private final static String MENU_MESSAGE = "Чем займёмся?";
    
    /**
     * Сообщение о начале игры
     */
    private final static String GAME_STARTED = "Игра началась";
	
	/**
	 * Сообщение о досрочном завершении матча
	 */
	private final static String SURRENDERED = "Ваш противник вышел. Матч завершён.";

	/**
	 * Сообщение об успешном создании матча
	 */
	private final static String LOBBY_BOOKED = """
			Матч %s создан и доступен для других игроков.
			Ожидайте присоединения противника
			""";
	
    /**
     * Сообщение о ходе противника
     */
    private final static String OPPONENTS_MOVE =
            "Вы не можете сейчас ходить. Дождитесь хода противника.";
    
    /**
     * Сообщение о неизвестном типе вводда в игре
     */
    private final static String UNKNOWN_GAME_INPUT = "Неизвестный тип ввода";
    
    /**
     * Ответ на неизвестную команду
     */
    private final static String UNKNOWN_COMMAND = "Неизвестная команда";
    
    /**
     * Конструктор, требует хранителя состояний
     */
    public MainLogic(StatesHandler currentStatesHandler) {
    	statesHandler = currentStatesHandler;
    	commandHandler = new CommandHandler(currentStatesHandler);
    	gameInputHandler = new GameInputHandler(currentStatesHandler);
    }    
    
    /**
     * Обработать ввод в соответствии с режимом пользователя
     */
    public void processInput(Bot bot, String userInput, long chatId) {
        if (bot instanceof TelegramBot && tgBot == null) {
            tgBot = (TelegramBot) bot;
        }
        if (!statesHandler.isUserExisting(chatId)) {
            UserState.MessengerType newUserMessenger = null;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.MessengerType.TELEGRAM;
            }
            statesHandler.addNewUser(chatId, newUserMessenger);
        }        
        Matcher command = COMMAND_PATTERN.matcher(userInput);
        ImmutablePair<List<String>, List<String>> responseMessages = null;
    	long secondChatId = 0;
    	String lobbyName = statesHandler.getUserLobbyName(chatId);
    	if (!lobbyName.isEmpty()) {
    		secondChatId = statesHandler.getLobbyAnotherUserId(lobbyName, chatId);
    	}
        if (command.find()) {
        	String argument = command.group(2);
        	if (argument == null) {
        		argument = "";
        	}
        	responseMessages = handleCommand(chatId, command.group(1), argument);
        } else {
        	responseMessages = handleByMode(chatId, userInput);
        }
        lobbyName = statesHandler.getUserLobbyName(chatId);
    	if (!lobbyName.isEmpty()) {
    		secondChatId = statesHandler.getLobbyAnotherUserId(lobbyName, chatId);
    	}
		List<String> firstMessages = responseMessages.getKey();
		List<String> secondMessages = responseMessages.getValue();
    	if (!firstMessages.isEmpty()) {
    		long messageId = statesHandler.getUserMessageId(chatId);
    		if (messageId >= 0) {
    			String messageText = firstMessages.removeFirst();
    			editMessage(bot, chatId, messageId, messageText, !firstMessages.isEmpty());
    		}
    		if (!firstMessages.isEmpty()) {
    			sendMessages(bot, chatId, firstMessages);
    		}
    	}
    	if (!secondMessages.isEmpty() && secondChatId != 0 && secondChatId != chatId) {
    		sendMessages(bot, secondChatId, secondMessages);    		
    	}
    }
        
    
    /**
     * Получить простые кнопки в соответствии с режимом пользователя
     */
    public List<SimpleButton> getCurrentSimpleButtons(Bot bot, long chatId) {
        if (!statesHandler.isUserExisting(chatId)) {
            UserState.MessengerType newUserMessenger = null;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.MessengerType.TELEGRAM;
            }
            statesHandler.addNewUser(chatId, newUserMessenger);
        }
        switch (statesHandler.getUserStatus(chatId)) {
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
        if (!statesHandler.isUserExisting(chatId)) {
            UserState.MessengerType newUserMessenger = null;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.MessengerType.TELEGRAM;
            }
            statesHandler.addNewUser(chatId, newUserMessenger);
        }
        switch (statesHandler.getUserStatus(chatId)) {
            case UserState.UserStatus.MAINMENU:
            case UserState.UserStatus.AWAITING:
            case UserState.UserStatus.CREATING:
                return List.of();
            case UserState.UserStatus.INGAME:
                String lobbyName = statesHandler.getUserLobbyName(chatId);
                if (statesHandler.isLobbyExisting(lobbyName)) {
                	if (userCanMove(chatId, lobbyName)) {
                    	return buttonsCreator.getGameButtons(
                    			statesHandler.getUserMovePartFigure(chatId),
                    			statesHandler.getUserMovePartStart(chatId),
                    			statesHandler.getUserMovePartFinish(chatId),
                    			statesHandler.getGameChessboard(lobbyName),
                    			statesHandler.isGameWhiteToMove(lobbyName));
                	}
                }
                return List.of();
            case UserState.UserStatus.CHOOSING:
                List<String> listOfLobbiesID = statesHandler.getBookedLobbies();
                return buttonsCreator.getLobbyButtons(listOfLobbiesID);
        }
        return List.of();
    }
    
    /**
	 * Обработка введённой команды
	 */
	private ImmutablePair<List<String>, List<String>> handleCommand(
			long chatId, String command, String argument) {
		List<String> responseMessagesFirst = new ArrayList<String>();
		List<String> responseMessagesSecond = new ArrayList<String>();
		try {
			switch (command) {
			case "start" -> {
				statesHandler.changeUserLastMessage(chatId, -1);
				commandHandler.processQuitCommand(chatId);
				responseMessagesFirst.add(START_MESSAGE);
				responseMessagesFirst.add(MENU_MESSAGE);
				responseMessagesSecond.add(SURRENDERED);
				responseMessagesSecond.add(MENU_MESSAGE);
			}
			case "help" -> {
				responseMessagesFirst.add(HELP_MESSAGE);
			}
			case "quit" -> {
				statesHandler.changeUserLastMessage(chatId, -1);
				commandHandler.processQuitCommand(chatId);
				responseMessagesFirst.add(MENU_MESSAGE);
			}
			case "new_local" -> {
				commandHandler.processQuitCommand(chatId);
				commandHandler.processNewLocalCommand(chatId);
				responseMessagesFirst.add(GAME_STARTED);
				ImmutablePair<List<String>, List<String>> gameMessages = 
						gameInputHandler.processMove(chatId, "", "", "");
				responseMessagesFirst.addAll(gameMessages.getKey());
			}
			case "create" -> {
				statesHandler.changeUserLastMessage(chatId, -1);
				commandHandler.processCreateCommand(chatId, argument);
				responseMessagesFirst.add(LOBBY_BOOKED.formatted(argument));
			}
			case "join" -> {
				commandHandler.processQuitCommand(chatId);
				boolean isFirstWhite = commandHandler.processJoinCommand(chatId, argument);
				responseMessagesFirst.add(GAME_STARTED);
				responseMessagesSecond.add(GAME_STARTED);
				ImmutablePair<List<String>, List<String>> gameMessages = 
						gameInputHandler.processMove(chatId, "", "", "");
				if (isFirstWhite) {
					responseMessagesFirst.addAll(gameMessages.getKey());
					responseMessagesSecond.addAll(gameMessages.getValue());
				} else {
					responseMessagesFirst.addAll(gameMessages.getValue());
					responseMessagesSecond.addAll(gameMessages.getKey());					
				}
			}
			default -> {;
				responseMessagesFirst.add(UNKNOWN_COMMAND);
			}
			}
		} catch (CommandException e) {
			responseMessagesFirst.add(e.getMessage());
		}
		return new ImmutablePair<>(responseMessagesFirst, responseMessagesSecond);
	}
	
	/**
	 * Обработать ввод в зависимости от режима пользователя
	 */
    private ImmutablePair<List<String>, List<String>> handleByMode(
    		long chatId, String userInput) {
    	switch (statesHandler.getUserStatus(chatId)) {
    	case UserState.UserStatus.MAINMENU -> {
    		String commandAnalog = menuButtonsConverter.getMenuCommand(userInput);
    		return handleCommand(chatId, commandAnalog, "");
    	}
    	case UserState.UserStatus.INGAME -> {
    		if (userCanMove(chatId, statesHandler.getUserLobbyName(chatId))) {
    			Matcher notationMatch = NOTATION_PATTERN.matcher(userInput);
        		Matcher callbackMatch = MOVE_PART_PATTERN.matcher(userInput);
        		if (callbackMatch.find()) {
            		return gameInputHandler.processMovePart(
            				chatId, callbackMatch.group(1));
            	} else if (notationMatch.find()) {
            		return gameInputHandler.processMove(
            				chatId, 
            				notationMatch.group(1).toLowerCase(), 
            				notationMatch.group(2).toLowerCase(), 
            				notationMatch.group(3).toLowerCase());
            	} else {
            		List<String> messages = new ArrayList<String>();
            		messages.add(UNKNOWN_GAME_INPUT);
            		return new ImmutablePair<>(messages, List.of());
        		}
    		} else {
        		List<String> messages = new ArrayList<String>();
        		messages.add(OPPONENTS_MOVE);
    			return new ImmutablePair<>(messages, List.of());
    		}
    	}
    	case UserState.UserStatus.AWAITING -> {
    		String commandAnalog = menuButtonsConverter.getMenuCommand(userInput);
    		return handleCommand(chatId, commandAnalog, "");    		
    	}
    	case UserState.UserStatus.CREATING -> {
    		return handleCommand(chatId, "create", userInput);	
    	}
    	case UserState.UserStatus.CHOOSING -> {
        	Matcher callbackMatch = LOBBY_BUTTON_PATTERN.matcher(userInput);
        	if (callbackMatch.find()) {
        		return handleCommand(chatId, "join", callbackMatch.group(1));
        	} else {
        		return handleCommand(chatId, "join", userInput);
        	}
    	}
    	}
    	return new ImmutablePair<>(List.of(), List.of());
	}
    
    /**
	 * Отправить сообщения пользователю
	 */
	private void sendMessages(Bot bot, long chatId, List<String> messagesTexts) {
		long lastMessageId = bot.sendMessages(chatId, messagesTexts);
		statesHandler.changeUserLastMessage(chatId, lastMessageId);
	}

	/**
	 * Изменить сообщение
	 */
	private void editMessage(Bot bot, long chatId, 
			long messageId, String editedMessageText, boolean moreMessages) {
		bot.editMessage(chatId, messageId, editedMessageText, moreMessages);
	}
	
	/**
     * Проверить, что пользователь может сделать ход
     */
    private boolean userCanMove(long chatId, String lobbyName) {
    	return (chatId == statesHandler.getLobbyFirstPlayer(lobbyName) 
    			&& statesHandler.isLobbyFirstPlayerToMove(lobbyName)) 
    			|| (chatId == statesHandler.getLobbySecondPlayer(lobbyName) 
    			&& !statesHandler.isLobbyFirstPlayerToMove(lobbyName));
	}
}
