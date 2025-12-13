package org.example.states;

/**
 * Хранитель состояния готовности хода
 */
public class MoveState {
    /**
     * Возможные состояния готовности хода
     */
    private enum MoveStatus {
        /**
         * Ход не собран
         */
        NOTHING,
        /**
         * Известна фигура
         */
        FIGURE,
        /**
         * Известна фигура и начальная позиция
         */
        START,
        /**
         * Известна фигура, начальны=ая и конечная позиции, ход готов
         */
        FINISH
    }

    /**
     * Код фигуры
     */
    private String figure;

    /**
     * Код начальной позиции
     */
    private String startPosition;

    /**
     * Код конечной позиции
     */
    private String finishPosition;

    /**
     * Текущее состояние готовности хода
     */
    private MoveStatus currentStatus;

    /**
     * Конструктор, ставит 0 в фигуру, -1 в начальную и конечную позиции и
     * состояние отсутствия готовности хода
     */
    public MoveState() {
        figure = "";
        startPosition = "";
        finishPosition = "";
        currentStatus = MoveStatus.NOTHING;
    }

    /**
     * Получить фигуру
     */
    public String getFigure() {
        return figure;
    }

    /**
     * Получить начальную позицию
     */
    public String getStartPosition() {
        return startPosition;
    }

    /**
     * Получить конечную позицию
     */
    public String getFinishPosition() {
        return finishPosition;
    }

    /**
     * Индикатор готовности хода
     */
    public boolean isMoveReady() {
        return currentStatus.equals(MoveStatus.FINISH);
    }

    /**
     * Поставить новую часть хода в соответствующее поле поменять статус
     * готовности хода на следующее
     *
     * @param newMovePart в зависимости от текущего состояния готовности хода
     * может быть фигурой, начальной или конечной позицией
     */
    public void nextStatus(String newMovePart) {
        switch (currentStatus) {
            case NOTHING -> {
                figure = newMovePart;
                currentStatus = MoveStatus.FIGURE;
            }
            case FIGURE -> {
                startPosition = newMovePart;
                currentStatus = MoveStatus.START;
            }
            case START -> {
                finishPosition = newMovePart;
                currentStatus = MoveStatus.FINISH;
            }
            case FINISH -> {
                clearMoveState();
            }
        }
    }

    /**
     * Очистить готовность хода, ставит 0 в фигуру, -1 в начальную и
     * конечную позиции и состояние отсутствия готовности хода
     */
    public void clearMoveState() {
        figure = "";
        startPosition = "";
        finishPosition = "";
        currentStatus = MoveStatus.NOTHING;
    }
}
