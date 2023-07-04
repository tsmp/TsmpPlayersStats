window.addEventListener('load', function () {
    const load_screen = document.getElementById('load_screen');
    const page = document.getElementById('body');
    load_screen.style.display = 'none';
    page.style.animation = 'page_init_anim 0.3s ease-in-out forwards';
});