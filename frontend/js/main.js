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

    const a_page_nums = document.querySelectorAll('a.page_num');
    a_page_nums.forEach((a) => {
        a.textContent = curPage + " / " + maxPage;
    });

    const search_button = document.getElementById('search');
    search_button.addEventListener('click', function () {
        this.style.animation = 'search_icon_anim 0.3s forwards';
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

function CreatePlayersList(playersJSON) {
    const list_div = document.createElement('div');
    list_div.classList.add('player_list');
    const main_div = document.getElementById('main');
    const last_nav = document.getElementById('last_nav');
    resultsCnt = playersJSON.resultsCnt
    document.getElementById('results_cnt').innerHTML = "Найдено игроков: " + resultsCnt

    pagesCnt = playersJSON.pagesCnt
    maxPage = pagesCnt
    firstNumber = playersJSON.firstNumber

    for (let i = 0; i < playersJSON.players.length; i++) {
        const player_div = document.createElement('div');
        player_div.classList.add('player_list_element');
        list_div.appendChild(player_div);

        const ple_info_div = document.createElement('div');
        ple_info_div.classList.add('ple_info');
        player_div.appendChild(ple_info_div);

        const a_id = document.createElement('a');
        a_id.classList.add('player_id');
        ple_info_div.appendChild(a_id);
        a_id.innerHTML = playersJSON.players[i].playerId;

        let ip_address = '';

        for (let j = 0; j < playersJSON.players[i].addresses.length; j++) {
            if (j !== 0)
                ip_address += ', ';

            ip_address += playersJSON.players[i].addresses[j];
        }

        const a_ip_address = document.createElement('a');
        a_ip_address.classList.add('player_ip');
        ple_info_div.appendChild(a_ip_address);
        a_ip_address.innerHTML = ip_address;

        let nicknames = '';

        for (let j = 0; j < playersJSON.players[i].nicknames.length; j++) {
            if (j !== 0)
                nicknames += ' , ';

            nicknames += playersJSON.players[i].nicknames[j];
        }

        const p_nicknames = document.createElement('p');
        p_nicknames.classList.add('player_nicknames');
        ple_info_div.appendChild(p_nicknames);
        p_nicknames.innerHTML = nicknames;

        const a_hwid = document.createElement('a');
        a_hwid.classList.add('player_hwid');
        ple_info_div.appendChild(a_hwid);
        if (playersJSON.players[i].hwid != null)
            a_hwid.innerHTML = playersJSON.players[i].hwid;
        else
            a_hwid.innerHTML = 'Неизвестен';

        const button_info = document.createElement('button');
        button_info.classList.add('player_info');
        player_div.appendChild(button_info);
        button_info.innerHTML = 'Информация \u2192';
        button_info.onclick = function () {
            PlayerInfo(a_id.innerHTML);
        };
    }

    main_div.insertBefore(list_div, last_nav);
}

function httpGet(theUrl) {
    var xmlHttp = new XMLHttpRequest({mozSystem: true});
    xmlHttp.open("GET", theUrl, false); // false for synchronous request
    //xmlHttp.setRequestHeader('Content-Type', 'application/json');
    //xmlHttp.getResponseHeader('Content-Type', 'application/json');
    //xmlHttp.responseType = 'json';
    xmlHttp.send();

    if (xmlHttp.responseText === "Unauthorized") {
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
