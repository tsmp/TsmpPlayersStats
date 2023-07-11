const PlayerId = GetUrlParamValue("player");

var Load = function()
{
    if (PlayerId == null)	
        toMainPage();	

    const req = servicesUrl + "StatsSite/v1/GetPlayer?player=" + PlayerId;
    let M = httpGet(req)

    const parsedJson = JSON.parse(M.responseText);
    CreatePlayerStat(parsedJson);
    CreateGamesList(parsedJson.games)
}

function CreatePlayerStat(player) 
{
    SetElemText("playerId", "Игрок " + PlayerId);
    SetElemText("nicknames", "Никнеймы:<br>" + player.nicknames);
    SetElemText("kills", "Фраги:<br>" + player.frags);
    SetElemText("kd", "K/D:<br>" + player.kd.toFixed(2));
    SetElemText("hoursIngame", "Часов в игре:<br>" + player.hoursIngame);
    SetElemText("bestWpn", "Любимое оружие:<br>" + player.favouriteWeapon);
}

function GetGameColText(gameData, index) {
    switch (index) {
        case 0:
            return gameData.serverName;

        case 1:
            return gameData.mapName;

        case 2:
            return gameData.date;
    }

    return "ree";
}

function CreateGameEntry(container, gameData) {
    const row = CreateElem("div", "table_row", undefined);
    const columnsCnt = 3;

    for(let i = 0; i < columnsCnt; i++) {
        const textNode = CreateElem("div", "table_data", row);
        textNode.appendChild(document.createTextNode(GetGameColText(gameData, i)));
    }

    row.onclick = ()=>OnGameClick(gameData.id);
    container.appendChild(row);
}

function CreateGamesList(games)
{
    const gamesContainer = document.getElementById("table_stats");

    for (let i = 0; i < games.length; i++)
        CreateGameEntry(gamesContainer, games[i]);
}

function getPdf()
{
    window.open(servicesUrl + "StatsSite/v1/GetPlayerPdf?player=" + PlayerId, '_blank').focus();
} 

function OnGameClick(gameId)
{
    toGamePage(gameId);
}

function toGamePage(gameId)
{
    toPage("game.html?game=" + gameId + "&player=" + PlayerId);
}

function toPlayersPage()
{
    toPage("players.html");
}

Load();
