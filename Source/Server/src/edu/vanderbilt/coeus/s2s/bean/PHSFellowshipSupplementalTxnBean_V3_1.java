package edu.vanderbilt.coeus.s2s.bean;

import java.util.* ;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.math.*;

  
public class PHSFellowshipSupplementalTxnBean_V3_1 {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
    String propNumber;
    int    version;
    BigDecimal bdZero = new BigDecimal("0");

    
    /** Creates a new instance of PHSFellowshipSupplementalTxnBean_V3_1 */
    public PHSFellowshipSupplementalTxnBean_V3_1() {
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
       
    }
    
    /* Get vertebrate animals information */
    public CoeusVector getHumanSubjects (String proposalNumber) throws CoeusException, DBException {
     
        CoeusVector cvQuestions = new CoeusVector();
        QuestionAnswerBean bean = new QuestionAnswerBean();

        Vector param = new Vector();
        HashMap row = null;
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getFellowshipHumanSubjects( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result.size()> 0) {
        	for(int i=0; i < result.size(); i++) {
        		row = (HashMap) result.elementAt(i);
       		 
        		bean = new QuestionAnswerBean();
	       		bean.setQuestionnaireCompletionId((String) row.get("QUESTIONNAIRE_COMPLETION_ID"));
	       		bean.setQuestionId(row.get("QUESTION_ID") == null ? null :
	       			new Integer(row.get("QUESTION_ID").toString()));
	       		bean.setQuestionNumber(row.get("QUESTION_NUMBER") == null ? null :
	       			new Integer(row.get("QUESTION_NUMBER").toString()));
	       		bean.setAnswerNumber(row.get("ANSWER_NUMBER") == null ? null :
	       			new Integer(row.get("ANSWER_NUMBER").toString()));
	       		bean.setAnswer((String) row.get("ANSWER"));
	       		bean.setQuestionnaireVersionNumber(row.get("QUESTION_VERSION_NUMBER") == null ? null :
	       			new Integer(row.get("QUESTION_VERSION_NUMBER").toString()));
	       		 
	       		cvQuestions.add(bean);
       	 	}
        }

        return cvQuestions;
    }
    
    /* Get the clinical trial answers from the smart questionnaire */
    public CoeusVector getClinicalTrial (String proposalNumber) throws CoeusException, DBException {
     
        CoeusVector cvQuestions = new CoeusVector();
        QuestionAnswerBean bean = new QuestionAnswerBean();

        Vector param = new Vector();
        HashMap row = null;
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getClinicalTrials( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result.size()> 0) {
        	for(int i=0; i < result.size(); i++) {
        		row = (HashMap) result.elementAt(i);
       		 
        		bean = new QuestionAnswerBean();
	       		bean.setQuestionnaireCompletionId((String) row.get("QUESTIONNAIRE_COMPLETION_ID"));
	       		bean.setQuestionId(row.get("QUESTION_ID") == null ? null :
	       			new Integer(row.get("QUESTION_ID").toString()));
	       		bean.setQuestionNumber(row.get("QUESTION_NUMBER") == null ? null :
	       			new Integer(row.get("QUESTION_NUMBER").toString()));
	       		bean.setAnswerNumber(row.get("ANSWER_NUMBER") == null ? null :
	       			new Integer(row.get("ANSWER_NUMBER").toString()));
	       		bean.setAnswer((String) row.get("ANSWER"));
	       		bean.setQuestionnaireVersionNumber(row.get("QUESTION_VERSION_NUMBER") == null ? null :
	       			new Integer(row.get("QUESTION_VERSION_NUMBER").toString()));
	       		 
	       		cvQuestions.add(bean);
       	 	}
        }

        return cvQuestions;
   }
    
    /* Get vertebrate animals information */
    public CoeusVector getVertebrateAnimals (String proposalNumber) throws CoeusException, DBException {
     
        CoeusVector cvQuestions = new CoeusVector();
        QuestionAnswerBean bean = new QuestionAnswerBean();

        Vector param = new Vector();
        HashMap row = null;
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getFellowshipVertebrateAnimals( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result.size()> 0) {
        	for(int i=0; i < result.size(); i++) {
        		row = (HashMap) result.elementAt(i);
       		 
        		bean = new QuestionAnswerBean();
	       		bean.setQuestionnaireCompletionId((String) row.get("QUESTIONNAIRE_COMPLETION_ID"));
	       		bean.setQuestionId(row.get("QUESTION_ID") == null ? null :
	       			new Integer(row.get("QUESTION_ID").toString()));
	       		bean.setQuestionNumber(row.get("QUESTION_NUMBER") == null ? null :
	       			new Integer(row.get("QUESTION_NUMBER").toString()));
	       		bean.setAnswerNumber(row.get("ANSWER_NUMBER") == null ? null :
	       			new Integer(row.get("ANSWER_NUMBER").toString()));
	       		bean.setAnswer((String) row.get("ANSWER"));
	       		bean.setQuestionnaireVersionNumber(row.get("QUESTION_VERSION_NUMBER") == null ? null :
	       			new Integer(row.get("QUESTION_VERSION_NUMBER").toString()));
	       		 
	       		cvQuestions.add(bean);
       	 	}
        }

        return cvQuestions;
    }
    
