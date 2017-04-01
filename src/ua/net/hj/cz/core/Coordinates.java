package ua.net.hj.cz.core;

/**
 * Описывает координаты ячейки на игровой доске.
 * @author Hobbit Jedi
 */
public class Coordinates {
	private final byte mX; // x-координата.
	private final byte mY; // y-координата.
	
	/**
	 * Создать координаты.
	 * @param aX - x-коодината (числовая).
	 * @param aY - y-координата (числовая).
	 */
	public Coordinates(byte aX, byte aY)
	{
		mX = aX;
		mY = aY;
	}
	
	/**
	 * Получить x-координату в числовом виде.
	 * @return - X-координата в числовом виде.
	 */
	public byte getX()
	{
		return mX;
	}
	
	/**
	 * Получить y-координату в числовом виде.
	 * @return - Y-координата в числовом виде.
	 */
	public byte getY()
	{
		return mY;
	}
	
	/**
	 * Получить x-координату в буквенном виде.
	 * @return - X-координата в буквенном виде.
	 */
	public String getXAsString()
	{
		return indexToCoordinate(mX);
	}
	
	/**
	 * Получить y-координату в буквенном виде.
	 * @return - Y-координата в буквенном виде.
	 */
	public String getYAsString()
	{
		return indexToCoordinate(mY);
	}
	
	/**
	 * Получить строковое представление координат,
	 * @return - строковое представление координат.
	 */
	@Override
	public String toString()
	{
		return new StringBuilder(getXAsString()).append(",").append(mY).toString();
	}
	
	/**
	 * Проверить совпадают ли координаты текущего объекта с другим объектом координат.
	 * @param obj - Координаты, с которыми выполняем сравнение текущего объекта.
	 * @return - Признак того, что координаты указанного объекта координат совпадают с координатами текущего объектом координат.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Coordinates other = (Coordinates) obj;
		boolean result = (this.mX == other.mX) && (this.mY == other.mY);
		return result;
	}
	
	/**
	 * Вычислить хэш-код объекта.
	 * @return - хэш-код объекта.
	 */
	@Override
	public int hashCode()
	{
		int hash = (mY<<8) + mX;
		return hash;
	}
	
/**
	 * Преобразует числовую координату в буквенную (индекс в пользовательскую координату).
	 * Буквенная координата представляет собой 26-ричное представление числовой координаты.
	 * Например:
	 *	0 = a
	 *	1 = b
	 *	25 = z
	 *	26 = ba
	 *	90 = dm
	 *	298 = lm
	 * @param aX - Числовая координата-индекс.
	 * @return - Соответствующая переданному значению строковая координата.
	 *           Пустая строка, если передано отрицательное значение.
	 */
	public static String indexToCoordinate(byte aX)
	{
		StringBuilder result = new StringBuilder();
		if (aX >= 0)
		{
			int rest = aX;
			byte current;
			char coordinatePart;
			do {				
				current = (byte) (rest % 26);
				rest /= 26;
				coordinatePart = (char)('a' + current);
				result.insert(0, coordinatePart);
			} while (rest > 0);
		}
		return result.toString();
	}
	
	/**
	 * Преобразует буквенную координату в числовую координату-индекс.
	 * По своему действию обратна методу indexToCoordinate (см. его для получения подробностей).
	 * @param aCoordinate - Строкове пользовательское представление координаты.
	 * @return - Числовая координата-индекс, соответствующая переданному значению.
	 *           -1, если переданное значение не является строковым представлением координаты.
	 */
	public static byte coordinateToIndex(String aCoordinate)
	{
		byte result = (byte)-1;
		String clearCoordinate = aCoordinate.trim().toLowerCase();
		int length = clearCoordinate.length();
		if (length > 0)
		{
			result = 0;
			for (int i = 0; i < length; i++)
			{
				char current = clearCoordinate.charAt(i);
				if (current >= 'a' && current <= 'z')
				{
					result += (current - 'a') * Math.pow(26, length-1-i);
				}
				else
				{
					result = (byte)-1;
				}
			}
		}
		return result;
	}
	
	/**
	 * Метод для тестирования методов indexToCoordinate(...) и coordinateToIndex(...).
	 * @param aShowErrorsOnly - Признак того, что нужно выводить в консоль только сообщения об ошибках, а не весь лог тестирования.
	 */
	public static void test_indexToCoordinateAndReverse(boolean aShowErrorsOnly)
	{
		System.out.println("--> Start of Testing of indexToXCoordinate()");
		for (int i = -5; i <= Byte.MAX_VALUE; i++)
		{
			String xCoordinate = indexToCoordinate((byte)i);
			byte reverse = coordinateToIndex(xCoordinate);
			boolean isError = ((byte)i < 0 && reverse != -1) || ((byte)i >= 0 && (byte)i != reverse);
			if (isError || !aShowErrorsOnly)
			{
				System.out.println((isError ? "!!! ОШИБКА !!! " : "") + i + " => " + xCoordinate + " => " + reverse);
			}
		}
		System.out.println("<-- End of Testing of indexToXCoordinate()");
	}
	
}
