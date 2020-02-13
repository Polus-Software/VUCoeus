
package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.propdev.bean.ProposalSpecialReviewFormBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.bean.KirschsteinBean;
import edu.mit.coeus.s2s.bean.S2SPHS398FellowshipSupTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.s2s.Attachment;
import java.math.BigDecimal;
import java.util.Calendar;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.DateUtils;

/**
 *
 * @author ele
 */
public class PHS398_FellowshipSup_V1_1Stream extends S2SBaseStream  { 
    private gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    
    private S2SPHS398FellowshipSupTxnBean phsFellowshipSupTxnBean;
    private ProposalDevelopmentFormBean proposalBean;
    private ProposalDevelopmentTxnBean proposalDevTxnBean;
    private String propNumber;
    private UtilFactory utilFactory;
    private Calendar calendar;

    private static final int HUMAN = 1;
    private static final int VERT = 4;
    private static final int CLINICAL = 2;
    private static final int PHASE3CLINICAL = 3;
    private static final int STEMCELLS = 5;
    private static final int KIRST_START_KNOWN = 43;
    private static final int KIRST_END_KNOWN = 49;
    private static final int KIRST_START_DT = 44;
    private static final int KIRST_END_DT = 45;
    private static final int KIRST_GRANT_KNOWN = 46;
    private static final int KIRST_GRANT_NUM = 27;
    private static final int PRE_OR_POST = 32;
    private static final int IND_OR_INST = 33;
    private static final int STEMCELLLINES = 7;
    private static final int CELLLINEIND = 6;
    private static final int  DEGREE_TYPE_SOUGHT =15;
    private static final int DEG_EXP_COMP_DATE = 35;
    private static final int NRSA_SUPPORT = 24;
    private static final int FIELD_TRAINING = 22;
    private static final int BROAD_TRAINING = 23;
    private static final int OTHER_MASTERS=16;
    private static final int  OTHER_DOCT=17;
    private static final int  OTHER_DDOT=18;
    private static final int OTHER_VDOT=19;
    private static final int  OTHER_DBOTH=20;
    private static final int   OTHER_MDOT=21;
    private static final int SUBMITTED_DIFF_INST=28;
    private static final int SENIOR_FELL = 36;
    private static final int SUPP_FUNDING_AMT = 38;
    private static final int SUPP_MONTHS = 51;
    private static final int  SUPP_SOURCE = 41;
    private static final int  SUPP_TYPE = 40;

    HashMap hmBudgetQuestions;
    
   
    public PHS398_FellowshipSup_V1_1Stream(){
        objFactory = new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
        xmlGenerator = new CoeusXMLGenrator();  
           
    }    

  private PHSFellowshipSupplemental11Type getPHSFellowshipSup()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
  
     
      PHSFellowshipSupplemental11Type phsFellowshipSupplementalType = objFactory.createPHSFellowshipSupplemental11();
      PHSFellowshipSupplemental11Type.AdditionalInformationType additionalInfoType =
               objFactory.createPHSFellowshipSupplemental11TypeAdditionalInformationType();
      PHSFellowshipSupplemental11Type.AdditionalInformationType.StemCellsType stemCellstype =
               objFactory.createPHSFellowshipSupplemental11TypeAdditionalInformationTypeStemCellsType();
      PHSFellowshipSupplemental11Type.AdditionalInformationType.GraduateDegreeSoughtType degreeSoughtType =
                objFactory.createPHSFellowshipSupplemental11TypeAdditionalInformationTypeGraduateDegreeSoughtType();
      PHSFellowshipSupplemental11Type.AdditionalInformationType.CurrentPriorNRSASupportType nrsaSupportType =
               objFactory.createPHSFellowshipSupplemental11TypeAdditionalInformationTypeCurrentPriorNRSASupportType();
      PHSFellowshipSupplemental11Type.AdditionalInformationType.ConcurrentSupportDescriptionType concurrentSupportType =
              objFactory.createPHSFellowshipSupplemental11TypeAdditionalInformationTypeConcurrentSupportDescriptionType();
      PHSFellowshipSupplemental11Type.AdditionalInformationType.FellowshipTrainingAndCareerGoalsType fellowshipType =
              objFactory.createPHSFellowshipSupplemental11TypeAdditionalInformationTypeFellowshipTrainingAndCareerGoalsType();
      PHSFellowshipSupplemental11Type.AdditionalInformationType.DissertationAndResearchExperienceType dissertationType =
              objFactory.createPHSFellowshipSupplemental11TypeAdditionalInformationTypeDissertationAndResearchExperienceType();
      PHSFellowshipSupplemental11Type.AdditionalInformationType.ActivitiesPlannedUnderThisAwardType activitiesType =
              objFactory.createPHSFellowshipSupplemental11TypeAdditionalInformationTypeActivitiesPlannedUnderThisAwardType();
      PHSFellowshipSupplemental11Type.ApplicationTypeType applicationType =
              objFactory.createPHSFellowshipSupplemental11TypeApplicationTypeType();
      PHSFellowshipSupplemental11Type.BudgetType budgetType =
              objFactory.createPHSFellowshipSupplemental11TypeBudgetType();
      PHSFellowshipSupplemental11Type.ResearchTrainingPlanType resTrainingPlanType =
              objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanType();
      PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ResearchStrategyType resStrategyType =
              objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeResearchStrategyType();

      gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
      Attachment attachment = null;
        
    
          
      phsFellowshipSupTxnBean = new S2SPHS398FellowshipSupTxnBean();
      proposalDevTxnBean = new ProposalDevelopmentTxnBean();
       
          try{
         
          
          proposalBean = proposalDevTxnBean.getProposalDevelopmentDetails(propNumber);
          /**
            *FormVersion
          */
           phsFellowshipSupplementalType.setFormVersion("1.1");
      
          
            /**
             * application Type
             */
           
            HashMap hmInfo = new HashMap();
          
            hmInfo = phsFellowshipSupTxnBean.getApplicationType(propNumber);
            if (hmInfo.get("APPLICATIONTYPE") != null){
                applicationType.setTypeOfApplication(hmInfo.get("APPLICATIONTYPE").toString());
                phsFellowshipSupplementalType.setApplicationType(applicationType);
             }

            /**
            * citizenship
            */
            
            hmInfo = phsFellowshipSupTxnBean.getCitizenship(propNumber);
            if (hmInfo.get("CITIZENSHIP") != null) {
                additionalInfoType.setCitizenship(hmInfo.get("CITIZENSHIP").toString());
                
            }

           /**
            *HumanSubjectsIndicator and VertebrateAnimalsIndicator
           */
           Vector vecSpecialReview = proposalBean.getPropSpecialReviewFormBean();
           ProposalSpecialReviewFormBean specialReviewBean;
           String description;

           resTrainingPlanType.setHumanSubjectsInvolved("N: No");
           resTrainingPlanType.setVertebrateAnimalsUsed("N: No");


           if (vecSpecialReview != null) {
                for (int vecCount = 0 ; vecCount < vecSpecialReview.size() ; vecCount++) {
                  specialReviewBean = (ProposalSpecialReviewFormBean) vecSpecialReview.get(vecCount);

                  switch(specialReviewBean.getSpecialReviewCode()) {
                      case 1:
                          resTrainingPlanType.setHumanSubjectsInvolved("Y: Yes");
                         break;
                      case 2:
                          resTrainingPlanType.setVertebrateAnimalsUsed("Y: Yes");
                          break;
                      default:
                          break;
                  }   //switch
                }//for
            } //if

       
            /***************************************************
             * additional information
             *
             ****************************************************/
            HashMap hmAdditionalInfo = new HashMap();

            hmAdditionalInfo = phsFellowshipSupTxnBean.getAdditionalInfo(propNumber);
            if (hmAdditionalInfo != null) {
                if (hmAdditionalInfo.get("SECONDRY_OFFICE_PHONE") != null)
                    additionalInfoType.setAlernatePhoneNumber(hmAdditionalInfo.get("SECONDRY_OFFICE_PHONE").toString());
            }
           /********************************************************
            * get questionnaire answers
            ********************************************************/
           CoeusVector cvQuestions = null;
           cvQuestions =  phsFellowshipSupTxnBean.getQuestionnaireAnswers(proposalBean.getProposalNumber().toString());
           HashMap hmQuestions = new HashMap();
           hmBudgetQuestions = new HashMap();

           int numQuestions = cvQuestions.size();
           int questionId = 0;
           int questionNumber = 0;
           int parentQuestionNumber =0;
           String answer = null;
           KirschsteinBean cbKirschstein = new KirschsteinBean();
           CoeusVector cvKirsch = new CoeusVector();


           Calendar dateString = null;

           if (numQuestions >0){
            for(int i=0;i<numQuestions;i++){
                hmQuestions = (HashMap) cvQuestions.get(i);
                questionId = Integer.parseInt(hmQuestions.get("QUESTION_ID").toString());
                answer = hmQuestions.get("ANSWER").toString();
                questionNumber = Integer.parseInt(hmQuestions.get("QUESTION_NUMBER").toString());
                parentQuestionNumber = Integer.parseInt(hmQuestions.get("PARENT_QUESTION_NUMBER").toString());

              switch (questionId) {
                  case HUMAN:
                      //human subject involvement indefinite
                      if (answer != null) 
                          resTrainingPlanType.setHumanSubjectsIndefinite(answer.equals("Y") ? "Y: Yes" : "N: No");
                      break;
                  case VERT:
                        //will the inclusion of vertebrate animals use be indefinite
                      if (answer != null)
                          resTrainingPlanType.setVertebrateAnimalsIndefinite(answer.equals("Y") ? "Y: Yes" : "N: No");
                      break;
                  case CLINICAL:
                        //clinical trial
                      if (answer != null) 
                          resTrainingPlanType.setClinicalTrial(answer.equals("Y") ? "Y: Yes" : "N: No");
                      break;
                  case PHASE3CLINICAL:
                        //phase 3 clinical trial
                      if (answer != null)
                          resTrainingPlanType.setPhase3ClinicalTrial(answer.equals("Y") ? "Y: Yes" : "N: No");
                      break;
                  case STEMCELLS:
                       //stem cells used
                       if (answer != null)
                            stemCellstype.setIsHumanStemCellsInvolved(answer.equals("Y") ? "Y: Yes" : "N: No");
                        break;
                  case CELLLINEIND:
                      
                       //stem cell line indicator
                        if (answer!=null)
                            stemCellstype.setStemCellsIndicator(answer.equals("Y") ? "N: No" : "Y: Yes");
                        break;
                   case STEMCELLLINES:
                       
                        //stem cell lines - there can be multiple ones
                        if (answer!= null)
                            stemCellstype.getCellLines().add(answer);
                        break;

//                    case 9:OTHER_MOTH_SOUGHT = 9;
//                    case 10:OTHER_DOTH_SOUGHT =10
//                    case 11:OTHER_DDOT_SOUGHT=11;
//                    case 12: OTHER_MDOT_SOUGHT=12;
//                    case 13: OTHER_VDOT_SOUGHT =13;
                   case  DEGREE_TYPE_SOUGHT :
                        //graduate degree sought
                        if (answer!= null)
                            degreeSoughtType.setDegreeType(answer);
                        break;
                   case DEG_EXP_COMP_DATE:
                      
                        //graduate degree expected completion date
                        if (answer!= null)
                        // nih wants date in format YYYY-MM
                        //    degreeSoughtType.setDegreeDate(answer.substring(0,3) + answer.substring(6,10));
                              degreeSoughtType.setDegreeDate(answer.substring(6,10) + '-' + answer.substring(0,2));
                        break;
                  case OTHER_MASTERS:
                  case OTHER_DOCT:
                  case OTHER_DDOT:
                  case OTHER_VDOT:
                  case OTHER_DBOTH:
                  case OTHER_MDOT:
                        //other graduate degree sought
                        if (answer!= null)
                           degreeSoughtType.setOtherDegreeTypeText(answer);
                        break;
                 //   case 23:
                    case BROAD_TRAINING:
                    case  FIELD_TRAINING:
                        
                        //field of training
                        if (answer!= null )
                            if (!answer.toUpperCase().equals("SUB CATEGORY NOT FOUND"))
                               additionalInfoType.setFieldOfTraining(answer);
                        break;
                   case NRSA_SUPPORT:
                      
                        //   Kirschstein-NRSA support nrsaSupportType
                      if (answer != null)
                            additionalInfoType.setCurrentPriorNRSASupportIndicator(answer.equals("Y") ? "Y: Yes" : "N: No");
                        break;
                    // questions  -  can repeat for kirschstein

                 case KIRST_START_KNOWN: //do you know start date
                 case KIRST_END_KNOWN: // do you know end date
                 case KIRST_START_DT: //start date
                 case KIRST_END_DT: //end date
                 case KIRST_GRANT_KNOWN: // do you know grant number
                 case KIRST_GRANT_NUM:  //grant num
                 case PRE_OR_POST:  //pre or post
                 case IND_OR_INST: //ind or inst

                    if (answer != null){
                        if (questionId == KIRST_START_KNOWN){
                            if (answer.equals("N")){
                                answer = "Unknown";
                                questionId = KIRST_START_DT;  //start date
                                //do not use question number or parent question number
                            } else
                                break;
                        }
                         if (questionId == KIRST_END_KNOWN){
                            if (answer.equals("N")){
                                answer = "Unknown";
                                questionId = KIRST_END_DT;  //end date
                                //do not use question number or parent question number
                            } else
                                break;
                         }
                        if (questionId == KIRST_GRANT_KNOWN){
                            if (answer.equals("N")){
                                answer = "Unknown";
                                questionId = KIRST_GRANT_NUM;  //grant number
                                //do not use question number or parent question number
                            } else
                                break;
                         }
                        cbKirschstein = new KirschsteinBean();
                        cbKirschstein.setAnswer(answer);
                        cbKirschstein.setQuestionId(questionId);
                        cbKirschstein.setQuestionNumber(questionNumber);
                        cbKirschstein.setParentQuestionNumber(parentQuestionNumber);
                        cvKirsch.add(cbKirschstein);
                        }
                        break;
                    case SUBMITTED_DIFF_INST:

                        //   submitted by different institution
                        if (answer!= null)
                            additionalInfoType.setChangeOfInstitution(answer.equals("Y") ? "Y: Yes" : "N: No");
                        break;
                    case 29:
                        //    different institution
                        if (answer!= null)
                            additionalInfoType.setFormerInstitution(answer);
                        break;
                  case 36:

                      // senior fellowship
                      if (answer != null)
                          hmBudgetQuestions.put("SENIOR", answer);
                      break;
                  case SUPP_SOURCE:

                      //supplemental sources
                        if (answer != null)
                          hmBudgetQuestions.put("SUPP_SOURCE", answer);
                      break;
                      
                  case SUPP_FUNDING_AMT:

                      //supplemental funding
                        if (answer != null)
                          hmBudgetQuestions.put("SUPP_FUNDING", answer);
                      break;
                  case SUPP_MONTHS:

                      // supp months
                      if (answer != null)
                          hmBudgetQuestions.put("SUPP_MONTHS", answer);
                      break;
                  case SUPP_TYPE:

                      //supp type
                       if (answer != null)
                          hmBudgetQuestions.put("SUPP_TYPE", answer);
                      break;
                  case 50:
                      // number of months of salary
                      if (answer != null)
                          hmBudgetQuestions.put("SALARY_MONTHS", answer);
                      break;
                  case 48:
                      //academic period
                      if (answer != null)
                          hmBudgetQuestions.put("ACAD_PERIOD", answer);
                      break;
                  case 47:
                      //base salary
                      if (answer != null)
                          hmBudgetQuestions.put("BASE_SALARY", answer);
                      break;
                 
                  default:
                        break;

                 }  //switch question id
              }   //for num questions
           }  //if qustion

          if (stemCellstype != null)
              additionalInfoType.setStemCells(stemCellstype);
         

          if (degreeSoughtType.getDegreeType() != null)
              additionalInfoType.setGraduateDegreeSought(degreeSoughtType);

           CoeusVector cvType = new CoeusVector();
           CoeusVector cvStart = new CoeusVector();
           CoeusVector cvEnd = new CoeusVector();
           CoeusVector cvLevel = new CoeusVector();
           CoeusVector cvGrant = new CoeusVector();
           KirschsteinBean kbBean1 = new KirschsteinBean();
           KirschsteinBean kbBean2 = new KirschsteinBean();
           KirschsteinBean kbBean3 = new KirschsteinBean();
           KirschsteinBean kbBean4 = new KirschsteinBean();
           KirschsteinBean kbBean5 = new KirschsteinBean();

           if (additionalInfoType.getCurrentPriorNRSASupportIndicator() != null){
             if (additionalInfoType.getCurrentPriorNRSASupportIndicator().equals("Y: Yes")) {
                  KirschsteinBean kbBean = new KirschsteinBean();
                 //cvKirsch has all the beans
                 //sort them by question_number
                 cvKirsch.sort("questionNumber");
                 //go through vector looking for the 4 questions we care about
                 //and put them in their own vector
                   for(int i=0;i<cvKirsch.size();i++){
                        kbBean = (KirschsteinBean) cvKirsch.get(i);
                        if (kbBean.getQuestionId() == PRE_OR_POST) {
                             cvLevel.add(kbBean);
                        }
                        if (kbBean.getQuestionId() == IND_OR_INST) {
                             cvType.add(kbBean);
                        }
                        if (kbBean.getQuestionId() == KIRST_START_DT) {
                             cvStart.add(kbBean);
                        }
                        if (kbBean.getQuestionId() == KIRST_END_DT) {
                             cvEnd.add(kbBean);
                        }
                        if (kbBean.getQuestionId() == KIRST_GRANT_NUM) {
                             cvGrant.add(kbBean);
                        }

                        }
                   }

                  //now grab first row of each vector
                  //each vector will have same number of rows
                 int numberRepeats = cvLevel.size();
                 if (numberRepeats > 0) {
                  for (int j=0;j<numberRepeats;j++){
                    kbBean1 = (KirschsteinBean) cvLevel.get(j);
                    kbBean2 = (KirschsteinBean) cvType.get(j);
                    kbBean3 = (KirschsteinBean) cvStart.get(j);
                    kbBean4 = (KirschsteinBean) cvEnd.get(j);
                    kbBean5 = (KirschsteinBean) cvGrant.get(j);

                    nrsaSupportType =
                      objFactory.createPHSFellowshipSupplemental11TypeAdditionalInformationTypeCurrentPriorNRSASupportType();
                    nrsaSupportType.setLevel(kbBean1.getAnswer().toString());
                    nrsaSupportType.setType(kbBean2.getAnswer().toString());
                    nrsaSupportType.setStartDate(convertDateStringToCalendar(kbBean3.getAnswer().toString()));
                    nrsaSupportType.setEndDate(convertDateStringToCalendar(kbBean4.getAnswer().toString()));
                    nrsaSupportType.setGrantNumber(kbBean5.getAnswer().toString());

                    additionalInfoType.getCurrentPriorNRSASupport().add(nrsaSupportType);
                   }
                 }
             } // if equals yes


           /* use questionnaire instead of proposal budget */

            /*******************************************************
            * budget
            *
            *******************************************************/
            PHSFellowshipSupplemental11Type.BudgetType.InstitutionalBaseSalaryType instBaseSalary =
                 objFactory.createPHSFellowshipSupplemental11TypeBudgetTypeInstitutionalBaseSalaryType();
            
            PHSFellowshipSupplemental11Type.BudgetType.FederalStipendRequestedType fedStipend =
                 objFactory.createPHSFellowshipSupplemental11TypeBudgetTypeFederalStipendRequestedType();
            
            PHSFellowshipSupplemental11Type.BudgetType.SupplementationFromOtherSourcesType suppType =
                 objFactory.createPHSFellowshipSupplemental11TypeBudgetTypeSupplementationFromOtherSourcesType();

            BigDecimal bdTotalTuition ;
            BigDecimal bdtuitionYr1 ;
            BigDecimal bdtuitionYr2 ;
            BigDecimal bdtuitionYr3 ;
            BigDecimal bdtuitionYr4;
            BigDecimal bdtuitionYr5 ;
            BigDecimal bdtuitionYr6 ;

            BigDecimal bdCalcBase = null;
            BigDecimal bdNumMonths = null;
            BigDecimal bdStipendMonths = null;
            BigDecimal bdStipend = null;

            HashMap hmBudget = new HashMap();

            budgetType.setTuitionAndFeesRequested("N: No");
            hmBudget = phsFellowshipSupTxnBean.getBudgetInfo(propNumber);
            if (!hmBudgetQuestions.isEmpty()) {
              if (hmBudgetQuestions.get("SENIOR").toString().equals( "Y")) {
                if (hmBudgetQuestions.get("BASE_SALARY") != null) {
                        bdCalcBase = new BigDecimal(hmBudgetQuestions.get("BASE_SALARY").toString());
                        instBaseSalary.setAmount(bdCalcBase);
                }


                if (hmBudgetQuestions.get("ACAD_PERIOD") != null) {
                      instBaseSalary.setAcademicPeriod(hmBudgetQuestions.get("ACAD_PERIOD").toString());
                }

                if (hmBudgetQuestions.get("SALARY_MONTHS") != null){
                    //check if it is a number
                    String numMonths = hmBudgetQuestions.get("SALARY_MONTHS").toString();
                    if (isANum(numMonths)) {
                     bdNumMonths = new BigDecimal(hmBudgetQuestions.get("SALARY_MONTHS").toString());
                    instBaseSalary.setNumberOfMonths(bdNumMonths);
                    }
                 }
                if (instBaseSalary != null){
                    budgetType.setInstitutionalBaseSalary(instBaseSalary);
                 }
           
                /*
                *  stipends
                */
                if (hmBudget.get("STIPEND_MONTHS") != null){
                  bdStipendMonths = new BigDecimal(hmBudget.get("STIPEND_MONTHS").toString());
                     fedStipend.setNumberOfMonths(bdStipendMonths);
                }

                if (hmBudget.get("STIPEND_COST") != null){
                  bdStipend = new BigDecimal(hmBudget.get("STIPEND_COST").toString());
                  fedStipend.setAmount(bdStipend);
                }

                 if (bdStipendMonths != null)
                   budgetType.setFederalStipendRequested(fedStipend);

          }  //end is senior
        }
             /*
              * tuition
              */
             if (hmBudget.get("TUITION1") != null){
                 bdtuitionYr1 = new BigDecimal(hmBudget.get("TUITION1").toString());
                 budgetType.setTuitionRequestedYear1(bdtuitionYr1);
             }
              if (hmBudget.get("TUITION2") != null){
                   bdtuitionYr2 = new BigDecimal(hmBudget.get("TUITION2").toString());
                     budgetType.setTuitionRequestedYear2(bdtuitionYr2);
             }
              if (hmBudget.get("TUITION3") != null){
                  bdtuitionYr3 = new BigDecimal(hmBudget.get("TUITION3").toString());
                   budgetType.setTuitionRequestedYear3(bdtuitionYr3);
             }
              if (hmBudget.get("TUITION4") != null){
                 bdtuitionYr4 = new BigDecimal(hmBudget.get("TUITION4").toString());
                  budgetType.setTuitionRequestedYear4(bdtuitionYr4);
             }
               if (hmBudget.get("TUITION5") != null){
                   bdtuitionYr5 = new BigDecimal(hmBudget.get("TUITION5").toString());
                   budgetType.setTuitionRequestedYear5(bdtuitionYr5);
             }
              if (hmBudget.get("TUITION6") != null){
                  bdtuitionYr6 = new BigDecimal(hmBudget.get("TUITION6").toString());
                  budgetType.setTuitionRequestedYear6(bdtuitionYr6);
             }
             if (hmBudget.get("TUITION_TOTAL") != null){
                   bdTotalTuition = new BigDecimal(hmBudget.get("TUITION_TOTAL").toString());
                   budgetType.setTuitionRequestedTotal(bdTotalTuition);
                   budgetType.setTuitionAndFeesRequested("Y: Yes");
             }


             if (!hmBudgetQuestions.isEmpty()) {
               if(hmBudgetQuestions.get("SENIOR") != null) {
                    if (hmBudgetQuestions.get("SENIOR").toString().toUpperCase().equals("Y")) {
                         /* supp type from questionnaire  */
                        if (hmBudgetQuestions.get("SUPP_SOURCE")  != null) {
                            suppType.setSource(hmBudgetQuestions.get("SUPP_SOURCE").toString());
                             suppType.setAmount(new BigDecimal (hmBudgetQuestions.get("SUPP_FUNDING").toString()));
                             //check if number of months is a number
                             String strSuppMonths = hmBudgetQuestions.get("SUPP_MONTHS").toString();
                             if (isANum(strSuppMonths)) {
                             suppType.setNumberOfMonths(new BigDecimal (hmBudgetQuestions.get("SUPP_MONTHS").toString()));
                             }
                            suppType.setType(hmBudgetQuestions.get("SUPP_TYPE").toString());
                           
                            budgetType.setSupplementationFromOtherSources(suppType);

                        }
                    }
               }
             }

            /**
             * attachments
             */
        
            int narrativeType;
            int moduleNum;
          ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
          ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;

          additionalInfoType.setConcurrentSupport("N: No"); //default
                      
          Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber); 
         
