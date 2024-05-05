window.onload = async function(){
	await initiateMain();
	await startLoading();

	//Сброс отображения окна ввода при перезагрузке
	set_radio_input_type("clean");
	change_input_box();
}

function startLoading(){
	window.isLoading = true;
	document.getElementById("loading").style.display = "block";
	console.log("Загрузка началась");

}
function endLoading(){
	document.getElementById("loading").style.display = "none";
	console.log("Загрузка завершена");
	window.isLoading = false;
}
function configLoaded(){
	initiateState();
}

async function initiateMain(){
	window.isLoading = true;
	window.waitStart = startLoading;
	window.waitDone = endLoading;
	window.initDone = configLoaded;
	window.DOMS = {
		textBox_CANT: document.getElementById("textBox_CANT"),
		textBox_CURcell: document.getElementById("textBox_CURcell"),

		textBox_RO: document.getElementById("textBox_RO"),
		textBox_ALU: document.getElementById("textBox_ALU"),

		select_RAM_list: document.getElementById("select_RAM_list"),

		RAM_choser: document.getElementById("RAM_choser"),

		input_zone_clean : document.getElementById("input_zone_clean"),
		input_zone_comm : document.getElementById("input_zone_comm"),
		input_zone_data : document.getElementById("input_zone_data"),

		input_type_radio_clean: document.getElementById("input_type_radio_clean"),
		input_type_radio_com: document.getElementById("input_type_radio_comm"),
		input_type_radio_data: document.getElementById("input_type_radio_data"),

		output_comm_c: document.getElementById("output_comm_c"),
		output_comm_addr: document.getElementById("output_comm_addr"),
		output_integer: document.getElementById("output_integer"),
		output_float: document.getElementById("output_float"),
	}
	window.CONFIG = {
		MEM: 0,
		CELL: 0,
		BMEM: 0,
		VER: "",
		ajaxURL: '/EMU/main',
		maxDigits: 0,
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

async function initiateState(){
	window.STATE = {
		CHOSEN: 0,
		CANT: 0,
		ALU: "0000 0000 0000 0000 0000 0000 0000 0000",
		RAM: [],
	}
	CONFIG.maxDigits = CONFIG.MEM.toString().length
	RAM_choser.max = CONFIG.MEM - 1;
	for (let i = 0; i < CONFIG.MEM; i++) {
		STATE.RAM.push(
			{
				clean: "0000 0000 0000 0000 0000 0000 0000 0000",
				comm_c: 0,
				comm_addr: 0,
				comm_char: "",
				data_int: 0,
				data_float: 0.0,
			}
		);
	}
	refresh_UI();
	endLoading();
}

function makeIndex(i){
	let curDigits = i.toString().length;
	let index = "[";
	for (let j = 0; j < CONFIG.maxDigits - curDigits; j++) index += "0";
	index += i.toString();
	index += "] ";
	return index;
}

function change_input_box(){
	if (DOMS.input_type_radio_clean.checked) DOMS.input_zone_clean.style.display = "block";
	else DOMS.input_zone_clean.style.display = "none";

	if (DOMS.input_type_radio_com.checked) DOMS.input_zone_comm.style.display = "block";
	else DOMS.input_zone_comm.style.display = "none";

	if (DOMS.input_type_radio_data.checked) DOMS.input_zone_data.style.display = "block";
	else DOMS.input_zone_data.style.display = "none";
}

function set_radio_input_type(target_radio){
	if (target_radio === 'clean') DOMS.input_type_radio_clean.checked = "true";
	else if (target_radio === 'comm') DOMS.input_type_radio_com.checked = "true";
	else if (target_radio === 'data') DOMS.input_type_radio_data.checked = "true";

	change_input_box();
}

function refresh_UI(){
	show_RAM();
	show_REGS();
	showOutput();
}

function show_RAM(){
	for (let i = 0; i < select_RAM_list.options.length; i++) {
		select_RAM_list.remove(i);
	}
	for (let i = 0; i < CONFIG.MEM; i++) {
		let cell = document.createElement('option');
		cell.value = i;
		cell.innerHTML = makeIndex(i) + STATE.RAM[i].clean;
		select_RAM_list.appendChild(cell);
	}
}

function show_REGS(){
	textBox_CANT.value = makeIndex(STATE.CANT);
	textBox_CURcell.value = STATE.RAM[STATE.CANT].clean;
	textBox_RO.value = "[RO]";
	textBox_ALU.value = STATE.ALU;
}

function showOutput(){
	let comm_c = ""
	if (STATE.RAM[STATE.CHOSEN].comm_char !== "") tmp += STATE.RAM[STATE.CHOSEN].comm_char + " - ";
	comm_c += STATE.RAM[STATE.CHOSEN].comm_c;
	output_comm_c.value = comm_c;

	output_comm_addr.value = STATE.RAM[STATE.CHOSEN].comm_addr;
	output_integer.value = STATE.RAM[STATE.CHOSEN].data_int;
	output_float.value = STATE.RAM[STATE.CHOSEN].data_float;
}

function validate_RAM_index(value){
	value = Number(value);
	if (!Number.isInteger(value)) return 0;
	if (value < 0) return 0;
	else if (value >= CONFIG.MEM) return CONFIG.MEM - 1;
	else return value;
}

function RAM_listChange(){
	let value = validate_RAM_index(select_RAM_list.value);
	RAM_choser.value = value;
	setChosen(value);
}

function spinnerChange(){
	let value = validate_RAM_index(RAM_choser.value);
	RAM_choser.value = value;
	select_RAM_list.value = value;
	setChosen(value);
}

function setChosen(i){
	STATE.CHOSEN = i;
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


function getState(){
	$.ajax({
		url: CONFIG.ajaxURL,
		type: "GET",
		data: {
			method: "GETSTATE",
		},
		success: function (data) {			
			console.log(data);
			window["STATE"].CANT = data.CANT;
			window["STATE"].ALU = data.RO;
			window["STATE"].RAM = data.RAM;

		},
		error: function (error) {
			console.log(`Error ${error}`);
		}
	});
}