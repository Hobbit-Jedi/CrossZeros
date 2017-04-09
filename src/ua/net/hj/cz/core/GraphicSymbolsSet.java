package ua.net.hj.cz.core;

import java.nio.charset.Charset;

/**
 * Хранит набор символов для отображения элементов игры.
 * @author Hobbit Jedi
 */
public class GraphicSymbolsSet {
	public static final char FIGURE_CROSS;               // Символ, которым отображается фигура Крестик.
	public static final char FIGURE_ZERO;                // Символ, которым отображается фигура Нолик.
	public static final char FIGURE_STAR;                // Символ, которым отображается фигура Звезда.
	public static final char FIGURE_STOP;                // Символ, которым отображается фигура Стоп.
	public static final char FIGURE_CHECK;               // Символ, которым отображается фигура Галочка.
	public static final String LINE_TOP_LEFT_CORNER;     // Символ, которым отображается верхний левый угол таблиц.
	public static final String LINE_TOP_RIGHT_CORNER;    // Символ, которым отображается верхний правый угол таблиц.
	public static final String LINE_BOTTOM_LEFT_CORNER;  // Символ, которым отображается нижний левый угол таблиц.
	public static final String LINE_BOTTOM_RIGHT_CORNER; // Символ, которым отображается нижний правый угол таблиц.
	public static final String LINE_HORIZONTAL;          // Символ, которым отображается горизонтальная линия в таблицах.
	public static final String LINE_VERTICAL;            // Символ, которым отображается вертикальная линия в таблицах.
	public static final String LINE_CROSS;               // Символ, которым отображается пересечение горизонтальной и вертикальной линий в таблицах.
	public static final String LINE_CROSS_TOP;           // Символ, которым отображается пересечение верхней горизонтальной линии с вертикальной линией таблиц.
	public static final String LINE_CROSS_BOTTOM;        // Символ, которым отображается пересечение нижней горизонтальной линии с вертикальной линией таблиц.
	public static final String LINE_CROSS_LEFT;          // Символ, которым отображается пересечение горизонтальной линии с левой вертикальной линией таблиц.
	public static final String LINE_CROSS_RIGHT;         // Символ, которым отображается пересечение горизонтальной линии с правой вертикальной линией таблиц.
	public static final String SPACE_FULL_CELL;          // Специальный широкий пробел, который как раз по размеру в ячейку попадает.
	public static final String SPACE_AROUND_LETTER;      // Специальный узкий пробел, которым надо обрамить букву или цифру, чтобы они в ячейку вписались.
	static
	{
		if (Charset.defaultCharset().equals(Charset.forName("UTF-8")))
		{
			FIGURE_CROSS             = '\u2573'; // ╳
			FIGURE_ZERO              = '\u2B58'; // ⭘
			FIGURE_STAR              = '\u26E4'; // ⛤
			FIGURE_STOP              = '\u26D4'; // ⛔
			FIGURE_CHECK             = '\u2705'; // ✅
			LINE_TOP_LEFT_CORNER     = "\u250C"; // ┌
			LINE_TOP_RIGHT_CORNER    = "\u2510"; // ┐
			LINE_BOTTOM_LEFT_CORNER  = "\u2514"; // └
			LINE_BOTTOM_RIGHT_CORNER = "\u2518"; // ┘
			LINE_HORIZONTAL          = "\u2500"; // ─
			LINE_VERTICAL            = "\u2502"; // │
			LINE_CROSS               = "\u253C"; // ┼
			LINE_CROSS_TOP           = "\u252C"; // ┬
			LINE_CROSS_BOTTOM        = "\u2534"; // ┴
			LINE_CROSS_LEFT          = "\u251C"; // ├
			LINE_CROSS_RIGHT         = "\u2524"; // ┤
			SPACE_FULL_CELL          = "\u3000"; // Специальный широкий пробел, который как раз по размеру в ячейку попадает.
			SPACE_AROUND_LETTER      = "\u2006"; // Специальный узкий пробел, которым надо обрамить букву или цифру, чтобы они в ячейку вписались.
		}
		else
		{
			FIGURE_CROSS             = 'X';
			FIGURE_ZERO              = 'O';
			FIGURE_STAR              = '*';
			FIGURE_STOP              = '#';
			FIGURE_CHECK             = 'V';
			LINE_TOP_LEFT_CORNER     = "+";
			LINE_TOP_RIGHT_CORNER    = "+";
			LINE_BOTTOM_LEFT_CORNER  = "+";
			LINE_BOTTOM_RIGHT_CORNER = "+";
			LINE_HORIZONTAL          = "-";
			LINE_VERTICAL            = "|";
			LINE_CROSS               = "+";
			LINE_CROSS_TOP           = "+";
			LINE_CROSS_BOTTOM        = "+";
			LINE_CROSS_LEFT          = "+";
			LINE_CROSS_RIGHT         = "+";
			SPACE_FULL_CELL          = " ";
			SPACE_AROUND_LETTER      = ""; // Пустая строка.
		}
	}
}
