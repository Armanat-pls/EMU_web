package emulator.compiler.parts;

import emulator.compiler.parts.enums.*;

public class TOKEN{
	public TokenType tokenType;
	public String value;
	public int codeLine;
	public TOKEN(TokenType tokenType, String value, int codeLine){
		this.tokenType = tokenType;
		this.value = value;
		this.codeLine = codeLine;
	}
	public String toString(){
		String num = "";
		if (codeLine < 10) num = "00" + codeLine;
		else if (codeLine < 100) num = "0" + codeLine;
		else if (codeLine < 1000) num = "" + codeLine;
		return num + "| " + tokenType + " : " + value + "\n"; 
	}
}