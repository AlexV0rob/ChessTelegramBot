package org.example;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
    void CheckTest() throws Exception {
        Method isCheckMethod = GameHandler.class.getDeclaredMethod("isCheck", int.class, byte[].class,
                boolean.class, Chessmen.class);
        isCheckMethod.setAccessible(true);
        byte[] board = new byte[]{
                0, 0, 0, -6, 0, 0, 0, 0,
                0, 0, 5, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                2, 3, 4, 0, 6, 0, 3, 2};
        Chessmen rook = new Rook();
        Chessmen queen = new Queen();
        boolean rookResult = (boolean) isCheckMethod.invoke(gameHandler, 56, board, false, rook);
        boolean queenResult = (boolean) isCheckMethod.invoke(gameHandler, 10, board, false, queen);
        Assertions.assertFalse(rookResult);
        Assertions.assertTrue(queenResult);

    }

    /**
     * Проверка матовой ситуации
     */
    @Test
    void CheckmateTest() throws Exception {
        Method isThisMoveOneKingMethod = GameHandler.class.getDeclaredMethod("isThisMoveOnKing", byte[].class,
                int.class, boolean.class);
        isThisMoveOneKingMethod.setAccessible(true);
        byte[] board = new byte[]{
                0, 0, 0, 0, 0, 0, 5, -6,
                0, 0, 0, 0, 0, 0, -1, -1,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 8, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                2, 3, 4, 0, 6, 0, 3, 2};
        Assertions.assertFalse((boolean) isThisMoveOneKingMethod.invoke(gameHandler, board, 13, false));
        Assertions.assertTrue((boolean) isThisMoveOneKingMethod.invoke(gameHandler, board, 7, false));
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
        ArrayList<Integer> pawnList = new ArrayList<>();
        pawnList.add(16);
        pawnList.add(24);
        ArrayList<Integer> knightList = new ArrayList<>();
        knightList.add(11);
        knightList.add(18);

        Assertions.assertIterableEquals(pawnList, gameHandler.everyPossibleRightMove((byte) 1, 8, board, false));
        Assertions.assertIterableEquals(knightList, gameHandler.everyPossibleRightMove((byte) 3, 1, board, false));

    }
}
