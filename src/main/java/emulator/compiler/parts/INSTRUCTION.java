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

	public String toJSONentry(){
		String s = "{";
		s += "\"type\":\""+type.toString()+"\",";
		s += "\"writeTo\":\""+writeTo+"\",";
		s += "\"operand1\":\""+operand1+"\",";
		s += "\"operator\":\""+operator+"\",";
		s += "\"operand2\":\""+operand2+"\",";
		s += "\"blockDeep\":\""+blockDeep+"\"";
		s += "}";
		return s;
	}
}
