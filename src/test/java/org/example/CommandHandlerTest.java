package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.example.states.LobbyState;
import org.example.states.UserState;
import org.example.statesHandlers.FakeStatesHandler;

/**
 * Проверка обработчика команд
 */
public class CommandHandlerTest {
    /**
     * Хранитель состояний для проверки их изменения
     */
    private final FakeStatesHandler states = new FakeStatesHandler();

    /**
     * Обработчик команд
     */
    private final CommandHandler commandHandler = new CommandHandler(states);

    /**
     * Проверить работу команды /quit на одном пользователе
     */
    @Test
    public void quitCommandSingleUserTest() {
        states.resetAll();
        long userId = states.addNewUserWithStatus(UserState.UserStatus.INGAME);
        states.createNewLobby("game", userId, userId, true, LobbyState.LobbyType.SINGLEPLAYER);
        states.setUserLobbyName(userId, "game");
        commandHandler.processQuitCommand(userId);
        Assertions.assertEquals(UserState.UserStatus.MAINMENU, states.getUserStatus(userId));
        Assertions.assertEquals("", states.getUserLobbyName(userId));
        Assertions.assertFalse(states.isLobbyExisting("game"));
    }

    /**
     * Проверить работу команды /quit на двух пользователях
     */
    @Test
    public void quitCommandTwoUsersTest() {
        states.resetAll();
        long userId1 = states.addNewUserWithStatus(UserState.UserStatus.INGAME);
        long userId2 = states.addNewUserWithStatus(UserState.UserStatus.INGAME);
        states.createNewLobby("game", userId1, userId2, true, LobbyState.LobbyType.MULTIPLAYER);
        states.setUserLobbyName(userId1, "game");
        states.setUserLobbyName(userId2, "game");
        commandHandler.processQuitCommand(userId1);
        Assertions.assertEquals(UserState.UserStatus.MAINMENU, states.getUserStatus(userId1));
        Assertions.assertEquals(UserState.UserStatus.MAINMENU, states.getUserStatus(userId2));
        Assertions.assertEquals("", states.getUserLobbyName(userId1));
        Assertions.assertEquals("", states.getUserLobbyName(userId2));
        Assertions.assertFalse(states.isLobbyExisting("game"));
    }

    /**
     * Проверить работу команды /new_local
     */
    @Test
    public void newLocalCommandSingleUserTest() {
        states.resetAll();
        long userId = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        commandHandler.processNewLocalCommand(userId);
        Assertions.assertEquals(UserState.UserStatus.INGAME, states.getUserStatus(userId));
        Assertions.assertTrue(states.isLobbyExisting("1"));
        Assertions.assertFalse(states.isLobbyAvailable("1"));
        Assertions.assertEquals(LobbyState.LobbyType.SINGLEPLAYER, states.getLobbyType("1"));
        Assertions.assertEquals("1", states.getUserLobbyName(userId));
        Assertions.assertEquals(userId, states.getLobbyFirstPlayer("1"));
        Assertions.assertEquals(userId, states.getLobbySecondPlayer("1"));
    }

    /**
     * Проверить создание матча
     */
    @Test
    public void createValidLobbyNameTest() {
        states.resetAll();
        long userId = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        try {
            commandHandler.processCreateCommand(userId, "game");
        } catch (CommandException e) {
            Assertions.assertFalse(true);
        }
        Assertions.assertEquals(UserState.UserStatus.AWAITING, states.getUserStatus(userId));
        Assertions.assertEquals("game", states.getUserLobbyName(userId));
        Assertions.assertTrue(states.isLobbyExisting("game"));
        Assertions.assertTrue(states.isLobbyAvailable("game"));
    }

    /**
     * Проверить создание матча без аргумента
     */
    @Test
    public void createNoLobbyNameExceptionTest() {
        states.resetAll();
        long userId = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        CommandException exception = Assertions.assertThrows(
                CommandException.class, () ->
                        commandHandler.processCreateCommand(userId, ""));
        Assertions.assertEquals(
                "Придумайте название для матча (не более 16 символов):",
                exception.getMessage());
        Assertions.assertEquals(UserState.UserStatus.CREATING, states.getUserStatus(userId));
    }

    /**
     * Проверить создание матча со слишком длинным названием
     */
    @Test
    public void createTooLongLobbyNameExceptionTest() {
        states.resetAll();
        long userId = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        CommandException exception = Assertions.assertThrows(
                CommandException.class, () ->
                        commandHandler.processCreateCommand(userId, "abcdefghijklmnopqrstuvwxyz"));
        Assertions.assertEquals(
                "Извините, название должно быть не более 16 символов. Придумайте другое:",
                exception.getMessage());
        Assertions.assertEquals(UserState.UserStatus.CREATING, states.getUserStatus(userId));
    }

