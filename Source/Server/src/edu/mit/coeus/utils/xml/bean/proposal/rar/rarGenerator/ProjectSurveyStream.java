/*
 * ProjectSurveyStream.java
 * Created on March 10,2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;

import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import java.util.* ;
import java.util.Hashtable;


public class ProjectSurveyStream {
    
    ObjectFactory objFactory ;
    ProjectSurveyType projectSurveyType;
    ProposalYNQBean proposalYNQBean;
    ProposalPrintingTxnBean propPrintTxnBean;
    private static final String STEMCELL = "18";
    private static final String INTERNATIONAL_ACTIVITIES    = "H1";
    private static final String PROPRIETARY_INFO = "G8";
    private static final String HISTORICAL_SITES = "G6";
    private static final String GENETICALLY_ENGINEERED = "G4";
    private static final String HAZARDOUS_MATERIALS = "G3";
    private static final String NSFSMALL_GRANT = "14" ;
    private static final String NSF_BEGINNING_INV = "12";
    private static final String LOBBYING_ACTIVITIES= "H4";
    //case 3997
    private static final String CLINICAL_TRIAL="28";
    //case 4111
    private static final String DISCLOSURE_PERMISSION="29";
    
    /** Creates a new instance of ProjectSurveyStream */
    public ProjectSurveyStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public ProjectSurveyType getProjectSurvey(ProposalDevelopmentFormBean proposalBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  boolean answer;
       String  strAnswer;
       String questionId ;
       String explanation;
       
       String propNumber = proposalBean.getProposalNumber();
       
       projectSurveyType = objFactory.createProjectSurvey();
           
        Vector vecYNQ = proposalBean.getPropYNQAnswerList();
        if (vecYNQ != null) {    
              for (int vecCount = 0 ; vecCount < vecYNQ.size() ; vecCount++) {
                proposalYNQBean = (ProposalYNQBean) vecYNQ.get(vecCount);
                questionId = proposalYNQBean.getQuestionId();
                strAnswer = proposalYNQBean.getAnswer();
                explanation = (proposalYNQBean.getExplanation()== null ? " " : proposalYNQBean.getExplanation());
                answer = (strAnswer.equals("Y") ? true : false);
                if (questionId.equals( HAZARDOUS_MATERIALS)) { 
                  projectSurveyType.setG3Question(answer);  
                  projectSurveyType.setG3Text(explanation);
                } else if (questionId .equals( GENETICALLY_ENGINEERED )) {
                  projectSurveyType.setG4Question(answer);
                  projectSurveyType.setG4Text(explanation);
                } else if (questionId .equals( HISTORICAL_SITES)) {
                  projectSurveyType.setG6Question(answer);
                  projectSurveyType.setG6Text(explanation);
                 } else if (questionId .equals( PROPRIETARY_INFO)) {
                  projectSurveyType.setG8Question(answer);
                  projectSurveyType.setG8Text(explanation);
                 } else if (questionId .equals( INTERNATIONAL_ACTIVITIES)) {
                  projectSurveyType.setH1Question(answer);
                  projectSurveyType.setH1Text(explanation); 
                 } //CASE 3997
                 else if (questionId.equals(CLINICAL_TRIAL)) {
                     projectSurveyType.setClinicalTrialQuestion(answer);
               //    projectSurveyType.set
                     //nsf extensions
                 } else if (questionId .equals( LOBBYING_ACTIVITIES)) {
                     projectSurveyType.setH4Question(answer);
                 } else if (questionId. equals( NSFSMALL_GRANT)) {
                     projectSurveyType.setSmallGrantQuestion(answer);
                 } else if (questionId. equals (NSF_BEGINNING_INV)) {
                     projectSurveyType.setNSFbeginningInvestQuestion(answer);
                 } else if (questionId.equals (STEMCELL)) {
                     projectSurveyType.setStemCellQuestion(answer);
                     projectSurveyType.setStemCellText(explanation);
                 } else if (questionId.equals (DISCLOSURE_PERMISSION)){
                     //CASE 4111
                     projectSurveyType.setDisclosurePermissionQuestion(answer);
                 }
              } //end for
        } else {
            projectSurveyType.setG3Question(false);  
            projectSurveyType.setG3Text("This question has not been answered");
            projectSurveyType.setG4Question(false);
            projectSurveyType.setG4Text("This question has not been answered");
            projectSurveyType.setG6Question(false);
            projectSurveyType.setG6Text("This question has not been answered");
            projectSurveyType.setG8Question(false);
            projectSurveyType.setG8Text("This question has not been answered");
            projectSurveyType.setH1Question(false);
            projectSurveyType.setH1Text("This question has not been answered"); 
                 
        }
              
        //nsf extension
        //get H4 question (an individual question)
        propPrintTxnBean = new ProposalPrintingTxnBean();
        String h4 = propPrintTxnBean.getH4LobbyQuestion(propNumber);
        answer = (h4.equals("Y") ? true : false);
        projectSurveyType.setH4Question(answer);
        
        //hard coding these questions. we don't have them
         projectSurveyType.setCBQuestion(false);
         projectSurveyType.setCBText("This question has not been answered");
         projectSurveyType.setEnvExemptionQuestion(false);
         projectSurveyType.setEnvExemptionCBText("This question has not been answered");
         projectSurveyType.setEnvImpactQuestion(false);
         projectSurveyType.setEnvImpactText("This question has not been answered");
       
       
         return projectSurveyType;
    }
    
   
}
