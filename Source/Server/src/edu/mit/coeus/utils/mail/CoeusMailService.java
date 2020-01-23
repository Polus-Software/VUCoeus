/*
 * @(#)CoeusMailService.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.utils.mail;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import java.util.Properties;
import java.util.Date;
import javax.mail.*;
import javax.activation.DataHandler;
import javax.mail.internet.*;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

/**
 * This class is used to send mails
 *
 * @author Geo Thomas
 * @version 1.0 Oct 12, 2006, 4:08 PM
 */

public class CoeusMailService implements MailPropertyKeys{
    
    private SetMailAttributes attrObj;
    private static Session mailSession;
    private InternetAddress[] toAddrs, ccAddrs, bccAddrs;
    private String mailer;
    
    public CoeusMailService(){}//constructor ends
    
    private Session configureMailServer(){
        Properties props = new Properties();
        props.put(MailProperties.getProperty(CMS_MAIL_PROTOCOL_KEY), 
                    MailProperties.getProperty(CMS_MAIL_PROTOCOL,"smtps"));
        props.put(MailProperties.getProperty(CMS_MAIL_HOST_KEY),
                    MailProperties.getProperty(CMS_MAIL_HOST));
        props.put(MailProperties.getProperty(CMS_MAIL_PORT_KEY),
                    MailProperties.getProperty(CMS_MAIL_PORT));
        props.put(MailProperties.getProperty(CMS_MAIL_AUTH_KEY),
                    MailProperties.getProperty(CMS_MAIL_AUTH,"true"));
        final String userId = MailProperties.getProperty(CMS_MAIL_USER_ID);
        final String password = MailProperties.getProperty(CMS_MAIL_PASSWORD);
        Authenticator auth = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(userId,password);
            };
        };
        return Session.getDefaultInstance(props,auth);
    }
    public void sendMessage() throws CoeusException {
        sendMessage(this.attrObj);
    }
    //Send Mail
    public void sendMessage(SetMailAttributes attrObj) throws CoeusException {

        if(!MailProperties.getProperty(CMS_ENABLED,"1").equals("1")) return;
        String cmsMode = MailProperties.getProperty(CMS_MODE);
        if(cmsMode==null || cmsMode.trim().toUpperCase().equals("T")){
            String testRecId = MailProperties.getProperty(CMS_TEST_MAIL_RECEIVE_ID);
            String dummyMsg = "\t-----------------------------------------------------\n"+
                                "\tTEST MODE\n"+
                            "\tIn Production mode this mail will be sent to "+attrObj.getTo()+"\n"+
                            "\t-----------------------------------------------------\n";

            String changedMsg = dummyMsg+attrObj.getMessage();
            attrObj.setMessage(changedMsg);
            attrObj.setTo(testRecId);
        }
        boolean isMailSent=false;
        MimeMessage message = null;
        try {
            if(mailSession==null) mailSession = configureMailServer();
            Transport transport = mailSession.getTransport();
            message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(MailProperties.getProperty(CMS_SENDER_ID)));
            if (attrObj.getSubject() != null)
                message.setSubject(attrObj.getSubject());
            
            if (attrObj.getMessage() != null) {
                message.setText(attrObj.getMessage());
            }
            message.setHeader("X-Mailer", mailer);
            
            message.setSentDate(new Date());
            message.setDataHandler( new DataHandler( new ByteArrayDataSource(attrObj.getMessage(),"text/plain")));
            
            
            if (attrObj.getTo() != null) {
                try {
                    toAddrs = InternetAddress.parse(attrObj.getTo(), false);
                    message.setRecipients(Message.RecipientType.TO, toAddrs);
                } catch  (MessagingException mex) {
                    throw new CoeusException("Error: Bad address in the TO address field.");
                } // Catch
            }else
                throw new CoeusException("No \"To\" address specified");
            
            if (attrObj.getCC() != null) {
                try {
                    ccAddrs = InternetAddress.parse(attrObj.getCC(), false);
                    message.setRecipients(Message.RecipientType.CC, ccAddrs);
                } catch  (MessagingException mex) {
                    throw new CoeusException("Error: Bad address in the CC address field.");
                } // Catch
            }
            
            if (attrObj.getBCC() != null) {
                try {
                    bccAddrs = InternetAddress.parse(attrObj.getBCC(), false);
                    message.setRecipients(Message.RecipientType.BCC, bccAddrs);
                } catch  (MessagingException mex) {
                    throw new CoeusException("Error: Bad address in the BCC address field.");
                } // Catch
            }
            
            if(attrObj.isAttachmentPresent()){
                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();
                // Fill the message
                messageBodyPart.setText(attrObj.getMessage());
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attrObj.getFileName());
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attrObj.getAttachmentName());
                multipart.addBodyPart(messageBodyPart);
                // Put parts in message
                message.setContent(multipart);
                message.saveChanges();
            }
            transport.connect();
            transport.sendMessage(message,toAddrs);
            transport.close();
        } catch (NoSuchProviderException mex) {
            mex.printStackTrace();
            UtilFactory.log(mex.getMessage(),mex,"CoeusMailService","sendMessage");
            throw new CoeusException(new edu.mit.coeus.bean.CoeusMessageResourcesBean().parseMessageKey("mail_general_exceptionCode.7505"));
        }catch (MessagingException mex) {
            mex.printStackTrace();
            UtilFactory.log(mex.getMessage(),mex,"CoeusMailService","sendMessage");
            throw new CoeusException(new edu.mit.coeus.bean.CoeusMessageResourcesBean().parseMessageKey("mail_general_exceptionCode.7505"));
        }
    }//method ends
    
    public static void main(String args[]) throws Exception{
        System.setProperty("javax.net.ssl.trustStore", "C:/Coeus/Keystore/coeus_mail");
        System.setProperty("javax.net.ssl.trustStorePassword", "coeus_mail");
        SetMailAttributes att = new SetMailAttributes();
        att.setSubject("Test Message from Javamail");
        att.setTo("bijulalgs@gmail.com");
        att.setMessage("This is a test message");
        new CoeusMailService().sendMessage(att);
        System.out.println("Done");
        
    }
    
}//Class ends