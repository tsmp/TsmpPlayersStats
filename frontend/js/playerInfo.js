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
    SetElemText("nicknames", player.nicknames);
    SetElemText("kills", player.frags);
    SetElemText("kd", player.kd.toFixed(2));
    SetElemText("hoursIngame", player.hoursIngame);
    SetElemText("bestWpn", player.favouriteWeapon);
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
    const row = CreateElem("div", "row game-row", undefined);
    const columnsCnt = 3;

    for(let i = 0; i < columnsCnt; i++)
    {
        let classes = "col-xl-4 col-md-4 col-sm-4 game-col";

        if(i + 1 == columnsCnt)
            classes = "col-xl-4 col-md-4 col-sm-4 game-col-last";

        const col = CreateElem("div", classes, row);
        const textNode = CreateElem("h5", "text-center", col);
        textNode.appendChild(document.createTextNode(GetGameColText(gameData, i)));
    }

    row.onclick = ()=>OnGameClick(gameData.id);
    container.appendChild(row);
}

function CreateGamesList(games)
{
    const gamesContainer = document.getElementById("games-container");

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
