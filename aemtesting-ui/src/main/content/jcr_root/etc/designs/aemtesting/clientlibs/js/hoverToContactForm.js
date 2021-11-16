$(document).ready(function() {  // <-- Readable ready function
//$(function() { // <-- Short version of ready function
    var target = $('#formContact');
    if (target.length) {
        $('html,body').animate({
            scrollTop: target.offset().top
        }, 1000);
        return false;
    }
});


