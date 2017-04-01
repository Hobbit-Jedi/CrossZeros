package ua.net.hj.cz.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import ua.net.hj.cz.core.ActionFigure;
import ua.net.hj.cz.core.Move;
import ua.net.hj.cz.core.MoveResult;
import ua.net.hj.cz.roles.Board;
import ua.net.hj.cz.roles.Referee;
import ua.net.hj.cz.roles.Rules;
import ua.net.hj.cz.roles.players.Player;
import ua.net.hj.cz.roles.players.PlayerException;
import ua.net.hj.cz.roles.players.PlayersFactory;

/**
 * Описывает сеанс одной игры.
 * @author Hobbit Jedi
 */
public class Game {
	enum PlayerType
	{
		HUMAN ("Человек"),
		RANDOM("Компьютер: Случайный стрелок"),
		CLEVER("Компьютер: Умный")
		;
		
		private final String mName; // Представление значения при выводе на экран.

		private PlayerType(String aName)
		{
			mName = aName;
		}
		
		@Override
		public String toString()
		{
			return mName;
		}
	}
	
	private final Scanner SCANNER;                            // Поток ввода данных от пользователя.
	private final PlayersFactory mPlayersFactory;             // Фабрика для создания игроков.
	private Rules mRules;                                     // Правила игры.
	private Player[] mInitialPlayersSequence;                 // Порядок хода игроков в начале игры.
	private HashMap<Byte, ActionFigure> mPlayerIDsFiguresMap; // Соответствие идентификаторов игроков и фигур, которыми они играют.
	
	public Game()
	{
		mRules = null;
		mInitialPlayersSequence = null;
		mPlayerIDsFiguresMap = null;
		SCANNER = new Scanner(System.in);
		SCANNER.useDelimiter("\\n");
		mPlayersFactory = new PlayersFactory();
	}
	
	/**
	 * Завершение игры.
	 * Должен вызываться перед удалением игры.
	 * Освобождает захваченные ресурсы.
	 */
	public void close()
	{
		SCANNER.close();
		mRules = null;
		mInitialPlayersSequence = null;
		mPlayerIDsFiguresMap = null;
		try
		{
			releasePlayers();
		}
		catch (PlayerException e)
		{
			// Ничего не делаем, т.к. игра и так готовится к удалению.
		}
	}
	
	/**
	 * Проверить готова ли игра к запуску?
	 * Проверяет наличие у игры правил и игроков.
	 * @return - Признак того, что правила игры определены.
	 */
	public boolean isReadyToStart()
	{
		return (mRules != null && mInitialPlayersSequence != null && mPlayerIDsFiguresMap != null);
	}
	
	/**
	 * Инициализировать игру стандартными правилами.
	 * @return - Признак того, что игра инициализирована и готова к запуску.
	 *           false, если инициализация игры прервана необходимостью выйти из игры.
	 */
	public boolean initClassicRules()
	{
		boolean result = true;
		try
		{
			mRules = new Rules();
			createPlayers(true);
		}
		catch (ScanExitException e)
		{
			result = false;
		}
		catch (PlayerException e)
		{
			String eMsg = e.getLocalizedMessage();
			System.out.println("Неудачная попытка создания игрока по причине: " + (eMsg != null ? eMsg : "Неизвестно."));
			System.out.println("Программа будет завершена.");
			result = false;
		}
		return result;
	}
	
