/*
 * PHS398CoverPageSupplementTxnBean_V3_0.java
 *
 */

package edu.vanderbilt.coeus.s2s.bean;

import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.util.HashMap;
import java.util.Vector;


public class PHS398CoverPageSupplementTxnBean_V3_0 {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
    String propNumber;
    
    /** Creates a new instance of PHS398CoverPageSupplementTxnBean */
    public PHS398CoverPageSupplementTxnBean_V3_0() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
    public HashMap getIsNewInvestigator (String propNumber)
        throws CoeusException, DBException {
     
        if(propNumber==null) 
                throw new CoeusXMLException("Proposal Number is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPHS398CoverPageSupplemtPkg.get_is_new_investigator( <<PROPOSAL_NUMBER>> , "
                                + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
    }
    
    public Vector getPIDegrees (String propNumber)
        throws CoeusException, DBException {
     
        if(propNumber==null) 
                throw new CoeusXMLException("Proposal Number is Null");   
        
        Vector result = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPHS398CoverPageSupplemtPkg.get_pi_degrees( <<PROPOSAL_NUMBER>> , "
                                + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
       
        return result;
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
                    "call S2S_FORMSET_D.getVertebrateAnimals( <<PROPOSAL_NUMBER>> , "+
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
    
    /* Get list of budget periods for program income */
    public CoeusVector getProgramIncome (String proposalNumber) throws CoeusException, DBException {

        Vector param = new Vector();
        HashMap row = null;
        CoeusVector data = new CoeusVector();
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getProgramIncome( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if (result.size()> 0) {
        	for(int i=0; i < result.size(); i++) {
        		row = (HashMap) result.elementAt(i);
        		data.add(row.get("BUDGET_PERIOD"));
        	}
        }

        return data;
    }
    
    /* Get program income for the budget period */
    public HashMap getProgramIncomeBudgetPeriod (String proposalNumber, Integer budgetPeriod) throws CoeusException, DBException {

        Vector param = new Vector();
        HashMap data = null;
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("BUDGET_PERIOD",DBEngineConstants.TYPE_INTEGER,budgetPeriod));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getProgramIncomeBudgetPeriod( <<PROPOSAL_NUMBER>> , <<BUDGET_PERIOD>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        
        int listSize = result.size();
        if (listSize > 0) {
            data = (HashMap) result.elementAt(0);
        }

        return data;
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
    
    /* Get inventions and patents answers */
    public CoeusVector getInventionsAndPatents (String proposalNumber) throws CoeusException, DBException {
     
        CoeusVector cvQuestions = new CoeusVector();
        QuestionAnswerBean bean = new QuestionAnswerBean();

        Vector param = new Vector();
        HashMap row = null;
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getInventionsAndPatents( <<PROPOSAL_NUMBER>> , "+
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
    
    /* Get change of PI / institution */
    public CoeusVector getChangeOfInvestigatorOrInstitution (String proposalNumber) throws CoeusException, DBException {
     
        CoeusVector cvQuestions = new CoeusVector();
        QuestionAnswerBean bean = new QuestionAnswerBean();

        Vector param = new Vector();
        HashMap row = null;
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getChangeOfPiOrInst( <<PROPOSAL_NUMBER>> , "+
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
    
    /* Get person name for PI */
    public RolodexDetailsBean getPreviousPiName (String rolodexId) throws CoeusException, DBException {
     
        RolodexDetailsBean bean = new RolodexDetailsBean();

        Vector param = new Vector();
        HashMap row = null;
        Vector result = new Vector(3,2);

        param.addElement(new Parameter("ROLODEX_ID",DBEngineConstants.TYPE_STRING,rolodexId));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call S2S_FORMSET_D.getPreviousPiName( <<ROLODEX_ID>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result.size()> 0) {
    		row = (HashMap) result.elementAt(0);
   		 
    		bean = new RolodexDetailsBean();
       		bean.setRolodexId(rolodexId);
       		bean.setPrefix((String) row.get("PREFIX"));
       		bean.setFirstName((String) row.get("FIRST_NAME"));
       		bean.setMiddleName((String) row.get("MIDDLE_NAME"));
       		bean.setLastName((String) row.get("LAST_NAME"));
       		bean.setSuffix((String) row.get("SUFFIX"));
        }

        return bean;
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

}
