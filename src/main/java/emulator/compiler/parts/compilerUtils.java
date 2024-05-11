package emulator.compiler.parts;

import java.util.ArrayList;

public class compilerUtils{

	public static String printTokens(ArrayList<TOKEN> TableOfTokens){
		String s = "";
		for (TOKEN token : TableOfTokens) {
			s += token.toJSONentry();
			s += ",";
		}
		if (TableOfTokens.size() == 0) return "\"\"";
		return "[" + s.substring(0, s.length() - 1) + "]";
	}
	public static String printErrors(ArrayList<LexicError> LexicErrors){
		String s = "";
		for (LexicError lexicError : LexicErrors) {
			s += lexicError.toJSONentry();
			s += ",";
		}
		if (LexicErrors.size() == 0) return "\"\"";
		return "[" + s.substring(0, s.length() - 1) + "]";
	}
	public static String printVariables(ArrayList<VARIABLE> VariablesList){
		String s = "";
		for (VARIABLE variable : VariablesList) {
			s += variable.toJSONentry();
			s += ",";
		}
		if (VariablesList.size() == 0) return "\"\"";
		return "[" + s.substring(0, s.length() - 1) + "]";
	}
	public static String printInstructions(ArrayList<INSTRUCTION> InstructionsList){
		String s = "";
		for (INSTRUCTION instruction : InstructionsList) {
			s += instruction.toJSONentry();
			s += ",";
		}
		if (InstructionsList.size() == 0) return "\"\"";
		return "[" + s.substring(0, s.length() - 1) + "]";
	}

	public static VARIABLE getVarbyName(Infoblock ib, String name){
		for (VARIABLE var : ib.variablesList) {
			if (var.name.equals(name)) return var;
		}
		return new VARIABLE(null, "NULL", "NULL", 0);
	}
}