package ua.net.hj.cz.analytics;

import java.util.ArrayList;
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
		/**
		 * Описывает счетчик фигур в линии.
		 * Расчитывает вес линии.
		 */
		class LineCounter {
			private byte mLineLength;        // Потенциальная длина линии.
			private byte mFigureCount;       // Количество отслеживаемых фигур в линии.
			private ArrayList<Byte> mChains; // Цепочки цельных последовательностей отслеживаемый фигур.
			private byte mChainFigureCount;  // Длина текущей отслеживаемой цепочки.
			private double mWeight;          // Вес линии.
			
			public LineCounter()
			{
				mLineLength = 0;
				mFigureCount = 0;
				mChains = new ArrayList<>();
				mChainFigureCount = 0;
				mWeight = 0;
			}
			
			/**
			 * Выполняет следующий шаг рассчета линии.
			 * @param aCurrentCellValue - Значение текущей рассматриваемой ячейки.
			 * @param aAnalyticID - Идентификатор игрока, линии которого считаем.
			 * @param aWinLineLength - Длина победной линии.
			 * @return - Признак того, что позиция победная для данного игрока,
			 *           и можно дальше анализ не проводить.
			 */
			public boolean nextCountStep(byte aCurrentCellValue, byte aAnalyticID, byte aWinLineLength)
			{
				boolean result = false;
				if (aCurrentCellValue == 0)
				{
					mLineLength++;
					if (mChainFigureCount > 0)
					{
						mChains.add(mChainFigureCount);
						mChainFigureCount = 0;
					}
				}
				else if (aCurrentCellValue == aAnalyticID)
				{
					if (++mChainFigureCount < aWinLineLength)
					{
						mLineLength++;
						mFigureCount++;
					}
					else
					{
						mWeight = Double.POSITIVE_INFINITY;
						result = true;
					}
				}
				else
				{
					closeLine(aWinLineLength);
				}
				return result;
			}
			
			/**
			 * Закрывает линию (накапливает расчитанный вес линии).
			 * @param aWinLineLength - Длина победной линии.
			 */
			public void closeLine(byte aWinLineLength)
			{
				if (mLineLength >= aWinLineLength)
				{
					// Учитываем последнюю не закрытую цепочку.
					if (mChainFigureCount > 0)
					{
						mWeight += Math.pow(10, mChainFigureCount);
					}
					// Учитываем уже закрытые цепочки.
					for (Byte chainLength: mChains)
					{
						mWeight += Math.pow(10, chainLength);
					}
					// Учитываем количество неучтеных победных линий, которые могут поместиться в потенциальной линии.
					mWeight += Math.floor((mLineLength - mFigureCount - (mFigureCount == 0 ? 0 : 1)) / aWinLineLength);
				}
				mLineLength = 0;
				mFigureCount = 0;
				mChains = new ArrayList<>();
				mChainFigureCount = 0;
			}
			
			/**
			 * Получить накопленное значение веса линии.
			 * @return - Накопленное значение веса линии.
			 */
			public double getWeight()
			{
				return mWeight;
			}
			
		}
		
		double result = 0d;
		byte boardXSize = mBoard.getXSize();
		byte boardYSize = mBoard.getYSize();
		byte winLineLength = mRules.getWinLineLength();
		int diagonalsQuantityCodirectional = boardXSize + boardYSize - (winLineLength<<1) + 1;
		int diagonalsQuantityTotal = diagonalsQuantityCodirectional<<1;
		LineCounter[] columnCounters = new LineCounter[boardXSize];
		LineCounter[] diagonalCounters = new LineCounter[diagonalsQuantityTotal];
		for (int i = 0; i < boardXSize; i++)
		{
			columnCounters[i] = new LineCounter();
		}
		for (int i = 0; i < diagonalsQuantityTotal; i++)
		{
			diagonalCounters[i] = new LineCounter();
		}
		
		exit:
		for (byte y = 0; y < boardYSize; y++)
		{
			LineCounter rowCounter = new LineCounter();
			for (byte x = 0; x < boardXSize; x++)
			{
				int diagonalIndex;
				byte currentCellValue = mBoard.lookAt(x, y);
				// Обработаем строку.
				if (rowCounter.nextCountStep(currentCellValue, mAnalyticID, winLineLength))
				{
					result = rowCounter.getWeight();
					break exit;
				}
				// Обработаем колонки.
				if (columnCounters[x].nextCountStep(currentCellValue, mAnalyticID, winLineLength))
				{
					result = columnCounters[x].getWeight();
					break exit;
				}
				// Обработаем прямые диагонали.
				diagonalIndex = x - y + boardYSize - winLineLength;
				if (diagonalIndex >= 0 && diagonalIndex < diagonalsQuantityCodirectional)
				{
					if (diagonalCounters[diagonalIndex].nextCountStep(currentCellValue, mAnalyticID, winLineLength))
					{
						result = diagonalCounters[diagonalIndex].getWeight();
						break exit;
					}
				}
				// Обработаем обратные диагонали.
				diagonalIndex = x + y + boardXSize + boardYSize - 3*winLineLength + 2;
				if (diagonalIndex >= diagonalsQuantityCodirectional && diagonalIndex < diagonalsQuantityTotal)
				{
					if (diagonalCounters[diagonalIndex].nextCountStep(currentCellValue, mAnalyticID, winLineLength))
					{
						result = diagonalCounters[diagonalIndex].getWeight();
						break exit;
					}
				}
			}
			rowCounter.closeLine(winLineLength);
			result += rowCounter.getWeight();
		}
		if (Double.isFinite(result))
		{
			for (int i = 0; i < boardXSize; i++)
			{
				columnCounters[i].closeLine(winLineLength);
				result += columnCounters[i].getWeight();
			}
			for (int i = 0; i < diagonalsQuantityTotal; i++)
			{
				diagonalCounters[i].closeLine(winLineLength);
				result += diagonalCounters[i].getWeight();
			}
		}
		return result;
	}
	
}
