package org.example.states;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Проверка хранителя состояния готовности хода
 */
public class MoveStateTest {
	/**
	 * Хранитель состояния готовности хода
	 */
	private final MoveState moveState = new MoveState();
	
	/**
	 * Проверить сохранение частей хода и индикатор готовности
	 */
	@Test
	public void partsHandlingTest() {
		Assertions.assertEquals("", moveState.getFigure());
		Assertions.assertEquals("", moveState.getStartPosition());
		Assertions.assertEquals("", moveState.getFinishPosition());
		Assertions.assertFalse(moveState.isMoveReady());
		moveState.nextStatus("p");
		Assertions.assertEquals("p", moveState.getFigure());
		Assertions.assertEquals("", moveState.getStartPosition());
		Assertions.assertEquals("", moveState.getFinishPosition());
		Assertions.assertFalse(moveState.isMoveReady());
		moveState.nextStatus("e2");
		Assertions.assertEquals("p", moveState.getFigure());
		Assertions.assertEquals("e2", moveState.getStartPosition());
		Assertions.assertEquals("", moveState.getFinishPosition());
		Assertions.assertFalse(moveState.isMoveReady());
		moveState.nextStatus("e4");
		Assertions.assertEquals("p", moveState.getFigure());
		Assertions.assertEquals("e2", moveState.getStartPosition());
		Assertions.assertEquals("e4", moveState.getFinishPosition());
		Assertions.assertTrue(moveState.isMoveReady());
		moveState.nextStatus("");
		Assertions.assertEquals("", moveState.getFigure());
		Assertions.assertEquals("", moveState.getStartPosition());
		Assertions.assertEquals("", moveState.getFinishPosition());
		Assertions.assertFalse(moveState.isMoveReady());
	}
	
	/**
	 * Проверить очистку хода
	 */
	@Test
	public void moveClearTest() {
		moveState.nextStatus("p");
		moveState.nextStatus("e2");
		moveState.clearMoveState();
		Assertions.assertEquals("", moveState.getFigure());
		Assertions.assertEquals("", moveState.getStartPosition());
		Assertions.assertEquals("", moveState.getFinishPosition());
		Assertions.assertFalse(moveState.isMoveReady());
	}
}
