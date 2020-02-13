/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.services;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.utils.mail.CoeusMailService;
import edu.mit.coeus.utils.mail.SetMailAttributes;
import edu.mit.coeuslite.coiv2.actions.SetStatusAdminCoiV2;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiUsersBean;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeuslite.coiv2.utilities.UserDetailsBeanCoiV2;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mr Lijo Joy
 */
public class CoiAttachmentService {

    private Timestamp dbTimestamp;
    private static final String DSN = "Coeus";
    private DBEngineImpl dbEngine;

    private CoiAttachmentService() {
    }
    private static CoiAttachmentService instance = null;
    private static int ATTACHMENT_ADDED_ACTION_CODE = 801;
    private static int ATTACHMENT_EDITED_ACTION_CODE = 804;
    private static int ATTACHMENT_DELETED_ACTION_CODE = 805;
    public static CoiAttachmentService getInstance() {
        if (instance == null) {
            instance = new CoiAttachmentService();
        }
        return instance;
    }

    /**
     * @return the dbTimestamp
     */
    public Timestamp getDbTimestamp() {
        return dbTimestamp;
    }

    /**
     * @param dbTimestamp the dbTimestamp to set
     */
    public void setDbTimestamp(Timestamp dbTimestamp) {
        this.dbTimestamp = dbTimestamp;
    }

