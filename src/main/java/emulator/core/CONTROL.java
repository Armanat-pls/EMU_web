package emulator.core;

import java.util.BitSet;
import static emulator.core.Config.*;

public class CONTROL{
	public int CANT;
	public BitSet RC;

	public CONTROL(){
		this.CANT = 0;
		this.RC = new BitSet(CELL);
		for(int i = 0; i < CELL; i++)
			this.RC.clear(i);  //явное выставление длинны CELL и заполнение нулями
	}
}