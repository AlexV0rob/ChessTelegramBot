package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Проверка конвертера частей хода
 */
public class MovePartsConverterTest {
	/**
	 * Конвертер частей хода
	 */
	private final MovePartsConverter movePartsConverter = new MovePartsConverter();
	
	/**
	 * Проверить получение фигуры по коду, кода по фигуре и названия по фигуре
	 */
	@Test
	public void figuresTest() {
		String[] figuresNames = 
			{"ПЕШКА", "ЛАДЬЯ", "КОНЬ", "СЛОН", "ФЕРЗЬ", "КОРОЛЬ"};
		String[] figuresSymbols = {"p", "r", "n", "b", "q", "k"};
		int[] figuresCodes = {1, 2, 3, 4, 5, 6};
		for (int i = 0; i < figuresSymbols.length; ++i) {
			Assertions.assertEquals(figuresCodes[i], 
					movePartsConverter.getFigureCode(figuresSymbols[i]));
			Assertions.assertEquals(figuresNames[i], 
					movePartsConverter.getFigureName(figuresSymbols[i]));
		}
		for (int i = 0; i < figuresCodes.length; ++i) {
			Assertions.assertEquals(figuresSymbols[i], 
					movePartsConverter.getFigureSymbol(figuresCodes[i]));
		}
		Assertions.assertEquals(1, movePartsConverter.getFigureCode(""));
		Assertions.assertEquals(0, movePartsConverter.getFigureCode("something"));
		Assertions.assertEquals(
				"НЕИЗВЕСТНО", movePartsConverter.getFigureName("something"));
		Assertions.assertEquals("", movePartsConverter.getFigureSymbol(0));
	}
	
	/**
	 * Проверить получение ряда по коду и кода по ряду
	 */
	@Test
	public void positionRowsTest() {
		char[] rowsSymbols = {'1', '2', '3', '4', '5', '6', '7', '8'};
		int[] rowsCodes = {0, 1, 2, 3, 4, 5, 6, 7};
		for (int i = 0; i < rowsSymbols.length; ++i) {
			Assertions.assertEquals(rowsCodes[i], 
					movePartsConverter.getPositionRowCode(rowsSymbols[i]));
		}
		for (int i = 0; i < rowsCodes.length; ++i) {
			Assertions.assertEquals(rowsSymbols[i], 
					movePartsConverter.getPositionRowSymbol(rowsCodes[i]));
		}
		Assertions.assertEquals(-1, movePartsConverter.getPositionRowCode('k'));
		Assertions.assertEquals('\0', movePartsConverter.getPositionRowSymbol(9));
	}
	
	/**
	 * Проверить получение столбца по коду и кода по столбцу
	 */
	@Test
	public void positionColumnsTest() {
		char[] columnsSymbols = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
		int[] columnsCodes = {0, 1, 2, 3, 4, 5, 6, 7};
		for (int i = 0; i < columnsSymbols.length; ++i) {
			Assertions.assertEquals(columnsCodes[i], 
					movePartsConverter.getPositionColumnCode(columnsSymbols[i]));
		}
		for (int i = 0; i < columnsCodes.length; ++i) {
			Assertions.assertEquals(columnsSymbols[i], 
					movePartsConverter.getPositionColumnSymbol(columnsCodes[i]));
		}
		Assertions.assertEquals(-1, movePartsConverter.getPositionColumnCode('k'));
		Assertions.assertEquals('\0', movePartsConverter.getPositionColumnSymbol(9));
	}
}
