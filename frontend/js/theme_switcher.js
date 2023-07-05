let currentDate = new Date();
if (currentDate.getHours() >= 20 || currentDate.getHours() < 8) {
    const style_script_block = document.getElementById("dark_preloader_style");
    const style_block = document.createElement("style");
    style_block.innerHTML = style_script_block.innerHTML;

    for (var i = 0; i < style_script_block.attributes.length; i++) {
        var attr = style_script_block.attributes[i];
        style_block.setAttribute(attr.name, attr.value);
    }
    style_script_block.parentNode.replaceChild(style_block, style_script_block);
    document.getElementById('link_dark_theme').href = '../css/dark_theme.css';
}