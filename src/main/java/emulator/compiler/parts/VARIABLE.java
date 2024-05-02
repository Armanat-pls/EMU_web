package emulator.compiler.parts;

import emulator.compiler.parts.enums.*;

public class VARIABLE{
	public VarTypes type;
	public String name;
	public int intVal;
	public float floatVal;
	public int address;

	public VARIABLE(TokenType type, String name, String Val, int addrCount){
		if (type == TokenType.numInt){
			this.type = VarTypes.intE;
			this.intVal = Integer.valueOf(Val);
			this.floatVal = 0.0f;
		}
		else if (type == TokenType.numFloat){
			this.type = VarTypes.floatE;
			this.floatVal = Float.valueOf(Val);
			this.intVal = 0;
		}
		else{
			this.type = VarTypes.NULLE;
			this.intVal = 0;
			this.floatVal = 0.0f;
		}
		this.name = name;
		this.address = addrCount;
	}
	public String toString(){
		String res = "";
		if (address < 10) res = "     00" + this.address + " | ";
		else if (address < 100) res = "     0" + this.address + " | ";
		else if (address < 1000) res = "     " + this.address + " | ";
		res += this.name + " |    ";
		res += this.type + " |    ";
		res += this.intVal + " | ";
		res += this.floatVal + " | ";
		return res;
	}
}