package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainLogic {
	private final InGameInputHandler inGameHandler = new InGameInputHandler();
	private final MainMenuInputHandler inMenuHandler = new MainMenuInputHandler();
	private final CommandHandler commandHandler = new CommandHandler();

	private Map<Long, UserState> games = new HashMap<Long, UserState>();
    
    private final static Pattern COMMAND_PATTERN =
            Pattern.compile("^/([\\w]+)(?: ([\\wа-яА-ЯёЁ]+))?");
	
	public String processInput(String userInput, long chatId) {
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
        case UserState.USER_STATE.MAINMENU:
        	return inMenuHandler.processInput(userInput, currentUserState);
        case UserState.USER_STATE.INGAME:
        	return inGameHandler.processInput(userInput, currentUserState);
        }
        return commandHandler.processCommand("quit", "", currentUserState);
	}
}
