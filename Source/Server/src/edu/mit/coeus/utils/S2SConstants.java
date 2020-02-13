/*
 * S2SConstants.java
 *
 * Created on October 19, 2004, 1:07 PM
 */

package edu.mit.coeus.utils;

/**
 *
 * @author  geot
 */
public interface S2SConstants {
    public static final char GET_OPPORTUNITY_LIST = 'G';
    public static final char SUBMIT_FORM_FIRST = 'F';
    public static final char SUBMIT_FORM_WITH_FORM_URL_LIST = 'S';
    public static final char SUBMIT_FORM_WITH_SCHEMA = 'W';
    public static final char GET_APPLICATION_LIST = 'A';
    public static final char VALIDATE_APPLICATION = 'V';
    public static final char SAVE_OPPORTUNITY = 'O';
    public static final char SAVE_OPP_FORMS = 'R';
    public static final char SAVE_FORMS_N_SUBMIT_APP = 'N';
    public static final char SAVE_GRANTS_GOV = 'B';
    public static final char GET_DATA = 'D';
    public static final char GET_OPPORTUNITY = 'P';
    public static final char REFRESH_GRANTS_DATA = 'T';
    public static final char IS_S2S_CANDIDATE = 'I';
    public static final char GET_STATUS_DETAIL = 'L';
    public static final char PRINT_FORM = 'M';
    public static final char DELETE_OPPORTUNITY = 'C';
    public static final char GET_XML_FROM_PURE_EDGE = 'E';
    public static final char CHECK_FORMS_AVAILABLE = 'H';
    
    public static final char AUTO_SUBMISSION = 'Z';    
    //Right Ids
    public static final String SUBMIT_TO_SPONSOR = "SUBMIT_TO_SPONSOR";
    public static final String IS_READY_TO_SUBMIT = "IS_READY_TO_SUBMIT";
    public static final String IS_ATTR_MATCH = "IS_ATTR_MATCH";
    public static final String ALTER_PROPOSAL_DATA = "ALTER_PROPOSAL_DATA";
    
    public static final String SCHEMA_URL = "Schema_Url";
    public static final String SOAP_SERVER_PROPERTY_FILE = "/soap_server.properties";
    public static final String FORM_URLS = "Form_URL_List";
    
    public static final String EST_TIMEZONE_ID = "US/Eastern";//"GMT-05:00";
    
    public static final String SOAP_SERVICE_SERVER_HOST = "SOAP_SERVICE_SERVER_HOST";
    public static final String SOAP_SERVICE_SERVER_PORT = "SOAP_SERVICE_SERVER_PORT";

    public static final String SOAP_HOST_2 = "SOAP_HOST_2";
    public static final String SOAP_HOST = "SOAP_HOST";
    public static final String SOAP_HOST_2_DISPLAY = "SOAP_HOST_2_DISPLAY";
    public static final String SOAP_HOST_DISPLAY = "SOAP_HOST_DISPLAY";
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
    public static final char GET_DIVISION = 'J';    
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End

   //COEUSQA-4066
    public static final char GET_USER_ATTACHED_S2S_FORM = 'a';
    public static final char SAVE_USER_ATTACHED_S2S_FORM = 'b';
    public static final char TRANSLATE_USER_ATTACHED_S2S_FORM = 'c';
    public static final char GET_PDF = 'C';
    public static final char GET_XML = 'D';
    public static final String PDF = "pdf";
    public static final String XML = "xml";
    public static final String ATTACHMENT = "ATTACHMENT";
    public static final String XML_GENERATED_SUCCESSFULLY = "XML Generated successfully";
    public static final String COULD_NOT_DETERMINE_DOC_TYPE = "Could Not Determine Document Type";
    public static final String COULD_NOT_EXTRACT_XML_FROM_PDF = "Could Not Extract XML From PDF";
    public static final String PROPOSAL_NUMBER = "proposalNumber";
    public static final String USER_ATTACHED_FORM_NUMBER = "userAttachedFormNumber";
    public static final String PHS_HUMANSUBJECT_FORM_NUMBER = "phsHumanSubjectFormNumber";
    public static final String PHS_HUMANSUBJECT_ATTMNT_TYPE = "phsHumanSubjectAttachmentType";
    public static final String PHS_HUMANSUBJECT_ATTMNT_NUMBER = "phsHumanSubjectAttachmentNumber";
    public static final String PHS_ATTMNT_TYPE = "phsAttachmentType";
    public static final String PHS_HUMANSUBJECTS_AND_CLINICALTRIALSINFO_FORM_NAME = "PHSHumanSubjectsAndClinicalTrialsInfo";
    public static final String FILE = "file";
    public static final String TIMESTAMP = "TS";
    public static final String CONTENT_ID = "CONTENT_ID";
    //COEUSQA-4066
	public static final Object NAMESPACE = "NAMESPACE";
}
