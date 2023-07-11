function pasuser(form) {
    logg = document.getElementById('login');
    psww = document.getElementById('password');

    if (logg.value == "") {
        alert("Введите логин!")
    } else if (psww.value == "") {
        alert("Введите пароль!")
    } else {
        log = logg.value
        psw = psww.value

        url = "http://194.147.90.72:8080/PlayersSite/v1/Authorize"
        loginParam = "?login="
        passwordParam = "&password="

        req = url + loginParam + encodeURIComponent(log) + passwordParam + encodeURIComponent(psw)
        //alert(req)

        M = httpGet(req)
        //alert(M.responseText)

        resp = M.responseText
        id = "id: "

        if (resp.includes(id)) {
            idCode = resp.substring(id.length, resp.length);
            //alert(idCode)
            loc = "main.html?id=" + idCode
            location = loc
        } else {
            alert("Неверный логин или пароль")
        }
    }
}

function httpGet(theUrl) {
    var xmlHttp = new XMLHttpRequest({mozSystem: true});
    xmlHttp.open("GET", theUrl, false); // false for synchronous request
    //xmlHttp.setRequestHeader('Content-Type', 'application/json');
    //xmlHttp.getResponseHeader('Content-Type', 'application/json');
    //xmlHttp.responseType = 'json';
    xmlHttp.send();

    return xmlHttp;
}