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
    private final static String LINK_MESSENGER = "Привязать текущий мессенджер";
    /**
     * Текст кнопки для выхода из режима ожидания
     */
    private final static String NEW_MESSENGER = "Зарегистрироваться как новый пользователь";

    /**
     * Текст кнопки для выхода из режима ожидания
     */
    private final static String QUIT_LOBBY = "Отменить поиск соперника и удалить матч";
    
    /**
     * Текст кнопки вывода таблицы лидеров
     */
    private final static String LEADER_BOARD = "Таблица лидеров";

    /**
     * Получить командный эквивалент запросу меню
     */
    public String getMenuCommand(String menuQuery) {
        return switch (menuQuery) {
            case NEW_SINGLE_GAME -> "new_local";
            case NEW_MULTIPLAYER_GAME -> "create";
            case JOIN_MULTIPLAYER_GAME -> "join";
            case LINK_MESSENGER -> "link";
            case QUIT_LOBBY -> "quit";
            case NEW_MESSENGER -> "new_messenger";
            case LEADER_BOARD -> "leadertable";
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
            case "link" -> LINK_MESSENGER;
            case "new_messenger" -> NEW_MESSENGER;
            case "leadertable" -> LEADER_BOARD;
            default -> "Неизвестно";
        };
    }
}
