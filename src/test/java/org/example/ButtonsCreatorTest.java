package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.example.buttons.IdentifiedButton;
import org.example.buttons.SimpleButton;

import org.example.states.MoveState;

import java.util.List;

/**
 * Тестирование создателя кнопок
 */
public class ButtonsCreatorTest {
    /**
     * Создатель кнопок
     */
    private final ButtonsCreator buttonsCreator = new ButtonsCreator();

    /**
     * Состояние готовности хода
     */
    private final MoveState moveState = new MoveState();

    /**
     * Доска, на которой идёт проверка
     */
    private final static byte[][] BOARD = {
            {0, 0, 0, 0, -5, 0, 0, 0},
            {0, 0, 0, -2, -3, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 6, 0, 0, 0}
    };

    /**
     * Проверить список кнопок главного меню
     */
    @Test
    public void menuButtonsTest() {
        Assertions.assertIterableEquals(
                List.of(new SimpleButton("Начать игру на этом устройстве")),
                buttonsCreator.getMenuButtons());
    }

    /**
     * Проверить кнопки игры
     */
    @Test
    public void gameButtonsTest() {
        List<IdentifiedButton> whiteFigures = List.of(
                new IdentifiedButton("__r__", "ЛАДЬЯ"),
                new IdentifiedButton("__n__", "КОНЬ"),
                new IdentifiedButton("__q__", "ФЕРЗЬ"));
        List<IdentifiedButton> blackFigures = List.of(
                new IdentifiedButton("__p__", "ПЕШКА"),
                new IdentifiedButton("__k__", "КОРОЛЬ"));
        List<IdentifiedButton> pawnsPositions = List.of(
                new IdentifiedButton("__d7__", "D7"),
                new IdentifiedButton("__e7__", "E7"));
        List<IdentifiedButton> pawnMoves = List.of(
                new IdentifiedButton("__d6__", "D6"),
                new IdentifiedButton("__d5__", "D5"));
        List<IdentifiedButton> currentButtons;
        currentButtons = buttonsCreator.getGameButtons(moveState, BOARD, true);
        Assertions.assertTrue(whiteFigures.containsAll(currentButtons));
        Assertions.assertEquals(whiteFigures.size(), currentButtons.size());
        currentButtons = buttonsCreator.getGameButtons(moveState, BOARD, false);
        Assertions.assertTrue(blackFigures.containsAll(currentButtons));
        Assertions.assertEquals(blackFigures.size(), currentButtons.size());
        moveState.nextStatus("p");
        currentButtons = buttonsCreator.getGameButtons(moveState, BOARD, false);
        Assertions.assertTrue(pawnsPositions.containsAll(currentButtons));
        Assertions.assertEquals(pawnsPositions.size(), currentButtons.size());
        moveState.nextStatus("d7");
        currentButtons = buttonsCreator.getGameButtons(moveState, BOARD, false);
        Assertions.assertTrue(pawnMoves.containsAll(currentButtons));
        Assertions.assertEquals(pawnMoves.size(), currentButtons.size());
        moveState.nextStatus("d5");
        currentButtons = buttonsCreator.getGameButtons(moveState, BOARD, false);
        Assertions.assertIterableEquals(List.of(), currentButtons);
    }
}
