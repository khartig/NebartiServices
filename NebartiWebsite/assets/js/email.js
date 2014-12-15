$(document).ready(function() {
        $('#cancelBtn').click(function(event) {
          $('#formFeedback').trigger("reset");
        });
        
        $('#sendBtn').click(function(event) {
          var data = {
            name: $("#name").val(),
            email: $("#email").val(),
            feedbacktype: $("#feedbacktype").val(),
            message: $("#message").val()
          };
          
          $.ajax({
            type: "POST",
            url: "email.php",
            data: data,
            success: function(data, textStatus, jqXHR) {
              alert("Message sent successfully");
              $('#contact').modal('hide');
            },
            error: function(jqXHR, textStatus, errorThrown) {
              alert('Message send error: ' + textStatus);
              $('#contact').modal('hide')
            }
          });
          
          $('#formFeedback').trigger("reset");
        
        });
      
});