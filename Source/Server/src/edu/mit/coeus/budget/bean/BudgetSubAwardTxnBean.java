/*
 * BudgetSubAwardTxnBean.java
 *
 * Created on May 19, 2006, 2:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/* PMD check performed, and commented unused imports and variables 
 * on 25-JULY-2011 by Satheesh Kumar K N
 */

package edu.mit.coeus.budget.bean;

import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNameTree;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfString;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.budget.BudgetSubAwardConstants;
import edu.mit.coeus.budget.calculator.LineItemCalculator;
import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.budget.xml.BudgetSubawardTrainBudgetImpl;
import edu.mit.coeus.budget.xml.BudgetSubawardXmlBuilder;
import edu.mit.coeus.budget.xml.BudgetSubawardXmlExtract;
import edu.mit.coeus.budget.xml.BudgetSubawardXmlModifier;
import edu.mit.coeus.budget.xml.BudgetSubawardXmlModifierImpl;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.DocumentType;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.GreaterThan;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Operator;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.QueryEngine;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This transaction bean does all database operations pertaining to BudgetSubAwardBean.
 * Also it communicates to S2SServlet which inturn communicates to the grants.gov webservice to fetch the XML for the PDF
 * contained in BudgetSubAwardBean.
 * @author sharathk
 */
public class BudgetSubAwardTxnBean {
    
    /**
     * dbEngine instance
     */
    private DBEngineImpl dbEngine;
        
    private Timestamp timestamp;
    
    private final String CONTENT_KEY = "Content-Type";
    private final String CONTENT_VALUE = "application/octet-stream";
    
//    private static final String XFA_NS = "http://www.xfa.org/schema/xfa-data/1.0/";
    
    private String userId;
    
    private RequesterBean requesterBean;
    
    private static final String DUPLICATE_FILE_NAMES = "attachments contain duplicate file names";
    private static final double COST_TWENTY_FIVE_THOUSAND = 25000.00;
    private static final String TRAIN_BUDGET = "http://apply.grants.gov/forms/PHS398_TrainingSubawardBudget-V1.0";
    
    /**
     * creates an BudgetSubAwardTxnBean
     * @param userId userId
     */
    public BudgetSubAwardTxnBean(String userId) {
        dbEngine = new DBEngineImpl();
        this.userId = userId;
    }
    
    public BudgetSubAwardTxnBean(RequesterBean requesterBean) {
        dbEngine = new DBEngineImpl();
        this.requesterBean = requesterBean;
        this.userId = requesterBean.getUserName();
    }
    
    /**
     * creates an BudgetSubAwardTxnBean
     */
     public BudgetSubAwardTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
     // JM 6-25-2013 method to get organizations from proposal for combobox in subaward
    /**
      * Retrieves list of organizations from proposal for combobox in subaward
      * @param String proposalNumber : proposal number
      * @return CoeusVector cvOrganizations : vector of org data
      */
     public Vector getPropOrganizationsForSubaward(String proposalNumber)
     	throws CoeusException, DBException {
         Vector param = new Vector();
         Vector result = new Vector();

         param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
         
         if(dbEngine != null) {
             result = dbEngine.executeRequest("Coeus", "call VU_GET_EPS_PROP_SUB_ORGS(<< PROPOSAL_NUMBER >> , <<OUT RESULTSET rset>> )", 
            		 "Coeus", param);
         }
         else {
             throw new CoeusException("db_exceptionCode.1000");
         }

    	 return result;
     }
     // JM END
     
    /**
     * retreives a List of BudgetSubAward for this proposalNumber and versionNumber
     * @param proposalNumber proposalNumber
     * @param versionNumber versionNumber
     * @return a List of BudgetSubAwardBeans
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on the DBEngine
     */
    public List getBudgetSubAward(String proposalNumber, int versionNumber)
    throws CoeusException, DBException {
        Vector param = new Vector();
        Vector result = new Vector();
        Vector attachments = new Vector();
        List lstBudgetSubAward = new ArrayList();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER", "int", "" + versionNumber));
        if(dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", "call GET_BUDGET_SUB_AWARDS(<< PROPOSAL_NUMBER >>, << VERSION_NUMBER >> , <<OUT RESULTSET rset>> )", "Coeus", param);
            attachments = dbEngine.executeRequest("Coeus", "call GET_BUDGET_SUB_AWARD_ATT_DET(<< PROPOSAL_NUMBER >>, << VERSION_NUMBER >> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        int attachmentIndex = 0;
        if(listSize > 0) {
            for(int count = 0; count < listSize; count++) {
                BudgetSubAwardBean budgetSubAwardBean = new BudgetSubAwardBean();
                HashMap mapRow = (HashMap)result.elementAt(count);
                budgetSubAwardBean.setProposalNumber((String)mapRow.get("PROPOSAL_NUMBER"));
                budgetSubAwardBean.setVersionNumber(Integer.parseInt(mapRow.get("VERSION_NUMBER").toString()));
                budgetSubAwardBean.setSubAwardNumber(Integer.parseInt(mapRow.get("SUB_AWARD_NUMBER").toString()));
                budgetSubAwardBean.setAwSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                budgetSubAwardBean.setOrganizationName((String)mapRow.get("ORGANIZATION_NAME"));
                // JM 6-28-2013 need organization id too
                budgetSubAwardBean.setOrganizationId((String)mapRow.get("ORGANIZATION_ID"));
                // JM END
                // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                budgetSubAwardBean.setAwOrganizationName((String)mapRow.get("ORGANIZATION_NAME"));
                // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                budgetSubAwardBean.setSubAwardStatusCode(Integer.parseInt(mapRow.get("SUB_AWARD_STATUS_CODE").toString()));
                budgetSubAwardBean.setPdfUpdateUser((String)mapRow.get("XFD_UPDATE_USER"));
                budgetSubAwardBean.setPdfUpdateTimestamp((Timestamp)mapRow.get("XFD_UPDATE_TIMESTAMP"));
                budgetSubAwardBean.setPdfFileName((String)mapRow.get("SUB_AWARD_XFD_FILE_NAME"));
                budgetSubAwardBean.setComments((String)mapRow.get("COMMENTS"));
                budgetSubAwardBean.setXmlUpdateTimestamp((Timestamp)mapRow.get("XML_UPDATE_TIMESTAMP"));
                budgetSubAwardBean.setXmlUpdateUser((String)mapRow.get("XML_UPDATE_USER"));
                budgetSubAwardBean.setTranslationComments((String)mapRow.get("TRANSLATION_COMMENTS"));
                budgetSubAwardBean.setUpdateTimestamp((Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
                budgetSubAwardBean.setUpdateUser((String)mapRow.get("UPDATE_USER"));
                budgetSubAwardBean.setNamespace((String)mapRow.get("NAMESPACE"));
                budgetSubAwardBean.setFormName((String)mapRow.get("FORM_NAME"));
                //Check if XML timestamps are later then PDF timestamps.
                //else mark XML as U (i.e. XML Generation required)
                if(budgetSubAwardBean.getPdfUpdateTimestamp() != null && budgetSubAwardBean.getXmlUpdateTimestamp() != null) {
                    if(budgetSubAwardBean.getPdfUpdateTimestamp().compareTo(budgetSubAwardBean.getXmlUpdateTimestamp()) > 0) {
                        //XML was Generated for older PDF. Needs Regeneration
                        budgetSubAwardBean.setXmlAcType(TypeConstants.UPDATE_RECORD);
                    }
                }else if(budgetSubAwardBean.getXmlUpdateTimestamp() == null){
                    //XML was not generated for PDF. Needs Generation
                  //coeusqa-3883 start
                  if (budgetSubAwardBean.getSubAwardPDF() != null && budgetSubAwardBean.getSubAwardPDF().length > 0 )
                 //coeusqa-3883 end
                    budgetSubAwardBean.setXmlAcType(TypeConstants.UPDATE_RECORD);
                }
                
                //Attachments - START
                //Attachments are sorted by sub award number in ascending order
                int subAwardNumber;
                List lstAttachment = new ArrayList();
                for(; attachmentIndex < attachments.size(); attachmentIndex++) {
                    HashMap attachmentRow = (HashMap)attachments.elementAt(attachmentIndex);
                    subAwardNumber = Integer.parseInt(attachmentRow.get("SUB_AWARD_NUMBER").toString());
                    if(subAwardNumber != budgetSubAwardBean.getSubAwardNumber()) {
                        break;
                    }
                    BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean = new BudgetSubAwardAttachmentBean();
                    budgetSubAwardAttachmentBean.setProposalNumber((String)attachmentRow.get("PROPOSAL_NUMBER"));
                    budgetSubAwardAttachmentBean.setVersionNumber(Integer.parseInt(attachmentRow.get("VERSION_NUMBER").toString()));
                    budgetSubAwardAttachmentBean.setSubAwardNumber(subAwardNumber);
                    budgetSubAwardAttachmentBean.setContentId((String)attachmentRow.get("CONTENT_ID"));
                    budgetSubAwardAttachmentBean.setContentType((String)attachmentRow.get("CONTENT_TYPE"));
                    budgetSubAwardAttachmentBean.setUpdateTimestamp((Timestamp)attachmentRow.get("UPDATE_TIMESTAMP"));
                    budgetSubAwardAttachmentBean.setUpdateUser((String)attachmentRow.get("UPDATE_USER"));
                    
                    lstAttachment.add(budgetSubAwardAttachmentBean);
                }
                budgetSubAwardBean.setAttachments(lstAttachment);
                //Attachments - END
                // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                budgetSubAwardBean.setSubAwardPeriodDetails(
                        getSubAwardDetails(budgetSubAwardBean.getProposalNumber(),budgetSubAwardBean.getVersionNumber(),budgetSubAwardBean.getSubAwardNumber()));
                // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
                lstBudgetSubAward.add(budgetSubAwardBean);
            }
            
        }
        return lstBudgetSubAward;
    }

    private Vector createSubawardParamVector(String proposalNumber, int versionNumber, String namespace) throws DBException {
        //sqlBudgetSubAward.append("AND SUB_AWARD_NUMBER = <<SUB_AWARD_NUMBER>> ");
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER", "int", "" + versionNumber));
        param.addElement(new Parameter("NAMESPACE", "String", namespace));
        return param;
    }

    private StringBuffer createSqlSubaward() {
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        sqlBudgetSubAward.append("SELECT ");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER, ");
        sqlBudgetSubAward.append(" VERSION_NUMBER, ");
        sqlBudgetSubAward.append(" SUB_AWARD_NUMBER, ");
        sqlBudgetSubAward.append(" ORGANIZATION_NAME, ");
        sqlBudgetSubAward.append(" ORGANIZATION_ID, "); // JM 7-30-2013
        sqlBudgetSubAward.append(" SUB_AWARD_XML_FILE ");
        sqlBudgetSubAward.append(" FROM  OSP$BUDGET_SUB_AWARDS WHERE");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
        sqlBudgetSubAward.append("AND VERSION_NUMBER = <<VERSION_NUMBER>> ");
        sqlBudgetSubAward.append("AND (NAMESPACE = <<NAMESPACE>> OR NAMESPACE IS NULL)");
        return sqlBudgetSubAward;
    }
    
    private StringBuffer createSqlFedNonFedSubaward() {
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        sqlBudgetSubAward.append("SELECT ");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER, ");
        sqlBudgetSubAward.append(" VERSION_NUMBER, ");
        sqlBudgetSubAward.append(" SUB_AWARD_NUMBER, ");
        sqlBudgetSubAward.append(" ORGANIZATION_NAME, ");
        sqlBudgetSubAward.append(" ORGANIZATION_ID, "); // JM 7-30-2013
        sqlBudgetSubAward.append(" SUB_AWARD_XML_FILE ");
        sqlBudgetSubAward.append(" FROM  OSP$BUDGET_SUB_AWARDS WHERE");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
        sqlBudgetSubAward.append("AND VERSION_NUMBER = <<VERSION_NUMBER>> ");
        sqlBudgetSubAward.append("AND NAMESPACE = <<NAMESPACE>> ");
        return sqlBudgetSubAward;
    }
    
    /**
     * returns a ProcReqParameter for this BudgetSubAward
     * @param budgetSubAwardBean bean instance for which a ProcReqParameter has to be created
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on the DBEngine
     * @return ProcReqParameter
     */
    private ProcReqParameter getProcParameter(BudgetSubAwardBean budgetSubAwardBean)
    throws DBException {
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        //timestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean hasPDF = budgetSubAwardBean.getPdfAcType() != null;
        boolean hasXML = (budgetSubAwardBean.getSubAwardXML() != null) && (budgetSubAwardBean.getSubAwardXML().length > 0);
        if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
            sqlBudgetSubAward.append("insert into OSP$BUDGET_SUB_AWARDS(");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER, ");
            sqlBudgetSubAward.append(" VERSION_NUMBER, ");
            sqlBudgetSubAward.append(" SUB_AWARD_NUMBER, ");
            sqlBudgetSubAward.append(" ORGANIZATION_NAME, ");
            // JM 6-28-2013 added org id
            sqlBudgetSubAward.append(" ORGANIZATION_ID, ");
            // JM END
            sqlBudgetSubAward.append(" SUB_AWARD_STATUS_CODE, ");
            if(hasPDF) {
                sqlBudgetSubAward.append(" SUB_AWARD_XFD_FILE, ");
                sqlBudgetSubAward.append(" SUB_AWARD_XFD_FILE_NAME, ");
                sqlBudgetSubAward.append(" XFD_UPDATE_USER, ");
                sqlBudgetSubAward.append(" XFD_UPDATE_TIMESTAMP, ");
                if(hasXML) {
                    sqlBudgetSubAward.append(" SUB_AWARD_XML_FILE, ");
                    sqlBudgetSubAward.append(" XML_UPDATE_USER, ");
                    sqlBudgetSubAward.append(" XML_UPDATE_TIMESTAMP, ");
                    sqlBudgetSubAward.append(" NAMESPACE, ");
                    sqlBudgetSubAward.append(" FORM_NAME, ");
                }
            }
            sqlBudgetSubAward.append(" COMMENTS, ");
            sqlBudgetSubAward.append(" TRANSLATION_COMMENTS, ");
            sqlBudgetSubAward.append(" UPDATE_TIMESTAMP , ");
            sqlBudgetSubAward.append(" UPDATE_USER ) ");
            sqlBudgetSubAward.append(" VALUES (");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<VERSION_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<SUB_AWARD_NUMBER>> , ");
            sqlBudgetSubAward.append(" <<ORGANIZATION_NAME>> , ");
            // JM 6-28-2013 added org id
            sqlBudgetSubAward.append(" <<ORGANIZATION_ID>> , ");
            // JM END
            sqlBudgetSubAward.append(" <<SUB_AWARD_STATUS_CODE>> , ");
            if(hasPDF) {
                sqlBudgetSubAward.append(" <<SUB_AWARD_XFD_FILE>> , ");
                sqlBudgetSubAward.append(" <<SUB_AWARD_XFD_FILE_NAME>> , ");
                sqlBudgetSubAward.append(" <<XFD_UPDATE_USER>> , ");
                sqlBudgetSubAward.append(" <<XFD_UPDATE_TIMESTAMP>> , ");
                if(hasXML) {
                    sqlBudgetSubAward.append(" <<SUB_AWARD_XML_FILE>>, ");
                    sqlBudgetSubAward.append(" <<XML_UPDATE_USER>>, ");
                    sqlBudgetSubAward.append(" <<XML_UPDATE_TIMESTAMP>>, ");
                    sqlBudgetSubAward.append(" <<NAMESPACE>>, ");
                    sqlBudgetSubAward.append(" <<FORM_NAME>>, ");
                }
            }
            sqlBudgetSubAward.append(" <<COMMENTS>> , ");
            sqlBudgetSubAward.append(" <<TRANSLATION_COMMENTS>> , ");
            sqlBudgetSubAward.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudgetSubAward.append(" <<UPDATE_USER>> ) ");
        } else if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
            sqlBudgetSubAward.append("update osp$budget_sub_awards set");
            sqlBudgetSubAward.append(" SUB_AWARD_NUMBER = ");
            sqlBudgetSubAward.append(" <<SUB_AWARD_NUMBER>> , ");
            sqlBudgetSubAward.append(" ORGANIZATION_NAME = ");
            sqlBudgetSubAward.append(" <<ORGANIZATION_NAME>> , ");
            // JM 6-28-2013 added org id
            sqlBudgetSubAward.append(" ORGANIZATION_ID = ");
            sqlBudgetSubAward.append(" <<ORGANIZATION_ID>> , ");
            // JM END
            sqlBudgetSubAward.append(" SUB_AWARD_STATUS_CODE = ");
            sqlBudgetSubAward.append(" <<SUB_AWARD_STATUS_CODE>> , ");
            if(hasPDF) {
                sqlBudgetSubAward.append(" SUB_AWARD_XFD_FILE = ");
                sqlBudgetSubAward.append(" <<SUB_AWARD_XFD_FILE>> , ");
                sqlBudgetSubAward.append(" SUB_AWARD_XFD_FILE_NAME = ");
                sqlBudgetSubAward.append(" <<SUB_AWARD_XFD_FILE_NAME>> , ");
                sqlBudgetSubAward.append(" XFD_UPDATE_TIMESTAMP = ");
                sqlBudgetSubAward.append(" <<XFD_UPDATE_TIMESTAMP>> , ");
                sqlBudgetSubAward.append(" XFD_UPDATE_USER = ");
                sqlBudgetSubAward.append(" <<XFD_UPDATE_USER>> , ");
                if(hasXML) {
                    sqlBudgetSubAward.append(" SUB_AWARD_XML_FILE = ");
                    sqlBudgetSubAward.append(" <<SUB_AWARD_XML_FILE>> , ");
                    sqlBudgetSubAward.append(" XML_UPDATE_USER = ");
                    sqlBudgetSubAward.append(" <<XML_UPDATE_USER>> , ");
                    sqlBudgetSubAward.append(" XML_UPDATE_TIMESTAMP = ");
                    sqlBudgetSubAward.append(" <<XML_UPDATE_TIMESTAMP>> , ");
                    sqlBudgetSubAward.append(" NAMESPACE = ");
                    sqlBudgetSubAward.append(" <<NAMESPACE>> , ");
                    sqlBudgetSubAward.append(" FORM_NAME = ");
                    sqlBudgetSubAward.append(" <<FORM_NAME>> , ");
                }
            }
            sqlBudgetSubAward.append(" COMMENTS = ");
            sqlBudgetSubAward.append(" <<COMMENTS>> , ");
            sqlBudgetSubAward.append(" TRANSLATION_COMMENTS = ");
            sqlBudgetSubAward.append(" <<TRANSLATION_COMMENTS>> , ");
            sqlBudgetSubAward.append(" UPDATE_TIMESTAMP = ");
            sqlBudgetSubAward.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudgetSubAward.append(" UPDATE_USER = ");
            sqlBudgetSubAward.append(" <<UPDATE_USER>> ");
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append(" AND VERSION_NUMBER = ");
            sqlBudgetSubAward.append(" <<VERSION_NUMBER>> ");
            sqlBudgetSubAward.append("AND SUB_AWARD_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_SUB_AWARD_NUMBER>> ");
            sqlBudgetSubAward.append(" AND UPDATE_TIMESTAMP = ");
            sqlBudgetSubAward.append(" <<AW_UPDATE_TIMESTAMP>>  ");
        } else if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.DELETE_RECORD)) {
            sqlBudgetSubAward.append("delete FROM osp$budget_sub_awards ");
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append(" AND VERSION_NUMBER = ");
            sqlBudgetSubAward.append(" <<VERSION_NUMBER>> ");
            sqlBudgetSubAward.append("AND SUB_AWARD_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_SUB_AWARD_NUMBER>> ");
            sqlBudgetSubAward.append(" AND UPDATE_TIMESTAMP = ");
            sqlBudgetSubAward.append(" <<AW_UPDATE_TIMESTAMP>> ");
        } else if(hasPDF) {
            sqlBudgetSubAward.append("update osp$budget_sub_awards set");
            sqlBudgetSubAward.append(" SUB_AWARD_XFD_FILE = ");
            sqlBudgetSubAward.append(" <<SUB_AWARD_XFD_FILE>> , ");
            sqlBudgetSubAward.append(" SUB_AWARD_XFD_FILE_NAME = ");
            sqlBudgetSubAward.append(" <<SUB_AWARD_XFD_FILE_NAME>> , ");
            sqlBudgetSubAward.append(" XFD_UPDATE_TIMESTAMP = ");
            sqlBudgetSubAward.append(" <<XFD_UPDATE_TIMESTAMP>> , ");
            sqlBudgetSubAward.append(" XFD_UPDATE_USER = ");
            sqlBudgetSubAward.append(" <<XFD_UPDATE_USER>> , ");
            if(hasXML) {
                sqlBudgetSubAward.append(" SUB_AWARD_XML_FILE = ");
                sqlBudgetSubAward.append(" <<SUB_AWARD_XML_FILE>> , ");
                sqlBudgetSubAward.append(" XML_UPDATE_USER = ");
                sqlBudgetSubAward.append(" <<XML_UPDATE_USER>> , ");
                sqlBudgetSubAward.append(" XML_UPDATE_TIMESTAMP = ");
                sqlBudgetSubAward.append(" <<XML_UPDATE_TIMESTAMP>> , ");
                sqlBudgetSubAward.append(" NAMESPACE = ");
                sqlBudgetSubAward.append(" <<NAMESPACE>> , ");
                sqlBudgetSubAward.append(" FORM_NAME = ");
                sqlBudgetSubAward.append(" <<FORM_NAME>> , ");
            }
            sqlBudgetSubAward.append(" TRANSLATION_COMMENTS = ");
            sqlBudgetSubAward.append(" <<TRANSLATION_COMMENTS>> ");
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append(" AND VERSION_NUMBER = ");
            sqlBudgetSubAward.append(" <<VERSION_NUMBER>> ");
            sqlBudgetSubAward.append("AND SUB_AWARD_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_SUB_AWARD_NUMBER>> ");
            sqlBudgetSubAward.append(" AND UPDATE_TIMESTAMP = ");
            sqlBudgetSubAward.append(" <<AW_UPDATE_TIMESTAMP>>  ");
        }else if(budgetSubAwardBean.getXmlAcType() != null){
            sqlBudgetSubAward.append("update osp$budget_sub_awards set");
            if(hasXML) {
                sqlBudgetSubAward.append(" SUB_AWARD_XML_FILE = ");
                sqlBudgetSubAward.append(" <<SUB_AWARD_XML_FILE>> , ");
                sqlBudgetSubAward.append(" XML_UPDATE_USER = ");
                sqlBudgetSubAward.append(" <<XML_UPDATE_USER>> , ");
                sqlBudgetSubAward.append(" XML_UPDATE_TIMESTAMP = ");
                sqlBudgetSubAward.append(" <<XML_UPDATE_TIMESTAMP>> , ");
                sqlBudgetSubAward.append(" NAMESPACE = ");
                sqlBudgetSubAward.append(" <<NAMESPACE>> , ");
                sqlBudgetSubAward.append(" FORM_NAME = ");
                sqlBudgetSubAward.append(" <<FORM_NAME>> , ");
            }
            sqlBudgetSubAward.append(" TRANSLATION_COMMENTS = ");
            sqlBudgetSubAward.append(" <<TRANSLATION_COMMENTS>> ");
            sqlBudgetSubAward.append(" WHERE");
            sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
            sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
            sqlBudgetSubAward.append(" AND VERSION_NUMBER = ");
            sqlBudgetSubAward.append(" <<VERSION_NUMBER>> ");
            sqlBudgetSubAward.append("AND SUB_AWARD_NUMBER = ");
            sqlBudgetSubAward.append(" <<AW_SUB_AWARD_NUMBER>> ");
            sqlBudgetSubAward.append(" AND UPDATE_TIMESTAMP = ");
            sqlBudgetSubAward.append(" <<AW_UPDATE_TIMESTAMP>>  ");
            
        }
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", budgetSubAwardBean.getProposalNumber()));
        param.addElement(new Parameter("VERSION_NUMBER", "int", "" + budgetSubAwardBean.getVersionNumber()));
        param.addElement(new Parameter("SUB_AWARD_NUMBER", "int", "" + budgetSubAwardBean.getSubAwardNumber()));
        param.addElement(new Parameter("AW_SUB_AWARD_NUMBER", "int", "" + budgetSubAwardBean.getAwSubAwardNumber()));
        param.addElement(new Parameter("ORGANIZATION_NAME", "String", budgetSubAwardBean.getOrganizationName()));
        // JM 6-28-2013 added org id
        param.addElement(new Parameter("ORGANIZATION_ID", "String", budgetSubAwardBean.getOrganizationId()));
        // JM END
        param.addElement(new Parameter("SUB_AWARD_STATUS_CODE", "int", "" + budgetSubAwardBean.getSubAwardStatusCode()));
        param.addElement(new Parameter("SUB_AWARD_XFD_FILE", "Blob", budgetSubAwardBean.getSubAwardPDF() != null ? ((Object) (budgetSubAwardBean.getSubAwardPDF())) : null));//((Object) (new byte[0]))));
        param.addElement(new Parameter("SUB_AWARD_XFD_FILE_NAME", "String", budgetSubAwardBean.getPdfFileName()));
        param.addElement(new Parameter("COMMENTS", "String", budgetSubAwardBean.getComments()));
        param.addElement(new Parameter("XFD_UPDATE_USER", "String", userId));
        param.addElement(new Parameter("XFD_UPDATE_TIMESTAMP", "Timestamp", timestamp));
        
