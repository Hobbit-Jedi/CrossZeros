package ua.net.hj.cz.game;

/**
 * Описывает исключение, используемое для прерывания ввода пользователя.
 * @author Hobbit Jedi
 */
public class ScanExitException extends Exception {
	
	/**
	 * Создать исключение без описания.
	 */
	public ScanExitException()
	{
		super();
	}
	
	/**
	 * Создать исключение с описанием.
	 * @param aMsg - Текст, описывающий подробности исключения.
	 */
	public ScanExitException(String aMsg)
	{
		super(aMsg);
	}
}
