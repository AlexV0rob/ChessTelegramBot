package org.example.bots;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import org.example.MainLogic;
import reactor.core.publisher.Mono;

import java.util.Iterator;

public class DiscordBot implements Bot {
    /**
     * Экземпляр класса MainLogic, обрабатывает входящие сообщения
     */
    private MainLogic mainLogic = new MainLogic();

    /**
     * Экземпляр класса DiscordClient
     */
    private final DiscordClient discordClient;

    /**
     * Конструктор класса
     */
    public DiscordBot(String botToken) {
        discordClient = DiscordClient.create(botToken);
    }

    public void consume() {
        Mono<Void> login = discordClient.withGateway((GatewayDiscordClient gateway) ->
                gateway.on(MessageCreateEvent.class, event -> {
                    Message message = event.getMessage();
                    processTextMessage(message.getContent(), message.getChannelId().asLong());
                    return Mono.empty();
                }));
        login.block();
    }

    /**
     * Обработать текстовое сообщение, полученное от пользователя
     */
    private void processTextMessage(String incomingMessage, long chatId) {
        mainLogic.processInput(this, incomingMessage, chatId);
    }

    /**
     * Отправить список сообщений
     */
    public long sendMessages(long chatId, Iterator<String> messagesTextsIterator) {
        if (messagesTextsIterator == null || !messagesTextsIterator.hasNext()) {
            return -1;
        }
        Snowflake channelSnowflake = Snowflake.of(chatId);
        MessageChannel channel = discordClient.login().ofType(MessageChannel.class).block();
        if (channel == null) {
            return -1;
        }
        while (messagesTextsIterator.hasNext()) {
            String messageText = messagesTextsIterator.next();
            if (messageText != null) {
                channel.createMessage(MessageCreateSpec.builder()
                        .content(messageText)
                        .build()).block();
            }
        }
        return chatId;

    }

    @Override
    public void editMessage(long chatId, long messageId, String editedMessageText, boolean moreMessages) {
        //Ничего
    }
}
