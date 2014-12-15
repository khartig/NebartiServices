<?php
if($_POST){
    $name = $_POST['name'];
    $email = $_POST['email'];
    $message = $_POST['message'];
    $feedbacktype = $_POST['feedbacktype'];
    $headers = "From: kevin.hartig@nebarti.com";
    
    ini_set("SMTP", "localhost");
    ini_set("sendmail_from", "kevin.hartig@nebarti.com");
    ini_set("smtp_port", "25");
    
    foreach($_REQUEST as $fields => $value) 
        if(eregi("TO:", $value) || eregi("CC:", $value) || eregi("CCO:", $value) || eregi("Content-Type", $value)) 
            exit("ERROR: Code injection attempt denied! Please don't use the following sequences in your message: 'TO:', 'CC:', 'CCO:' or 'Content-Type'."); 

//send email
    mail("kevin.hartig@nebarti.com", "Nebarti website comment from ".$email, $message, $headers);
}
?>