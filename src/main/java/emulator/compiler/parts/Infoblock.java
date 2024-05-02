package emulator.compiler.parts;

import java.util.ArrayList;
import java.io.*;

public class Infoblock{
	public ArrayList<TOKEN> TableOfTokens;
	public ArrayList<VARIABLE> variablesList;
	public ArrayList<LexicError> errorrsList;
	public ArrayList<INSTRUCTION> instructionsList;
	public FileWriter writer;
	
	public Infoblock(){
		TableOfTokens = new ArrayList<TOKEN>();
		variablesList = new ArrayList<VARIABLE>();
		errorrsList = new ArrayList<LexicError>();
		instructionsList = new ArrayList<INSTRUCTION>();
	}
}