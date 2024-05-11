window.onload = async function(){
	await initiateMain();

	set_radio_input_type("clean");
	onchange_input_box("clean");
	change_input_dataType("int")
}

function configLoaded(){
	getState();
}

async function initiateMain(){
	window.isLoading = true;
	window.initDone = configLoaded;
	window.stateDone = initiateState;
	window.callRefresh = refresh_UI;
	window.callRefresh_regs = show_REGS;
	window.DOMS = {
		textBox_CANT: document.getElementById("textBox_CANT"),
		textBox_CURcell: document.getElementById("textBox_CURcell"),
		textBox_CURcell_comm_c: document.getElementById("output_CANT_comm_c"),
		textBox_CURcell_comm_addr: document.getElementById("output_CANT_addr"),


		textBox_ALU: document.getElementById("textBox_ALU"),
		textBox_ALU_int: document.getElementById("output_ALU_integer"),
		textBox_ALU_float: document.getElementById("output_ALU_float"),

		select_RAM_list: document.getElementById("select_RAM_list"),

		RAM_choser: document.getElementById("RAM_choser"),

		input_zone_clean : document.getElementById("input_zone_clean"),
		input_zone_comm : document.getElementById("input_zone_comm"),
		input_zone_data : document.getElementById("input_zone_data"),

		input_type_radio_clean: document.getElementById("input_type_radio_clean"),
		input_type_radio_com: document.getElementById("input_type_radio_comm"),
		input_type_radio_data: document.getElementById("input_type_radio_data"),

		input_type_checkbox_ALU: document.getElementById("input_ALU"),

		input_dataType_radio_int: document.getElementById("input_data_type_radio_integer"),
		input_dataType_radio_float: document.getElementById("input_data_type_radio_float"),

		input_cell_label: document.getElementById("input_cell_label"),

		input_textbox_clean: document.getElementById("input_ramwrite_clean"),
		input_textbox_comm_c: document.getElementById("input_ramwrite_comm_c"),
		input_textbox_comm_addr: document.getElementById("input_ramwrite_comm_addr"),
		input_textbox_data: document.getElementById("input_ramwrite_data"),

		output_comm_c: document.getElementById("output_comm_c"),
		output_comm_addr: document.getElementById("output_comm_addr"),
		output_integer: document.getElementById("output_integer"),
		output_float: document.getElementById("output_float"),

		output_text_sign: document.getElementById("output_text_sign"),
	}
	window.CONFIG = {
		MEM: 0,
		CELL: 0,
		BMEM: 0,
		VER: "",
		ajaxURL: '/EMU/main',
		maxDigits: 0,
	}
	window.INPUT_STATE = {
		type: "clean",
		dataType: "int",
		toALU: false
	}
	AJAXgetConfig();
}

function AJAXgetConfig(){
	$.ajax({
		url: CONFIG.ajaxURL,
		type: "GET",
		data: {
			method: "GETCONFIG",
		},
		success: function (data) {			
			window["CONFIG"].MEM = data.MEM;
			window["CONFIG"].CELL = data.CELL;
			window["CONFIG"].BMEM = data.BMEM;
			window["CONFIG"].VER = data.VER;
			window["initDone"]();
		},
		error: function (error) {
			console.log(`Error ${error}`);
		}
	});
}

function getState(clear = false){
	let method = clear ? "CLEARMEM" : "GETSTATE";
	$.ajax({
		url: CONFIG.ajaxURL,
		type: "GET",
		data: {
			method: method,
		},
		success: function (data) {
			window["stateDone"](data.CANT, data.RO, data.RAM);
		},
		error: function (error) {
			console.log(`Error ${error}`);
		}
	});
}

async function initiateState(CANT, ALU, RAM){
	window.STATE = {
		CHOSEN: RAM_choser.value,
		CANT: CANT,
		ALU: ALU,
		RAM: RAM,
	}
	CONFIG.maxDigits = CONFIG.MEM.toString().length;
	DOMS.RAM_choser.max = CONFIG.MEM - 1;
	DOMS.input_textbox_comm_addr.max = CONFIG.MEM - 1;
	refresh_UI();
}

function makeIndex(i){
	let curDigits = i.toString().length;
	let index = "[";
	for (let j = 0; j < CONFIG.maxDigits - curDigits; j++) index += "0";
	index += i.toString();
	index += "] ";
	return index;
}

