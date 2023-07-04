let currentDate = new Date();
if (currentDate.getHours() >= 20 || currentDate.getHours() < 8) {
    console.log('Темная тема');
} else
    console.log('Светлая тема');