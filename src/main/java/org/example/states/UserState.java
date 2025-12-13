package org.example.states;

/**
 * Хранитель состояния пользователя
 */
public class UserState {
    /**
     * Код пустого поля
     */
    private final static byte EMPTY = 0;

    /**
     * Код пешки
     */
    private final static byte PAWN = 1;

    /**
     * Код ладьи
     */
    private final static byte ROOK = 2;

    /**
     * Код коня
     */
    private final static byte KNIGHT = 3;

    /**
     * Код слона
     */
    private final static byte BISHOP = 4;

    /**
     * Код ферзя
     */
    private final static byte QUEEN = 5;

    /**
     * Код короля
     */
    private final static byte KING = 6;

    /**
     * Код белой стороны
     */
    private final static byte WHITE = -1;

    /**
     * Код чёрной стороны
     */
    private final static byte BLACK = 1;

    /**
     * Начальная доска
     */
    private final static byte[][] START_BOARD =
            {
                    {WHITE * ROOK, WHITE * KNIGHT, WHITE * BISHOP, WHITE * QUEEN,
                            WHITE * KING, WHITE * BISHOP, WHITE * KNIGHT, WHITE * ROOK},
                    {WHITE * PAWN, WHITE * PAWN, WHITE * PAWN, EMPTY * PAWN,
                            WHITE * PAWN, WHITE * PAWN, WHITE * PAWN, WHITE * PAWN},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                    {BLACK * PAWN, BLACK * PAWN, BLACK * PAWN, BLACK * PAWN,
                            BLACK * PAWN, BLACK * PAWN, BLACK * PAWN, BLACK * PAWN},
                    {BLACK * ROOK, BLACK * KNIGHT, BLACK * BISHOP, BLACK * QUEEN,
                            BLACK * KING, BLACK * BISHOP, BLACK * KNIGHT, BLACK * ROOK}

            };

    /**
     * Состояние пользователя
     */
    public enum userStatus {
        /**
         * Главное меню
         */
        MAIN_MENU,
        /**
         * В игре
         */
        IN_GAME
    }

    /**
     * Длина стороны доски
     */
    private final static int BOARD_SIDE_LENGTH = 8;

    /**
     * Текущее состояние пользователя
     */
    private userStatus currentUserState;

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
        currentUserState = userStatus.MAIN_MENU;
        currentMoveState = new MoveState();
    }

    /**
     * Установить новое состояние пользователя
     */
    public void setUserState(userStatus newUserState) {
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
    public userStatus getUserState() {
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
