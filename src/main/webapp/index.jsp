<!DOCTYPE html>
<html lang="en">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Учебный эмулятор ЭВМ</title>

	<link rel="stylesheet" href="css/styles.css">
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<script type="text/javascript" src="js/script.js"></script>
	<script type="text/javascript" src="js/bootstrap.bundle.min.js"></script>
	<script type="text/javascript" src="js/jQuery_3.7.1.js"></script>
</head>
<body>
	<div class="container-xl">
		<div class="row">
			<div class="col">
				<div class = "container-cnt_reg">
					<div class="row mt-1">
						<div class="col-lg-9">
							<h5>Аккумулятор</h5>
							<div><input type="text" id = "textBox_ALU" class = "form-control bit_out" disabled value="0000 0000 0000 0000 0000 0000 0000 0000"></div>
						</div>
						<div class="row mt-2">
							<div class="col-lg-2">integer</div>
							<div class="col-8"><input type="text" id="output_ALU_integer" class="form-control num_out" disabled value="0"></div>
						</div>
						<div class="row mt-2 mb-2">
							<div class="col-lg-2">float</div>
							<div class="col-8"><input type="text" id="output_ALU_float" class="form-control num_out" disabled value="0.0"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="col">
				<div class="row mt-1">
					<div class="col-lg-3">
						<div>СЧАК</div>
						<div>
							<input type="text" id="textBox_CANT" class="form-control index_out" disabled value="[000]">
						</div>
					</div>
					<div class="col-lg-9">
						<div>Команда на выполнение</div>
						<div class="row mt">
							<div class="col-5"><input type="text" id="output_CANT_comm_c" class="form-control" disabled value="0"></div>
							<div class="col-3"><input type="text" id="output_CANT_addr" class="form-control index_out" disabled value="0"></div>
						</div>
					</div>
					<div class="row mt-2">
						<div class="col">
							<input type="text" id="textBox_CURcell" class="form-control bit_out" disabled value="0000 0000 0000 0000 0000 0000 0000 0000">
						</div>
					</div>
					<div class="row mt-2">
						<div class="col">
							<button class="btn btn-primary" onclick="execONE()">Выполнить одну команду</button>
						</div>
						<div class="col">
							<button class="btn btn-primary" onclick="execALL()">Выполнить все команды</button>
						</div>
					</div>
				</div>
			</div>
		</div>


		<div class="row">

			<div class="col">

				<div class = "row container-RAM">
					<h5>Оперативная память</h5>
					<div class = "RAM-list mt-2">
						<select size="15" class="form-select" id = "select_RAM_list" onchange="RAM_listChange()">
						</select>
					</div>
				</div>
				<div class="row mt-2">
					<div>
						<label for="input_ram_file" class="form-label">Заполнить память из файла</label>
						<input class="form-control bit_out" type="file" id="input_ram_file" onchange="sendRAMfile()">
					</div>
				</div>
				<div class="row mt-2">
					<div class="col-lg-4 mt-1">
						<button class="btn btn-primary" onclick="onclick_RAM_clear()">Очистить память</button>
					</div>
					<div class="col-lg-4 mt-1">
						<button class="btn btn-primary" onclick="onclick_RAM_dump()">Скачать состояние</button>
					</div>
				</div>

			</div>


			<div class="col">

				<div class = "row container-CANT_select">
					<h5>Выбор ячейки</h5>
					<div class="col-lg-3">
						<input type="number" class="form-control index_out" id = "RAM_choser" min="0" value="0" step="1" onchange="spinnerChange()">
					</div>
					<div class="col-lg-6">
						<button class="btn btn-primary" onclick="setCANT()">Установить СЧАК</button>
					</div>
				</div>

				<div class = "row container-input mt-2">
					<h5 id="input_cell_label">Запись в ячейку 0</h5>
					<div class = "row container-input-inner">
						<div class = "col-lg-5 container-input-left">
							<div>
								<input type = "radio" class="form-check-input" name="input-type" id = "input_type_radio_clean" onchange="onchange_input_box('clean')" checked>
								<label class="form-check-label" for="input-type-radio-clean" onclick="set_radio_input_type('clean')">Прямой ввод</label>
							</div>
							<div>
								<input type = "radio" class="form-check-input" name="input-type" id = "input_type_radio_comm" onchange="onchange_input_box('comm')">
								<label class="form-check-label" for="input-type-radio-comm" onclick="set_radio_input_type('comm')">Команда</label>
							</div>
							<div>
								<input type = "radio" class="form-check-input" name="input-type" id = "input_type_radio_data" onchange="onchange_input_box('data')">
								<label class="form-check-label" for="input-type-radio-data" onclick="set_radio_input_type('data')">Данные</label>
							</div>
							<div>
								<input type = "checkbox" class="form-check-input" name="input-ALU" id = "input_ALU" onchange="onchange_input_ALU()">
								<label class="form-check-label text-wrap" for="input-ALU" onclick="change_input_ALU()">Аккумулятор</label>
							</div>
							
						</div>
						<div class = "col container-input-right">
							<div class="row">
								<div class="col" id = "input_zone_clean" style="display: block">
									<input type="text" class="form-control bit_out" id = "input_ramwrite_clean" style="display: block;" maxlength="32">
									<label for="input-ramwrite-clean" style="display: block;">Битовый набор без пробелов (32bit)</label>
								</div>
	
								<div class="col" id = "input_zone_comm" style="display: none">
									<div class="row">
										<div class="col">
											<input type="text" class="form-control num_out" id = "input_ramwrite_comm_c">
											
										</div>
										<div class="col">
											<input type="number" class="form-control index_out" id = "input_ramwrite_comm_addr" min="0" value="0" step="1">
										</div>
									</div>
									<div class="row"><label for="input-ramwrite-comm-c" style="display: block;">Команда; адрес операнда</label></div>
								</div>
	
								<div class="col" id = "input_zone_data" style="display: none">
									<div class = "row container-input-zone-data">
										<div class="col">
											<input type="text" class="form-control num_out" id = "input_ramwrite_data" style="display: block;">
											<label for="input-ramwrite-data" style="display: block;">Десятичное число</label>
										</div>
										<div class="col dataType-radio">
											<div>
												<input type = "radio" class="form-check-input" name="input-data-type" id = "input_data_type_radio_integer" onchange="onchange_input_dataType('int')">
												<label class="form-check-label" for="input-data-type-radio-integer" onclick="change_input_dataType('int')">integer</label>
											</div>
											<div>
												<input type = "radio" class="form-check-input" name="input-data-type" id = "input_data_type_radio_float" onchange="onchange_input_dataType('float')">
												<label class="form-check-label" for="input-data-type-radio-float" onclick="change_input_dataType('float')">float</label>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row"><button class="center_button btn btn-primary mt-2" onclick="onclick_input_cell()">Ввод</button></div>
						</div>
					</div>
				</div>
				<div class = "row container-output mt-2">
					<h5 id = "output_text_sign">Содержимое ячейки</h5>
					<div class = "container-output-inner">
						<div class="row mt-2">
							<div class="col-3"><label for="output-comm-c">Команда</label></div>
							<div class="col-5"><input type="text" class="form-control" id = "output_comm_c" disabled value="0"></div>
							<div class="col-3"><input type="text" class="form-control index_out" id = "output_comm_addr" disabled value="0"></div>
						</div>
						<div class="row mt-2">
							<div class="col-3"><label for="output-integer">integer</label></div>
							<div class="col-7"><input type="text" class="form-control num_out" id = "output_integer" disabled value="0"></div>
						</div>
						<div class="row mt-2">
							<div class="col-3"><label for="output-float">float</label></div>
							<div class="col-7"><input type="text" class="form-control num_out" id = "output_float" disabled value="0"></div>
						</div>
					</div>
				</div>
				<div class="mt-2">
					<button class="btn btn-primary">
						<a href="/EMU/help.jsp" target="_blank" class="link-light">Помощь</a>
					</button>
				</div>
			</div>
		</div>

		<div class="row mt-4 mb-3 border">
			<div class="row">

				<div class="col">
					<h4>Компилятор</h4>
					<div>
						<label for="input_ram_file" class="form-label">Загрузить исходный код и скомпилировать программу</label>
						<input class="form-control bit_out" type="file" id="compiler_source_file" onchange="sendCompilerFile()">
					</div>
				</div>
				<div class="col">
					<div class="row">
						<h5 id="compiler-message-box">Компиляция не выполнялась</h5>
						<div>Ошибок - <span id="compiler-errors-number">0</span></div>
						<div>Токенов - <span id="compiler-tokens-number">0</span></div>
						<div>Переменных - <span id="compiler-variables-number">0</span></div>
						<div>Инструкций - <span id="compiler-instructions-number">0</span></div>
					</div>
				</div>
			</div>

			<div class="row mt-2">
				<div class="col">
					<ul class="nav nav-tabs" id="compilerTabs" role="tablist">
						<li class="nav-item" role="presentation">
							<button class="nav-link active" id="compiler-errors-tab" data-bs-toggle="tab" data-bs-target="#compiler-errors" type="button" role="tab">Ошибки</button>
						</li>
						<li class="nav-item" role="presentation">
							<button class="nav-link" id="compiler-tokens-tab" data-bs-toggle="tab" data-bs-target="#compiler-tokens" type="button" role="tab" >Токены</button>
						</li>
						<li class="nav-item" role="presentation">
							<button class="nav-link" id="compiler-variables-tab" data-bs-toggle="tab" data-bs-target="#compiler-variables" type="button" role="tab">Переменные</button>
						</li>
						<li class="nav-item" role="presentation">
							<button class="nav-link" id="compiler-instructions-tab" data-bs-toggle="tab" data-bs-target="#compiler-instructions" type="button" role="tab">Инструкции</button>
						</li>
					</ul>
	
					<div class="tab-content" id="compilerTabsContent">
						<div class="border border-top-0 mb-2 tab-pane show active table-box" id="compiler-errors" role="tabpanel">
							<table class="table" id="compiler-errors-table">
								<thead>
									<tr>
										<th scope="col">#</th>
										<th scope="col">Строка</th>
										<th scope="col">Токен</th>
										<th scope="col">Ошибка</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
						<div class="border border-top-0 mb-2 tab-pane table-box" id="compiler-tokens" role="tabpanel">
							<table class="table" id="compiler-tokens-table">
								<thead>
									<tr>
										<th scope="col">#</th>
										<th scope="col">Строка</th>
										<th scope="col">Тип токена</th>
										<th scope="col">Значение</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
						<div class="border border-top-0 mb-2 tab-pane table-box" id="compiler-variables" role="tabpanel">
							<table class="table" id="compiler-variables-table">
								<thead>
									<tr>
										<th scope="col">#</th>
										<th scope="col">Адрес</th>
										<th scope="col">Тип</th>
										<th scope="col">Имя</th>
										<th scope="col">Значение int</th>
										<th scope="col">Значение float</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
						<div class="border border-top-0 mb-2 tab-pane table-box" id="compiler-instructions" role="tabpanel">
							<table class="table" id="compiler-instructions-table">
								<thead>
									<tr>
										<th scope="col">#</th>
										<th scope="col">Тип</th>
										<th scope="col">Объект записи</th>
										<th scope="col">Операнд 1</th>
										<th scope="col">Оператор</th>
										<th scope="col">Операнд 2</th>
										<th scope="col">Глубина</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
</body>
</html>