package emulator.core;

import java.util.BitSet;
import static emulator.core.Config.*;

public class BRAIN{ //АЛУ

	private BitSet RO;	//Аккумулятор
	
	public BRAIN(){
		this.RO = new BitSet(CELL);
	}

	public void write_RO(BitSet data){
		this.RO = (BitSet)data.clone();
	}

	public BitSet get_RO(){
		return (BitSet)this.RO.clone();
	}

	public void clearRO(){
		this.RO.clear();
	}
}