package emulator.compiler;

import java.util.ArrayList;
import java.util.BitSet;
import static emulator.core.Config.*;
import static emulator.compiler.parts.enums.*;
import static emulator.CMS.*;
import emulator.compiler.parts.*;
import static emulator.compiler.parts.compilerUtils.*;
import static emulator.EMU.*;
import java.io.IOException;

public class Translator{
	private ArrayList<String> errors;
	private ArrayList<BlockHead> blocks;
	private int MEMcount;
	private BitSet[] BitSets;
	private boolean checkIf;

	Translator(){
		blocks = new ArrayList<BlockHead>();
		errors = new ArrayList<String>();
		MEMcount = 0;
		BitSets = new BitSet[MEM];
		checkIf = false;
	}

	private class BlockHead{
		InstrType type;
		//поля для while и if-else
		int conditionPos;	//	Адрес ячейки с првоеркой условия 
		int jumpPos;	//	Адрес ячейки с прыжком через блок, если условие не выполнено

		//поля для if-else
		public int blockEndPos;	//	Адрес ячейки в конце блока, если нужно перепрыгнуть else. Заполняется по завершению блока.

		public BlockHead(InstrType type, int conditionPos, int jumpPos){
			this.type = type;
			this.conditionPos = conditionPos;
			this.jumpPos = jumpPos;
		}

	}
	private boolean MEMcntUP(){
		if (++MEMcount < MEM) return true;
		else{
			errors.add("Memory overload");
			return false;
		}
	}
	public ArrayList<String> Compile(Infoblock ib){

		if (ib.variablesList.size() + ib.instructionsList.size() >= MEM + 1){
			errors.add("Memory overload. varCount: " + ib.variablesList.size() + ", instrCount: " + ib.instructionsList.size());
			return errors;
		}
		else if(ib.instructionsList.size() >= MEM + 1){
			errors.add("Memory overload. instrCount: " + ib.instructionsList.size());
			return errors;
		}
		else if(ib.variablesList.size() >= MEM + 1){
			errors.add("Memory overload. varCount: " + ib.variablesList.size());
			return errors;
		}
		
		//заполнение данных
		for (VARIABLE var : ib.variablesList){
			if (!MEMcntUP()) break;
			if (var.type == VarTypes.intE)
				BitSets[MEMcount] = int_to_bit(var.intVal);
			else if (var.type == VarTypes.floatE)
				BitSets[MEMcount] = float_to_bit(var.floatVal);
			else errors.add("Type NULL, shouldn't be impossible");
		}
		if (!MEMcntUP()) return errors;
		BitSets[0] = make_one( CMSmap.get("JUMP"), MEMcount);

		for (INSTRUCTION instr : ib.instructionsList) {
			if (MEMcount >= MEM){
				errors.add("Memory overload");
				break;
			}
			if (checkIf && instr.type != InstrType.elseblock){
				MEMcount--;
				blocks.remove(blocks.size() - 1);
			}
			checkIf = false;
			if (instr.type == InstrType.asign){
				BitSets[MEMcount] = make_one(CMSmap.get("LOAD"), Integer.valueOf(getVarbyName(ib, instr.operand1).address));
				if (!MEMcntUP()) break;
				BitSets[MEMcount] = make_one(CMSmap.get("SAVE"), Integer.valueOf(getVarbyName(ib, instr.writeTo).address));
				if (!MEMcntUP()) break;
			}
			else if (instr.type == InstrType.ariph){
				BitSets[MEMcount] = make_one(CMSmap.get("LOAD"), Integer.valueOf(getVarbyName(ib, instr.operand1).address));
				if (!MEMcntUP()) break;
				String F = getVarbyName(ib, instr.writeTo).type == VarTypes.intE ? "" : "F";
				String COMM = "";
				if (instr.operator.equals("+"))
					COMM = "PLUS";
				else if (instr.operator.equals("-"))
					COMM = "MINUS";
				else if (instr.operator.equals("*"))
					COMM = "MULT";
				else if (instr.operator.equals("/"))
					COMM = "DIVIS";
				BitSets[MEMcount] = make_one(CMSmap.get(F + COMM), Integer.valueOf(getVarbyName(ib, instr.operand2).address));
				if (!MEMcntUP()) break;
				BitSets[MEMcount] = make_one(CMSmap.get("SAVE"), Integer.valueOf(getVarbyName(ib, instr.writeTo).address));
				if (!MEMcntUP()) break;
			}
			else if (instr.type == InstrType.whileblock || instr.type == InstrType.ifblock){
				int conditionPosBuffer;
				int jumpPosBuffer;

				conditionPosBuffer = MEMcount;  // На этот адрес нужно будет возвращатсья в конце while блока;
				BitSets[MEMcount] = make_one(CMSmap.get("LOAD"), Integer.valueOf(getVarbyName(ib, instr.operand1).address));
				if (!MEMcntUP()) break;
				String F = getVarbyName(ib, instr.operand1).type == VarTypes.intE ? "" : "F";
				String COMM = "";
				if (instr.operator.equals("<"))
					COMM = "LESS";
				else if (instr.operator.equals("<="))
					COMM = "LESSOE";
				else if (instr.operator.equals("=="))
					COMM = "EQUAL";
				else if (instr.operator.equals("!="))
					COMM = "NOTEQUAL";
				BitSets[MEMcount] = make_one(CMSmap.get(F + COMM), Integer.valueOf(getVarbyName(ib, instr.operand2).address));
				if (!MEMcntUP()) break;
				BitSets[MEMcount] = make_one(CMSmap.get("NOT"), 0);
				if (!MEMcntUP()) break;
				jumpPosBuffer = MEMcount;   //  По этому адресу нужно будет расположить команду команду прыжка через блок.
				if (!MEMcntUP()) break;
				blocks.add(new BlockHead(instr.type, conditionPosBuffer, jumpPosBuffer));                        
			}
			else if (instr.type == InstrType.elseblock){
				blocks.add(new BlockHead(instr.type, 0, 0));
			}
			else if (instr.type == InstrType.endblock){
				BlockHead lastBlockHead = blocks.get(blocks.size() - 1);
				if (lastBlockHead.type == InstrType.whileblock){
					BitSets[MEMcount] = make_one(CMSmap.get("JUMP"), lastBlockHead.conditionPos);
					if (!MEMcntUP()) break;
					BitSets[lastBlockHead.jumpPos] = make_one(CMSmap.get("IFJUMP"), MEMcount);
					blocks.remove(blocks.size() - 1);
				}
				else if (lastBlockHead.type == InstrType.ifblock){
					lastBlockHead.blockEndPos = MEMcount;
					blocks.set(blocks.size() - 1, lastBlockHead);
					if (!MEMcntUP()) break;
					BitSets[lastBlockHead.jumpPos] = make_one(CMSmap.get("IFJUMP"), MEMcount);
					// если за if не идёт else, этот блок будет удалён. Удаление происходит в начале цикла.
					checkIf = true;
				}
				else if (lastBlockHead.type == InstrType.elseblock){
					lastBlockHead = blocks.get(blocks.size() - 2);
					BitSets[lastBlockHead.blockEndPos] = make_one(CMSmap.get("JUMP"), MEMcount);
					blocks.remove(blocks.size() - 1);
					blocks.remove(blocks.size() - 1);
				}
			}
			else if (instr.type == InstrType.endprog){
				BitSets[MEMcount] = make_one(CMSmap.get("STOP"), 0);
				break;
			}
		}
		if (errors.size() == 0){
			try{
				for (int i = 0; i < MEM; i++)
					ib.writer.write(show_bitset(BitSets[i]) + "\n");
				ib.writer.close();
			}
			catch(IOException e){
				System.out.println(e);
			}
		}
		return errors;
	}
}