package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Тестирование класса MoveMaker
 */
public class MoveMakerTest {
	/**
	 * Проверить, если запрос не имеет частей хода
	 */
	@Test
	void noPartsTest() {
		User user = new User();
		user.changeMode((byte) 2);
		user.newBoard();
		String callBackQuery = "move";
		
		ArrayList<MoveButton> response = new MoveMaker()
				.assembleMove(callBackQuery, user);
		int buttonsCount = response.size();
		
		for (int i = 0; i < buttonsCount; ++i) {
			String newCallbackQuery = response.get(i).getCallbackQuery();
			if (!newCallbackQuery.equals("message") &&
					!newCallbackQuery.equals("board") &&
					!newCallbackQuery.equals("side") &&
					!newCallbackQuery.equals("error") &&
					!newCallbackQuery.equals("win")) {
				int partsCount = StringUtils.countMatches(newCallbackQuery, '_');
				int lastPart = (int) StringUtils.split(newCallbackQuery, '_')[1].charAt(0);
			
				Assertions.assertEquals(partsCount, 1);
				Assertions.assertTrue(lastPart >= 1 && lastPart <= 12);
			}
		}
	}
	
	/**
	 * Проверить, если запрос имеет 1 часть хода (фигура)
	 */
	@Test
	void onePartTest() {
		User user = new User();
		user.changeMode((byte) 2);
		user.newBoard();
		String callBackQuery = "move_" + ((char) 2);
		
		ArrayList<MoveButton> response = new MoveMaker()
				.assembleMove(callBackQuery, user);
		int buttonsCount = response.size();
		
		for (int i = 0; i < buttonsCount; ++i) {
			String newCallbackQuery = response.get(i).getCallbackQuery();
			if (!newCallbackQuery.equals("message") &&
					!newCallbackQuery.equals("board") &&
					!newCallbackQuery.equals("side") &&
					!newCallbackQuery.equals("error") &&
					!newCallbackQuery.equals("win")) {
				int partsCount = StringUtils.countMatches(newCallbackQuery, '_');
				int lastPart = (int) StringUtils.split(newCallbackQuery, '_')[2].charAt(0);
			
				Assertions.assertEquals(partsCount, 2);
				Assertions.assertTrue(lastPart >= 0 && lastPart <= 63);
			}
		}
	}
	
	/**
	 * Проверить, если запрос имеет 2 части хода (фигура и начальная позиция)
	 */
	@Test
	void twoPartsTest() {
		User user = new User();
		user.changeMode((byte) 2);
		user.newBoard();
		String callBackQuery = "move_" + ((char) 2) + "_" + ((char) (5 * 8 + 2));
		
		ArrayList<MoveButton> response = new MoveMaker()
				.assembleMove(callBackQuery, user);
		int buttonsCount = response.size();
		
		for (int i = 0; i < buttonsCount; ++i) {
			String newCallbackQuery = response.get(i).getCallbackQuery();
			if (!newCallbackQuery.equals("message") &&
					!newCallbackQuery.equals("board") &&
					!newCallbackQuery.equals("side") &&
					!newCallbackQuery.equals("error") &&
					!newCallbackQuery.equals("win")) {
				int partsCount = StringUtils.countMatches(newCallbackQuery, '_');
				int lastPart = (int) StringUtils.split(newCallbackQuery, '_')[3].charAt(0);
			
				Assertions.assertEquals(partsCount, 3);
				Assertions.assertTrue(lastPart >= 0 && lastPart <= 7);
			}
		}
	}
	
	/**
	 * Проверить, если запрос имеет 3 части хода (фигура, начальная позиция,
	 * буква конечной позиции)
	 */
	@Test
	void threePartsTest() {
		User user = new User();
		user.changeMode((byte) 2);
		user.newBoard();
		String callBackQuery = "move_" + ((char) 2) + "_" + ((char) (5 * 8 + 2)) +
				"_" + ((char) 5);
		
		ArrayList<MoveButton> response = new MoveMaker()
				.assembleMove(callBackQuery, user);
		int buttonsCount = response.size();
		
		for (int i = 0; i < buttonsCount; ++i) {
			String newCallbackQuery = response.get(i).getCallbackQuery();
			if (!newCallbackQuery.equals("message") &&
					!newCallbackQuery.equals("board") &&
					!newCallbackQuery.equals("side") &&
					!newCallbackQuery.equals("error") &&
					!newCallbackQuery.equals("win")) {
				int partsCount = StringUtils.countMatches(newCallbackQuery, '_');
				int lastPart = (int) StringUtils.split(newCallbackQuery, '_')[4].charAt(0);
			
				Assertions.assertEquals(partsCount, 4);
				Assertions.assertTrue(lastPart >= 0 && lastPart <= 8);
			}
		}
	}
	
	/**
	 * Проверить, если запрос имеет 4 части (запрос полный и правильный) 
	 */
	@Test
	void fourPartsTest() {
		User user = new User();
		user.changeMode((byte) 2);
		user.newBoard();
		String callBackQuery = "move_" + ((char) 2) + "_" + ((char) (5 * 8 + 2)) +
				"_" + ((char) 5) + "_" + ((char) 4);
		
		ArrayList<MoveButton> response = new MoveMaker()
				.assembleMove(callBackQuery, user);
		int buttonsCount = response.size();
		
		for (int i = 0; i < buttonsCount; ++i) {
			String newCallbackQuery = response.get(i).getCallbackQuery();
			if (!newCallbackQuery.equals("message") &&
					!newCallbackQuery.equals("board") &&
					!newCallbackQuery.equals("side") &&
					!newCallbackQuery.equals("error") &&
					!newCallbackQuery.equals("win")) {
				int partsCount = StringUtils.countMatches(newCallbackQuery, '_');
				int lastPart = (int) StringUtils.split(newCallbackQuery, '_')[1].charAt(0);
			
				Assertions.assertEquals(partsCount, 1);
				Assertions.assertTrue(lastPart >= 1 && lastPart <= 12);
			}
		}
	}
	
	/**
	 * Проверить текстовый запрос
	 */
	@Test
	void textQueryTest() {
		User user = new User();
		user.changeMode((byte) 2);
		user.newBoard();
		String messageQuery = "пешка e2 e4";
		String badQuery = "что-то t9 z12";
		
		ArrayList<String> response = new MoveMaker()
				.fromStringToQuery(messageQuery, user);
		ArrayList<String> badResponse = new MoveMaker()
				.fromStringToQuery(badQuery, user);
		boolean responseContains = false, badContains = false;
		for (String message : response) {
			responseContains = responseContains || message.contains("Ошибка");
		}
		for (String message : badResponse) {
			badContains = badContains || message.contains("Ошибка");
		}
		
		Assertions.assertFalse(responseContains);
		Assertions.assertTrue(badContains);
	}
}
	