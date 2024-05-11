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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import emulator.EmulatorInterface;

import static emulator.EMU.int_to_bit;
import static emulator.core.Config.MEM;
import static emulator.EMU.*;

@WebServlet(name = "api.FileRAMget", urlPatterns = "/FileRAMget")
public class FileRAMget extends HttpServlet {
	HttpServletRequest request;
	HttpServletResponse response;
	EmulatorInterface api;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.request=req;
		this.response=res;
		this.api = new EmulatorInterface(request);
		String dump = api.dumpRAM();

		response.setContentType("text/plain");
		response.setHeader("Content-disposition", "attachment; filename=memory_dump.txt");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(dump);
		printWriter.close();
	}
}
