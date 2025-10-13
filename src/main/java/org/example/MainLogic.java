package org.example;

import java.util.ArrayList;

/**
 * Выполняет обработку пользовательского ввода
 */
public class MainLogic {
	/**
	 * Определить тип пользовательского ввода и передать управление
	 * соответствующему обработчику
	 * @param userInput
	 * @return экземпляр ArrayList, содержит ответные сообщения (класс String)
	 */
	public ArrayList<String> processInput(String userInput, User curUser) {
		ArrayList<String> responses;
		if (userInput.charAt(0) == '/') {
			responses = new CommandHandler().processCommand(userInput, curUser);
		} else {
			/*
			 * Коды режимов:
			 * 0 - главное меню
			 * 1 - режим эхо
			 * 2 - одиночная игра (игра на одном устройстве)
			 */
			switch (curUser.getMode()) {
			case 0:
				responses = new MenuHandler().processMenu(userInput, curUser);
				break;
			case 1:
				responses = new ArrayList<String>();
				responses.add(new EchoHandler().echoMessage(userInput));
				break;
			case 2:
				responses = new MoveMaker().fromStringToQuery(userInput, curUser);
				break;
				
			//Если режим другой, то произошла ошибка, выходим в главное меню
			default:
				responses = new CommandHandler().processCommand("/menu", curUser);
			}
		}
		return responses;
	}
}
