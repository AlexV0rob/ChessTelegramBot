package org.example;
/**
 * Класс для упрощённого подсчёта правильности хода
 * @param rawPosition позиция фигуры в одномерном массиве
 * @return кортеж длинны 2, состоящий из номера линии, на которой стоит фигура,
 * и позиции фигуры на линии
 */
public class Position {
/**
 * @param rawPosition позиция фигуры в одномерном массиве
 * @return кортеж длинны 2, состоящий из номера линии, на которой стоит фигура,
 * и позиции фигуры на линии
 */
 int[] convertPosition(int rawPosition){
	 int[] convertedPosition = new int[2];
	 //вычисление номера линии
	 convertedPosition[0] = rawPosition / 8;
	 //вычисление позиции фигуры на линии
	 convertedPosition[1] = rawPosition % 8;
	 return  convertedPosition;
 }
}
