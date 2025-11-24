package org.example;

/**
 * Конвертер кнопок меню в соответствующие им команды
 */
public class MenuButtonsConverter {
	/**
     * Текст кнопки для начала игры в главном меню
     */
    private final static String NEW_SINGLE_GAME = "Начать игру на этом устройстве";

    /**
     * Текст кнопки для начала игры в главном меню
     */
    private final static String NEW_MULTIPLAYER_GAME = "Создать собственное лобби";

    /**
     * Текст кнопки для начала игры в главном меню
     */
    private final static String JOIN_MULTIPLAYER_GAME = "Присоединится к чужому лобби";

    /**
     * Текст кнопки для выхода из режима ожидания
     */
    private final static String QUIT_LOBBY = "Отменить поиск матча и удалить лобби";
    
    /**
     * Получить командный эквивалент запросу меню
     */
    public String getMenuCommand(String menuQuery) {
    	return switch (menuQuery) {
    	case NEW_SINGLE_GAME -> "newsinglegame";
    	case NEW_MULTIPLAYER_GAME -> "creategame";
    	case JOIN_MULTIPLAYER_GAME -> "joingame";
    	case QUIT_LOBBY -> "quit";
    	default -> "unknown";
    	};
    }
    
    /**
     * Получить текстовый эквивалент команде
     */
    public String getCommandText(String command) {
    	return switch (command) {
    	case "newsinglegame" -> NEW_SINGLE_GAME;
    	case "creategame" -> NEW_MULTIPLAYER_GAME;
    	case "joingame" -> JOIN_MULTIPLAYER_GAME;
    	case "quit" -> QUIT_LOBBY;
    	default -> "Неизвестно";
    	};
    }
}
