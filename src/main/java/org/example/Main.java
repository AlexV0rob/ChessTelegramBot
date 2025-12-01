package org.example;

import org.example.bots.DiscordBot;
import org.example.bots.TelegramBot;
import org.example.statesHandlers.DatabaseException;
import org.example.statesHandlers.DatabaseStatesHandler;
import org.example.statesHandlers.MemoryStatesHandler;
import org.example.statesHandlers.StatesHandler;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

/**
 * Основной класс, где происходит запуск бота
 */
public class Main {
	/**
	 * Точка входа программы. 
	 */
	public static void main(String[] args) {
		String botToken = System.getenv("telegram_bot_token");
		String databaseURL = "jdbc:sqlite:./src/main/resources/states.db";
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
        } catch (Exception e) {
        	System.out.println("Couldn't connect to telegram");
            e.printStackTrace();
        }
        try {
        	JDA api = JDABuilder.createDefault(System.getenv("discord_bot_token")).build();
        	api.addEventListener(new DiscordBot(api, mainLogic));
            System.out.println("Дискорд бот запущен");
        } catch (Exception e) {
        	System.out.println("Couldn't connect to discord");
            e.printStackTrace();
        }
    }
}