package org.example.bots;

import java.util.List;

import org.example.MainLogic;
import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.model.objects.basic.Message;
import api.longpoll.bots.model.events.messages.MessageNew;

public class VKBot extends LongPollBot implements Bot {
    /**
     * Экземпляр класса MainLogic, обрабатывает входящие сообщения
     */
    private final MainLogic logic;

    public VKBot(MainLogic mainLogic) {
        logic = mainLogic;
    }

    /**
     * Обработать текстовое сообщение, полученное от пользователя
     */
    private void processTextMessage(String incomingMessage, long chatId) {
        logic.processInput(this, incomingMessage, chatId);
    }

    @Override
    public String getAccessToken() {
        return "vk1.a.Ml3xWPSnkVxoYUgR7UgfzJXLNE5Cqj6AqC4KmuGOBE-" +
                "rgrKsk1GswCnRJbf0v4joxmITV-nXJo8Towlh_toWSAc9Ig0nXuayE5sK8avNr4_" +
                "Fr42zxLbEG8Ml5nMg5_RZDsSRXYaXn_3ss_Pj60b4JR2GTdP9kWa2Xj6EBTmj4ECQqx2HBeVDIHYYezXzL7jW57v8iBP5Qu" +
                "-K-gpO4v82wQ";
    }

    @Override
    public void onMessageNew(MessageNew messageNew) {
        Message message = messageNew.getMessage();
        if (message.hasText()) {
            processTextMessage(message.getText(), message.getPeerId());
        }

    }

    private int sendMessage(long chatId, String messageText) {
        try {
            vk.messages.send()
                    .setPeerId(Math.toIntExact(chatId))
                    .setMessage(messageText)
                    .execute();
            return Math.toIntExact(chatId);

        } catch (VkApiException e) {
            e.printStackTrace();
            return -1;
        }

    }

    @Override
    public long sendMessages(long chatId, List<String> messagesTexts) {
        String lastMessage = messagesTexts.removeLast();
        for (int i = 0; i < messagesTexts.size(); ++i) {
            sendMessage(chatId, messagesTexts.get(i));
        }
        return chatId;
    }

    @Override
    public void editMessage(long chatId, long messageId, String editedMessageText, boolean moreMessages) {
        // TODO Auto-generated method stub

    }

}
