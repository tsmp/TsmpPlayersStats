let currentDate = new Date();
if (currentDate.getHours() >= 20 || currentDate.getHours() < 8) {
    console.log('Темная тема');
    //document.getElementById('main').style.background = 'black';
    //document.getElementById('main').style.color = 'white';
} else
    console.log('Светлая тема');