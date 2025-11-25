package org.example;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.example.bots.DiscordBot;
import org.example.bots.TelegramBot;
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

        //Попытка запуска Телеграм бота
        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new TelegramBot(botToken));
            System.out.println("Телеграмм бот запущен");
            String token = System.getenv("discord_bot_token");
            DiscordBot discordBot = new DiscordBot(token);
            discordBot.consume();
            System.out.println("Дискорд бот запущен");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}