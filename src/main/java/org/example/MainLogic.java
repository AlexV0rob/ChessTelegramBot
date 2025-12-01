package org.example;

import org.example.auxiliary.IdentifiedButton;
import org.example.auxiliary.SimpleButton;
import org.example.bots.Bot;
import org.example.bots.DiscordBot;
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
     * Экземпляр DiscordBot для отправки сообщений в Дискорд
     */
    private DiscordBot dsBot = null;

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
    	long userId = getSystemId(bot, chatId);
        Matcher command = COMMAND_PATTERN.matcher(userInput);
        ImmutablePair<List<String>, List<String>> responseMessages = null;
    	long secondUserId = 0;
    	String lobbyName = statesHandler.getUserLobbyName(userId);
    	if (!lobbyName.isEmpty()) {
    		secondUserId = statesHandler.getLobbyAnotherUserId(lobbyName, userId);
    	}
        if (command.find()) {
        	String argument = command.group(2);
        	if (argument == null) {
        		argument = "";
        	}
        	responseMessages = handleCommand(userId, command.group(1), argument);
        } else {
        	responseMessages = handleByMode(userId, userInput);
        }
        lobbyName = statesHandler.getUserLobbyName(userId);
    	if (!lobbyName.isEmpty()) {
    		secondUserId = statesHandler.getLobbyAnotherUserId(lobbyName, userId);
    	}
		List<String> firstMessages = responseMessages.getKey();
		List<String> secondMessages = responseMessages.getValue();
    	if (!firstMessages.isEmpty()) {
    		long messageId = statesHandler.getUserMessageId(userId);
    		if (messageId >= 0) {
    			String messageText = firstMessages.removeFirst();
    			editMessage(bot, userId, messageId, messageText, !firstMessages.isEmpty());
    		}
    		if (!firstMessages.isEmpty()) {
    			sendMessages(bot, userId, firstMessages);
    		}
    	}
    	if (!secondMessages.isEmpty() && secondUserId != 0 && secondUserId != userId) {
    		Bot botToSend = switch (statesHandler.getUserMessenger(secondUserId)) {
    		case UserState.MessengerType.TELEGRAM -> tgBot;
    		case UserState.MessengerType.DISCORD -> dsBot;
    		default -> bot;
    		};
    		sendMessages(botToSend, secondUserId, secondMessages);    		
    	}
    }
        
    
    /**
     * Получить простые кнопки в соответствии с режимом пользователя
     */
    public List<SimpleButton> getCurrentSimpleButtons(Bot bot, long chatId) {
    	long userId = getSystemId(bot, chatId);
        switch (statesHandler.getUserStatus(userId)) {
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
    	long userId = getSystemId(bot, chatId);
        switch (statesHandler.getUserStatus(userId)) {
            case UserState.UserStatus.MAINMENU:
            case UserState.UserStatus.AWAITING:
            case UserState.UserStatus.CREATING:
                return List.of();
            case UserState.UserStatus.INGAME:
                String lobbyName = statesHandler.getUserLobbyName(userId);
                if (statesHandler.isLobbyExisting(lobbyName)) {
                	if (userCanMove(userId, lobbyName)) {
                    	return buttonsCreator.getGameButtons(
                    			statesHandler.getUserMovePartFigure(userId),
                    			statesHandler.getUserMovePartStart(userId),
                    			statesHandler.getUserMovePartFinish(userId),
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
     * Получить внутренний идентификатор системы
     */
    private long getSystemId(Bot bot, long chatId) {
    	if (bot instanceof TelegramBot && tgBot == null) {
            tgBot = (TelegramBot) bot;
        }
    	if (bot instanceof DiscordBot && dsBot == null) {
            dsBot = (DiscordBot) bot;
        }
        long userId = 0;
        if (bot instanceof TelegramBot) {
        	userId = statesHandler.getUserIdFromTelegramId(chatId);
        } else if (bot instanceof DiscordBot) {
        	userId = statesHandler.getUserIdFromDiscordId(chatId);
        } else {
        	userId = statesHandler.getUserIdFromUnknownId(chatId);
        }
        if (userId == 0) {
            UserState.MessengerType newUserMessenger = UserState.MessengerType.UNKNOWN;
            if (bot instanceof TelegramBot) {
                newUserMessenger = UserState.MessengerType.TELEGRAM;
            }
            if (bot instanceof DiscordBot) {
                newUserMessenger = UserState.MessengerType.DISCORD;
            }
            userId = statesHandler.addNewUser(newUserMessenger);
            statesHandler.addNewMessengerId(userId, newUserMessenger, chatId);
        }
        return userId;
    }
    
    /**
	 * Обработка введённой команды
	 */
	private ImmutablePair<List<String>, List<String>> handleCommand(
			long userId, String command, String argument) {
		List<String> responseMessagesFirst = new ArrayList<String>();
		List<String> responseMessagesSecond = new ArrayList<String>();
		try {
			switch (command) {
			case "start" -> {
				statesHandler.changeUserLastMessage(userId, -1);
				commandHandler.processQuitCommand(userId);
				responseMessagesFirst.add(START_MESSAGE);
				responseMessagesFirst.add(MENU_MESSAGE);
				responseMessagesSecond.add(SURRENDERED);
				responseMessagesSecond.add(MENU_MESSAGE);
			}
			case "help" -> {
				responseMessagesFirst.add(HELP_MESSAGE);
			}
			case "quit" -> {
				statesHandler.changeUserLastMessage(userId, -1);
				commandHandler.processQuitCommand(userId);
				responseMessagesFirst.add(MENU_MESSAGE);
			}
			case "new_local" -> {
				commandHandler.processQuitCommand(userId);
				commandHandler.processNewLocalCommand(userId);
				responseMessagesFirst.add(GAME_STARTED);
				ImmutablePair<List<String>, List<String>> gameMessages = 
						gameInputHandler.processMove(userId, "", "", "");
				responseMessagesFirst.addAll(gameMessages.getKey());
			}
			case "create" -> {
				statesHandler.changeUserLastMessage(userId, -1);
				commandHandler.processCreateCommand(userId, argument);
				responseMessagesFirst.add(LOBBY_BOOKED.formatted(argument));
			}
			case "join" -> {
				commandHandler.processQuitCommand(userId);
				boolean isFirstWhite = commandHandler.processJoinCommand(userId, argument);
				responseMessagesFirst.add(GAME_STARTED);
				responseMessagesSecond.add(GAME_STARTED);
				ImmutablePair<List<String>, List<String>> gameMessages = 
						gameInputHandler.processMove(userId, "", "", "");
				if (isFirstWhite) {
					responseMessagesFirst.addAll(gameMessages.getValue());
					responseMessagesSecond.addAll(gameMessages.getKey());
				} else {
					responseMessagesFirst.addAll(gameMessages.getKey());
					responseMessagesSecond.addAll(gameMessages.getValue());					
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
    		long userId, String userInput) {
    	switch (statesHandler.getUserStatus(userId)) {
    	case UserState.UserStatus.MAINMENU -> {
    		String commandAnalog = menuButtonsConverter.getMenuCommand(userInput);
    		return handleCommand(userId, commandAnalog, "");
    	}
    	case UserState.UserStatus.INGAME -> {
    		if (userCanMove(userId, statesHandler.getUserLobbyName(userId))) {
    			Matcher notationMatch = NOTATION_PATTERN.matcher(userInput);
        		Matcher callbackMatch = MOVE_PART_PATTERN.matcher(userInput);
        		if (callbackMatch.find()) {
            		return gameInputHandler.processMovePart(
            				userId, callbackMatch.group(1));
            	} else if (notationMatch.find()) {
            		return gameInputHandler.processMove(
            				userId, 
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
    		return handleCommand(userId, commandAnalog, "");    		
    	}
    	case UserState.UserStatus.CREATING -> {
    		return handleCommand(userId, "create", userInput);	
    	}
    	case UserState.UserStatus.CHOOSING -> {
        	Matcher callbackMatch = LOBBY_BUTTON_PATTERN.matcher(userInput);
        	if (callbackMatch.find()) {
        		return handleCommand(userId, "join", callbackMatch.group(1));
        	} else {
        		return handleCommand(userId, "join", userInput);
        	}
    	}
    	case UserState.UserStatus.MESSENGER_CHOOSING -> {
    		List<String> messages = new ArrayList<String>();
    		try {
    			messages.add(commandHandler.processLinkCommand(userId, "", null, 0));
    		} catch (CommandException e) {
    			messages.add(e.getMessage());
    		}
    		return new ImmutablePair<>(messages, List.of());
    	}
    	case UserState.UserStatus.ID_ENTERING -> {
    		List<String> messages = new ArrayList<String>();
    		try {
    			messages.add(commandHandler.processLinkCommand(userId, userInput, null, 0));
    		} catch (CommandException e) {
    			messages.add(e.getMessage());
    		}
    		return new ImmutablePair<>(messages, List.of());
    	}
    	}
    	return new ImmutablePair<>(List.of(), List.of());
	}
    
    /**
	 * Отправить сообщения пользователю
	 */
	private void sendMessages(Bot bot, long userId, List<String> messagesTexts) {
		UserState.MessengerType userMessenger = statesHandler.getUserMessenger(userId);
		long chatId = statesHandler.getUserMessengerId(userId, userMessenger);
		if (chatId != 0) {
			Bot botToSend = switch (userMessenger) {
			case UserState.MessengerType.UNKNOWN -> bot;
			case UserState.MessengerType.TELEGRAM -> tgBot;
			case UserState.MessengerType.DISCORD -> bot;
			};
			long lastMessageId = botToSend.sendMessages(chatId, messagesTexts);
			statesHandler.changeUserLastMessage(userId, lastMessageId);
		}
	}

	/**
	 * Изменить сообщение
	 */
	private void editMessage(Bot bot, long userId, 
			long messageId, String editedMessageText, boolean moreMessages) {
		UserState.MessengerType userMessenger = statesHandler.getUserMessenger(userId);
		long chatId = statesHandler.getUserMessengerId(userId, userMessenger);
		if (chatId != 0) {
			Bot botToSend = switch (userMessenger) {
			case UserState.MessengerType.UNKNOWN -> bot;
			case UserState.MessengerType.TELEGRAM -> tgBot;
			case UserState.MessengerType.DISCORD -> bot;
			};
			botToSend.editMessage(chatId, messageId, editedMessageText, moreMessages);
		}
	}
	
	/**
     * Проверить, что пользователь может сделать ход
     */
    private boolean userCanMove(long userId, String lobbyName) {
    	return (userId == statesHandler.getLobbyFirstPlayer(lobbyName) 
    			&& statesHandler.isLobbyFirstPlayerToMove(lobbyName)) 
    			|| (userId == statesHandler.getLobbySecondPlayer(lobbyName) 
    			&& !statesHandler.isLobbyFirstPlayerToMove(lobbyName));
	}
}
