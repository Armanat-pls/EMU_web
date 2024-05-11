package emulator.compiler.core;

import java.util.ArrayList;
import emulator.compiler.parts.*;
import static emulator.compiler.parts.enums.*;
import static emulator.compiler.parts.compilerUtils.*;

public class SemanticAnalyser{
	private int addrCount;
	private TokenType lasTokenType;
	private TokenType curTokenType;
	private String lasTokenValue;

	private TokenType typeBuffer;
	private String nameBuffer;

	private boolean varExists = false;

	private ArrayList<InstrType> blockLayers;
	private boolean expectingElse;
	private TOKEN token;
	private int i;
	private String ErrorBuffer;

	public SemanticAnalyser(){
		addrCount = 0;
		lasTokenType = TokenType.EoI;
		curTokenType = TokenType.EoI;
		lasTokenValue = "";
		//typeBuffer;
		//nameBuffer;
		varExists = false;
		blockLayers = new ArrayList<InstrType>();
		expectingElse = false;
		i = -1;  //старт с -1, из-за повышения в методе
		ErrorBuffer = "";
	}

	private boolean getNextToken(ArrayList<TOKEN> TableOfTokens){
		if (i + 1 < TableOfTokens.size()){
			lasTokenValue = token.value;
			token = TableOfTokens.get(++i);
			lasTokenType = curTokenType;
			curTokenType = token.tokenType;
			return true;
		}
		else return false;
	}
	private String CheckOperand(VarTypes targetType, boolean CreateConstant, Infoblock ib){
		VARIABLE tmpVar;
		boolean checkType = true;
		if (targetType == VarTypes.NULLE) checkType = false;
		if (token.tokenType == TokenType.numInt){
			if (checkType && targetType != VarTypes.intE) return "Type mismatch, expected: " + targetType;
			if (CreateConstant && getVarbyName(ib, token.value).type == VarTypes.NULLE)
				ib.variablesList.add(new VARIABLE(TokenType.numInt, token.value, token.value, ++addrCount));
		}
		else if (token.tokenType == TokenType.numFloat){
			if (checkType && targetType != VarTypes.floatE) return "Type mismatch, expected: " + targetType;
			if (CreateConstant && getVarbyName(ib, token.value).type == VarTypes.NULLE)
				ib.variablesList.add(new VARIABLE(TokenType.numFloat, token.value, token.value, ++addrCount));
		}
		else if (token.tokenType == TokenType.varName){
			tmpVar = getVarbyName(ib, token.value);
			if (tmpVar.type == VarTypes.NULLE) return "Variable doesn't exist";
			if (checkType && tmpVar.type != targetType) return "Type mismatch, expected: " + targetType;
		}
		else return "Expected number or varName";
		return "";
	}

