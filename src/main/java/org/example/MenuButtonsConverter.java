package org.example;

/**
 * Конвертер кнопок меню в соответствующие им команды
 */
public class MenuButtonsConverter {
	/**
     * Текст кнопки для начала игры в главном меню
     */
    private final static String NEW_SINGLE_GAME = "Начать новую одиночную игру";

    /**
     * Текст кнопки для начала игры в главном меню
     */
    private final static String NEW_MULTIPLAYER_GAME = "Создать многопользовательский матч";

    /**
     * Текст кнопки для начала игры в главном меню
     */
    private final static String JOIN_MULTIPLAYER_GAME = "Присоединится к существующему матчу";

    /**
     * Текст кнопки для выхода из режима ожидания
     */
    private final static String QUIT_LOBBY = "Отменить поиск соперника и удалить матч";
    
    /**
     * Получить командный эквивалент запросу меню
     */
    public String getMenuCommand(String menuQuery) {
    	return switch (menuQuery) {
    	case NEW_SINGLE_GAME -> "new_local";
    	case NEW_MULTIPLAYER_GAME -> "create";
    	case JOIN_MULTIPLAYER_GAME -> "join";
    	case QUIT_LOBBY -> "quit";
    	default -> "unknown";
    	};
    }
    
    /**
     * Получить текстовый эквивалент команде
     */
    public String getCommandText(String command) {
    	return switch (command) {
    	case "new_local" -> NEW_SINGLE_GAME;
    	case "create" -> NEW_MULTIPLAYER_GAME;
    	case "join" -> JOIN_MULTIPLAYER_GAME;
    	case "quit" -> QUIT_LOBBY;
    	default -> "Неизвестно";
    	};
    }
}
