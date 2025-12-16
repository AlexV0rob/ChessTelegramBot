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
         * Telegram
         */
        TELEGRAM,
        /**
         * Discord
         */
        DISCORD
    }

    /**
     * Количество сыгранных игр
     */
    private long countOfPlayedGames;
    /**
     * Количество выигранных игр
     */
    private long countOfWonGames;

    private final String userName;

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
     * Идентфикатор матча, в котором находится пользователь
     */
    private String currentLobbyId;

    /**
     * Конструктор класса
     */
    public UserState(MessengerType userMessenger, String nameOfUser) {
        currentUserState = UserStatus.MAINMENU;
        currentMoveState = new MoveState();
        messenger = userMessenger;
        countOfPlayedGames = 0;
        countOfWonGames = 0;
        currentLobbyId = "";
        userName = nameOfUser;
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
     * Изменить количество сыгранных матчей
     */
    public void updatePlayedGames() {
        countOfPlayedGames++;
    }

    /**
     * Изменить количество сыгранных матчей
     */
    public void updateWonGames() {
        countOfWonGames++;
    }

    /**
     * Получить пару Пользователь/Статистика
     */
    public double getUserRating() {
        Double result = 0.0;
        if (countOfPlayedGames != 0) {
            result = (double) countOfWonGames / countOfPlayedGames;
        }
        return result;
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

    /**
     * Получить имя пользователя
     */
	public String getUserName() {
		return userName;
	}
}
