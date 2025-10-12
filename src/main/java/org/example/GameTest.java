package org.example;

import org.telegram.telegrambots.meta.api.objects.Update;

public class GameTest {
	void TestSomething(Update update){
		byte[] board = new byte[] {3, 5, 7, 9, 11, 7, 5, 3,
				  1, 1, 1, 1, 1, 1, 1, 1,
				  0, 0, 0, 0, 0, 0, 0, 0,
				  0, 0, 0, 0, 0, 0, 0, 0,
				  0, 0, 0, 0, 0, 0, 0, 0,
				  0, 0, 0, 0, 0, 0, 0, 0,
				  2, 2, 2, 2, 2, 2, 2, 2,
				  4, 6, 8, 10, 12, 8, 6, 4};
		boolean GameState = true;
		byte isBlack = 0;// первыми ходят белые
		while(GameState)
		{

			String rawUserInput="";
			// получение сообщения от игрока в userInput
			String[] userInput = rawUserInput.split(" ");
			switch(userInput[0])
			{
				case "ПЕШКА":
					Chessmen pawn = new Pawn();
					pawn.Move(ConvertUserInput(userInput[1]),ConvertUserInput(userInput[2]),board,isBlack);
					break;
				case "СЛОН":
					Chessmen bishop = new Bishop();
					bishop.Move(ConvertUserInput(userInput[1]),ConvertUserInput(userInput[2]),board,isBlack);
					break;
				case "КОРОЛЬ":
					Chessmen king = new King();
					king.Move(ConvertUserInput(userInput[1]),ConvertUserInput(userInput[2]),board,isBlack);
					break;
				case "ФЕРЗЬ":
					Chessmen queen = new Queen();
					queen.Move(ConvertUserInput(userInput[1]),ConvertUserInput(userInput[2]),board,isBlack);
				case "ЛАДЬЯ":
					Chessmen castle = new Castle();
					castle.Move(ConvertUserInput(userInput[1]),ConvertUserInput(userInput[2]),board,isBlack);
				case "КОНЬ":
					Chessmen knights = new Knight();
					knights.Move(ConvertUserInput(userInput[1]),ConvertUserInput(userInput[2]),board,isBlack);
			}

			isBlack = (byte)((-1)*isBlack);
		}
	}
	int ConvertUserInput(String rawUserInput)
	{;
		int result = 0;
		//проверяем что первый символ это Английская буква
		if(((int)rawUserInput.charAt(0) > 90 || (int)rawUserInput.charAt( 0) < 65)) 
			result = -1;
		//проверяем что точка состоит только из одной буквы и одной цифры, а также что второй символ это цифра
		else if((rawUserInput.length() != 2) || ((int)rawUserInput.charAt(1) > 57 || (int)rawUserInput.charAt(1) < 49))
			result = -1;
		else
			result = ((int)rawUserInput.charAt(0) - 65) + 8 *((int)rawUserInput.charAt(0)) - 49;
		return result;
	}
}
