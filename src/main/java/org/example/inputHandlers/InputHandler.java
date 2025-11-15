package org.example.inputHandlers;

import java.util.List;

import org.example.states.UserState;

/**
 * Интерфейс обработчика ввода в различных режимах
 */
public interface InputHandler {
	/**
	 * Обработать пользовательский ввод
	 */
	public List<String> processInput(String userInput, UserState currentUserState);
}
