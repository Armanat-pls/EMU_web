package emulator;

import static emulator.EMU.string_to_bit;

import java.util.BitSet;

import emulator.dataContainer.emuMEMcellContainer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class EmulatorInterface {
	private HttpServletRequest request;
	public boolean started;

	public EmulatorInterface(HttpServletRequest request){
		this.request = request;
		this.started = false;
		if (request.getSession(false) == null){
			initiateSession();
			this.started = true;
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
}
