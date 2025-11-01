package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionConverterTest {
    PositionConverter positionConverter = new PositionConverter(8, 0, 63);

    /**
     * проверка правильности подсчёта строки фигуры
     */
    @Test
    void positionRowTest() {
        Assertions.assertEquals(1, positionConverter.positionRow(9));
        Assertions.assertEquals(10, positionConverter.positionRow(81));
    }

    /**
     * проверка правильности подсчёта столбца фигуры
     */
    @Test
    void positionColumnTest() {
        Assertions.assertEquals(1, positionConverter.positionColumn(9));
        Assertions.assertEquals(3, positionConverter.positionColumn(83));
    }

    /**
     * проверка сдвига по вертикали
     */
    @Test
    void verticalMovingTest() {
        Assertions.assertEquals(-1, positionConverter.verticalMoving(1, -63,
                PositionConverter.DIRECTION_OF_SHIFT.UP));
        Assertions.assertEquals(8, positionConverter.verticalMoving(1, 0,
                PositionConverter.DIRECTION_OF_SHIFT.UP));
    }

    /**
     * проверка сдвига по горизонтали
     */
    @Test
    void horizontalMovingTest() {
        Assertions.assertEquals(-1, positionConverter.horizontalMoving(2, 8,
                PositionConverter.DIRECTION_OF_SHIFT.LEFT));
        Assertions.assertEquals(3, positionConverter.horizontalMoving(3, 0,
                PositionConverter.DIRECTION_OF_SHIFT.RIGHT));
    }

    /**
     * проверка сдвига по горизонтали
     */
    @Test
    void refreshCurrentPositionTest() {
        Assertions.assertEquals(-1, positionConverter.refreshCurrentPosition(
                PositionConverter.SHIFT_PROPERTY.GREATER, PositionConverter.SHIFT_PROPERTY.GREATER, -63));
        Assertions.assertEquals(16, positionConverter.refreshCurrentPosition(
                PositionConverter.SHIFT_PROPERTY.GREATER, PositionConverter.SHIFT_PROPERTY.EQUAL, 8));
    }
}