	/**
	 * Инициализировать игру заданными вручную правилами.
	 * @return - Признак того, что игра инициализирована и готова к запуску.
	 *           false, если инициализация игры прервана необходимостью выйти из игры.
	 */
	public boolean initCustomRules()
	{
		boolean result = true;
		try
		{
			byte boardXSize       = scanByteBordered("Укажите ширину доски (3..100):", (byte)3, (byte)100);
			byte boardYSize       = scanByteBordered("Укажите высоту доски (3..100):", (byte)3, (byte)100);
			byte winLineLength    = scanByteBordered("Укажите длину линии для победы (3..100):", (byte)3, (byte)100);
			byte numErrorsAllowed = scanByteBordered("Укажите допустимое количество ошибочных ходов до дисквалификации (0..100, или \"-1\" - неограничено):", (byte)-1, (byte)100);
			byte numOfPlayers     = scanByteBordered("Укажите количество игроков (2..5):", (byte)2, (byte)5);
			mRules = new Rules(boardXSize, boardYSize, winLineLength, numErrorsAllowed, numOfPlayers);
			createPlayers(false);
		}
		catch (ScanExitException e)
		{
			result = false;
		}
		catch (PlayerException e)
		{
			String eMsg = e.getLocalizedMessage();
			System.out.println("Неудачная попытка создания игрока по причине: " + (eMsg != null ? eMsg : "Неизвестно."));
			System.out.println("Программа будет завершена.");
			result = false;
		}
		return result;
	}
	
	/**
	 * Провести сеанс игры.
	 * @return - Признак того, что игра завершена естественным путем.
	 *           false, если пользователь прервал игру требованием выйти из программы.
	 */
	public boolean play()
	{
		Move move;             // Текущий ход игрока.
		MoveResult moveResult; // Результат проверки хода игрока судьей.
		
		boolean result = true;
		try
		{
			// Создаем доску для игры и настроим ее.
			Board board = new Board(mRules.getBoardXSize(), mRules.getBoardYSize());
			for (Map.Entry<Byte, ActionFigure> entry: mPlayerIDsFiguresMap.entrySet())
			{
				board.setPlayerFigure(entry.getKey(), entry.getValue());
			}
			
			// Создаем судью и знакомим его с правилами.
			Referee referee = new Referee();
			referee.checkOutRules(mRules);
			
			// Создадим вспомогательные списки активных игроков и их идентификаторов.
			ArrayList<Player> players = new ArrayList<>();
			byte[] playersIDs = new byte[mRules.getNumOfPlayers()];
			for (int i = 0; i < mInitialPlayersSequence.length; i++)
			{
				Player player = mInitialPlayersSequence[i];
				players.add(player);
				playersIDs[i] = player.getID();
			}
			System.out.println();
			System.out.println("-----------------------------------------");
			System.out.println("Подготовка к игре...");
			for (Player player: players)
			{
				System.out.print("Игрок " + player + "(" + mPlayerIDsFiguresMap.get(player.getID()) + ") знакомится с правилами");
				player.checkOutRules(mRules, playersIDs);
				System.out.println(" - Готово!");
			}
			
			System.out.println();
			System.out.println("-----------------------------------------");
			System.out.println("Игра началась!");
			board.print();
			
			boolean gameOver = (players.size() < 2); // Если в игре меньше двух игроков, то играть смысла нет.
			while (!gameOver)
			{
				for (int i = 0; i < players.size(); i++)
				{
					Player player = players.get(i);
					ActionFigure playerFigure = mPlayerIDsFiguresMap.get(player.getID());
					if (playerFigure == null)
					{
						System.out.println();
						System.out.println("ОШИБКА!");
						System.out.println("Игроку " + player + " не назначена фигура.");
						System.out.println("Программа завершает свою работу...");
						return false;
					}
					do {			
						move = player.makeMove(new Board(board), playersIDs, playerFigure);
						System.out.println();
						System.out.println("Игрок " + move);
						moveResult = referee.commitMove(player, move, board, mRules, players);
					} while (moveResult == null); // Пока Игроку есть куда ходить, но он делает некорректные хода.
					board.print();
					switch (moveResult)
					{
						case WIN:
							gameOver = true;
							System.out.println();
							System.out.println("ВЫИГРАЛ Игрок " + player + "!!!");
							// Оповестим всех игроков о победе игрока.
							byte winnerID = player.getID();
							for (Player playerToNotify: mInitialPlayersSequence)
							{
								playerToNotify.winNotificationHandler(winnerID);
							}
							break;
						case DEADLOCK:
							gameOver = true;
							System.out.println();
							System.out.println("НИЧЬЯ!!!");
							// Оповестим всех игроков о ничье.
							for (Player playerToNotify: mInitialPlayersSequence)
							{
								playerToNotify.deadlockNotificationHandler();
							}
							break;
						case DISQUALIFICATION:
							players.remove(player);
							i--;
							System.out.println();
							System.out.println("ДИСКВАЛИФИЦИРОВАН Игрок " + player + "!!!");
							// Переформируем массив идентификаторов оставшихся игроков.
							playersIDs = new byte[players.size()];
							for (int j = 0; j < players.size(); j++)
							{
								playersIDs[j] = players.get(j).getID();
							}
							// Оповестим всех игроков о дисквалификации игрока.
							byte looserID = player.getID();
							for (Player playerToNotify: mInitialPlayersSequence)
							{
								playerToNotify.disqualificationNotificationHandler(looserID, playersIDs);
							}
							if (players.size() == 1)
							{
								Player winner = players.get(0);
								gameOver = true;
								System.out.println();
								System.out.println("ВЫИГРАЛ Игрок " + winner + "!!!");
								// Оповестим всех игроков о победе игрока.
								winnerID = winner.getID();
								for (Player playerToNotify: mInitialPlayersSequence)
								{
									playerToNotify.winNotificationHandler(winnerID);
								}
							}
							break;
					}
					if (gameOver) break;
				}
			}
		}
		catch (ScanExitException e)
		{
			result = false;
		}
		return result;
	}
	
