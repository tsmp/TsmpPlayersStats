function CreatePlayersList(playersJSON) {
	const body = document.body, tbl = document.createElement('table');

	tbl.style.width = '100%';
	tbl.style.border = '1px solid black';
	tbl.style.borderCollapse = "collapse";

	const tr1 = tbl.insertRow();
	const td1 = tr1.insertCell();

	text = "Номер";
	td1.appendChild(document.createTextNode(text));
	td1.style.border = '1px solid black';
	td1.style.borderCollapse = "collapse";
	td1.style.padding = '10px'
	td1.style.width = "30px"

	td2 = tr1.insertCell();
	text = "Никнеймы"
	td2.style.overflowWrap = 'anywhere'
	td2.style.border = '1px solid black';
	td2.style.borderCollapse = "collapse";
	td2.appendChild(document.createTextNode(text));

	td3 = tr1.insertCell();
	td3.style.border = '1px solid black';
	td3.style.borderCollapse = "collapse";
	td3.appendChild(document.createTextNode("Фраги"));

	td4 = tr1.insertCell();
	td4.style.border = '1px solid black';
	td4.style.borderCollapse = "collapse";
	td4.appendChild(document.createTextNode("K/D"));

	td5 = tr1.insertCell();
	td5.style.border = '1px solid black';
	td5.style.borderCollapse = "collapse";
	td5.appendChild(document.createTextNode("Артефакты"));

	td6 = tr1.insertCell();
	td6.style.border = '1px solid black';
	td6.style.borderCollapse = "collapse";
	td6.appendChild(document.createTextNode("Часов в игре"));

	for (let i = 0; i < playersJSON.length; i++) 
	{
		const tr = tbl.insertRow();
		const td = tr.insertCell();

		tr.onclick = ()=>OnPlayerClick(playersJSON[i].id);
		td.style.border = '1px solid black';
		td.style.borderCollapse = "collapse";
		td.style.padding = '10px'
		td.style.width = "30px"		

		num = playersJSON[i].num;
		td.appendChild(document.createTextNode(num));


		td2 = tr.insertCell();
		td2.style.overflowWrap = 'anywhere'
		td2.style.border = '1px solid black';
		td2.style.borderCollapse = "collapse";
		td2.appendChild(document.createTextNode(playersJSON[i].nicknames));

		td3 = tr.insertCell();
		td3.style.border = '1px solid black';
		td3.style.borderCollapse = "collapse";
		td3.appendChild(document.createTextNode(playersJSON[i].frags));

		td4 = tr.insertCell();
		td4.style.border = '1px solid black';
		td4.style.borderCollapse = "collapse";
		td4.appendChild(document.createTextNode(playersJSON[i].kd));

		td5 = tr.insertCell();
		td5.style.border = '1px solid black';
		td5.style.borderCollapse = "collapse";
		td5.appendChild(document.createTextNode(playersJSON[i].arts));

		td6 = tr.insertCell();
		td6.style.border = '1px solid black';
		td6.style.borderCollapse = "collapse";
		td6.appendChild(document.createTextNode(playersJSON[i].hoursIngame));
	}

	body.appendChild(tbl);
}

function OnPlayerClick(id) {
	toPlayerInfo(id);
}

function toPlayerInfo(player) {
    toPage("playerInfo.html?player=" + player);
}

window.onload = function(e) { 
    const req = servicesUrl +  "StatsSite/v1/GetPlayers";
	let M = httpGet(req)
	const parsedJson = JSON.parse(M.responseText);
    //PrintJSON(parsedJson);
	CreatePlayersList(parsedJson);
}
