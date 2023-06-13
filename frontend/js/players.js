function GetPlayerColText(playerData, index) {
    switch (index) {
        case 0:
            return playerData.num;

        case 1:
            return playerData.nicknames;

        case 2:
            return playerData.frags;

        case 3:
            return playerData.kd.toFixed(2);

        case 4:
            return playerData.arts;

        case 5:
            return playerData.hoursIngame;
    }

    return "ree";
}

function CreatePlayerEntry(container, playerData) {
    const row = CreateElem("div", "row player-row", undefined);
    const columnsCnt = 6;

    for(let i = 0; i < columnsCnt; i++)
    {
        let classes = "col-xl-1 col-md-1 col-sm-1 player-col";

        if(i == 1)
            classes = "col-xl-7 col-md-7 col-sm-7 player-col";

        if(i + 1 == columnsCnt)
            classes = "col-xl-1 col-md-1 col-sm-1 player-col-last";

        const col = CreateElem("div", classes, row);
        const textNode = CreateElem("h5", "text-center", col);
        textNode.appendChild(document.createTextNode(GetPlayerColText(playerData, i)));
    }

    row.onclick = ()=>OnPlayerClick(playerData.id);
    container.appendChild(row);
}

function CreatePlayersList(playersJSON) {
    const playersContainer = document.getElementById("players-container");

    for (let i = 0; i < playersJSON.length; i++)
        CreatePlayerEntry(playersContainer, playersJSON[i]);
}

function OnPlayerClick(id) {
	toPlayerInfo(id);
}

function toPlayerInfo(player) {
    toPage("playerInfo.html?player=" + player);
}

function OnLoad() {
    const req = servicesUrl +  "StatsSite/v1/GetPlayers";
	let M = httpGet(req)
	const parsedJson = JSON.parse(M.responseText);
    //PrintJSON(parsedJson);
	CreatePlayersList(parsedJson);
}

OnLoad();
