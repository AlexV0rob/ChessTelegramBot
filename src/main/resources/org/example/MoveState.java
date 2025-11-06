package org.example;

public class MoveState {
    /**
     * Возможный состояния готовности хода
	     */
    private static enum STATUS {
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
    private STATUS currentStatus;
    
    /**
     * Конструктор, ставит 0 в фигуру, -1 в начальную и конечную позиции и
     * состояние отсутствия готовности хода
     */
    public MoveState() {
        figure = "";
        startPosition = "";
        finishPosition = "";
        currentStatus = STATUS.NOTHING;
    }
    
    public String getFigure() {
    	return figure;
    }
    
    public String getStartPosition() {
    	return startPosition;
    }
    
    public String getFinishPosition() {
    	return finishPosition;
    }
    
    public boolean isMoveReady() {
    	return currentStatus.equals(STATUS.FINISH);
    }
    
    /**
     * Поставить новую часть хода в соответсвующее поле  поменять статус
     * готовности хода на следующее
     *
     * @param newMovePart в зависимости от текущего состояния готовности хода
     * может быть фигурой, начальной или конечной позицией
     */
    public void nextStatus(String newMovePart) {
        switch (currentStatus) {
            case NOTHING -> {
                figure = newMovePart;
                currentStatus = STATUS.FIGURE;
            }
            case FIGURE -> {
                startPosition = newMovePart;
                currentStatus = STATUS.START;
            }
            case START -> {
                finishPosition = newMovePart;
                currentStatus = STATUS.FINISH;
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
    private void clearMoveState() {
        figure = "";
        startPosition = "";
        finishPosition = "";
        currentStatus = STATUS.NOTHING;
    }
}