    public Vector getDocumentType(HttpServletRequest request) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable documentList = (Hashtable) webTxnBean.getResults(request, "getDocumentTypeCoiv2", null);
        Vector dcoumentType = (Vector) documentList.get("getDocumentTypeCoiv2");
        return dcoumentType;
    }


     public String saveOrUpdateOrDelete(Coiv2AttachmentBean coiAttachmentBean, HttpServletRequest request) throws Exception {
        Vector vcProcedures = new Vector(3, 2);
        vcProcedures.addElement(addUpdUploadDocument(coiAttachmentBean, request));
        DBEngineImpl dbEngine = new DBEngineImpl();
        if (dbEngine != null) {
            java.sql.Connection conn = null;
            conn = dbEngine.beginTxn();
            if ((vcProcedures != null) && (vcProcedures.size() > 0)) {
                dbEngine.batchSQLUpdate(vcProcedures, conn);
            }
            dbEngine.commit(conn);



            Coiv2AttachmentBean coiv2AttachmentBean = coiAttachmentBean;
            int actionId=0;
            if (coiv2AttachmentBean.getAcType().trim().equals("I")) {
                actionId=ATTACHMENT_ADDED_ACTION_CODE;

            }
            if (coiv2AttachmentBean.getAcType().trim().equals("U")) {
                  actionId=ATTACHMENT_EDITED_ACTION_CODE;

            }
            if (coiv2AttachmentBean.getAcType().trim().equals("D")) {
                 actionId=ATTACHMENT_DELETED_ACTION_CODE;

            }
            String disclosureNumber = coiAttachmentBean.getDisclosureNumber();
             String setDisclosureNumberData = "Disclosure Number : "+ coiAttachmentBean.getDisclosureNumber();
            WebTxnBean webTxn = new WebTxnBean();
            PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId =person.getPersonID();
            HashMap hmData2=new HashMap();
                    Vector reporterpre = null;
                    reporterpre = getReporterByDiscl(disclosureNumber, request);
                PersonInfoBean person1 = new PersonInfoBean();

                    if (reporterpre != null && reporterpre.size() > 0) {
                        person1 = (PersonInfoBean) reporterpre.get(0);
                    }


             //personId=person1.getPersonID();
            //CoiDisclosureBean CurDisclosureBean = new CoiDisclosureBean();
            hmData2.put("coiDisclosureNumber", disclosureNumber);
            hmData2.put("sequenceNumber",coiAttachmentBean.getSequenceNumber());
            hmData2.put("personId", person1.getPersonID());

           // Hashtable DisclData1 = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData2);
         //   DisclDet = (Vector) DisclData.get("getDisclBySequnce");
           try{
                    Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData2);
                    Vector statusDet = (Vector) DisclData.get("getDisclStatus");
                    CoiDisclosureBean CurDisclosureBean = new CoiDisclosureBean();
                    if (statusDet != null && statusDet.size() > 0) {
                        CurDisclosureBean = (CoiDisclosureBean) statusDet.get(0);
                    }
                    String userName = "User Name : ";
                    userName = userName + person1.getUserName();
                    String disclosureStatus = "Disclosure Status : ";
                    disclosureStatus = disclosureStatus+CurDisclosureBean.getDisclosureStatus();

                    SetStatusAdminCoiV2 setstatus = new SetStatusAdminCoiV2();
                    Vector reporter = null;
                    Vector vecRecipients = new Vector();
                    reporter = setstatus.getReporterByDiscl(disclosureNumber, request);
                            //viewers = setstatus.getViewerByDisclosure(assignDisclUserBean.getCoiDisclosureNumber(), request);
                    if (reporter != null && reporter.size() > 0) {
                        for (Iterator it = reporter.iterator(); it.hasNext();) {
                                     PersonInfoBean ob = (PersonInfoBean) it.next();
                                     PersonRecipientBean reciepientob = new PersonRecipientBean();
                                     if(ob.getEmail()!=null){
                                         reciepientob.setEmailId(ob.getEmail());
                                     }

                                    //ob.setUserId(string);//set userid
                                    vecRecipients.add(reciepientob);
                        }
                    }
                      UtilFactory.log("@@@@@ Attachment reporter assigned  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//                      Vector viewers = null;
//                        //viewers = getViewersByDiscl(disclosureNumber, request);
//                        viewers = setstatus.getViewersByDiscl(disclosureNumber, request);
//                    if (viewers != null && viewers.size() > 0) {
//                        for (Iterator itv = viewers.iterator(); itv.hasNext();) {
//                              PersonInfoBean ob = (PersonInfoBean) itv.next();
//                            PersonRecipientBean object = new PersonRecipientBean();
//                                if(ob.getEmail()!=null){
//                                     object.setEmailId(ob.getEmail());
//                                 }
//
//                             vecRecipients.add(object);
//                        }
//                    }

                      Vector adminsviewer = null;
                    HashMap hmData = new HashMap();

                    Hashtable users = (Hashtable) webTxn.getResults(request, "getAdminsByDisclosure", hmData);
                    adminsviewer = (Vector) users.get("getAdminsByDisclosure");
                    if (adminsviewer != null && adminsviewer.size() > 0) {
                        for (Iterator ita = adminsviewer.iterator(); ita.hasNext();) {
                           // PersonInfoBean adminobject = (PersonInfoBean) ita.next();
                            PersonRecipientBean adminobject = (PersonRecipientBean) ita.next();
//                             PersonRecipientBean reciepientob = new PersonRecipientBean();
//                                     if(adminobject.getEmail()!=null){
//                                         reciepientob.setEmailId(adminobject.getEmail());
//                                     }
                           vecRecipients.add(adminobject);
                        }
                    }

                  DisclosureMailNotification discloNotification = new  DisclosureMailNotification();
                  MailMessageInfoBean mailMsgInfoBean = null;
                   Integer isAdminViewer=0;
                   isAdminViewer=(Integer) request.getSession().getAttribute("ADMINVIEWER");
                                // Send mail to newly added reviewer
                        try{
                            boolean  mailSent;
                            if(isAdminViewer==1){

                                  mailMsgInfoBean = discloNotification.prepareNotification(actionId);
                                  if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                   mailMsgInfoBean.setPersonRecipientList(vecRecipients);
//                                   String msg=mailMsgInfoBean.getMessage();
//                                    UtilFactory.log("8888888888----msg"+msg);
//                                   msg=msg+"  ||DisclosureNumber: "+disclosureNumber;
//                                    UtilFactory.log("8888888888----msg1"+msg);
//                                   mailMsgInfoBean.setMessage(msg);
//                                    UtilFactory.log("8888888888----msg2"+msg);
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(setDisclosureNumberData, "\n");
                                   mailMsgInfoBean.appendMessage(disclosureStatus, "\n");
                                   mailMsgInfoBean.appendMessage(userName, "\n");
                                   mailSent = discloNotification.sendNotification(mailMsgInfoBean);

                                }
                            }
                        } catch (Exception ex){
                            UtilFactory.log(ex.getMessage());
                        }
            } catch (Exception ex){
                UtilFactory.log(ex.getMessage());
            }

        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }


        return "success";
    }


