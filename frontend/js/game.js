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

    const backBtn = document.getElementById("back-btn");
    backBtn.onclick = () => ToPlayerInfo();
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
    const row = CreateElem("div", "row wpn-row", undefined);
    const columnsCnt = 2;

    for(let i = 0; i < columnsCnt; i++)
    {
        let classes = "col-xl-6 col-md-6 col-sm-6 wpn-col";

        if(i + 1 == columnsCnt)
            classes = "col-xl-6 col-md-6 col-sm-6 wpn-col-last";

        const col = CreateElem("div", classes, row);
        const textNode = CreateElem("h5", "text-center", col);

        let text = wpnData.wpnName;

        if(i + 1 == columnsCnt)
            text = wpnData.hits;

        textNode.appendChild(document.createTextNode(text));
    }

    container.appendChild(row);
}

function CreateWeaponsList(lst)
{
    const wpnContainer = document.getElementById("wpn-container");

    for (let i = 0; i < lst.length; i++)
        CreateWpnEntry(wpnContainer, lst[i]);
}

function ToPlayerInfo()
{
    toPage("playerInfo.html?player=" + PlayerId);
}

Load();
