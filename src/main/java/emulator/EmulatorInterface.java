package emulator;

import static emulator.EMU.bit_to_string;
import static emulator.EMU.string_to_bit;

import java.util.BitSet;

import static emulator.core.Config.*;
import emulator.dataContainer.emuMEMcellContainer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class EmulatorInterface {
	private HttpServletRequest request;

	public EmulatorInterface(HttpServletRequest request){
		this.request = request;
		if (request.getSession(false) == null || request.getSession().getAttribute("emu") == null){
			initiateSession();
		}
	}

	private void initiateSession()
	{
		HttpSession session = request.getSession(true);
		EMU emu = new EMU();
		session.setAttribute("emu", emu);
	}
	public void endSession()
	{
		HttpSession sess = request.getSession(false);
		if (sess != null){
			sess.invalidate();
		}
	}

	private EMU getEMU()
	{
		return (EMU)request.getSession().getAttribute("emu");
	}
	private void setEMU(EMU emu)
	{
		request.getSession().setAttribute("emu", emu);
	}

	public String getConfig(){
		String s = "{";
		s += makeJSONentry("MEM", MEM, false);
		s += makeJSONentry("CELL", CELL, false);
		s += makeJSONentry("BMEM", BMEM, false);
		s += makeJSONentry("VER", VER, true);
		s += "}";
		return s;
	}


	public int getCANT()
	{
		EMU emu = getEMU();
		return emu.UU.CANT;
	}
	public void setCANT(int newCANT)
	{
		EMU emu = getEMU();
		emu.UU.CANT = newCANT;
		setEMU(emu);
	}

	public void setMemCellRAW(int addr, String input){
		EMU emu = getEMU();
		BitSet tmp = string_to_bit(input);
		emu.RAM.write_cell(addr, tmp);
		setEMU(emu);
	}
	public void setMemCellComm(int commandCode, int commandAddr){
		
	}
	public void setMemCellComm(String commandMnemonic, int commandAddr){
		
	}
	public void setMemCellData(int input){

	}
	public void setMemCellData(float input){
		
	}

	public String getMemCell(int addr){
		EMU emu = getEMU();
		BitSet memCell = emu.RAM.get_cell(addr);
		emuMEMcellContainer cell = new emuMEMcellContainer(memCell);
		return cell.toString();
	}

	public String getMemAll(){
		EMU emu = getEMU();
		String s = "{";
		s += makeJSONentry("CANT", emu.UU.CANT, false);
		s += makeJSONentry("RO", bit_to_string(emu.ALU.get_RO()), false);
		s += "\"RAM\": [";
		for (int i = 0; i < MEM; i++){
			s += "{";
			emuMEMcellContainer cell = new emuMEMcellContainer(emu.RAM.get_cell(i));
			s += makeJSONentry("clean", bit_to_string(cell.bits), false);
			s += makeJSONentry("comm_c", cell.commandCode, false);
			s += makeJSONentry("comm_addr", cell.commandAddr, false);
			s += makeJSONentry("comm_char", cell.commandMnemonic, false);
			s += makeJSONentry("data_int", cell.intValue, false);
			s += makeJSONentry("data_float", cell.floatValue, true);
			s += "}";
			if (i < MEM - 1) s += ",";
		}
		s += "]";
		s += "}";
		return s;
	}

	public static String makeJSONentry(String key, int value, boolean last){
		String s = "\"" + key + "\":" + value + "";
		if (!last) s+=",";
		return s;
	}

	public static String makeJSONentry(String key, float value, boolean last){
		String s = "\"" + key + "\":" + value + "";
		if (!last) s+=",";
		return s;
	}

	public static String makeJSONentry(String key, String value, boolean last){
		String s = "\"" + key + "\":\"" + value + "\"";
		if (!last) s+=",";
		return s;
	}

	public static String makeJSONone(String key, String value){
		String s = "{";
		s += makeJSONentry(key, value, true);
		s += "}";
		return s;
	}
}
