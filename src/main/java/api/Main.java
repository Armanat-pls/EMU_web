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
		String json;

		switch (request.getParameter("method")) {
			case "GETCONFIG":
				json = api.getConfig();
				break;
			case "GETSTATE":
				json = api.getMemAll();
				break;
			default:
				json = "";
				break;
		}
		response.setContentType("application/json");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(json);
		printWriter.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.request=req;
		this.response=res;
		this.api = new EmulatorInterface(request);
		String json;

		switch (request.getParameter("method")) {
			case "SETCANT":
				json = "SETCANT";
				break;
			case "SETMEMCELL":
				json = "SETMEMCELL";
				break;
			case "EXECONE":
				json = "EXECONE";
				break;
			case "EXECALL":
				json = "EXECALL";
				break;
			default:
				json = "nothing";
				break;
		}

		response.setContentType("application/json");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(json);
		printWriter.close();
	}
}