function onchange_input_box(target_radio){
	INPUT_STATE.type = target_radio;
	if (DOMS.input_type_radio_clean.checked) DOMS.input_zone_clean.style.display = "block";
	else DOMS.input_zone_clean.style.display = "none";

	if (DOMS.input_type_radio_com.checked) DOMS.input_zone_comm.style.display = "block";
	else DOMS.input_zone_comm.style.display = "none";

	if (DOMS.input_type_radio_data.checked) DOMS.input_zone_data.style.display = "block";
	else DOMS.input_zone_data.style.display = "none";
}

function set_radio_input_type(target_radio){
	INPUT_STATE.type = target_radio;
	if (target_radio === 'clean') DOMS.input_type_radio_clean.checked = "true";
	else if (target_radio === 'comm') DOMS.input_type_radio_com.checked = "true";
	else if (target_radio === 'data') DOMS.input_type_radio_data.checked = "true";

	onchange_input_box(target_radio);
}

function onchange_input_ALU(){
	INPUT_STATE.toALU = DOMS.input_type_checkbox_ALU.checked;
	setInputText();
}

function change_input_ALU(){
	DOMS.input_type_checkbox_ALU.checked = !DOMS.input_type_checkbox_ALU.checked;
	onchange_input_ALU();
}

function onchange_input_dataType(dataType){
	INPUT_STATE.dataType = dataType;
}

function change_input_dataType(dataType){
	INPUT_STATE.dataType = dataType;
	if (dataType === 'int') DOMS.input_dataType_radio_int.checked = "true";
	else if (dataType === 'float') DOMS.input_dataType_radio_float.checked = "true";
	onchange_input_dataType(dataType);
}

function onclick_input_cell(){
	let inputData = {};
	inputData.type = INPUT_STATE.type;
	inputData.chosen = STATE.CHOSEN;
	inputData.toRO = INPUT_STATE.toALU;
	switch (INPUT_STATE.type) {
		case "clean":
			let data = DOMS.input_textbox_clean.value;
			if (data.length > CONFIG.CELL){
				alert("Слишком длинный битовый набор!");
				return;
			}
			if (!/^[01]+$/g.test(data)){
				alert("Посторонние символы в битовом наборе!");
				return;
			}
			inputData.data = data;
			break;
		case "comm":
			inputData.comm_c = DOMS.input_textbox_comm_c.value;
			let addr = validate_RAM_index_strict(DOMS.input_textbox_comm_addr.value);
			if (addr === false){
				alert("Адрес некорректен!");
				return;
			}
			inputData.comm_addr = addr
			break;
		case "data":
			inputData.dataType = INPUT_STATE.dataType
			if (isNaN(DOMS.input_textbox_data.value)){
				alert("Введено не число!");
				return;
			}
			inputData.data = DOMS.input_textbox_data.value
			break;
		default:
			return;
	}

	sendInput(inputData);
}

function onclick_RAM_clear(){
	getState(true);
	DOMS.RAM_choser.value = 0;
	set_radio_input_type("clean");
	onchange_input_box("clean");
	change_input_dataType("int");
	DOMS.input_textbox_clean.value = "";
	DOMS.input_textbox_comm_c.value = "";
	DOMS.input_textbox_comm_addr.value = 0;
	DOMS.input_textbox_data.value = "";
}

function setCANT(){
	requestPOST_CANT();
}
function requestPOST_CANT(){
	$.ajax({
		url: CONFIG.ajaxURL,
		type: "POST",
		data: {
			method: "SETCANT",
			CANT: STATE.CHOSEN
		},
		success: function (data) {
			window["STATE"].CANT = data.CANT;
			window["callRefresh_regs"]();
		},
		error: function (error) {
			console.log(`Error ${error}`);
		}
	});
}

function execONE(){
	execRequest("EXECONE");
}
function execALL(){
	execRequest("EXECALL");
}

function execRequest(type){
	$.ajax({
		url: CONFIG.ajaxURL,
		type: "POST",
		data: {
			method: type
		},
		success: function (data) {
			window["STATE"].CANT = data.CANT;
			window["STATE"].ALU = data.RO;
			window["STATE"].RAM = data.RAM;
			if (data.message !== "") alert(data.message);
			window["callRefresh"]();
		},
		error: function (error) {
			console.log(`Error ${error}`);
		}
	});
}

function refresh_UI(){
	show_RAM();
	show_REGS();
	showOutput();
}

