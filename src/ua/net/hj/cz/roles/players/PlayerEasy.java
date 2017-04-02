package ua.net.hj.cz.roles.players;

import java.util.ArrayList;
import ua.net.hj.cz.analytics.GameTreeNode;
import ua.net.hj.cz.core.ActionFigure;
import ua.net.hj.cz.core.Coordinates;
import ua.net.hj.cz.core.Move;
import ua.net.hj.cz.game.ScanExitException;
import ua.net.hj.cz.roles.Board;
import ua.net.hj.cz.roles.Rules;

/**
 * Описывает игрока, с которым играть легко.
 * Он оценивает состояние доски после своего хода, и таким образом выбирает наиболее подходящий ход.
 * При этом не видит того, что на следующем ходу противник может выиграть.
 * @author Hobbit Jedi
 */
public class PlayerEasy extends Player {
	
	/**
	 * Создает игрока.
	 * @param aName - Имя игрока.
	 * @param aPlayerID - Уникальный идентифиатор игрока.
	 */
	PlayerEasy(String aName, byte aPlayerID)
	{
		super(aName, aPlayerID);
	}
	
	/**
	 * Выполнить ход.
	 * @param aBoard - Слепок текущей ситуации на игровом поле.
	 * @param aActivePlayersSequence - Порядок, в котором ходят еще активные участвующие в игре игроки.
	 *                                 Массив содержит уникальные идентификаторы игроков.
	 * @param aRules - Правила, по которым ведется игра.
	 * @param aFigure - Фигура, которой игрок должен сделать ход.
	 * @return - Ход, который собирается делать игрок.
	 *           null, если игрок не знает куда пойти.
	 * @throws ua.net.hj.cz.game.ScanExitException - Управляемые человеком игроки могут вызвать это исключение,
	 *                                               чтобы мгновенно прекратить игру.
	 */
	@Override
	public Move makeMove(Board aBoard, byte[] aActivePlayersSequence, Rules aRules, ActionFigure aFigure) throws ScanExitException
	{
		Move result = null;
		byte boardXSize = aBoard.getXSize();
		byte boardYSize = aBoard.getYSize();
		double maxWeight = Double.NEGATIVE_INFINITY;
		ArrayList<Coordinates> nextMoves = new ArrayList<>();
		for (byte y = 0; y < boardYSize; y++)
		{
			for (byte x = 0; x < boardXSize; x++)
			{
				if (aBoard.lookAt(x, y) == 0)
				{
					aBoard.setAt(x, y, mPlayerID);
					GameTreeNode nextGameState = new GameTreeNode(mPlayerID, aBoard, aRules);
					aBoard.setAt(x, y, (byte)0); // Вернем доску в исходное состояние.
					double currentWeight = nextGameState.getBoardWeigtht();
					if (maxWeight < currentWeight)
					{
						maxWeight = currentWeight;
						nextMoves.clear();
						nextMoves.add(new Coordinates(x, y));
					}
					else if (maxWeight == currentWeight)
					{
						nextMoves.add(new Coordinates(x, y));
					}
				}
			}
		}
		if (nextMoves.size() > 0)
		{
			int choose = (int)(Math.random() * nextMoves.size());
			result = new Move(nextMoves.get(choose), this, aFigure);
		}
		return result;
	}
	
}
