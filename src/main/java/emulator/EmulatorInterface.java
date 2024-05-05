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

		int CANT = emu.UU.CANT;
		String RO = bit_to_string(emu.ALU.get_RO());

		emuMEMcellContainer[] MEMORY = new emuMEMcellContainer[MEM];
		String s = "";
		s += "СЧАК: " + CANT + "<br>";
		s += "Регистр: " + RO + "<br>";
		s += "Память: <br>";
		for (int i = 0; i < MEM; i++){
			MEMORY[i] = new emuMEMcellContainer(emu.RAM.get_cell(i));
			s += i + " | " + MEMORY[i].toString() + "<br>";
		}
		return s;
	}

	public String getConfig(){

		String result = "{";
		result += makeJSONentry("MEM", ""+MEM);
		result += makeJSONentry("CELL", ""+CELL);
		result += makeJSONentry("BMEM", ""+BMEM);
		result += makeJSONentry("VER", VER);
		result += makeJSONentry("SessionID", request.getSession().getId(), true);
		result += "}";
		return result;
	}

	private String makeJSONentry(String key, String value){
		String s = "\"" + key + "\":\"" + value + "\",";
		return s;
	}

	private String makeJSONentry(String key, String value, boolean last){
		String s = "\"" + key + "\":\"" + value + "\"";
		return s;
	}
}
