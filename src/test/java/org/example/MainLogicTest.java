package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.example.auxiliary.IdentifiedButton;
import org.example.auxiliary.SimpleButton;
import org.example.bots.FakeBot;
import org.example.states.UserState;
import org.example.statesHandlers.MemoryStatesHandler;
import org.example.statesHandlers.StatesHandler;

import java.util.List;

/**
 * Проверка главного логического модуля
 */
public class MainLogicTest {
    /**
     * Фальшивый аккумулирующий бот
     */
    private final FakeBot fakeBot = new FakeBot(UserState.MessengerType.TELEGRAM);

    /**
     * Хранитель состояний
     */
    private StatesHandler states;

    /**
     * Главный логический модуль
     */
    private MainLogic mainLogic;

    /**
     * Сброс состояний
     */
    @BeforeEach
    public void resetStates() {
    	states = new MemoryStatesHandler();
    	mainLogic = new MainLogic(states);
    	mainLogic.processInput(fakeBot, "/start", 1);
    	mainLogic.processInput(fakeBot, "/start", 2);
    	mainLogic.processInput(fakeBot, "new", 1);
    	mainLogic.processInput(fakeBot, "new", 2);
    	fakeBot.clearMessages();
    }
    
    /**
     * Проверить работу меню
     */
    @Test
    public void menuInputTest() {
        mainLogic.processInput(fakeBot, "/quit", 1);
        List<String> responseReal = fakeBot.getAccumulatedMessages(1);
        Assertions.assertIterableEquals(List.of("Чем займёмся?"), responseReal);
        Assertions.assertIterableEquals(
                List.of(new SimpleButton("Начать новую одиночную игру"),
                        new SimpleButton("Создать многопользовательский матч"),
                        new SimpleButton("Присоединится к существующему матчу")),
                mainLogic.getCurrentSimpleButtons(fakeBot, 1));
        Assertions.assertIterableEquals(List.of(),
                mainLogic.getCurrentIdentifiedButtons(fakeBot, 1));
    }

    /**
     * Проверить создание матча
     */
    @Test
    public void createValidLobbyTest() {
        mainLogic.processInput(fakeBot, "/create game", 1);
        List<String> responseReal = fakeBot.getAccumulatedMessages(1);
        Assertions.assertIterableEquals(
                List.of("""
                        Матч game создан и доступен для других игроков.
                        Ожидайте присоединения противника
                        """),
                responseReal);
        mainLogic.processInput(fakeBot, "/join", 2);
        List<IdentifiedButton> lobbiesButtons = mainLogic
                .getCurrentIdentifiedButtons(fakeBot, 2);
        Assertions.assertIterableEquals(
                List.of(new IdentifiedButton("__game__", "game")),
                lobbiesButtons);
    }

    /**
     * Проверить неверное создание матча
     */
    @Test
    public void createInvalidLobbyTest() {
        mainLogic.processInput(fakeBot, "/create game", 1);
        fakeBot.clearMessages();
        mainLogic.processInput(fakeBot, "/create очевиднослишкомдлинноеимя", 1);
        mainLogic.processInput(fakeBot, "/create game", 1);
        List<String> responseReal = fakeBot.getAccumulatedMessages(1);
        Assertions.assertIterableEquals(
                List.of("Извините, название должно быть не более 16 символов. Придумайте другое:",
                        "Извините, данное название уже занято. Придумайте другое:"),
                responseReal);
    }

