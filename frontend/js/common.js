// common
const servicesUrl = "http://194.147.90.72:8080/";

function toPage(pageUrl) {
	location = pageUrl;
	throw new Error();
}

// header related
function toMainPage() {
    toPage("players.html");
}

function toRadminPage() {
    toPage("login.html");
}

function setHeaderHandlers() {
    document.getElementById('header-main').onclick = () => toMainPage();
    document.getElementById('radmins-func').onclick = () => toRadminPage();
}

window.onload = function(e) { 
    setHeaderHandlers();
}

// debug
function PrintS(s) { document.body.innerHTML += s; }
function PrintJSON (str,obj) { PrintS( str + ":<p>"      + unescape(JSON.stringify(obj).replace(/\\u/g, '%u').replace(/</g,"&lt;").replace(/>/g,"&gt;"))); }

// requests
function httpGet(theUrl)
{
    let xmlHttp = new XMLHttpRequest({mozSystem: true});
    xmlHttp.open( "GET", theUrl, false ); // false for synchronous request
    xmlHttp.send();
    return xmlHttp;
}
