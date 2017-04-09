package ua.net.hj.cz;

import java.util.Scanner;
import ua.net.hj.cz.game.Game;

/**
 * Главный запускаемый класс программы.
 * @author Hobbit Jedi
 */
public class CrossZeros {
	private enum MainMenuEntries {
		REPEAT      ("R", "Повторить игру с предыдущими параметрами"),
		CLASSIC     ("C", "Играть по классическим правилам"),
		PARAMETRIZED("P", "Играть по заданным вручную правилам"),
		EXIT        ("E", "Выйти из программы");
		
		private final String mCommand; // Комманда, соответствующая пункту меню.
		private final String mText;    // Текст с описанием пункта меню.
		
		private MainMenuEntries(String aCommand, String aText)
		{
			mCommand = aCommand.toUpperCase();
			mText    = aText;
		}
		
		public String getCommand()
		{
			return mCommand;
		}
		
		public String getText()
		{
			return mText;
		}
		
		@Override
		public String toString()
		{
			StringBuilder result = new StringBuilder(mCommand);
			result.append(" - ");
			result.append(mText);
			return result.toString();
		}
		
	}
	
	/**
	 * Поток ввода данных от пользователя.
	 * И его инициализация.
	 */
	private static final Scanner SCANNER;
	static
	{
		SCANNER = new Scanner(System.in);
		SCANNER.useDelimiter("\\n");
	}
	
	/**
	 * Точка входа в программу.
	 * @param args - Параметры командной строки.
	 */
	public static void main(String[] args) {
		Game game = new Game();
		mainLoop:
		while (true) // Пока пользователь в меню не выберет выход из игры.
		{
			MainMenuEntries choosenEntry = mainMenu(game.isReadyToStart());
			switch (choosenEntry)
			{
				case CLASSIC:
					if (!game.initClassicRules())
					{
						// Если инициализация игры не состоялась, то выходим из программы.
						break mainLoop;
					}
					break;
				case PARAMETRIZED:
					if (!game.initCustomRules())
					{
						// Если инициализация игры не состоялась, то выходим из программы.
						break mainLoop;
					}
					break;
				case REPEAT:
					// Повторяем предыдущую игру.
					// Дополнительно ничего делать не нужно.
					break;
				case EXIT:
					break mainLoop;
				default:
					break;
			}
			if (!game.play())
			{
				// Если игра прервана, то выходим из программы.
				break;
			}
		}
		System.out.println();
		System.out.println("Всего доброго.");
		game.close();
		SCANNER.close();
	}
	
	/**
	 * Главное меню игры.
	 * Запрашивает у пользователя выбор варианта игры, либо предоставляет возможность выйти из игры.
	 * @param aIsPreviousRulesExist - Признак того, что игра уже проводилась, и можно отображать пункт меню,
	 *                                позволяющий повторить игру по тем же правилам, что и в предыдущей игре.
	 * @return - Выбранный пункт меню.
	 */
	private static MainMenuEntries mainMenu(boolean aIsPreviousRulesExist)
	{
		MainMenuEntries result = null;
		answerUnderstandable:
		while (true)
		{
			System.out.println();
			System.out.println("+------------------------------------+");
			System.out.println("| Игра Крестики-Нолики-Йорики-Дорики |");
			System.out.println("+------------------------------------+");
			if (aIsPreviousRulesExist)
			{
				System.out.println(MainMenuEntries.REPEAT);
			}
			System.out.println(MainMenuEntries.CLASSIC);
			System.out.println(MainMenuEntries.PARAMETRIZED);
			System.out.println(MainMenuEntries.EXIT);
			System.out.print("Сделайте выбор:");
			String answer = SCANNER.next().toUpperCase();
			System.out.println("DEBUG: answer = " + answer);
			for (MainMenuEntries entry: MainMenuEntries.values())
			{
				System.out.println("DEBUG: entry = " + entry);
				System.out.println("DEBUG: entry.getCommand() = " + entry.getCommand());
				System.out.println("DEBUG: answer.equals(entry.getCommand()) = " + answer.equals(entry.getCommand()));
				if ((aIsPreviousRulesExist || entry != MainMenuEntries.REPEAT) && answer.equals(entry.getCommand()))
				{
					result = entry;
					break answerUnderstandable;
				}
			}
		}
		return result;
	}
}
