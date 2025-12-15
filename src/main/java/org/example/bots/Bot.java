package org.example.bots;

import java.util.List;

/**
 * Интерфейс бота
 */
public interface Bot {
    /**
     * Отправить список сообщений
     */
    public long sendMessages(long chatId, List<String> messagesTexts);

    /**
     * Редактировать сообщение по идентификатору или отправить новое, если
     * невозможно изменить
     */
    public void editMessage(long chatId, long messageId,
                            String editedMessageText, boolean moreMessages);
}
