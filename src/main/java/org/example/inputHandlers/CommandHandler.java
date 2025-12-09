package org.example.inputHandlers;

import java.util.List;

import org.example.GameTranslator;
import org.example.chess.GameHandler;
import org.example.states.UserState;

/**
 * Обработчик хода фигуры
 */
public class CommandHandler {
    /**
     * Переводчик игры
     */
    private final GameTranslator gameTranslator = new GameTranslator();
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
     * Определить тип команды, поменять при необходимости на соответсвующий режим
     * и отправить ответ
     */
    public List<String> processCommand(String command, String argument,
                                       UserState currentUserState) {
        switch (command) {
            case "start", "quit" -> {
                currentUserState.setUserState(UserState.userStatus.MAIN_MENU);
                if (command.equals("start")) {
                    return List.of(START_MESSAGE, MENU_MESSAGE);
                }
                return List.of(MENU_MESSAGE);
            }
            case "help" -> {
                return List.of(HELP_MESSAGE);
            }
            case "newsinglegame" -> {
                currentUserState.setUserState(UserState.userStatus.IN_GAME);
                currentUserState.resetGameState();
                return List.of(
                        GAME_STARTED,
                        gameTranslator.chessboardString(
                                GameHandler.MoveProperty.REGULAR,
                                currentUserState.getGameState().getBoard(),
                                currentUserState.getGameState().isWhiteToMove()),
                        YOUR_MOVE);
            }
            default -> {
                return List.of(UNKNOWN_MESSAGE);
            }
        }
    }
}
