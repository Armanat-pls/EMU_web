package emulator.compiler.core;

import java.io.*;
import emulator.compiler.parts.*;
import static emulator.compiler.parts.enums.*;

public class Lexer{

	Infoblock ib;

	char specials[] = {'+','-','*','/','=','!','<','>','{','}','(',')',};
	String WORDS[] = {
		"if",
		"else",
		"while",
	};
	String TYPES[] = {
		"int",
		"float",
	};
	String STRUCTURE[] = {
		"{",
		"}",
		"(",
		")",
	};
	String OPERATORS[] = {
		"=",
		"+",
		"-",
		"*",
		"/",
	};
	String LOGIC[] = {
		"<",
		"<=",
		"!=",
		"=="
	};

	ReadingWhat state;
	int codeLine;

	public Lexer(){
		ib = new Infoblock();
		state = ReadingWhat.nothing;
		codeLine = 1;
	}

	boolean isEmpty(char c){
		if (c == ' ' || c == '\t') return true;
		if (c == '\n') {
			codeLine++;
			return true;
		}
		return false;
	}
	boolean isSpecial(char c){
		for (char item : specials) {
			if (c == item) return true;
		}
		return false;
	}
	public Infoblock lexerAnalyse(BufferedReader bufferedReader){
		int symbol;
		try{
			String buffer = "";
			char c;
			do{
				symbol = bufferedReader.read();
				if (symbol == -1){
					if (state != ReadingWhat.nothing) makeToken(buffer, false);
					break;
				};
				c = (char)symbol;
				if (Character.isAlphabetic(symbol) || c == '_'){
					if (state == ReadingWhat.nothing || state == ReadingWhat.word){
						state = ReadingWhat.word;
						buffer += c;
					}
					else if (state == ReadingWhat.operator){
						makeToken(buffer, false);
						state = ReadingWhat.word;
						buffer = "";
						buffer += c;
					}
					else{
						System.out.print("char in number");
						break;
					}
				}
				if (Character.isDigit(c)){
					if (state == ReadingWhat.nothing){
						state = ReadingWhat.number;
						buffer += c;
					}
					else if (state == ReadingWhat.operator){
						makeToken(buffer, false);
						state = ReadingWhat.number;
						buffer = "";
						buffer += c;
					}
					else if (state == ReadingWhat.word || state == ReadingWhat.number || state == ReadingWhat.numFloat){
						buffer += c;
					}
					continue;
				}
				if (c == '.' && state == ReadingWhat.number){
					state = ReadingWhat.numFloat;
					buffer += c;
					continue;
				}
				if (isSpecial(c)){
					if (state == ReadingWhat.nothing || state == ReadingWhat.operator){
						state = ReadingWhat.operator;
						buffer += c;
					}
					else{
						makeToken(buffer, false);
						state = ReadingWhat.operator;
						buffer = "";
						buffer += c;
					}
					continue;
				}
				if (isEmpty(c)){
					if (state != ReadingWhat.nothing){
						makeToken(buffer, false);
						state = ReadingWhat.nothing;
						buffer = "";
					}
					continue;
				}
				if (c == ';'){
					if (state != ReadingWhat.nothing){
						makeToken(buffer, true);
						state = ReadingWhat.nothing;
						buffer = "";
					}
					continue;
				} 
			}while(symbol != -1);
			bufferedReader.close();
		}
		catch(IOException e){
			System.out.print(e);
		}

		return ib;
	}
	private void makeToken(String buffer, boolean isEOI){
		if (state == ReadingWhat.number){
			ib.TableOfTokens.add(new TOKEN(TokenType.numInt, buffer, codeLine));
			if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
			return;
		}else if (state == ReadingWhat.numFloat){
			ib.TableOfTokens.add(new TOKEN(TokenType.numFloat, Float.valueOf(buffer).toString(), codeLine));
			if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
			return;
		}else if (state == ReadingWhat.word){
			for (String type : TYPES) {
				if (buffer.toLowerCase().equals(type)){
					ib.TableOfTokens.add(new TOKEN(TokenType.type, buffer, codeLine));
					if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
					return;
				}
			}
			for (String word : WORDS) {
				if (buffer.toLowerCase().equals(word)){
					ib.TableOfTokens.add(new TOKEN(TokenType.word, buffer, codeLine));
					if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
					return;
				}
			}
			ib.TableOfTokens.add(new TOKEN(TokenType.varName, buffer, codeLine));
			if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
				return;

		}else if (state == ReadingWhat.operator){
			for (String operator : OPERATORS) {
				if (buffer.equals(operator)){
					ib.TableOfTokens.add(new TOKEN(TokenType.operator, buffer, codeLine));
					if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
					return;
				}
			}
			for (String struct : STRUCTURE) {
				if (buffer.equals(struct)){
					ib.TableOfTokens.add(new TOKEN(TokenType.struct, buffer, codeLine));
					if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
					return;
				}
			}
			for (String logic : LOGIC) {
				if (buffer.equals(logic)){
					ib.TableOfTokens.add(new TOKEN(TokenType.logic, buffer, codeLine));
					if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
					return;
				}
			}
			ib.TableOfTokens.add(new TOKEN(TokenType.error, "unknown operator: " + buffer, codeLine));
			if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
			return;
		}else if (state == ReadingWhat.nothing){
			ib.TableOfTokens.add(new TOKEN(TokenType.error, "no state", codeLine));
			if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
			return;
		}
		if (buffer == "" || buffer == " "){
			ib.TableOfTokens.add(new TOKEN(TokenType.error, "no buffer", codeLine));
			if (isEOI) ib.TableOfTokens.add(new TOKEN(TokenType.EoI, "EoI", codeLine));
			return;
		}
	}
}