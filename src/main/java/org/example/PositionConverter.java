package org.example;

public class PositionConverter {
    /**
     * варианты расположеиния позиций между друг другом
     */
    public static enum SHIFT_PROPERTY {

        GREATER,

        LESS,

        EQUAL

    }

    /**
     * направление движения фигуры
     */
    public static enum DIRECTION_OF_SHIFT {

        UP,

        DOWN,

        LEFT,

        RIGHT

    }

    /**
     * Длина линии
     */
    private int lineLength;
    /**
     * Минимальная позиция
     */
    private int minPosition;
    /**
     * Максимальная позиция
     */
    private int maxPosition;

    /**
     * Смещение на одну клетку по вертикали
     */
    private final static int VERTICAL_SQUARE = 8;
    /**
     * Смещение на одну клетку по горизонтали
     */
    private final static int HORIZONTAL_SQUARE = 1;

    /**
     * Конструктор
     */
    public PositionConverter(int lineLength, int minPosition, int maxPosition) {
        this.lineLength = lineLength;
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
    }

    /**
     * смещение на n квадратов по вертикали
     *
     * @return код позиции или -1, если выход за границу доски
     */
    public int verticalMoving(int n, int currentPosition, DIRECTION_OF_SHIFT direction) {
        if (currentPosition < minPosition || currentPosition > maxPosition) {
            return -1;
        }
        int newPosition = currentPosition;
        if (direction.equals(DIRECTION_OF_SHIFT.UP)) {
            newPosition += n * VERTICAL_SQUARE;
        } else if (direction.equals(DIRECTION_OF_SHIFT.DOWN)) {
            newPosition -= n * VERTICAL_SQUARE;
        } else {
            newPosition = -1;
        }
        if (newPosition < minPosition || newPosition > maxPosition)
            return -1;
        else
            return newPosition;
    }

    /**
     * с
     *
     * @return код позиции или -1, если выход за границу доски
     */
    public int horizontalMoving(int n, int currentPosition, DIRECTION_OF_SHIFT direction) {
        if (currentPosition < minPosition || currentPosition > maxPosition) {
            return -1;
        }
        int newPosition = currentPosition;
        int oldLine = currentPosition / lineLength;
        if (direction.equals(DIRECTION_OF_SHIFT.RIGHT)) {
            newPosition += n * HORIZONTAL_SQUARE;
        } else if (direction.equals(DIRECTION_OF_SHIFT.LEFT)) {
            newPosition -= n * HORIZONTAL_SQUARE;
        } else {
            newPosition = -1;
        }
        return (newPosition / lineLength != oldLine ? -1 : newPosition);
    }

    /**
     * Получить ряд позиции
     */
    public int positionRow(int position) {
        return position / lineLength;
    }

    /**
     * Получить столбец позиции
     */
    public int positionColumn(int position) {
        return position % lineLength;
    }

    /**
     *
     */
    public int refreshCurrentPosition(SHIFT_PROPERTY verticalProperty,
                                      SHIFT_PROPERTY horizontalProperty, int currentPosition) {
        if (verticalProperty.equals(SHIFT_PROPERTY.GREATER)) {
            currentPosition = verticalMoving(1, currentPosition, DIRECTION_OF_SHIFT.DOWN);
        } else if (verticalProperty.equals(SHIFT_PROPERTY.LESS)) {
            currentPosition = verticalMoving(1, currentPosition, DIRECTION_OF_SHIFT.UP);
        }

        if (horizontalProperty.equals(SHIFT_PROPERTY.GREATER)) {
            currentPosition = horizontalMoving(1, currentPosition, DIRECTION_OF_SHIFT.RIGHT);
        } else if (horizontalProperty.equals(SHIFT_PROPERTY.LESS)) {
            currentPosition = horizontalMoving(1, currentPosition, DIRECTION_OF_SHIFT.LEFT);
        }
        return currentPosition;
    }
}
