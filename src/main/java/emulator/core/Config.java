package emulator.core;

import java.util.BitSet;
import emulator.*;

public class Config  {
	public static final int CELL = 32; //Размер ячейки в бит
	public static final int MEM = 256; // Количество ячеек памяти
	public static final int BMEM = 8; // Количество бит на адрес памяти
	public static final String VER = "1.9.3 +Сompiler";
	public static final boolean comands = CMS.initialiseCMS(); // Инициализация словаря команд
	public static int MEMzeros = (int)Math.ceil(Math.log10(MEM + 1));

	public static String show_bitset(BitSet data)   //функция вывода набора битов в строку
	{
		String S = "";
		if (data == null) return S;
		for (int i = CELL - 1; i >= 0; i--)
		{
			if (i % 4 == 3)
				S += " ";
			if (data.get(i))
				S += "1";
			else
				S += "0";
		}
		return S;
	}
}