package ua.net.hj.cz.core;

import ua.net.hj.cz.roles.players.PlayerReadOnly;

/**
 * Описывает ход игрока.
 * @author Hobbit Jedi
 */
public class Move {
	private final Coordinates mCoordinates; // Координаты, в которые делается ход.
	private final PlayerReadOnly mPlayer;   // Игрок, который делает ход.
	private final ActionFigure mFigure;     // Фигура, которой игрок делает ход.
	
	/**
	 * Создать ход по указанным по одтельности координатам.
	 * @param aX - X-координата, в которую делается ход.
	 * @param aY - Y-координата, в которую делается ход.
	 * @param aPlayer - Игрок, который делает ход.
	 * @param aFigure - Фигура, которой игрок делает ход.
	 */
	public Move(byte aX, byte aY, PlayerReadOnly aPlayer, ActionFigure aFigure)
	{
		mCoordinates = new Coordinates(aX, aY);
		mPlayer = aPlayer;
		mFigure = aFigure;
	}
	
	/**
	 * Создать ход по указанному объекту координат.
	 * @param aCoordinates - Координаты, в которые делается ход.
	 * @param aPlayer - Игрок, который делает ход.
	 * @param aFigure - Фигура, которой игрок делает ход.
	 */
	public Move(Coordinates aCoordinates, PlayerReadOnly aPlayer, ActionFigure aFigure)
	{
		mCoordinates = aCoordinates;
		mPlayer = aPlayer;
		mFigure = aFigure;
	}
	
	/**
	 * Получить координаты хода.
	 * @return - Координаты хода.
	 */
	public Coordinates getCoordinates()
	{
		return mCoordinates;
	}
	
	/**
	 * Получить числовую x-координату хода.
	 * @return - Числовая x-координата хода.
	 */
	public byte getX()
	{
		return mCoordinates.getX();
	}
	
	/**
	 * Получить числовую y-координату хода.
	 * @return - Числовая y-координата хода.
	 */
	public byte getY()
	{
		return mCoordinates.getY();
	}
	
	/**
	 * Получить сделавшего ход игрока.
	 * @return - Игрок, который сделал ход.
	 */
	public PlayerReadOnly getPlayer()
	{
		return mPlayer;
	}
	
	/**
	 * Получить фигуру, которой игрок сделал ход.
	 * @return - Фигура, которой сделан ход.
	 */
	public ActionFigure getFigure()
	{
		return mFigure;
	}
	
	/**
	 * Получить строковое представление хода.
	 * @return - Строковое представление хода.
	 */
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(mPlayer);
		result.append(" (");
		result.append(mFigure);
		result.append(") ходит в (");
		result.append(mCoordinates);
		result.append(")");
		return result.toString();
	}
	
	/**
	 * Проверить совпадает ли данный ход с другим ходом.
	 * @param obj - Ход, с которым выполняется сравнение текущего хода.
	 * @return - Признак того, что указанный ход совпадает с текущим ходом.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Move other = (Move) obj;
		boolean result = this.mCoordinates.equals(other.mCoordinates)
						&& this.mPlayer.equals(other.mPlayer)
						&& this.mFigure.equals(other.mFigure)
						;
		return result;
	}
	
	/**
	 * Вычислить хэш-код объекта.
	 * @return - хэш-код объекта.
	 */
	@Override
	public int hashCode()
	{
		int hash = (mCoordinates.hashCode()<<16) + (mPlayer.hashCode()<<8 & 0xFFFF) + (mFigure.hashCode() & 0xFF);
		return hash;
	}
	
}
