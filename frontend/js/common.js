// common
const servicesUrl = "http://194.147.90.72:8080/";

function toPage(pageUrl) {
	location = pageUrl;
	throw new Error();
}

function CreateElem(elemType, className, parent) {
    const newElem = document.createElement(elemType);
    newElem.className = className;

    if (parent !== undefined)
        parent.appendChild(newElem);

    return newElem;
}

function SetElemText(elemId, text) {
    document.getElementById(elemId).innerHTML = text;
}

function GetUrlParamValue(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

// header related
function toMainPage() {
    toPage("players.html");
}

function toRadminPage() {
    toPage("login.html");
}

function setHeaderHandlers() {
    console.log("set header handlers");
    //document.getElementById('header-main').onclick = () => toMainPage();
    //document.getElementById('radmins-func').onclick = () => toRadminPage();
}

setHeaderHandlers();

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
