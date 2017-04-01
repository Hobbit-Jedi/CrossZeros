package ua.net.hj.cz.roles.players;

import ua.net.hj.cz.core.ActionFigure;
import ua.net.hj.cz.core.Move;
import ua.net.hj.cz.roles.Board;
import ua.net.hj.cz.roles.Rules;

/**
 * Описывает Игрока в целом (как абстрактную сущность).
 * Набор его интерфейсных методов и хранимых полей.
 * Конкретные реализации Игрока должны реализовать метод выполнения хода.
 * @author Hobbit Jedi
 */
public abstract class Player implements PlayerReadOnly {
	protected final byte mPlayerID; // Уникальный идентификатор игрока.
	protected final String mName;   // Имя игрока.
	boolean mIsDead;                // Признак того, что игрок больше не может использоваться и должен быть уничтожен.
	
	/**
	 * Создает игрока.
	 * @param aName - Имя игрока.
	 * @param aPlayerID - Уникальный идентифиатор игрока.
	 */
	protected Player(String aName, byte aPlayerID)
	{
		mName     = aName;
		mPlayerID = aPlayerID;
		mIsDead   = false;
	}
	
	@Override
	public byte getID()
	{
		if (mIsDead)
		{
			throw new IllegalStateException("Dead player usage.");
		}
		return mPlayerID;
	}

	@Override
	public String getName()
	{
		if (mIsDead)
		{
			throw new IllegalStateException("Dead player usage.");
		}
		return mName;
	}
	
	/**
	 * Получить строковое представление игрока.
	 * @return - Строковое представление игрока.
	 */
	@Override
	public String toString()
	{
		if (mIsDead)
		{
			throw new IllegalStateException("Dead player usage.");
		}
		StringBuilder result = new StringBuilder();
		result.append(mPlayerID);
		result.append(": ");
		result.append(mName);
		return result.toString();
	}
	
	/**
	 * Проверить совпадает ли данный игрок с другим игроком.
	 * @param obj - Игрок, с которым выполняется сравнение текущего игрока.
	 * @return - Признак того, что указанный игрок совпадает с текущим игроком.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (mIsDead)
		{
			throw new IllegalStateException("Dead player usage.");
		}
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Player other = (Player) obj;
		boolean result = (this.mPlayerID == other.mPlayerID);
		return result;
	}
	
	/**
	 * Вычислить хэш-код объекта.
	 * @return - хэш-код объекта.
	 */
	@Override
	public int hashCode()
	{
		if (mIsDead)
		{
			throw new IllegalStateException("Dead player usage.");
		}
		int hash = mPlayerID;
		return hash;
	}
	
	/**
	 * Ознакомиться с правилами.
	 * @param aRules - Правила, по которым будет вестись игра.
	 * @param aPlayersSequence - Порядок, в котором ходят участвующие в игре игроки.
	 *                           Массив содержит уникальные идентификаторы игроков.
	 */
	public void checkOutRules(Rules aRules, byte[] aPlayersSequence)
	{
		if (mIsDead)
		{
			throw new IllegalStateException("Dead player usage.");
		}
		// Реализация по умолчанию ничего не делает.
	}
	
	/**
	 * Обработать сообщение о победе одного из игроков.
	 * @param aPlayerID - Идентификатор победившего игрока.
	 */
	public void winNotificationHandler(byte aPlayerID)
	{
		if (mIsDead)
		{
			throw new IllegalStateException("Dead player usage.");
		}
		// Реализация по умолчанию ничего не делает.
	}
	
	/**
	 * Обработать сообщение о ничье.
	 */
	public void deadlockNotificationHandler()
	{
		if (mIsDead)
		{
			throw new IllegalStateException("Dead player usage.");
		}
		// Реализация по умолчанию ничего не делает.
	}
	
	/**
	 * Обработать сообщение о дисквалификации одного из игроков.
	 * @param aPlayerID - Идентификатор дисквалифицируемого игрока.
	 * @param aPlayersSequence - Порядок, в котором ходят оставшиеся в игре игроки.
	 *                           Массив содержит уникальные идентификаторы игроков.
	 */
	public void disqualificationNotificationHandler(byte aPlayerID, byte[] aPlayersSequence)
	{
		if (mIsDead)
		{
			throw new IllegalStateException("Dead player usage.");
		}
		// Реализация по умолчанию ничего не делает.
	}
	
	/**
	 * Выполнить ход.
	 * @param aBoard - Слепок текущей ситуации на игровом поле.
	 * @param aActivePlayersSequence - Порядок, в котором ходят еще активные участвующие в игре игроки.
	 *                                 Массив содержит уникальные идентификаторы игроков.
	 * @param aFigure - Фигура, которой игрок должен сделать ход.
	 * @return - Ход, который собирается делать игрок.
	 *           null, если игрок не знает куда пойти.
	 */
	abstract public Move makeMove(Board aBoard, byte[] aActivePlayersSequence, ActionFigure aFigure);
}
