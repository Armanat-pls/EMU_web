package emulator.compiler.parts;

public class LexicError{
	private String error;
	private TOKEN token;

	public LexicError(TOKEN token, String error){
		this.token = token;
		this.error = error;
	}
	public String toString(){
		String log = "";
		log += token.toString() + "     |    ";
		log += "Error: " + error;
		return log;
	}
}