function show_RAM(){
	select_RAM_list.innerHTML = "";
	for (let i = 0; i < CONFIG.MEM; i++) {
		let cell = document.createElement('option');
		cell.value = i;
		cell.innerHTML = makeIndex(i) + STATE.RAM[i].clean;
		select_RAM_list.appendChild(cell);
	}
}

function show_REGS(){
	DOMS.textBox_CANT.value = makeIndex(STATE.CANT);
	DOMS.textBox_CURcell.value = STATE.RAM[STATE.CANT].clean;

	let cant = STATE.RAM[STATE.CANT];
	let comm_c = ""
	if (cant.comm_char !== "") comm_c += cant.comm_char + " - ";
	comm_c += cant.comm_c;
	DOMS.textBox_CURcell_comm_c.value = comm_c;
	DOMS.textBox_CURcell_comm_addr.value = cant.comm_addr;

	DOMS.textBox_ALU.value = STATE.ALU.clean;
	DOMS.textBox_ALU_int.value = STATE.ALU.data_int;
	DOMS.textBox_ALU_float.value = STATE.ALU.data_float;

}

function showOutput(){
	let source = STATE.RAM[STATE.CHOSEN];
	DOMS.output_text_sign.innerHTML = "Содержимое ячейки " + makeIndex(source.index);
	let comm_c = ""
	if (source.comm_char !== "") comm_c += source.comm_char + " - ";
	comm_c += source.comm_c;
	DOMS.output_comm_c.value = comm_c;

	DOMS.output_comm_addr.value = source.comm_addr;
	DOMS.output_integer.value = source.data_int;
	DOMS.output_float.value = source.data_float;

}

function validate_RAM_index(value){
	value = Number(value);
	if (!Number.isInteger(value)) return 0;
	if (value < 0) return 0;
	else if (value >= CONFIG.MEM) return CONFIG.MEM - 1;
	else return value;
}

function validate_RAM_index_strict(value){
	value = Number(value);
	if (!Number.isInteger(value)) return false;
	if (value < 0) return false;
	else if (value >= CONFIG.MEM) return false;
	else return value;
}

function RAM_listChange(){
	let value = validate_RAM_index(select_RAM_list.value);
	DOMS.RAM_choser.value = value;
	setChosen(value);
}

function spinnerChange(){
	let value = validate_RAM_index(RAM_choser.value);
	DOMS.RAM_choser.value = value;
	DOMS.select_RAM_list.value = value;
	setChosen(value);
}

function setInputText(){
	let line = "";
	if (INPUT_STATE.toALU === true)
		line = "Запись в аккумулятор";
	else
		line = "Запись в ячейку " + STATE.CHOSEN;
	DOMS.input_cell_label.innerHTML = line;
}

function setChosen(i){
	STATE.CHOSEN = i;
	setInputText();
	showOutput();
}

function isNumeric(num){
	return !isNaN(num)
}

function isNumeric(str) {
	if (typeof str != "string") return false // we only process strings!  
	return !isNaN(str) && // use type coercion to parse the _entirety_ of the string (`parseFloat` alone does not do this)...
		   !isNaN(parseFloat(str)) // ...and ensure strings of whitespace fail
}

function sendInput(inputData){
	inputData.method = "SETMEMCELL";
	$.ajax({
		url: CONFIG.ajaxURL,
		type: "POST",
		data: inputData,
		success: function (data) {
			if (data.index === "RO")
				window["STATE"].ALU = data;
			else
				window["STATE"].RAM[data.index] = data;
			window["callRefresh"]();
		},
		error: function (error) {
			console.log(`Error ${error}`);
		}
	});
}

function handleAJAXError(error){
	console.log(`Error ${error}`);
	if (error.error !== "") alert(error.error);
}

function sendRAMfile(){
	let formData = new FormData();
	formData.append('file', $("#input_ram_file")[0].files[0]);
	$.ajax({
		url: '/EMU/FileRAMfill',
		type: "POST",
		cache: false,
		contentType: false,
		processData: false,
		data: formData,
		success: function(data){
			window["STATE"].CANT = data.CANT;
			window["STATE"].ALU = data.RO;
			window["STATE"].RAM = data.RAM;
			if (data.message !== "") alert(data.message);
			window["callRefresh"]();
		},
		error: function(error){
			handleAJAXError(error);
		}
	});
}