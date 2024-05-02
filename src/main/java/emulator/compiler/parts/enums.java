package emulator.compiler.parts;

public class enums {
	public static enum TokenType{
		type,
		numInt,
		numFloat,
		word,
		operator,
		logic,
		struct,
		varName,
		EoI,
		error
	}
	
	public static enum VarTypes{
		floatE,
		intE,
		NULLE
	}
	
	public static enum InstrType{
		ariph,
		asign,
		whileblock,
		ifblock,
		elseblock,
		endblock,
		endprog,
	}
	
	public static enum ReadingWhat{
		number,
		word,
		numFloat,
		operator,
		nothing
	}
}
