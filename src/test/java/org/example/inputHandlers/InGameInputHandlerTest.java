package org.example.inputHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.states.UserState;

import java.util.ArrayList;
import java.util.List;

/**
 * Проверка обработчика ввода в игре
 */
public class InGameInputHandlerTest {
	/**
	 * Обработчик ввода в игре
	 */
	private final InGameInputHandler inGameInputHandler = 
			new InGameInputHandler();
	
	/**
	 * Пригласительное сообщение к ходу
	 */
	private final static String YOUR_MOVE = "Ваш ход: ";
	
	/**
	 * Проверить совершение хода
	 */
	@Test
	public void moveTest() {
		UserState userStateReal = new UserState();
		List<String> textsExpected = List.of(
				"""
Ход чёрных

1  [WR][WN][WB][WK][WQ][WB][WN][WR]
2  [WP][WP][WP][      ][WP][WP][WP][WP]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][WP][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
8  [ BR][ BN][ BB][ BK][ BQ][ BB][ BN][ BR]
      H      G      F      E      D      C      B      A     \s
				""", 
				YOUR_MOVE);
		List<String> textsReal = inGameInputHandler.processInput("e2e4", userStateReal);
		Assertions.assertIterableEquals(textsExpected, textsReal);
	}
	
	/**
	 * Проверить обработку части хода
	 */
	@Test
	public void movePartsTest() {
		UserState userStateReal = new UserState();
		UserState userStateExpected = new UserState();
		List<String> real = inGameInputHandler.processInput("__p__", userStateReal);
		Assertions.assertEquals(List.of(YOUR_MOVE + "ПЕШКА"), real);
		real = inGameInputHandler.processInput("__e2__", userStateReal);
		Assertions.assertEquals(List.of(YOUR_MOVE + "ПЕШКА E2"), real);
		real = inGameInputHandler.processInput("__e4__", userStateReal);
		List<String> expected = new ArrayList<String>();
		expected.add(YOUR_MOVE + "ПЕШКА E2 E4");
		expected.addAll(inGameInputHandler.processInput("pe2e4", userStateExpected));
		Assertions.assertEquals(expected, real);
	}
	
	/**
	 * Проверить неизвестный ввод
	 */
	@Test
	public void unknownInputTest() {
		UserState userState = new UserState();
		List<String> real = inGameInputHandler.processInput("something", userState);
		Assertions.assertIterableEquals(List.of("Неизвестный формат ввода хода"), real);
	}
}
