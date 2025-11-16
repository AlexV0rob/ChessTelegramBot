package org.example.inputHandlers;

import java.util.ArrayList;
import java.util.List;

import org.example.GameTranslator;
import org.example.auxiliary.CommandResults;

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
    public CommandResults processCommand(String command) {
    	List<String> responseTextsFirst = new ArrayList<String>();
    	List<String> responseTextsSecond = new ArrayList<String>();
    	CommandResults.lobbyStatus commandLobbyStatus = CommandResults.lobbyStatus.NOTHING;
        switch (command) {
            case "start", "quit" -> {
                if (command.equals("start")) {
                	responseTextsFirst.add(START_MESSAGE);
                }
                responseTextsFirst.add(MENU_MESSAGE);
                commandLobbyStatus = CommandResults.lobbyStatus.CLOSE;
            }
            case "help" -> {
            	responseTextsFirst.add(HELP_MESSAGE);
            }
            case "newsinglegame" -> {
            	responseTextsFirst.add(GAME_STARTED);
            	commandLobbyStatus = CommandResults.lobbyStatus.SINGLEPLAYER;
            }
            default -> {
            	responseTextsFirst.add(UNKNOWN_MESSAGE);
            }
        }
        return new CommandResults(commandLobbyStatus, List.of(
        		responseTextsFirst, responseTextsSecond));
    }
}
