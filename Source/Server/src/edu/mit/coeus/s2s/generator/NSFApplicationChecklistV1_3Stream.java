/*
 * @(#)NSFApplicationChecklistV1_3Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.NSFAppChecklistTxnBean;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.nsf_applicationchecklist_1_3_v1_3.*;
import java.util.HashMap;
import javax.xml.bind.JAXBException;

 
 public class NSFApplicationChecklistV1_3Stream extends S2SBaseStream{ 
    private gov.grants.apply.forms.nsf_applicationchecklist_1_3_v1_3.ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;

    private String propNumber;
    private UtilFactory utilFactory;
    
    private NSFAppChecklistTxnBean nsfAppChecklistTxnBean;

    private static final int PRELIMINARY = 58;
    private static final int MERIT  = 59;
    private static final int MENTORING = 61;
    private static final int PRIOR_SUPPORT = 62;
    private static final int HR_QUESTION = 56;
     private static final int HR_REQUIRED_INFO = 65;

    /** Creates a new instance of NSFApplicationChecklistV1_1Stream */
    public NSFApplicationChecklistV1_3Stream(){
        objFactory = new gov.grants.apply.forms.nsf_applicationchecklist_1_3_v1_3.ObjectFactory();
     } 
   
  
    private NSFApplicationChecklist13Type getNSFApplicationChecklist()
        throws CoeusXMLException,CoeusException,DBException, JAXBException{
            
        nsfAppChecklistTxnBean = new NSFAppChecklistTxnBean();
        
        try{
            NSFApplicationChecklist13Type NSFApplicationChecklist =
                                    objFactory.createNSFApplicationChecklist13();
            
            NSFApplicationChecklist13Type.CoverSheetType coverSheet =
                objFactory.createNSFApplicationChecklist13TypeCoverSheetType();


           /********************************************************
            * get questionnaire answers
            *
            ********************************************************/
           CoeusVector cvQuestions = null;
           cvQuestions =  nsfAppChecklistTxnBean.getQuestionnaireAnswers(propNumber);
           HashMap hmQuestions = new HashMap();

           int numQuestions = cvQuestions.size();
           int questionId = 0;
           int questionNumber = 0;

           String answer = null;
           NSFApplicationChecklist13Type.RRSrProfileType RRSrProfile =
                objFactory.createNSFApplicationChecklist13TypeRRSrProfileType();
           NSFApplicationChecklist13Type.ProjectNarrativeType projectNarrative =
                objFactory.createNSFApplicationChecklist13TypeProjectNarrativeType();


            if (numQuestions >0){
            //temporary default answer
                projectNarrative.setCheckMentoring("N: No");
            for(int i=0;i<numQuestions;i++){
                hmQuestions = (HashMap) cvQuestions.get(i);
                questionId = Integer.parseInt(hmQuestions.get("QUESTION_ID").toString());
                answer = hmQuestions.get("ANSWER").toString();
                questionNumber = Integer.parseInt(hmQuestions.get("QUESTION_NUMBER").toString());


              switch (questionId) {
                  case PRELIMINARY:
                      //related to submission of preliminary app
                      if (answer != null)
                           if (answer.equalsIgnoreCase("Y"))
                                 answer = "Y: Yes";
                            if (answer.equalsIgnoreCase("N"))
                                answer = "N: No";
                            if (answer.equalsIgnoreCase("X"))
                                 answer = "NA: Not Applicable";
                      coverSheet.setCheckFullApp(answer);
                       

                      break;
                     case MERIT:

                      if (answer != null)
                           if (answer.equalsIgnoreCase("Y"))
                                 answer = "Y: Yes";
                            if (answer.equalsIgnoreCase("N"))
                                answer = "N: No";
                            if (answer.equalsIgnoreCase("X"))
                                 answer = "NA: Not Applicable";
                       projectNarrative.setCheckMeritReview(answer);

                      break;

                    case MENTORING:

                      if (answer != null)
                           if (answer.equalsIgnoreCase("Y"))
                                 answer = "Y: Yes";
                            if (answer.equalsIgnoreCase("N"))
                                answer = "N: No";

                     projectNarrative.setCheckMentoring(answer);

                      break;

                   case PRIOR_SUPPORT:
                      /** Does narrative include info regarding prior support? */
                      if (answer != null)
                           if (answer.equalsIgnoreCase("Y"))
                                 answer = "Y: Yes";
                            if (answer.equalsIgnoreCase("N"))
                                answer = "N: No";
                        if (answer.equalsIgnoreCase("X"))
                                 answer = "NA: Not Applicable";
                      projectNarrative.setCheckPriorSupport(answer);
                      break;
                   case HR_QUESTION:
                    /* HR Info that is mandatory for renwals from academic institutions. */
                      if (answer != null){
                           if (answer.equalsIgnoreCase("N"))
                                answer = "N: No";
                           else answer = "Y: Yes";
                        projectNarrative.setCheckHRInfo(answer);
                      }
                      break;
                    case HR_REQUIRED_INFO:
                    /* HR Info that is mandatory for renwals from academic institutions. */
                      if (answer != null)
                           if (answer.equalsIgnoreCase("Y"))
                                answer = "Y: Yes";
                           else answer ="N: No";

                        projectNarrative.setCheckHRInfo(answer);
                      break;

                  default:
                        break;

                 }  //switch question id
              }   //for num questions
           }  //if question

           //get other answers from ynqs
            coverSheet.setCheckCoverSheet(getChecklistAnswer(1));
            coverSheet.setCheckRenewal(getChecklistAnswer(2));
          
           
            /* NSF proposals should not be "continuations" */
            coverSheet.setCheckTypeApp(getChecklistAnswer(4));
            /* Is the application certified? */
            coverSheet.setCheckAppCert(getChecklistAnswer(5));
            NSFApplicationChecklist.setCoverSheet(coverSheet);
            
            NSFApplicationChecklist.setCheckRRSite(getChecklistAnswer(6));
            NSFApplicationChecklist.setCheckRROtherInfo(getChecklistAnswer(7));
            
            NSFApplicationChecklist.setCheckProjectSummary(getChecklistAnswer(8));;
                      
            projectNarrative.setCheckProjectNarrative(getChecklistAnswer(9));
       
            projectNarrative.setCheckURL(getChecklistAnswer(11));            
        

       
            NSFApplicationChecklist.setProjectNarrative(projectNarrative);
            /* Bibliography attached. */
            NSFApplicationChecklist.setCheckBiblio(getChecklistAnswer(14));
            /* Facilities attached. */
            NSFApplicationChecklist.setCheckFacilities(getChecklistAnswer(15));
            
            NSFApplicationChecklist13Type.EquipmentType equipment =
                objFactory.createNSFApplicationChecklist13TypeEquipmentType();
            /* Equipment attached. */
            equipment.setCheckEquipment(getChecklistAnswer(16));
            /* Supplementary information pdf attached. */
            equipment.setCheckSuppDoc(getChecklistAnswer(17));   
            /* Additional items relevant to NSF Program complete. */
            equipment.setCheckAdditionalItems(getChecklistAnswer(18));
            NSFApplicationChecklist.setEquipment(equipment);
            
           
            RRSrProfile.setCheckRRSrProfile(getChecklistAnswer(19));            
            RRSrProfile.setCheckBioSketch(getChecklistAnswer(20));
            RRSrProfile.setCheckCurrentPendingSupport(getChecklistAnswer(21));
            
            NSFApplicationChecklist.setRRSrProfile(RRSrProfile);
            
            NSFApplicationChecklist.setCheckRRPersonalData(getChecklistAnswer(22));

            NSFApplicationChecklist13Type.RRBudgetType RRBudget =
                objFactory.createNSFApplicationChecklist13TypeRRBudgetType();
            /* Budget complete */
            RRBudget.setCheckRRBudget(getChecklistAnswer(23)); 
            /* Budget justification attached. */
            RRBudget.setCheckRRBudgetJustification(getChecklistAnswer(24));
//            /* Cost sharing correctly implemented. */
//            RRBudget.setCheckCostSharing(getChecklistAnswer(25));
            NSFApplicationChecklist.setRRBudget(RRBudget);
            
            NSFApplicationChecklist13Type.NSFCoverType NSFCover =
                objFactory.createNSFApplicationChecklist13TypeNSFCoverType();
            //NSF Cover Page stream included
            NSFCover.setCheckNSFCover(getChecklistAnswer(26));
            //NSF Unit Consideration complete on NSF Cover Page
            NSFCover.setCheckNSFUnit(getChecklistAnswer(27));
            //Other NSF Info
            NSFCover.setCheckNSFOtherInfo(getChecklistAnswer(28));
            //Lobbying stream included
            NSFCover.setCheckNSFSFLLL(getChecklistAnswer(29));
            /* Deviation athorization included */
            NSFCover.setCheckNSFDevAuth(getChecklistAnswer(30));
            /* NSF Fast Lane registration included */
            NSFCover.setCheckNSFReg(getChecklistAnswer(31));
            /* Suggested Reviewers included. */
            NSFCover.setCheckDoNotInclude(getChecklistAnswer(32));
            NSFApplicationChecklist.setNSFCover(NSFCover);
            
           /**
            *FormVersion
           */
            
           NSFApplicationChecklist.setFormVersion("1.3");
             
           return NSFApplicationChecklist;
           
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"NSFApplicationChecklistV1_3Stream",
                "getNSFApplicationChecklist()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
    }
    
    private String getChecklistAnswer(int questionId)
    throws DBException, CoeusException{
         String answer = nsfAppChecklistTxnBean.getNSFChecklistAnswer(propNumber, questionId);

        //v1.1 changed enumeration. (from v1.0 "Yes/No/NotApplicable" to  V1.1 "Y: Yes/N: No/NA: Not Applicable")
        if (answer != null)
            if (answer.equalsIgnoreCase("Yes"))
                answer = "Y: Yes";
            if (answer.equalsIgnoreCase("No"))
                answer = "N: No";
            if (answer.equalsIgnoreCase("NotApplicable"))
                answer = "NA: Not Applicable";
        return answer;
    } 
     
    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getNSFApplicationChecklist();
    }    
 
 }   
    
