$(document).ready(function() {  // <-- Readable ready function
//$(function() { // <-- Short version of ready function
    $("#submitContactForm").submit(function(event){
        alert("I got called");
        event.preventDefault(); //prevent default action
        var post_url = $(this).attr("action"); //get form action url
        var request_method = $(this).attr("method"); //get form GET/POST method
        var form_data = $(this).serialize(); //Encode form elements for submission

        $.ajax({
            url : post_url,
            type: request_method,
            data : form_data
        }).done(function(data){
            alert("Response submitted!");
            //$('#result').html(data.message);
            $('#formContact').html(data.message);
        }).fail(function(data){
            alert("Please input all fields");
            $('#result').html("");
        });
    });
});


