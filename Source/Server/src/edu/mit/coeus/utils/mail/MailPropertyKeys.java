/*
 * MailPropertyKeys.java
 *
 * Created on October 12, 2006, 11:21 AM
 */

package edu.mit.coeus.utils.mail;

/**
 *
 * @author  geot
 */
public interface MailPropertyKeys {
    public static final String CMS_MAIL_HOST_KEY = "CMS_MAIL_HOST_KEY";
    public static final String CMS_MAIL_PORT_KEY = "CMS_MAIL_PORT_KEY";
    public static final String CMS_MAIL_AUTH_KEY = "CMS_MAIL_AUTH_KEY";
    public static final String CMS_MAIL_AUTH = "CMS_MAIL_AUTH";
    public static final String CMS_MAIL_PROTOCOL_KEY = "CMS_MAIL_PROTOCOL_KEY";
    public static final String CMS_MAIL_PROTOCOL = "CMS_MAIL_PROTOCOL";
    public static final String CMS_MAIL_USER_ID = "CMS_MAIL_USER_ID";
    public static final String CMS_MAIL_PASSWORD = "CMS_MAIL_PASSWORD";
    public static final String CMS_MAIL_HOST = "CMS_MAIL_HOST";
    public static final String CMS_MAIL_PORT = "CMS_MAIL_PORT";
    public static final String CMS_MODULE_FOOTER = "CMS_MODULE_FOOTER";
    public static final String CMS_MAIL_FOOTER = "CMS_MAIL_FOOTER";
    
    /*
     *OSP$PARAMETER parameter keys
     */
    public static final String CMS_ENABLED = "CMS_ENABLED";
    public static final String CMS_MODE = "CMS_MODE";
    public static final String CMS_TEST_MAIL_RECEIVE_ID = "CMS_TEST_MAIL_RECEIVE_ID";
    public static final String CMS_SENDER_ID = "CMS_SENDER_ID";
    public static final String CMS_REPLY_TO_ID = "CMS_REPLY_TO_ID";
    public static final String CMS_DEFAULT_DOMAIN = "CMS_DEFAULT_DOMAIN";
    
    /*
     * Customizable mail Texts
     *COEUSDEV-75: Email Engine redesign
     */
    public static final String DEFAULT_PROTOCOL_MODULE_FOOTER = "DEFAULT_PROTOCOL_MODULE_FOOTER";
    public static final String DEFAULT_IACUC_PROTOCOL_MODULE_FOOTER = "DEFAULT_IACUC_PROTOCOL_MODULE_FOOTER";
    public static final String DEFAULT_PROPOSAL_MODULE_FOOTER = "DEFAULT_PROPOSAL_MODULE_FOOTER";
    public static final String DEFAULT_AWARD_MODULE_FOOTER = "DEFAULT_AWARD_MODULE_FOOTER";
    public static final String DEFAULT_IP_MODULE_FOOTER = "DEFAULT_IP_MODULE_FOOTER";
    public static final String DEFAULT_PERSON_MODULE_FOOTER = "DEFAULT_IP_MODULE_FOOTER";
    public static final String DEFAULT_SUBCONTRACT_MODULE_FOOTER = "DEFAULT_SUBCONTRACT_MODULE_FOOTER";
    public static final String PROPOSAL_NOTIFICATION = "proposalNotification";
    public static final String PROPOSAL_INST_NOTIFICATION = "IPNotification";
    public static final String AWARD_NOTIFICATION = "awardNotification";
    public static final String IRB_NOTIFICATION = "IRBNotification";
    public static final String IACUC_NOTIFICATION = "IACUCNotification";
    public static final String PERSON_NOTIFICATION = "personNotification";
    public static final String SUBCONTRACT_NOTIFICATION = "subcontractNotification";
    public static final String DISCLOSURE_NOTIFICATION = "DisclosureNotification";
    public static final String DEFAULT_DISCLOSURE_MODULE_FOOTER = "DEFAULT_DISCLOSURE_MODULE_FOOTER";
    public static final String DOT = ".";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    public static final String FOOTER = "footer";
    public static final String MESSAGE = "message";
    public static final String SUFFIX  = "suffix";

    //COEUSDEV - 733 Create a new notification for negotiation module - Start
    public static final String DEFAULT_NEGOTIATION_MODULE_FOOTER = "DEFAULT_NEGOTIATION_MODULE_FOOTER";
    public static final String NEGOTIATION_NOTIFICATION = "negotiationNotification";
    //COEUSDEV - 733 Create a new notification for negotiation module - End
    
}
