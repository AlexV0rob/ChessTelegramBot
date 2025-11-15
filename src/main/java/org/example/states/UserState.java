package org.example.states;

/**
 * Хранитель состояния пользователя
 */
public class UserState {
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
     * Состояние пользователя
     */
    public enum USER_STATE {
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
     * Длина стороны доски
     */
    private final static int BOARD_SIDE_LENGTH = 8;

    /**
     * Текущее состояние пользователя
     */
    private USER_STATE currentUserState;

    /**
     * Состояние игры
     */
    private GameState currentGameState;

    /**
     * Состояние готовности пользователя
     */
    private final MoveState currentMoveState;

    /**
     * Конструктор класса
     */
    public UserState() {
        currentGameState = new GameState(START_BOARD, BOARD_SIDE_LENGTH, true);
        currentUserState = USER_STATE.MAINMENU;
        currentMoveState = new MoveState();
    }

    /**
     * Установить новое состояние пользователя
     */
    public void setUserState(USER_STATE newUserState) {
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
    public USER_STATE getUserState() {
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
}
