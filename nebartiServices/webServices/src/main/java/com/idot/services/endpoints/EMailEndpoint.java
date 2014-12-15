/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.services.endpoints;

import com.nebarti.dataaccess.domain.EmailMessage;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 */
@Path("/email")
public class EMailEndpoint {
    public static final Logger logger = Logger.getLogger(EMailEndpoint.class.getName());
    
    /** 
     * Create new database and collections for new model data.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendEMail(String emailMessageJSON) {
        URI uri = null;
        EmailMessage emailMessage = new EmailMessage();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            emailMessage = mapper.readValue(emailMessageJSON, EmailMessage.class);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        } 
        
        sendEmail(emailMessage);
        
        return Response.created(uri).build();
    }

    private void sendEmail(EmailMessage emailMessage) {
        String SMTP_HOST_NAME = "smtp.gmail.com";
        int SMTP_HOST_PORT = 465;
        String USER = "kevin.hartig@nebarti.com";
        String PASSWORD = "A7imil@t0r";
        
        
      String to = "support@nebarti.com"; // Recipient's email address
      String from = emailMessage.getEmailAddress(); // Sender's email address
      String host = "smtp.googlemail.com"; // smtp host

      Properties properties = new Properties();      
      properties.put("mail.smtp.host", host); // Set mail server
      properties.put("mail.smtps.auth", "true");
      properties.put("mail.smtp.starttls.enable", "true");
      properties.put("mail.transport.protocol", "smtps");
      
      Session session = Session.getDefaultInstance(properties);
      Transport transport = null;
      try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
         message.setSubject("Feedback Message from Nebarti website");
         message.setContent(
                 "Name: " + emailMessage.getName() + "\n\n" +
                 "Email: " + from + "\n\n" + 
                 "Phone: " + emailMessage.getPhone() + "\n\n" +
                 "Feedback Type: " + emailMessage.getFeedbackType() + "\n\n" +
                 "Message: " + "\n" + emailMessage.getMessage(), "text/plain");

         transport = session.getTransport();
         transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, USER, PASSWORD);
         transport.sendMessage(message, message.getAllRecipients()); // Send message
      }catch (MessagingException mex) {
         logger.log(Level.WARNING, mex.getLocalizedMessage(), mex);
      } finally {
          if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException ex) {
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                }
          }
      }
    }
    
    public static void main(String [] args) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setEmailAddress("joe@place.com");
        emailMessage.setMessage("test message");
        emailMessage.setName("joe blow");
        
        EMailEndpoint service = new EMailEndpoint();
        service.sendEmail(emailMessage);
    }
}