	public Infoblock CheckSemantic(Infoblock ib){
		token = new TOKEN(curTokenType, lasTokenValue, 0);
		while (i < ib.TableOfTokens.size()){
			varExists = false;
			if (!getNextToken(ib.TableOfTokens)) break;
			if (token.tokenType == TokenType.error){
				ib.errorrsList.add(new LexicError(token, "Lexic error"));
				break;
			}
			else if (token.tokenType == TokenType.type){ //сценарий создания переменной
				if (!(lasTokenType == TokenType.EoI || lasTokenType == TokenType.struct) || blockLayers.size() > 0){
					ib.errorrsList.add(new LexicError(token, "Unexpected place for type"));
					continue;
				}
				if (token.value.equals("int")) typeBuffer = TokenType.numInt;
				else if (token.value.equals("float")) typeBuffer = TokenType.numFloat;
				if (!getNextToken(ib.TableOfTokens)) break;
				if (token.tokenType != TokenType.varName){
					ib.errorrsList.add(new LexicError(token, "Expected varName"));
					continue;
				}
				for (VARIABLE var : ib.variablesList)
					if (var.name.equals(token.value)){
						varExists = true;
						break;
					}
				if (varExists){
					ib.errorrsList.add(new LexicError(token, "Variable already exists"));
					continue;
				}
				nameBuffer = token.value;
				if (!getNextToken(ib.TableOfTokens)) break;
				if (token.tokenType != TokenType.operator || !token.value.equals("=")){
					ib.errorrsList.add(new LexicError(token, "Expected '='"));
					continue;
				}

				if (!getNextToken(ib.TableOfTokens)) break;
				VarTypes tmp = VarTypes.NULLE;
				if (typeBuffer == TokenType.numInt) tmp = VarTypes.intE;
				else if (typeBuffer == TokenType.numFloat) tmp = VarTypes.floatE;
				ErrorBuffer = CheckOperand(tmp, false, ib);
				if (!ErrorBuffer.equals("")){
					ib.errorrsList.add(new LexicError(token, ErrorBuffer));
					continue;
				}
				if (token.tokenType == TokenType.varName){
					ib.variablesList.add(new VARIABLE(typeBuffer, nameBuffer, "0", ++addrCount));
					ib.instructionsList.add(new INSTRUCTION(InstrType.asign, nameBuffer, token.value, null, null, 0));
				}
				else
					ib.variablesList.add(new VARIABLE(typeBuffer, nameBuffer, token.value, ++addrCount));
			
				if (!getNextToken(ib.TableOfTokens)) break;
				if (token.tokenType != TokenType.EoI){
					ib.errorrsList.add(new LexicError(token, "Expected ;"));
					continue;
				}
			}
			else if (token.tokenType == TokenType.varName){ // сценарий проверки операции
				if (!(lasTokenType == TokenType.EoI || lasTokenType == TokenType.struct)){
					ib.errorrsList.add(new LexicError(token, "unexpected place for varName"));
					continue;
				}
				for (VARIABLE var : ib.variablesList)
					if (var.name.equals(token.value)){
						varExists = true;
						break;
					}
				if (!varExists){
					ib.errorrsList.add(new LexicError(token, "Variable doesn't exist"));
					continue;
				}
				String writeToBuffer = token.value;
				String operand1Buffer = "0";
				String operatorBuffer = "0";
				String operand2Buffer = "0";
				VarTypes targetType = getVarbyName(ib, token.value).type;
				if (!getNextToken(ib.TableOfTokens)) break;
				if (token.tokenType != TokenType.operator || !token.value.equals("=")){
					ib.errorrsList.add(new LexicError(token, "Expected '='"));
					continue;
				}        
				if (!getNextToken(ib.TableOfTokens)) break;
				ErrorBuffer = CheckOperand(targetType, true, ib);
				if (!ErrorBuffer.equals("")){
					ib.errorrsList.add(new LexicError(token, ErrorBuffer));
					continue;
				}
				operand1Buffer = token.value;
	
				if (!getNextToken(ib.TableOfTokens)) break;
				if (token.tokenType == TokenType.EoI){
					if (ib.errorrsList.size() == 0)
						ib.instructionsList.add(new INSTRUCTION(InstrType.asign, writeToBuffer, operand1Buffer, null, null, blockLayers.size()));
					continue;
				}

				if (token.tokenType != TokenType.operator || token.value.equals("=")){
					ib.errorrsList.add(new LexicError(token, "Expected arithmetic operator"));
					continue;
				}
				operatorBuffer = token.value;

				if (!getNextToken(ib.TableOfTokens)) break;
				ErrorBuffer = CheckOperand(targetType, true, ib);
				if (!ErrorBuffer.equals("")){
					ib.errorrsList.add(new LexicError(token, ErrorBuffer));
					continue;
				}
				operand2Buffer = token.value;
	
				if (!getNextToken(ib.TableOfTokens)) break;
				if (token.tokenType != TokenType.EoI){
					ib.errorrsList.add(new LexicError(token, "Expected End Of Instruction"));
					continue;
				}
				if (ib.errorrsList.size() == 0)
					ib.instructionsList.add(new INSTRUCTION(InstrType.ariph, writeToBuffer, operand1Buffer, operatorBuffer, operand2Buffer, blockLayers.size()));
			}
			else if (token.tokenType == TokenType.struct && token.value.equals("}")){ //сценарий закрытия блока
				int layer = blockLayers.size();
				if (!(layer > 0)){
					ib.errorrsList.add(new LexicError(token, "No block to close"));
					continue;
				}
				if (blockLayers.get(layer - 1) == InstrType.ifblock){
					if (i + 1 < ib.TableOfTokens.size()){
						TOKEN tmpToken = ib.TableOfTokens.get(i + 1);
						if (tmpToken.tokenType == TokenType.word && tmpToken.value.equals("else"))
							expectingElse = true;
					}
				}
				blockLayers.remove(layer - 1);
				ib.instructionsList.add(new INSTRUCTION(InstrType.endblock, blockLayers.size()));
			}
			else if (token.tokenType == TokenType.word){ // сценарий блока
				if (token.value.equals("else")){
					if (!expectingElse){
						ib.errorrsList.add(new LexicError(token, "Enexpected 'else'"));
						continue;
					}
					expectingElse = false;
					if (!getNextToken(ib.TableOfTokens)) break;
					if (token.tokenType != TokenType.struct || !token.value.equals("{")){
						ib.errorrsList.add(new LexicError(token, "Enexpected block opening"));
						continue;
					}
					ib.instructionsList.add(new INSTRUCTION(InstrType.elseblock, null, null, null, null, blockLayers.size()));
					blockLayers.add(InstrType.elseblock);
				}
				else {
					InstrType ItypeBuffer;
					String operand1Buffer = "0";
					String operatorBuffer = "0";
					String operand2Buffer = "0";
					if (token.value.equals("while")) ItypeBuffer = InstrType.whileblock;
					else if (token.value.equals("if")) ItypeBuffer = InstrType.ifblock;
					else {
						ib.errorrsList.add(new LexicError(token, "Enexpected word"));
						continue;
					}
					if (!getNextToken(ib.TableOfTokens)) break;
					if (token.tokenType != TokenType.struct || !token.value.equals("(")){
						ib.errorrsList.add(new LexicError(token, "Expected logic block opening"));
						continue;
					}
					if (!getNextToken(ib.TableOfTokens)) break;
					ErrorBuffer = CheckOperand(VarTypes.NULLE, true, ib); // первый запуск без проверки типа
					if (!ErrorBuffer.equals("")){
						ib.errorrsList.add(new LexicError(token, ErrorBuffer));
						continue;
					}
					operand1Buffer = token.value;
					VarTypes tmpType = VarTypes.NULLE;
					if (token.tokenType == TokenType.numInt) tmpType = VarTypes.intE;
					else if (token.tokenType == TokenType.numFloat) tmpType = VarTypes.floatE;
					else if (token.tokenType == TokenType.varName) tmpType = getVarbyName(ib, token.value).type;

					if (!getNextToken(ib.TableOfTokens)) break;
					if (token.tokenType != TokenType.logic){
						ib.errorrsList.add(new LexicError(token, "Expected logic operator"));
						continue;
					}
					operatorBuffer = token.value;

					if (!getNextToken(ib.TableOfTokens)) break;
					ErrorBuffer = CheckOperand(tmpType, true, ib);
					if (!ErrorBuffer.equals("")){
						ib.errorrsList.add(new LexicError(token, ErrorBuffer));
						continue;
					}
					operand2Buffer = token.value;

					if (!getNextToken(ib.TableOfTokens)) break;
					if (token.tokenType != TokenType.struct || !token.value.equals(")")){
						ib.errorrsList.add(new LexicError(token, "Expected logic block closing"));
						continue;
					}
					if (!getNextToken(ib.TableOfTokens)) break;
					if (token.tokenType != TokenType.struct || !token.value.equals("{")){
						ib.errorrsList.add(new LexicError(token, "Expected block opening"));
						continue;
					}
					if (ib.errorrsList.size() == 0)
						ib.instructionsList.add(new INSTRUCTION(ItypeBuffer, null, operand1Buffer, operatorBuffer, operand2Buffer, blockLayers.size()));
					blockLayers.add(ItypeBuffer);

				}
			}
		}
		ib.instructionsList.add(new INSTRUCTION(InstrType.endprog, 0));
		return ib;
	}
}