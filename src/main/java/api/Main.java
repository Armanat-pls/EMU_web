package api;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import emulator.EmulatorInterface;

@WebServlet(name = "api.Main", urlPatterns = "/main")
public class Main extends HttpServlet {
	HttpServletRequest request;
	HttpServletResponse response;
	EmulatorInterface api;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.request=req;
		this.response=res;
		this.api = new EmulatorInterface(request);
		String text;

		switch (request.getParameter("method")) {
			case "GETCONFIG":
				text = requestConfig();
				break;
			case "GETSTATE":
				text = requestState();
				break;
			default:
				text = "nothing";
				break;
		}
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(text);
		printWriter.close();
	}

	private String requestConfig(){
		return api.getConfig();
	}

	private String requestState(){
		return api.getMemAll();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.request=req;
		this.response=res;
		this.api = new EmulatorInterface(request);
		String text;
		switch (request.getParameter("method")) {
			case "SETCANT":
				text = "SETCANT";
				break;
			case "SETMEMCELL":
				text = "SETMEMCELL";
				break;
			case "EXECONE":
				text = "EXECONE";
				break;
			case "EXECALL":
				text = "EXECALL";
				break;
			default:
				text = "nothing";
				break;
		}
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(text);
		printWriter.close();
	}
}