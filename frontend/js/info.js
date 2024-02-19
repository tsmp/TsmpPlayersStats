const searchKey = "search"
const pageKey = "page"
const urlParams = new URLSearchParams(window.location.search);
id = urlParams.get("id")
PlayerId = urlParams.get("pl")

var Load = function () {
    // Если не вошли!!!
    if (id == null) {
        toLoginPage();
    }

    // Если нет id игрока
    if (PlayerId == null) {
        toLoginPage();
        throw new Error();
    }

    // Установить имя пользователя
    nameRequest = servicesUrl + "/PlayersSite/v1/MyName?key=" + id;
    user = httpGet(nameRequest);
    document.getElementById('user_name').innerHTML = user.responseText

    // Установить id пользователя
    document.getElementById('player_id').innerHTML = "Игрок: " + PlayerId

    getInfoReq = servicesUrl + "/PlayersSite/v1/GetPlayerInfo?key=" + id + "&playerId=" + PlayerId;
    //alert(getInfoReq)
    M = httpGet(getInfoReq)

    //PrintS(M.responseText)
    //PrintS(M.);

    var J = JSON.parse(M.responseText);

    //PrintJSON("res",J);
    CreatePlayerInfo(J);
}

function CreatePlayerInfo(playerJSON) {
    const list_div = document.createElement('div');
    list_div.classList.add('player_list');
    const main_div = document.getElementById('main');

    const player_div = document.createElement('div');
    player_div.classList.add('player_list_element');
    list_div.appendChild(player_div);

    const ple_info_div = document.createElement('div');
    ple_info_div.classList.add('ple_info');
    player_div.appendChild(ple_info_div);

    const a_id = document.createElement('a');
    a_id.classList.add('player_id');
    ple_info_div.appendChild(a_id);
    a_id.innerHTML = playerJSON.player.playerId;

    let ip_address = '';

    for (let j = 0; j < playerJSON.player.addresses.length; j++) {
        if (j !== 0)
            ip_address += ', ';

        ip_address += playerJSON.player.addresses[j].address
    }

    const a_ip_address = document.createElement('a');
    a_ip_address.classList.add('player_ip');
    ple_info_div.appendChild(a_ip_address);
    a_ip_address.innerHTML = ip_address;

    let nicknames = '';

    for (let j = 0; j < playerJSON.player.nicknames.length; j++) {
        if (j !== 0)
            nicknames += ' , ';

        nicknames += playerJSON.player.nicknames[j].nickname
    }

    const p_nicknames = document.createElement('p');
    p_nicknames.classList.add('player_nicknames');
    ple_info_div.appendChild(p_nicknames);
    p_nicknames.innerHTML = nicknames;

    const a_hwid = document.createElement('a');
    a_hwid.classList.add('player_hwid');
    ple_info_div.appendChild(a_hwid);
    if (playerJSON.player.hwid != null)
        a_hwid.innerHTML = playerJSON.player.hwid;
    else
        a_hwid.innerHTML = 'Неизвестен';

    main_div.appendChild(list_div);
}

function AddNewNote() {
    if (document.getElementById('newNote') != undefined)
        return;

    tabl = document.getElementById('taaable');
    const tr = tabl.insertRow();
    const td2 = tr.insertCell();

    var br = document.createElement("br");
    td2.appendChild(br);
    var br = document.createElement("br");
    td2.appendChild(br);

    td2.appendChild(document.createTextNode("Новая заметка: "));
    td2.id = "newNote";
    var br = document.createElement("br");
    td2.appendChild(br);
    noteText = document.createElement("input");
    noteText.type = "input";
    noteText.value = "Введите текст заметки";

    td2.appendChild(noteText);

    butSave = document.createElement("input");
    butSave.type = "button"
    butSave.value = "Сохранить"
    butSave.onclick = () => OnSaveClick(-1, noteText.value);
    td2.appendChild(butSave);

    butDel = document.createElement("input");
    butDel.type = "button";
    butDel.value = "Удалить";
    butDel.onclick = () => OnDelClick(-1, noteText.value);
    td2.appendChild(butDel);
}

function httpGet(theUrl) {
    var xmlHttp = new XMLHttpRequest({mozSystem: true});
    xmlHttp.open("GET", theUrl, false); // false for synchronous request
    //xmlHttp.setRequestHeader('Content-Type', 'application/json');
    //xmlHttp.getResponseHeader('Content-Type', 'application/json');
    //xmlHttp.responseType = 'json';
    xmlHttp.send();

    if (xmlHttp.responseText == "Unauthorized") {
        toLoginPage();
    }

    return xmlHttp;
}

function OnSaveClick(noteId, text) {
    if (noteId === -1) {
        req = servicesUrl + "/PlayersSite/v1/addNote?key=" + id + "&playerId=" + PlayerId + "&text=" + text;
        M = httpGet(req);
    } else {
        req = servicesUrl + "/PlayersSite/v1/editNote?key=" + id + "&note=" + noteId + "&text=" + text;
        M = httpGet(req);
    }
    RefreshPage();
}

function OnDelClick(noteId, text) {
    if (noteId != -1) {
        req = servicesUrl + "/PlayersSite/v1/delNote?key=" + id + "&note=" + noteId;
        M = httpGet(req);
    }
    RefreshPage();
}

function RefreshPage() {
    location.reload();
}

function toLoginPage() {
    loginPage = "login.html"
    location = loginPage;
    throw new Error();
}

function GoBack() {
    history.back()
}

function PrintS(s) {
    document.body.innerHTML += s;
}

function PrintP(p) {
    document.body.innerHTML += "\<img src=\"data:image\/png;base64," + p + "\"\/\>";
}

function PrintTIME(s, t1) {
    PrintS("<b><i>" + s + ": " + (new Date().getTime() - t1.getTime()) / 1000 + "</i></b> sec.<br>")
}

function PrintJSON(str, obj) {
    PrintS(str + ":<p>" + unescape(JSON.stringify(obj).replace(/\\u/g, '%u').replace(/</g, "&lt;").replace(/>/g, "&gt;")));
}

function PrintJSONb(str, obj) {
    PrintS(str + ":<p><pre>" + unescape(JSON.stringify(obj, null, 4).replace(/\\u/g, '%u').replace(/</g, "&lt;").replace(/>/g, "&gt;")) + "</pre>");
}

function PrintXML(str, obj) {
    PrintS(str + ":<p>" + obj.replace(/</g, "&lt;").replace(/>/g, "&gt;") + "<br>");
}

Load();