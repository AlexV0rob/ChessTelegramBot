package org.example;

import java.sql.SQLException;

import api.longpoll.bots.exceptions.VkApiException;
import org.example.bots.TelegramBot;
import org.example.statesHandlers.DatabaseStatesHandler;
import org.example.statesHandlers.MemoryStatesHandler;
import org.example.statesHandlers.StatesHandler;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

/**
 * Основной класс, где происходит запуск бота
 */
public class Main {
    /**
     * Точка входа программы.
     */
    public static void main(String[] args) throws VkApiException {
        String botToken = System.getenv("telegram_bot_token");
        String databaseURL = "jdbc:sqlite:./src/main/resources/states.db";
        StatesHandler statesHandler;
        try {
            statesHandler = new DatabaseStatesHandler(databaseURL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            statesHandler = new MemoryStatesHandler();
        }
        MainLogic mainLogic = new MainLogic(statesHandler);
        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new TelegramBot(botToken, mainLogic));
            System.out.println("Телеграм бот запущен");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.out.println("Couldn't connect to telegram");
            e.printStackTrace();
        }


    }
}