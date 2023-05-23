package com.example.gmailserver.functionGmail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GmailQuickStart {
    public static void sendGmail(String sendTo,String massage){
        String subject="musicFlyProject";
        try {
            EmailSender.setSender("musicflyproject@gmail.com");
            EmailSender.init("/Client.json");
            EmailSender.sendEmail(sendTo,subject,massage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
