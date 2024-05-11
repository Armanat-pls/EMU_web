package emulator.compiler;

import java.io.BufferedReader;

import emulator.compiler.core.*;
import emulator.compiler.parts.*;

public class Compiler {
	private Lexer lexer;
	private SemanticAnalyser analyzer;
	private Translator translator;

	private Infoblock ib;

	private BufferedReader bufferedReader;

	public Compiler(BufferedReader bufferedReader){
		this.lexer = new Lexer();
		this.analyzer = new SemanticAnalyser();
		this.translator = new Translator();

		this.bufferedReader = bufferedReader;
	}

	public Infoblock compile(){
		this.ib = lexer.lexerAnalyse(bufferedReader);
		this.ib = analyzer.CheckSemantic(this.ib);
		if (ib.errorrsList.size() != 0) return this.ib;
		else return translator.Compile(this.ib);
	}
}