    /**
     * Проверить создание одиночной игры
     */
    @Test
    public void createSinglegameTest() {
        mainLogic.processInput(fakeBot, "/new_local", 1);
        List<String> responseReal = fakeBot.getAccumulatedMessages(1);
        Assertions.assertIterableEquals(List.of(
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
     * Проверить совершение хода целиком
     */
    @Test
    public void makeMoveTest() {
        mainLogic.processInput(fakeBot, "/new_local", 1);
        fakeBot.clearMessages();
        mainLogic.processInput(fakeBot, "e2e4", 1);
        List<String> responseReal = fakeBot.getAccumulatedMessages(1);
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
    }

    /**
     * Проверить совершение хода по частям
     */
    @Test
    public void makeMoveByPartsTest() {
        mainLogic.processInput(fakeBot, "/new_local", 1);
        fakeBot.clearMessages();
        mainLogic.processInput(fakeBot, "__p__", 1);
        mainLogic.processInput(fakeBot, "__e2__", 1);
        mainLogic.processInput(fakeBot, "__e4__", 1);
        List<String> responseReal = fakeBot.getAccumulatedMessages(1);
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
    }

    /**
     * Проверить невозможный ход
     */
    @Test
    public void impossibleMoveTest() {
        mainLogic.processInput(fakeBot, "/new_local", 1);
        fakeBot.clearMessages();
        mainLogic.processInput(fakeBot, "e2e8", 1);
        List<String> responseReal = fakeBot.getAccumulatedMessages(1);
        Assertions.assertTrue(responseReal.getFirst().contains("Невозможный ход! Попробуйте снова."));
    }

    /**
     * Проверить присоединение к матчу
     */
    @Test
    public void joinLobbyTest() {
        mainLogic.processInput(fakeBot, "/create game", 1);
        fakeBot.clearMessages();
        mainLogic.processInput(fakeBot, "/join game", 2);
        List<String> responseRealFirst = fakeBot.getAccumulatedMessages(1);
        List<String> responseRealSecond = fakeBot.getAccumulatedMessages(2);
        Assertions.assertIterableEquals(List.of(
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
                responseRealFirst);
        Assertions.assertIterableEquals(List.of(
                        "Игра началась",
                        """
                                Ход белых
                                
                                1  [WR][WN][WB][WK][WQ][WB][WN][WR]
                                2  [WP][WP][WP][WP][WP][WP][WP][WP]
                                3  [      ][      ][      ][      ][      ][      ][      ][      ]
                                4  [      ][      ][      ][      ][      ][      ][      ][      ]
                                5  [      ][      ][      ][      ][      ][      ][      ][      ]
                                6  [      ][      ][      ][      ][      ][      ][      ][      ]
                                7  [ BP][ BP][ BP][ BP][ BP][ BP][ BP][ BP]
                                8  [ BR][ BN][ BB][ BK][ BQ][ BB][ BN][ BR]
                                      H      G      F      E      D      C      B      A     \s
                                """,
                        "Сейчас ходит противник."),
                responseRealSecond);
    }

    /**
     * Проверить совершение хода в многопользовательской игре
     */
    @Test
    public void multiplayerMoveTest() {
        mainLogic.processInput(fakeBot, "/create game", 1);
        mainLogic.processInput(fakeBot, "/join game", 2);
        fakeBot.clearMessages();
        mainLogic.processInput(fakeBot, "e7e5", 2);
        mainLogic.processInput(fakeBot, "e2e4", 1);
        mainLogic.processInput(fakeBot, "e7e5", 2);
        List<String> responseRealSecond = fakeBot.getAccumulatedMessages(1);
        List<String> responseRealFirst = fakeBot.getAccumulatedMessages(2);
        Assertions.assertEquals("Вы не можете сейчас ходить. Дождитесь хода противника.",
                responseRealFirst.get(0));
        Assertions.assertEquals("Ваш ход: ",
                responseRealFirst.get(2));
        Assertions.assertEquals("Сейчас ходит противник.",
                responseRealSecond.get(1));
    }
    
    /**
     * Проверить добавление нового пользователя
     */
    @Test
    public void newUserTest() {
    	Assertions.assertEquals(0, states.getUserIdFromTelegramId(3));
    	mainLogic.processInput(fakeBot, "/start", 3);
    	mainLogic.processInput(fakeBot, "new", 3);
    	Assertions.assertEquals(3, states.getUserIdFromTelegramId(3));
    }
    
    /**
     * Проверить привязку старого пользователя
     */
    @Test
    public void oldUserTest() {
    	Assertions.assertEquals(0, states.getUserIdFromDiscordId(1));
    	FakeBot otherBot = new FakeBot(UserState.MessengerType.DISCORD);
    	mainLogic.processInput(otherBot, "/start", 1);
    	mainLogic.processInput(otherBot, "old", 1);
    	mainLogic.processInput(otherBot, "Telegram 1", 1);
    	Assertions.assertEquals(1, states.getUserIdFromDiscordId(1));
    }
    
    /**
     * Проверить вывод таблицы лидеров
     */
    @Test
    public void leaderBoardTest() {
    	//TODO
    	String leaderString = "";
    	Assertions.assertTrue(leaderString.contains("Таблица Лидеров:"));
    	Assertions.assertTrue(leaderString.contains("(1) Name1: Win rate 0.0"));
    	Assertions.assertTrue(leaderString.contains("(2) Name2: Win rate 0.0"));
    	Assertions.assertTrue(leaderString.contains("Ваш рейтинг: Name1 Win rate 0.0"));
    }
}
