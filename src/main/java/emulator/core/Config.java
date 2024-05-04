package emulator.core;

public class Config  {
	public static final int CELL = 32; //Размер ячейки в бит
	public static final int MEM = 256; // Количество ячеек памяти
	public static final int BMEM = 8; // Количество бит на адрес памяти
	public static final String VER = "2.0 Web";
	public static int MEMzeros = (int)Math.ceil(Math.log10(MEM + 1));
}