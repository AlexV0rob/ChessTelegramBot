package org.example.inputHandlers;

import org.example.auxiliary.CommandResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Обработчик хода фигуры
 */
public class CommandInputHandler {
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
     * Сообщение о выборе лобби
     */
    private final static String CHOOSE_GAME = "Выберите Лобби:";
    /**
     * Сообщение о подключении к игре
     */
    private final static String JOIN_GAME = "Пытаюсь подключить вас к игре";
    /**
     * Сообщение, оповещающее о создании лобби
     */
    private final static String GAME_CREATED = "Лобби создано";

    /**
     * Определить тип команды, поменять при необходимости на соответсвующий режим
     * и отправить ответ
     */
    public CommandResults processCommand(String command) {
        List<String> responseTextsFirst = new ArrayList<String>();
        List<String> responseTextsSecond = new ArrayList<String>();
        CommandResults.LobbyStatus commandLobbyStatus = CommandResults.LobbyStatus.NOTHING;
        switch (command) {
            case "start", "quit" -> {
                if (command.equals("start")) {
                    responseTextsFirst.add(START_MESSAGE);
                }
                responseTextsFirst.add(MENU_MESSAGE);
                commandLobbyStatus = CommandResults.LobbyStatus.CLOSE;
            }
            case "help" -> {
                responseTextsFirst.add(HELP_MESSAGE);
            }
            case "newsinglegame" -> {
                responseTextsFirst.add(GAME_STARTED);
                commandLobbyStatus = CommandResults.LobbyStatus.SINGLEPLAYER;
            }
            case "joingame" -> {
                responseTextsFirst.add(CHOOSE_GAME);
                commandLobbyStatus = CommandResults.LobbyStatus.JOIN;
            }
            case "newmultiplayergame" -> {
                responseTextsFirst.add(GAME_CREATED);
                commandLobbyStatus = CommandResults.LobbyStatus.MULTIPLAYER;

            }
            case "joinlobby" -> {
                responseTextsFirst.add(JOIN_GAME);
                commandLobbyStatus = CommandResults.LobbyStatus.JOIN;
            }

            default -> {
                responseTextsFirst.add(UNKNOWN_MESSAGE);
            }
        }
        return new CommandResults(commandLobbyStatus, List.of(
                responseTextsFirst, responseTextsSecond));
    }
}
