package org.example;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * Конвертер частей хода
 */
public class MovePartsConverter {
	/**
	 * Двунаправленный ассоциативный массив с соответствием 
	 * символ фигуры - код фигуры 
	 */
	private final static BidiMap<String, Integer> FIGURES_CODES = 
			new DualHashBidiMap<String, Integer>();
	/**
	 * Двунаправленный ассоциативный массив с соответствием 
	 * символ столбца - код столбца 
	 */
	private final static BidiMap<Character, Integer> LETTERS_CODES = 
			new DualHashBidiMap<Character, Integer>();
	/**
	 * Двунаправленный ассоциативный массив с соответствием 
	 * символ ряда - код ряда 
	 */
	private final static BidiMap<Character, Integer> DIGITS_CODES = 
			new DualHashBidiMap<Character, Integer>();
	/**
	 * Двунаправленный ассоциативный массив с соответствием 
	 * символ фигуры - название фигуры 
	 */
	private final static BidiMap<String, String> FIGURES_NAMES = 
			new DualHashBidiMap<String, String>();
	/**
	 * Строка с сообщением о неизвестном коде, символе или названии
	 */
	private final static String UNKNOWN_FIGURE = "НЕИЗВЕСТНО";
	
	/**
	 * Конструктор класса
	 */
	public MovePartsConverter() {
		FIGURES_CODES.put("p", 1);
		FIGURES_CODES.put("r", 2);
		FIGURES_CODES.put("n", 3);
		FIGURES_CODES.put("b", 4);
		FIGURES_CODES.put("q", 5);
		FIGURES_CODES.put("k", 6);

		LETTERS_CODES.put('a', 0);
		LETTERS_CODES.put('b', 1);
		LETTERS_CODES.put('c', 2);
		LETTERS_CODES.put('d', 3);
		LETTERS_CODES.put('e', 4);
		LETTERS_CODES.put('f', 5);
		LETTERS_CODES.put('g', 6);
		LETTERS_CODES.put('h', 7);
		
		DIGITS_CODES.put('1', 0);
		DIGITS_CODES.put('2', 1);
		DIGITS_CODES.put('3', 2);
		DIGITS_CODES.put('4', 3);
		DIGITS_CODES.put('5', 4);
		DIGITS_CODES.put('6', 5);
		DIGITS_CODES.put('7', 6);
		DIGITS_CODES.put('8', 7);
		
		FIGURES_NAMES.put("p", "ПЕШКА");
		FIGURES_NAMES.put("r", "ЛАДЬЯ");
		FIGURES_NAMES.put("n", "КОНЬ");
		FIGURES_NAMES.put("b", "СЛОН");
		FIGURES_NAMES.put("q", "ФЕРЗЬ");
		FIGURES_NAMES.put("k", "КОРОЛЬ");
	}
	
	/**
	 * Получить название фигуры по символу
	 */
	public String getFigureName(String figureSymbol) {
    	return FIGURES_NAMES.getOrDefault(figureSymbol, UNKNOWN_FIGURE);
	}
	
	/**
	 * Получить код фигуры по символу
	 */
	public int getFigureCode(String figure) {
		if (figure.isEmpty()) {
			return 1;
		}
    	return FIGURES_CODES.getOrDefault(figure, 0);
    }
    
	/**
	 * Получить код ряда по символу
	 */
    public int getPositionRowCode(char digit) {
    	return DIGITS_CODES.getOrDefault(digit, -1);
    }
    
    /**
	 * Получить код столбца по символу
	 */
    public int getPositionColumnCode(char letter) {
    	return LETTERS_CODES.getOrDefault(letter, -1);
    }
    
    /**
	 * Получить символ фигуры по коду
	 */
    public String getFigureSymbol(int figureCode) {
    	if (FIGURES_CODES.getKey(figureCode) == null) {
    		return "";
    	}
    	return FIGURES_CODES.getKey(figureCode);
    }
    
    /**
	 * Получить символ ряда по коду
	 */
    public char getPositionRowSymbol(int rowCode) {
    	if (DIGITS_CODES.getKey(rowCode) == null) {
    		return '\0';
    	}
    	return DIGITS_CODES.getKey(rowCode);
    }
    
    /**
	 * Получить символ столбца по коду
	 */
    public char getPositionColumnSymbol(int rowCode) {
    	if (LETTERS_CODES.getKey(rowCode) == null) {
    		return '\0';
    	}
    	return LETTERS_CODES.getKey(rowCode);
    }
}