//    public String saveOrUpdateOrDelete(Coiv2AttachmentBean coiAttachmentBean, HttpServletRequest request) throws Exception {
//        Vector vcProcedures = new Vector(3, 2);
//        vcProcedures.addElement(addUpdUploadDocument(coiAttachmentBean, request));
//        DBEngineImpl dbEngine = new DBEngineImpl();
//        if (dbEngine != null) {
//            java.sql.Connection conn = null;
//            conn = dbEngine.beginTxn();
//            if ((vcProcedures != null) && (vcProcedures.size() > 0)) {
//                dbEngine.batchSQLUpdate(vcProcedures, conn);
//            }
//            dbEngine.commit(conn);
//
//////for mail
////            UtilFactory.log("===================Attachment mail start===================" + new Date());
////            //for mail: notification about insertion,updation or deletion of attachments.
////            SetMailAttributes att = new SetMailAttributes();
////            CoeusMailService cms = new CoeusMailService();
////            att.setAttachmentPresent(false);
////            att.setSubject("Test Message from coeus");
////            att.setFrom("roshin@invisionlabs.com");
////            Coiv2AttachmentBean coiv2AttachmentBean = coiAttachmentBean;
////            if (coiv2AttachmentBean.getAcType().trim().equals("I")) {
////                att.setMessage("Coeus Welcomes You \n Attachment Inserted Successfully");
////            }
////            if (coiv2AttachmentBean.getAcType().trim().equals("U")) {
////                att.setMessage("Coeus Welcomes You \n Attachment Updated Successfully");
////            }
////            if (coiv2AttachmentBean.getAcType().trim().equals("D")) {
////                att.setMessage("Coeus Welcomes You \n Attachment Deleted Successfully");
////            }
////            String disclosureNumber = coiAttachmentBean.getDisclosureNumber();
////
////            //for getting reporter email
////            String reporterEmail = "";
////            Vector reporter = null;
////            reporter = getReporterByDiscl(disclosureNumber, request);
////            if (reporter != null && reporter.size() > 0) {
////                for (Iterator it = reporter.iterator(); it.hasNext();) {
////                    PersonInfoBean object = (PersonInfoBean) it.next();
////                    reporterEmail = object.getEmail();
////                }
////            }
////
////            //for getting viewers email ids
////            String viewer = "";
////            Vector viewers = null;
////            viewers = getViewersByDiscl(disclosureNumber, request);
////            if (viewers != null && viewers.size() > 0) {
////                for (Iterator it = viewers.iterator(); it.hasNext();) {
////                    PersonInfoBean object = (PersonInfoBean) it.next();
////                    viewer += object.getEmail() + ",";
////                }
////            }
////
////            //for getting admin emails
////            String admins = "";
////            Vector userDet = null;
////            HashMap hmData = new HashMap();
////            WebTxnBean webTxn = new WebTxnBean();
////            Hashtable users = (Hashtable) webTxn.getResults(request, "getAdmins", hmData);
////            userDet = (Vector) users.get("getAdmins");
////            if (userDet != null && userDet.size() > 0) {
////                for (Iterator it = userDet.iterator(); it.hasNext();) {
////                    CoiUsersBean coiUsersBean = (CoiUsersBean) it.next();
////                    admins += coiUsersBean.getEmail() + ",";
////                }
////            }
////            att.setTo(admins + viewer + reporterEmail);
////            cms.sendMessage(att);
////            System.out.println("Done");
////
////            UtilFactory.log("===================Attachment mail end===================" + new Date());
//        } else {
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//
//
//        return "success";
//    }

    public ProcReqParameter addUpdUploadDocument(Coiv2AttachmentBean coiv2AttachmentBean, HttpServletRequest request)
            throws CoeusException, DBException, Exception {
        Vector param = new Vector();
        param.addElement(new Parameter("COI_DISCLOSURE_NUMBER",
                DBEngineConstants.TYPE_STRING, coiv2AttachmentBean.getDisclosureNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, coiv2AttachmentBean.getSequenceNumber()));
        param.addElement(new Parameter("COI_DOCUMENT_TYPE_CODE",
                DBEngineConstants.TYPE_STRING, coiv2AttachmentBean.getDocType()));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING, coiv2AttachmentBean.getDescription().trim()));
        param.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING, coiv2AttachmentBean.getFileName()));

        if (coiv2AttachmentBean.getDocument() != null) {
            byte data[] = coiv2AttachmentBean.getFileBytes();
            param.addElement(new Parameter("DOCUMENT",
                    DBEngineConstants.TYPE_BLOB, data));
        }
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, coiv2AttachmentBean.getUpdateUser()));
                param.addElement(new Parameter("COI_DISC_DETAILS_NUMBER",
                DBEngineConstants.TYPE_INT, coiv2AttachmentBean.getDiscDetailsNumber()));

        StringBuffer sqlUploadDocument = new StringBuffer("");

        if (coiv2AttachmentBean.getAcType().trim().equals("I")) {
            //Get the next document Id to be set
            int entityNumber = getNextUploadID(coiv2AttachmentBean.getDisclosureNumber(), coiv2AttachmentBean.getSequenceNumber(), request);
            coiv2AttachmentBean.setEntityNumber(entityNumber);
            param.addElement(new Parameter("DOCUMENT_NUMBER",
                    DBEngineConstants.TYPE_INT, new Integer(coiv2AttachmentBean.getEntityNumber())));
            sqlUploadDocument.append("insert into OSP$COI_ATTACHMENTS(");
            sqlUploadDocument.append(" COI_DISCLOSURE_NUMBER , ");
            sqlUploadDocument.append(" SEQUENCE_NUMBER , ");
            sqlUploadDocument.append(" COI_DOCUMENT_TYPE_CODE , ");
            sqlUploadDocument.append(" DESCRIPTION , ");
            sqlUploadDocument.append(" FILE_NAME , ");
            sqlUploadDocument.append(" DOCUMENT , ");
            sqlUploadDocument.append(" UPDATE_TIMESTAMP , ");
            sqlUploadDocument.append(" UPDATE_USER , ");
            sqlUploadDocument.append(" DOCUMENT_NUMBER , ");
            sqlUploadDocument.append(" COI_DISC_DETAILS_NUMBER ) ");
            sqlUploadDocument.append(" VALUES (");
            sqlUploadDocument.append(" <<COI_DISCLOSURE_NUMBER>> , ");
            sqlUploadDocument.append(" <<SEQUENCE_NUMBER>> , ");
            sqlUploadDocument.append(" <<COI_DOCUMENT_TYPE_CODE>> , ");
            sqlUploadDocument.append(" <<DESCRIPTION>> , ");
            sqlUploadDocument.append(" <<FILE_NAME>> , ");
            sqlUploadDocument.append(" <<DOCUMENT>> , ");
            sqlUploadDocument.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlUploadDocument.append(" <<UPDATE_USER>> , ");
            sqlUploadDocument.append(" <<DOCUMENT_NUMBER>> , ");
            sqlUploadDocument.append(" <<COI_DISC_DETAILS_NUMBER>> ) ");
        } else if (coiv2AttachmentBean.getAcType().trim().equals("U")) {
            param.addElement(new Parameter("DOCUMENT_NUMBER",
                    DBEngineConstants.TYPE_INT, new Integer(coiv2AttachmentBean.getEntityNumber())));
            sqlUploadDocument.append("update OSP$COI_ATTACHMENTS set");
            sqlUploadDocument.append(" COI_DOCUMENT_TYPE_CODE = ");
            sqlUploadDocument.append(" <<COI_DOCUMENT_TYPE_CODE>>, ");
            sqlUploadDocument.append(" DESCRIPTION = ");
            sqlUploadDocument.append(" <<DESCRIPTION>>, ");
            sqlUploadDocument.append(" FILE_NAME = ");
            sqlUploadDocument.append(" <<FILE_NAME>>, ");
            sqlUploadDocument.append(" DOCUMENT = ");
            sqlUploadDocument.append(" <<DOCUMENT>>, ");
            sqlUploadDocument.append(" UPDATE_TIMESTAMP = ");
            sqlUploadDocument.append(" <<UPDATE_TIMESTAMP>>, ");
            sqlUploadDocument.append(" UPDATE_USER = ");
            sqlUploadDocument.append(" <<UPDATE_USER>> ");
            sqlUploadDocument.append(" <<COI_DISC_DETAILS_NUMBER>> ) ");
            sqlUploadDocument.append(" where ");
            sqlUploadDocument.append(" COI_DISCLOSURE_NUMBER = ");
            sqlUploadDocument.append(" <<COI_DISCLOSURE_NUMBER>> ");
            sqlUploadDocument.append(" and SEQUENCE_NUMBER = ");
            sqlUploadDocument.append(" <<SEQUENCE_NUMBER>> ");
            sqlUploadDocument.append(" and DOCUMENT_NUMBER = ");
            sqlUploadDocument.append(" <<DOCUMENT_NUMBER>> ");

        } else if (coiv2AttachmentBean.getAcType().trim().equals("D")) {
            param.addElement(new Parameter("DOCUMENT_NUMBER",
                    DBEngineConstants.TYPE_INT, new Integer(coiv2AttachmentBean.getEntityNumber())));
            sqlUploadDocument.append(" delete from OSP$COI_ATTACHMENTS ");
            sqlUploadDocument.append(" where ");
            sqlUploadDocument.append(" COI_DISCLOSURE_NUMBER = ");
            sqlUploadDocument.append(" <<COI_DISCLOSURE_NUMBER>> ");
            sqlUploadDocument.append(" and SEQUENCE_NUMBER = ");
            sqlUploadDocument.append(" <<SEQUENCE_NUMBER>> ");
            sqlUploadDocument.append(" and DOCUMENT_NUMBER = ");
            sqlUploadDocument.append(" <<DOCUMENT_NUMBER>> ");
        }


        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlUploadDocument.toString());

        return procReqParameter;
    }

    private int getNextUploadID(String disclosureNumber, int sequenceNumber, HttpServletRequest request) throws Exception {

        int newMaxEntryNumber = 0;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmMaxEntryNumber = new HashMap();
        hmMaxEntryNumber.put("coiDisclosureNumber", disclosureNumber);
        hmMaxEntryNumber.put("coiSequenceNumber", sequenceNumber);
        //Get max entry number
        Hashtable htMaxEntryNumber = (Hashtable) webTxnBean.getResults(request, "getMaxEntryNumberAttachmentsCoiv2", hmMaxEntryNumber);
        hmMaxEntryNumber = (HashMap) htMaxEntryNumber.get("getMaxEntryNumberAttachmentsCoiv2");
        if (hmMaxEntryNumber != null && hmMaxEntryNumber.size() > 0) {
            String maxEntryNumber = (String) hmMaxEntryNumber.get("maxEntryNumber");
            newMaxEntryNumber = Integer.parseInt(maxEntryNumber) + 1;
        }
        return new Integer(newMaxEntryNumber);
    }

    public Vector getUploadDocumentForDisclosure(String disclosureNumber, int sequenceNumber) throws DBException {
        dbEngine = new DBEngineImpl();
        Vector result = new Vector(3, 2);
        Vector vecUploadDoc = null;
        HashMap hmUploadDoc = null;
        Vector param = new Vector();
        param.addElement(new Parameter("COI_DISCLOSURE_NUMBER",
                DBEngineConstants.TYPE_STRING, disclosureNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, sequenceNumber));
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call GET_DISL_DOCUMENTLIST_COIV2 ( <<COI_DISCLOSURE_NUMBER>>,<<SEQUENCE_NUMBER>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        } else {
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount > 0) {
            vecUploadDoc = new Vector();
            for (int types = 0; types < ctypesCount; types++) {
                hmUploadDoc = (HashMap) result.elementAt(types);
                Coiv2AttachmentBean uploadDocumentBean = new Coiv2AttachmentBean();
                if (hmUploadDoc.get("COI_DISCLOSURE_NUMBER") != null) {
                    uploadDocumentBean.setDisclosureNumber((String) hmUploadDoc.get("COI_DISCLOSURE_NUMBER"));
                    uploadDocumentBean.setSequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
                    uploadDocumentBean.setDocType(hmUploadDoc.get("COI_DOCUMENT_TYPE_CODE").toString());
                    uploadDocumentBean.setDocdescription((String) hmUploadDoc.get("DOC_TYPE_DESCRIPTION"));
                    uploadDocumentBean.setDescription(hmUploadDoc.get("DESCRIPTION") == null ? "" : (String) hmUploadDoc.get("DESCRIPTION"));
                    uploadDocumentBean.setFileName((String) hmUploadDoc.get("FILE_NAME"));
                    uploadDocumentBean.setUpdateUser((String) hmUploadDoc.get("UPDATE_USER"));
                    if (hmUploadDoc.get("DOCUMENT") != null) {
                        uploadDocumentBean.setFileBytes(((ByteArrayOutputStream) hmUploadDoc.get("DOCUMENT")).toByteArray());
                    } else {
                        uploadDocumentBean.setDocument(null);
                    }
                    uploadDocumentBean.setEntityNumber(Integer.parseInt(hmUploadDoc.get("DOCUMENT_NUMBER").toString()));

                    if (hmUploadDoc.get("UPDATE_USER_NAME") != null) {
                        uploadDocumentBean.setUserName((String) hmUploadDoc.get("UPDATE_USER_NAME"));
                    }
                    uploadDocumentBean.setUpdateTimeStamp((Timestamp) hmUploadDoc.get("UPDATE_TIMESTAMP"));
                    vecUploadDoc.addElement(uploadDocumentBean);
                }
            }
        }
        return vecUploadDoc;
    }

    public Vector getUploadDocumentForPerson(String disclosureNumber, int sequenceNumber, String personId) throws DBException {
        dbEngine = new DBEngineImpl();
        Vector result = new Vector(3, 2);
        Vector vecUploadDoc = null;
        HashMap hmUploadDoc = null;
        Vector param = new Vector();
        param.addElement(new Parameter("COI_DISCLOSURE_NUMBER",
                DBEngineConstants.TYPE_STRING, disclosureNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, sequenceNumber));
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call GET_DISL_PER_DOCUMENT_COIV2 ( <<COI_DISCLOSURE_NUMBER>>,<<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        } else {
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount > 0) {
            vecUploadDoc = new Vector();
            for (int types = 0; types < ctypesCount; types++) {
                hmUploadDoc = (HashMap) result.elementAt(types);
                Coiv2AttachmentBean uploadDocumentBean = new Coiv2AttachmentBean();
                    uploadDocumentBean.setDisclosureNumber((String) hmUploadDoc.get("COI_DISCLOSURE_NUMBER"));
                    uploadDocumentBean.setSequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
                    uploadDocumentBean.setDocType(hmUploadDoc.get("COI_DOCUMENT_TYPE_CODE").toString());
                    uploadDocumentBean.setDocdescription((String) hmUploadDoc.get("DOC_TYPE_DESCRIPTION"));
                    uploadDocumentBean.setDescription(hmUploadDoc.get("DESCRIPTION") == null ? "" : (String) hmUploadDoc.get("DESCRIPTION"));
                    uploadDocumentBean.setFileName((String) hmUploadDoc.get("FILE_NAME"));
                    uploadDocumentBean.setModuleItemKey((String) hmUploadDoc.get("MODULE_ITEM_KEY"));
                    uploadDocumentBean.setUpdateUser((String) hmUploadDoc.get("UPDATE_USER"));
                    uploadDocumentBean.setEntName((String) hmUploadDoc.get("ENTITY_NAME"));
                    if (hmUploadDoc.get("DOCUMENT") != null) {
                        uploadDocumentBean.setFileBytes(((ByteArrayOutputStream) hmUploadDoc.get("DOCUMENT")).toByteArray());
                    } else {
                        uploadDocumentBean.setDocument(null);
                    }
                    uploadDocumentBean.setEntityNumber(Integer.parseInt(hmUploadDoc.get("DOCUMENT_NUMBER").toString()));

                    if (hmUploadDoc.get("UPDATE_USER_NAME") != null) {
                        uploadDocumentBean.setUserName((String) hmUploadDoc.get("UPDATE_USER_NAME"));
                    }
                    uploadDocumentBean.setUpdateTimeStamp((Timestamp) hmUploadDoc.get("UPDATE_TIMESTAMP"));
                    vecUploadDoc.addElement(uploadDocumentBean);
                }
            
        }
        return vecUploadDoc;
    }

    private String viewDocument(int versionNumber, int docType, HttpServletRequest request,
            String protocolNumber, int sequenceNum, int statusCode, int viewDocId) throws Exception {
        //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -End
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ProtocolDocumentReader");

        UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
        uploadDocumentBean.setProtocolNumber(protocolNumber);
        uploadDocumentBean.setDocCode(docType);
        uploadDocumentBean.setVersionNumber(versionNumber);
        uploadDocumentBean.setSequenceNumber(sequenceNum);
        //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
        uploadDocumentBean.setDocumentId(viewDocId);
        //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -End
        uploadDocumentBean.setStatusCode(statusCode);
        map.put("UPLOAD_DOC_BEAN", uploadDocumentBean);

        documentBean.setParameterMap(map);

        String docId = DocumentIdGenerator.generateDocumentId();

        //StringBuffer strBuff = request.getRequestURL();
        StringBuffer stringBuffer = new StringBuffer();
        //String strPath = new String(strBuff);
        //strPath = strPath.substring(0,strPath.lastIndexOf('/'));
        //stringBuffer.append(strPath);
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);

        request.getSession().setAttribute(docId, documentBean);

        return stringBuffer.toString();
    }

    public Vector getViewersByDiscl(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector viewerDet = null;
        Hashtable viewerData = (Hashtable) webTxn.getResults(request, "getViewersByDiscl", hmData);
        viewerDet = (Vector) viewerData.get("getViewersByDiscl");
        return viewerDet;
    }

    public Vector getReporterByDiscl(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector reporterDet = null;
        Hashtable reporterData = (Hashtable) webTxn.getResults(request, "getReporterByDiscl", hmData);
        reporterDet = (Vector) reporterData.get("getReporterByDiscl");
        return reporterDet;
    }
    /*public Vector getReporterByDisclosure(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector reporterDet = null;
        Hashtable reporterData = (Hashtable) webTxn.getResults(request, "getReporterByDisclosure", hmData);
        reporterDet = (Vector) reporterData.get("getReporterByDisclosure");
        return reporterDet;
    }*/


}