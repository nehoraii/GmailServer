package com.example.gmailserver.functionGmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;

public class EmailSender {

    protected static String client_url_permission;
    private static Credential CREDENTIAL;
    private static String Sender;

    private static NetHttpTransport HTTP_TRANSPORT;

    private static GsonFactory gsonFactory;

    private static boolean is_init=false;
    public static void setSender(String sender){
        Sender=sender;
    }


    /**
     *
     * @param client_url_permission
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static void init(String client_url_permission) throws GeneralSecurityException, IOException {
        EmailSender.HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        EmailSender.gsonFactory=GsonFactory.getDefaultInstance();
        EmailSender.client_url_permission =client_url_permission;
        EmailSender.CREDENTIAL =getCredentials();
        is_init=true;
    }
    private static Credential getCredentials() throws IOException, GeneralSecurityException {
        // Load client secrets.
        InputStream in = GmailQuickStart.class.getResourceAsStream(client_url_permission);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(gsonFactory, new InputStreamReader(in));

        List<String> SCOPES = List.of(GmailScopes.GMAIL_SEND);
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, gsonFactory, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        EmailSender.CREDENTIAL =credential;
        return credential;
    }



    public static void sendEmail(String to,String subject, String massage) throws GeneralSecurityException, IOException, MessagingException {
        if(!is_init){

            throw new RuntimeException("the class not init");
        }
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, gsonFactory,CREDENTIAL)
                .setApplicationName("GmailTest")
                .build();

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(Sender));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(massage);
        ByteArrayOutputStream buffer =new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMassegeBytes =buffer.toByteArray();
        String encodeEmail= Base64.encodeBase64URLSafeString(rawMassegeBytes);
        Message masg=new Message();
        masg.setRaw(encodeEmail);

        masg=service.users().messages().send("me",masg).execute();
        System.out.println("the id of message :"+masg.getId());
        System.out.println(masg.toPrettyString());
    }

}

