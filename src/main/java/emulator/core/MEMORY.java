package emulator.core;

import java.util.BitSet;
import static emulator.core.Config.*;

public class MEMORY{	//память

	private BitSet[] TABLE;

	public MEMORY(){
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
}