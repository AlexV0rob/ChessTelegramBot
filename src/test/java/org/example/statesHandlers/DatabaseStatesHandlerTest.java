package org.example.statesHandlers;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.example.CriticalError;
import org.example.states.LobbyState;
import org.example.states.UserState;

/**
 * Проверка работы базы данных
 */
public class DatabaseStatesHandlerTest {
    /**
     * Адрес базы данных
     */
    private final static String DB_URL = "jdbc:sqlite:./src/test/resources/states.db";

    /**
     * Обработчик базы данных
     */
    private StatesHandler states;

    /**
     * Конструктор, создающий экземпляр обработчика базы данных
     */
    public DatabaseStatesHandlerTest() {
        try {
            states = new DatabaseStatesHandler(DB_URL);
        } catch (DatabaseException e) {
            throw new CriticalError("Couldn't connect to database", e);
        }
    }

    /**
     * Сбросить сохранённое состоние перед каждым тестом
     */
    @BeforeEach
    public void StatesReset() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();) {
            String deleteUsers = "DROP TABLE IF EXISTS users;";
            String deleteGames = "DROP TABLE IF EXISTS games;";
            String deleteNames = "DROP TABLE IF EXISTS names;";
            statement.execute(deleteUsers);
            statement.execute(deleteGames);
            statement.execute(deleteNames);
        } catch (SQLException e) {
            throw new CriticalError("Couldn't reset states", e);
        } 
        try {
            states = new DatabaseStatesHandler(DB_URL);
        } catch (DatabaseException e) {
        	throw new CriticalError("Couldn't reconnect to database", e);
        }
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Петя");
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Вася");
    }

    /**
     * Проверить установку пользователю нового статуса
     */
    @Test
    public void setNewUserStatusTest() {
        states.setNewUserStatus(1, UserState.UserStatus.INGAME);
        states.setNewUserStatus(2, UserState.UserStatus.CREATING);
        Assertions.assertEquals(UserState.UserStatus.INGAME, states.getUserStatus(1));
        Assertions.assertEquals(UserState.UserStatus.CREATING, states.getUserStatus(2));
    }

    /**
     * Проверить сброс идентификатора матча пользователя
     */
    @Test
    public void resetUserLobbyNameTest() {
        states.setUserLobbyName(1, "game");
        states.setUserLobbyName(2, "game");
        states.resetUserLobbyName(1);
        Assertions.assertEquals("", states.getUserLobbyName(1));
        Assertions.assertEquals("game", states.getUserLobbyName(2));
    }

    /**
     * Проверить вывод игроков
     */
    @Test
    public void getTopTenUsersTest() {
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Три");
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Четыре");
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Пять");
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Шесть");
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Семь");
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Восемь");
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Девять");
        states.addNewUser(UserState.MessengerType.TELEGRAM, "Десять");
        for (int i = 1; i < 11; ++i) {
            states.addUserLose(i);
            states.addUserWin(i);
        }
        List<ImmutablePair<String, Double>> expectedList = List.of(
                new ImmutablePair<>("Петя", 0.5),
                new ImmutablePair<>("Вася", 0.5),
                new ImmutablePair<>("Три", 0.5),
                new ImmutablePair<>("Четыре", 0.5),
                new ImmutablePair<>("Пять", 0.5),
                new ImmutablePair<>("Шесть", 0.5),
                new ImmutablePair<>("Семь", 0.5),
                new ImmutablePair<>("Восемь", 0.5),
                new ImmutablePair<>("Девять", 0.5),
                new ImmutablePair<>("Десять", 0.5));
        Assertions.assertIterableEquals(expectedList, states.getTopTenUsers());
    }

    /**
     * Проверить подсчёт статистики
     */
    @Test
    public void getUserRatingTest() {
        states.addUserLose(1);
        states.addUserWin(2);
        states.addUserWin(1);
        states.addUserLose(2);
        Assertions.assertEquals(new ImmutablePair<>("Петя", (double) 0.5), states.getUserRating(1));
        Assertions.assertEquals(new ImmutablePair<>("Вася", (double) 0.5), states.getUserRating(2));
    }

    /**
     * Проверить создание нового матча
     */
    @Test
    public void createNewLobbyTest() {
        states.createNewLobby("game", 1, 2,
                true, LobbyState.LobbyType.MULTIPLAYER,
                new byte[0][0], 0, true);
        Assertions.assertEquals(1, states.getLobbyFirstPlayer("game"));
        Assertions.assertEquals(2, states.getLobbySecondPlayer("game"));
        Assertions.assertTrue(states.isLobbyFirstPlayerToMove("game"));
        Assertions.assertEquals(LobbyState.LobbyType.MULTIPLAYER, states.getLobbyType("game"));
    }

    /**
     * Проверить удаление матча
     */
    @Test
    public void deleteLobbyTest() {
        states.createNewLobby("game", 1, 2,
                true, LobbyState.LobbyType.MULTIPLAYER,
                new byte[0][0], 0, true);
        states.deleteLobby("game");
        Assertions.assertNull(states.getLobbyType("game"));
    }

    /**
     * Проверить получение идентификатора другого игрока
     */
    @Test
    public void getLobbyAnotherUserIdTest() {
        states.createNewLobby("game", 1, 2,
                true, LobbyState.LobbyType.MULTIPLAYER,
                new byte[0][0], 0, true);
        Assertions.assertEquals(2, states.getLobbyAnotherUserId("game", 1));
    }

    /**
     * Проверить резервирование идентификатора матча
     */
    @Test
    public void bookLobbyNameTest() {
    	states.addNewUser(UserState.MessengerType.TELEGRAM, "SomeName3");
    	states.addNewUser(UserState.MessengerType.TELEGRAM, "SomeName4");
    	states.addUserWin(3);
    	states.addUserWin(4);
		states.bookLobbyName("game3", 3);
		states.bookLobbyName("game", 1);
		Assertions.assertIterableEquals(
				List.of(new ImmutablePair<>("game", 0.0)), 
				states.getBookedLobbies(1));
		states.bookLobbyName("game1", 2);
		Assertions.assertIterableEquals(
				List.of(
						new ImmutablePair<>("game", 0.0),
						new ImmutablePair<>("game1", 0.0)), 
				states.getBookedLobbies(1));
		Assertions.assertIterableEquals(
				List.of(new ImmutablePair<>("game3", 1.0)), 
				states.getBookedLobbies(4));
    }

    /**
     * Проверить удаление идентификатора матча из зарезервированных
     */
    @Test
    public void unbookLobbyNameTest() {
    	states.addNewUser(UserState.MessengerType.TELEGRAM, "SomeName3");
    	states.addUserWin(3);
		states.bookLobbyName("game3", 3);
		states.bookLobbyName("game", 1);
		states.bookLobbyName("game1", 2);
		states.unbookLobbyName("game3");
		Assertions.assertIterableEquals(
				List.of(
						new ImmutablePair<>("game", 0.0),
						new ImmutablePair<>("game1", 0.0)), 
				states.getBookedLobbies(1));
		states.unbookLobbyName("game");
		Assertions.assertIterableEquals(
				List.of(new ImmutablePair<>("game1", 0.0)), 
				states.getBookedLobbies(1));
    }

    /**
     * Проверить получение идентификатора создателя матча
     */
    @Test
    public void getLobbyCreatorTest() {
        states.bookLobbyName("game", 1);
        states.bookLobbyName("game1", 2);
        Assertions.assertEquals(1, states.getLobbyCreator("game"));
        Assertions.assertEquals(2, states.getLobbyCreator("game1"));
    }

    /**
     * Проверить установку идентификатора матча пользователя
     */
    @Test
    public void setUserLobbyNameTest() {
        states.setUserLobbyName(1, "game");
        states.setUserLobbyName(2, "game1");
        Assertions.assertEquals("game", states.getUserLobbyName(1));
        Assertions.assertEquals("game1", states.getUserLobbyName(2));
    }

    /**
     * Проверить существование лобби
     */
    @Test
    public void isLobbyExistingTest() {
        states.createNewLobby("game", 1, 2,
                true, LobbyState.LobbyType.MULTIPLAYER,
                new byte[0][0], 0, true);
        states.bookLobbyName("game1", 1);
        Assertions.assertTrue(states.isLobbyExisting("game"));
        Assertions.assertTrue(states.isLobbyExisting("game1"));
        Assertions.assertFalse(states.isLobbyExisting("game2"));
    }

    /**
     * Проверить доступность лобби
     */
    @Test
    public void isLobbyAvailableTest() {
        states.createNewLobby("game", 1, 2,
                true, LobbyState.LobbyType.MULTIPLAYER,
                new byte[0][0], 0, true);
        states.bookLobbyName("game1", 1);
        Assertions.assertFalse(states.isLobbyAvailable("game"));
        Assertions.assertTrue(states.isLobbyAvailable("game1"));
    }

    /**
     * Проверить добавление пользователю новой части хода
     */
    @Test
    public void addUserNewMovePartTest() {
        states.addUserNewMovePart(1, "p");
        states.addUserNewMovePart(1, "e2");
        states.addUserNewMovePart(2, "q");
        Assertions.assertEquals("p", states.getUserMovePartFigure(1));
        Assertions.assertEquals("e2", states.getUserMovePartStart(1));
        Assertions.assertEquals("", states.getUserMovePartFinish(1));
        Assertions.assertEquals("q", states.getUserMovePartFigure(2));
        Assertions.assertEquals("", states.getUserMovePartStart(2));
        Assertions.assertEquals("", states.getUserMovePartFinish(2));
    }

    /**
     * Проверить готовность хода
     */
    @Test
    public void isUserMoveReadyTest() {
        Assertions.assertFalse(states.isUserMoveReady(1));
        states.addUserNewMovePart(1, "p");
        states.addUserNewMovePart(1, "e2");
        states.addUserNewMovePart(1, "e4");
        Assertions.assertTrue(states.isUserMoveReady(1));
    }

    /**
     * Проверить смену ходящей стороны в матче
     */
    @Test
    public void changeLobbyMovingUserTest() {
        states.createNewLobby("game", 1, 2,
                true, LobbyState.LobbyType.MULTIPLAYER,
                new byte[0][0], 0, true);
        Assertions.assertTrue(states.isLobbyFirstPlayerToMove("game"));
        states.changeLobbyMovingUser("game");
        Assertions.assertFalse(states.isLobbyFirstPlayerToMove("game"));
    }

    /**
     * Проверить смену ходящей стороны в игре
     */
    @Test
    public void changeGameMovingSideTest() {
        states.createNewLobby("game", 1, 2,
                true, LobbyState.LobbyType.MULTIPLAYER,
                new byte[0][0], 0, true);
        Assertions.assertTrue(states.isGameWhiteToMove("game"));
        states.changeGameMovingSide("game");
        Assertions.assertFalse(states.isGameWhiteToMove("game"));
    }

    /**
     * Проверить добавление нового пользователя
     */
    @Test
    public void addNewUserTest() {
        Assertions.assertNull(states.getUserLobbyName(3));
        Assertions.assertNull(states.getUserLobbyName(4));
        states.addNewUser(UserState.MessengerType.TELEGRAM, "SomeName");
        Assertions.assertEquals("", states.getUserLobbyName(3));
        Assertions.assertNull(states.getUserLobbyName(4));
        states.addNewUser(UserState.MessengerType.TELEGRAM, "SomeName");
        Assertions.assertEquals("", states.getUserLobbyName(4));
    }

    /**
     * Проверить изменение последнего сообщения пользователю
     */
    @Test
    public void changeUserLastMessageTest() {
        states.changeUserLastMessage(1, 1);
        states.changeUserLastMessage(2, 2);
        Assertions.assertEquals(1, states.getUserMessageId(1));
        Assertions.assertEquals(2, states.getUserMessageId(2));
        states.changeUserLastMessage(1, 3);
        Assertions.assertEquals(3, states.getUserMessageId(1));
    }

    /**
     * Проверка сброса накопленного хода пользователя
     */
    @Test
    public void resetUserMoveStateTest() {
        states.addUserNewMovePart(1, "p");
        states.addUserNewMovePart(1, "e2");
        states.resetUserMoveState(1);
        Assertions.assertEquals("", states.getUserMovePartFigure(1));
        Assertions.assertEquals("", states.getUserMovePartStart(1));
        Assertions.assertEquals("", states.getUserMovePartFinish(1));
    }

    /**
     * Проверить установку идентификатора из мессенджера
     */
    @Test
    public void addNewMessengerIdTest() {
        states.addNewMessengerId(1, UserState.MessengerType.TELEGRAM, 10);
        states.addNewMessengerId(1, UserState.MessengerType.DISCORD, 11);
        states.addNewMessengerId(2, UserState.MessengerType.TELEGRAM, 20);
        Assertions.assertEquals(10, states.getUserMessengerId(1, UserState.MessengerType.TELEGRAM));
        Assertions.assertEquals(11, states.getUserMessengerId(1, UserState.MessengerType.DISCORD));
        Assertions.assertEquals(20, states.getUserMessengerId(2, UserState.MessengerType.TELEGRAM));
        Assertions.assertEquals(1, states.getUserIdFromTelegramId(10));
        Assertions.assertEquals(1, states.getUserIdFromDiscordId(11));
        Assertions.assertEquals(2, states.getUserIdFromTelegramId(20));
    }
}
