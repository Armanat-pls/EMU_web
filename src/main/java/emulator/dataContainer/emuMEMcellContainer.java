package emulator.dataContainer;

import java.util.BitSet;
import static emulator.EMU.cut_com;
import static emulator.EMU.bit_to_float;
import static emulator.EMU.bit_to_int;
import static emulator.CMS.*;

public class emuMEMcellContainer {
	public BitSet bits;
	public int commandCode;
	public int commandAddr;
	public String commandMnemonic;
	public int intValue;
	public float floatValue;

	public emuMEMcellContainer(BitSet bits){
		this.bits = bits;
		if (CMSmap == null) initialiseCMS();
		decodeBits();
	}

	private void decodeBits(){

		BitSet[] coms = cut_com(this.bits);
		if (coms[0].isEmpty()) this.commandCode = 0;
		else this.commandCode = (int)coms[0].toLongArray()[0];
		if (coms[1].isEmpty()) this.commandAddr = 0;
		else this.commandAddr = (int)coms[1].toLongArray()[0];

		this.commandMnemonic = decoder(this.commandCode);

		this.intValue = bit_to_int(this.bits);
		this.floatValue = bit_to_float(this.bits);
	}
}
