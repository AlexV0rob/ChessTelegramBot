package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

class User
{
	boolean isWhite;
	byte[] chessDesk;
	byte[] getDesk()
	{
		return chessDesk;
	}
	boolean doesWhitesMove() {
		return isWhite;
	}
}
public class Main {

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