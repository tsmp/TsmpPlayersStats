function setCookie(name, value, hours) {
    var expires = "";
    if (hours) {
        var date = new Date();
        date.setTime(date.getTime() + (hours * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function getCookie(name) {
    var cookieName = name + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(';');

    for (var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i];
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(cookieName) === 0) {
            return cookie.substring(cookieName.length, cookie.length);
        }
    }

    return null;
}


setCookie("theme", "light", 1);
var currentDate = new Date();
if (currentDate.getHours() >= 20 || currentDate.getHours() < 8) {
    dark_theme_enabled = true;
    const style_script_block = document.getElementById("dark_preloader_style");
    const style_block = document.createElement("style");
    style_block.innerHTML = style_script_block.innerHTML;

    for (var i = 0; i < style_script_block.attributes.length; i++) {
        var attr = style_script_block.attributes[i];
        style_block.setAttribute(attr.name, attr.value);
    }
    style_script_block.parentNode.replaceChild(style_block, style_script_block);
    document.getElementById('link_dark_theme').href = '../css/dark_theme.css';
    setCookie("theme", "dark", 1);
}

document.getElementById('theme_switcher').addEventListener('click', function () {
    if (getCookie("theme") === "dark") {
        document.getElementById('link_dark_theme').href = '';
        dark_theme_enabled = false;
        setCookie("theme", "light", 1);
    } else {
        document.getElementById('link_dark_theme').href = '../css/dark_theme.css';
        dark_theme_enabled = true;
        setCookie("theme", "dark", 1);
    }
});