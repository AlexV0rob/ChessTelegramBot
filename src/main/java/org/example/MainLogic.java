package org.example;

import java.util.ArrayList;

/**
 * Отвечает за основную логику
 */
public class MainLogic {
	/**
	 * Выполняет первичное управление сообщением от пользователя
	 * @param input содержит текст сообщения
	 * @return ответное сообщение
	 */
	public ArrayList<String> processInput(String input, User curUser) {
		ArrayList<String> responses;
		if (input.charAt(0) == '/') {
			responses = new CommandHandler().processCommand(input, curUser);
		} else {
			/*
			 * Коды режимов:
			 * 0 - главное меню
			 * 1 - режим эхо
			 * 2 - одиночная игра (игра на одном устройстве)
			 */
			switch (curUser.getMode()) {
			case 0:
				//TODO MenuHandler
				responses = new ArrayList<String>();
				responses.add("Прости, не очень понял запрос");
				responses.add("Чем займёмся?");
				break;
			case 1:
				responses = new ArrayList<String>();
				responses.add(new EchoHandler().echoMessage(input));
				break;
			case 2:
				//TODO GameHandler
				//responses = new GameHandler(curUser.getBoard());
			default:
				responses = new ArrayList<String>();
				curUser.changeMode((byte) 0);
				responses.add("Чем займёмся?");
			}
		}
		return responses;
	}	
	
	
}