           S2STxnBean s2sTxnBean = new S2STxnBean();
           LinkedHashMap hmArg = new LinkedHashMap();
                     
           HashMap hmNarrative = new HashMap();
     
           int size=vctNarrative==null?0:vctNarrative.size();
           for (int row=0; row < size;row++) {
               proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                           
               moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();   
               hmNarrative = new HashMap();
               hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
               narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
               description = hmNarrative.get("DESCRIPTION").toString();
      
               hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
               hmArg.put(ContentIdConstants.DESCRIPTION, description);
               
               attachment = getAttachment(hmArg);
               PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.IntroductionToApplicationType
                  introType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeIntroductionToApplicationType();


                //IntroductionToApplication
                 if (narrativeType == 97) {
                  if (attachment == null) {
                     proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);      
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        introType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeIntroductionToApplicationType();

                        introType.setAttFile(attachedFileType);
                        resTrainingPlanType.setIntroductionToApplication(introType);
                     }
                   }                  
                  }
               
                //Specific Aims
                 PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.SpecificAimsType specificAimsType ;

                 if (narrativeType == 98) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);                   
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        specificAimsType =  objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeSpecificAimsType();
                        specificAimsType.setAttFile(attachedFileType);
                        resTrainingPlanType.setSpecificAims(specificAimsType);
                    }
                  }                  
               }

               
               //InclusionEnrollmentReport
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.InclusionEnrollmentReportType inclusionType;
                if (narrativeType == 102) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);

                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        inclusionType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeInclusionEnrollmentReportType();
                        inclusionType.setAttFile(attachedFileType);
                        resTrainingPlanType.setInclusionEnrollmentReport(inclusionType);

                     }
                  }                  
               }


               //ProgressReportPublicationList
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ProgressReportPublicationListType progressType;
               if (narrativeType == 103) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        progressType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeProgressReportPublicationListType();
                        progressType.setAttFile(attachedFileType);
                        resTrainingPlanType.setProgressReportPublicationList(progressType);
                     }
                  }                  
               }
               
            
               //Protection of human subjects
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ProtectionOfHumanSubjectsType humanType;
                  if (narrativeType == 104) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        humanType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeProtectionOfHumanSubjectsType();
                        humanType.setAttFile(attachedFileType);

                        resTrainingPlanType.setProtectionOfHumanSubjects(humanType);
                     }
                  }
               }

                //Inclusion of Women and Minorities
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.InclusionOfWomenAndMinoritiesType womenAndMinType;
                  if (narrativeType == 105) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        womenAndMinType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeInclusionOfWomenAndMinoritiesType();

                        womenAndMinType.setAttFile(attachedFileType);

                        resTrainingPlanType.setInclusionOfWomenAndMinorities(womenAndMinType);
                     }
                  }
               }

                //Targeted Planned Enrollment
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.TargetedPlannedEnrollmentType targetType;
                  if (narrativeType == 106) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        targetType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeTargetedPlannedEnrollmentType();

                        targetType.setAttFile(attachedFileType);

                        resTrainingPlanType.setTargetedPlannedEnrollment(targetType);
                     }
                  }
               }

                 //Inclusion of Children
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.InclusionOfChildrenType childrenType;
                  if (narrativeType == 107) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        childrenType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeInclusionOfChildrenType();

                        childrenType.setAttFile(attachedFileType);

                        resTrainingPlanType.setInclusionOfChildren(childrenType);
                     }
                  }
               }
             
                //Vertebrate Animals
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.VertebrateAnimalsType vertAnimalsType;
                  if (narrativeType == 108) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        vertAnimalsType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeVertebrateAnimalsType();
                             
                        vertAnimalsType.setAttFile(attachedFileType);
                     
                        resTrainingPlanType.setVertebrateAnimals(vertAnimalsType);
                     }
                  }                  
               }

                //Select Agent Research
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.SelectAgentResearchType selectAgentType;
                  if (narrativeType == 109) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        selectAgentType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeSelectAgentResearchType();

                        selectAgentType.setAttFile(attachedFileType);

                        resTrainingPlanType.setSelectAgentResearch(selectAgentType);
                     }
                  }
               }

                //Resource Sharing plan
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ResourceSharingPlanType resourceSharingType;
                  if (narrativeType == 110) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        resourceSharingType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeResourceSharingPlanType();

                        resourceSharingType.setAttFile(attachedFileType);

                        resTrainingPlanType.setResourceSharingPlan(resourceSharingType);
                     }
                  }
               }

                //Respective Contributions
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.RespectiveContributionsType respectiveContType;
                  if (narrativeType == 88) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        respectiveContType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeRespectiveContributionsType();

                        respectiveContType.setAttFile(attachedFileType);

                        resTrainingPlanType.setRespectiveContributions(respectiveContType);
                     }
                  }
               }


                 //Selection of Sponsor and Institution
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.SelectionOfSponsorAndInstitutionType selectionOfSponType;
                  if (narrativeType == 89) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        selectionOfSponType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeSelectionOfSponsorAndInstitutionType();

                        selectionOfSponType.setAttFile(attachedFileType);

                        resTrainingPlanType.setSelectionOfSponsorAndInstitution(selectionOfSponType);
                     }
                  }
               }


                //Responsible conduct of research
                PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ResponsibleConductOfResearchType conductType;
                  if (narrativeType == 90) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        conductType = objFactory.createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeResponsibleConductOfResearchType();

                        conductType.setAttFile(attachedFileType);

                        resTrainingPlanType.setResponsibleConductOfResearch(conductType);
                     }
                  }
               }

               //Concurrent support
                if (narrativeType == 91) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        concurrentSupportType.setAttFile(attachedFileType);
                        additionalInfoType.setConcurrentSupportDescription(concurrentSupportType);
                        additionalInfoType.setConcurrentSupport("Y: Yes");
                     }
                  }
               }

                 //Fellowship 

                  if (narrativeType == 92) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        fellowshipType.setAttFile(attachedFileType);
                        additionalInfoType.setFellowshipTrainingAndCareerGoals(fellowshipType);

                     }
                  }
               }

                 //Dissertation 

                  if (narrativeType == 93) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        dissertationType.setAttFile(attachedFileType);
                        additionalInfoType.setDissertationAndResearchExperience(dissertationType);

                     }
                  }
               }

                 //Activities 

                  if (narrativeType == 94) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        activitiesType.setAttFile(attachedFileType);
                        additionalInfoType.setActivitiesPlannedUnderThisAward(activitiesType);

                     }
                  }
               }

                //Research Strategy

                  if (narrativeType == 127) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        resStrategyType.setAttFile(attachedFileType);
                        resTrainingPlanType.setResearchStrategy(resStrategyType);
                        

                     }
                  }
               }

             //Appendix - can be multiple
                  if (narrativeType == 96) {
                      if (attachment == null) {
                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                        Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                        if (narrativeAttachment.getContent() != null){
                           attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                           attachmentGroup.getAttachedFile().add(attachedFileType);

                         }
                      }
                   }



           }

            if (attachmentGroup != null && attachmentGroup.getAttachedFile().size()  > 0 )
                   phsFellowshipSupplementalType.setAppendix(attachmentGroup);


          

           
       }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398_FellowshipSup_V1_0Stream","getPHSFellowshipSup()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }     

      if (additionalInfoType != null) phsFellowshipSupplementalType.setAdditionalInformation(additionalInfoType);
      if (resTrainingPlanType != null) phsFellowshipSupplementalType.setResearchTrainingPlan(resTrainingPlanType);
      if (budgetType != null) phsFellowshipSupplementalType.setBudget(budgetType);
      return phsFellowshipSupplementalType;
    
  }

   public Calendar convertDateStringToCalendar(String dateStr)
    {
        try {
            java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();

        DateUtils dtUtils = new DateUtils();
        if (dateStr != null)
        {
            if (dateStr.indexOf('-')!= -1)
            { // if the format obtd is YYYY-MM-DD
              dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
            }
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                        Integer.parseInt(dateStr.substring(0,2)) - 1,
                        Integer.parseInt(dateStr.substring(3,5))) ;

            return calDate ;
        }
        }catch(Exception ex){

        }
        return null ;
     }

public  boolean  isANum(String s) {
try {
  Double.parseDouble(s);
}
catch (NumberFormatException nfe) {
return false;
}
return true;
}

 public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHSFellowshipSup();
    }     
    
}
