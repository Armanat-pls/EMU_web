package emulator.compiler.parts;

public class LexicError{
	private String error;
	private TOKEN token;

	public LexicError(TOKEN token, String error){
		this.token = token;
		this.error = error;
	}

	public String toJSONentry(){
		String s = "{";
		s += "\"codeLine\":\""+token.codeLine+"\",";
		s += "\"token\":\""+token.tokenType + " : " + token.value +"\",";
		s += "\"error\":\""+error+"\"";
		s += "}";
		return s;
	}
}
