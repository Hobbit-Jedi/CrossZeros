package ua.net.hj.cz.analytics;

import java.util.Arrays;
import ua.net.hj.cz.roles.Board;
import ua.net.hj.cz.roles.Rules;

/**
 * Описывает узел дерева анализа игры.
 * @author Hobbit Jedi
 */
public class GameTreeNode {
	private final byte mAnalyticID;      // Идентификатор игрока, который анализирует игру.
	private final Board mBoard;          // Состояние доски на хранимый в узле момент игры.
	private final Rules mRules;          // Правила, по которым ведется игра.
	private final double mCurrentWeight; // Вес доски с точки зрения игрока, который анализирует игру.
	
	public GameTreeNode(byte aAnalyticID, Board aBoard, Rules aRules)
	{
		mAnalyticID = aAnalyticID;
		mBoard = new Board(aBoard); // Делаем "слепок" с преданной нам доски, чтобы не быть привязанным к оригиналу.
		mRules = aRules;
		mCurrentWeight = calculateBoardWeigtht();
	}
	
	/**
	 * Получить вес доски данного состояния игры с точки зрения игрока, который анализирует игру.
	 * @return - Весовой коэффициент данного состояния игры (чем выше - тем лучше, для анализирующего игру игрока).
	 */
	public double getBoardWeigtht()
	{
		return mCurrentWeight;
	}
	
	/**
	 * Рассчитать вес доски с точки зрения анализирующего игру игрока.
	 * @return - Весовой коэффициент данного состояния доски (чем выше - тем лучше, для анализирующего игру игрока).
	 */
	private double calculateBoardWeigtht()
	{
		double result = 0d;
		byte boardXSize = mBoard.getXSize();
		byte boardYSize = mBoard.getYSize();
		byte winLineLength = mRules.getWinLineLength();
		int diagonalsQuantityCodirectional = boardXSize + boardYSize - (winLineLength<<1) + 1;
		int diagonalsQuantityTotal = diagonalsQuantityCodirectional<<1;
		byte[] columnFigureCounters = new byte[boardXSize];
		byte[] columnLengthCounters = new byte[boardXSize];
		byte[] diagonalFigureCounters = new byte[diagonalsQuantityTotal];
		byte[] diagonalLengthCounters = new byte[diagonalsQuantityTotal];
		Arrays.fill(columnFigureCounters, (byte)0);
		Arrays.fill(columnLengthCounters, (byte)0);
		Arrays.fill(diagonalFigureCounters, (byte)0);
		Arrays.fill(diagonalLengthCounters, (byte)0);
		exit:
		for (byte y = 0; y < boardYSize; y++)
		{
			byte rowFigureCounter = 0;
			byte rowLengthCounter = 0;
			for (byte x = 0; x < boardXSize; x++)
			{
				int diagonalIndex;
				byte currentCellValue = mBoard.lookAt(x, y);
				// Обработаем строки и колонки.
				if (currentCellValue == 0)
				{
					rowLengthCounter++;
					columnLengthCounters[x]++;
				}
				else if (currentCellValue == mAnalyticID)
				{
					if (++rowFigureCounter >= winLineLength)
					{
						result = Double.POSITIVE_INFINITY;
						break exit;
					}
					if (++columnFigureCounters[x] >= winLineLength)
					{
						result = Double.POSITIVE_INFINITY;
						break exit;
					}
					rowLengthCounter++;
					columnLengthCounters[x]++;
				}
				else
				{
					if (rowLengthCounter >= winLineLength)
					{
						result += Math.pow(10, rowFigureCounter)
								+ Math.floor((rowLengthCounter - rowFigureCounter - 1) / winLineLength);
					}
					rowLengthCounter = 0;
					rowFigureCounter = 0;
					if (columnLengthCounters[x] >= winLineLength)
					{
						result += Math.pow(10, columnFigureCounters[x])
								+ Math.floor((columnLengthCounters[x] - columnFigureCounters[x] - 1) / winLineLength);
					}
					columnLengthCounters[x] = 0;
					columnFigureCounters[x] = 0;
				}
				// Обработаем прямые диагонали.
				diagonalIndex = x - y + boardYSize - winLineLength;
				if (diagonalIndex >= 0 && diagonalIndex < diagonalsQuantityCodirectional)
				{
					if (currentCellValue == 0)
					{
						diagonalLengthCounters[diagonalIndex]++;
					}
					else if (currentCellValue == mAnalyticID)
					{
						if (++diagonalFigureCounters[diagonalIndex] >= winLineLength)
						{
							result = Double.POSITIVE_INFINITY;
							break exit;
						}
						diagonalLengthCounters[diagonalIndex]++;
					}
					else
					{
						if (diagonalLengthCounters[diagonalIndex] >= winLineLength)
						{
							result += Math.pow(10, diagonalFigureCounters[diagonalIndex])
									+ Math.floor((diagonalLengthCounters[diagonalIndex] - diagonalFigureCounters[diagonalIndex] - 1) / winLineLength);
						}
						diagonalLengthCounters[diagonalIndex] = 0;
						diagonalFigureCounters[diagonalIndex] = 0;
					}
				}
				// Обработаем обратные диагонали.
				diagonalIndex = x + y + boardXSize + boardYSize - 3*winLineLength + 2;
				if (diagonalIndex >= diagonalsQuantityCodirectional && diagonalIndex < diagonalsQuantityTotal)
				{
					if (currentCellValue == 0)
					{
						diagonalLengthCounters[diagonalIndex]++;
					}
					else if (currentCellValue == mAnalyticID)
					{
						if (++diagonalFigureCounters[diagonalIndex] >= winLineLength)
						{
							result = Double.POSITIVE_INFINITY;
							break exit;
						}
						diagonalLengthCounters[diagonalIndex]++;
					}
					else
					{
						if (diagonalLengthCounters[diagonalIndex] >= winLineLength)
						{
							result += Math.pow(10, diagonalFigureCounters[diagonalIndex])
									+ Math.floor((diagonalLengthCounters[diagonalIndex] - diagonalFigureCounters[diagonalIndex] - 1) / winLineLength);
						}
						diagonalLengthCounters[diagonalIndex] = 0;
						diagonalFigureCounters[diagonalIndex] = 0;
					}
				}
			}
			if (rowLengthCounter >= winLineLength)
			{
				result += Math.pow(10, rowFigureCounter)
						+ Math.floor((rowLengthCounter - rowFigureCounter - 1) / winLineLength);
			}
		}
		for (int i = 0; i < boardXSize; i++)
		{
			if (columnLengthCounters[i] >= winLineLength)
			{
				result += Math.pow(10, columnFigureCounters[i])
						+ Math.floor((columnLengthCounters[i] - columnFigureCounters[i] - 1) / winLineLength);
			}
		}
		for (int i = 0; i < diagonalsQuantityTotal; i++)
		{
			if (diagonalLengthCounters[i] >= winLineLength)
			{
				result += Math.pow(10, diagonalFigureCounters[i])
						+ Math.floor((diagonalLengthCounters[i] - diagonalFigureCounters[i] - 1) / winLineLength);
			}
		}
		return result;
	}
	
}
