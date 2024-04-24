<!DOCTYPE html>
<html lang="en">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Учебный эмулятор ЭВМ</title>

	<link rel="stylesheet" href="css/styles.css">
	<script type="text/javascript" src="js/script.js"></script>
</head>
<body>
	<div class = "content-zone">
		<div class = "left-side">
			<div class = "container-COUNT container-cnt_reg">
				<div>СЧАК</div>
				<div>Ячейка на исполнение</div>
				<div>
					<input type="text" id = "textBox_CANT" disabled>
				</div>
				<div>
					<input type="text" id = "textBox_CURcell"  disabled>
				</div>
			</div>
			<div class = "container-REGISTER container-cnt_reg">
				<div></div>
				<div>Регистр АЛУ</div>
				<div>
					<input type="text" id = "textBox_RO" disabled>
				</div>
				<div>
					<input type="text" id = "textBox_ALU" disabled>
				</div>
			</div>
			<div class = "container-RAM">
				RAM
				<div class = "RAM-list">
					<select size="10" id = "select_RAM_list" onchange="RAM_listChange()">
					</select>
				</div>
			</div>
		</div>
		<div class = "right-side">
			<div class = "container-controls">
				<div class = "container-CANT_select">
					<div>
						Выберите
					</div>
					<div>
						<input type="number" id = "RAM_choser" min="0" value="0" step="1" onchange="spinnerChange()">
					</div>
				</div>
				<button>Установить счётчик</button>
				<button>Выполнить текущую ячейку</button>
				<button>Выполнить программу</button>
			</div>
			<div class = "container-input">
				Запись ячейки
				<div class = "container-input-inner">
					<div class = "container-input-left">
						<div>
							<input type = "radio" name="input-type" id = "input_type_radio_clean" onchange="change_input_box()" checked>
							<label for="input-type-radio-clean" onclick="set_radio_input_type('clean')">Прямой ввод</label>
						</div>
						<div>
							<input type = "radio" name="input-type" id = "input_type_radio_comm" onchange="change_input_box()">
							<label for="input-type-radio-comm" onclick="set_radio_input_type('comm')">Команда</label>
						</div>
						<div>
							<input type = "radio" name="input-type" id = "input_type_radio_data" onchange="change_input_box()">
							<label for="input-type-radio-data" onclick="set_radio_input_type('data')">Данные</label>
						</div>
						<div>
							<input type = "checkbox" name="input-ALU" id = "input_ALU">
							<label for="input-ALU">Запись в регистр АЛУ</label>
						</div>
						
					</div>
					<div class = "container-input-right">
						<div id = "input_zone_clean" style="display: block">
							<input type="text" id = "input_ramwrite_clean" style="display: block;" maxlength="32">
							<label for="input-ramwrite-clean" style="display: block;">Битовый набор без пробелов (32bit)</label>
						</div>
						<div id = "input_zone_comm" style="display: none">
							<input type="text" id = "input_ramwrite_comm_c">
							<input type="number" id = "input_ramwrite_comm_addr">
							<label for="input-ramwrite-comm-c" style="display: block;">Команда; адрес операнда</label>
						</div>
						<div id = "input_zone_data"  style="display: none">
							<div class = "container-input-zone-data">
								<div>
									<input type="text" id = "input_ramwrite_data" style="display: block;">
									<label for="input-ramwrite-data" style="display: block;">Десятичное число</label>
								</div>
								<div>
									<div>
										<input type = "radio" name="input-data-type" id = "input_data_type_radio_integer">
										<label for="input-data-type-radio-integer">integer</label>
									</div>
									<div>
										<input type = "radio" name="input-data-type" id = "input_data_type_radio_float">
										<label for="input-data-type-radio-float">float</label>
									</div>
								</div>
							</div>
						</div>
						<button>Ввод</button>
					</div>

				</div>
			</div>
			<div class = "container-output">
				Содержимое ячейки
				<div class = "container-output-inner">
					<div>
						<label for="output-comm-c">Команда</label>
						<input type="text" id = "output_comm_c" disabled>
						<input type="text" id = "output_comm_addr" disabled>
					</div>
					<div>
						<label for="output-integer">Целое</label>
						<input type="text" id = "output_integer" disabled>
					</div>
					<div>
						<label for="output-float">Дробное</label>
						<input type="text" id = "output_float" disabled>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class = "bottom-row">
		<button>Очистить память</button>
		<button>Считать файл памяти</button>
		<button>Дамп памяти</button>
		<button>Компилятор</button>
	</div>
</body>
</html>