    /**
     * Проверить создание матча с занятым названием
     */
    @Test
    public void createExistingLobbyNameExceptionTest() {
        states.resetAll();
        long userId1 = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        long userId2 = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        try {
            commandHandler.processCreateCommand(userId2, "game");
        } catch (CommandException e) {
            Assertions.assertFalse(true);
        }
        CommandException exception = Assertions.assertThrows(
                CommandException.class, () ->
                        commandHandler.processCreateCommand(userId1, "game"));
        Assertions.assertEquals(
                "Извините, данное название уже занято. Придумайте другое:",
                exception.getMessage());
        Assertions.assertEquals(UserState.UserStatus.CREATING, states.getUserStatus(userId1));
    }

    /**
     * Проверить присоединение к матчу
     */
    @Test
    public void joinValidLobbyNameTest() {
        states.resetAll();
        long userId1 = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        long userId2 = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        try {
            commandHandler.processCreateCommand(userId1, "game");
            commandHandler.processJoinCommand(userId2, "game");
        } catch (CommandException e) {
            Assertions.assertFalse(true);
        }
        Assertions.assertEquals(UserState.UserStatus.INGAME, states.getUserStatus(userId1));
        Assertions.assertEquals(UserState.UserStatus.INGAME, states.getUserStatus(userId2));
        Assertions.assertEquals("game", states.getUserLobbyName(userId1));
        Assertions.assertEquals("game", states.getUserLobbyName(userId2));
        Assertions.assertTrue(states.isLobbyExisting("game"));
        Assertions.assertFalse(states.isLobbyAvailable("game"));
        Assertions.assertEquals(LobbyState.LobbyType.MULTIPLAYER, states.getLobbyType("game"));
        Assertions.assertEquals(userId1, states.getLobbyFirstPlayer("game"));
        Assertions.assertEquals(userId2, states.getLobbySecondPlayer("game"));
    }

    /**
     * Проверить присоединение к матчу без аргумента
     */
    @Test
    public void joinNoLobbyNameExceptionTest() {
        states.resetAll();
        long userId = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        CommandException exception = Assertions.assertThrows(
                CommandException.class, () ->
                        commandHandler.processJoinCommand(userId, ""));
        Assertions.assertEquals(
                """
                        Вот список доступных сейчас матчей.
                        Нажмите на название или введите его, чтобы присоединиться.
                        Введите /quit, чтобы выйти.
                        """,
                exception.getMessage());
        Assertions.assertEquals(UserState.UserStatus.CHOOSING, states.getUserStatus(userId));
    }

    /**
     * Проверка команды link
     */
    @Test
    public void commandLinkTest() {
        states.resetAll();
        Assertions.assertFalse(states.isMessengerIdExisting(UserState.MessengerType.TELEGRAM, 1));
        long userId1 = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        try {
            commandHandler.processLinkCommand(userId1, 1, UserState.MessengerType.TELEGRAM);
        } catch (CommandException e) {
            {
                Assertions.assertFalse(true);
            }
        }
        Assertions.assertFalse(states.isMessengerIdExisting(UserState.MessengerType.DISCORD, 1));
        Assertions.assertTrue(states.isMessengerIdExisting(UserState.MessengerType.TELEGRAM, 1));
    }

    /**
     * Проверить присоединение к несуществующему матчу
     */
    @Test
    public void joinNotExistingLobbyNameExceptionTest() {
        states.resetAll();
        long userId = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        CommandException exception = Assertions.assertThrows(
                CommandException.class, () ->
                        commandHandler.processJoinCommand(userId, "game"));
        Assertions.assertEquals(
                "Матча с таким идентификатором не существует",
                exception.getMessage());
        Assertions.assertEquals(UserState.UserStatus.CHOOSING, states.getUserStatus(userId));
    }

    /**
     * Проверить присоединение к идущему матчу
     */
    @Test
    public void joinNotAvailableLobbyNameExceptionTest() {
        states.resetAll();
        long userId1 = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        long userId2 = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        long userId3 = states.addNewUserWithStatus(UserState.UserStatus.MAINMENU);
        try {
            commandHandler.processCreateCommand(userId2, "game");
            commandHandler.processJoinCommand(userId3, "game");
        } catch (CommandException e) {
            Assertions.assertFalse(true);
        }
        CommandException exception = Assertions.assertThrows(
                CommandException.class, () ->
                        commandHandler.processJoinCommand(userId1, "game"));
        Assertions.assertEquals(
                "Этот матч уже начат, Вы не можете к нему подключиться",
                exception.getMessage());
        Assertions.assertEquals(UserState.UserStatus.CHOOSING, states.getUserStatus(userId1));
    }
}
