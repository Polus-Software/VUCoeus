/*
 * Notification.java
 *
 * Created on September 5, 2008, 10:28 AM
 *
 */

package edu.mit.coeus.mailaction.bean;

import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import java.util.HashMap;
//import java.util.HashMap;

/**
 *
 * @author noorula
 *Removed unnecessary interfaces for case#4197
 */
  public interface Notification{
      
      /**
       * Method to send the email notification
       * @param mailActionInfoBean 
       * @throws java.lang.Exception
       * @return boolean
       */
      public boolean sendNotification(MailMessageInfoBean mailMessageInfoBean)throws Exception;
      public boolean sendNotification(int actionId, String moduleItemKey, int moduleItemKeySequence,MailMessageInfoBean mailMessageInfoBean) throws Exception;
      /**
       * Method to get the notification defined for a particular action performed in a particular module
       * @param actionId
       * @throws java.lang.Exception
       * @return MailActionInfoBean
       */
      public MailMessageInfoBean prepareNotification(int actionId ) throws Exception;
      public MailMessageInfoBean prepareNotification(int actionId,String moduleItemKey , int moduleItemKeySeq) throws Exception;
      /**
       * Method to get the data required to show the module information
       * @param moduleItemKey
       * @param moduleItemKeySequence
       * @throws java.lang.Exception
       * @return HashMap
       */
      // COEUSQA-2105: No notification for some IRB actions
//      public HashMap getNotificationData(String moduleItemKey, int moduleItemKeySequence)throws Exception;
  }
