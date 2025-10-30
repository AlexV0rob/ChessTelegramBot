package org.example;

public class PositionConverter {
    public static enum SHIFT_PROPERTY {

        GREATER,

        LESS,

        EQUAL

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
     * Смещение на одну клетку вверх
     */
    private final static int SQUARE_UP = -8;
    /**
     * Смещение на одну клетку вниз
     */
    private final static int SQUARE_DOWN = 8;
    /**
     * Смещение на одну клетку влево
     */
    private final static int SQUARE_LEFT = -1;
    /**
     * Смещение на одну клетку вправо
     */
    private final static int SQUARE_RIGHT = 1;

    /**
     * Конструктор
     */
    public PositionConverter(int lineLength, int minPosition, int maxPosition) {
        this.lineLength = lineLength;
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
    }

    /**
     * Позиция на n квадратов выше позиции x
     *
     * @return код позиции или -1, если выход за границу доски
     */
    public int nSquaresUpFromPositionX(int n, int x) {
        if (x < minPosition || x > maxPosition) {
            return -1;
        }
        int newPosition = x + SQUARE_UP * n;
        return (newPosition < minPosition ? -1 : newPosition);
    }

    /**
     * Позиция на n квадратов ниже позиции x
     *
     * @return код позиции или -1, если выход за границу доски
     */
    public int nSquaresDownFromPositionX(int n, int x) {
        if (x < minPosition || x > maxPosition) {
            return -1;
        }
        int newPosition = x + SQUARE_DOWN * n;
        return (newPosition > maxPosition ? -1 : newPosition);
    }

    /**
     * Позиция на n квадратов левее позиции x
     *
     * @return код позиции или -1, если выход за границу доски
     */
    public int nSquaresLeftFromPositionX(int n, int x) {
        if (x < minPosition || x > maxPosition) {
            return -1;
        }
        int oldLine = x / lineLength;
        int newPosition = x + SQUARE_LEFT * n;
        return (newPosition / lineLength != oldLine ? -1 : newPosition);
    }

    /**
     * Позиция на n квадратов правее позиции x
     *
     * @return код позиции или -1, если выход за границу доски
     */
    public int nSquaresRightFromPositionX(int n, int x) {
        if (x < minPosition || x > maxPosition) {
            return -1;
        }
        int oldLine = x / lineLength;
        int newPosition = x + SQUARE_RIGHT * n;
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
            currentPosition = nSquaresDownFromPositionX(1, currentPosition);
        } else if (verticalProperty.equals(SHIFT_PROPERTY.LESS)) {
            currentPosition = nSquaresUpFromPositionX(1, currentPosition);
        }

        if (horizontalProperty.equals(SHIFT_PROPERTY.GREATER)) {
            currentPosition = nSquaresRightFromPositionX(1, currentPosition);
        } else if (horizontalProperty.equals(SHIFT_PROPERTY.LESS)) {
            currentPosition = nSquaresLeftFromPositionX(1, currentPosition);
        }
        return currentPosition;
    }
}