        param.addElement(new Parameter("SUB_AWARD_XML_FILE", DBEngineConstants.TYPE_CLOB, budgetSubAwardBean.getSubAwardXML() != null ? new String(budgetSubAwardBean.getSubAwardXML()) : null));
        param.addElement(new Parameter("XML_UPDATE_USER", "String", userId));
        param.addElement(new Parameter("XML_UPDATE_TIMESTAMP", "Timestamp", timestamp));
        param.addElement(new Parameter("NAMESPACE", "String", budgetSubAwardBean.getNamespace()));
        param.addElement(new Parameter("FORM_NAME", "String", budgetSubAwardBean.getFormName()));
        
        param.addElement(new Parameter("TRANSLATION_COMMENTS", "String", budgetSubAwardBean.getTranslationComments()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP", "Timestamp", timestamp));
        param.addElement(new Parameter("UPDATE_USER", "String", userId));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", "Timestamp", budgetSubAwardBean.getUpdateTimestamp()));
        ProcReqParameter procBudgetSubAward = new ProcReqParameter();
        procBudgetSubAward.setDSN("Coeus");
        procBudgetSubAward.setParameterInfo(param);
        procBudgetSubAward.setSqlCommand(sqlBudgetSubAward.toString());
        return procBudgetSubAward;
    }
    
    /**
     * returns a instance of ProcReqParameter to set XML document
     * @param budgetSubAwardBean bean which contains XML document to be saved
     * @return ProcReqParameter which would save the XML Document
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on the DBEngine
     */
    public ProcReqParameter getProcReqParameterToSetXML(BudgetSubAwardBean budgetSubAwardBean)throws CoeusException, DBException{
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        //Timestamp timestamp = (new CoeusFunctions()).getDBTimestamp();
        sqlBudgetSubAward.append("update osp$budget_sub_awards set");
        sqlBudgetSubAward.append(" SUB_AWARD_XML_FILE = ");
        sqlBudgetSubAward.append(" <<SUB_AWARD_XML_FILE>> , ");
        sqlBudgetSubAward.append(" XML_UPDATE_TIMESTAMP = ");
        sqlBudgetSubAward.append(" <<XML_UPDATE_TIMESTAMP>> , ");
        sqlBudgetSubAward.append(" XML_UPDATE_USER = ");
        sqlBudgetSubAward.append(" <<XML_UPDATE_USER>> ");
        sqlBudgetSubAward.append(" WHERE");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
        sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
        sqlBudgetSubAward.append(" AND VERSION_NUMBER = ");
        sqlBudgetSubAward.append(" <<VERSION_NUMBER>> ");
        sqlBudgetSubAward.append("AND SUB_AWARD_NUMBER = ");
        sqlBudgetSubAward.append(" <<AW_SUB_AWARD_NUMBER>> ");
        //sqlBudgetSubAward.append(" AND UPDATE_TIMESTAMP = ");
        //sqlBudgetSubAward.append(" <<AW_UPDATE_TIMESTAMP>>  ");
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", budgetSubAwardBean.getProposalNumber()));
        param.addElement(new Parameter("VERSION_NUMBER", "int", "" + budgetSubAwardBean.getVersionNumber()));
        //param.addElement(new Parameter("SUB_AWARD_NUMBER", "int", "" + budgetSubAwardBean.getSubAwardNumber()));
        param.addElement(new Parameter("AW_SUB_AWARD_NUMBER", "int", "" + budgetSubAwardBean.getAwSubAwardNumber()));
        param.addElement(new Parameter("SUB_AWARD_XML_FILE", DBEngineConstants.TYPE_CLOB, budgetSubAwardBean.getSubAwardXML() != null ? new String(budgetSubAwardBean.getSubAwardXML()) : "T"));
        param.addElement(new Parameter("XML_UPDATE_USER", "String", budgetSubAwardBean.getXmlUpdateUser()));
        param.addElement(new Parameter("XML_UPDATE_TIMESTAMP", "Timestamp", timestamp));
        //param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", "Timestamp", budgetSubAwardBean.getUpdateTimestamp()));
        ProcReqParameter procBudgetSubAward = new ProcReqParameter();
        procBudgetSubAward.setDSN("Coeus");
        procBudgetSubAward.setParameterInfo(param);
        procBudgetSubAward.setSqlCommand(sqlBudgetSubAward.toString());
        return procBudgetSubAward;
    }
    
    /**
     * retreives the Contents of XML/PDF document
     * @param proposalNumber proposal number
     * @param version version number
     * @param subAwardNumber sub award number
     * @param file PDF/XML
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on the DBEngine
     * @return Contents of the File requested
     */
    private Object getContents(String proposalNumber, int version, int subAwardNumber, String file)
    throws CoeusException, DBException {
        //ByteArrayOutputStream byteArrayOutputStream = null;
        Object object = null;
        String content = null;
        if(file == BudgetSubAwardConstants.PDF) {
            content = "SUB_AWARD_XFD_FILE";
        }else if(file == BudgetSubAwardConstants.XML) {
            content = "SUB_AWARD_XML_FILE";
        }
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        sqlBudgetSubAward.append("SELECT ");
        sqlBudgetSubAward.append(content);
        sqlBudgetSubAward.append(" FROM  OSP$BUDGET_SUB_AWARDS WHERE");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> ");
        sqlBudgetSubAward.append("AND VERSION_NUMBER = <<VERSION_NUMBER>> ");
        sqlBudgetSubAward.append("AND SUB_AWARD_NUMBER = <<SUB_AWARD_NUMBER>> ");
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER", "int", "" + version));
        param.addElement(new Parameter("SUB_AWARD_NUMBER", "int", "" + subAwardNumber));
        ProcReqParameter procBudgetSubAward = new ProcReqParameter();
        procBudgetSubAward.setDSN("Coeus");
        procBudgetSubAward.setParameterInfo(param);
        procBudgetSubAward.setSqlCommand(sqlBudgetSubAward.toString());
        Vector result = new Vector();
        if(dbEngine != null)
            result = dbEngine.executeRequest("Coeus", sqlBudgetSubAward.toString(), "Coeus", param);
        else
            throw new CoeusException("db_exceptionCode.1000");
        int listSize = result.size();
        if(listSize > 0) {
            HashMap mapRow = (HashMap)result.elementAt(0);
            object = mapRow.get(content);
            /*if(object instanceof ByteArrayOutputStream) {
            byteArrayOutputStream = (ByteArrayOutputStream)object;
            }else if() {
             
            }*/
        }
        return object;
    }
    
    /**
     * retreives the contents of PDF Document
     * @return contents of PDF Document
     * @param proposalNumber proposal number
     * @param version version
     * @param subAwardNumber sub award number
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on DBEngine
     */
    public ByteArrayOutputStream getPDF(String proposalNumber, int version, int subAwardNumber)
    throws CoeusException, DBException {
        return (ByteArrayOutputStream)getContents(proposalNumber, version, subAwardNumber, BudgetSubAwardConstants.PDF);
    }
    
    /**
     * retreives the contents of XML Document
     * @return contents of XML Document
     * @param proposalNumber proposalNumber
     * @param version version
     * @param subAwardNumber sub award number
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on DBEngine
     */
    public String getXML(String proposalNumber, int version, int subAwardNumber)
    throws CoeusException, DBException {
        return (String)getContents(proposalNumber, version, subAwardNumber, BudgetSubAwardConstants.XML);
    }
    
       
    public List findBudgetSubAwardXML(StringBuffer sqlBudgetSubAward, Vector param) throws CoeusException, SQLException, IOException, DBException{
        List lstBudgetSubAward = new ArrayList();
        //param.addElement(new Parameter("SUB_AWARD_NUMBER", "int", "" + subAwardNumber));
        ProcReqParameter procBudgetSubAward = new ProcReqParameter();
        procBudgetSubAward.setDSN("Coeus");
        procBudgetSubAward.setParameterInfo(param);
        procBudgetSubAward.setSqlCommand(sqlBudgetSubAward.toString());
        Vector result = new Vector();
        if(dbEngine != null)
            result = dbEngine.executeRequest("Coeus", sqlBudgetSubAward.toString(), "Coeus", param);
        else
            throw new CoeusException("db_exceptionCode.1000");
        int listSize = result.size();
        if(listSize > 0) {
            byte fileByte[];
            for(int count = 0; count < listSize; count++) {
                BudgetSubAwardBean budgetSubAwardBean = new BudgetSubAwardBean();
                HashMap mapRow = (HashMap)result.elementAt(count);
                budgetSubAwardBean.setProposalNumber((String)mapRow.get("PROPOSAL_NUMBER"));
                budgetSubAwardBean.setVersionNumber(Integer.parseInt(mapRow.get("VERSION_NUMBER").toString()));
                budgetSubAwardBean.setSubAwardNumber(Integer.parseInt(mapRow.get("SUB_AWARD_NUMBER").toString()));
                budgetSubAwardBean.setAwSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                budgetSubAwardBean.setOrganizationName((String)mapRow.get("ORGANIZATION_NAME"));
                // JM 7-30-2013
                budgetSubAwardBean.setOrganizationId((String)mapRow.get("ORGANIZATION_ID"));
                // JM END
                // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                budgetSubAwardBean.setAwOrganizationName((String)mapRow.get("ORGANIZATION_NAME"));
                // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
                //budgetSubAwardBean.setSubAwardStatusCode(Integer.parseInt(mapRow.get("SUB_AWARD_STATUS_CODE").toString()));
                //budgetSubAwardBean.setPdfUpdateUser((String)mapRow.get("XFD_UPDATE_USER"));
                //budgetSubAwardBean.setPdfUpdateTimestamp((Timestamp)mapRow.get("XFD_UPDATE_TIMESTAMP"));
                //budgetSubAwardBean.setPdfFileName((String)mapRow.get("SUB_AWARD_XFD_FILE_NAME"));
                //budgetSubAwardBean.setComments((String)mapRow.get("COMMENTS"));
                //budgetSubAwardBean.setXmlUpdateTimestamp((Timestamp)mapRow.get("XML_UPDATE_TIMESTAMP"));
                //budgetSubAwardBean.setXmlUpdateUser((String)mapRow.get("XML_UPDATE_USER"));
                //budgetSubAwardBean.setTranslationComments((String)mapRow.get("TRANSLATION_COMMENTS"));
                //budgetSubAwardBean.setUpdateTimestamp((Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
                //budgetSubAwardBean.setUpdateUser((String)mapRow.get("UPDATE_USER"));
                
                String str = (String)mapRow.get("SUB_AWARD_XML_FILE");
                if(str != null) {
                    budgetSubAwardBean.setSubAwardXML(str.toCharArray());
                }
                lstBudgetSubAward.add(budgetSubAwardBean);
            }
            
        }
        return lstBudgetSubAward;
    }
    
    /**
     * returns a list of BudgetSubAwardBeans with XML contents for final version of this Proposal
     * @param proposalNumber proposalNumber
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws java.sql.SQLException if any error occurs reading CLOB data
     * @throws java.io.IOException if any error occurs on FileSystem
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on DBEngine
     * @return list of BudgetSubAwardBeans with XML contents for this Proposal
     */
    public List getBudgetSubAwardXMLForFinalVersion(String proposalNumber,String namespace) throws CoeusException, SQLException, IOException, DBException{
        S2STxnBean s2STxnBean = new S2STxnBean();
        StringBuffer sqlBudgetSubAward = createSqlSubaward();
        Vector param = createSubawardParamVector(proposalNumber, s2STxnBean.getVersion(proposalNumber), namespace);
        return findBudgetSubAwardXML(sqlBudgetSubAward, param);
    }
    public List getBudgetFedNonFedSubAwardXMLForFinalVersion(String proposalNumber,String namespace) throws CoeusException, SQLException, IOException, DBException{
        S2STxnBean s2STxnBean = new S2STxnBean();
        StringBuffer sqlBudgetSubAward = createSqlFedNonFedSubaward();
        Vector param = createSubawardParamVector(proposalNumber, s2STxnBean.getVersion(proposalNumber), namespace);
        return findBudgetSubAwardXML(sqlBudgetSubAward, param);
    }
    
    /**
     * returns the final version for this proposal.
     * @param proposalNumber proposalnumber
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on DBEngine
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @return final version for this proposal
     */
    public int getBudgetFinalVersion(String proposalNumber)throws DBException,CoeusException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        int versionNumber = -1;
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));     
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER VERSION_NUMBER>> = "
                    +" call FN_GET_BUD_FINAL_VERSION(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }               
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            versionNumber = Integer.parseInt(rowParameter.get("VERSION_NUMBER").toString());
        }
        return versionNumber;
    }
    
        
    // Modified for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    // Sub award will be updated one by one , instance of updating all to the database at the same time
