package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.example.buttons.IdentifiedButton;
import org.example.buttons.SimpleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Проверка главного логического модуля
 */
public class MainLogicTest {
    /**
     * Главный логический модуль
     */
    private MainLogic mainLogic;

    /**
     * Восстановить начальное состояние MainLogic
     */
    @BeforeEach
    public void recoverMainLogic() {
    	mainLogic = new MainLogic();
    	mainLogic.processInput("/start", 1);
    }
    
    /**
     * Проверить выход в меню
     */
    @Test
    public void quitMenuTest() {
        List<String> responseReal = mainLogic.processInput("/quit", 1);
        Assertions.assertIterableEquals(List.of("Чем займёмся?"), responseReal);
        Assertions.assertIterableEquals(
                List.of(new SimpleButton("Начать игру на этом устройстве")),
                mainLogic.getCurrentSimpleButtons(1));
        Assertions.assertIterableEquals(List.of(), mainLogic.getCurrentIdentifiedButtons(1));
    }

    /**
     * Проверить ввод в меню
     */
    @Test
    public void menuInputTest() {
    	mainLogic.processInput("/quit", 1);
        List<String> responseReal = mainLogic.processInput("Начать игру на этом устройстве", 1);
        Assertions.assertIterableEquals(
        		List.of(
        				"Игра началась",
        				"""
Ход белых

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][WP][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
        				""",
        				"Ваш ход: "),
        		responseReal);
    }
    
    /**
     * Проверить неизвестный ввод в меню
     */
    @Test
    public void invalidMenuInputTest() {
    	mainLogic.processInput("/quit", 1);
        List<String> responseReal = mainLogic.processInput("Другой ввод", 1);
        Assertions.assertIterableEquals(List.of("Неизвестный запрос меню"), responseReal);
    }
    
    /**
     * Проверить создание одиночной игры
     */
    @Test
    public void createSinglegameTest() {
        List<String> responseReal = mainLogic.processInput("/newsinglegame", 1);
        Assertions.assertIterableEquals(
        		List.of(
        				"Игра началась",
                        """
Ход белых

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][WP][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
                        """,
                        "Ваш ход: "),
                responseReal);
        Assertions.assertIterableEquals(List.of(), mainLogic.getCurrentSimpleButtons(1));
        List<IdentifiedButton> gameButtons = mainLogic.getCurrentIdentifiedButtons(1);
        Assertions.assertTrue(
        		List.of(
        				new IdentifiedButton("__p__", "ПЕШКА"), 
        				new IdentifiedButton("__r__", "ЛАДЬЯ"),
        				new IdentifiedButton("__n__", "КОНЬ"),
        				new IdentifiedButton("__b__", "СЛОН"),
        				new IdentifiedButton("__q__", "ФЕРЗЬ"),
        				new IdentifiedButton("__k__", "КОРОЛЬ"))
        		.containsAll(gameButtons));
        Assertions.assertEquals(6, gameButtons.size());
    }

    /**
     * Проверить совершение хода целиком
     */
    @Test
    public void makeMoveTest() {
        mainLogic.processInput("/newsinglegame", 1);
        List<String> responseReal = mainLogic.processInput("e2e4", 1);
        Assertions.assertIterableEquals(List.of(
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
                        "Ваш ход: "),
                responseReal);
        Assertions.assertIterableEquals(List.of(), mainLogic.getCurrentSimpleButtons(1));
        List<IdentifiedButton> gameButtons = mainLogic.getCurrentIdentifiedButtons(1);
        Assertions.assertTrue(
        		List.of(
        				new IdentifiedButton("__p__", "ПЕШКА"), 
        				new IdentifiedButton("__r__", "ЛАДЬЯ"),
        				new IdentifiedButton("__n__", "КОНЬ"),
        				new IdentifiedButton("__b__", "СЛОН"),
        				new IdentifiedButton("__q__", "ФЕРЗЬ"),
        				new IdentifiedButton("__k__", "КОРОЛЬ"))
        		.containsAll(gameButtons));
        Assertions.assertEquals(6, gameButtons.size());
    }

