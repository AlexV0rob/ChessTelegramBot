package org.example.bots;

import java.util.List;

import org.example.MainLogic;
import org.example.states.UserState;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Discord бот
 */
public class DiscordBot extends ListenerAdapter implements Bot {
    /**
     * Экземпляр класса для работы с Discord
     */
    private final JDA jda;

    /**
     * Обработчик логики бота
     */
    private final MainLogic mainLogic;

    /**
     * Конструктор класса
     */
    public DiscordBot(JDA jdaClient, MainLogic logic) {
        mainLogic = logic;
        jda = jdaClient;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            String userInput = event.getMessage().getContentRaw();
            long chatId = event.getChannel().getIdLong();
            mainLogic.processInput(this, userInput, chatId, getUsernameByID(chatId));
        }
    }

    /**
     * Отправить сообщение
     */
    private void sendMessage(long chatId, String messageText) {
        PrivateChannel channel = jda.getPrivateChannelById(chatId);
        if (channel != null) {
            channel.sendMessage(messageText).queue();
        }
    }

    @Override
    public long sendMessages(long chatId, List<String> messagesTexts) {
        for (String messageText : messagesTexts) {
            sendMessage(chatId, messageText);
        }
        return -1;
    }

    /**
     * Получить Имя пользователя
     */
    private String getUsernameByID(long userId) {
        String userName = jda.retrieveUserById(userId).complete().getName();
        return userName;
    }

    @Override
    public void editMessage(long chatId, long messageId, String editedMessageText, boolean moreMessages) {
        sendMessage(chatId, editedMessageText);
    }

    @Override
    public UserState.MessengerType getBotMessengerType() {
        return UserState.MessengerType.DISCORD;
    }
}
