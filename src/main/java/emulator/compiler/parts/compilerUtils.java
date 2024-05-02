package emulator.compiler.parts;

import java.util.ArrayList;

public class compilerUtils{

	compilerUtils(){};

	public ArrayList<String> printTokens(ArrayList<TOKEN> TableOfTokens){
		ArrayList<String> tokens = new ArrayList<String>();
		for (TOKEN token : TableOfTokens) {
			tokens.add(token.toString());
		}
		return tokens;
	}
	public ArrayList<String> printErrors(ArrayList<LexicError> LexicErrors){
		ArrayList<String> errors = new ArrayList<String>();
		if (LexicErrors.size() > 0){
			for (LexicError error : LexicErrors) {
				errors.add(error.toString());
			}
		}
		return errors;
	}
	public ArrayList<String> printVariables(ArrayList<VARIABLE> VariablesList){
		ArrayList<String> variables = new ArrayList<String>();
		for (VARIABLE var : VariablesList) {
			variables.add(var.toString());
		}
		return variables;
	}
	public ArrayList<String> printInstructions(ArrayList<INSTRUCTION> InstructionsList){
		ArrayList<String> instructions = new ArrayList<String>();
		for (INSTRUCTION instruction : InstructionsList) {
			instructions.add(instruction.toString());
		}
		return instructions;
	}

	public static VARIABLE getVarbyName(Infoblock ib, String name){
		for (VARIABLE var : ib.variablesList) {
			if (var.name.equals(name)) return var;
		}
		return new VARIABLE(null, "NULL", "NULL", 0);
	}
}