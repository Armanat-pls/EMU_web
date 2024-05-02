package emulator.core;

import java.util.BitSet;
import static emulator.core.Config.*;

public class BRAIN{ //АЛУ

	private BitSet RO;	//Аккумулятор, на одну ячейку
	
	public BRAIN(){	//конструктор
		this.RO = new BitSet(CELL);
	}

	public void write_RO(BitSet data){
		this.RO = (BitSet)data.clone();
	}

	public BitSet get_RO(){		//получение регистра
		return (BitSet)this.RO.clone();
	}

	public String showRO(){		//вывод регистра
		return show_bitset(this.RO);
	}

	public void clearRO(){
		this.RO.clear();
	}
}