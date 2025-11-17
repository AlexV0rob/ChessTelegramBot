package org.example.inputHandlers;

/**
 * Обработчик ввлода в главном меню
 */
public class MainMenuInputHandler {
    /**
     * Текст кнопки меню для начала одиночной игры
     */
    private final static String NEW_SINGLE_GAME = "Начать игру на этом устройстве";
    /**
     * Текст кнопки меню для создания  многопользовательского лобби
     */
    private final static String NEW_MULTIPLAYER_GAME = "Создать собственное лобби";
    /**
     * Текст кнопки меню для открытия списка доступных лобби
     */
    private final static String JOIN_MULTIPLAYER_GAME = "Присоединится к чужому лобби";

    /**
     * Получить командный эквивалент текстовому вводу
     */
    public String processInput(String userInput) {
        switch (userInput) {
            case NEW_SINGLE_GAME:
                return "newsinglegame";
            case NEW_MULTIPLAYER_GAME:
                return "newmultiplayergame";
            case JOIN_MULTIPLAYER_GAME:
                return "joinmultiplayergame";
            default:
                return "unknown";
        }
    }
}
