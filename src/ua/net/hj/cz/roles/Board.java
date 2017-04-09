package ua.net.hj.cz.roles;

import java.util.Arrays;
import java.util.HashMap;
import ua.net.hj.cz.core.ActionFigure;
import ua.net.hj.cz.core.Coordinates;
import ua.net.hj.cz.core.GraphicSymbolsSet;

/**
 * Описывает игровое поле.
 * @author Hobbit Jedi
 */
public class Board {
	private final byte mXSize;                                      // Горизонтальный размер игрового поля.
	private final byte mYSize;                                      // Вертикальный размер игрового поля.
	private final byte[] mField;                                    // Игровое поле (храним матрицу в одномерном массиве).
	private final HashMap<Byte, ActionFigure> mPlayerIDsFiguresMap; // Соответствие идентификаторов игроков и фигур, которыми они играют.
	
	/**
	 * Создает игровое поле указанных размеров.
	 * @param aXSize - Горизонтальный размер создаваемого игрового поля.
	 * @param aYSize - Вертикальный размер создаваемого игрового поля.
	 */
	public Board(byte aXSize, byte aYSize)
	{
		mXSize = aXSize;
		mYSize = aYSize;
		mField = new byte[mYSize * mXSize];
		Arrays.fill(mField, (byte)0);
		mPlayerIDsFiguresMap = new HashMap();
	}
	
	/**
	 * Создает копию игрового поля.
	 * @param aBoard - Игровое поле, копия которого создается.
	 */
	public Board(Board aBoard)
	{
		this(aBoard.mXSize, aBoard.mYSize);
		System.arraycopy(aBoard.mField, 0, this.mField, 0, mXSize * mYSize);
		mPlayerIDsFiguresMap.putAll(aBoard.mPlayerIDsFiguresMap);
	}
	
	/**
	 * Добавить соответствие игровой фигуры игроку.
	 * @param aPlayerID - Идентификатор игрока, которому в соответствие устанавливается фигура.
	 * @param aFigure - Игровая фигура, которая устанавливается в соответствие игроку.
	 */
	public void setPlayerFigure(byte aPlayerID, ActionFigure aFigure)
	{
		if (aPlayerID > 0)
		{
			mPlayerIDsFiguresMap.put(aPlayerID, aFigure);
		}
		else
		{
			throw new IllegalArgumentException("Передан некорректный идентификатор игрока (" + aPlayerID + ") в метод Board.setAt()");
		}
	}
	
	/**
	 * Получить горизонтальный размер игрового поля.
	 * @return - Горизонтальный размер игрового поля.
	 */
	public byte getXSize()
	{
		return mXSize;
	}
	
	/**
	 * Получить вертикальный размер игрового поля.
	 * @return - Вертикальный размер игрового поля.
	 */
	public byte getYSize()
	{
		return mYSize;
	}
	
	/**
	 * Определить попадает ли клеточка в игровое поле.
	 * @param aX - X-координата проверяемой клеточки поля.
	 * @param aY - Y-координата проверяемой клеточки поля.
	 * @return - Признак того, что клеточка с указанными координатами присутствует на игровом поле.
	 */
	public boolean isCoordinateAtBoard(byte aX, byte aY)
	{
		return (aX >= (byte)0 && aX < mXSize && aY >= (byte)0 && aY < mYSize);
	}
	
	/**
	 * Определить попадает ли клеточка в игровое поле.
	 * @param aCoordinates - Координаты проверяемой клеточки поля.
	 * @return - Признак того, что клеточка с указанными координатами присутствует на игровом поле.
	 */
	public boolean isCoordinateAtBoard(Coordinates aCoordinates)
	{
		return isCoordinateAtBoard(aCoordinates.getX(), aCoordinates.getY());
	}
	
	/**
	 * Посмотреть на игровое поле (получить значение клеточки по координатам).
	 * @param aX - X-координата клеточки, в которую смотрим.
	 * @param aY - Y-координата клеточки, в которую смотрим.
	 * @return - Идентификатор игрока, фигура которого находится на поле по указанным координатам.
	 *           0, если клеточка свободна.
	 * @throws IllegalArgumentException - Если координаты не попадают в поле, то вызывается исключение.
	 */
	public byte lookAt(byte aX, byte aY) throws IllegalArgumentException
	{
		if (isCoordinateAtBoard(aX, aY))
		{
			return mField[aY * mXSize + aX];
		}
		else
		{
			throw new IllegalArgumentException("Некорректно переданы координаты в метод Board.lookAt()");
		}
	}
	