	private void createPlayers(boolean aIsRulesClassic) throws ScanExitException, PlayerException
	{
		releasePlayers();
		mInitialPlayersSequence = new Player[mRules.getNumOfPlayers()];
		mPlayerIDsFiguresMap = new HashMap();
		ActionFigure[] figures = ActionFigure.values();
		for (int i = 0; i < mInitialPlayersSequence.length; i++)
		{
			System.out.println();
			System.out.println("-----------------------------------------");
			System.out.println("Создание игрока №" + (i+1) + (aIsRulesClassic ? " (" + figures[i] + ")" : "") + ":");
			mInitialPlayersSequence[i] = createPlayer();
			
			///////////////////////////////////
			// Определение фигуры, которой будет ходить игрок.
			ActionFigure figure;
			if (aIsRulesClassic)
			{
				figure = (figures[0] != null) ? figures[0] : figures[1];
			}
			else
			{
				byte figuresCount = 0;
				byte choose = -1;
				for (byte j = 1; j <= figures.length; j++)
				{
					if (figures[j-1] != null)
					{
						figuresCount++;
						choose = j;
					}
				}
				if (figuresCount > 1)
				{
					while (true)
					{
						System.out.println();
						System.out.println("Выберите фигуру, которой будет ходить игрок:");
						for (byte j = 0; j < figures.length; j++)
						{
							if (figures[j] != null)
							{
								System.out.println("	" + (j+1) + " - " + figures[j]);
							}
						}
						choose = scanByte("Сделайте выбор:");
						if (choose >=1 && choose <= figures.length && figures[choose-1] != null)
						{
							break;
						}
						else
						{
							System.out.println();
							System.out.println("Сделайте правильный выбор.");
						}
					}
				}
				figure = figures[choose-1];
			}

			System.out.println("Играет фигурой \"" + figure + "\".");
			figures[figure.ordinal()] = null;
			mPlayerIDsFiguresMap.put(mInitialPlayersSequence[i].getID(), figure);
		}
	}
	
