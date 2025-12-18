package org.example;

import org.example.bots.DiscordBot;
import org.example.bots.TelegramBot;
import org.example.statesHandlers.DatabaseException;
import org.example.statesHandlers.DatabaseStatesHandler;
import org.example.statesHandlers.MemoryStatesHandler;
import org.example.statesHandlers.StatesHandler;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

/**
 * Основной класс, где происходит запуск бота
 */
public class Main {
	/**
	 * Точка входа программы. 
	 */
	public static void main(String[] args) {
		String botToken = System.getenv("telegram_bot_token");
		String databaseURL = "jdbc:sqlite:./target/database/states.db";
		StatesHandler statesHandler;
		try {
			statesHandler = new DatabaseStatesHandler(databaseURL);
            System.out.println("Соединение с базой данных установлено");
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			statesHandler = new MemoryStatesHandler();
            System.out.println("Соединение с базой данных не установлено");
		}
        MainLogic mainLogic = new MainLogic(statesHandler);
        try {
        	TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new TelegramBot(botToken, mainLogic));
            System.out.println("Телеграм бот запущен");
        } catch (TelegramApiException e) {
            System.out.println("Can't connect to Telegram");
            e.printStackTrace();
        }
        try {
        	JDA api = JDABuilder.createDefault(System.getenv("discord_bot_token")).build();
        	api.addEventListener(new DiscordBot(api, mainLogic));
            System.out.println("Дискорд бот запущен");
        } catch (ErrorResponseException e) {
        	System.out.println("Can't connect to Discord");
            e.printStackTrace();
        }
    }
}