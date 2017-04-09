package ua.net.hj.cz.core;

/**
 * Описывает фигуры, которые отображаются на доске в местах ходов игроков.
 * @author Hobbit Jedi
 */
public enum ActionFigure {
	CROSS(GraphicSymbolsSet.FIGURE_CROSS), //крестик
	NOUGHT(GraphicSymbolsSet.FIGURE_ZERO), //нолик
	STAR(GraphicSymbolsSet.FIGURE_STAR),   //звезда
	STOP(GraphicSymbolsSet.FIGURE_STOP),   //знак СТОП
	CHECK(GraphicSymbolsSet.FIGURE_CHECK)  //галочка
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
