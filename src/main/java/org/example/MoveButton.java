package org.example;

/**
 * Класс, играющий вспомогательную роль в созданиии Inline кнопок
 * для хода фигур
 */
public class MoveButton {
	/**
	 * Текст callback запроса, установленного для кнопки 
	 */
	private final String query;
	/**
	 * Текст самой кнопки
	 */
	private final String text;
	
	/**
	 * Создать новую кнопку
	 * @param callbackQuery
	 * @param buttonText
	 */
	public MoveButton(String callbackQuery, String buttonText) {
		query = callbackQuery;
		text = buttonText;
	}
	
	/**
	 * Узнать текст callback запроса
	 * @return экземпляр String
	 */
	public String getCallbackQuery() {
		return query;
	}
	/**
	 * Узнать текст кнопки
	 * @return экзмепляр String
	 */
	public String getButtonMessage() {
		return text;
	}
}