	/**
	 * Посмотреть на игровое поле (получить значение клеточки по координатам).
	 * @param aCoordinates - Координаты клеточки, в которую смотрим.
	 * @return - Идентификатор игрока, фигура которого находится на поле по указанным координатам.
	 *           0, если клеточка свободна.
	 * @throws IllegalArgumentException - Если координаты не попадают в поле, то вызывается исключение.
	 */
	public byte lookAt(Coordinates aCoordinates) throws IllegalArgumentException
	{
		return lookAt(aCoordinates.getX(), aCoordinates.getY());
	}
	
	/**
	 * Установить фигуру на игровом поле.
	 * !!!ВНИМАНИЕ!!! Затирает расположенную в указанных координатах старую фигуру.
	 * @param aX - X-координата клеточки, в которой устанавливаем фигуру.
	 * @param aY - Y-координата клеточки, в которой устанавливаем фигуру.
	 * @param aPlayerID - Идентификатор игрока, фигуру которого устанавливаем в указанных координатах.
	 *                    0, если требуется очистить клеточку.
	 * @throws IllegalArgumentException - Если координаты выходят за пределы поля,
	 *                                    или указан неизвестный доске идентификатор игрока,
	 *                                    то вызывает исключение.
	 */
	public void setAt(byte aX, byte aY, byte aPlayerID) throws IllegalArgumentException
	{
		if (isCoordinateAtBoard(aX, aY))
		{
			if (aPlayerID == 0 || mPlayerIDsFiguresMap.get(aPlayerID) != null)
			{
				mField[aY * mXSize + aX] = aPlayerID;
			}
			else
			{
				throw new IllegalArgumentException("Передан некорректный идентификатор игрока (" + aPlayerID + ") в метод Board.setAt()");
			}
		}
		else
		{
			throw new IllegalArgumentException("Некорректно переданы координаты в метод Board.setAt()");
		}
	}
	
	/**
	 * Установить фигуру на игровом поле.
	 * !!!ВНИМАНИЕ!!! Затирает расположенную в указанных координатах старую фигуру.
	 * @param aCoordinates - Координаты клеточки, в которой устанавливаем фигуру.
	 * @param aPlayerID - Идентификатор игрока, фигуру которого устанавливаем в указанных координатах.
	 * @throws IllegalArgumentException - Если координаты выходят за пределы поля,
	 *                                    или указан неизвестный доске идентификатор игрока,
	 *                                    то вызывает исключение.
	 */
	public void setAt(Coordinates aCoordinates, byte aPlayerID) throws IllegalArgumentException
	{
		setAt(aCoordinates.getX(), aCoordinates.getY(), aPlayerID);
	}
	
	/**
	 * Найти первую попавшуюся свободную клеточку на поле.
	 * @return - Координаты найденной свободной клеточки.
	 */
	public Coordinates searchFirstEmpty()
	{
		Coordinates result = null;
		for (int i = 0; i < mXSize * mYSize; i++)
		{
			if (mField[i] == 0)
			{
				result = new Coordinates((byte)(i % mXSize), (byte)(i / mXSize));
				break;
			}
		}
		return result;
	}
	
	/**
	 * Проверить есть ли еще свободные клеточки?
	 * @return - Признак того, что свободные клеточки на поле еще есть.
	 */
	public boolean hasMoreSpace()
	{
		boolean result = (searchFirstEmpty() != null);
		return result;
	}
	
