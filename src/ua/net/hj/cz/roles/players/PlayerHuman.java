package ua.net.hj.cz.roles.players;

import java.util.Scanner;
import ua.net.hj.cz.core.ActionFigure;
import ua.net.hj.cz.core.Coordinates;
import ua.net.hj.cz.core.Move;
import ua.net.hj.cz.game.ScanExitException;
import ua.net.hj.cz.roles.Board;

/**
 * Описывает Игрока, которым управляет человек с консоли.
 * @author Hobbit Jedi
 */
public class PlayerHuman extends Player {
	private final Scanner SCANNER; // Поток ввода данных от пользователя.
	
	/**
	 * Создает игрока, назначая ему игровую фигуру.
	 * @param aName - Имя игрока.
	 * @param aPlayerID - Уникальный идентифиатор игрока.
	 */
	public PlayerHuman(String aName, byte aPlayerID)
	{
		super(aName, aPlayerID);
		SCANNER = new Scanner(System.in);
		SCANNER.useDelimiter("\\n");
	}

	/**
	 * Выполнить ход.
	 * @param aBoard - Слепок текущей ситуации на игровом поле.
	 * @param aActivePlayersSequence - Порядок, в котором ходят еще активные участвующие в игре игроки.
	 *                                 Массив содержит уникальные идентификаторы игроков.
	 * @param aFigure - Фигура, которой игрок должен сделать ход.
	 * @return - Ход, который собирается делать игрок.
	 *           null, если игрок не знает куда пойти.
	 * @throws ua.net.hj.cz.game.ScanExitException - Управляемые человеком игроки могут вызвать это исключение,
	 *                                               чтобы мгновенно прекратить игру.
	 */
	@Override
	public Move makeMove(Board aBoard, byte[] aActivePlayersSequence, ActionFigure aFigure) throws ScanExitException
	{
		System.out.println();
		System.out.println("Ходит игрок " + mName);
		Coordinates coordinates = scanCoordinates();
		return new Move(coordinates, this, aFigure);
	}
	
	private Coordinates scanCoordinates() throws ScanExitException
	{
		Coordinates result;
		String inputX;
		String inputY;
		final String scanPattern = "([Ee][Xx][Ii][Tt])|(\\s*[a-zA-Z]+[\\s|\\.|,]*[0-9]+\\s*)";
		
		while (true)
		{
			System.out.println();
			System.out.println("Варианты:");
			System.out.println("	- Координаты хода (x - буквенная координата, y - числовая координата) в виде \"xy\" или \"x y\" или \"x,y\" или \"x.y\".");
			System.out.println("	- \"exit\" для выхода из игры.");
			System.out.print("Ввведите:");
			while (true)
			{
				if (SCANNER.hasNext(scanPattern))
				{
					String scanInput;
					scanInput = SCANNER.next(scanPattern);
					if (!scanInput.toLowerCase().equals("exit"))
					{
						String[] parts;
						parts = scanInput.split("\\s*,\\s*");
						if (parts.length != 2)
						{
							parts = scanInput.split("\\s*\\.\\s*");
						}
						if (parts.length != 2)
						{
							parts = scanInput.split("\\s*\\s+\\s*");
						}
						if (parts.length != 2)
						{
							// Попробуем разделить строку на символы в начале и число в конце.
							parts = new String[2];
							if (scanInput.matches("[a-zA-Z]+[0-9]+")) // Эта проверка излишняя, но для надежности пусть будет.
							{
								for (int i = 0; i < scanInput.length(); i++)
								{
									if (scanInput.charAt(i) >= '0' && scanInput.charAt(i) <= '9')
									{
										parts[0] = scanInput.substring(0, i);
										parts[1] = scanInput.substring(i);
										break;
									}
								}
							}
						}
						if (parts[0] != null)
						{
							inputX = parts[0].trim();
						}
						else
						{
							inputX = "";
						}
						if (parts[1] != null)
						{
							inputY = parts[1].trim();
						}
						else
						{
							inputY = "";
						}
						break;
					}
					else
					{
						SCANNER.close();						
						throw new ScanExitException();
					}
				}
				else
				{
					SCANNER.next();
				}
			}
			byte x = Coordinates.coordinateToIndex(inputX);
			if (x >= 0)
			{
				byte y;
				try
				{
					y = new Byte(inputY);
				}
				catch (NumberFormatException e)
				{
					System.out.println("Некорректно указана y-координата.");
					continue;
				}
				result = new Coordinates(x, y);
				break;
			}
			else
			{
				System.out.println("Некорректно указана x-координата.");
			}
		}
		return result;
	}

}
