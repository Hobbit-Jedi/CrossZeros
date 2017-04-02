package ua.net.hj.cz.roles;

import java.util.ArrayList;
import java.util.HashMap;
import ua.net.hj.cz.core.Coordinates;
import ua.net.hj.cz.core.Move;
import ua.net.hj.cz.core.MoveResult;
import ua.net.hj.cz.roles.players.Player;

/**
 * Описывает судью, следящего за выполнением правил игры.
 * @author Hobbit Jedi
 */
public class Referee {
	/**
	 * По каждому игроку считает количество попыток сделать ошибочный ход.
	 * Если количество ошибок игрока превысит указанный в правилах предел, то игрок будет дисквалифицирован.
	 */
	private final HashMap<Byte, Integer> mPlayersErrorsCounters;
	
	/**
	 * Создает судью, который будет следить за выполнением правил игры.
	 */
	public Referee()
	{
		mPlayersErrorsCounters = new HashMap<>();
	}
	
	/**
	 * Ознакомиться с правилами.
	 * @param aRules - Правила, по которым будет вестись игра.
	 */
	public void checkOutRules(Rules aRules)
	{
		mPlayersErrorsCounters.clear();
	}
	
	/**
	 * Выполняет проверку хода игрока, и если ход корректный, то фиксирует его на доске.
	 * После чего проверяет результат этого хода.
	 * @param aPlayer - Игрок, который совершает ход.
	 * @param aMove   - Проверяемый ход.
	 * @param aBoard  - Доска, к которой будет применен ход.
	 * @param aRules - Правила, по которым ведется игра.
	 * @param aPlayers - Еще оставшиеся в игре игроки.
	 * @return - Результат выполнения хода.
	 *           Если ход недопустим, то возвращает null.
	 */
	public MoveResult commitMove(Player aPlayer, Move aMove, Board aBoard, Rules aRules, ArrayList<Player> aPlayers)
	{
		MoveResult result = null;
		byte currentPlayerID = aPlayer.getID();
		if (aMove != null)
		{
			// Проверим допустимость хода.
			Coordinates cellToMove = aMove.getCoordinates();
			if (aBoard.isCoordinateAtBoard(cellToMove))
			{
				if (aBoard.lookAt(cellToMove) == 0)
				{
					// Если ход допустим, то отметим его на доске.
					aBoard.setAt(cellToMove, aPlayer.getID());
					// Определим результат хода.
					if (aRules.isWin(aMove, aBoard))
					{
						result = MoveResult.WIN;
					}
					else if (aBoard.hasMoreSpace())
					{
						result = MoveResult.CONTINUE;
					}
					else
					{
						result = MoveResult.DEADLOCK;
					}
					System.out.println("Ход игрока принят: " + aMove);
				}
				else
				{
					System.out.println("Указанная игроком " + aPlayer + " клеточка (" + cellToMove + ") уже занята.");
				}
			}
			else
			{
				System.out.println("Игрок " + aPlayer + " некорректно указал координаты хода: " + cellToMove);
			}
		}
		else
		{
			if (!aBoard.hasMoreSpace())
			{
				result = MoveResult.DEADLOCK;
			}
			else
			{
				System.out.println("Игрок " + aPlayer + " не знает куда ему пойти.");
			}
		}
		byte numErrorsAllowed = aRules.getNumErrorsAllowed();
		if (result == null && numErrorsAllowed >= 0)
		{
			// Проверим не пора ли игрока дисквалифицировать.
			Integer errorsQuantity = mPlayersErrorsCounters.getOrDefault(currentPlayerID, 0);
			if (errorsQuantity < numErrorsAllowed)
			{
				errorsQuantity += 1;
				mPlayersErrorsCounters.put(currentPlayerID, errorsQuantity);
				System.out.println("Игрок " + aPlayer + " получает предупреждение: " + errorsQuantity + "/" + numErrorsAllowed + ".");
			}
			else
			{
				result = MoveResult.DISQUALIFICATION;
			}
		}
		return result;
	}
	
}
