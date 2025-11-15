package org.example;

/**
 * Конвертер частей хода
 */
public class MovePartsConverter {
	/**
	 * Строка с сообщением о неизвестном коде, символе или названии
	 */
	private final static String UNKNOWN_FIGURE = "НЕИЗВЕСТНО";
	
	/**
	 * Получить название фигуры по символу
	 */
	public String getFigureName(String figureSymbol) {
		switch (figureSymbol) {
		case "p":
			return "ПЕШКА";
		case "r":
			return "ЛАДЬЯ";
		case "n":
			return "КОНЬ";
		case "b":
			return "СЛОН";
		case "q":
			return "ФЕРЗЬ";
		case "k":
			return "КОРОЛЬ";
		default:
			return UNKNOWN_FIGURE;
		}
	}
	
	/**
	 * Получить код фигуры по символу
	 */
	public int getFigureCode(String figure) {
		switch (figure) {
		case "":
			return 1;
		case "p":
			return 1;
		case "r":
			return 2;
		case "n":
			return 3;
		case "b":
			return 4;
		case "q":
			return 5;
		case "k":
			return 6;
		default:
			return 0;
		}
    }
    
	/**
	 * Получить код ряда по символу
	 */
    public int getPositionRowCode(char digit) {
    	switch (digit) {
		case '1':
			return 0;
		case '2':
			return 1;
		case '3':
			return 2;
		case '4':
			return 3;
		case '5':
			return 4;
		case '6':
			return 5;
		case '7':
			return 6;
		case '8':
			return 7;
		default:
			return -1;
		}
    }
    
    /**
	 * Получить код столбца по символу
	 */
    public int getPositionColumnCode(char letter) {
    	switch (letter) {
		case 'a':
			return 0;
		case 'b':
			return 1;
		case 'c':
			return 2;
		case 'd':
			return 3;
		case 'e':
			return 4;
		case 'f':
			return 5;
		case 'g':
			return 6;
		case 'h':
			return 7;
		default:
			return -1;
		}
    }
    
    /**
	 * Получить символ фигуры по коду
	 */
    public String getFigureSymbol(int figureCode) {
    	switch (figureCode) {
		case 1:
			return "p";
		case 2:
			return "r";
		case 3:
			return "n";
		case 4:
			return "b";
		case 5:
			return "q";
		case 6:
			return "k";
		default:
			return "";
		}
    }
    
    /**
	 * Получить символ ряда по коду
	 */
    public char getPositionRowSymbol(int rowCode) {
    	switch (rowCode) {
		case 0:
			return '1';
		case 1:
			return '2';
		case 2:
			return '3';
		case 3:
			return '4';
		case 4:
			return '5';
		case 5:
			return '6';
		case 6:
			return '7';
		case 7:
			return '8';
		default:
			return '\0';
		}
    }
    
    /**
	 * Получить символ столбца по коду
	 */
    public char getPositionColumnSymbol(int columnCode) {
    	switch (columnCode) {
		case 0:
			return 'a';
		case 1:
			return 'b';
		case 2:
			return 'c';
		case 3:
			return 'd';
		case 4:
			return 'e';
		case 5:
			return 'f';
		case 6:
			return 'g';
		case 7:
			return 'h';
		default:
			return '\0';
		}
    }
}
