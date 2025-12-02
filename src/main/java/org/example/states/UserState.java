package org.example.states;

/**
 * Хранитель состояния пользователя
 */
public class UserState {
    /**
     * Вид мессенджера
     */
    public enum MessengerType {
        /**
         * Неизвестный мессенджер
         */
        UNKNOWN,
        /**
         * Telegram
         */
        TELEGRAM,
        /**
         * Discord
         */
        DISCORD
    }

    /**
     * Состояние пользователя
     */
    public enum UserStatus {
        /**
         * Главное меню
         */
        MAINMENU,
        /**
         * В игре
         */
        INGAME,
        /**
         * В ожидании начала матча
         */
        AWAITING,
        /**
         * В поиске подходящего матча
         */
        CHOOSING,
        /**
         * Создаёт матч
         */
        CREATING,
        /**
         * Выбирает мессенджер для привязки
         */
        MESSENGER_CHOOSING
    }

    /**
     * Текущее состояние пользователя
     */
    private UserStatus currentUserState;

    /**
     * Состояние готовности пользователя
     */
    private final MoveState currentMoveState;

    /**
     * Тип мессенджера пользователя
     */
    private final MessengerType messenger;

    /**
     * Идентфикатор матча. в котором находится пользователь
     */
    private String currentLobbyId;

    /**
     * Конструктор класса
     */
    public UserState(MessengerType userMessenger) {
        currentUserState = UserStatus.MAINMENU;
        currentMoveState = new MoveState();
        messenger = userMessenger;
        currentLobbyId = "";
    }

    /**
     * Установить новое состояние пользователя
     */
    public void setUserState(UserStatus newUserState) {
        currentUserState = newUserState;
    }

    /**
     * Установить новый идентификатор матча
     */
    public boolean setCurrentLobbyId(String newLobbyId) {
        if (currentLobbyId.equals("")) {
            currentLobbyId = newLobbyId;
            return true;
        }
        return false;
    }

    /**
     * Сбросить идентификатор матча
     */
    public void resetLobbyId() {
        currentLobbyId = "";
    }

    /**
     * Получить текущее состояние пользователя
     */
    public UserStatus getUserStatus() {
        return currentUserState;
    }

    /**
     * Получить текущее состояние готовности хода
     */
    public MoveState getMoveState() {
        return currentMoveState;
    }

    /**
     * Получить тип мессенджера пользователя
     */
    public MessengerType getUserMessenger() {
        return messenger;
    }

    /**
     * Получить идентифкатор матча
     */
    public String getCurrentLobbyId() {
        return currentLobbyId;
    }
}
