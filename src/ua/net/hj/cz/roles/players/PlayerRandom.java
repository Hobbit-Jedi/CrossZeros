package ua.net.hj.cz.roles.players;

import java.util.Random;
import ua.net.hj.cz.core.ActionFigure;
import ua.net.hj.cz.core.Coordinates;
import ua.net.hj.cz.core.Move;
import ua.net.hj.cz.roles.Board;

/**
 * Описывает Игрока, который ходит, в основном, случайным образом.
 * @author Hobbit Jedi
 */
public class PlayerRandom extends Player {
	private final Random mRandom;                      // Генератор случайных чисел.
	private static final int NUM_OF_RANDOM_TRIES = 10; // Количество попыток походить случайно, после которого ходим в первую свободную ячейку.
	
	/**
	 * Создает игрока.
	 * @param aName - Имя игрока.
	 * @param aPlayerID - Уникальный идентифиатор игрока.
	 */
	PlayerRandom(String aName, byte aPlayerID)
	{
		super(aName, aPlayerID);
		mRandom = new Random();
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
	@Override
	public Move makeMove(Board aBoard, byte[] aActivePlayersSequence, ActionFigure aFigure)
	{
		final byte boardXSize = aBoard.getXSize();
		final byte boardYSize = aBoard.getYSize();
		Move result = null;
		Coordinates emptyCell = aBoard.searchFirstEmpty();
		if (emptyCell != null)
		{
			byte x;
			byte y;
			boolean found = false;
			int randomTriesCounter = NUM_OF_RANDOM_TRIES;
			do {				
				x = (byte)mRandom.nextInt(boardXSize);
				y = (byte)mRandom.nextInt(boardYSize);
				found = (aBoard.lookAt(x, y) == 0);
			} while (!found && --randomTriesCounter >= 0);
			if (found)
			{
				result = new Move(x, y, this, aFigure);
			}
			else
			{
				// Если испробовали все попытки попасть в пустую клетку случайно,
				// то ходим в первую свободную.
				result = new Move(emptyCell, this, aFigure);
			}
		}
		return result;
	}
}
