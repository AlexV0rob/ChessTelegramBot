package org.example.states;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Проверка хранителя пользовательского состояния
 */
public class UserStateTest {
    /**
     * Храниель пользовательского состояния
     */
    private final UserState userState = new UserState(null, "");

    /**
     * Проверка смены режима
     */
    @Test
    public void modeChangingTest() {
        userState.setUserState(UserState.UserStatus.INGAME);
        Assertions.assertEquals(UserState.UserStatus.INGAME,
                userState.getUserStatus());
        userState.setUserState(UserState.UserStatus.MAINMENU);
        Assertions.assertEquals(UserState.UserStatus.MAINMENU,
                userState.getUserStatus());
    }
}
