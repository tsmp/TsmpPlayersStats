function toMainPage() {
    let targetPage = "players.html"
	location = targetPage;
	throw new Error();
}

function toRadminPage() {
    let targetPage = "login.html"
	location = targetPage;
	throw new Error();
}

function setHeaderHandlers() {
    document.getElementById('header-main').onclick = () => toMainPage();
    document.getElementById('radmins-func').onclick = () => toRadminPage();
}

window.onload = function(e) { 
    setHeaderHandlers();
}
