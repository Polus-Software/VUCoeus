/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.phshumansubject.bean;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import java.util.*;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.query.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.http.HttpSession;

/**
 *
 * @author anishk
 */
public class PHSHumanSubjectTxnBean {

    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon;
    QueryEngine queryEngine;
    private Timestamp timestamp;
    public static final String INSERT_RECORD = "I";
    public static final String UPDATE_RECORD = "U";
    public static final String DELETE_RECORD = "D";
    public static String userId;
    private static final String DSN = "Coeus";

    public PHSHumanSubjectTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }

    /**
     * creates an PHSHumanSubjectTxnBean
     *
     * @param userId userId
     */
    public PHSHumanSubjectTxnBean(String userId) {
        dbEngine = new DBEngineImpl();
        this.userId = userId;
    }

    /**
     * retrieves a List of phsHumnSubjtFormBean for this proposalNumber
     *
     * @param proposalNumber proposalNumber
     * @return a List of phsHumnSubjtFormBean
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on
     * the DBEngine
     */
    public List<PHSHumanSubjectFormBean> getUserAttachedS2SForm(String proposalNumber)
            throws CoeusException, DBException {
        Vector param = new Vector();
        Vector result = new Vector();
        List<PHSHumanSubjectFormBean> lstBudgetSubAward = new ArrayList<PHSHumanSubjectFormBean>();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", "call GET_S2S_PHS_HUMNSUBJT_FORM(<< PROPOSAL_NUMBER >>, <<OUT RESULTSET rset>> )", "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0) {
            for (int count = 0; count < listSize; count++) {
                PHSHumanSubjectFormBean phsHumnSubjtFormBean = new PHSHumanSubjectFormBean();
                HashMap mapRow = (HashMap) result.elementAt(count);
                phsHumnSubjtFormBean.setProposalNumber((String) mapRow.get("PROPOSAL_NUMBER"));
                phsHumnSubjtFormBean.setFormNumber(Integer.parseInt(mapRow.get("FORM_NUMBER").toString()));
                phsHumnSubjtFormBean.setAwformNumber(phsHumnSubjtFormBean.getFormNumber());
                phsHumnSubjtFormBean.setDescription((String) mapRow.get("DESCRIPTION"));
                phsHumnSubjtFormBean.setAwDescription(phsHumnSubjtFormBean.getDescription());
                phsHumnSubjtFormBean.setPdfFileName((String) mapRow.get("FORM_FILE_NAME"));
                phsHumnSubjtFormBean.setUpdateTimestamp((Timestamp) mapRow.get("UPDATE_TIMESTAMP"));
                phsHumnSubjtFormBean.setUpdateUser((String) mapRow.get("UPDATE_USER"));
                phsHumnSubjtFormBean.setPdfUpdateUser((String) mapRow.get("FULL_NAME"));
                phsHumnSubjtFormBean.setNamespace((String) mapRow.get("NAMESPACE"));
                phsHumnSubjtFormBean.setFormName((String) mapRow.get("FORM_NAME"));
                List attachments = getUserAttachedS2SFormAttachments(proposalNumber);
                phsHumnSubjtFormBean.setAttachments(attachments);
                lstBudgetSubAward.add(phsHumnSubjtFormBean);
            }

        }
        return lstBudgetSubAward;
    }

    public void deleteHumanSubjectForm(String proposalNumber, String formNumber)
            throws CoeusException, DBException {
        Vector param = new Vector();
        Vector result = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("FORM_NUMBER", "String", formNumber));
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", "call UPD_DELETE_PHS_HUMAN_FORM(<< PROPOSAL_NUMBER >>,<< FORM_NUMBER >>)", "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
        
    public List saveUserS2SForm(List list) throws CoeusException, DBException {
        timestamp = (new CoeusFunctions()).getDBTimestamp();
        List retList = new ArrayList();
        PHSHumanSubjectFormBean phsHumnSubjtFormBean;
        int maxUserAttFormNumber = -1;
        List sqlList = new ArrayList();
        List subAwards = new ArrayList();

        Vector vecInsert = new Vector();
        Vector vecUpdate = new Vector();
        Vector vecDelete = new Vector();
        Vector vecPDFUpdate = new Vector();
        for (int index = 0; index < list.size(); index++) {
            phsHumnSubjtFormBean = (PHSHumanSubjectFormBean) list.get(index);
            if (phsHumnSubjtFormBean.getAcType() != null && phsHumnSubjtFormBean.getAcType().equals(INSERT_RECORD)) {
                if (maxUserAttFormNumber == -1) {
                    maxUserAttFormNumber = getMaxUserAttFormNumber(phsHumnSubjtFormBean.getProposalNumber());
                }
                maxUserAttFormNumber = maxUserAttFormNumber + 1;
                phsHumnSubjtFormBean.setFormNumber(maxUserAttFormNumber);
                vecInsert.add(getProcParameter(phsHumnSubjtFormBean));
                List attachments = phsHumnSubjtFormBean.getAttachments();
                if (attachments != null) {
                    for (int i = 0; i < attachments.size(); i++) {
                        PHSHumanSubjectFormAttachmentBean attachment = (PHSHumanSubjectFormAttachmentBean) attachments.get(i);
                        attachment.setFormNumber(maxUserAttFormNumber);
                        attachment.setFormAttachmentNumber(i + 1);
                        vecInsert.add(getProcParameter(attachment));
                    }
                }
            } else if (phsHumnSubjtFormBean.getAcType() != null && phsHumnSubjtFormBean.getAcType().equals(UPDATE_RECORD)) {
                vecUpdate.add(getProcParameter(phsHumnSubjtFormBean));
                List attachments = phsHumnSubjtFormBean.getAttachments();
                if (attachments != null) {
                    for (int i = 0; i < attachments.size(); i++) {
                        PHSHumanSubjectFormAttachmentBean attachment = (PHSHumanSubjectFormAttachmentBean) attachments.get(i);
                        attachment.setAwformNumber(phsHumnSubjtFormBean.getAwformNumber());
                        if (i == 0) {
                            attachment.setAcType(DELETE_RECORD);
                            vecDelete.add(getProcParameter(attachment));
                        }
                        attachment.setFormAttachmentNumber(i + 1);
                        attachment.setAcType(INSERT_RECORD);
                        vecInsert.add(getProcParameter(attachment));
                    }
                }

                //continue;
            } else if (phsHumnSubjtFormBean.getAcType() != null && phsHumnSubjtFormBean.getAcType().equals(DELETE_RECORD)) {
                vecDelete.add(getProcParameter(phsHumnSubjtFormBean));
                //continue;
            } else if (phsHumnSubjtFormBean.getPdfAcType() != null) {
                vecPDFUpdate.add(getProcParameter(phsHumnSubjtFormBean));
            }
            if ((phsHumnSubjtFormBean.getAcType() != null && !phsHumnSubjtFormBean.getAcType().equals(DELETE_RECORD))) {
                if (phsHumnSubjtFormBean.getAcType() != null) {
                    phsHumnSubjtFormBean.setUpdateTimestamp(timestamp);
                }
                phsHumnSubjtFormBean.setUpdateUser(userId);
                phsHumnSubjtFormBean.setAcType(null);
                phsHumnSubjtFormBean.setPdfAcType(null);
                phsHumnSubjtFormBean.setAwformNumber(phsHumnSubjtFormBean.getFormNumber());
                phsHumnSubjtFormBean.setUserAttachedS2SPDF(null);
                phsHumnSubjtFormBean.setUserAttachedS2SXML(null);
                subAwards.add(phsHumnSubjtFormBean);
            }

        }
        sqlList.addAll(vecDelete);
        sqlList.addAll(vecPDFUpdate);
        sqlList.addAll(vecUpdate);
        sqlList.addAll(vecInsert);

        if (sqlList.size() > 0) {
            if (dbEngine != null) {
                java.sql.Connection conn = null;
                try {
                    conn = dbEngine.beginTxn();
                    for (int sqlQueryIndex = 0; sqlQueryIndex < sqlList.size(); sqlQueryIndex++) {
                        dbEngine.batchSQLUpdate(
                                (ProcReqParameter) sqlList.get(sqlQueryIndex),
                                conn);
                    }
                    dbEngine.commit(conn);
                } catch (Exception sqlEx) {
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                } finally {
                    dbEngine.endTxn(conn);
                }

            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }
        }// End
        retList.add(0, subAwards);
        return retList;
    }

    private Object getProcParameter(PHSHumanSubjectFormAttachmentBean attachment) throws DBException {
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        if (attachment.getAcType() != null && attachment.getAcType().equals(INSERT_RECORD)) {
            sqlBudgetSubAward.append("insert into OSP$S2S_PHS_HUMNSUBJT_FORM_ATT(");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER, ");
            sqlBudgetSubAward.append(" FORM_NUMBER, ");
            sqlBudgetSubAward.append(" FORM_ATT_NUMBER, ");
            sqlBudgetSubAward.append(" CONTENT_TYPE, ");
            sqlBudgetSubAward.append(" FILE_NAME, ");
            sqlBudgetSubAward.append(" CONTENT_ID, ");
            sqlBudgetSubAward.append(" ATTACHMENT, ");
            sqlBudgetSubAward.append(" UPDATE_TIMESTAMP , ");
            sqlBudgetSubAward.append(" UPDATE_USER ) ");
            sqlBudgetSubAward.append(" VALUES (");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<FORM_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<FORM_ATT_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<CONTENT_TYPE>>, ");
            sqlBudgetSubAward.append(" <<FILE_NAME>> , ");
            sqlBudgetSubAward.append(" <<CONTENT_ID>>, ");
            sqlBudgetSubAward.append(" <<ATTACHMENT>>, ");
            sqlBudgetSubAward.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudgetSubAward.append(" <<UPDATE_USER>> ) ");
        } else if (attachment.getAcType() != null && attachment.getAcType().equals(DELETE_RECORD)) {
            sqlBudgetSubAward.append("delete FROM OSP$S2S_PHS_HUMNSUBJT_FORM_ATT ");
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append("AND FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_FORM_NUMBER>> ");
        }
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", attachment.getProposalNumber()));
        param.addElement(new Parameter("FORM_NUMBER", "int", "" + attachment.getFormNumber()));
        param.addElement(new Parameter("AW_FORM_NUMBER", "int", "" + attachment.getAwformNumber()));
        param.addElement(new Parameter("FORM_ATT_NUMBER", "int", attachment.getFormAttachmentNumber()));
        param.addElement(new Parameter("CONTENT_TYPE", "String", attachment.getContentType()));
        param.addElement(new Parameter("FILE_NAME", "String", attachment.getFilename()));
        param.addElement(new Parameter("CONTENT_ID", "String", attachment.getContentId()));
        param.addElement(new Parameter("ATTACHMENT", "Blob", (Object) attachment.getAttachment()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP", "Timestamp", timestamp));
        param.addElement(new Parameter("UPDATE_USER", "String", userId));
        ProcReqParameter procUserAttachmentForm = new ProcReqParameter();
        procUserAttachmentForm.setDSN("Coeus");
        procUserAttachmentForm.setParameterInfo(param);
        procUserAttachmentForm.setSqlCommand(sqlBudgetSubAward.toString());
        return procUserAttachmentForm;
    }

    /**
     * retrieves a maximum user attachment form number for this proposalNumber
     *
     * @param proposalNumber proposalNumber
     * @return a int of UserAttFormNumber
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on
     * the DBEngine
     */
    private int getMaxUserAttFormNumber(String proposalNumber) throws CoeusException, DBException {
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
        Vector result = new Vector();
        int maxUserAttFormNumber = 0;
        if (dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER MAX_USER_ATT_FORM_NUMBER>> = call FN_GET_MAX_S2S_PHS_FORM_NUM( <<PROPOSAL_NUMBER>>) }", param);

        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            HashMap row = (HashMap) result.elementAt(0);
            maxUserAttFormNumber = Integer.parseInt(row.get("MAX_USER_ATT_FORM_NUMBER").toString());
        }
        return maxUserAttFormNumber;
    }

    /**
     * retrieves a maximum user attachment form number for this proposalNumber
     *
     * @param namespace namespace
     * @return count of UserAttForms
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on
     * the DBEngine
     */
    public boolean isFormAvailable(String proposalNumber, String namespace) throws CoeusException, DBException {
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("NAMESPACE", DBEngineConstants.TYPE_STRING, namespace));
        Vector result = new Vector();
        int formCount = 0;
        if (dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER FORM_AVAILABLE>> = call FN_IS_PHS_HUMAN_FORM_AVAILABLE(<<PROPOSAL_NUMBER>>,<<NAMESPACE>>) }", param);

        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            HashMap row = (HashMap) result.elementAt(0);
            formCount = Integer.parseInt(row.get("FORM_AVAILABLE").toString());
        }
        return formCount > 0;
    }

    /**
     * returns a ProcReqParameter for this PHSHumanSubjectFormBean
     *
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on
     * the DBEngine
     * @return ProcReqParameter
     */
    private ProcReqParameter getProcParameter(PHSHumanSubjectFormBean phsHumnSubjtFormBean)
            throws DBException {
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        //timestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean hasPDF = phsHumnSubjtFormBean.getPdfAcType() != null;
        boolean hasXML = (phsHumnSubjtFormBean.getUserAttachedS2SXML() != null) && (phsHumnSubjtFormBean.getUserAttachedS2SXML().length > 0);
        if (phsHumnSubjtFormBean.getAcType() != null && phsHumnSubjtFormBean.getAcType().equals(INSERT_RECORD)) {
            sqlBudgetSubAward.append("insert into OSP$S2S_PHS_HUMNSUBJT_FORM(");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER, ");
            sqlBudgetSubAward.append(" FORM_NUMBER, ");
            sqlBudgetSubAward.append(" DESCRIPTION, ");
            if (hasPDF) {
                sqlBudgetSubAward.append(" FORM_FILE, ");
                sqlBudgetSubAward.append(" FORM_FILE_NAME, ");
                sqlBudgetSubAward.append(" NAMESPACE, ");
                sqlBudgetSubAward.append(" FORM_NAME, ");
                if (hasXML) {
                    sqlBudgetSubAward.append(" XML_FILE, ");
                }
            }
            sqlBudgetSubAward.append(" UPDATE_TIMESTAMP , ");
            sqlBudgetSubAward.append(" UPDATE_USER ) ");
            sqlBudgetSubAward.append(" VALUES (");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<FORM_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<DESCRIPTION>> , ");
            if (hasPDF) {
                sqlBudgetSubAward.append(" <<FORM_FILE>>, ");
                sqlBudgetSubAward.append(" <<FORM_FILE_NAME>> , ");
                sqlBudgetSubAward.append(" <<NAMESPACE>>, ");
                sqlBudgetSubAward.append(" <<FORM_NAME>>, ");
                if (hasXML) {
                    sqlBudgetSubAward.append(" <<XML_FILE>>, ");
                }
            }
            sqlBudgetSubAward.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudgetSubAward.append(" <<UPDATE_USER>> ) ");
        } else if (phsHumnSubjtFormBean.getAcType() != null && phsHumnSubjtFormBean.getAcType().equals(UPDATE_RECORD)) {
            sqlBudgetSubAward.append("update OSP$S2S_PHS_HUMNSUBJT_FORM set");
            sqlBudgetSubAward.append(" FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<FORM_NUMBER>> , ");
            sqlBudgetSubAward.append(" DESCRIPTION = ");
            sqlBudgetSubAward.append(" <<DESCRIPTION>> , ");
            if (hasPDF) {
                sqlBudgetSubAward.append(" FORM_FILE = ");
                sqlBudgetSubAward.append(" <<FORM_FILE>> , ");
                sqlBudgetSubAward.append(" FORM_FILE_NAME = ");
                sqlBudgetSubAward.append(" <<FORM_FILE_NAME>> , ");
                sqlBudgetSubAward.append(" NAMESPACE = ");
                sqlBudgetSubAward.append(" <<NAMESPACE>> , ");
                sqlBudgetSubAward.append(" FORM_NAME = ");
                sqlBudgetSubAward.append(" <<FORM_NAME>> , ");
                if (hasXML) {
                    sqlBudgetSubAward.append(" XML_FILE = ");
                    sqlBudgetSubAward.append(" <<XML_FILE>> , ");
                }
            }
            sqlBudgetSubAward.append(" UPDATE_TIMESTAMP = ");
            sqlBudgetSubAward.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudgetSubAward.append(" UPDATE_USER = ");
            sqlBudgetSubAward.append(" <<UPDATE_USER>> ");
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append("AND FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_FORM_NUMBER>> ");
        } else if (phsHumnSubjtFormBean.getAcType() != null && phsHumnSubjtFormBean.getAcType().equals(DELETE_RECORD)) {
            sqlBudgetSubAward.append("delete FROM OSP$S2S_PHS_HUMNSUBJT_FORM ");
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append("AND FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_FORM_NUMBER>> ");
        } else if (hasPDF) {
            sqlBudgetSubAward.append("update OSP$S2S_PHS_HUMNSUBJT_FORM set");
            sqlBudgetSubAward.append(" FORM_FILE = ");
            sqlBudgetSubAward.append(" <<FORM_FILE>> , ");
            sqlBudgetSubAward.append(" FORM_FILE_NAME = ");
            sqlBudgetSubAward.append(" <<FORM_FILE_NAME>> , ");
            sqlBudgetSubAward.append(" NAMESPACE = ");
            sqlBudgetSubAward.append(" <<NAMESPACE>> , ");
            sqlBudgetSubAward.append(" FORM_NAME = ");
            sqlBudgetSubAward.append(" <<FORM_NAME>>  ");
            if (hasXML) {
                sqlBudgetSubAward.append(" XML_FILE = ");
                sqlBudgetSubAward.append(" <<XML_FILE>> , ");
            }
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append("AND FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_FORM_NUMBER>> ");
        }
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", phsHumnSubjtFormBean.getProposalNumber()));
        param.addElement(new Parameter("FORM_NUMBER", "int", "" + phsHumnSubjtFormBean.getFormNumber()));
        param.addElement(new Parameter("AW_FORM_NUMBER", "int", "" + phsHumnSubjtFormBean.getAwformNumber()));
        param.addElement(new Parameter("DESCRIPTION", "String", phsHumnSubjtFormBean.getDescription()));
        param.addElement(new Parameter("FORM_FILE", "Blob", phsHumnSubjtFormBean.getUserAttachedS2SPDF() != null ? ((Object) (phsHumnSubjtFormBean.getUserAttachedS2SPDF())) : null));//((Object) (new byte[0]))));
        param.addElement(new Parameter("NAMESPACE", "String", phsHumnSubjtFormBean.getNamespace()));
        param.addElement(new Parameter("FORM_NAME", "String", phsHumnSubjtFormBean.getFormName()));
        param.addElement(new Parameter("FORM_FILE_NAME", "String", phsHumnSubjtFormBean.getPdfFileName()));
        param.addElement(new Parameter("XML_FILE", DBEngineConstants.TYPE_CLOB, hasXML ? new String(phsHumnSubjtFormBean.getUserAttachedS2SXML()) : null));
        param.addElement(new Parameter("UPDATE_TIMESTAMP", "Timestamp", timestamp));
        param.addElement(new Parameter("UPDATE_USER", "String", userId));
        ProcReqParameter procBudgetSubAward = new ProcReqParameter();
        procBudgetSubAward.setDSN("Coeus");
        procBudgetSubAward.setParameterInfo(param);
        procBudgetSubAward.setSqlCommand(sqlBudgetSubAward.toString());
        return procBudgetSubAward;
    }

    /**
     * retrieves the contents of PDF Document
     *
     * @return contents of PDF Document
     * @param proposalNumber proposal number
     * @param userAttachFormNumber sub award number
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on
     * DBEngine
     */
    public ByteArrayOutputStream getPDF(String proposalNumber, int userAttachFormNumber)
            throws CoeusException, DBException {
        return (ByteArrayOutputStream) getContents(proposalNumber, userAttachFormNumber, S2SConstants.PDF);
    }
    
    /**
     * retrieves the contents of PDF Document
     *
     * @return contents of PDF Document
     * @param proposalNumber proposal number
     * @param userAttachFormNumber sub award number
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on
     * DBEngine
     */
    public ByteArrayOutputStream getAttachment(String proposalNumber, int attachmentNumber )
            throws CoeusException, DBException {
        return (ByteArrayOutputStream) getAttachmentContent(proposalNumber, attachmentNumber);
    } 

    
     public ByteArrayOutputStream getDelayStudyAttachment(String proposalNumber, int studyNumber )
            throws CoeusException, DBException {
        return (ByteArrayOutputStream) getDelayStudyAttachmentContent(proposalNumber, studyNumber);
    }
    /**
     * retrieves the Contents of PDF document
     *
     * @param proposalNumber proposal number
     * @param userAttachFormNumber sub award number
     * @param file PDF
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on
     * the DBEngine
     * @return Contents of the File requested
     */
    private Object getContents(String proposalNumber, int userAttachFormNumber, String file)
            throws CoeusException, DBException {
        //ByteArrayOutputStream byteArrayOutputStream = null;
        Object object = null;
        String content = null;
        if (file != null && file.equals(S2SConstants.PDF)) {
            content = "FORM_FILE";
        }
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        sqlBudgetSubAward.append("SELECT ");
        sqlBudgetSubAward.append(content);
        sqlBudgetSubAward.append(" FROM  OSP$S2S_PHS_HUMNSUBJT_FORM WHERE");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
        sqlBudgetSubAward.append("AND FORM_NUMBER = <<FORM_NUMBER>> ");
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("FORM_NUMBER", "int", "" + userAttachFormNumber));
        ProcReqParameter procBudgetSubAward = new ProcReqParameter();
        procBudgetSubAward.setDSN("Coeus");
        procBudgetSubAward.setParameterInfo(param);
        procBudgetSubAward.setSqlCommand(sqlBudgetSubAward.toString());
        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", sqlBudgetSubAward.toString(), "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0) {
            HashMap mapRow = (HashMap) result.elementAt(0);
            object = mapRow.get(content);
        }
        return object;
    }
    /**
     * retrieves the Contents of PHS Attachemnt
     *
     * @param proposalNumber proposal number
     * @param userAttachFormNumber sub award number
     * @param file PDF
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on
     * the DBEngine
     * @return Contents of the File requested
     */
    private Object getAttachmentContent(String proposalNumber, int attachmentNumber)
            throws CoeusException, DBException {
        //ByteArrayOutputStream byteArrayOutputStream = null;
        Object object = null;       
        
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        sqlBudgetSubAward.append("SELECT ");
        sqlBudgetSubAward.append(" ATTACHMENT");
        sqlBudgetSubAward.append(" FROM  OSP$S2S_PHS_HUMNSUBJT_ATTCMNT WHERE");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
        sqlBudgetSubAward.append("AND ATTACHMENT_NUMBER = <<ATTACMENT_NUMBER>> ");
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("ATTACMENT_NUMBER", "int", "" + attachmentNumber));
        ProcReqParameter procBudgetSubAward = new ProcReqParameter();
        procBudgetSubAward.setDSN("Coeus");
        procBudgetSubAward.setParameterInfo(param);
        procBudgetSubAward.setSqlCommand(sqlBudgetSubAward.toString());
        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", sqlBudgetSubAward.toString(), "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0) {
            HashMap mapRow = (HashMap) result.elementAt(0);
            object = mapRow.get("ATTACHMENT");
        }
        return object;
    }
    
    
     private Object getDelayStudyAttachmentContent(String proposalNumber, int studyNumber)
            throws CoeusException, DBException {
        //ByteArrayOutputStream byteArrayOutputStream = null;
        Object object = null;       
        
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        sqlBudgetSubAward.append("SELECT ");
        sqlBudgetSubAward.append(" JUSTIFICATION");
        sqlBudgetSubAward.append(" FROM  OSP$S2S_PHS_HUMNSUBJT_DLY_STDY WHERE");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
        sqlBudgetSubAward.append("AND STUDY_NUMBER = <<STUDY_NUMBER>> ");
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("STUDY_NUMBER", "int", "" + studyNumber));
        ProcReqParameter procBudgetSubAward = new ProcReqParameter();
        procBudgetSubAward.setDSN("Coeus");
        procBudgetSubAward.setParameterInfo(param);
        procBudgetSubAward.setSqlCommand(sqlBudgetSubAward.toString());
        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", sqlBudgetSubAward.toString(), "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0) {
            HashMap mapRow = (HashMap) result.elementAt(0);
            object = mapRow.get("JUSTIFICATION");
        }
        return object;
    }
     

    /**
     * Getter for property userId.
     *
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }

    /**
     * Setter for property userId.
     *
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

    public String getUserAttachedFormXml(String proposalNumber,
            int formNumber) throws DBException, CoeusException {
        String xmlFile = null;
        StringBuffer sqlUserAttachedForm = new StringBuffer();
        sqlUserAttachedForm.append("SELECT ");
        sqlUserAttachedForm.append(" PROPOSAL_NUMBER, ");
        sqlUserAttachedForm.append(" FORM_NUMBER, ");
        sqlUserAttachedForm.append(" XML_FILE ");
        sqlUserAttachedForm.append(" FROM  OSP$S2S_PHS_HUMNSUBJT_FORM WHERE");
        sqlUserAttachedForm.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
        sqlUserAttachedForm.append("AND FORM_NUMBER = <<FORM_NUMBER>>");
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("FORM_NUMBER", "int", "" + formNumber));

        ProcReqParameter procUserAttachedForm = new ProcReqParameter();
        procUserAttachedForm.setDSN("Coeus");
        procUserAttachedForm.setParameterInfo(param);
        procUserAttachedForm.setSqlCommand(sqlUserAttachedForm.toString());
        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", sqlUserAttachedForm.toString(), "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
         if (listSize > 0) {            
                HashMap mapRow = (HashMap) result.elementAt(0);               
                xmlFile = (String) mapRow.get("XML_FILE");            
        }
        return xmlFile;
    }
    public ArrayList<String> getUserAttachedFormXmlList(String proposalNumber) throws DBException, CoeusException {
        String xmlFile = null;
        ArrayList<String> lsHumanXML = new ArrayList();
        StringBuffer sqlUserAttachedForm = new StringBuffer();
        sqlUserAttachedForm.append("SELECT ");
        sqlUserAttachedForm.append(" PROPOSAL_NUMBER, ");
        sqlUserAttachedForm.append(" FORM_NUMBER, ");
        sqlUserAttachedForm.append(" XML_FILE ");
        sqlUserAttachedForm.append(" FROM  OSP$S2S_PHS_HUMNSUBJT_FORM WHERE");
        sqlUserAttachedForm.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");      
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));        

        ProcReqParameter procUserAttachedForm = new ProcReqParameter();
        procUserAttachedForm.setDSN("Coeus");
        procUserAttachedForm.setParameterInfo(param);
        procUserAttachedForm.setSqlCommand(sqlUserAttachedForm.toString());
        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", sqlUserAttachedForm.toString(), "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0) {
            for (int count = 0; count < listSize; count++) {
                HashMap mapRow = (HashMap) result.elementAt(count);
                xmlFile = (String) mapRow.get("XML_FILE");
                lsHumanXML.add(xmlFile);
            }
        }
        return lsHumanXML;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List getUserAttachedS2SFormAttachments(String proposalNumber) throws DBException, CoeusException {
        List lstAttachment = new ArrayList();
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));       

        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", "call GET_S2S_PHS_HUMNSUBJT_FORM_ATT(<< PROPOSAL_NUMBER >>, <<OUT RESULTSET rset>> )", "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        if (listSize > 0) {
            lstAttachment = new ArrayList();
            PHSHumanSubjectFormAttachmentBean userAttachedFormAttachment;
            for (int index = 0; index < listSize; index++) {
                HashMap attachmentRow = (HashMap) result.elementAt(index);
                userAttachedFormAttachment = getAttachmentBean(attachmentRow);
                lstAttachment.add(userAttachedFormAttachment);
            }
        }
        return lstAttachment;
    }

    /**
     * created PHSHumanSubjectFormAttachmentBean from the values got from database call.
     *
     * @param attachmentRow hashmap got from database call
     * @return PHSHumanSubjectFormAttachmentBean
     */
    private PHSHumanSubjectFormAttachmentBean getAttachmentBean(HashMap attachmentRow) {
        PHSHumanSubjectFormAttachmentBean userAttachedFormAttachment = new PHSHumanSubjectFormAttachmentBean();
        userAttachedFormAttachment.setProposalNumber((String) attachmentRow.get("PROPOSAL_NUMBER"));
        userAttachedFormAttachment.setFormNumber(Integer.parseInt(attachmentRow.get("FORM_NUMBER").toString()));
        userAttachedFormAttachment.setFormAttachmentNumber(Integer.parseInt(attachmentRow.get("FORM_ATT_NUMBER").toString()));
        userAttachedFormAttachment.setContentId((String) attachmentRow.get("CONTENT_ID"));
        userAttachedFormAttachment.setContentType((String) attachmentRow.get("CONTENT_TYPE"));
        userAttachedFormAttachment.setUpdateTimestamp((Timestamp) attachmentRow.get("UPDATE_TIMESTAMP"));
        userAttachedFormAttachment.setUpdateUser((String) attachmentRow.get("UPDATE_USER"));
        userAttachedFormAttachment.setFilename((String) attachmentRow.get("FILE_NAME"));
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) attachmentRow.get("ATTACHMENT");
        userAttachedFormAttachment.setAttachment(byteArrayOutputStream.toByteArray());

        return userAttachedFormAttachment;
    }
    
    public PHSHumanSubjectsBean getPHSHumanSubjectHeaderDetails(String proposalNumber) throws CoeusException, DBException {
        return getPHSHumanSubjectHeaderDetails( proposalNumber,new PHSHumanSubjectsBean());
    }
    
    public PHSHumanSubjectsBean getPHSHumanSubjectHeaderDetails(String proposalNumber,PHSHumanSubjectsBean phsHumanSubjectsBean) throws CoeusException, DBException {
        //PHSHumanSubjectsBean phsHumanSubjectsBean = new PHSHumanSubjectsBean();
        Vector param = new Vector();
        Vector result = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", "call GET_PHS_HUMNSUBJT_DETAILS(<< PROPOSAL_NUMBER >>, <<OUT RESULTSET rset>> )", "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap row = (HashMap) result.elementAt(0);
            phsHumanSubjectsBean.setProposalNumber((String) row.get("PROPOSAL_NUMBER"));
            phsHumanSubjectsBean.setIsHuman((String) row.get("IS_HUMAN"));
            phsHumanSubjectsBean.setIsInvolveHumanSpecimen((String) row.get("IS_INVLV_HUMAN_SPECIMENS"));
            phsHumanSubjectsBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
            phsHumanSubjectsBean.setUpdateUser((String) row.get("UPDATE_USER"));
            if(phsHumanSubjectsBean.getIsInvolveHumanSpecimen() != null ){
                phsHumanSubjectsBean.setHeaderAcType("U");
            }else
                phsHumanSubjectsBean.setHeaderAcType("I");
        }
        else{
             phsHumanSubjectsBean.setHeaderAcType("I");
        }
        return phsHumanSubjectsBean;    
    }

    public void savePHSHumanSubHeaderDetails(PHSHumanSubjectsBean phsHumanSubjectsBean,UserInfoBean userInfoBean) throws CoeusException, DBException {

        Vector procedures = new Vector(5,3);
        Vector paramProposal= new Vector();
        paramProposal.addElement(new Parameter("av_proposal_number",DBEngineConstants.TYPE_STRING,phsHumanSubjectsBean.getProposalNumber()));
        paramProposal.addElement(new Parameter("av_is_human",DBEngineConstants.TYPE_STRING,phsHumanSubjectsBean.getIsHuman()));
        paramProposal.addElement(new Parameter("av_is_invlv_human_specimens",DBEngineConstants.TYPE_STRING,phsHumanSubjectsBean.getIsInvolveHumanSpecimen()));
        paramProposal.addElement(new Parameter("av_update_user",DBEngineConstants.TYPE_STRING,userInfoBean.getUserId()));
        paramProposal.addElement(new Parameter("av_type",DBEngineConstants.TYPE_STRING, phsHumanSubjectsBean.getHeaderAcType()));
        if("N".equalsIgnoreCase(phsHumanSubjectsBean.getIsInvolveHumanSpecimen())){
            phsHumanSubjectsBean.setPhsHumnsubjtAttachmentType(1);            
        }else{ 
            phsHumanSubjectsBean.setPhsHumnsubjtAttachmentType(2);                
         }
        StringBuffer sqlProposal = new StringBuffer(
                "call Upd_phs_humnsubjt(");
        sqlProposal.append(" <<av_proposal_number>> , ");
        sqlProposal.append(" <<av_is_human>> , ");
        sqlProposal.append(" <<av_is_invlv_human_specimens>> , ");
        sqlProposal.append(" <<av_update_user>> , ");
        sqlProposal.append(" <<av_type>> ) ");
        ProcReqParameter procProposal  = new ProcReqParameter();
        procProposal.setDSN(DSN);
        procProposal.setParameterInfo(paramProposal);
        procProposal.setSqlCommand(sqlProposal.toString());
        procedures.add(procProposal);
         if(dbEngine!=null){
            try{
                dbEngine.executeStoreProcs(procedures);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
            }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    
     public List<PHSHumanSubjectAttachments> getPHSHumanandOthrReqAttchmnts(String proposalNumber) throws Exception{
        
        Vector param = new Vector();
        Vector result = new Vector();
        List<PHSHumanSubjectAttachments> phsHumanSubjectAttList = new ArrayList<PHSHumanSubjectAttachments>();
        HashMap attachmntDetails = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        if(dbEngine != null){
           result = dbEngine.executeRequest("Coeus", "call  GET_PHS_HUMNSUBJT_ATTACHMENT(<< PROPOSAL_NUMBER >>, <<OUT RESULTSET rset>> )", "Coeus", param);
        } 
        int count = result.size();
        if (count >0){
            for(int value=0;value<count;value++)
            {
             PHSHumanSubjectAttachments phsHumanSubjectAttachments = new PHSHumanSubjectAttachments(); 
             attachmntDetails = (HashMap)result.elementAt(value);
             phsHumanSubjectAttachments.setProposalNumber((String)attachmntDetails.get("PROPOSAL_NUMBER"));
             phsHumanSubjectAttachments.setFileName((String)attachmntDetails.get("FILE_NAME"));
             phsHumanSubjectAttachments.setPhsHumnsubjtAttachmentType(Integer.parseInt(attachmntDetails.get("PHS_HUMNSUBJT_ATTACHMENT_TYPE").toString()));
             phsHumanSubjectAttachments.setAttachmentNumber(Integer.parseInt(attachmntDetails.get("ATTACHMENT_NUMBER").toString()));
             phsHumanSubjectAttachments.setContentType((String)attachmntDetails.get("CONTENT_TYPE"));
             phsHumanSubjectAttachments.setUpdateTimestamp((Timestamp)attachmntDetails.get("UPDATE_TIMESTAMP"));
             phsHumanSubjectAttachments.setUpdateUser((String)attachmntDetails.get("UPDATE_USER"));           
             phsHumanSubjectAttachments.setAcType("N");
             phsHumanSubjectAttList.add(phsHumanSubjectAttachments);
           } 
        }
        return phsHumanSubjectAttList;
    }

    public List<PHSHumanSubjectDelayedStudyBean> getgetDelayedOnsetStudy(String proposalNumber) 
        throws Exception{
        Vector param = new Vector();
        Vector result = new Vector();
        HashMap onsetStudyDetails = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        List< PHSHumanSubjectDelayedStudyBean> phsHumnSubjtDlyStdyList = new ArrayList<PHSHumanSubjectDelayedStudyBean>();
        if(dbEngine != null){
           result = dbEngine.executeRequest("Coeus", "call  get_phs_humnsubjt_dly_stdy(<< PROPOSAL_NUMBER >>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }
        int count = result.size();
        if (count >0){
            for(int value=0;value<count;value++)
            {
                PHSHumanSubjectDelayedStudyBean phsHumanSubjectDelayedStudyBean= new PHSHumanSubjectDelayedStudyBean();
                onsetStudyDetails = (HashMap)result.elementAt(value);
                phsHumanSubjectDelayedStudyBean.setProposalNumber((String)onsetStudyDetails.get("PROPOSAL_NUMBER"));
                phsHumanSubjectDelayedStudyBean.setStudyTitle((String)onsetStudyDetails.get("STUDY_TITLE"));
                phsHumanSubjectDelayedStudyBean.setStudyNumber(Integer.parseInt(onsetStudyDetails.get("STUDY_NUMBER").toString()));
                phsHumanSubjectDelayedStudyBean.setUpdateTimestamp((Timestamp)onsetStudyDetails.get("UPDATE_TIMESTAMP"));
                phsHumanSubjectDelayedStudyBean.setUpdateUser((String)onsetStudyDetails.get("UPDATE_USER"));
                phsHumanSubjectDelayedStudyBean.setIsAnticipatedCt((String)onsetStudyDetails.get("IS_ANTICIPATED_CT"));  
                phsHumanSubjectDelayedStudyBean.setContentType((String)onsetStudyDetails.get("CONTENT_TYPE"));  
                phsHumanSubjectDelayedStudyBean.setFileName((String)onsetStudyDetails.get("FILE_NAME"));  
                phsHumnSubjtDlyStdyList.add(phsHumanSubjectDelayedStudyBean);
            }   
        }

        return phsHumnSubjtDlyStdyList;
    }
     
     public boolean saveHumanAttachment(PHSHumanSubjectsBean phsHumanSubjectsBean)throws Exception {
      boolean success = false;
        Vector procedures = new Vector(5,3);
         UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
         if(phsHumanSubjectsBean.getPhsHumnsubjtAttachmentType() == 1){
         uploadDocumentBean.setDocument(phsHumanSubjectsBean.getHumanSbjtFormFile().getFileData());
         }else{
              uploadDocumentBean.setDocument(phsHumanSubjectsBean.getOtherAttmntFormFile().getFileData());
         }
                Vector paramProposalOther= new Vector();
                paramProposalOther.addElement(new Parameter("PROPOSAL_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                        phsHumanSubjectsBean.getProposalNumber()));
                paramProposalOther.addElement(new Parameter("attachment_number",
                        DBEngineConstants.TYPE_INT,
                        ""+phsHumanSubjectsBean.getAttachmentNumber()));
                paramProposalOther.addElement(new Parameter("phs_humnsubjt_attachment_type",
                        DBEngineConstants.TYPE_INT,
                        ""+phsHumanSubjectsBean.getPhsHumnsubjtAttachmentType()));  
                if(uploadDocumentBean.getDocument() != null){
                    byte data[] = uploadDocumentBean.getDocument();
               paramProposalOther.addElement(new Parameter("attachment",
                    DBEngineConstants.TYPE_BLOB, data ));
                }else{
                 paramProposalOther.addElement(new Parameter("attachment",
                    DBEngineConstants.TYPE_BLOB, new byte[0] ));   
                }
                if(phsHumanSubjectsBean.getPhsHumnsubjtAttachmentType() == 1){
                    paramProposalOther.addElement(new Parameter("file_name",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getHumanSbjtFormFile().getFileName()));
               paramProposalOther.addElement(new Parameter("content_type",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getHumanSbjtFormFile().getContentType()));
                }else{
                    paramProposalOther.addElement(new Parameter("file_name",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getOtherAttmntFormFile().getFileName()));
               paramProposalOther.addElement(new Parameter("content_type",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getOtherAttmntFormFile().getContentType()));
                }
               
               paramProposalOther.addElement(new Parameter("update_user",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getUpdateUser()));
               paramProposalOther.addElement(new Parameter("av_type",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getAcType()));

                StringBuffer sqlProposalOther = new StringBuffer(
                        "call upd_phs_humnsubjt_attachment(");
                sqlProposalOther.append(" <<PROPOSAL_NUMBER>> , ");
                sqlProposalOther.append(" <<attachment_number>> , ");
                sqlProposalOther.append(" <<phs_humnsubjt_attachment_type>> , ");
                sqlProposalOther.append(" <<attachment>> , ");
                sqlProposalOther.append(" <<file_name>> , ");
                sqlProposalOther.append(" <<content_type>> , ");
                sqlProposalOther.append(" <<update_user>> , ");
                sqlProposalOther.append(" <<av_type>> ) ");

                ProcReqParameter procProposalOther  = new ProcReqParameter();
                procProposalOther.setDSN(DSN);
                procProposalOther.setParameterInfo(paramProposalOther);
                procProposalOther.setSqlCommand(sqlProposalOther.toString());

                procedures.add(procProposalOther);
              
                
         if(dbEngine!=null){
          dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        } 
        success = true;
        return success; 
     }
     
     public boolean saveDelayedOnsetStudyDetails(PHSHumanSubjectsBean phsHumanSubjectsBean)throws Exception {
      boolean success = false;
        Vector procedures = new Vector(5,3);
         UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
         uploadDocumentBean.setDocument(phsHumanSubjectsBean.getDelayedFormFile().getFileData());
                Vector paramProposalOther= new Vector();
                paramProposalOther.addElement(new Parameter("PROPOSAL_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                        phsHumanSubjectsBean.getProposalNumber()));
                paramProposalOther.addElement(new Parameter("study_number",
                        DBEngineConstants.TYPE_INT,
                        ""+phsHumanSubjectsBean.getStudyNumber()));
                paramProposalOther.addElement(new Parameter("study_title",
                        DBEngineConstants.TYPE_STRING,
                        phsHumanSubjectsBean.getStudyTitle()));
                paramProposalOther.addElement(new Parameter("is_anticipated_ct",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getIsAnticipatedCt()));  
                if(uploadDocumentBean.getDocument() != null){
                    byte data[] = uploadDocumentBean.getDocument();
               paramProposalOther.addElement(new Parameter("attachment",
                    DBEngineConstants.TYPE_BLOB, data ));
                }else{
                 paramProposalOther.addElement(new Parameter("attachment",
                    DBEngineConstants.TYPE_BLOB, new byte[0] ));   
                }
               paramProposalOther.addElement(new Parameter("file_name",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getDelayedFormFile().getFileName()));
               paramProposalOther.addElement(new Parameter("content_type",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getDelayedFormFile().getContentType()));
               paramProposalOther.addElement(new Parameter("update_user",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getUpdateUser()));
               paramProposalOther.addElement(new Parameter("av_type",
                        DBEngineConstants.TYPE_STRING,
                        ""+phsHumanSubjectsBean.getAcType()));

                StringBuffer sqlProposalOther = new StringBuffer(
                        "call Upd_phs_humnsubjt_dly_stdy(");
                sqlProposalOther.append(" <<PROPOSAL_NUMBER>> , ");
                sqlProposalOther.append(" <<study_number>> , ");
                 sqlProposalOther.append(" <<study_title>> , ");
                sqlProposalOther.append(" <<is_anticipated_ct>> , ");
                sqlProposalOther.append(" <<attachment>> , ");
                sqlProposalOther.append(" <<file_name>> , ");
                sqlProposalOther.append(" <<content_type>> , ");
                sqlProposalOther.append(" <<update_user>> , ");
                sqlProposalOther.append(" <<av_type>> ) ");

                ProcReqParameter procProposalOther  = new ProcReqParameter();
                procProposalOther.setDSN(DSN);
                procProposalOther.setParameterInfo(paramProposalOther);
                procProposalOther.setSqlCommand(sqlProposalOther.toString());

                procedures.add(procProposalOther);
              
                
         if(dbEngine!=null){
          dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        } 
        success = true;
        return success; 
     }
          
}
