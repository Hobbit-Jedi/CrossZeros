package ua.net.hj.cz.roles;

//TODO: Убрать мусор.
//import java.util.Arrays;
import ua.net.hj.cz.core.Move;
//import ua.net.hj.cz.roles.players.PlayerReadOnly;

/**
 * Описывает правила, по которым ведется игра.
 * @author Hobbit Jedi
 */
public class Rules {
	private final byte mBoardXSize;                      // Ширина доски.
	private final byte mBoardYSize;                      // Высота доски.
	private final byte mWinLineLength;                   // Длина линии из одинаковых фигур для победы.
	private final byte mNumErrorsAllowed;                // Допустимое количество ошибок до дисквалификации.
	private final byte mNumOfPlayers;                    // Количество играющих игроков.
	//TODO: Убрать мусор.
	//private final PlayerReadOnly[] mInitialPlayersOrder; // Порядок хода игроков в начале игры.
	
	/**
	 * Создать правила по умолчанию.
	 * Классические крестики-нолики.
	 * Поле 3х3.
	 * Для победы нужно поставить три фигуры в ряд.
	 * Играют два игрока.
	 * Разрешено 10 попыток подряд некорректно походить.
	 */
	public Rules()
	{
		this((byte)3, (byte)3, (byte)3, (byte)10, (byte)2);
	}
	
	/**
	 * Создать правила игры.
	 * @param aBoardXSize - Ширина игровой доски.
	 * @param aBoardYSize - Высота игровой доски.
	 * @param aWinLineLength - Количество фигур в линии, чтобы одержать победу.
	 * @param aNumErrorsAllowed - Допустимое количество попыток подряд сделать некорректный ход до получения дисквалификации.
	 * @param aNumOfPlayers - Количество играющих игроков.
	 */
	public Rules(byte aBoardXSize, byte aBoardYSize, byte aWinLineLength, byte aNumErrorsAllowed, byte aNumOfPlayers)
	{
		mBoardXSize          = aBoardXSize;
		mBoardYSize          = aBoardYSize;
		mWinLineLength       = aWinLineLength;
		mNumErrorsAllowed    = aNumErrorsAllowed;
		mNumOfPlayers        = aNumOfPlayers;
		//TODO: Убрать мусор.
		//mInitialPlayersOrder = new PlayerReadOnly[aNumOfPlayers];
	}
	
	/**
	 * Скопировать правила игры.
	 * @param aRules - Правила игры с которых создается копия.
	 */
	public Rules(Rules aRules)
	{
		this(aRules.getBoardXSize(), aRules.getBoardYSize(), aRules.getWinLineLength(), aRules.getNumErrorsAllowed(), aRules.getNumOfPlayers());
		//TODO: Убрать мусор.
		//PlayerReadOnly[] rulesPlayers = aRules.getInitialPlayersOrder();
		//System.arraycopy(rulesPlayers, 0, mInitialPlayersOrder, 0, rulesPlayers.length);
	}
	
	/**
	 * Получить ширину игровой доски.
	 * @return - Ширина игровой доски.
	 */
	public byte getBoardXSize()
	{
		return mBoardXSize;
	}
	
	/**
	 * Получить высоту игровой доски.
	 * @return - Высота игровой доски.
	 */
	public byte getBoardYSize()
	{
		return mBoardYSize;
	}
	
	/**
	 * Получить длину линии для победы.
	 * @return - Длина линии из одинаковых фигур для победы.
	 */
	public byte getWinLineLength()
	{
		return mWinLineLength;
	}
	
	/**
	 * Получить допустимое количество попыток некорректно походить до того,
	 * как игрок будет дисквалифицирован.
	 * @return - Допустимое количество ошибок до дисквалификации.
	 */
	public byte getNumErrorsAllowed()
	{
		return mNumErrorsAllowed;
	}
	
	/**
	 * Получить количество игроков.
	 * @return - Количество игроков.
	 */
	public byte getNumOfPlayers()
	{
		return mNumOfPlayers;
	}
	
	//TODO: Убрать мусор.
	///**
	// * Получить массив с начальным порядком участвующих в игре игроков.
	// * @return - Ссылка на массив игроков.
	// */
	//public PlayerReadOnly[] getInitialPlayersOrder()
	//{
	//	return mInitialPlayersOrder;
	//}
	
