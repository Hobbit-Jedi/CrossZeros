package ua.net.hj.cz.roles.players;

/**
 * Описывает минимально доступный набор методов,
 * с помощью которых можно только получать данные от Игрока (без возможности их изменить).
 * @author Hobbit Jedi
 */
public interface PlayerReadOnly {

	/**
	 * Получить идентификатор игрока.
	 * @return - Идентификатор игрока.
	 */
	public byte getID();

	/**
	 * Получить имя игрока.
	 * @return - Имя игрока.
	 */
	public String getName();
	
}