//    public List saveBudgetSubAward(List list) throws CoeusException, DBException {
//        timestamp = (new CoeusFunctions()).getDBTimestamp();
//        List retList = new ArrayList();
//        Vector vecInsert = new Vector();
//        Vector vecUpdate = new Vector();
//        Vector vecDelete = new Vector();
//        Vector vecPDFUpdate = new Vector();
//        BudgetSubAwardBean budgetSubAwardBean;
//        int maxBudgetSubAward = -1;
//        for(int index = 0; index < list.size(); index++) {
//            budgetSubAwardBean = (BudgetSubAwardBean)list.get(index);
//            if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
//                if(maxBudgetSubAward == -1){
//                    maxBudgetSubAward = getMaxBudgetSubAward(budgetSubAwardBean.getProposalNumber(), budgetSubAwardBean.getVersionNumber());
//                }
//                maxBudgetSubAward = maxBudgetSubAward + 1;
//                budgetSubAwardBean.setSubAwardNumber(maxBudgetSubAward);
//                vecInsert.add(getProcParameter(budgetSubAwardBean));
//                List attachments = budgetSubAwardBean.getAttachments();
//                if(attachments != null) {
//                    BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
//                    for(int attIndex = 0; attIndex < attachments.size(); attIndex++) {
//                        budgetSubAwardAttachmentBean = (BudgetSubAwardAttachmentBean)attachments.get(attIndex);
//                        budgetSubAwardAttachmentBean.setSubAwardNumber(maxBudgetSubAward);
//                    }
//                }
//                vecInsert = addAttachmentProcs(vecInsert, attachments);
//                //continue;
//            } else if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
//                vecUpdate.add(getProcParameter(budgetSubAwardBean));
//                boolean pdfUpdated = budgetSubAwardBean.getPdfAcType() != null;
//                boolean hasXML = (budgetSubAwardBean.getSubAwardXML() != null) && (budgetSubAwardBean.getSubAwardXML().length > 0);
//                if(pdfUpdated) {
//                    vecUpdate.add(getAttachmentDeleteProc(budgetSubAwardBean));
//                    if(hasXML){
//                        vecUpdate = addAttachmentProcs(vecUpdate, budgetSubAwardBean.getAttachments());
//                    }
//                    
//                }
//                //continue;
//            } else if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.DELETE_RECORD)) {
//                vecDelete.add(getAttachmentDeleteProc(budgetSubAwardBean));
//                vecDelete.add(getProcParameter(budgetSubAwardBean));
//                //continue;
//            } else if(budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null) {
//                vecPDFUpdate.add(getProcParameter(budgetSubAwardBean));
//                vecPDFUpdate.add(getAttachmentDeleteProc(budgetSubAwardBean));
//                boolean hasXML = (budgetSubAwardBean.getSubAwardXML() != null) && (budgetSubAwardBean.getSubAwardXML().length > 0);
//                if(hasXML) {
//                    vecPDFUpdate = addAttachmentProcs(vecPDFUpdate, budgetSubAwardBean.getAttachments());
//                }
//            }
//            
//            //Set TimeStamp and AcType to send data back to client
//            if((budgetSubAwardBean.getAcType() != null && !budgetSubAwardBean.getAcType().equals(TypeConstants.DELETE_RECORD)) ||
//                    (budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null)) {
//                if(budgetSubAwardBean.getAcType() != null) {
//                    budgetSubAwardBean.setUpdateTimestamp(timestamp);
//                }
//                budgetSubAwardBean.setUpdateUser(userId);
//                budgetSubAwardBean.setAcType(null);
//                budgetSubAwardBean.setAwSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
//                budgetSubAwardBean.setSubAwardPDF(null);
//                budgetSubAwardBean.setSubAwardXML(null);
//                if(budgetSubAwardBean.getPdfAcType() != null) {
//                    budgetSubAwardBean.setPdfUpdateTimestamp(timestamp);
//                    budgetSubAwardBean.setPdfUpdateUser(userId);
//                    budgetSubAwardBean.setPdfAcType(null);
//                    
//                    if(budgetSubAwardBean.getTranslationComments() != null && budgetSubAwardBean.getTranslationComments().equals(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY)) {
//                        budgetSubAwardBean.setXmlUpdateTimestamp(timestamp);
//                        budgetSubAwardBean.setXmlUpdateUser(userId);
//                        budgetSubAwardBean.setXmlAcType(null);
//                    }//End if TranslationComments != null...
//                    
//                }//End if pdfActype != null
//                
//                if(budgetSubAwardBean.getAttachments() != null && budgetSubAwardBean.getAttachments().size() > 0){
//                    BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
//                    for(int attachIndex = 0; attachIndex < budgetSubAwardBean.getAttachments().size(); attachIndex++){
//                        budgetSubAwardAttachmentBean = (BudgetSubAwardAttachmentBean)budgetSubAwardBean.getAttachments().get(attachIndex);
//                        budgetSubAwardAttachmentBean.setAttachment(null);
//                    }
//                }//End IF Attachments > 0
//                retList.add(budgetSubAwardBean);
//            }
//        }//End For
//        
//        List sqlList = new ArrayList();
//        sqlList.addAll(vecDelete);
//        sqlList.addAll(vecPDFUpdate);
//        sqlList.addAll(vecUpdate);
//        sqlList.addAll(vecInsert);
//        
//        if(sqlList.size() > 0) {
//            if(dbEngine != null) {
//                java.sql.Connection conn = null;
//                try {
//                    conn = dbEngine.beginTxn();
//                    for(int index = 0; index < sqlList.size(); index++) {
//                        dbEngine.batchSQLUpdate((ProcReqParameter)sqlList.get(index), conn);
//                    }
//                    dbEngine.commit(conn);
//                } catch(Exception sqlEx) {
//                    dbEngine.rollback(conn);
//                    throw new CoeusException(sqlEx.getMessage());
//                } finally {
//                    dbEngine.endTxn(conn);
//                }
//                //dbEngine.endTxn(conn);
//            } else {
//                throw new CoeusException("db_exceptionCode.1000");
//            }
//        }//End If sqlList.size() > 0
//        
//        return retList;
//    }
    /**
     * Saves a List of BudgetSubAward beans - update one bean at a time
     * @param list list of BudgetSubAwardBeans to be saved
     * @return List of transalationComments
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error occurs on DBEngine
     */
    public List saveBudgetSubAward(List list) throws CoeusException, DBException {
        timestamp = (new CoeusFunctions()).getDBTimestamp();
        List retList = new ArrayList();
        List subAwards = new ArrayList();
        boolean isCostElementInactive = false;
        boolean isCostElemInactiveToClientSide = false;
        BudgetSubAwardBean budgetSubAwardBean;
        int maxBudgetSubAward = -1;
        for(int index = 0; index < list.size(); index++) {
            String subAwardAcType = "";
            Vector vecInsert = new Vector();
            Vector vecUpdate = new Vector();
            Vector vecDelete = new Vector();
            Vector vecPDFUpdate = new Vector();
            budgetSubAwardBean = (BudgetSubAwardBean)list.get(index);
            subAwardAcType = budgetSubAwardBean.getAcType();
            if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                if(maxBudgetSubAward == -1){
                    maxBudgetSubAward = getMaxBudgetSubAward(budgetSubAwardBean.getProposalNumber(), budgetSubAwardBean.getVersionNumber());
                }
                maxBudgetSubAward = maxBudgetSubAward + 1;
                budgetSubAwardBean.setSubAwardNumber(maxBudgetSubAward);
                vecInsert.add(getProcParameter(budgetSubAwardBean));
                List attachments = budgetSubAwardBean.getAttachments();
                if(attachments != null) {
                    BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
                    for(int attIndex = 0; attIndex < attachments.size(); attIndex++) {
                        budgetSubAwardAttachmentBean = (BudgetSubAwardAttachmentBean)attachments.get(attIndex);
                        budgetSubAwardAttachmentBean.setSubAwardNumber(maxBudgetSubAward);
                    }
                }
                vecInsert = addAttachmentProcs(vecInsert, attachments);
            } else if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                vecUpdate.add(getProcParameter(budgetSubAwardBean));
                boolean pdfUpdated = budgetSubAwardBean.getPdfAcType() != null;
                boolean hasXML = (budgetSubAwardBean.getSubAwardXML() != null) && (budgetSubAwardBean.getSubAwardXML().length > 0);
                if(pdfUpdated) {
                    vecUpdate.add(getAttachmentDeleteProc(budgetSubAwardBean));
                    if(hasXML){
                        vecUpdate = addAttachmentProcs(vecUpdate, budgetSubAwardBean.getAttachments());
                    }
                    
                }
                //continue;
            } else if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.DELETE_RECORD)) {
                vecDelete.add(getAttachmentDeleteProc(budgetSubAwardBean));
                vecDelete.add(getProcParameter(budgetSubAwardBean));
                //continue;
            } else if(budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null) {
                vecPDFUpdate.add(getProcParameter(budgetSubAwardBean));
                vecPDFUpdate.add(getAttachmentDeleteProc(budgetSubAwardBean));
                boolean hasXML = (budgetSubAwardBean.getSubAwardXML() != null) && (budgetSubAwardBean.getSubAwardXML().length > 0);
                if(hasXML) {
                    vecPDFUpdate = addAttachmentProcs(vecPDFUpdate, budgetSubAwardBean.getAttachments());
                }
            }

            
            //Set TimeStamp and AcType to send data back to client
            if((budgetSubAwardBean.getAcType() != null && !budgetSubAwardBean.getAcType().equals(TypeConstants.DELETE_RECORD)) ||
                    (budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null)) {
                if(budgetSubAwardBean.getAcType() != null) {
                    budgetSubAwardBean.setUpdateTimestamp(timestamp);
                }
                budgetSubAwardBean.setUpdateUser(userId);
                budgetSubAwardBean.setAcType(null);
                budgetSubAwardBean.setAwSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                budgetSubAwardBean.setSubAwardPDF(null);
                budgetSubAwardBean.setSubAwardXML(null);
                String pdfAcType = budgetSubAwardBean.getPdfAcType();
                if(budgetSubAwardBean.getPdfAcType() != null) {
                    budgetSubAwardBean.setPdfUpdateTimestamp(timestamp);
                    budgetSubAwardBean.setPdfUpdateUser(userId);
                    budgetSubAwardBean.setPdfAcType(null);
                    
                    if(budgetSubAwardBean.getTranslationComments() != null && budgetSubAwardBean.getTranslationComments().equals(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY)) {
                        budgetSubAwardBean.setXmlUpdateTimestamp(timestamp);
                        budgetSubAwardBean.setXmlUpdateUser(userId);
                        budgetSubAwardBean.setXmlAcType(null);
                    }//End if TranslationComments != null...
                    
                }//End  
                
                if(budgetSubAwardBean.getAttachments() != null && budgetSubAwardBean.getAttachments().size() > 0){
                    BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
                    for(int attachIndex = 0; attachIndex < budgetSubAwardBean.getAttachments().size(); attachIndex++){
                        budgetSubAwardAttachmentBean = (BudgetSubAwardAttachmentBean)budgetSubAwardBean.getAttachments().get(attachIndex);
                        budgetSubAwardAttachmentBean.setAttachment(null);
                    }
                }//End  
               
                // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - Start
                // Will delete the sub award period details based on the PDF data and insert the new period details to the sub award details
                Vector vecPDFSubawardDetails = budgetSubAwardBean.getPDFSubAwardPeriodDetails();
                Vector vecActualSubAwardPeriodDetails = budgetSubAwardBean.getSubAwardPeriodDetails();
                if(vecPDFSubawardDetails != null && !vecPDFSubawardDetails.isEmpty()){
                    if(vecActualSubAwardPeriodDetails != null && !vecActualSubAwardPeriodDetails.isEmpty()){
                        for(Object subAwardDetails : vecActualSubAwardPeriodDetails){
                            BudgetSubAwardDetailBean subAwardDetailBean = (BudgetSubAwardDetailBean)subAwardDetails;
                            String acType = subAwardDetailBean.getAcType();
                            subAwardDetailBean.setAcType(TypeConstants.DELETE_RECORD);
                            updateSubAwardDetails(subAwardDetailBean);
                            subAwardDetailBean.setAcType(acType);
                        }
                    }
                    Vector mergedPeriodDetails = mergePeriodDetailsFromPDF(vecPDFSubawardDetails,budgetSubAwardBean.getSubAwardPeriodDetails());
                    budgetSubAwardBean.setSubAwardPeriodDetails(mergedPeriodDetails);
                }
                // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - End
            }
            List sqlList = new ArrayList();
            sqlList.addAll(vecDelete);
            sqlList.addAll(vecPDFUpdate);
            sqlList.addAll(vecUpdate);
            sqlList.addAll(vecInsert);
            
            if(sqlList.size() > 0) {
                if(dbEngine != null) {
                    java.sql.Connection conn = null;
                    try {
                        conn = dbEngine.beginTxn();
                        for(int sqlQueryIndex = 0; sqlQueryIndex < sqlList.size(); sqlQueryIndex++) {
                            dbEngine.batchSQLUpdate((ProcReqParameter)sqlList.get(sqlQueryIndex), conn);
                        }
                        dbEngine.commit(conn);
                    } catch(Exception sqlEx) {
                        dbEngine.rollback(conn);
                        throw new CoeusException(sqlEx.getMessage());
                    } finally {
                        dbEngine.endTxn(conn);
                    }
                    
                } else {
                    throw new CoeusException("db_exceptionCode.1000");
                }
            }//End 
            
             // Update the sub award details - Start
            boolean isSubAwardDetailsInsertOrModified = false;
            Vector vecSubAwardPeriodDetails = budgetSubAwardBean.getSubAwardPeriodDetails();
            if(vecSubAwardPeriodDetails != null && !vecSubAwardPeriodDetails.isEmpty()){
                for(Object subAwardDetails : vecSubAwardPeriodDetails){
                    BudgetSubAwardDetailBean subAwardDetailBean = (BudgetSubAwardDetailBean)subAwardDetails;
                    subAwardDetailBean.setSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                    subAwardDetailBean.setProposalNumber(budgetSubAwardBean.getProposalNumber());
                    subAwardDetailBean.setVersionNumber(budgetSubAwardBean.getVersionNumber());
                    // Deltes the sub award detail when the sub award is deleted
                    if(TypeConstants.DELETE_RECORD.equals(subAwardAcType) && 
                            (TypeConstants.UPDATE_RECORD.equals(subAwardDetailBean.getAcType()) || 
                            "".equals(subAwardDetailBean.getAcType()) || subAwardDetailBean.getAcType() == null)){
                        subAwardDetailBean.setAcType(TypeConstants.DELETE_RECORD);
                    }

                    if(subAwardDetailBean.getAcType() != null){
                        updateSubAwardDetails(subAwardDetailBean);
                        subAwardDetailBean.setAcType(null);
                        isSubAwardDetailsInsertOrModified = true;
                    }
                }
            }
            // Once the sub award details are modified or inserted, get back the details from the database
            if(isSubAwardDetailsInsertOrModified){
                budgetSubAwardBean.setSubAwardPeriodDetails(
                        getSubAwardDetails(budgetSubAwardBean.getProposalNumber(),budgetSubAwardBean.getVersionNumber(),budgetSubAwardBean.getSubAwardNumber()));
            }
            // Update the sub award details - End
            // Deleting the budget line items specific for the sub award, when the sub award is deleted or sub award details are modified
           BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userId);
           if(TypeConstants.DELETE_RECORD.equals(subAwardAcType) || isSubAwardDetailsInsertOrModified){
               budgetUpdateTxnBean.deleteSubAwardCostLineItem(budgetSubAwardBean.getProposalNumber(),
                       budgetSubAwardBean.getVersionNumber(),
                       budgetSubAwardBean.getSubAwardNumber());
           }
           // Checks when there is no line item details and sub award details are not modified, line item will be generated
           if(!isSubAwardDetailsInsertOrModified && !TypeConstants.DELETE_RECORD.equals(subAwardAcType)){
             boolean budgetHasSubAwardLineItem = budgetUpdateTxnBean.checkBudgetHasSubawardLineItem(budgetSubAwardBean.getProposalNumber(),
                                           budgetSubAwardBean.getVersionNumber(),
                                           budgetSubAwardBean.getSubAwardNumber());
               if(!budgetHasSubAwardLineItem){
                   isCostElementInactive = createLineItemsForSubAward(budgetSubAwardBean.getProposalNumber(), budgetSubAwardBean.getVersionNumber(),
                           budgetSubAwardBean.getOrganizationName() ,vecSubAwardPeriodDetails);
                   if(isCostElementInactive){
                       isCostElemInactiveToClientSide = true;
                   }
               }
           }
           // Line item for sub award will be generate, only when the sub award details or inserted or modified
           if(isSubAwardDetailsInsertOrModified && !TypeConstants.DELETE_RECORD.equals(subAwardAcType)){
               isCostElementInactive = createLineItemsForSubAward(budgetSubAwardBean.getProposalNumber(),budgetSubAwardBean.getVersionNumber(),
                       budgetSubAwardBean.getOrganizationName(),vecSubAwardPeriodDetails);
               if(isCostElementInactive){
                   isCostElemInactiveToClientSide = true;
               }
               
           }else if(TypeConstants.UPDATE_RECORD.equals(subAwardAcType) && vecSubAwardPeriodDetails != null && !vecSubAwardPeriodDetails.isEmpty()){
               // All the sub award line item description will be updated, when the organization name is modified
               budgetUpdateTxnBean.updateSubAwdLineItemDescription(budgetSubAwardBean);
           }
            subAwards.add(budgetSubAwardBean);
           
        }//End For
        retList.add(0,subAwards);
        
        retList.add(1,new Boolean(isCostElemInactiveToClientSide));
        return retList;
    }
    // Modified for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    
    /**
     * Used for translate, so no need to check for actype == delete
     * @param budgetSubAwardBean budgetSubAwardBean to save
     * @return BudgetSubAwardBean with update timestamps and acTypes reset.
     * @throws edu.mit.coeus.exception.CoeusException if any other error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException id database error occurs
     */
    public BudgetSubAwardBean saveBudgetSubAward(BudgetSubAwardBean budgetSubAwardBean) throws CoeusException, DBException{
        timestamp = (new CoeusFunctions()).getDBTimestamp();
        Vector vecInsert = new Vector();
        Vector vecUpdate = new Vector();
        Vector vecPDFUpdate = new Vector();
        if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
            budgetSubAwardBean.setSubAwardNumber(getMaxBudgetSubAward(budgetSubAwardBean.getProposalNumber(), budgetSubAwardBean.getVersionNumber()) + 1);
            budgetSubAwardBean.setAwSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
            vecInsert.add(getProcParameter(budgetSubAwardBean));
            vecInsert = addAttachmentProcs(vecInsert, budgetSubAwardBean.getAttachments());
            //continue;
        } else if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
            vecUpdate.add(getProcParameter(budgetSubAwardBean));
            vecUpdate.add(getAttachmentDeleteProc(budgetSubAwardBean));
            vecUpdate = addAttachmentProcs(vecUpdate, budgetSubAwardBean.getAttachments());
            //continue;
        } else if(budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null) {
            vecPDFUpdate.add(getProcParameter(budgetSubAwardBean));
            vecPDFUpdate.add(getAttachmentDeleteProc(budgetSubAwardBean));
            vecPDFUpdate = addAttachmentProcs(vecPDFUpdate, budgetSubAwardBean.getAttachments());
        }
        List sqlList = new ArrayList();
        sqlList.addAll(vecPDFUpdate);
        sqlList.addAll(vecUpdate);
        sqlList.addAll(vecInsert);
        if(sqlList.size() > 0) {
            if(dbEngine != null) {
                java.sql.Connection conn = null;
                try {
                    conn = dbEngine.beginTxn();
                    for(int index = 0; index < sqlList.size(); index++) {
                        dbEngine.batchSQLUpdate((ProcReqParameter)sqlList.get(index), conn);
                    }
                    dbEngine.commit(conn);
                } catch(Exception sqlEx) {
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                } finally {
                    dbEngine.endTxn(conn);
                }
                //dbEngine.endTxn(conn);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }
        }//End If sqlList.size() > 0

        budgetSubAwardBean.setUpdateTimestamp(timestamp);
        budgetSubAwardBean.setUpdateUser(userId);
        if(budgetSubAwardBean.getTranslationComments() != null && budgetSubAwardBean.getTranslationComments().equals(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY)) {
            budgetSubAwardBean.setXmlUpdateTimestamp(timestamp);
            budgetSubAwardBean.setPdfUpdateTimestamp(timestamp);
            budgetSubAwardBean.setXmlUpdateUser(userId);
            budgetSubAwardBean.setPdfUpdateUser(userId);
        }
        
        return budgetSubAwardBean;
    }
    
    /**
     * returns max budget Award number for this proposal number and version number
     * @param proposalNumber proposal number
     * @param version version number
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if DBEngine Error occurs
     * @return max budget Award number for this proposal number and version number
     */
    private int getMaxBudgetSubAward(String proposalNumber, int version)throws CoeusException, DBException {
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("VERSION", DBEngineConstants.TYPE_INT, ""+version));
        
        Vector result = new Vector(); 
        int maxBudgetSubAward = 0;
        if(dbEngine !=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER MAX_BUDGET_SUB_AWARD>> = call FN_GET_MAX_BUD_SUB_AWARD_NUM( <<PROPOSAL_NUMBER>> , <<VERSION>>) }", param);
            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap row = (HashMap)result.elementAt(0);
            maxBudgetSubAward = Integer.parseInt(row.get("MAX_BUDGET_SUB_AWARD").toString());
        }
        return maxBudgetSubAward;
    }
    
    /**
     * connects to servlet for retreiving results from web service
     * @param object object to send
     * @param servletPath servlet which connects to web service
     * @return result
     * @throws java.io.IOException if servlet streaming fails
     * @throws java.net.MalformedURLException if servlet URL is incorrect or the servlet service unavailable
     * @throws java.lang.ClassNotFoundException ClassNotFoundException
     */
    public Object getXMLFromPureEdge(Object object, String servletPath)throws IOException, MalformedURLException, ClassNotFoundException {
        
        //RequesterBean request = new RequesterBean();
        requesterBean.setUserName(userId);
        requesterBean.setDataObject(object);
        requesterBean.setFunctionType(S2SConstants.GET_XML_FROM_PURE_EDGE);
        
        URL urlSrvServlet = new URL(servletPath);
        URLConnection servletConnection = urlSrvServlet.openConnection();
        // prepare for both input and output
        servletConnection.setDoInput(true);
        servletConnection.setDoOutput(true);
        // turn off caching
        servletConnection.setUseCaches(false);
        // Specify the content type that we will send binary data
        servletConnection.setRequestProperty(CONTENT_KEY, CONTENT_VALUE);
        // send the requester object to the servlet using serialization
        ObjectOutputStream outputToServlet = new ObjectOutputStream(servletConnection.getOutputStream());
        // serialize the object and send to servlet
        outputToServlet.writeObject(requesterBean);
        outputToServlet.flush();
        outputToServlet.close();
        
        // read the object
        InputStream inputToApplet = servletConnection.getInputStream();
        ResponderBean response = (ResponderBean) (new ObjectInputStream(inputToApplet)).readObject();
        
        return response.getDataObject();
    }
    
    /**
     * connects to servlet for retreiving results from web service
     * @param lstBudgetSubAwardBean list of Beans
     * @param servletPath servlet which connects to web service
     * @return result
     * @throws java.io.IOException if servlet streaming fails
     * @throws java.net.MalformedURLException if servlet URL is incorrect or the servlet service unavailable
     * @throws java.lang.ClassNotFoundException ClassNotFoundException
     */
    public List getXMLFromPureEdge(List lstBudgetSubAwardBean, String servletPath)throws IOException, MalformedURLException, ClassNotFoundException {
        //Check if contains PDF data, else get PDF data and populate Bean
        BudgetSubAwardBean budgetSubAwardBean;
        ByteArrayOutputStream byteArrayOutputStream;
        boolean delete = false;
        for(int index = 0; index < lstBudgetSubAwardBean.size(); index++) {
            budgetSubAwardBean = (BudgetSubAwardBean)lstBudgetSubAwardBean.get(index);
            delete = budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.DELETE_RECORD);
            if(!delete && (budgetSubAwardBean.getSubAwardPDF() == null || budgetSubAwardBean.getSubAwardPDF().length == 0)) {
                //Catch exception within loop, coz it could continue with other PDF's.
                try{
                    byteArrayOutputStream = getPDF(budgetSubAwardBean.getProposalNumber(), budgetSubAwardBean.getVersionNumber(), budgetSubAwardBean.getSubAwardNumber());
                    budgetSubAwardBean.setSubAwardPDF(byteArrayOutputStream.toByteArray());
                }catch (Exception exception) {
                    UtilFactory.log(exception.getMessage(),exception,"BudgetSubAwardTxnBean","getXMLFromPureEdge");
                }
            }
        }
        return (List)getXMLFromPureEdge((Object)lstBudgetSubAwardBean, servletPath);
    }
    
    /**
     * connects to servlet for retreiving results from web service
     * @param budgetSubAwardBean BudgetSubAwardBean
     * @param servletPath servlet which connects to web service
     * @return result
     * @throws java.io.IOException if servlet streaming fails
     * @throws java.net.MalformedURLException if servlet URL is incorrect or the servlet service unavailable
     * @throws java.lang.ClassNotFoundException ClassNotFoundException
     */
    public BudgetSubAwardBean getXMLFromPureEdge(BudgetSubAwardBean budgetSubAwardBean, String servletPath) throws IOException, MalformedURLException, ClassNotFoundException {
        return (BudgetSubAwardBean)getXMLFromPureEdge((Object)budgetSubAwardBean, servletPath);
    }
        
    /**
     * calls webservice for retreiving XML only of required.
     * @param budgetSubAwardBean bean to be checked and updated before saving to database for XML Stream.
     * @param servletPath servlet which connects to web service
     * @throws java.io.IOException if servlet streaming fails
     * @throws java.net.MalformedURLException if servlet URL is incorrect or the servlet service unavailable
     * @throws java.lang.ClassNotFoundException ClassNotFoundException
     * @return updated BudgetSubAwardBean
     */
    public BudgetSubAwardBean checkAndUpdate(BudgetSubAwardBean budgetSubAwardBean, String servletPath)throws IOException, MalformedURLException, ClassNotFoundException {
        if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.DELETE_RECORD)) {
            return budgetSubAwardBean;
        }
        DocumentTypeChecker documentTypeChecker = new DocumentTypeChecker();
        byte fileContents[] = budgetSubAwardBean.getSubAwardPDF();
        DocumentType documentType = null;
        try {
            documentType = documentTypeChecker.getDocumentType(fileContents);
        }catch(Exception exception) {
            //Could Not Determine Document Type
            UtilFactory.log(exception.getMessage(), exception, "BudgetSubAwardTxnBean","checkAndUpdate");
            budgetSubAwardBean.setTranslationComments(exception.getMessage()==null?BudgetSubAwardConstants.COULD_NOT_DETERMINE_DOC_TYPE:exception.getMessage());
        }
        
        if(documentType != null && documentType.getMimeType().equals(DocumentConstants.MIME_PDF)
            && (budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null)){
            try{
                /*
                byte xmlBytes[] = getXMLFromPDF(fileContents);
                String str = new String(xmlBytes);
                budgetSubAwardBean.setSubAwardXML(str.toCharArray());
                budgetSubAwardBean.setTranslationComments(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY);
                budgetSubAwardBean.setXmlUpdateUser(userId);
                budgetSubAwardBean.setAttachments(new ArrayList()); //PDF would not contain attachments as of now
                */
                budgetSubAwardBean = updateSubAward(budgetSubAwardBean);
                budgetSubAwardBean.setTranslationComments(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY);
                budgetSubAwardBean.setXmlUpdateUser(userId);
                
            }catch(Exception exception) {
                //Could not extract XML from PDF. could be a faulty PDF.
                UtilFactory.log(exception.getMessage(), exception, "BudgetSubAwardTxnBean","checkAndUpdate");
                budgetSubAwardBean.setTranslationComments(exception.getMessage()==null?BudgetSubAwardConstants.COULD_NOT_EXTRACT_XML_FROM_PDF:exception.getMessage());
            }
        }else if//(documentType != null && documentType.getMimeType().equals(DocumentConstants.MIME_XFD) && //no Need tp check for Document Type, If Document Type is null treat it as PDF
                (budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null) {
            budgetSubAwardBean = getXMLFromPureEdge(budgetSubAwardBean, servletPath);
        }

        return budgetSubAwardBean;
    }
    
    /**
     * generate ProcReqParamaters for list of BudgetSubAwardAttachmentBean
     * @param procVector Vector to add ProcReqParamaters generated from BudgetSubAwardAttachmentBean
     * @param attachmentBeans beans for which procReqParameters have to be created.
     * @return vector
     * @throws edu.mit.coeus.utils.dbengine.DBException if any exception occurs
     */
    private Vector addAttachmentProcs(Vector procVector, List attachmentBeans) throws DBException{
        if(attachmentBeans != null) {
            BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
            ProcReqParameter procReqParameter;
            for(int index = 0; index < attachmentBeans.size(); index++) {
                budgetSubAwardAttachmentBean = (BudgetSubAwardAttachmentBean)attachmentBeans.get(index);
                procReqParameter = getAttachmentProc(budgetSubAwardAttachmentBean);
                procVector.add(procReqParameter);
            }//End For
        }//End if attachmentBeans != null
        return procVector;
    }
    
    /**
     * generate ProcReqParamater for BudgetSubAwardAttachmentBean
     * @param budgetSubAwardAttachmentBean for which ProcReqParamater has to be generated.
     * @return ProcReqParameter
     * @throws edu.mit.coeus.utils.dbengine.DBException if any exception occurs
     */
    private ProcReqParameter getAttachmentProc(BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean)throws DBException{
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        //timestamp = (new CoeusFunctions()).getDBTimestamp();
        
        sqlBudgetSubAward.append("insert into OSP$BUDGET_SUB_AWARD_ATT(");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER, ");
        sqlBudgetSubAward.append(" VERSION_NUMBER, ");
        sqlBudgetSubAward.append(" SUB_AWARD_NUMBER, ");
        sqlBudgetSubAward.append(" CONTENT_ID, ");
        sqlBudgetSubAward.append(" CONTENT_TYPE, ");
        sqlBudgetSubAward.append(" ATTACHMENT, ");
        sqlBudgetSubAward.append(" UPDATE_TIMESTAMP , ");
        sqlBudgetSubAward.append(" UPDATE_USER ) ");
        sqlBudgetSubAward.append(" VALUES (");
        sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudgetSubAward.append(" <<VERSION_NUMBER>> , ");
        sqlBudgetSubAward.append(" <<SUB_AWARD_NUMBER>> , ");
        sqlBudgetSubAward.append(" <<CONTENT_ID>> , ");
        sqlBudgetSubAward.append(" <<CONTENT_TYPE>> , ");
        sqlBudgetSubAward.append(" <<ATTACHMENT>> , ");
        sqlBudgetSubAward.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudgetSubAward.append(" <<UPDATE_USER>> ) ");

        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", budgetSubAwardAttachmentBean.getProposalNumber()));
        param.addElement(new Parameter("VERSION_NUMBER", "int", "" + budgetSubAwardAttachmentBean.getVersionNumber()));
        param.addElement(new Parameter("SUB_AWARD_NUMBER", "int", "" + budgetSubAwardAttachmentBean.getSubAwardNumber()));
        param.addElement(new Parameter("CONTENT_ID", "String", budgetSubAwardAttachmentBean.getContentId()));
        param.addElement(new Parameter("CONTENT_TYPE", "String", budgetSubAwardAttachmentBean.getContentType()));
        param.addElement(new Parameter("ATTACHMENT", DBEngineConstants.TYPE_BLOB, budgetSubAwardAttachmentBean.getAttachment()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP", "Timestamp", timestamp));
        param.addElement(new Parameter("UPDATE_USER", "String", userId));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", "Timestamp", budgetSubAwardAttachmentBean.getUpdateTimestamp()));
        
        ProcReqParameter procBudgetSubAwardAttachment = new ProcReqParameter();
        procBudgetSubAwardAttachment.setDSN("Coeus");
        procBudgetSubAwardAttachment.setParameterInfo(param);
        procBudgetSubAwardAttachment.setSqlCommand(sqlBudgetSubAward.toString());
        return procBudgetSubAwardAttachment;
    }
    
    /**
     * generate procReqParameter for deleting Attachments
     * @param budgetSubAwardBean generate procReqParameter for deleting this attachment bean
     * @throws edu.mit.coeus.utils.dbengine.DBException if any exception occurs
     * @return procReqParameter
     */
    private ProcReqParameter getAttachmentDeleteProc(BudgetSubAwardBean budgetSubAwardBean) throws DBException {
        StringBuffer sqlBudgetSubAward = new StringBuffer();
        sqlBudgetSubAward.append("delete FROM OSP$BUDGET_SUB_AWARD_ATT ");
        sqlBudgetSubAward.append(" WHERE");
        sqlBudgetSubAward.append(" PROPOSAL_NUMBER = ");
        sqlBudgetSubAward.append(" <<PROPOSAL_NUMBER>> ");
        sqlBudgetSubAward.append(" AND VERSION_NUMBER = ");
        sqlBudgetSubAward.append(" <<VERSION_NUMBER>> ");
        sqlBudgetSubAward.append("AND SUB_AWARD_NUMBER = ");
        sqlBudgetSubAward.append(" <<SUB_AWARD_NUMBER>> ");
        
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", budgetSubAwardBean.getProposalNumber()));
        param.addElement(new Parameter("VERSION_NUMBER", "int", "" + budgetSubAwardBean.getVersionNumber()));
        param.addElement(new Parameter("SUB_AWARD_NUMBER", "int", "" + budgetSubAwardBean.getAwSubAwardNumber()));
        
        ProcReqParameter procBudgetSubAwardAttachment = new ProcReqParameter();
        procBudgetSubAwardAttachment.setDSN("Coeus");
        procBudgetSubAwardAttachment.setParameterInfo(param);
        procBudgetSubAwardAttachment.setSqlCommand(sqlBudgetSubAward.toString());
        return procBudgetSubAwardAttachment;
        
    }
    
    /**
     * gets attachment for this proposal number, version number, sub award number and content id.
     * @param proposalNumber proposalNumber
     * @param version version
     * @param subAwardNumber subAwardNumber
     * @param contentId contentId
     * @return BudgetSubAwardAttachmentBean
     * @throws edu.mit.coeus.exception.CoeusException if any exception occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any Database Exception occurs
     */
    public BudgetSubAwardAttachmentBean getAttachment(String proposalNumber, int version, int subAwardNumber, String contentId) throws CoeusException, DBException {
        BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean = null;
        
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER", "int", "" + version));
        param.addElement(new Parameter("SUB_AWARD_NUMBER", "int", "" + subAwardNumber));
        param.addElement(new Parameter("CONTENT_ID", "String", "" + contentId));
        
        Vector result = new Vector();
        StringBuffer sqlBuffer = new StringBuffer("call GET_BUDGET_SUB_AWARD_ATT(");
        sqlBuffer.append("<< PROPOSAL_NUMBER >>,");
        sqlBuffer.append("<< VERSION_NUMBER >> ,");
        sqlBuffer.append("<< SUB_AWARD_NUMBER >>,");
        sqlBuffer.append("<< CONTENT_ID >>,");
        sqlBuffer.append("<<OUT RESULTSET rset>> )");
        
        if(dbEngine != null)
            result = dbEngine.executeRequest("Coeus", sqlBuffer.toString(), "Coeus", param);
        else
            throw new CoeusException("db_exceptionCode.1000");
        
        int listSize = result.size();
        if(listSize > 0) {
            HashMap attachmentRow = (HashMap)result.elementAt(0);
            budgetSubAwardAttachmentBean = getAttachmentBean(attachmentRow);
        }
        return budgetSubAwardAttachmentBean;
    }
    
    /**
     * returns list of BudgetSubAwardAttachmentBeans for this proposal numer.
     * the version number is got from calling <CODE>s2STxnBean.getVersion(proposalNumer)</CODE>
     * @param proposalNumber proposal numer
     * @return list of BudgetSubAwardAttachmentBeans
     * @throws edu.mit.coeus.exception.CoeusException if any error ocuurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any database exception occurs
     */
    public List getAttachments(String proposalNumber)throws CoeusException, DBException {
        S2STxnBean s2STxnBean = new S2STxnBean();
        return getAttachments(proposalNumber, s2STxnBean.getVersion(proposalNumber));
    }
    
    /**
     * returns list of BudgetSubAwardAttachmentBeans for this proposal numer and version number.
     * @param proposalNumber proposalNumber
     * @param version version
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     * @throws edu.mit.coeus.utils.dbengine.DBException if any database error occurs
     * @return list of BudgetSubAwardAttachmentBeans
     */
    public List getAttachments(String proposalNumber, int version)throws CoeusException, DBException {
        List lstAttachment = null;
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER", "int", "" + version));
        
        Vector result = new Vector();
        if(dbEngine != null)
            result = dbEngine.executeRequest("Coeus", "call GET_BUDGET_SUB_AWARD_ATT_ALL(<< PROPOSAL_NUMBER >>, << VERSION_NUMBER >> , <<OUT RESULTSET rset>> )", "Coeus", param);
        else
            throw new CoeusException("db_exceptionCode.1000");
        
        int listSize = result.size();
        if(listSize > 0) {
            lstAttachment = new ArrayList();
            BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
            for(int index = 0 ;index < listSize; index++) {
                HashMap attachmentRow = (HashMap)result.elementAt(index);
                budgetSubAwardAttachmentBean = getAttachmentBean(attachmentRow);
                lstAttachment.add(budgetSubAwardAttachmentBean);
            }
        }
        return lstAttachment;
    }
    
    /**
     * created BudgetSubAwardAttachmentBean from the values got from database call.
     * @param attachmentRow hashmap got from database call
     * @return BudgetSubAwardAttachmentBean
     */
    private BudgetSubAwardAttachmentBean getAttachmentBean(HashMap attachmentRow) {
         BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean = new BudgetSubAwardAttachmentBean();
         budgetSubAwardAttachmentBean.setProposalNumber((String)attachmentRow.get("PROPOSAL_NUMBER"));
         budgetSubAwardAttachmentBean.setVersionNumber(Integer.parseInt(attachmentRow.get("VERSION_NUMBER").toString()));
         budgetSubAwardAttachmentBean.setSubAwardNumber(Integer.parseInt(attachmentRow.get("SUB_AWARD_NUMBER").toString()));
         budgetSubAwardAttachmentBean.setContentId((String)attachmentRow.get("CONTENT_ID"));
         budgetSubAwardAttachmentBean.setContentType((String)attachmentRow.get("CONTENT_TYPE"));
         budgetSubAwardAttachmentBean.setUpdateTimestamp((Timestamp)attachmentRow.get("UPDATE_TIMESTAMP"));
         budgetSubAwardAttachmentBean.setUpdateUser((String)attachmentRow.get("UPDATE_USER"));
         
         ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)attachmentRow.get("ATTACHMENT");
         budgetSubAwardAttachmentBean.setAttachment(byteArrayOutputStream.toByteArray());
         
         return budgetSubAwardAttachmentBean;
    }
    
    /**
     * iterates thru a list of beans and calls webservice for retreiving XML only of required.
     * @param list list of beans
     * @param servletPath servlet which connects to web service
     * @return list of beans with xml files
     * @throws java.io.IOException if servlet streaming fails
     * @throws java.net.MalformedURLException if servlet URL is incorrect or the servlet service unavailable
     * @throws java.lang.ClassNotFoundException ClassNotFoundException
     */
    public List checkAndUpdate(List lstBudgetSubAwardBean, String servletPath)throws IOException, MalformedURLException, ClassNotFoundException {
        List lstPDF = new ArrayList();
        int indexArray[] = new int[lstBudgetSubAwardBean.size()];
        int count = 0;
        BudgetSubAwardBean budgetSubAwardBean;
        for(int index = 0; index < lstBudgetSubAwardBean.size(); index++) {
            budgetSubAwardBean = (BudgetSubAwardBean)lstBudgetSubAwardBean.get(index);
            
            if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.DELETE_RECORD)) {
                continue;
            }
            
            DocumentTypeChecker documentTypeChecker = new DocumentTypeChecker();
            byte fileContents[] = budgetSubAwardBean.getSubAwardPDF();
        //COEUSQA-4104    
            if(fileContents == null){
                continue;
            }
       //COEUSQA-4104     
            DocumentType documentType = null;
            try{
                documentType = documentTypeChecker.getDocumentType(fileContents);
            }catch(Exception exception) {
                //Could Not Determine Document Type
                UtilFactory.log(exception.getMessage(), exception, "BudgetSubAwardTxnBean","checkAndUpdate");
                budgetSubAwardBean.setTranslationComments(exception.getMessage()==null?BudgetSubAwardConstants.COULD_NOT_DETERMINE_DOC_TYPE:exception.getMessage());
                continue;
            }
            if(documentType != null && documentType.getMimeType().equals(DocumentConstants.MIME_PDF) 
                && (budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null)){
                try{
                    /*
                    byte xmlBytes[] = getXMLFromPDF(fileContents);
                    String str = new String(xmlBytes);
                    budgetSubAwardBean.setSubAwardXML(str.toCharArray());
                    budgetSubAwardBean.setTranslationComments(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY);
                    budgetSubAwardBean.setXmlUpdateUser(userId);
                    budgetSubAwardBean.setAttachments(new ArrayList()); //PDF would not contain attachments as of now
                    */
                    budgetSubAwardBean = updateSubAward(budgetSubAwardBean);
                    budgetSubAwardBean.setTranslationComments(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY);
                    budgetSubAwardBean.setXmlUpdateUser(userId);
                    //budgetSubAwardBean.setAttachments(new ArrayList());
                    
                }catch(Exception exception) {
                    //Could not extract XML from PDF. could be a faulty PDF.
                    UtilFactory.log(exception.getMessage(), exception, "BudgetSubAwardTxnBean","checkAndUpdate");
                    budgetSubAwardBean.setTranslationComments(exception.getMessage()==null?BudgetSubAwardConstants.COULD_NOT_EXTRACT_XML_FROM_PDF:exception.getMessage());
                }
            }else if//(documentType != null && documentType.getMimeType().equals(DocumentConstants.MIME_XFD) && //If document Type is null, treat it as PDF
                    (budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null) {
                lstPDF.add(budgetSubAwardBean);
                //Remove from the original list for looping
                indexArray[count] = index;
                count = count + 1;
            }
            
        }//End For
        //Extract Xml from PDF FIles
        if(lstPDF.size() > 0) {
            lstPDF = getXMLFromPureEdge(lstPDF, servletPath);
        }
        
        //Refill Data to original List
        for(int index = 0; index < count; index++) {
            lstBudgetSubAwardBean.set(indexArray[index], lstPDF.get(index));
        }
        
        return lstBudgetSubAwardBean;
    }
    
    /**
     * updates the subawawd by extracting the xml, attachments from pdf and 
     * modifying the xml to include hashvalues for the attachments
     */
    public BudgetSubAwardBean updateSubAward(BudgetSubAwardBean budgetSubAwardBean)throws Exception {
        byte pdfFileContents[] = budgetSubAwardBean.getSubAwardPDF();
        
        PdfReader reader = new PdfReader(pdfFileContents);
        //byte xmlContents[] = getXMLFromPDF(reader);
        Map fileMap = extractAttachments(reader);
        //budgetSubAwardBean = updateXML(xmlContents, fileMap, budgetSubAwardBean);
        BudgetSubawardXmlBuilder budgetSubawardXmlModifier = new BudgetSubawardXmlBuilder();
        budgetSubawardXmlModifier.getXMLFromPDF(reader, fileMap, budgetSubAwardBean);
        return budgetSubAwardBean;
    }
    
    private Map extractAttachments(PdfReader reader)throws IOException, CoeusException {
        //PdfReader reader = new PdfReader(pdfFileContents);
        Map fileMap = new HashMap();
        
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary names = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.NAMES));
        if (names != null) {
            PdfDictionary embFiles = (PdfDictionary) PdfReader.getPdfObject(names.get(new PdfName("EmbeddedFiles")));
            if (embFiles != null) {
                HashMap embMap = PdfNameTree.readTree(embFiles);
                
                for (Iterator i = embMap.values().iterator(); i.hasNext();) {
                    PdfDictionary filespec = (PdfDictionary) PdfReader.getPdfObject((PdfObject) i.next());
                    Object fileInfo[] = unpackFile(reader, filespec);
                    if(fileMap.containsKey(fileInfo[0])) {
                        throw new CoeusException(DUPLICATE_FILE_NAMES);
                    }
                    fileMap.put(fileInfo[0], fileInfo[1]);
                }
            }
        }
        for (int k = 1; k <= reader.getNumberOfPages(); ++k) {
            PdfArray annots = (PdfArray) PdfReader.getPdfObject(reader.getPageN(k).get(PdfName.ANNOTS));
            if (annots == null)
                continue;
            for (Iterator i = annots.listIterator(); i.hasNext();) {
                PdfDictionary annot = (PdfDictionary) PdfReader.getPdfObject((PdfObject) i.next());
                PdfName subType = (PdfName) PdfReader.getPdfObject(annot.get(PdfName.SUBTYPE));
                if (!PdfName.FILEATTACHMENT.equals(subType))
                    continue;
                PdfDictionary filespec = (PdfDictionary) PdfReader.getPdfObject(annot.get(PdfName.FS));
                Object fileInfo[] = unpackFile(reader, filespec);
                if(fileMap.containsKey(fileInfo[0])) {
                    throw new CoeusException(DUPLICATE_FILE_NAMES);
                }
                fileMap.put(fileInfo[0], fileInfo[1]);
            }
        }
        
        return fileMap;
    }
    
    /**
     * Unpacks a file attachment.
     *
     * @param reader
     *            The object that reads the PDF document
     * @param filespec
     *            The dictonary containing the file specifications
     * @throws IOException
     */
    private static Object[] unpackFile(PdfReader reader, PdfDictionary filespec)throws IOException  {
        Object arr[] = new Object[2]; //use to store name and file bytes
        if (filespec == null)
            return null;
        
        PdfName type = (PdfName) PdfReader.getPdfObject(filespec.get(PdfName.TYPE));
        
        if (!PdfName.F.equals(type) && !PdfName.FILESPEC.equals(type))
            return null;
        
        PdfDictionary ef = (PdfDictionary) PdfReader.getPdfObject(filespec.get(PdfName.EF));
        if (ef == null)
            return null;
        
        PdfString fn = (PdfString) PdfReader.getPdfObject(filespec.get(PdfName.F));
        if (fn == null)
            return null;
        
        File fLast = new File(fn.toUnicodeString());
        
        PRStream prs = (PRStream) PdfReader.getPdfObject(ef.get(PdfName.F));
        if (prs == null)
            return null;
        
        byte attachmentByte[] = PdfReader.getStreamBytes(prs);
        arr[0] = fLast.getName();
        arr[1] = attachmentByte;
        return arr;
    }
        

    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    /**
     * This method is used to update the budge sub award details
     * 
     * 
     * @subAwardParam subAwardDetailBean
     * @return subAwardDetail - ProcReqParameter procedure parameter avs values.
     */  
    public void updateSubAwardDetails(BudgetSubAwardDetailBean subAwardDetailBean) throws DBException, CoeusException{
        Vector subAwardParam= new Vector();
        subAwardParam.add(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,subAwardDetailBean.getProposalNumber()));
        subAwardParam.add(new Parameter("AV_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,subAwardDetailBean.getVersionNumber()));
        subAwardParam.add(new Parameter("AV_SUB_AWARD_NUMBER",
                DBEngineConstants.TYPE_INT,subAwardDetailBean.getSubAwardNumber()));
        subAwardParam.add(new Parameter("AV_BUDGET_PERIOD",
                DBEngineConstants.TYPE_INT,subAwardDetailBean.getBudgetPeriod()));
        subAwardParam.add(new Parameter("AV_DIRECT_COST",
                DBEngineConstants.TYPE_DOUBLE,subAwardDetailBean.getDirectCost()));
        subAwardParam.add(new Parameter("AV_INDIRECT_COST",
                DBEngineConstants.TYPE_DOUBLE,subAwardDetailBean.getIndirectCost()));
        subAwardParam.add(new Parameter("AV_COST_SHARING_AMOUNT",
                DBEngineConstants.TYPE_DOUBLE,subAwardDetailBean.getCostSharingAmount()));
        subAwardParam.add(new Parameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,userId));
        if(timestamp == null){
            timestamp = (new CoeusFunctions()).getDBTimestamp();
        }
        subAwardParam.add(new Parameter("AV_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,timestamp));
        subAwardParam.add(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,subAwardDetailBean.getAwUpdateUser()));
        subAwardParam.add(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,subAwardDetailBean.getAwUpdateTimestamp()));
        subAwardParam.add(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,subAwardDetailBean.getAcType()));
        
        StringBuffer sql = new StringBuffer("call UPD_BUDGET_SUB_AWARD_DETAILS( ");
        
        sql.append(" <<AV_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AV_VERSION_NUMBER>> , ");
        sql.append(" <<AV_SUB_AWARD_NUMBER>> , ");
        sql.append(" <<AV_BUDGET_PERIOD>> , ");         
        sql.append(" <<AV_DIRECT_COST>> , ");
        sql.append(" <<AV_INDIRECT_COST>> , ");
        sql.append(" <<AV_COST_SHARING_AMOUNT>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AW_UPDATE_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>>)");              
        ProcReqParameter subAwardDetail = new ProcReqParameter();
        subAwardDetail.setDSN("Coeus");
        subAwardDetail.setParameterInfo(subAwardParam);
        subAwardDetail.setSqlCommand(sql.toString());
        Vector vecProcedure = new Vector();
        vecProcedure.add(subAwardDetail);
        if(dbEngine!=null){
            try{
                dbEngine.executeStoreProcs(vecProcedure);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }       
    }  

    /**
     * Method to get the sub award details
     * @param proposalNumber 
     * @param versionNumber 
     * @param subAwardNumber 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return vecSubAwardDetails
     */
    public Vector getSubAwardDetails(String proposalNumber,int versionNumber, int subAwardNumber) throws DBException{
        Vector result = new Vector(3,2);
        Vector vecSubAwardDetails = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("AW_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(versionNumber).toString()));
        param.addElement(new Parameter("AW_SUB_AWARD_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(subAwardNumber).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_BUDGET_SUB_AWARD_DETAILS ( <<AW_PROPOSAL_NUMBER>> , "
                    +" <<AW_VERSION_NUMBER>> ,<<AW_SUB_AWARD_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int subAwardDetailCount =result.size();
        if (subAwardDetailCount >0){
            vecSubAwardDetails = new Vector();
            for(int rowIndex=0;rowIndex<subAwardDetailCount;rowIndex++){
                BudgetSubAwardDetailBean subAwardDetailBean = new BudgetSubAwardDetailBean();
                HashMap hmSubAwardDetails = (HashMap) result.elementAt(rowIndex);
                subAwardDetailBean.setProposalNumber((String)hmSubAwardDetails.get("PROPOSAL_NUMBER"));
                subAwardDetailBean.setVersionNumber(Integer.parseInt(hmSubAwardDetails.get("VERSION_NUMBER") == null ? "0" :
                    hmSubAwardDetails.get("VERSION_NUMBER").toString()));
                subAwardDetailBean.setSubAwardNumber(Integer.parseInt(hmSubAwardDetails.get("SUB_AWARD_NUMBER") == null ? "0" :
                    hmSubAwardDetails.get("SUB_AWARD_NUMBER").toString()));
                subAwardDetailBean.setBudgetPeriod(Integer.parseInt(hmSubAwardDetails.get("BUDGET_PERIOD") == null ? "0" :
                    hmSubAwardDetails.get("BUDGET_PERIOD").toString()));         
                subAwardDetailBean.setDirectCost(Double.parseDouble(hmSubAwardDetails.get("DIRECT_COST") == null ? "0.0" :
                    hmSubAwardDetails.get("DIRECT_COST").toString()));
                subAwardDetailBean.setIndirectCost(Double.parseDouble(hmSubAwardDetails.get("INDIRECT_COST") == null ? "0.0" :
                    hmSubAwardDetails.get("INDIRECT_COST").toString()));
                subAwardDetailBean.setCostSharingAmount(Double.parseDouble(hmSubAwardDetails.get("COST_SHARING_AMOUNT") == null ? "0.0" :
                    hmSubAwardDetails.get("COST_SHARING_AMOUNT").toString()));
                subAwardDetailBean.setTotalCost(subAwardDetailBean.getDirectCost() + subAwardDetailBean.getIndirectCost());
                subAwardDetailBean.setPeriodStartDate(hmSubAwardDetails.get("START_DATE") == null ? null
                        :new java.sql.Date( ((Timestamp) hmSubAwardDetails.get("START_DATE")).getTime()));
                subAwardDetailBean.setPeriodEndDate(hmSubAwardDetails.get("END_DATE") == null ? null
                        :new java.sql.Date( ((Timestamp) hmSubAwardDetails.get("END_DATE")).getTime()));
                subAwardDetailBean.setBeforeModifiedDirectCost(Double.parseDouble(hmSubAwardDetails.get("DIRECT_COST") == null ? "0.0" :
                    hmSubAwardDetails.get("DIRECT_COST").toString()));
                subAwardDetailBean.setBeforeModifiedIndirectCost(Double.parseDouble(hmSubAwardDetails.get("INDIRECT_COST") == null ? "0.0" :
                    hmSubAwardDetails.get("INDIRECT_COST").toString()));
                subAwardDetailBean.setBeforeModifiedCostSharingAmount(Double.parseDouble(hmSubAwardDetails.get("COST_SHARING_AMOUNT") == null ? "0.0" :
                    hmSubAwardDetails.get("COST_SHARING_AMOUNT").toString()));                
                subAwardDetailBean.setBeforeModifiedTotalCost(subAwardDetailBean.getBeforeModifiedDirectCost() + subAwardDetailBean.getBeforeModifiedIndirectCost());
                subAwardDetailBean.setUpdateTimestamp((Timestamp)hmSubAwardDetails.get("UPDATE_TIMESTAMP"));
                subAwardDetailBean.setUpdateUser((String)hmSubAwardDetails.get("UPDATE_USER"));
                subAwardDetailBean.setAwUpdateTimestamp((Timestamp)hmSubAwardDetails.get("UPDATE_TIMESTAMP"));
                subAwardDetailBean.setAwUpdateUser((String)hmSubAwardDetails.get("UPDATE_USER"));
                vecSubAwardDetails.add(subAwardDetailBean);
            }
        }
        return vecSubAwardDetails;
    }
    
    /**
     * Method to generate line item details for the sub award
     * @param proposalNumber 
     * @param versionNumber 
     * @param organizationName 
     * @param subAwardPeriodDetails 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return 
     */
    public boolean createLineItemsForSubAward(String proposalNumber,int versionNumber,
            String organizationName,Vector subAwardPeriodDetails) throws CoeusException, DBException{
        boolean isCostElementInactive = false;
        CoeusFunctions coeusFunctions = new CoeusFunctions(userId);
        String subContractorDirectGT25 = "";
        String subContractorDirectLT25 = "";
        String subContractorInDirGT25 = "";
        String subContractorInDirLT25 = "";
        subContractorDirectGT25 = coeusFunctions.getParameterValue(BudgetSubAwardConstants.SUBCONTRACTOR_DIRECT_F_AND_A_GT_25K);
        subContractorDirectLT25 = coeusFunctions.getParameterValue(BudgetSubAwardConstants.SUBCONTRACTOR_DIRECT_F_AND_A_LT_25K);
        subContractorInDirGT25 = coeusFunctions.getParameterValue(BudgetSubAwardConstants.SUBCONTRACTOR_F_AND_A_GT_25K);
        subContractorInDirLT25 = coeusFunctions.getParameterValue(BudgetSubAwardConstants.SUBCONTRACTOR_F_AND_A_LT_25K);
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        CostElementsBean costElementForDirectGT25 =  budgetDataTxnBean.getCostElementsDetails(subContractorDirectGT25);
        CostElementsBean costElementForDirectLT25 = budgetDataTxnBean.getCostElementsDetails(subContractorDirectLT25);
        CostElementsBean costElementForGT25 = budgetDataTxnBean.getCostElementsDetails(subContractorInDirGT25);
        CostElementsBean costElementForLT25 = budgetDataTxnBean.getCostElementsDetails(subContractorInDirLT25);
        HashMap hmCostElementDetails = new HashMap();
        hmCostElementDetails.put(subContractorDirectGT25,costElementForDirectGT25);
        hmCostElementDetails.put(subContractorDirectLT25,costElementForDirectLT25);
        hmCostElementDetails.put(subContractorInDirGT25,costElementForGT25);
        hmCostElementDetails.put(subContractorInDirLT25,costElementForLT25);
        
        if( subAwardPeriodDetails != null && !subAwardPeriodDetails.isEmpty()){
            CoeusVector cvBudgetDetails = budgetDataTxnBean.getBudgetDetail(proposalNumber,versionNumber);
            CoeusVector cvBudgetDetailsUpdate = new CoeusVector();
            boolean is25KLineItemCreated = false;
            double lineItemCostFor25K = 0.0;
            HashMap hmPeriodDedetails = new HashMap();
            BudgetInfoBean budgetInfoBean =  budgetDataTxnBean.getBudgetForProposal(proposalNumber,versionNumber);
            for(Object subAwardDetails : subAwardPeriodDetails){
                boolean isCostSharingAdded = false;
                BudgetSubAwardDetailBean subAwardDetailBean = (BudgetSubAwardDetailBean)subAwardDetails;
                CoeusVector cvBudgetDetailsForPeriod =  geBudgetLineItems(cvBudgetDetails,subAwardDetailBean.getProposalNumber(),
                        subAwardDetailBean.getVersionNumber(),subAwardDetailBean.getBudgetPeriod());
                if(cvBudgetDetailsForPeriod == null){
                    cvBudgetDetailsForPeriod = new CoeusVector();
                }
                hmPeriodDedetails.put(subAwardDetailBean.getBudgetPeriod(),cvBudgetDetailsForPeriod);
                int lineItemNumber = getMaxLineItemNumber(cvBudgetDetailsForPeriod);
                BudgetDetailBean budgetDetailForDirGT25 = new BudgetDetailBean();
                BudgetDetailBean budgetDetailForDirLT25 = new BudgetDetailBean();
                // Update the budget details bean for the sub award line item creation
                budgetDetailForDirGT25.setSubmitCostSharingFlag(budgetInfoBean.isSubmitCostSharingFlag());                
                //COEUSQA-4028
                budgetDetailForDirGT25.setOnOffCampusFlag(budgetInfoBean.isDefaultIndicator()?costElementForDirectGT25.isOnOffCampusFlag():budgetInfoBean.isOnOffCampusFlag());
                //COEUSQA-4028
                budgetDetailForDirGT25 = updBudgetBeanForSubAwardLineItem(
                        budgetDetailForDirGT25,subAwardDetailBean,costElementForDirectGT25,organizationName);

                budgetDetailForDirLT25.setSubmitCostSharingFlag(budgetInfoBean.isSubmitCostSharingFlag());
                //COEUSQA-4028
                budgetDetailForDirLT25.setOnOffCampusFlag(budgetInfoBean.isDefaultIndicator()?costElementForDirectLT25.isOnOffCampusFlag():budgetInfoBean.isOnOffCampusFlag());                
                //COEUSQA-4028
                budgetDetailForDirLT25 = updBudgetBeanForSubAwardLineItem(
                        budgetDetailForDirLT25,subAwardDetailBean,costElementForDirectLT25,organizationName);
                
                BudgetDetailBean budgetDetailForInDirGT25 = new BudgetDetailBean();
                BudgetDetailBean budgetDetailForInDirLT25 = new BudgetDetailBean();
                
                budgetDetailForInDirGT25.setSubmitCostSharingFlag(budgetInfoBean.isSubmitCostSharingFlag());
                //COEUSQA-4028
                budgetDetailForInDirGT25.setOnOffCampusFlag(budgetInfoBean.isDefaultIndicator()?costElementForGT25.isOnOffCampusFlag():budgetInfoBean.isOnOffCampusFlag());                
                //COEUSQA-4028
                budgetDetailForInDirGT25 = updBudgetBeanForSubAwardLineItem(
                        budgetDetailForInDirGT25,subAwardDetailBean,costElementForGT25,organizationName);
                
                budgetDetailForInDirLT25.setSubmitCostSharingFlag(budgetInfoBean.isSubmitCostSharingFlag());
                //COEUSQA-4028
                budgetDetailForInDirLT25.setOnOffCampusFlag(budgetInfoBean.isDefaultIndicator()?costElementForLT25.isOnOffCampusFlag():budgetInfoBean.isOnOffCampusFlag());              
                //COEUSQA-4028
                budgetDetailForInDirLT25 = updBudgetBeanForSubAwardLineItem(
                        budgetDetailForInDirLT25,subAwardDetailBean,costElementForLT25,organizationName);
                
                if(is25KLineItemCreated){
                    budgetDetailForDirGT25.setLineItemCost(subAwardDetailBean.getDirectCost());
                    budgetDetailForDirGT25.setCostElement(subContractorDirectGT25);
                    if(budgetDetailForDirGT25.getLineItemCost() > 0.0){
                        budgetDetailForDirGT25.setAcType(TypeConstants.INSERT_RECORD);
                        if(!isCostSharingAdded){
                            budgetDetailForDirGT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                            isCostSharingAdded = true;
                        }
                    }
                }else if(!is25KLineItemCreated &&
                        subAwardDetailBean.getDirectCost() >= lineItemCostFor25K &&
                        subAwardDetailBean.getDirectCost() >= COST_TWENTY_FIVE_THOUSAND){
                    double costNeeded = COST_TWENTY_FIVE_THOUSAND - lineItemCostFor25K;
                    double directCostToAdd = 0.0;
                    boolean noCostToAdd = false;
                    if(costNeeded == COST_TWENTY_FIVE_THOUSAND){
                        directCostToAdd = COST_TWENTY_FIVE_THOUSAND;
                        if(subAwardDetailBean.getDirectCost() == COST_TWENTY_FIVE_THOUSAND){
                            noCostToAdd = true;
                        }
                    }else if(costNeeded >= subAwardDetailBean.getDirectCost() || subAwardDetailBean.getDirectCost() >= costNeeded){
                        directCostToAdd = costNeeded;
                    }else{
                        directCostToAdd = subAwardDetailBean.getDirectCost();
                        noCostToAdd = true;
                    }
                    budgetDetailForDirLT25.setLineItemCost(directCostToAdd);
                    budgetDetailForDirLT25.setCostElement(subContractorDirectLT25);
                    if(budgetDetailForDirLT25.getLineItemCost() > 0.0){
                        budgetDetailForDirLT25.setAcType(TypeConstants.INSERT_RECORD);
                        if(!isCostSharingAdded){
                            budgetDetailForDirLT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                            isCostSharingAdded = true;
                        }
                    }
                    lineItemCostFor25K = lineItemCostFor25K + directCostToAdd;
                    if(!noCostToAdd){
                        double remainingDirectCost = subAwardDetailBean.getDirectCost() - directCostToAdd;
                        if(remainingDirectCost > 0.0){
                            budgetDetailForDirGT25.setLineItemCost(remainingDirectCost);
                            budgetDetailForDirGT25.setCostElement(subContractorDirectGT25);
                            if(budgetDetailForDirGT25.getLineItemCost() > 0.0){
                                budgetDetailForDirGT25.setAcType(TypeConstants.INSERT_RECORD);
                                if(!isCostSharingAdded){
                                    budgetDetailForDirGT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                                    isCostSharingAdded = true;
                                }
                            }
                        }
                    }
                    is25KLineItemCreated  = true;
                    
                }else if(!is25KLineItemCreated &&
                        subAwardDetailBean.getDirectCost() < COST_TWENTY_FIVE_THOUSAND
                        && lineItemCostFor25K < COST_TWENTY_FIVE_THOUSAND &&
                        subAwardDetailBean.getDirectCost() > 0.0){
                    double costNeeded = COST_TWENTY_FIVE_THOUSAND - lineItemCostFor25K;
                    double directCostToAdd = 0.0;
                    boolean noCostToAdd = false;
                    if(costNeeded <= subAwardDetailBean.getDirectCost()){
                        directCostToAdd = costNeeded;
                    }else{
                        directCostToAdd = subAwardDetailBean.getDirectCost();
                        noCostToAdd = true;
                    }
                    
                    lineItemCostFor25K = lineItemCostFor25K + directCostToAdd;
                    budgetDetailForDirLT25.setLineItemCost(directCostToAdd);
                    
                    budgetDetailForDirLT25.setCostElement(subContractorDirectLT25);
                    if(budgetDetailForDirLT25.getLineItemCost() > 0.0){
                        budgetDetailForDirLT25.setAcType(TypeConstants.INSERT_RECORD);
                        if(!isCostSharingAdded){
                            budgetDetailForDirLT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                            isCostSharingAdded = true;
                        }
                    }
                    
                    // remaining cost
                    if(lineItemCostFor25K >= COST_TWENTY_FIVE_THOUSAND){
                        is25KLineItemCreated  = true;
                        if(!noCostToAdd){
                            double remainingDirectCost = subAwardDetailBean.getDirectCost() - directCostToAdd;
                            if(remainingDirectCost > 0.0){
                                budgetDetailForDirGT25.setLineItemCost(remainingDirectCost);
                                budgetDetailForDirGT25.setCostElement(subContractorDirectGT25);
                                if(budgetDetailForDirGT25.getLineItemCost() > 0.0){
                                    budgetDetailForDirGT25.setAcType(TypeConstants.INSERT_RECORD);
                                    if(!isCostSharingAdded){
                                        budgetDetailForDirGT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                                        isCostSharingAdded = true;
                                    }
                                }
                            }
                        }
                    }
                    
                }
                
                // Indirect cost - start
                if(is25KLineItemCreated){
                    budgetDetailForInDirGT25.setLineItemCost(subAwardDetailBean.getIndirectCost());
                    budgetDetailForInDirGT25.setCostElement(subContractorInDirGT25);
                    if(budgetDetailForInDirGT25.getLineItemCost() > 0.0){
                        if(!isCostSharingAdded){
                            budgetDetailForInDirGT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                        }
                        budgetDetailForInDirGT25.setAcType(TypeConstants.INSERT_RECORD);
                    }
                }else if(!is25KLineItemCreated &&
                        subAwardDetailBean.getIndirectCost() >= lineItemCostFor25K &&
                        subAwardDetailBean.getIndirectCost() >= COST_TWENTY_FIVE_THOUSAND){
                    double costNeeded = COST_TWENTY_FIVE_THOUSAND - lineItemCostFor25K;
                    double inDirectCostToAdd = 0.0;
                    boolean noCostToAdd = false;
                    if(costNeeded == COST_TWENTY_FIVE_THOUSAND){
                        inDirectCostToAdd = COST_TWENTY_FIVE_THOUSAND;
                        if(subAwardDetailBean.getIndirectCost() == COST_TWENTY_FIVE_THOUSAND){
                            noCostToAdd = true;
                        }
                    }else if(costNeeded >= subAwardDetailBean.getIndirectCost() || subAwardDetailBean.getIndirectCost() >= costNeeded){
                        inDirectCostToAdd = costNeeded;
                    }else{
                        inDirectCostToAdd = subAwardDetailBean.getIndirectCost();
                        noCostToAdd = true;
                    }
                    budgetDetailForInDirLT25.setLineItemCost(inDirectCostToAdd);
                    budgetDetailForInDirLT25.setCostElement(subContractorInDirLT25);
                    if(budgetDetailForInDirLT25.getLineItemCost() > 0.0){
                        budgetDetailForInDirLT25.setAcType(TypeConstants.INSERT_RECORD);
                        if(!isCostSharingAdded){
                            budgetDetailForInDirLT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                            isCostSharingAdded = true;
                        }
                    }
                    lineItemCostFor25K = lineItemCostFor25K + inDirectCostToAdd;
                    if(!noCostToAdd){
                        double remainingDirectCost = subAwardDetailBean.getIndirectCost() - inDirectCostToAdd;
                        if(remainingDirectCost > 0.0){
                            budgetDetailForInDirGT25.setLineItemCost(remainingDirectCost);
                            budgetDetailForInDirGT25.setCostElement(subContractorInDirGT25);
                            if(budgetDetailForInDirGT25.getLineItemCost() > 0.0){
                                budgetDetailForInDirGT25.setAcType(TypeConstants.INSERT_RECORD);
                                if(!isCostSharingAdded){
                                    budgetDetailForInDirGT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                                    isCostSharingAdded = true;
                                }
                            }
                        }
                    }
                    is25KLineItemCreated  = true;

                }else if(!is25KLineItemCreated &&
                        subAwardDetailBean.getIndirectCost() < COST_TWENTY_FIVE_THOUSAND
                        && lineItemCostFor25K < COST_TWENTY_FIVE_THOUSAND &&
                        subAwardDetailBean.getIndirectCost() > 0.0){
                    double costNeeded = COST_TWENTY_FIVE_THOUSAND - lineItemCostFor25K;
                    double inDirectCostToAdd = 0.0;
                    boolean noCostToAdd = false;
                    if(costNeeded <= subAwardDetailBean.getIndirectCost()){
                        inDirectCostToAdd = costNeeded;
                    }else{
                        inDirectCostToAdd = subAwardDetailBean.getIndirectCost();
                        noCostToAdd = true;
                    }
                    
                    lineItemCostFor25K = lineItemCostFor25K + inDirectCostToAdd;
                    budgetDetailForInDirLT25.setLineItemCost(inDirectCostToAdd);
                    
                    budgetDetailForInDirLT25.setCostElement(subContractorInDirLT25);
                    if(budgetDetailForInDirLT25.getLineItemCost() > 0.0){
                        budgetDetailForInDirLT25.setAcType(TypeConstants.INSERT_RECORD);
                        if(!isCostSharingAdded){
                            budgetDetailForInDirLT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                            isCostSharingAdded = true;
                        }
                    }
                    
                    // remaining cost
                    if(lineItemCostFor25K >= COST_TWENTY_FIVE_THOUSAND){
                        is25KLineItemCreated  = true;
                        if(!noCostToAdd){
                            double remainingDirectCost = subAwardDetailBean.getIndirectCost() - inDirectCostToAdd;
                            if(remainingDirectCost > 0.0){
                                budgetDetailForInDirGT25.setLineItemCost(remainingDirectCost);
                                budgetDetailForInDirGT25.setCostElement(subContractorInDirGT25);
                                if(budgetDetailForInDirGT25.getLineItemCost() > 0.0){
                                    budgetDetailForInDirGT25.setAcType(TypeConstants.INSERT_RECORD);
                                    if(!isCostSharingAdded){
                                        budgetDetailForInDirGT25.setCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                                        isCostSharingAdded = true;
                                    }
                                }
                            }
                        }
                    }
                    
                }
                // Indirect cost - End

                CoeusVector cvPeriodDetails = (CoeusVector)hmPeriodDedetails.get(subAwardDetailBean.getBudgetPeriod());
                if(TypeConstants.INSERT_RECORD.equals(budgetDetailForDirLT25.getAcType())){
                    lineItemNumber = lineItemNumber+1;
                    budgetDetailForDirLT25.setLineItemNumber(lineItemNumber);
                    cvPeriodDetails.add(budgetDetailForDirLT25);
                    cvBudgetDetailsUpdate.add(budgetDetailForDirLT25);                    
                }
                if(TypeConstants.INSERT_RECORD.equals(budgetDetailForDirGT25.getAcType())){
                    lineItemNumber = lineItemNumber+1;
                    budgetDetailForDirGT25.setLineItemNumber(lineItemNumber);
                    cvPeriodDetails.add(budgetDetailForDirGT25);
                    cvBudgetDetailsUpdate.add(budgetDetailForDirGT25);
                }
                // Indirect Cost
                if(TypeConstants.INSERT_RECORD.equals(budgetDetailForInDirLT25.getAcType())){
                    lineItemNumber = lineItemNumber+1;
                    budgetDetailForInDirLT25.setLineItemNumber(lineItemNumber);
                    cvPeriodDetails.add(budgetDetailForInDirLT25);
                    cvBudgetDetailsUpdate.add(budgetDetailForInDirLT25);
                }
                if(TypeConstants.INSERT_RECORD.equals(budgetDetailForInDirGT25.getAcType())){
                    lineItemNumber = lineItemNumber+1;
                    budgetDetailForInDirGT25.setLineItemNumber(lineItemNumber);
                    cvPeriodDetails.add(budgetDetailForInDirGT25);
                    cvBudgetDetailsUpdate.add(budgetDetailForInDirGT25);
                }
                if(cvBudgetDetailsUpdate != null && !cvBudgetDetailsUpdate.isEmpty()){
                    cvPeriodDetails = resetOrderOfLineItemSequence(cvPeriodDetails);
                }
            }
            
            if(!cvBudgetDetailsUpdate.isEmpty()){
                for(Object budgetDetails : cvBudgetDetailsUpdate){
                    BudgetDetailBean budgetDetailBean = (BudgetDetailBean)budgetDetails;
                    CostElementsBean costElementBean = (CostElementsBean)hmCostElementDetails.get(budgetDetailBean.getCostElement());
                    String costElementActive = costElementBean.getActive();
                     if(!"Y".equals(costElementActive)){
                        isCostElementInactive = true;
                        break;
                     }
                }
                // If any of the cost element for which sub award line item to be created is inactive, then line item wont be created for all the cost elements for the sub award
                BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userId);
                if(!isCostElementInactive){
                    Set periodSet = hmPeriodDedetails.keySet();
                    Iterator periodIterator = periodSet.iterator();
                    while(periodIterator.hasNext()){
                        CoeusVector cvLineItemDetails = (CoeusVector)hmPeriodDedetails.get(periodIterator.next());
                        budgetUpdateTxnBean.updateBudgetDetails(cvLineItemDetails);
                    }
                    
                    
                }
                // Added for COEUSQA-3439 : New Subaward Upload tool inserted line items are not parsed into the F&A base properly - Start
                // insert applicable cal amounts for the line items
                budgetUpdateTxnBean = null;
                generateCalAmountForBudgetDetail(cvBudgetDetailsUpdate);
                // Added for COEUSQA-3439 : New Subaward Upload tool inserted line items are not parsed into the F&A base properly - End
             
            }
        }
        return isCostElementInactive;
    }
    
    /**
     * Method to update the BudgetDetailBean with the sub award details for the lin item generation
     * @param budgetDetailBean 
     * @param subAwardDetailBean 
     * @param costElementForDirectGT25 
     * @param organizationName 
     * @return 
     */
    private BudgetDetailBean updBudgetBeanForSubAwardLineItem(BudgetDetailBean budgetDetailBean, BudgetSubAwardDetailBean subAwardDetailBean,
            CostElementsBean costElementForDirectGT25, String organizationName){
        budgetDetailBean.setProposalNumber(subAwardDetailBean.getProposalNumber());
        budgetDetailBean.setBudgetPeriod(subAwardDetailBean.getBudgetPeriod());
        budgetDetailBean.setVersionNumber(subAwardDetailBean.getVersionNumber());
        budgetDetailBean.setLineItemStartDate(subAwardDetailBean.getPeriodStartDate());
        budgetDetailBean.setLineItemEndDate(subAwardDetailBean.getPeriodEndDate());
        budgetDetailBean.setBudgetCategoryCode(costElementForDirectGT25.getBudgetCategoryCode());
        //Replace CostElementsBean with budgetDetailBean for COEUSQA-4028 -Start
        budgetDetailBean.setOnOffCampusFlag(budgetDetailBean.isOnOffCampusFlag());
        //Replace CostElementsBean with budgetDetailBean for COEUSQA-4028 -Start
        budgetDetailBean.setLineItemDescription(organizationName);
        budgetDetailBean.setApplyInRateFlag(false);
        budgetDetailBean.setQuantity(0);
        budgetDetailBean.setBasedOnLineItem(0);
        budgetDetailBean.setSubAwardNumber(subAwardDetailBean.getSubAwardNumber());
        budgetDetailBean.setSubmitCostSharingFlag(budgetDetailBean.isSubmitCostSharingFlag());
        return budgetDetailBean;
    }
    
    /**
     * Method to get the line items for the budget version and period
     * @param cvBudgetDetails 
     * @param proposalNumber 
     * @param versionNumber 
     * @param budgetPeriod 
     * @param subAwardNumber 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return cvFilteredBudgetDetails
     */
    private CoeusVector geBudgetLineItems(CoeusVector cvBudgetDetails, String proposalNumber, int versionNumber, int budgetPeriod) throws CoeusException{        
        if(cvBudgetDetails == null){
            return null;
        }        
        Equals propNo = new Equals("proposalNumber", proposalNumber);
        Equals versionNo = new Equals("versionNumber", new Integer(versionNumber));
        Equals periodNo = new Equals("budgetPeriod", new Integer(budgetPeriod));
        And propVersion = new And(propNo, versionNo);
        And condition = new And(propVersion, periodNo);
        CoeusVector cvFilteredBudgetDetails = cvBudgetDetails.filter(condition);
        return cvFilteredBudgetDetails;
    }
    
    /**
     * Method to re-order the line item sequence
     * @param cvPeriodLineItemDetails 
     * @return cvPeriodLineItemDetails
     */
    private CoeusVector resetOrderOfLineItemSequence(CoeusVector cvPeriodLineItemDetails){
        if(cvPeriodLineItemDetails != null && !cvPeriodLineItemDetails.isEmpty()){
            for(int lineItemIndex = 0;lineItemIndex<cvPeriodLineItemDetails.size();lineItemIndex++){
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)cvPeriodLineItemDetails.get(lineItemIndex);
                budgetDetailBean.setLineItemSequence(lineItemIndex+1);
                if(!TypeConstants.INSERT_RECORD.equals(budgetDetailBean.getAcType())){
                    budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
        return cvPeriodLineItemDetails;
    }
    
    /**
     * Method to get the max line item number
     * @param cvBudgetLineItem 
     * @return lineItemnumber
     */
    private int getMaxLineItemNumber(CoeusVector cvBudgetLineItem) {
        if(cvBudgetLineItem == null || cvBudgetLineItem.size() == 0){
            return 0;
        }
        CoeusVector cvLineItem = new CoeusVector();
        cvLineItem.addAll(cvBudgetLineItem);
        cvLineItem.sort("lineItemNumber", false);
        BudgetDetailBean maxLineItemBean = (BudgetDetailBean)cvLineItem.get(0);
        return maxLineItemBean.getLineItemNumber();
    }
    
    /**
     * Method to delete the period deletes for all the sub awards
     * @param proposalNumber 
     * @param versionNumber 
     * @param budgetPeriod 
     * @return isRecordDeleted
     */
    public boolean deleteSubAwardPeriodDetails(String proposalNumber, int versionNumber, int budgetPeriod) throws DBException, CoeusException{
        Vector result = new Vector();
        Vector param= new Vector();
        boolean isRecordDeleted = false;
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("AW_VERSION_NUMBER", DBEngineConstants.TYPE_INT, versionNumber));
        param.addElement(new Parameter("AW_BUDGET_PERIOD", DBEngineConstants.TYPE_INT, budgetPeriod));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER IS_RECORD_DELETE>> = "
                    +" call FN_DEL_SUB_AWD_PERIOD_DETAILS(<< AW_PROPOSAL_NUMBER >> , << AW_VERSION_NUMBER >>, << AW_BUDGET_PERIOD >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            int isDeleted = Integer.parseInt(rowParameter.get("IS_RECORD_DELETE").toString());
            if(isDeleted == 0){
                isRecordDeleted = true;
            }
        }
        return isRecordDeleted;
    }
    
    /**
     * Method to merge the PDF data to the sub award details
     * @param vecPDFPeriodDetails 
     * @param vecSubAwardPeriodDetails 
     * @return vecSubAwardPeriodDetails
     */
    private Vector mergePeriodDetailsFromPDF(Vector vecPDFPeriodDetails, Vector vecSubAwardPeriodDetails){
        if(vecSubAwardPeriodDetails != null && !vecSubAwardPeriodDetails.isEmpty()){
            for(int pdfIndex = 0 ; pdfIndex < vecPDFPeriodDetails.size() ; pdfIndex++){
                BudgetSubAwardDetailBean budgetSubAwardPDFDetail = (BudgetSubAwardDetailBean)vecPDFPeriodDetails.get(pdfIndex);
                for(int subAwardDetailIndex = 0 ; subAwardDetailIndex < vecSubAwardPeriodDetails.size() ; subAwardDetailIndex++){
                    BudgetSubAwardDetailBean budgetSubAwardDetail = (BudgetSubAwardDetailBean)vecSubAwardPeriodDetails.get(subAwardDetailIndex);
                    if(budgetSubAwardPDFDetail.getBudgetPeriod() == budgetSubAwardDetail.getBudgetPeriod()){
                        // Will remove the sub award details and add the bean for PDF data in the same index
                        vecSubAwardPeriodDetails.remove(subAwardDetailIndex);
                        vecSubAwardPeriodDetails.add(subAwardDetailIndex, budgetSubAwardPDFDetail);
                        break;
                    }else{
                        budgetSubAwardDetail.setAcType(TypeConstants.INSERT_RECORD);
                    }
                }   
            }
        }else{
            vecSubAwardPeriodDetails = vecPDFPeriodDetails;
        }
        return vecSubAwardPeriodDetails;
    }
    
    /**
     * Method to sync the XML period sub award details
     * @param budgetSubAwardBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws javax.xml.parsers.ParserConfigurationException 
     * @throws org.xml.sax.SAXException 
     * @throws java.io.IOException 
     * @throws javax.xml.transform.TransformerException 
     * @return vecSubAwardPeriodDetail
     */
    public Vector syncXML(BudgetSubAwardBean budgetSubAwardBean) throws CoeusException, 
                                                                        DBException, ParserConfigurationException, SAXException, IOException, TransformerException{
        Vector vecSubAwardPeriodDetail = new Vector();
        String xmlData = getXML(budgetSubAwardBean.getProposalNumber(),budgetSubAwardBean.getVersionNumber(),budgetSubAwardBean.getSubAwardNumber());
        DocumentBuilderFactory domParserFactory = DocumentBuilderFactory.newInstance();
        domParserFactory.setNamespaceAware(true);
        domParserFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder documentBuilder = domParserFactory.newDocumentBuilder();
        // Removes the <Forms> tag in the XML to get the name space, since this form tag is add during the XML extract from PDF
        xmlData = xmlData.replaceFirst("<Forms>","");
        xmlData = xmlData.replaceFirst("</Forms>","");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xmlData.getBytes());
        Document document = documentBuilder.parse(byteArrayInputStream);
        byteArrayInputStream.close();

        String nameSpace = document.getDocumentElement().getNamespaceURI();
        
        domParserFactory.setNamespaceAware(false);
        documentBuilder = domParserFactory.newDocumentBuilder();
        domParserFactory.setIgnoringElementContentWhitespace(true);
        byteArrayInputStream = new ByteArrayInputStream(xmlData.getBytes());
        document = documentBuilder.parse(byteArrayInputStream);

        BudgetSubawardXmlModifier xmlMod = null;
        if(TRAIN_BUDGET.equalsIgnoreCase(nameSpace)){
            xmlMod = new BudgetSubawardTrainBudgetImpl();
        }else if("http://apply.grants.gov/forms/RR_Budget10-V1.1".equalsIgnoreCase(nameSpace)){
            xmlMod = new BudgetSubawardXmlExtract(false, "RR_Budget10", "http://apply.grants.gov/forms/RR_Budget10-V1.1");
        }else if("http://apply.grants.gov/forms/RR_FedNonFedBudget10-V1.1".equalsIgnoreCase(nameSpace)){
            xmlMod = new BudgetSubawardXmlExtract(false, "RR_FedNonFedBudget10", "http://apply.grants.gov/forms/RR_FedNonFedBudget10-V1.1");
        }else {
            xmlMod = new BudgetSubawardXmlModifierImpl();
        }
        vecSubAwardPeriodDetail = xmlMod.getSubAwardPeriodDetails(document,budgetSubAwardBean);

        return vecSubAwardPeriodDetail;
    }
    
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    
    // Added for COEUSQA-2735 Cost sharing distribution for Sub awards - Start
    /**
     * Method to get the sub award cost sharing details
     * @param proposalNumber 
     * @param versionNumber 
     * @param subAwardNumber 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return vecSubAwardDetails
     */
    public List getBudgetSubAwardCostSharingAmount(String proposalNumber,int versionNumber) throws DBException{
        Vector result = new Vector(3,2);
        List lstSubAwardDetails = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("AW_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(versionNumber).toString()));        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_COST_SHARING_FOR_SUB_AWARD ( <<AW_PROPOSAL_NUMBER>> , "
                    +" <<AW_VERSION_NUMBER>> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);

        }else{
            throw new DBException("DB instance is not available");
        }
        int subAwardDetailCount =result.size();
        if (subAwardDetailCount >0){
            lstSubAwardDetails = new ArrayList();
            for(int rowIndex=0;rowIndex<subAwardDetailCount;rowIndex++){
                BudgetSubAwardDetailBean budgetSubAwardDetailBean = new BudgetSubAwardDetailBean();
                HashMap hmSubAwardCostSharingDetails = (HashMap) result.elementAt(rowIndex);
                budgetSubAwardDetailBean.setProposalNumber((String)hmSubAwardCostSharingDetails.get("PROPOSAL_NUMBER"));
                budgetSubAwardDetailBean.setVersionNumber(Integer.parseInt(hmSubAwardCostSharingDetails.get("VERSION_NUMBER") == null ? "0" :
                    hmSubAwardCostSharingDetails.get("VERSION_NUMBER").toString()));
                budgetSubAwardDetailBean.setSubAwardNumber(Integer.parseInt(hmSubAwardCostSharingDetails.get("SUB_AWARD_NUMBER") == null ? "0" :
                    hmSubAwardCostSharingDetails.get("SUB_AWARD_NUMBER").toString()));
                budgetSubAwardDetailBean.setBudgetPeriod(Integer.parseInt(hmSubAwardCostSharingDetails.get("BUDGET_PERIOD") == null ? "0" :
                    hmSubAwardCostSharingDetails.get("BUDGET_PERIOD").toString()));
                budgetSubAwardDetailBean.setCostSharingAmount(Double.parseDouble(hmSubAwardCostSharingDetails.get("COST_SHARING_AMOUNT") == null ? "0.0" :
                    hmSubAwardCostSharingDetails.get("COST_SHARING_AMOUNT").toString()));
                budgetSubAwardDetailBean.setOrganizationName((String)hmSubAwardCostSharingDetails.get("ORGANIZATION_NAME"));
                budgetSubAwardDetailBean.setPeriodStartDate(hmSubAwardCostSharingDetails.get("START_DATE") == null ? null
                        :new java.sql.Date( ((Timestamp) hmSubAwardCostSharingDetails.get("START_DATE")).getTime()));
                lstSubAwardDetails.add(budgetSubAwardDetailBean);
            }

        }
        return lstSubAwardDetails;

    }
    // Added for COEUSQA-2735 Cost sharing distribution for Sub awards - End
    
    // Added for COEUSQA-3439 : New Subaward Upload tool inserted line items are not parsed into the F&A base properly - Start
    /**
     * Method to generate the applicable cal amounts for the line item and insert to the database
     * @param cvBudgetDetailsUpdate 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     */
    private void generateCalAmountForBudgetDetail(CoeusVector cvBudgetDetailsUpdate) throws CoeusException, DBException {
        if(cvBudgetDetailsUpdate != null && !cvBudgetDetailsUpdate.isEmpty()){
            BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userId);
            LineItemCalculator lineItemCalculator = new LineItemCalculator();
            for(Object budgetDetails : cvBudgetDetailsUpdate){
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)budgetDetails;
                String queryKey = budgetDetailBean.getProposalNumber()+budgetDetailBean.getVersionNumber();

                Equals eqProposalNumber = new Equals("proposalNumber", budgetDetailBean.getProposalNumber());
                Equals eqVersionNumber = new Equals("versionNumber", new Integer(budgetDetailBean.getVersionNumber()));
                Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
                Equals eqLineItemNumber = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
                And andProposalVersion = new And(eqProposalNumber,eqVersionNumber);
                And andBudgetPeriods = new And(andProposalVersion,eqBudgetPeriod);
                And andBudgetLineItem = new And(andBudgetPeriods,eqLineItemNumber);
                setCalAmountsToQueryEngine(budgetDetailBean);
                lineItemCalculator.calculate(budgetDetailBean);

                CoeusVector cvCalAmtsTAble = QueryEngine.getInstance().executeQuery(queryKey, BudgetDetailCalAmountsBean.class, andBudgetLineItem);
                if(cvCalAmtsTAble != null && !cvCalAmtsTAble.isEmpty()){
                    Vector vecPorcedures = new Vector();
                    for(Object budgetCalAmountsDetail : cvCalAmtsTAble){
                        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)budgetCalAmountsDetail;
                        vecPorcedures.add(budgetUpdateTxnBean.addUpdBudgetDetailCalAmounts(budgetDetailCalAmountsBean));
                    }
                    if(dbEngine!=null){
                        java.sql.Connection conn = null;
                        try{
                            conn = dbEngine.beginTxn();
                            dbEngine.executeStoreProcs(vecPorcedures,conn);
                            dbEngine.commit(conn);
                        }catch(Exception sqlEx){
                            dbEngine.rollback(conn);
                            throw new CoeusException(sqlEx.getMessage());
                        }finally{
                            dbEngine.endTxn(conn);
                        }
                    }else{
                        throw new CoeusException("db_exceptionCode.1000");
                    }                    
                }
            }
        }
    }

    /**
     * Method to evaluate the cal amounts for the line item and insert to the queryEngine
     * @param budgetDetailBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     */
    private void setCalAmountsToQueryEngine(BudgetDetailBean budgetDetailBean) throws CoeusException, DBException{
        
        String queryKey = budgetDetailBean.getProposalNumber()+budgetDetailBean.getVersionNumber();
        //Set Ac TYpe  = D for Old Budget Detail Cal Amts
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
        budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
        budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
        budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
        budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
        CoeusVector vecOldDetails = QueryEngine.getInstance().executeQuery(queryKey, budgetDetailCalAmountsBean);
        if(vecOldDetails !=null && !vecOldDetails.isEmpty()) {
            for(Object coeusBeanDetails : vecOldDetails) {
                ((CoeusBean)coeusBeanDetails).setAcType(TypeConstants.DELETE_RECORD);
                // Modified for COEUSQA-3439 : New Subaward Upload tool inserted line items are not parsed into the F&A base properly - Start
//                QueryEngine.getInstance().delete(queryKey, (CoeusBean)coeusBeanDetails);                
                QueryEngine.getInstance().removeData(queryKey, (CoeusBean)coeusBeanDetails);
                 // Modified for COEUSQA-3439 : New Subaward Upload tool inserted line items are not parsed into the F&A base properly - End
            }
        }
        
        //Set Valid CE Rates
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        
        CoeusVector vecValidRateTypes = budgetDataTxnBean.getValidCERateTypes(budgetDetailBean.getCostElement());
        if(vecValidRateTypes == null) vecValidRateTypes = new CoeusVector();
        
        //Check wheather it contains Inflation Rate
        Equals eqInflation = new Equals("rateClassType", RateClassTypeConstants.INFLATION);
        CoeusVector vecInflation = vecValidRateTypes.filter(eqInflation);
        if(vecInflation !=null && !vecInflation.isEmpty()) {
            budgetDetailBean.setApplyInRateFlag(true);
        }else {
            budgetDetailBean.setApplyInRateFlag(false);
        }
        
        CoeusVector vecBudgetInfo = QueryEngine.getInstance().getDetails(queryKey,BudgetInfoBean.class);
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)vecBudgetInfo.get(0);
        
        NotEquals nEqInflation = new NotEquals("rateClassType",RateClassTypeConstants.INFLATION);
        Equals eqOH = new Equals("rateClassType", RateClassTypeConstants.OVERHEAD);
        NotEquals nEqOH = new NotEquals("rateClassType", RateClassTypeConstants.OVERHEAD);
        Equals eqBudgetRateClass = new Equals("rateClassCode",new Integer(budgetInfoBean.getOhRateClassCode()));
        
        And eqOHAndEqBudgetRateClass = new And(eqOH, eqBudgetRateClass);
        Or eqOHAndEqBudgetRateClassOrNEqOH = new Or(eqOHAndEqBudgetRateClass, nEqOH);
        And nEqInflationAndeqOHAndEqBudgetRateClassOrNEqOH = new And(nEqInflation, eqOHAndEqBudgetRateClassOrNEqOH);
        
        vecValidRateTypes = vecValidRateTypes.filter(nEqInflationAndeqOHAndEqBudgetRateClassOrNEqOH);
        
        CoeusVector cvLARates = QueryEngine.getInstance().getDetails(queryKey, ProposalLARatesBean.class);
        
        if (cvLARates == null || cvLARates.size() == 0) {
            NotEquals neqLA = new NotEquals("rateClassType",RateClassTypeConstants.LAB_ALLOCATION);
            NotEquals neqLASal = new NotEquals("rateClassType", RateClassTypeConstants.LA_WITH_EB_VA);
            And laAndLaSal = new And(neqLA, neqLASal);
            
            vecValidRateTypes = vecValidRateTypes.filter(laAndLaSal);
        }
        
        //insert to Query Engine
        if(vecValidRateTypes != null && !vecValidRateTypes.isEmpty()) {
            for(Object calAmountDetails : vecValidRateTypes){
                budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                ValidCERateTypesBean validCERateTypesBean = (ValidCERateTypesBean)calAmountDetails;
                
                budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                budgetDetailCalAmountsBean.setRateClassType(validCERateTypesBean.getRateClassType());
                budgetDetailCalAmountsBean.setRateClassCode(validCERateTypesBean.getRateClassCode());
                budgetDetailCalAmountsBean.setRateTypeCode(validCERateTypesBean.getRateTypeCode());
                budgetDetailCalAmountsBean.setRateClassDescription(validCERateTypesBean.getRateClassDescription());
                budgetDetailCalAmountsBean.setRateTypeDescription(validCERateTypesBean.getRateTypeDescription());
                budgetDetailCalAmountsBean.setApplyRateFlag(true);
                budgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                QueryEngine.getInstance().insert(queryKey, budgetDetailCalAmountsBean);
            }
        }
        
        Equals eqLabAllocSal = new Equals("rateClassType",RateClassTypeConstants.LA_WITH_EB_VA);
        CoeusVector vecLabAllocSal = vecValidRateTypes.filter(eqLabAllocSal);
        
        if(vecLabAllocSal != null && !vecLabAllocSal.isEmpty()) {
            //Has Lab allocation and Salaries Entry (i.e Rate Class Type = Y)
            Equals eqE = new Equals("rateClassType",RateClassTypeConstants.EMPLOYEE_BENEFITS);
            CoeusVector vecCalCTypes = QueryEngine.getInstance().executeQuery(queryKey, ValidCalcTypesBean.class, eqE);
            if (vecCalCTypes.size() > 0) {
                ValidCalcTypesBean validCalcTypesBean = (ValidCalcTypesBean) vecCalCTypes.get(0);
                if (validCalcTypesBean.getDependentRateClassType().equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                    budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                    budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                    budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                    budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                    budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                    budgetDetailCalAmountsBean.setRateClassType(validCalcTypesBean.getRateClassType());
                    budgetDetailCalAmountsBean.setRateClassCode(validCalcTypesBean.getRateClassCode());
                    budgetDetailCalAmountsBean.setRateTypeCode(validCalcTypesBean.getRateTypeCode());
                    budgetDetailCalAmountsBean.setRateClassDescription(validCalcTypesBean.getRateClassDescription());
                    budgetDetailCalAmountsBean.setRateTypeDescription(validCalcTypesBean.getRateTypeDescription());
                    budgetDetailCalAmountsBean.setApplyRateFlag(true);
                    budgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                    QueryEngine.getInstance().insert(queryKey, budgetDetailCalAmountsBean);
                }
            }
            
            Equals eqV = new Equals("rateClassType",RateClassTypeConstants.VACATION);
            vecCalCTypes = QueryEngine.getInstance().executeQuery(queryKey, ValidCalcTypesBean.class, eqV);
            if (!vecCalCTypes.isEmpty()) {
                ValidCalcTypesBean validCalcTypesBean = (ValidCalcTypesBean) vecCalCTypes.get(0);
                if (RateClassTypeConstants.LA_WITH_EB_VA.equals(validCalcTypesBean.getDependentRateClassType())) {
                    budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                    budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                    budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                    budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                    budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                    budgetDetailCalAmountsBean.setRateClassType(validCalcTypesBean.getRateClassType());
                    budgetDetailCalAmountsBean.setRateClassCode(validCalcTypesBean.getRateClassCode());
                    budgetDetailCalAmountsBean.setRateTypeCode(validCalcTypesBean.getRateTypeCode());
                    budgetDetailCalAmountsBean.setRateClassDescription(validCalcTypesBean.getRateClassDescription());
                    budgetDetailCalAmountsBean.setRateTypeDescription(validCalcTypesBean.getRateTypeDescription());
                    budgetDetailCalAmountsBean.setApplyRateFlag(true);
                    budgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                    budgetDetailCalAmountsBean.setUpdateTimestamp(timestamp);
                    
                    QueryEngine.getInstance().insert(queryKey, budgetDetailCalAmountsBean);
                }
            }
            
        }
        
    }
    // Added for COEUSQA-3439 : New Subaward Upload tool inserted line items are not parsed into the F&A base properly - End
 
}