	/**
	 * Проверить совпадают ли данные правила игры с другими правилами игры.
	 * @param obj - Правила игры, с которыми выполняется сравнение текущих правил игры.
	 * @return - Признак того, что указанные правила игры совпадают с текущими правилами игры.
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
		final Rules other = (Rules) obj;
		boolean result	= (this.mBoardXSize == other.mBoardXSize)
						&& (this.mBoardYSize == other.mBoardYSize)
						&& (this.mWinLineLength == other.mWinLineLength)
						&& (this.mNumErrorsAllowed == other.mNumErrorsAllowed)
						&& (this.mNumOfPlayers == other.mNumOfPlayers)
						//TODO: Убрать мусор.
						//&& (Arrays.deepEquals(this.mInitialPlayersOrder, other.mInitialPlayersOrder))
						;
		return result;
	}
	
	/**
	 * Вычислить хэш-код объекта.
	 * @return - хэш-код объекта.
	 */
	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 53 * hash + mBoardXSize;
		hash = 53 * hash + mBoardYSize;
		hash = 53 * hash + mWinLineLength;
		hash = 53 * hash + mNumErrorsAllowed;
		hash = 53 * hash + mNumOfPlayers;
		//TODO: Убрать мусор.
		//hash = 53 * hash + Arrays.deepHashCode(mInitialPlayersOrder);
		return hash;
	}
	
	/**
	 * Проверяет принес ли ход победу тому, кто ходил.
	 * @param aLastMove - Последний ход на доске.
	 * @param aBoard - Доска, на которой ведется игра.
	 * @return - Признак того, что последний ход оказался победным.
	 */
	public boolean isWin(Move aLastMove, Board aBoard)
	{
		boolean result;
		byte xSize = aBoard.getXSize();
		byte ySize = aBoard.getYSize();
		byte lastMoveX = aLastMove.getX();
		byte lastMoveY = aLastMove.getY();
		byte lastMovePlayerID = aLastMove.getPlayer().getID();
		
		// Посчитаем сколько соответствующих ходящему игроку фигур в разных направлениях от точки хода.
		byte directions[][] =	{	{0, 0, 0},
									{0, 1, 0},
									{0, 0, 0}
								};
		for (byte dx = -1; dx <= 1; dx++) // Направление движения по оси X (-1 - Влево, 0 - Стоим, +1 - Вправо)
		{
			for (byte dy = -1; dy <= 1; dy++) // Направление движения по оси Y (-1 - Вверх, 0 - Стоим, +1 - Вниз)
			{
				if (dx != 0 || dy != 0) // Центральную клетку не изменяем.
				{
					byte directionLength = mWinLineLength;
					switch (dx)
					{
						case -1:
							directionLength = (byte)Math.min(directionLength, lastMoveX);
							break;
						case 1:
							directionLength = (byte)Math.min(directionLength, Math.max(xSize - lastMoveX - 1, 0));
							break;
					}
					switch (dy)
					{
						case -1:
							directionLength = (byte)Math.min(directionLength, lastMoveY);
							break;
						case 1:
							directionLength = (byte)Math.min(directionLength, Math.max(ySize - lastMoveY - 1, 0));
							break;
					}
					for (byte k = 1; k <= directionLength; k++)
					{
						if (aBoard.lookAt((byte)(lastMoveX + dx * k), (byte)(lastMoveY + dy * k)) != lastMovePlayerID)
						{
							break;
						}
						directions[dy+1][dx+1]++;
					}
				}
			}
		}
		
		// Проверим есть ли выигрышные строки, колонки или диагонали.
		byte figuresInRow = 0;
		byte figuresInColumn = 0;
		byte figuresInDiagonal1 = 0;
		byte figuresInDiagonal2 = 0;
		for (byte i = 0; i <= 2; i++)
		{
			figuresInRow       += directions[1][i];
			figuresInColumn    += directions[i][1];
			figuresInDiagonal1 += directions[i][i];
			figuresInDiagonal2 += directions[i][2-i];
		}
		byte maxLine = (byte)Math.max(figuresInRow, figuresInColumn);
		maxLine = (byte)Math.max(maxLine, figuresInDiagonal1);
		maxLine = (byte)Math.max(maxLine, figuresInDiagonal2);
		result = (maxLine >= mWinLineLength);
		
		return result;
	}

}
