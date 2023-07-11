const GameId = GetUrlParamValue("game");
const PlayerId = GetUrlParamValue("player");

var Load = function () {
    if (GameId == null)
        toMainPage();

    let req = servicesUrl + "StatsSite/v1/GetGame?game=" + GameId;
    let M = httpGet(req)
    const parsedJson = JSON.parse(M.responseText);

    CreateGameStat(parsedJson);
    CreateWeaponsList(parsedJson.wpnStats);
}

function CreateGameStat(game) {
    SetElemText("gameHeader", "Игра: " + game.date + ", сервер: " + game.server + ", карта: " + game.map);
    SetElemText("kills", "Фраги:<br>" + game.kills);
    SetElemText("killsAi", "Убийства НПС:<br>" + game.killsAi);
    SetElemText("deaths", "Смерти:<br>" + game.deaths);
    SetElemText("kd", "K/D:<br>" + game.kd.toFixed(2));
    SetElemText("hoursIngame", "Часов в игре:<br>" + game.hoursIngame.toFixed(2));
    SetElemText("killsOneLife", "Убийств за одну жизнь:<br>" + game.killsOneLife);
}

function CreateWpnEntry(container, wpnData) {
    const row = CreateElem("div", "table_row table_no_click", undefined);
    const columnsCnt = 2;

    for(let i = 0; i < columnsCnt; i++)
    {

        const col = CreateElem("div", "table_data", row);

        let text = wpnData.wpnName;

        if(i + 1 === columnsCnt)
            text = wpnData.hits;

        col.appendChild(document.createTextNode(text));
    }

    container.appendChild(row);
}

function CreateWeaponsList(lst)
{
    const wpnContainer = document.getElementById("table_stats");

    for (let i = 0; i < lst.length; i++)
        CreateWpnEntry(wpnContainer, lst[i]);
}

function ToPlayerInfo()
{
    toPage("playerInfo.html?player=" + PlayerId);
}

Load();
