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


@WebServlet(name = "api.FileRAMfill", urlPatterns = "/FileRAMfill")
@MultipartConfig(
	fileSizeThreshold = 1024 * 1024 * 1, 	// 1 MB
	maxFileSize = 1024 * 1024 * 5, 			// 5 MB
	maxRequestSize = 1024 * 1024 * 10 		// 10 MB
)
public class FileRAMfill extends HttpServlet {
	HttpServletRequest request;
	HttpServletResponse response;
	EmulatorInterface api;

	private int lineCNT = 0;
	private int ignoredCNT = 0;

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.request=req;
		this.response=res;
		this.api = new EmulatorInterface(request);
		String json = "";
		boolean fail = false;
		ArrayList<String> lines = new ArrayList<String>();
		Part part = request.getParts().iterator().next();
		InputStream partStream = part.getInputStream();
		BufferedReader part_reader = new BufferedReader(new InputStreamReader(partStream));
		String line = part_reader.readLine();
		if (upLineCNT()){
			while (line != null){
				if (!isValidLine(line)){
					ignoredCNT++;
					line = bit_to_string(int_to_bit(0));
				}
				lines.add(line);
				line = part_reader.readLine();
			}
		};

		if (!fail) json = api.fillRAMfromFile(lines, ignoredCNT);
		response.setContentType("application/json");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(json);
		printWriter.close();
	}

	private boolean upLineCNT(){
		lineCNT++;
		return (lineCNT < MEM);
	}

	private boolean isValidLine(String line){
		if (!line.matches("^[01 ]+$")) return false;
		return true;
	}
}
