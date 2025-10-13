package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

/**
 * Основной класс, где происходит запуск бота
 */

public class Main {
	/**
	 * Точка входа программы. 
	 */
	public static void main(String[] args) {
		//Берем токен бота из Environment
		String botToken = System.getenv("telegram_bot_token");
		
		//Попытка запуска
        try {
        	TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new TelegramBot(botToken));
            System.out.println("Бот запущен");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
