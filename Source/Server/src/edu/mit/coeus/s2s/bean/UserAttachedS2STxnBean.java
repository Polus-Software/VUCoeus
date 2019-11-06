/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.budget.bean.BudgetSubAwardAttachmentBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.exception.CoeusException;
import java.util.*;

import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.query.*;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;

/**
 *
 * @author vishal
 */
public class UserAttachedS2STxnBean {

    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon;
    QueryEngine queryEngine;
    private Timestamp timestamp;
    public static final String INSERT_RECORD = "I";
    public static final String UPDATE_RECORD = "U";
    public static final String DELETE_RECORD = "D";
    public static String userId;

    public UserAttachedS2STxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
    /**
     * creates an UserAttachedS2STxnBean
     * @param userId userId
     */
    public UserAttachedS2STxnBean(String userId) {
        dbEngine = new DBEngineImpl();
        this.userId = userId;
    }
    
    /**
     * retrieves a List of userAttachedS2SFormBean for this proposalNumber
     * @param proposalNumber proposalNumber
     * @return a List of userAttachedS2SFormBean
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on the DBEngine
     */
    public List getUserAttachedS2SForm(String proposalNumber)
            throws CoeusException, DBException {
        Vector param = new Vector();
        Vector result = new Vector();
        List lstBudgetSubAward = new ArrayList();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", "call GET_S2S_USER_ATTACHED_FORM(<< PROPOSAL_NUMBER >>, <<OUT RESULTSET rset>> )", "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0) {
            for (int count = 0; count < listSize; count++) {
                UserAttachedS2SFormBean userAttachedS2SFormBean = new UserAttachedS2SFormBean();
                HashMap mapRow = (HashMap) result.elementAt(count);
                userAttachedS2SFormBean.setProposalNumber((String) mapRow.get("PROPOSAL_NUMBER"));
                userAttachedS2SFormBean.setUserAttachedFormNumber(Integer.parseInt(mapRow.get("USER_ATTACHED_FORM_NUMBER").toString()));
                userAttachedS2SFormBean.setAwUserAttachedFormNumber(userAttachedS2SFormBean.getUserAttachedFormNumber());
                userAttachedS2SFormBean.setDescription((String) mapRow.get("DESCRIPTION"));
                userAttachedS2SFormBean.setAwDescription(userAttachedS2SFormBean.getDescription());
                userAttachedS2SFormBean.setPdfFileName((String) mapRow.get("FORM_FILE_NAME"));
                userAttachedS2SFormBean.setUpdateTimestamp((Timestamp) mapRow.get("UPDATE_TIMESTAMP"));
                userAttachedS2SFormBean.setUpdateUser((String) mapRow.get("UPDATE_USER"));
                userAttachedS2SFormBean.setNamespace((String) mapRow.get("NAMESPACE"));
                userAttachedS2SFormBean.setFormName((String) mapRow.get("FORM_NAME"));
                List attachments = getUserAttachedS2SFormAttachments(proposalNumber, userAttachedS2SFormBean.getNamespace());
                userAttachedS2SFormBean.setAttachments(attachments);
                lstBudgetSubAward.add(userAttachedS2SFormBean);
            }

        }
        return lstBudgetSubAward;
    }

    public List saveUserS2SForm(List list) throws CoeusException, DBException {
        timestamp = (new CoeusFunctions()).getDBTimestamp();
        List retList = new ArrayList();
        UserAttachedS2SFormBean userAttachedS2SFormBean;
        int maxUserAttFormNumber = -1;
        List sqlList = new ArrayList();
        List subAwards = new ArrayList();
        
        Vector vecInsert = new Vector();
        Vector vecUpdate = new Vector();
        Vector vecDelete = new Vector();
        Vector vecPDFUpdate = new Vector();
        for (int index = 0; index < list.size(); index++) {
            userAttachedS2SFormBean = (UserAttachedS2SFormBean) list.get(index);
            if (userAttachedS2SFormBean.getAcType() != null && userAttachedS2SFormBean.getAcType().equals(INSERT_RECORD)) {
                if (maxUserAttFormNumber == -1) {
                    maxUserAttFormNumber = getMaxUserAttFormNumber(userAttachedS2SFormBean.getProposalNumber());
                }
                maxUserAttFormNumber = maxUserAttFormNumber + 1;
                userAttachedS2SFormBean.setUserAttachedFormNumber(maxUserAttFormNumber);
                vecInsert.add(getProcParameter(userAttachedS2SFormBean));
                List attachments = userAttachedS2SFormBean.getAttachments();
                if(attachments!=null)
                for (int i = 0; i < attachments.size(); i++) {
                	UserAttachedS2SFormAttachmentBean attachment = (UserAttachedS2SFormAttachmentBean)attachments.get(i);
                	attachment.setUserAttachedFormNumber(maxUserAttFormNumber);
                	attachment.setUserAttachedFormAttachmentNumber(i+1);
                	vecInsert.add(getProcParameter(attachment));
				}
            } else if (userAttachedS2SFormBean.getAcType() != null && userAttachedS2SFormBean.getAcType().equals(UPDATE_RECORD)) {
                vecUpdate.add(getProcParameter(userAttachedS2SFormBean));
                List attachments = userAttachedS2SFormBean.getAttachments();
                if(attachments!=null)
                for (int i = 0; i < attachments.size(); i++) {
                	UserAttachedS2SFormAttachmentBean attachment = (UserAttachedS2SFormAttachmentBean)attachments.get(i);
                	attachment.setAwUserAttachedFormNumber(userAttachedS2SFormBean.getAwUserAttachedFormNumber());
                	if(i==0){
                		attachment.setAcType(DELETE_RECORD);
                		vecDelete.add(getProcParameter(attachment));
                	}
                	attachment.setUserAttachedFormAttachmentNumber(i+1);
                	attachment.setAcType(INSERT_RECORD);
                	vecInsert.add(getProcParameter(attachment));
				}
                
                //continue;
            } else if (userAttachedS2SFormBean.getAcType() != null && userAttachedS2SFormBean.getAcType().equals(DELETE_RECORD)) {
                vecDelete.add(getProcParameter(userAttachedS2SFormBean));
                //continue;
            } else if (userAttachedS2SFormBean.getPdfAcType() != null) {
                vecPDFUpdate.add(getProcParameter(userAttachedS2SFormBean));
            }
            if ((userAttachedS2SFormBean.getAcType() != null && !userAttachedS2SFormBean.getAcType().equals(DELETE_RECORD))) {
                if (userAttachedS2SFormBean.getAcType() != null) {
                	userAttachedS2SFormBean.setUpdateTimestamp(timestamp);
                }
                userAttachedS2SFormBean.setUpdateUser(userId);
                userAttachedS2SFormBean.setAcType(null);
                userAttachedS2SFormBean.setPdfAcType(null);
                userAttachedS2SFormBean.setAwUserAttachedFormNumber(userAttachedS2SFormBean.getUserAttachedFormNumber());
                userAttachedS2SFormBean.setUserAttachedS2SPDF(null);
                userAttachedS2SFormBean.setUserAttachedS2SXML(null);
                subAwards.add(userAttachedS2SFormBean);
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

    private Object getProcParameter(UserAttachedS2SFormAttachmentBean attachment) throws DBException{
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        if (attachment.getAcType() != null && attachment.getAcType().equals(INSERT_RECORD)) {
            sqlBudgetSubAward.append("insert into OSP$S2S_USER_ATTACHED_FORM_ATT(");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER, ");
            sqlBudgetSubAward.append(" USER_ATTACHED_FORM_NUMBER, ");
            sqlBudgetSubAward.append(" USER_ATTACHED_FORM_ATT_NUMBER, ");
                sqlBudgetSubAward.append(" CONTENT_TYPE, ");
                sqlBudgetSubAward.append(" FILE_NAME, ");
                sqlBudgetSubAward.append(" CONTENT_ID, ");
                sqlBudgetSubAward.append(" ATTACHMENT, ");
            sqlBudgetSubAward.append(" UPDATE_TIMESTAMP , ");
            sqlBudgetSubAward.append(" UPDATE_USER ) ");
            sqlBudgetSubAward.append(" VALUES (");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<USER_ATTACHED_FORM_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<USER_ATTACHED_FORM_ATT_NUMBER>> , ");
                sqlBudgetSubAward.append(" <<CONTENT_TYPE>>, ");
                sqlBudgetSubAward.append(" <<FILE_NAME>> , ");
                sqlBudgetSubAward.append(" <<CONTENT_ID>>, ");
                sqlBudgetSubAward.append(" <<ATTACHMENT>>, ");
            sqlBudgetSubAward.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudgetSubAward.append(" <<UPDATE_USER>> ) ");
        } else if (attachment.getAcType() != null && attachment.getAcType().equals(DELETE_RECORD)) {
            sqlBudgetSubAward.append("delete FROM OSP$S2S_USER_ATTACHED_FORM_ATT ");
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append("AND USER_ATTACHED_FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_USER_ATTACHED_FORM_NUMBER>> ");
        }
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", attachment.getProposalNumber()));
        param.addElement(new Parameter("USER_ATTACHED_FORM_NUMBER", "int", "" + attachment.getUserAttachedFormNumber()));
        param.addElement(new Parameter("AW_USER_ATTACHED_FORM_NUMBER", "int", "" + attachment.getAwUserAttachedFormNumber()));
        param.addElement(new Parameter("USER_ATTACHED_FORM_ATT_NUMBER", "int", attachment.getUserAttachedFormAttachmentNumber()));
        param.addElement(new Parameter("CONTENT_TYPE", "String", attachment.getContentType()));
        param.addElement(new Parameter("FILE_NAME", "String", attachment.getFilename()));
        param.addElement(new Parameter("CONTENT_ID", "String", attachment.getContentId()));
        param.addElement(new Parameter("ATTACHMENT", "Blob", (Object)attachment.getAttachment()));
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
     * @param proposalNumber proposalNumber
     * @return a int of UserAttFormNumber
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on the DBEngine
     */
    private int getMaxUserAttFormNumber(String proposalNumber) throws CoeusException, DBException {
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
        Vector result = new Vector();
        int maxUserAttFormNumber = 0;
        if (dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER MAX_USER_ATT_FORM_NUMBER>> = call FN_GET_MAX_S2S_USER_FORM_NUM( <<PROPOSAL_NUMBER>>) }", param);

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
     * @param namespace namespace
     * @return count of UserAttForms
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on the DBEngine
     */
    public boolean isFormAvailable(String proposalNumber,String namespace) throws CoeusException, DBException {
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("NAMESPACE", DBEngineConstants.TYPE_STRING, namespace));
        Vector result = new Vector();
        int formCount = 0;
        if (dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER FORM_AVAILABLE>> = call FN_IS_USER_ATTD_FORM_AVAILABLE(<<PROPOSAL_NUMBER>>,<<NAMESPACE>>) }", param);

        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            HashMap row = (HashMap) result.elementAt(0);
            formCount = Integer.parseInt(row.get("FORM_AVAILABLE").toString());
        }
        return formCount>0;
    }

    /**
     * returns a ProcReqParameter for this UserAttachedS2SFormBean
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on the DBEngine
     * @return ProcReqParameter
     */
    private ProcReqParameter getProcParameter(UserAttachedS2SFormBean userAttachedS2SFormBean)
            throws DBException {
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        //timestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean hasPDF = userAttachedS2SFormBean.getPdfAcType() != null;
        boolean hasXML = (userAttachedS2SFormBean.getUserAttachedS2SXML() != null) && (userAttachedS2SFormBean.getUserAttachedS2SXML().length > 0);
        if (userAttachedS2SFormBean.getAcType() != null && userAttachedS2SFormBean.getAcType().equals(INSERT_RECORD)) {
            sqlBudgetSubAward.append("insert into OSP$S2S_USER_ATTACHED_FORM(");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER, ");
            sqlBudgetSubAward.append(" USER_ATTACHED_FORM_NUMBER, ");
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
            sqlBudgetSubAward.append(" <<USER_ATTACHED_FORM_NUMBER>> , ");
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
        } else if (userAttachedS2SFormBean.getAcType() != null && userAttachedS2SFormBean.getAcType().equals(UPDATE_RECORD)) {
            sqlBudgetSubAward.append("update OSP$S2S_USER_ATTACHED_FORM set");
            sqlBudgetSubAward.append(" USER_ATTACHED_FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<USER_ATTACHED_FORM_NUMBER>> , ");
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
            sqlBudgetSubAward.append("AND USER_ATTACHED_FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_USER_ATTACHED_FORM_NUMBER>> ");
        } else if (userAttachedS2SFormBean.getAcType() != null && userAttachedS2SFormBean.getAcType().equals(DELETE_RECORD)) {
            sqlBudgetSubAward.append("delete FROM OSP$S2S_USER_ATTACHED_FORM ");
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append("AND USER_ATTACHED_FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_USER_ATTACHED_FORM_NUMBER>> ");
        } else if (hasPDF) {
            sqlBudgetSubAward.append("update OSP$S2S_USER_ATTACHED_FORM set");
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
            sqlBudgetSubAward.append("AND USER_ATTACHED_FORM_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_USER_ATTACHED_FORM_NUMBER>> ");
        }
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", userAttachedS2SFormBean.getProposalNumber()));
        param.addElement(new Parameter("USER_ATTACHED_FORM_NUMBER", "int", "" + userAttachedS2SFormBean.getUserAttachedFormNumber()));
        param.addElement(new Parameter("AW_USER_ATTACHED_FORM_NUMBER", "int", "" + userAttachedS2SFormBean.getAwUserAttachedFormNumber()));
        param.addElement(new Parameter("DESCRIPTION", "String", userAttachedS2SFormBean.getDescription()));
        param.addElement(new Parameter("FORM_FILE", "Blob", userAttachedS2SFormBean.getUserAttachedS2SPDF() != null ? ((Object) (userAttachedS2SFormBean.getUserAttachedS2SPDF())) : null));//((Object) (new byte[0]))));
        param.addElement(new Parameter("NAMESPACE", "String", userAttachedS2SFormBean.getNamespace()));
        param.addElement(new Parameter("FORM_NAME", "String", userAttachedS2SFormBean.getFormName()));
        param.addElement(new Parameter("FORM_FILE_NAME", "String", userAttachedS2SFormBean.getPdfFileName()));
    	param.addElement(new Parameter("XML_FILE", DBEngineConstants.TYPE_CLOB, hasXML?new String(userAttachedS2SFormBean.getUserAttachedS2SXML()):null));
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
     * @return contents of PDF Document
     * @param proposalNumber proposal number
     * @param userAttachFormNumber sub award number
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on DBEngine
     */
    public ByteArrayOutputStream getPDF(String proposalNumber, int userAttachFormNumber)
            throws CoeusException, DBException {
        return (ByteArrayOutputStream) getContents(proposalNumber, userAttachFormNumber, S2SConstants.PDF);
    }

    /**
     * retrieves the Contents of PDF document
     * @param proposalNumber proposal number
     * @param userAttachFormNumber sub award number
     * @param file PDF
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on the DBEngine
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
        sqlBudgetSubAward.append(" FROM  OSP$S2S_USER_ATTACHED_FORM WHERE");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
        sqlBudgetSubAward.append("AND USER_ATTACHED_FORM_NUMBER = <<USER_ATTACHED_FORM_NUMBER>> ");
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("USER_ATTACHED_FORM_NUMBER", "int", "" + userAttachFormNumber));
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
     * Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }

    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

	public String getUserAttachedFormXml(String proposalNumber,
			String namespace) throws DBException, CoeusException {
		String xmlFile = null;
		StringBuffer sqlUserAttachedForm = new StringBuffer();
        sqlUserAttachedForm.append("SELECT ");
        sqlUserAttachedForm.append(" PROPOSAL_NUMBER, ");
        sqlUserAttachedForm.append(" USER_ATTACHED_FORM_NUMBER, ");
        sqlUserAttachedForm.append(" XML_FILE ");
        sqlUserAttachedForm.append(" FROM  OSP$S2S_USER_ATTACHED_FORM WHERE");
        sqlUserAttachedForm.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
        sqlUserAttachedForm.append("AND NAMESPACE = <<NAMESPACE>>");
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("NAMESPACE", "String", namespace));

        ProcReqParameter procUserAttachedForm = new ProcReqParameter();
        procUserAttachedForm.setDSN("Coeus");
        procUserAttachedForm.setParameterInfo(param);
        procUserAttachedForm.setSqlCommand(sqlUserAttachedForm.toString());
        Vector result = new Vector();
        if(dbEngine != null)
            result = dbEngine.executeRequest("Coeus", sqlUserAttachedForm.toString(), "Coeus", param);
        else
            throw new CoeusException("db_exceptionCode.1000");
        int listSize = result.size();
        if(listSize > 0) {
            for(int count = 0; count < listSize; count++) {
                HashMap mapRow = (HashMap)result.elementAt(count);
                xmlFile = (String)mapRow.get("XML_FILE");
            }
        }
		return xmlFile;
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List getUserAttachedS2SFormAttachments(String proposalNumber,
			String namespace) throws DBException, CoeusException {
		List lstAttachment = new ArrayList();
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("NAMESPACE", "String", namespace));
        
        Vector result = new Vector();
        if(dbEngine != null)
            result = dbEngine.executeRequest("Coeus", "call GET_S2S_USER_ATTCHD_FORM_ATT(<< PROPOSAL_NUMBER >>, << NAMESPACE >> , <<OUT RESULTSET rset>> )", "Coeus", param);
        else
            throw new CoeusException("db_exceptionCode.1000");
        
        int listSize = result.size();
        if(listSize > 0) {
            lstAttachment = new ArrayList();
            UserAttachedS2SFormAttachmentBean userAttachedFormAttachment;
            for(int index = 0 ;index < listSize; index++) {
                HashMap attachmentRow = (HashMap)result.elementAt(index);
                userAttachedFormAttachment = getAttachmentBean(attachmentRow);
                lstAttachment.add(userAttachedFormAttachment);
            }
        }
        return lstAttachment;
	}
	
    
    /**
     * created UserAttachedS2SFormAttachmentBean from the values got from database call.
     * @param attachmentRow hashmap got from database call
     * @return UserAttachedS2SFormAttachmentBean
     */
    private UserAttachedS2SFormAttachmentBean getAttachmentBean(HashMap attachmentRow) {
    	UserAttachedS2SFormAttachmentBean userAttachedFormAttachment = new UserAttachedS2SFormAttachmentBean();
         userAttachedFormAttachment.setProposalNumber((String)attachmentRow.get("PROPOSAL_NUMBER"));
         userAttachedFormAttachment.setUserAttachedFormNumber(Integer.parseInt(attachmentRow.get("USER_ATTACHED_FORM_NUMBER").toString()));
         userAttachedFormAttachment.setUserAttachedFormAttachmentNumber(Integer.parseInt(attachmentRow.get("USER_ATTACHED_FORM_ATT_NUMBER").toString()));
         userAttachedFormAttachment.setContentId((String)attachmentRow.get("CONTENT_ID"));
         userAttachedFormAttachment.setContentType((String)attachmentRow.get("CONTENT_TYPE"));
         userAttachedFormAttachment.setUpdateTimestamp((Timestamp)attachmentRow.get("UPDATE_TIMESTAMP"));
         userAttachedFormAttachment.setUpdateUser((String)attachmentRow.get("UPDATE_USER"));
         userAttachedFormAttachment.setFilename((String)attachmentRow.get("FILE_NAME"));
         ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)attachmentRow.get("ATTACHMENT");
         userAttachedFormAttachment.setAttachment(byteArrayOutputStream.toByteArray());
         
         return userAttachedFormAttachment;
    }

}
