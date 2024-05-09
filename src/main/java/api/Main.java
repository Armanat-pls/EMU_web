package api;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import emulator.EmulatorInterface;
import static emulator.EmulatorInterface.*;
import static emulator.core.Config.*;
import static emulator.CMS.*;

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
		String method = request.getParameter("method");
		if (method != null)
		switch (method) {
			case "GETCONFIG":
				json = api.getConfig();
				break;
			case "GETSTATE":
				json = api.getMemAll();
				break;
			case "CLEARMEM":
				json = api.clearMEM();
				break;
			default:
				json = returnError("Метод не опознан");
				break;
		}
		else json = returnError("Не указан метод");
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
		String method = request.getParameter("method");
		if (method != null)
		switch (method) {
			case "SETCANT":
				json = "SETCANT";
				break;
			case "SETMEMCELL":
				json = handleSetMemCell(request);
				break;
			case "EXECONE":
				json = "EXECONE";
				break;
			case "EXECALL":
				json = "EXECALL";
				break;
			default:
				json = returnError("Метод не опознан");
				break;
		}
		else json = returnError("Не указан метод");
		response.setContentType("application/json");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(json);
		printWriter.close();
	}

	private String handleSetMemCell(HttpServletRequest req){
		String response = "";
		String type = request.getParameter("type");
		if (type == null) return returnError("Не указан тип ввода");

		String chosen_s = request.getParameter("chosen");
		if (chosen_s == null) return returnError("Не указана выбранная ячейка");
		//if (!validate_RAM_index(chosen_s)) return returnError("Некорректный индекс выбранной ячейки");
		if (!validate_RAM_index(chosen_s)) return returnError("Некорректный индекс выбранной ячейки");
		int chosen = Integer.parseInt(chosen_s);

		String toRO_s = request.getParameter("toRO");
		if (toRO_s == null) return returnError("Не указан флаг записи в регистр");
		boolean toRO = Boolean.parseBoolean(toRO_s);

		switch (type) {
			case "clean":
				String data = request.getParameter("data");
				if (data == null) return returnError("Не передан битовый набор");
				if (data.length() > CELL) return returnError("Слишком длинный битовый набор");
				if (!data.matches("^[01]+$")) return returnError("Посторонние символы в битовом наборе");
				response = api.setMemCellRAW(chosen, data, toRO);
				break;
			case "comm":
				String comm_c = request.getParameter("comm_c");
				if (comm_c == null) return returnError("Не передана команда");

				String comm_addr_s = request.getParameter("comm_addr");
				if (comm_addr_s == null) return returnError("Не указана ячейка операнда");
				if (!validate_RAM_index(comm_addr_s)) return returnError("Некорректный индекс ячейки операнда");
				int comm_addr = Integer.parseInt(comm_addr_s);

				int commandCode = decoder(comm_c);
				if (commandCode == 0){
					try {
						commandCode = Integer.parseInt(comm_c);
					} catch (NumberFormatException e) {
						return returnError("Некорректный код команды");
					}
				}
				response = api.setMemCellComm(chosen, commandCode, comm_addr, toRO);
				break;
			case "data":
				String dataType = request.getParameter("dataType");
				if (dataType == null) return returnError("Не передан тип данных");
				String data_i = request.getParameter("data");
				if (data_i == null) return returnError("Не передано число");
				switch (dataType) {
					case "int":
						int data_int;
						try {
							data_int = Integer.parseInt(data_i);
						} catch (Exception e) {
							return returnError("Некорректное целое число");
						}
						response = api.setMemCellData(chosen, data_int, toRO);
						break;
					case "float":
						float data_float;
						try {
							data_float = Float.parseFloat(data_i);
						} catch (Exception e) {
							return returnError("Некорректное число с плавающей запятой");
						}
						response = api.setMemCellData(chosen, data_float, toRO);
						break;
					default:
						return returnError("Неопознанный тип данных");
				}
				break;
			default:
				return returnError("Неопознанный тип ввода");
		}
		return response;
	}

	private boolean validate_RAM_index(String addr_s){
		int addr;
		try {
			addr = Integer.parseInt(addr_s);
			if ((addr < 0) || (addr >= MEM)) return false;
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private String returnError(String message){
		String result = makeJSONone("error", message);
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return result;
	}
}