	private Player createPlayer() throws ScanExitException, PlayerException
	{
		Player result;
		byte choose;
		String name;
		PlayerType playerType;
		PlayerType[] playerTypes = PlayerType.values();
		byte numOfPlayerTypes = (byte)playerTypes.length;
		
		///////////////////////////////////
		// Определение типа игрока.
		System.out.println();
		System.out.println("Выберите тип игрока:");
		for (int i = 0; i < numOfPlayerTypes; i++)
		{
			System.out.println("	" + (i+1) + " - " + playerTypes[i]);
		}
		choose = scanByteBordered("Сделайте выбор:", (byte)1, numOfPlayerTypes);
		playerType = playerTypes[choose-1];
		
		///////////////////////////////////
		// Определение имени игрока.
		System.out.println();
		System.out.print("Введите имя игрока:");
		name = SCANNER.next();
		
		///////////////////////////////////
		// Создание и возврат соответствующего объекта.
		switch (playerType)
		{
			case HUMAN:
				result = mPlayersFactory.createHumanPlayer(name);
				break;
			case RANDOM:
				result = mPlayersFactory.createRandomPlayer(name);
				break;
			case CLEVER:
				//TODO: Заменить на создание правильного типа игрока.
				//result = new PlayerInvincible(name);
				result = mPlayersFactory.createRandomPlayer(name);
				break;
			default:
				throw new UnknownError("Неизвестный выбранный тип игрока.");
		}
		System.out.println();
		System.out.println("Создан игрок " + result + " [" + playerType + "].");
		return result;
	}
	
	/**
	 * Запросить у пользователя ввод целого числа (размером в байт).
	 * @param aMsg - Сообщение с запросом пользователю на ввод числа.
	 * @return - Введенное пользователем число.
	 * @throws ua.net.hj.cz.game.ScanExitException - Если пользователь вместо числа,
	 *                                               набрал комманду "exit" (в любом регистре),
	 *                                               то вызывает данное исключение.
	 */
	private byte scanByte(String aMsg) throws ScanExitException
	{
		byte result = (byte)0;
		while (true)
		{
			System.out.println();
			System.out.println("Введите \"exit\" для выхода из игры.");
			System.out.print(aMsg);
			try
			{
				result = SCANNER.nextByte();
				break;
			}
			catch (InputMismatchException e)
			{
				String command = SCANNER.next(); // Забираем то, что пользователь ввел вместо байта.
				if (command.equalsIgnoreCase("exit"))
				{
					throw new ScanExitException();
				}
				System.out.println();
				String eMsg = e.getMessage();
				if (eMsg != null)
				{
					System.out.println("Введите числовое значение в указанном диапазоне.");
				}
				else
				{
					System.out.println("Введите числовое значение.");
				}
			}
		}
		return result;
	}
	
	/**
	 * Запросить у пользователя ввод целого числа из диапазона.
	 * @param aMsg - Сообщение с запросом пользователю на ввод числа.
	 * @param aMin - Нижняя граница допустимого диапазона ввода числа (включительно).
	 * @param aMax - Верхняя граница допустимого диапазона ввода числа (включительно).
	 * @return - Введенное пользователем число.
	 * @throws ua.net.hj.cz.game.ScanExitException - Если пользователь вместо числа,
	 *                                               набрал комманду "exit" (в любом регистре),
	 *                                               то вызывает данное исключение.
	 */
	private byte scanByteBordered(String aMsg, byte aMin, byte aMax) throws ScanExitException
	{
		byte result;
		while (true)
		{
			result = scanByte(aMsg);
			if (result >= aMin && result <= aMax)
			{
				break;
			}
			else
			{
				System.out.println();
				System.out.println("Введите числовое значение в указанном диапазоне.");
			}
		}
		return result;
	}
	
	/**
	 * Освободить фабрику от игроков.
	 */
	private void releasePlayers() throws PlayerException
	{
		if (mInitialPlayersSequence != null)
		{
			for (Player player: mInitialPlayersSequence)
			{
				mPlayersFactory.releasePlayer(player);
			}
		}
	}
}
