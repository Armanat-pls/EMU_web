package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import emulator.EmulatorInterface;

import static emulator.EMU.int_to_bit;
import static emulator.core.Config.MEM;
import static emulator.EMU.*;


@WebServlet(name = "api.compilerExec", urlPatterns = "/compilerExec")
@MultipartConfig(
	fileSizeThreshold = 1024 * 1024 * 1, 	// 1 MB
	maxFileSize = 1024 * 1024 * 5, 			// 5 MB
	maxRequestSize = 1024 * 1024 * 10 		// 10 MB
)
public class compilerExec extends HttpServlet {
	HttpServletRequest request;
	HttpServletResponse response;
	EmulatorInterface api;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.request=req;
		this.response=res;
		this.api = new EmulatorInterface(request);
		String json = "";

		response.setContentType("application/json");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(json);
		printWriter.close();
	}
}
