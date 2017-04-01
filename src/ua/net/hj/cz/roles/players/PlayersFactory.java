package ua.net.hj.cz.roles.players;

/**
 * Описывает фабрику, создающую и утилизирующую игроков.
 * @author Hobbit Jedi
 */
public class PlayersFactory {
	/*
	Массив использования уникальных идентификаторов игроков.
	Если mPlayerIDsUsage[i] == true,
	значит mPlayerID == i+1 уже используется каким-либо объектом игрока.
	Иначе соответствующий идентификатор игрока свободен для использования.
	*/
	private final boolean[] mPlayerIDsUsage;
	
	/**
	 * Создать фабрику игроков.
	 */
	public PlayersFactory()
	{
		mPlayerIDsUsage = new boolean[Byte.MAX_VALUE];
	}
	
	/**
	 * Создать нового игрока "Компьютер: Случайный стрелок".
	 * @param aName - Имя создаваемого игрока.
	 * @return - Созданный игрок.
	 * @throws PlayerException - Если во время создания игрока возникла ошибка,
	 *                           например, заполнен пул уникальных идентификаторов игроков,
	 *                           то вызывает данное исключение.
	 */
	public Player createRandomPlayer(String aName) throws PlayerException
	{
		byte newPlayerID = getFreePlayerID();
		if (newPlayerID != 0)
		{
			return new PlayerRandom(aName, newPlayerID);
		}
		else
		{
			throw new PlayerException("Players quantity overflow!");
		}
	}
	
	/**
	 * Создать нового игрока "Человек".
	 * @param aName - Имя создаваемого игрока.
	 * @return - Созданный игрок.
	 * @throws PlayerException - Если во время создания игрока возникла ошибка,
	 *                           например, заполнен пул уникальных идентификаторов игроков,
	 *                           то вызывает данное исключение.
	 */
	public Player createHumanPlayer(String aName) throws PlayerException
	{
		byte newPlayerID = getFreePlayerID();
		if (newPlayerID != 0)
		{
			return new PlayerHuman(aName, newPlayerID);
		}
		else
		{
			throw new PlayerException("Players quantity overflow!");
		}
	}
	
	/**
	 * Освобождение ресурсов, занимаемых игроком.
	 * @param aPlayer - Игрок, от которого освобождаются ресурсы.
	 * @throws PlayerException - Если методу передан игрок, у которого идентификатор
	 *                           не числится в данной фабрике как занятый,
	 *                           или у которого неположительный идентификатор,
	 *                           то вызывает исключение.
	 */
	public void releasePlayer(Player aPlayer) throws PlayerException
	{
		byte playerID = aPlayer.getID();
		if (playerID > 0)
		{
			if (mPlayerIDsUsage[playerID-1] == true)
			{
				mPlayerIDsUsage[playerID-1] = false;
				aPlayer.mIsDead = true;
			}
			else
			{
				throw new PlayerException("Trying to release a player, which is not belong to current factory.");
			}
		}
		else
		{
			throw new PlayerException("Trying to release player with non-positive ID.");
		}
	}
	
	/**
	* Получить первый свободный уникальный идентификатор игрока.
	 * @return - Уникальный идентификатор игрока.
	 *           0, если свободных идентификаторов игроков нет.
	*/
	private byte getFreePlayerID()
	{
		byte result = 0;
		for (byte i = 0; i < mPlayerIDsUsage.length; i++)
		{
			if (!mPlayerIDsUsage[i])
			{
				mPlayerIDsUsage[i] = true;
				result = (byte)(i + 1);
				break;
			}
		}
		return result;
	}
	
}
