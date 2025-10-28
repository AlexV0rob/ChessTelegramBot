package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.example.buttons.*;

/**
 * Проверка сборщика ходов
 */
public class MoveConstructorTest {
	/**
	 * Экземпляр сборщика ходов
	 */
	private final MoveConstructor moveConstructor = new MoveConstructor();
	/**
	 * Символы фигур 
	 */
	private final static String[] FIGURES = {"", "p", "r", "n", "b", "q", "k"};
	/**
	 * Буквы на доске
	 */
	private final static String[] LETTERS = {"a", "b", "c", "d", "e", "f", "g", "h"};
	/**
	 * Цифры на доске
	 */
	private final static String[] DIGITS = {"1", "2", "3", "4", "5", "6", "7", "8"};
	
	/**
	 * Проверка очищения состояния готовности хода
	 */
	@Test
	void clearTest() {
		moveConstructor.clear();
		Assertions.assertEquals((byte) 0, moveConstructor.getFigure());
		Assertions.assertEquals(-1, moveConstructor.getStartPosition());
		Assertions.assertEquals(-1, moveConstructor.getFinishPosition());
		Assertions.assertEquals(MoveConstructor.STATUS.NOTHING,
				moveConstructor.getStatus());
	}
	
	/**
	 * Проверка перехода на следующий статус готовности хода и вывода 
	 * хода в текстовом виде
	 */
	@Test
	void nextStatusTest() {
		//Пешка
		int figure = 1;
		//e2
		int startPosition = 52;
		//e4
		int finishPosition = 36;
		int squaresInARow = 8;
		moveConstructor.clear();
		moveConstructor.nextStatus(figure);
		Assertions.assertEquals((byte) figure, moveConstructor.getFigure());
		Assertions.assertEquals(MoveConstructor.STATUS.FIGURE,
				moveConstructor.getStatus());
		moveConstructor.nextStatus(startPosition);
		Assertions.assertEquals(startPosition, moveConstructor.getStartPosition());
		Assertions.assertEquals(MoveConstructor.STATUS.START,
				moveConstructor.getStatus());
		moveConstructor.nextStatus(finishPosition);
		Assertions.assertEquals(finishPosition, moveConstructor.getFinishPosition());
		Assertions.assertEquals(MoveConstructor.STATUS.FINISH,
				moveConstructor.getStatus());
		String move = moveConstructor.flushMove(FIGURES, LETTERS, DIGITS, squaresInARow);
		Assertions.assertEquals("pe2e4", move);
		Assertions.assertEquals((byte) 0, moveConstructor.getFigure());
		Assertions.assertEquals(-1, moveConstructor.getStartPosition());
		Assertions.assertEquals(-1, moveConstructor.getFinishPosition());
		Assertions.assertEquals(MoveConstructor.STATUS.NOTHING,
				moveConstructor.getStatus());
	}
	
	/**
	 * Проверка вывода кнопок
	 */
	@Test
	void buttonsCheck() {
		byte[] board = {
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 0, 0, 0,
				0, 0, 0, 0, 6, 0, 0, 0
		};
		boolean whiteToMove = false;
		//Пешка
		int figure = 1;
		//e2
		int startPosition = 52;
		//e4
		int finishPosition = 36;
		moveConstructor.clear();
		List<IdentificatedButton> buttons;
		
		buttons = moveConstructor.nextButtonsLine(
				board, whiteToMove, FIGURES, LETTERS, DIGITS);
		Assertions.assertIterableEquals(
				List.of(
						new IdentificatedButton("p", String.valueOf((char) 1)),
						new IdentificatedButton("k", String.valueOf((char) 6))),
				buttons);
		moveConstructor.nextStatus(figure);
		buttons = moveConstructor.nextButtonsLine(
				board, whiteToMove, FIGURES, LETTERS, DIGITS);
		Assertions.assertIterableEquals(
				List.of(
						new IdentificatedButton("d2", String.valueOf((char) 51)),
						new IdentificatedButton("e2", String.valueOf((char) 52))),
				buttons);
		moveConstructor.nextStatus(startPosition);
		buttons = moveConstructor.nextButtonsLine(
				board, whiteToMove, FIGURES, LETTERS, DIGITS);
		Assertions.assertIterableEquals(
				List.of(
						new IdentificatedButton("e3", String.valueOf((char) 44)),
						new IdentificatedButton("e4", String.valueOf((char) 36))),
				buttons);
		moveConstructor.nextStatus(finishPosition);
		buttons = moveConstructor.nextButtonsLine(
				board, whiteToMove, FIGURES, LETTERS, DIGITS);
		Assertions.assertTrue(buttons.isEmpty());
	}
}
