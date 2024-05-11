package emulator.compiler.parts;

import java.util.ArrayList;
import java.util.BitSet;
public class Infoblock{
	public ArrayList<TOKEN> TableOfTokens;
	public ArrayList<VARIABLE> variablesList;
	public ArrayList<LexicError> errorrsList;
	public ArrayList<INSTRUCTION> instructionsList;
	private String compileErrorMessage;
	private boolean compileError;
	public BitSet[] memoryTable;
	
	public Infoblock(){
		TableOfTokens = new ArrayList<TOKEN>();
		variablesList = new ArrayList<VARIABLE>();
		errorrsList = new ArrayList<LexicError>();
		instructionsList = new ArrayList<INSTRUCTION>();
		compileErrorMessage = "";
		compileError = false;
	}
	
	public void setCompileError(String message){
		compileError = true;
		compileErrorMessage = message;
	}
	public String getCompileError(){
		return compileErrorMessage;
	}
	public boolean isCompileError(){
		return compileError;
	}
}