    /* Get stem cells information */
    public CoeusVector getStemCells (String proposalNumber) throws CoeusException, DBException {
     
        CoeusVector cvQuestions = new CoeusVector();
        QuestionAnswerBean bean = new QuestionAnswerBean();

        Vector param = new Vector();
        HashMap row = null;
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getStemCells( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result.size()> 0) {
        	for(int i=0; i < result.size(); i++) {
        		row = (HashMap) result.elementAt(i);
       		 
        		bean = new QuestionAnswerBean();
	       		bean.setQuestionnaireCompletionId((String) row.get("QUESTIONNAIRE_COMPLETION_ID"));
	       		bean.setQuestionId(row.get("QUESTION_ID") == null ? null :
	       			new Integer(row.get("QUESTION_ID").toString()));
	       		bean.setQuestionNumber(row.get("QUESTION_NUMBER") == null ? null :
	       			new Integer(row.get("QUESTION_NUMBER").toString()));
	       		bean.setAnswerNumber(row.get("ANSWER_NUMBER") == null ? null :
	       			new Integer(row.get("ANSWER_NUMBER").toString()));
	       		bean.setAnswer((String) row.get("ANSWER"));
	       		bean.setQuestionnaireVersionNumber(row.get("QUESTION_VERSION_NUMBER") == null ? null :
	       			new Integer(row.get("QUESTION_VERSION_NUMBER").toString()));
	       		 
	       		cvQuestions.add(bean);
       	 	}
        }

        return cvQuestions;
    }
    
    /* Get organization for previous institution */
    public OrganizationMaintenanceFormBean getOrganizationName (String organizationId) throws CoeusException, DBException {

        OrganizationMaintenanceFormBean bean = new OrganizationMaintenanceFormBean();

        Vector param = new Vector();
        HashMap row = null;
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("ORGANIZATION_ID",DBEngineConstants.TYPE_STRING,organizationId));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getOrganizationName( <<ORGANIZATION_ID>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result.size()> 0) {
    		row = (HashMap) result.elementAt(0);
   		 
    		bean = new OrganizationMaintenanceFormBean();
       		bean.setOrganizationId(organizationId);
       		bean.setOrganizationName((String) row.get("ORGANIZATION_NAME"));
        }

        return bean;
    }  
    
    /* Get alternate phone */
    public String getAlternatePhone (String proposalNumber) throws CoeusException, DBException {

        Vector param = new Vector();
        HashMap row = null;
        Vector result = new Vector(3,2);
        String alternatePhone = null;

        param.addElement(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getFellowshipAltPhone( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result.size()> 0) {
    		row = (HashMap) result.elementAt(0);
    		alternatePhone = (String) row.get("SECONDRY_OFFICE_PHONE");
        }

        return alternatePhone;
    } 
    
    /* Get questionnaire answers */
    public CoeusVector getQuestionnaireAnswers(String proposalNumber)
    	throws CoeusException, DBException {

         CoeusVector cvQuestions = new CoeusVector();
         QuestionAnswerBean bean = new QuestionAnswerBean();

         Vector param = new Vector();
         HashMap row = null;
         Vector result = new Vector(3,2);

         param.addElement(new Parameter("PROPOSAL_NUMBER",
                 DBEngineConstants.TYPE_STRING,proposalNumber));
         
         if (dbEngine != null){
             result = dbEngine.executeRequest("Coeus",
                     "call S2S_FORMSET_D.getFellowshipQuestionAnswers( <<PROPOSAL_NUMBER>> , "+
                     "<<OUT RESULTSET rset>> )", "Coeus", param);
         }
         else{
             throw new CoeusException("db_exceptionCode.1000");
         }

         if (result.size() > 0) {
        	 for(int i=0; i < result.size(); i++) {
        		 row = (HashMap) result.elementAt(i);
        		 
        		 bean = new QuestionAnswerBean();
        		 bean.setQuestionnaireCompletionId((String) row.get("QUESTIONNAIRE_COMPLETION_ID"));
        		 bean.setQuestionId(row.get("QUESTION_ID") == null ? null :
                     new Integer(row.get("QUESTION_ID").toString()));
        		 bean.setQuestionNumber(row.get("QUESTION_NUMBER") == null ? null :
                     new Integer(row.get("QUESTION_NUMBER").toString()));
        		 bean.setAnswerNumber(row.get("ANSWER_NUMBER") == null ? null :
                     new Integer(row.get("ANSWER_NUMBER").toString()));
        		 bean.setAnswer((String) row.get("ANSWER"));
        		 bean.setQuestionnaireVersionNumber(row.get("QUESTION_VERSION_NUMBER") == null ? null :
                     new Integer(row.get("QUESTION_VERSION_NUMBER").toString()));
        		 
        		 cvQuestions.add(bean);
        	 }
         }

         return cvQuestions;
    }


    public HashMap getBudgetInfo(String proposalNumber) throws CoeusException , DBException{

    	Vector param = new Vector();

        param.addElement( new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING, proposalNumber));

        HashMap row = null;
        Vector result = new Vector();

        if (dbEngine !=null) {
        	result = dbEngine.executeRequest("Coeus",
        			"call S2SPHSFELLOWSHIPV2PKG.GET_BUDGET_INFO ( <<PROPOSAL_NUMBER>> ," +
                    " <<OUT RESULTSET rset>> )","Coeus", param);

        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        if (listSize > 0) {
            row = (HashMap) result.elementAt(0);
        }
        
        return row;
    }
    
    public HashMap getFellowshipStipends(String proposalNumber) throws CoeusException , DBException{

    	Vector param = new Vector();

        param.addElement( new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING, proposalNumber));

        HashMap row = null;
        Vector result = new Vector();

        if (dbEngine !=null) {
        	result = dbEngine.executeRequest("Coeus",
        			"call S2S_FORMSET_D.getFellowshipStipends ( <<PROPOSAL_NUMBER>> ," +
                    " <<OUT RESULTSET rset>> )","Coeus", param);

        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        int listSize = result.size();
        if (listSize > 0) {
            row = (HashMap) result.elementAt(0);
        }
        
        return row;
    }
  
}
       