/*
 * SendMailService.java
 *
 * Created on May 25, 2007, 4:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mail.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.mail.MailProperties;
import edu.mit.coeus.utils.mail.MailPropertyKeys;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 *
 * @author talarianand
 */

public class SendMailService implements MailPropertyKeys {
    // COEUSQA-2105: No notification for some IRB actions
    // private String mailer;
    //private ArrayList arList;
    //Added for COEUSQA-2421 : CLONE -Email notification parameters should be taken from parameter table - Start
    private static final String EMPTY_STRING = "";
    //COEUSQA-2421 : End
    
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
    
    public void postMail(MailAttributes mailAttr) throws MessagingException {
        boolean debug = false;
        List lstRecipients = null;
        if(!MailProperties.getProperty(CMS_ENABLED,"1").equals("1")) return;

        //Modified for COEUSQA-2421 : CLONE -Email notification parameters should be taken from parameter table - Start
        //CMS_MODE and CMS_TEST_MAIL_RECEIVE_ID are fetched from the property file
//        String testRecId = MailProperties.getProperty(CMS_TEST_MAIL_RECEIVE_ID);
//        String cmsMode = MailProperties.getProperty(CMS_MODE);
//        if(cmsMode==null || cmsMode.trim().toUpperCase().equals("T")){
          CoeusFunctions coeusFunctions = new CoeusFunctions();
          String testRecId = EMPTY_STRING;
          String cmsMode = EMPTY_STRING;
            try{
                testRecId = coeusFunctions.getParameterValue(CMS_TEST_MAIL_RECEIVE_ID);
                if(testRecId == null){
                    testRecId = EMPTY_STRING;
                }
                cmsMode = coeusFunctions.getParameterValue(CMS_MODE);
            }catch(DBException dbE){
                UtilFactory.log(dbE.getMessage(),dbE,"CoeusMailService","sendMessage");
            }catch(CoeusException cE){
                    UtilFactory.log(cE.getMessage(),cE,"CoeusMailService","sendMessage");
            }
         if(cmsMode==null || cmsMode.trim().toUpperCase().equals("T")){   //COEUSQA-2421 : End
            lstRecipients = new ArrayList();
            PersonRecipientBean recipient = new PersonRecipientBean();
            recipient.setEmailId(testRecId);
            recipient.setToOrCC('T');
            lstRecipients.add(recipient);
//            String dummyMsg = " -----------------------------------------------------------\n"+
//                                " TEST MODE\n"+
//                            " In Production mode this mail will be sent to "+mailAttr.getRecipients().toString()+"\n"+
//                            " -----------------------------------------------------------\n";

        String dummyMsg = " -----------------------------------------------------------\n"+
                " TEST MODE\n"+
                " In Production mode this mail will be sent to "+getMailIdsOfRecipients(mailAttr.getRecipients())+"\n"+
                " -----------------------------------------------------------\n";
            
            String changedMsg = dummyMsg+mailAttr.getMessage();
            mailAttr.setMessage(changedMsg);
            mailAttr.setRecipients(lstRecipients); 
        }
        
//        Session session = Session.getDefaultInstance(props, auth);
        Session session = configureMailServer();
        
        session.setDebug(debug);
        
        Message msg = new MimeMessage(session);
        
        // set the from and to address
        if(mailAttr.getFrom() != null) {
            InternetAddress addressFrom = new InternetAddress(mailAttr.getFrom());
            msg.setFrom(addressFrom);
        } else {
            InternetAddress addressFrom = new InternetAddress(MailProperties.getProperty(CMS_SENDER_ID));
            msg.setFrom(addressFrom);
        }
        // COEUSQA-2105: No notification for some IRB actions
        if(mailAttr.getRecipients() != null) {
            assignRecipients(msg, mailAttr);
        }
        
//        if(mailAttr.getCc() != null) {
//            recipients = mailAttr.getCc();
//            //InternetAddress[] addressCc = new InternetAddress[recipients.length];
//            for(int i = 0; i < recipients.length; i++) {
//                if(recipients[i] != null) {
//                    ccAddr = InternetAddress.parse(recipients[i], false);
//                    msg.setRecipients(Message.RecipientType.CC, ccAddr);
//                    //addressCc[i] = new InternetAddress(recipients[i]);
//                }
//            }
//            //msg.setRecipients(Message.RecipientType.CC, addressCc);
//        }
        
        if(mailAttr.getSubject() != null) {
            msg.setSubject(mailAttr.getSubject());
        }
        
        if(mailAttr.isAttachmentPresent()) {
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            // Fill the message
            messageBodyPart.setText(mailAttr.getMessage());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            // Part two is attachment
            String fileName = mailAttr.getFileName();
            String attachName = mailAttr.getAttachmentName();
            String[] fileList = fileName.split(";");
            String[] attachList = attachName.split(";");
            for(int index = 0; index < fileList.length; index++) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(fileList[index]);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attachList[index]);
                multipart.addBodyPart(messageBodyPart);
            }
            // Put parts in message
            msg.setSentDate(new Date());
            msg.setContent(multipart);
            msg.saveChanges();
        } else {
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mailAttr.getMessage());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            msg.setHeader("X-Mailer", null);
            msg.setSentDate(new Date());
            msg.setContent(multipart);
            msg.saveChanges();
        }
        
        Transport.send(msg);
    }
    // COEUSQA-2105: No notification for some IRB actions
    private void  assignRecipients(Message msg, MailAttributes mailAttr){
        List lstRecipients = mailAttr.getRecipients();
        //recipients = mailAttr.getTo();
        List toList = new ArrayList();
        for (int i = 0; i < lstRecipients.size(); i++) {
            PersonRecipientBean recipient = (PersonRecipientBean) lstRecipients.get(i);
            String emailId = recipient.getEmailId();
            if(emailId != null && !"".equals(emailId)) {
                String name = recipient.getPersonName();
                try {
                if (name!= null && !"".equals(name)){
                    toList.add( new InternetAddress(emailId,name));
                }else {
                    toList.add( new InternetAddress(emailId));
                }
                } catch(Exception ex){
                    UtilFactory.log("Could not add "+name+" to recipient list.", ex, "SendMailService", "assignRecipients" );
                }
            }
        }
        try {
            InternetAddress[] toAddress = (InternetAddress[])toList.toArray(new InternetAddress[lstRecipients.size()]);
            msg.setRecipients(Message.RecipientType.TO, toAddress);
        } catch (MessagingException ex) {
            UtilFactory.log( ex.getMessage(), ex, "SendMailService", "assignRecipients" );
        }
        
    }
    
    private String getMailIdsOfRecipients(List lstPersonDetails) {
        StringBuilder mailIds = new StringBuilder("[ ");
        if(lstPersonDetails != null && lstPersonDetails.size() >0){
            Iterator itr = lstPersonDetails.iterator();
            while(itr.hasNext()){
                PersonRecipientBean personBean =  (PersonRecipientBean) itr.next();
                if(personBean != null && personBean.getEmailId() != null
                        && !"".equals(personBean.getEmailId())){
                    mailIds.append(personBean.getEmailId()+" ");
                }
            }
        }
        mailIds.append(" ]");
        return mailIds.toString();
    }

}
