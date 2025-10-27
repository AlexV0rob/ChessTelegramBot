package org.example;

/**
 * Класс для упрощённого подсчёта правильности хода
 * 
 * @param rawPosition позиция фигуры в одномерном массиве
 * @return кортеж длинны 2, состоящий из номера линии, на которой стоит фигура,
 *         и позиции фигуры на линии
 */
public class Position {

	/**
	 * длина линии
	 */
	private final static int LINE_LENGTH = 8;

	/**
	 * @param rawPosition позиция фигуры в одномерном массиве
	 * @return кортеж длинны 2, состоящий из номера линии, на которой стоит фигура,
	 *         и позиции фигуры на линии
	 */
	int[] convertPosition(int rawPosition) {
		int[] convertedPosition = new int[2];
		// вычисление номера линии
		convertedPosition[0] = rawPosition / LINE_LENGTH;
		// вычисление позиции фигуры на линии
		convertedPosition[1] = rawPosition % LINE_LENGTH;
		return convertedPosition;
	}
}
