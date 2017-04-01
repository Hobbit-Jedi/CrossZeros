package ua.net.hj.cz.core;

/**
 * Описывает фигуры, которые отображаются на доске в местах ходов игроков.
 * @author Hobbit Jedi
 */
public enum ActionFigure {
	CROSS('\u2573'),  //крестик
	NOUGHT('\u2B58'), //нолик
	STAR('\u26E4'),   //звезда
	STOP('\u26D4'),   //знак СТОП
	CHECK('\u2705')   //галочка
	;
	
	private final char mImage; // Отображаемый символ.
	
	private ActionFigure(char aImage)
	{
		mImage = aImage;
	}
	
	/**
	 * Получить строковое представление фигуры.
	 * @return - Строковое представление фигуры.
	 */
	@Override
	public String toString()
	{
		return String.valueOf(mImage);
	}
}
