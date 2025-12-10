package org.example;

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
	 * Скомпилированный паттерн команды
	 */
    private final static Pattern COMMAND_PATTERN =
            Pattern.compile("^/([\\w]+)(?: ([\\wа-яА-ЯёЁ]+))?");
	
    /**
     * Обработать ввод в соответствии с режимом пользователя
     */
	public List<String> processInput(String userInput, long chatId) {
    	if (!games.containsKey(chatId)) {
        	games.put(chatId, new UserState());
        }
    	UserState currentUserState = games.get(chatId);

        Matcher command = COMMAND_PATTERN.matcher(userInput);
        
        if (command.find()) {
        	String commandText = command.group(1).toLowerCase();
        	String commandArgument = command.group(2);
        	return commandHandler.processCommand(commandText, commandArgument, currentUserState);
        }
        switch (currentUserState.getUserState()) {
        case UserState.userStatus.MAIN_MENU:
        	return inMenuHandler.processInput(userInput, currentUserState);
        case UserState.userStatus.IN_GAME:
        	return inGameHandler.processInput(userInput, currentUserState);
        }
        return commandHandler.processCommand("quit", "", currentUserState);
	}
	
	/**
	 * Получить простые кнопки в соответствии с режимом пользователя
	 */
	public List<SimpleButton> getCurrentSimpleButtons(long chatId) {
    	if (!games.containsKey(chatId)) {
        	games.put(chatId, new UserState());
        }
    	UserState currentUserState = games.get(chatId);
    	switch (currentUserState.getUserState()) {
        case UserState.userStatus.MAIN_MENU:
        	return buttonsCreator.getMenuButtons();
        case UserState.userStatus.IN_GAME:
        	return List.of();
        }
    	return List.of();
	}
	
	/**
	 * Получить идентифицированные кнопки в соответствии с режимом пользователя
	 */
	public List<IdentifiedButton> getCurrentIdentifiedButtons(long chatId) {
    	if (!games.containsKey(chatId)) {
        	games.put(chatId, new UserState());
        }
    	UserState currentUserState = games.get(chatId);
    	switch (currentUserState.getUserState()) {
        case UserState.userStatus.MAIN_MENU:
        	return List.of();
        case UserState.userStatus.IN_GAME:
        	return buttonsCreator.getGameButtons(
        			currentUserState.getMoveState(), 
        			currentUserState.getGameState().getBoard(),
        			currentUserState.getGameState().isWhiteToMove());
        }
    	return List.of();
	}
}
