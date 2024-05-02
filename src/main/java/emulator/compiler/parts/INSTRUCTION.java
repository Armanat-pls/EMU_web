package emulator.compiler.parts;

import emulator.compiler.parts.enums.*;

public class INSTRUCTION{
	public InstrType type;
	public String writeTo;
	public String operand1;
	public String operator;
	public String operand2;
	public int blockDeep;

	public INSTRUCTION(InstrType type, String writeTo, String operand1, String operator, String operand2, int blockDeep){
		this.type = type;
		this.writeTo = writeTo;
		this.operand1 = operand1;
		this.operator = operator;
		this.operand2 = operand2;
		this.blockDeep = blockDeep;
	}
	public INSTRUCTION(InstrType type, int blockDeep){
		this.type = type;
		this.blockDeep = blockDeep;
	}
	public String toString(){
		String log = "";
		log += type.toString() + "  |  ";
		log += writeTo + "  |  ";
		log += operand1 + " ";
		log += operator  + " ";
		log += operand2 + "  |  ";
		log += blockDeep;
		return log;
	}
}
