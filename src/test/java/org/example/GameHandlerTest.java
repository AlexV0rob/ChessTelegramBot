package org.example;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Проверка работы шаха и мата
 */
public class GameHandlerTest {
    private final GameHandler gameHandler = new GameHandler();
    private final GameState gameState = new GameState();

    /**
     * Проверка шаховой ситуации
     */
    @Test
    void CheckTest() {
        byte[] board = new byte[]{
                0, 0, 0, -6, 0, 0, 0, 0,
                0, 5, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                2, 3, 4, 0, 6, 0, 3, 2};
        Assertions.assertEquals(GameState.MOVE_PROPERTIES.CHECK, gameHandler.handleMove(board,
                9, 2, (byte) 5));
        Assertions.assertEquals(GameState.MOVE_PROPERTIES.REGULAR, gameHandler.handleMove(board,
                56, 8, (byte) 2));
    }

    /**
     * Проверка матовой ситуации
     */
    @Test
    void CheckmateTest() {
        byte[] board = new byte[]{
                0, 0, 0, 0, 0, 0, 5, -6,
                0, 0, 0, 0, 0, 0, -1, -1,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 8, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                2, 3, 4, 0, 6, 0, 3, 2};
        Assertions.assertEquals(GameState.MOVE_PROPERTIES.CHECKMATE, gameHandler.handleMove(board,
                6, 7, (byte) 5));
    }

    /**
     * проверка на выдачу корректного состояния игры
     */
    @Test
    void HadleMoveTest() {
        byte[] board = new byte[]{
                2, 3, 4, 5, 6, 4, 3, 2,
                1, 1, 1, 1, 1, 1, 1, 1,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                -1, -1, -1, -1, -1, -1, -1, -1,
                -2, -3, -4, -5, -6, -4, -3, -2};
        Assertions.assertEquals(GameState.MOVE_PROPERTIES.REGULAR, gameHandler.handleMove(board,
                8, 16, (byte) 1));
        Assertions.assertEquals(GameState.MOVE_PROPERTIES.IMPOSSIBLE, gameHandler.handleMove(board,
                0, 8, (byte) 2));
    }

    /**
     * проверка на выдачу корректного состояния игры
     */
    @Test
    void everyPossibleRightMoveTest() {
        byte[] board = new byte[]{
                2, 3, 4, 5, 6, 4, 3, 2,
                1, 1, 1, 0, 1, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                -1, -1, -1, -1, -1, -1, -1, 0,
                -2, -3, -4, -5, -6, -4, -3, -2};
        List<Integer> pawnList = List.of(16, 24);
        List<Integer> knightList = List.of(11, 16, 18);
        Assertions.assertTrue(pawnList.containsAll(gameHandler.everyPossibleRightMove((byte) 1,
                8, board, false)));
        Assertions.assertTrue(knightList.containsAll(gameHandler.everyPossibleRightMove((byte) 3,
                1, board, false)));
    }
}
