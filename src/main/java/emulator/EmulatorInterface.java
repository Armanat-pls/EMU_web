package emulator;

import static emulator.EMU.*;

import java.util.ArrayList;
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


	public String getCANT()
	{
		EMU emu = getEMU();
		return makeJSONone("CANT", emu.UU.CANT);
	}
	public String setCANT(int newCANT)
	{
		EMU emu = getEMU();
		emu.UU.CANT = newCANT;
		setEMU(emu);
		return getCANT();
	}

	public String setMemCellRAW(int addr, String input, boolean toALU){
		EMU emu = getEMU();
		BitSet tmp = string_to_bit(input);
		if (toALU)
			emu.ALU.write_RO(tmp);
		else
			emu.RAM.write_cell(addr, tmp);
		setEMU(emu);
		return getMemCell(addr, toALU);
	}
	public String setMemCellComm(int addr, int commandCode, int commandAddr, boolean toALU){
		EMU emu = getEMU();
		BitSet tmp = make_one(commandCode, commandAddr);
		if (toALU)
			emu.ALU.write_RO(tmp);
		else
			emu.RAM.write_cell(addr, tmp);
		setEMU(emu);
		return getMemCell(addr, toALU);
	}

	public String setMemCellData(int addr, int input, boolean toALU){
		EMU emu = getEMU();
		BitSet tmp = int_to_bit(input);
		if (toALU)
			emu.ALU.write_RO(tmp);
		else
			emu.RAM.write_cell(addr, tmp);
		setEMU(emu);
		return getMemCell(addr, toALU);
	}
	public String setMemCellData(int addr, float input, boolean toALU){
		EMU emu = getEMU();
		BitSet tmp = float_to_bit(input);
		if (toALU)
			emu.ALU.write_RO(tmp);
		else
			emu.RAM.write_cell(addr, tmp);
		setEMU(emu);
		return getMemCell(addr, toALU);
	}

	public String getMemCell(int addr, boolean fromRO){
		EMU emu = getEMU();
		BitSet memCell;
		if (fromRO)
			memCell = emu.ALU.get_RO();
		else 
			memCell = emu.RAM.get_cell(addr);
		emuMEMcellContainer cell = new emuMEMcellContainer(memCell);
		return memCellToJSON(cell, addr, fromRO);
	}

	public String getMemAll(String message){
		EMU emu = getEMU();
		String s = "{";
		s += makeJSONentry("message", message, false);
		s += makeJSONentry("CANT", emu.UU.CANT, false);
		emuMEMcellContainer RO = new emuMEMcellContainer(emu.ALU.get_RO());
		s += "\"RO\": " +  memCellToJSON(RO, 0, true) + ",";
		s += "\"RAM\": [";
		for (int i = 0; i < MEM; i++){
			emuMEMcellContainer cell = new emuMEMcellContainer(emu.RAM.get_cell(i));
			s += memCellToJSON(cell, i, false);
			if (i < MEM - 1) s += ",";
		}
		s += "]";
		s += "}";
		return s;
	}

	public String clearMEM(){
		EMU emu = getEMU();
		emu.RAM.zero();
		emu.ALU.clearRO();
		setEMU(emu);
		return getMemAll("Память очищена");
	}

	public String execONE(){
		String finalMessage = "";
		EMU emu = getEMU();
		if(emu.UU.CANT == MEM - 1)		
			finalMessage = "Достигнута последняя ячейка";
		else{
			emu.UU.RC = emu.RAM.get_cell(emu.UU.CANT);
			if (emu.compute() == 666)
				finalMessage = "Встречена команда завершения";
		}
		if (emu.UU.CANT == MEM)
			emu.UU.CANT--;
		setEMU(emu);
		return getMemAll(finalMessage);
	}

	public String execALL(){
		String finalMessage = "";
		EMU emu = getEMU();
		while(true)
		{
			emu.UU.RC = emu.RAM.get_cell(emu.UU.CANT);
			if(emu.UU.CANT == MEM - 1){
				finalMessage = "Достигнута последняя ячейка";
				break;
			}
			if (emu.compute() == 666)
			{
				finalMessage = "Встречена команда завершения";
				break;
			}
		}
		if (emu.UU.CANT == MEM)
			emu.UU.CANT--;
		setEMU(emu);
		return getMemAll(finalMessage);
	}

	public String fillRAMfromFile(ArrayList<String> lines, int ignoredCNT){
		String finalMessage = "";
		EMU emu = getEMU();
		emu.RAM.zero();
		
		for (int i = 0; i < lines.size(); i++)
			emu.RAM.write_cell(i, string_to_bit(lines.get(i)));

		setEMU(emu);
		finalMessage = "Заполнено " + lines.size() + " ячеек";
		if (ignoredCNT > 0) finalMessage += " | Проигнорировано " + ignoredCNT + " ячеек";
		return getMemAll(finalMessage);
	}

	public String dumpRAM(){
		String dump = "";
		EMU emu = getEMU();
		for (int i = 0; i < MEM; i++){
			dump += bit_to_string(emu.RAM.get_cell(i));
			if (i != MEM - 1) dump += "\n";
		}
		return dump;
	}

	public static String makeJSONentry(String key, int value, boolean last){
		String s = "\"" + key + "\":" + value + "";
		if (!last) s+=",";
		return s;
	}

	public static String makeJSONentry(String key, float value, boolean last){
		String s = "";
		if (Float.isFinite(value))
			s += "\"" + key + "\":" + value + "";
		else
			s += "\"" + key + "\":\"" + value + "\"";
		 
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

	public static String makeJSONone(String key, int value){
		String s = "{";
		s += makeJSONentry(key, value, true);
		s += "}";
		return s;
	}

	private static String memCellToJSON(emuMEMcellContainer cell, int index, boolean RO){
		String s = "{";
		if (RO)
			s += makeJSONentry("index", "RO", false);
		else
			s += makeJSONentry("index", index, false);
		s += makeJSONentry("clean", bit_to_string(cell.bits), false);
		s += makeJSONentry("comm_c", cell.commandCode, false);
		s += makeJSONentry("comm_addr", cell.commandAddr, false);
		s += makeJSONentry("comm_char", cell.commandMnemonic, false);
		s += makeJSONentry("data_int", cell.intValue, false);
		s += makeJSONentry("data_float", cell.floatValue, true);
		s += "}";
		return s;
	}
}
