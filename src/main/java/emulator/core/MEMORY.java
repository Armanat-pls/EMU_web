package emulator.core;

import java.util.BitSet;
import static emulator.core.Config.*;

public class  MEMORY{	//память

	private BitSet[] TABLE; //массив ячеек памяти

	public MEMORY(){	//конструктор
		this.TABLE = new BitSet[MEM];
		for (int i = 0; i < MEM; i++)
			this.TABLE[i] = new BitSet(CELL);
	}

	public void write_cell(int addr, BitSet data){
		this.TABLE[addr] = (BitSet)data.clone();
	}
	public BitSet get_cell(int addr){
		return (BitSet)this.TABLE[addr].clone();
	}
	public void zero(){
		for (int i = 0; i < MEM; i++)
			this.TABLE[i].clear();
	}
	public String show_cell(int addr){  
		return show_bitset(this.TABLE[addr]);
	}

	//обработка строки и ввод в память
	public boolean file_RAMfill(int addr, String line){
		BitSet writeline = new BitSet(CELL);
		boolean success = true;
		line = line.replace(" ", "");
		if (line.length() < CELL) //удлиннение строки при необходимости
		{
			String temp = "";
			for (int i = 0; i < CELL - line.length(); i++)
				temp += "0";
			line = temp + line;
		}
		for (int i = 0; i < CELL; i++)
		{
			if (line.charAt(i) == '0')
				writeline.clear(CELL - 1 - i);
			else if (line.charAt(i) == '1')
				writeline.set(CELL - 1 - i);
			else 
			{
				writeline.clear();
				success = false;
			}
		}
		if (success)
			this.write_cell(addr, writeline);
		return success;
	}
}