    /**
     * Проверить совершение хода по частям
     */
    @Test
    public void makeMoveByPartsTest() {
        mainLogic.processInput("/newsinglegame", 1);
        List<String> responseReal = new ArrayList<String>();
        responseReal.addAll(mainLogic.processInput("__p__", 1));
        List<IdentifiedButton> figureButtons = mainLogic.getCurrentIdentifiedButtons(1);
        responseReal.addAll(mainLogic.processInput("__e2__", 1));
        List<IdentifiedButton> startButtons = mainLogic.getCurrentIdentifiedButtons(1);
        responseReal.addAll(mainLogic.processInput("__e4__", 1));
        List<IdentifiedButton> finishButtons = mainLogic.getCurrentIdentifiedButtons(1);
        Assertions.assertIterableEquals(List.of(
                        "Ваш ход: ПЕШКА",
                        "Ваш ход: ПЕШКА E2",
                        "Ваш ход: ПЕШКА E2 E4",
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
                        "Ваш ход: "),
                responseReal);
        Assertions.assertIterableEquals(List.of(), mainLogic.getCurrentSimpleButtons(1));
        Assertions.assertTrue(
        		List.of(
        				new IdentifiedButton("__a2__", "A2"), 
        				new IdentifiedButton("__b2__", "B2"),
        				new IdentifiedButton("__c2__", "C2"),
        				new IdentifiedButton("__d2__", "D2"),
        				new IdentifiedButton("__e2__", "E2"),
        				new IdentifiedButton("__f2__", "F2"),
        				new IdentifiedButton("__g2__", "G2"),
        				new IdentifiedButton("__h2__", "H2"))
        		.containsAll(figureButtons));
        Assertions.assertEquals(8, figureButtons.size());
        Assertions.assertTrue(
        		List.of(
        				new IdentifiedButton("__e3__", "E3"), 
        				new IdentifiedButton("__e4__", "E4"))
        		.containsAll(startButtons));
        Assertions.assertEquals(2, startButtons.size());
        Assertions.assertTrue(
        		List.of(
        				new IdentifiedButton("__p__", "ПЕШКА"), 
        				new IdentifiedButton("__r__", "ЛАДЬЯ"),
        				new IdentifiedButton("__n__", "КОНЬ"),
        				new IdentifiedButton("__b__", "СЛОН"),
        				new IdentifiedButton("__q__", "ФЕРЗЬ"),
        				new IdentifiedButton("__k__", "КОРОЛЬ"))
        		.containsAll(finishButtons));
        Assertions.assertEquals(6, finishButtons.size());
    }

    /**
     * Проверить невозможный ход
     */
    @Test
    public void impossibleMoveTest() {
        mainLogic.processInput("/newsinglegame", 1);
        List<String> responseReal = mainLogic.processInput("e2e8", 1);
        Assertions.assertIterableEquals(
        		List.of(
        				"""
Ход белых

8  [ BR][ BN][ BB][ BQ][ BK][ BB][ BN][ BR]
7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
6  [      ][      ][      ][      ][      ][      ][      ][      ]
5  [      ][      ][      ][      ][      ][      ][      ][      ]
4  [      ][      ][      ][      ][      ][      ][      ][      ]
3  [      ][      ][      ][      ][      ][      ][      ][      ]
2  [WP][WP][WP][WP][WP][WP][WP][WP]
1  [WR][WN][WB][WQ][WK][WB][WN][WR]
      A      B      C      D      E      F      G      H     \s
Невозможный ход! Попробуйте снова.
        				""",
        				"Ваш ход: "),
        		responseReal);
        Assertions.assertIterableEquals(List.of(), mainLogic.getCurrentSimpleButtons(1));
        List<IdentifiedButton> gameButtons = mainLogic.getCurrentIdentifiedButtons(1);
        Assertions.assertTrue(
        		List.of(
        				new IdentifiedButton("__p__", "ПЕШКА"), 
        				new IdentifiedButton("__r__", "ЛАДЬЯ"),
        				new IdentifiedButton("__n__", "КОНЬ"),
        				new IdentifiedButton("__b__", "СЛОН"),
        				new IdentifiedButton("__q__", "ФЕРЗЬ"),
        				new IdentifiedButton("__k__", "КОРОЛЬ"))
        		.containsAll(gameButtons));
        Assertions.assertEquals(6, gameButtons.size());
    }
}