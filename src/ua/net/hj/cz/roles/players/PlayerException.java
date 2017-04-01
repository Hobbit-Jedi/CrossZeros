package ua.net.hj.cz.roles.players;

/**
 * Описывает исключения работы с классами игроков.
 * @author Hobbit Jedi
 */
public class PlayerException extends Exception {
	
	/**
	 * Создать исключение без описания.
	 */
	public PlayerException()
	{
		super();
	}
	
	/**
	 * Создать исключение с описанием.
	 * @param aMsg - Текст, описывающий подробности исключения.
	 */
	public PlayerException(String aMsg)
	{
		super(aMsg);
	}
}
