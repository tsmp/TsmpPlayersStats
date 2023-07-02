const searchKey = "search"
const pageKey = "page"
const urlParams = new URLSearchParams(window.location.search);
id = urlParams.get("id")
maxPage = 1
curPage = 1

var Load = function () {
    // Если не вошли!!!
    if (id == null) {
        toLoginPage();
    }

    // Добавить в url страницу и текст для поиска если их нет
    search = urlParams.get(searchKey)
    needToChangeUrl = false;

    if (search == null) {
        urlParams.append(searchKey, "")
        needToChangeUrl = true
    }

    curPage = urlParams.get(pageKey)

    if (curPage == null) {
        urlParams.append(pageKey, "1")
        needToChangeUrl = true
    }

    if (needToChangeUrl == true) {
        window.location.search = urlParams.toString()
        throw new Error()
    }

    // Установить имя пользователя
    nameRequest = "http://194.147.90.72:8080/PlayersSite/v1/MyName?key=" + id;
    user = httpGet(nameRequest);
    document.getElementById('user_name').innerHTML = user.responseText

    searchReq = "http://194.147.90.72:8080/PlayersSite/v1/SearchPlayers?key=" + id + "&search=" + encodeURIComponent(search) + "&page=" + curPage
    //alert(searchReq)
    M = httpGet(searchReq)


    //PrintS(M.responseText)
    //PrintS(M.);

    var J = JSON.parse(M.responseText);

    //PrintJSON("res",J);
    CreatePlayersList(J);
    SetSeachOnPressEnter();


    // ------------------------------------------------------------------------------- //
    window.addEventListener('load', function () {
        const load_screen = document.getElementById('load_screen');
        const page = document.getElementById('body');
        load_screen.style.display = 'none';
        page.style.animation = 'page_init_anim 0.5s ease-in-out forwards';
    });

    const a_page_nums = document.querySelectorAll('a.page_num');
    a_page_nums.forEach((a) => {
        a.textContent = "Страница " + curPage + " из " + maxPage;
    });

    const search_button = document.getElementById('search');
    search_button.addEventListener('click', function () {
        this.style.animation = 'search_icon_anim 0.3s forwards';
    });

    document.getElementById('search').addEventListener('click', function () {
        var form = document.getElementById('menu_search_form');
        Search(form);
    });

// ------------------------------------------------------------------------------- //

}

function SetSeachOnPressEnter() {
    var input = document.getElementById("search_field");
    input.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            document.getElementById("search").click();
        }
    });
}

function CreatePlayersList(playersJSON) {var targetDiv = document.getElementById("myDiv");

    const tbl = document.createElement('table');
    const main_div = document.getElementById('main');
    const last_nav = document.getElementById('last_nav');

    resultsCnt = playersJSON.resultsCnt
    document.getElementById('results_cnt').innerHTML = "Найдено игроков: " + resultsCnt

    pagesCnt = playersJSON.pagesCnt
    maxPage = pagesCnt
    firstNumber = playersJSON.firstNumber

    tbl.style.width = '100%';
    tbl.style.border = '1px solid black';
    spaces = '\xa0\xa0\xa0\xa0\xa0\xa0\xa0';

    for (let i = 0; i < playersJSON.players.length; i++) {
        const tr = tbl.insertRow();
        const td = tr.insertCell();
        tr.id = playersJSON.players[i].playerId

        num = firstNumber + i + 1
        text = num + ". Ники: "

        for (let j = 0; j < playersJSON.players[i].nicknames.length; j++) {
            if (j != 0)
                text = text + "," + spaces;

            text = text + playersJSON.players[i].nicknames[j]
        }

        td.appendChild(document.createTextNode(text));
        var br = document.createElement("br");
        td.appendChild(br);
        var br = document.createElement("br");
        td.appendChild(br);

        text = "ip: ";

        for (let j = 0; j < playersJSON.players[i].addresses.length; j++) {
            if (j != 0)
                text = text + "," + spaces;

            text = text + playersJSON.players[i].addresses[j]
        }

        td.appendChild(document.createTextNode(text));
        var br = document.createElement("br");
        td.appendChild(br);
        var br = document.createElement("br");
        td.appendChild(br);

        text = "hwid: " + playersJSON.players[i].hwid;
        td.appendChild(document.createTextNode(text));

        td.style.borderBottom = '1px solid black';
        td.style.padding = '10px'
        td.style.overflowWrap = 'anywhere'

        td2 = tr.insertCell();
        td2.className = "info_player"

        buttonInfo = document.createElement("input");
        buttonInfo.type = "button"
        buttonInfo.value = "Инфо"
        buttonInfo.className = "player_info_button"

        buttonInfo.onclick = function () {
            PlayerInfo(tr.id);
        };

        td2.appendChild(buttonInfo)

        //<input type="button" name="info" value="Главная" onClick="MainPage()">
    }

    main_div.insertBefore(tbl, last_nav);
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

function toLoginPage() {
    loginPage = "login.html"
    location = loginPage;
    throw new Error();
}

function MainPage() {
    urlParams.set(searchKey, "")
    urlParams.set(pageKey, 1)

    window.location.search = urlParams.toString()
    throw new Error()
}

function Search(form) {
    val = form.search_field.value;
    console.log(val);
    urlParams.set(searchKey, val);
    urlParams.set(pageKey, "1");

    window.location.search = urlParams.toString()
    throw new Error()
}

function ChangePage(form) {
    val = form.page_input.value

    if (typeof (val) != "string" || isNaN(val) || isNaN(parseFloat(val)) || val < 1 || val > pagesCnt) {
        alert("Введена недопустимая страница!")
        return;
    }

    urlParams.set(pageKey, val);
    window.location.search = urlParams.toString()
    throw new Error()
}

function PrevPage(form) {
    val = curPage
    val--

    if (val < 1 || val > pagesCnt) {
        return;
    }

    urlParams.set(pageKey, val);
    window.location.search = urlParams.toString()
    throw new Error()
}

function NextPage(form) {
    val = curPage
    val++

    if (val < 1 || val > pagesCnt) {
        return;
    }

    urlParams.set(pageKey, val);
    window.location.search = urlParams.toString()
    throw new Error()
}

function PlayerInfo(PlayerId) {
    //alert(PlayerId)
    infoPage = "info.html"
    idArg = "?id=" + id + "&pl=" + PlayerId
    location = infoPage + idArg;
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
