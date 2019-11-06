/*
 * YNQTxnBean.java
 *
 * Created on November 25, 2004, 4:24 PM
 */

package edu.mit.coeus.admin.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.exception.CoeusException;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Date;
import java.sql.Timestamp;
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class YNQTxnBean {
	
	// Instance of a dbEngine
    private DBEngineImpl dbEngine;   
    
    private static final String DSN = "Coeus";
    
    private String userId;
    
    /** Creates a new instance of YNQTxnBean */
    public YNQTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    public YNQTxnBean(String userId) {
        dbEngine = new DBEngineImpl();
        this.userId = userId;
    }
    /**
	 * This method is used to get all the YNQ.
	 * The stored procedure used is DW_GET_ALL_YNQ
	 * @return CoeusVector
	 * @throws CoeusException
	 * @throws DBException
	 */
    public CoeusVector getAllYNQ() throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmPayment = null;
        YNQBean ynqBean = null;
        CoeusVector cvYNQ = null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_ALL_YNQ ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0){
            cvYNQ = new CoeusVector();
            for(int index = 0; index < listSize; index++) {
                ynqBean = new YNQBean();
                hmPayment = (HashMap)result.elementAt(index);
				ynqBean.setQuestionId((String)hmPayment.get("QUESTION_ID"));
                ynqBean.setDescription((String)hmPayment.get("DESCRIPTION"));   
				ynqBean.setQuestionType((String)hmPayment.get("QUESTION_TYPE"));
				ynqBean.setNoOfAnswers(hmPayment.get("NO_OF_ANSWERS") == null ? 0 :
                    Integer.parseInt(hmPayment.get("NO_OF_ANSWERS").toString()));
				ynqBean.setExplanationRequiredFor((String)hmPayment.get("EXPLANATION_REQUIRED_FOR"));
				ynqBean.setDateRequiredFor((String)hmPayment.get("DATE_REQUIRED_FOR"));
				ynqBean.setStatus((String)hmPayment.get("STATUS"));
				ynqBean.setEffectiveDate(
                hmPayment.get("EFFECTIVE_DATE") == null ?
					null : new Date(((Timestamp) hmPayment.get("EFFECTIVE_DATE")).getTime()));
				ynqBean.setUpdateTimestamp((Timestamp)hmPayment.get("UPDATE_TIMESTAMP"));
                ynqBean.setUpdateUser((String)hmPayment.get("UPDATE_USER"));
                ynqBean.setGroupName((String)hmPayment.get("GROUP_NAME"));
                cvYNQ.addElement(ynqBean);
            }
        }
        return cvYNQ;
    }
	
	/**
     * get all the question explanation.
     * This method executes the procedure to get the question's explanation
     * for a question and will keep the results in the array of organizationYNQBean.
     *
     * @return CoeusVector contains YNQExplanationBean as value and question id as key
     * @exception DBException db end
     * @exception CoeusException db end.
     */
    public CoeusVector getQuestionExplanationAll()
    throws CoeusException, DBException {
        CoeusVector explanationList = new CoeusVector();
        YNQExplanationBean ynqExplanation = null;
        // keep the stored procedure result in a vector
        Vector result = null;
        // keep the parameters for the stored procedure in a vector
        Vector param = new Vector();
        // add the organization id parameter into param vector
        //param.addElement(new Parameter("QUESTION_ID", "String", questionId));
        // execute the stored procedure
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_ynq_explanation_all ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result != null && result.size() > 0) {
            for (int index = 0; index < result.size(); index++) {
                HashMap orgDetailsRow = (HashMap) result.elementAt(index);
                ynqExplanation = new YNQExplanationBean();
                ynqExplanation.setQuestionId( (String) orgDetailsRow.get("QUESTION_ID"));
                ynqExplanation.setExplanationType( (String) orgDetailsRow.get("EXPLANATION_TYPE"));
                ynqExplanation.setExplanation( (String) orgDetailsRow.get("EXPLANATION"));
				ynqExplanation.setUpdateTimestamp((Timestamp)orgDetailsRow.get("UPDATE_TIMESTAMP"));
                ynqExplanation.setUpdateUser((String)orgDetailsRow.get("UPDATE_USER"));
                explanationList.addElement(ynqExplanation);
            }
        }
        return explanationList;
    }
	
	/**
     * get the question explanation for a question.
     * This method executes the procedure to get the question's explanation
     * for a question and will keep the results in the array of organizationYNQBean.
     * @param questionId String
     * @return CoeusVector contains YNQExplanationBean as value and question id as key
     * @exception DBException db end
     * @exception CoeusException db end.
     */
    public CoeusVector getQuestionExplanation(String questionId)
											throws CoeusException, DBException {
        CoeusVector explanationList = new CoeusVector();
        YNQExplanationBean ynqExplanation = null;
        // keep the stored procedure result in a vector
        Vector result = null;
        // keep the parameters for the stored procedure in a vector
        Vector param = new Vector();
        
        param.addElement(new Parameter("QUESTION_ID",
			DBEngineConstants.TYPE_STRING, questionId));
        // execute the stored procedure
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_ynq_explanation (<<QUESTION_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result != null && result.size() > 0) {
            for (int index = 0; index < result.size(); index++) {
                HashMap orgDetailsRow = (HashMap) result.elementAt(index);
                ynqExplanation = new YNQExplanationBean();
                ynqExplanation.setQuestionId( (String) orgDetailsRow.get("QUESTION_ID"));
                ynqExplanation.setExplanationType( (String) orgDetailsRow.get("EXPLANATION_TYPE"));
                ynqExplanation.setExplanation( (String) orgDetailsRow.get("EXPLANATION"));
				ynqExplanation.setUpdateTimestamp((Timestamp)orgDetailsRow.get("UPDATE_TIMESTAMP"));
                ynqExplanation.setUpdateUser((String)orgDetailsRow.get("UPDATE_USER"));
                explanationList.addElement(ynqExplanation);
            }
        }
        return explanationList;
    }
	
	/**
	 * This method is used to get YNQ detail.
	 * The stored procedure used is DW_GET_ALL_YNQ
	 * @return CoeusVector
	 * @throws CoeusException
	 * @throws DBException
	 */
    public CoeusVector getYNQDetail(String questionId) throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmPayment = null;
        YNQBean ynqBean = null;
        CoeusVector cvYNQDetail = null;
        
        param.addElement(new Parameter("QUESTION_ID",
        DBEngineConstants.TYPE_STRING, questionId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_YNQ_DETAIL ( <<QUESTION_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0){
            cvYNQDetail = new CoeusVector();
            for(int index = 0; index < listSize; index++) {
                ynqBean = new YNQBean();
                hmPayment = (HashMap)result.elementAt(index);
                ynqBean.setQuestionId((String)hmPayment.get("QUESTION_ID"));
                ynqBean.setDescription((String)hmPayment.get("DESCRIPTION"));
                ynqBean.setQuestionType((String)hmPayment.get("QUESTION_TYPE"));
                ynqBean.setNoOfAnswers(hmPayment.get("NO_OF_ANSWERS") == null ? 0 :
                    Integer.parseInt(hmPayment.get("NO_OF_ANSWERS").toString()));
                    ynqBean.setExplanationRequiredFor((String)hmPayment.get("EXPLANATION_REQUIRED_FOR"));
                    ynqBean.setDateRequiredFor((String)hmPayment.get("DATE_REQUIRED_FOR"));
                    ynqBean.setStatus((String)hmPayment.get("STATUS"));
                    ynqBean.setEffectiveDate(
                    hmPayment.get("EFFECTIVE_DATE") == null ?
                    null : new Date(((Timestamp) hmPayment.get("EFFECTIVE_DATE")).getTime()));
                    ynqBean.setUpdateTimestamp((Timestamp)hmPayment.get("UPDATE_TIMESTAMP"));
                    ynqBean.setUpdateUser((String)hmPayment.get("UPDATE_USER"));
                    ynqBean.setGroupName((String)hmPayment.get("GROUP_NAME"));
                    cvYNQDetail.addElement(ynqBean);
            }
        }
        return cvYNQDetail;
    }
	
	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            YNQTxnBean bean = new YNQTxnBean();
            CoeusVector cvValidAwardBasis = bean.getQuestionExplanation("12");
            System.out.println("Vector size "+cvValidAwardBasis.size());
        }catch(Exception ex){
            ex.printStackTrace();
        }    
    }
}
