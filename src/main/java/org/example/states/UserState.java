package org.example.states;

/**
 * Хранитель состояния пользователя
 */
public class UserState {
	/**
	 * Вид мессенджера
	 */
	public enum messengerType {
		/**
		 * Фальшивый аккумулирующий бот
		 */
		FAKE,
		/**
		 * Telegram
		 */
		TELEGRAM
	}

    /**
     * Состояние пользователя
     */
    public enum userState {
        /**
         * Главное меню
         */
        MAINMENU,
        /**
         * В игре
         */
        INGAME
    }

    /**
     * Начальная доска
     */
    private final static byte[][] START_BOARD =
            {
                    {-2, -3, -4, -5, -6, -4, -3, -2},
                    {-1, -1, -1, -1, -1, -1, -1, -1},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 1, 1, 1, 1},
                    {2, 3, 4, 5, 6, 4, 3, 2}
            };

    /**
     * Длина стороны доски
     */
    private final static int BOARD_SIDE_LENGTH = 8;

    /**
     * Текущее состояние пользователя
     */
    private userState currentUserState;

    /**
     * Состояние игры
     */
    private GameState currentGameState;

    /**
     * Состояние готовности пользователя
     */
    private final MoveState currentMoveState;
    
    /**
     * Тип мессенджера пользователя
     */
    private final messengerType messenger;

    /**
     * Конструктор класса
     */
    public UserState(messengerType userMessenger) {
        currentGameState = new GameState(START_BOARD, BOARD_SIDE_LENGTH, true);
        currentUserState = userState.MAINMENU;
        currentMoveState = new MoveState();
        messenger = userMessenger;
    }

    /**
     * Установить новое состояние пользователя
     */
    public void setUserState(userState newUserState) {
        currentUserState = newUserState;
    }

    /**
     * Перезапустить состояние игры
     */
    public void resetGameState() {
        currentGameState = new GameState(START_BOARD, BOARD_SIDE_LENGTH, true);
    }

    /**
     * Получить текущее состояние пользователя
     */
    public userState getUserState() {
        return currentUserState;
    }

    /**
     * Получить текущее состояние игры
     */
    public GameState getGameState() {
        return currentGameState;
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
    public messengerType getUserMessenger() {
    	return messenger;
    }
}
