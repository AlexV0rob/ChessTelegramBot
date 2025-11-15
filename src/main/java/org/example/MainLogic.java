package org.example;

import org.example.bots.Bot;
import org.example.bots.TelegramBot;
import org.example.buttons.IdentifiedButton;
import org.example.buttons.SimpleButton;

import org.example.inputHandlers.CommandHandler;
import org.example.inputHandlers.InGameInputHandler;
import org.example.inputHandlers.MainMenuInputHandler;

import org.example.states.UserState;

import java.util.HashMap;
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
	private Map<Long, UserState> games = new HashMap<Long, UserState>();
	
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
     * Обработать ввод в соответствии с режимом пользователя
     */
	public void processInput(Bot bot, String userInput, long chatId) {
		if (bot instanceof TelegramBot && tgBot == null) {
			tgBot = (TelegramBot) bot;
		}
    	if (!games.containsKey(chatId)) {
    		UserState.messengerType newUserMessenger = null;
    		if (bot instanceof TelegramBot) {
    			newUserMessenger = UserState.messengerType.TELEGRAM;
    		}
        	games.put(chatId, new UserState(newUserMessenger));
        }
    	UserState currentUserState = games.get(chatId);

        Matcher command = COMMAND_PATTERN.matcher(userInput);
        
        List<String> responseMessages = null;
        
        if (command.find()) {
        	String commandText = command.group(1).toLowerCase();
        	String commandArgument = command.group(2);
        	responseMessages = commandHandler.processCommand(
        			commandText, commandArgument, currentUserState);
        } else {
        	responseMessages = switch (currentUserState.getUserState()) {
        	case UserState.userState.MAINMENU -> inMenuHandler.processInput(
        				userInput, currentUserState);
        	case UserState.userState.INGAME -> inGameHandler.processInput(
        				userInput, currentUserState);
        	default -> commandHandler.processCommand(
        				"quit", "", currentUserState);
        	};
        }
		bot.sendMessages(chatId, responseMessages.iterator());
		/*
        switch(currentUserState.getUserMessenger()) {
        	case UserState.messengerType.TELEGRAM -> {
        	}
        }
        */
	}
	
	/**
	 * Получить простые кнопки в соответствии с режимом пользователя
	 */
	public List<SimpleButton> getCurrentSimpleButtons(Bot bot, long chatId) {
    	if (!games.containsKey(chatId)) {
    		UserState.messengerType newUserMessenger = null;
    		if (bot instanceof TelegramBot) {
    			newUserMessenger = UserState.messengerType.TELEGRAM;
    		}
        	games.put(chatId, new UserState(newUserMessenger));
        }
    	UserState currentUserState = games.get(chatId);
    	switch (currentUserState.getUserState()) {
        case UserState.userState.MAINMENU:
        	return buttonsCreator.getMenuButtons();
        case UserState.userState.INGAME:
        	return List.of();
        }
    	return List.of();
	}
	
	/**
	 * Получить идентифицированные кнопки в соответствии с режимом пользователя
	 */
	public List<IdentifiedButton> getCurrentIdentifiedButtons(Bot bot, long chatId) {
    	if (!games.containsKey(chatId)) {
    		UserState.messengerType newUserMessenger = null;
    		if (bot instanceof TelegramBot) {
    			newUserMessenger = UserState.messengerType.TELEGRAM;
    		}
        	games.put(chatId, new UserState(newUserMessenger));
        }
    	UserState currentUserState = games.get(chatId);
    	switch (currentUserState.getUserState()) {
        case UserState.userState.MAINMENU:
        	return List.of();
        case UserState.userState.INGAME:
        	return buttonsCreator.getGameButtons(
        			currentUserState.getMoveState(), 
        			currentUserState.getGameState().getBoard(),
        			currentUserState.getGameState().isWhiteToMove());
        }
    	return List.of();
	}
}