	/**
	 * Отображает игровое поле в консоли.
	 */
	public void print()
	{
		byte xCoordinateLength = (byte)Coordinates.indexToCoordinate((byte)(mXSize-1)).length();
		byte yCoordinateLength = (byte)(new StringBuilder().append(mYSize-1).length());
		// Преобразуем числовые X-индексы в буквенные координаты.
		String[] xCoordinates = new String[mXSize];
		for (byte x = 0; x < mXSize; x++)
		{
			xCoordinates[x] = Coordinates.indexToCoordinate(x);
		}
		StringBuilder divideLine = new StringBuilder();
		// Сформируем горизонтальную разделительную линию таблицы.
		divideLine.append(GraphicSymbolsSet.LINE_CROSS_LEFT);
		for (byte j = 0; j < yCoordinateLength; j++)
		{
			divideLine.append(GraphicSymbolsSet.LINE_HORIZONTAL);
		}
		for (byte x = 0; x < mXSize; x++)
		{
			divideLine.append(GraphicSymbolsSet.LINE_CROSS).append(GraphicSymbolsSet.LINE_HORIZONTAL);
		}
		divideLine.append(GraphicSymbolsSet.LINE_CROSS_RIGHT);
		// Начинаем вывод.
		System.out.println();
		// Выведем верхнюю рамку поля.
		System.out.print(GraphicSymbolsSet.LINE_TOP_LEFT_CORNER);
		for (byte j = 0; j < yCoordinateLength; j++)
		{
			System.out.print(GraphicSymbolsSet.LINE_HORIZONTAL);
		}
		for (byte x = 0; x < mXSize; x++)
		{
			System.out.print(GraphicSymbolsSet.LINE_CROSS_TOP);
			System.out.print(GraphicSymbolsSet.LINE_HORIZONTAL);
		}
		System.out.println(GraphicSymbolsSet.LINE_TOP_RIGHT_CORNER);
		// Выведем X-координаты.
		for (byte i = 0; i < xCoordinateLength; i++)
		{
			System.out.print(GraphicSymbolsSet.LINE_VERTICAL);
			for (byte j = 0; j < yCoordinateLength; j++)
			{
				System.out.print(GraphicSymbolsSet.SPACE_FULL_CELL);
			}
			for (byte x = 0; x < mXSize; x++)
			{
				byte xCoordCurrentLen = (byte)xCoordinates[x].length();
				System.out.print(GraphicSymbolsSet.LINE_VERTICAL);
				if (i >= xCoordinateLength - xCoordCurrentLen)
				{
					System.out.print(GraphicSymbolsSet.SPACE_AROUND_LETTER);
					System.out.print(xCoordinates[x].charAt(i - xCoordinateLength + xCoordCurrentLen));
					System.out.print(GraphicSymbolsSet.SPACE_AROUND_LETTER);
				}
				else
				{
					System.out.print(GraphicSymbolsSet.SPACE_FULL_CELL);
				}
			}
			System.out.println(GraphicSymbolsSet.LINE_VERTICAL);
		}
		// Выводим разделительную горизонтальную черту.
		System.out.println(divideLine);
		// Выводим само поле.
		for (byte y = 0; y < mYSize; y++)
		{
			System.out.print(GraphicSymbolsSet.LINE_VERTICAL);
			for (byte spaceCounter = 0; spaceCounter < yCoordinateLength; spaceCounter++)
			{
				System.out.print(GraphicSymbolsSet.SPACE_AROUND_LETTER);
			}
			System.out.format("%"+yCoordinateLength+"d", y); // Выводим Y-координату
			for (byte spaceCounter = 0; spaceCounter < yCoordinateLength; spaceCounter++)
			{
				System.out.print(GraphicSymbolsSet.SPACE_AROUND_LETTER);
			}
			for (byte x = 0; x < mXSize; x++)
			{
				byte currentCellValue = mField[y * mXSize + x];
				System.out.print(GraphicSymbolsSet.LINE_VERTICAL);
				System.out.print(currentCellValue != 0 ? mPlayerIDsFiguresMap.get(currentCellValue) : GraphicSymbolsSet.SPACE_FULL_CELL);
			}
			System.out.println(GraphicSymbolsSet.LINE_VERTICAL);
			if (y != mYSize - 1) // После последней строки разделительную линию не выводим.
			{
				System.out.println(divideLine);
			}
		}
		// Выведем нижнюю рамку поля.
		System.out.print(GraphicSymbolsSet.LINE_BOTTOM_LEFT_CORNER);
		for (byte j = 0; j < yCoordinateLength; j++)
		{
			System.out.print(GraphicSymbolsSet.LINE_HORIZONTAL);
		}
		for (byte x = 0; x < mXSize; x++)
		{
			System.out.print(GraphicSymbolsSet.LINE_CROSS_BOTTOM);
			System.out.print(GraphicSymbolsSet.LINE_HORIZONTAL);
		}
		System.out.println(GraphicSymbolsSet.LINE_BOTTOM_RIGHT_CORNER);
	}
	
}
