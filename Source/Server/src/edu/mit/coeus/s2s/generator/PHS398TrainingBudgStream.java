/*
 * @(#)PHS398TrainingBudgStream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.phs398_trainingbudget_v1.*;

import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.web.GetNarrativeDocumentBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import java.io.IOException;

import java.math.BigDecimal;

import java.sql.Date;
//coeusqa-3900 start
import java.sql.Timestamp;
//coeusqa-3900 end

import java.util.Calendar;
import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  Eleanor Shavell
 */

 public class PHS398TrainingBudgStream extends S2SBaseStream{ 
    private gov.grants.apply.forms.phs398_trainingbudget_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalLibObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
  
    //txn bean
    private S2SPHS398TrainingBudgTxnBean s2SPHS398TrainingBudgTxnBean;
    private S2STxnBean s2sTxnBean;
 
    private String propNumber;
    private HashMap hmInfo ;
    private UtilFactory utilFactory;
    private Calendar calendar;
   
   
   
//    Attachment attachment = null;

    private static final String UNDERGRADS ="Undergraduates";
    private static final String PREDOC = "Predoctoral";
    private static final String POSTDOC = "Postdoctoral";
   
  

   
    /** Creates a new instance of PHS398ChecklistStream */
    public PHS398TrainingBudgStream(){
        objFactory = new gov.grants.apply.forms.phs398_trainingbudget_v1.ObjectFactory();
        globalObjFactory = new  gov.grants.apply.system.global_v1.ObjectFactory ();
        globalLibObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
      
        xmlGenerator = new CoeusXMLGenrator();
     
        s2SPHS398TrainingBudgTxnBean = new S2SPHS398TrainingBudgTxnBean();
        s2sTxnBean = new S2STxnBean();
        hmInfo = new HashMap();
        
    
    } 
   
    private PHS398TrainingBudgetType getPHS398TrainingBudget() throws CoeusXMLException,CoeusException,DBException{
        PHS398TrainingBudgetType trainingBudgetType = null;
        int version =0;
        int numPeriods = 0;

        try{
           trainingBudgetType = objFactory.createPHS398TrainingBudget();
           trainingBudgetType.setFormVersion("1.0");

           hmInfo = getSimpleInfo();
           if (hmInfo.get("NUM_PERIODS".toString()) != null)
               numPeriods = Integer.parseInt( hmInfo.get("NUM_PERIODS").toString());
           if (hmInfo.get("BUDGET_TYPE".toString()) != null)
                trainingBudgetType.setBudgetType(hmInfo.get("BUDGET_TYPE").toString());
           if (hmInfo.get("DUNS_NUMBER".toString()) != null)
               trainingBudgetType.setDUNSNumber(hmInfo.get("DUNS_NUMBER").toString());
           if (hmInfo.get("ORG_NAME".toString()) != null)
               trainingBudgetType.setOrganizationName(hmInfo.get("ORG_NAME").toString());
           if (hmInfo.get("BUDGET_VERSION".toString()) != null)
               version = Integer.parseInt( hmInfo.get("BUDGET_VERSION").toString());

           BigDecimal value = new BigDecimal("0");
           CoeusVector cvBudgetInfo = new CoeusVector();

           /************************************************
            *  get budget period info 
            ************************************************/

            HashMap hmbudgetinfo = new HashMap();
            HashMap hmIndirectCost = new HashMap();
            CoeusVector cvIndirectCost = new CoeusVector();

            int numPeople = 0;

            BigDecimal stipendAmountOtherFull=new BigDecimal("0"), stipendAmountOtherShort=new BigDecimal("0");
            BigDecimal stipendAmountF=new BigDecimal("0"), stipendAmountJ = new BigDecimal("0");
            BigDecimal stipendAmountPreSingFull=new BigDecimal("0"), stipendAmountPreDualFull = new BigDecimal("0");
            BigDecimal stipendAmountPreSingShort=new BigDecimal("0"), stipendAmountPreDualShort = new BigDecimal("0");
            BigDecimal stipendAmount0=new BigDecimal("0"),stipendAmount1=new BigDecimal("0"),stipendAmount2=new BigDecimal("0"),
                       stipendAmount3=new BigDecimal("0"),stipendAmount4 = new BigDecimal("0") ;
            BigDecimal stipendAmount5=new BigDecimal("0"), stipendAmount6=new BigDecimal("0"), stipendAmount7 = new BigDecimal("0");
            BigDecimal stipendAmountDeg0=new BigDecimal("0"),stipendAmountDeg1=new BigDecimal("0"),
                       stipendAmountDeg2=new BigDecimal("0"),stipendAmountDeg3=new BigDecimal("0"),stipendAmountDeg4 = new BigDecimal("0") ;
            BigDecimal stipendAmountDeg5=new BigDecimal("0"), stipendAmountDeg6=new BigDecimal("0"),
                       stipendAmountDeg7 = new BigDecimal("0");
            BigDecimal stipendAmountNonDeg0=new BigDecimal("0"),stipendAmountNonDeg1=new BigDecimal("0"),
                       stipendAmountNonDeg2=new BigDecimal("0"),stipendAmountNonDeg3=new BigDecimal("0"),stipendAmountNonDeg4 = new BigDecimal("0") ;
            BigDecimal stipendAmountNonDeg5=new BigDecimal("0"), stipendAmountNonDeg6=new BigDecimal("0"), stipendAmountNonDeg7 = new BigDecimal("0");

            /***** cumulative stipends **/
            BigDecimal cumUndergradStipends = new BigDecimal("0"), cumPreDocSingleStipends=new BigDecimal("0"),
                       cumPreDocDualStipends=new BigDecimal("0"), cumPreDocTotalStipends = new BigDecimal("0");
            BigDecimal cumPostDocNonDegStipends=new BigDecimal("0"), cumPostDocDegStipends = new BigDecimal("0"),
                       cumPostDocTotalStipends = new BigDecimal("0");
            BigDecimal cumOtherStipends = new BigDecimal("0");

            /***** cumulative tuition  **/
            BigDecimal cumUndergradTuition = new BigDecimal("0"),cumPreDocSingleTuition =new BigDecimal("0"),
                       cumPreDocDualTuition =new BigDecimal("0"), cumPreDocTotalTuition  = new BigDecimal("0");
            BigDecimal cumPostDocNonDegTuition =new BigDecimal("0"), cumPostDocDegTuition  = new BigDecimal("0"),
                       cumPostDocTotalTuition  = new BigDecimal("0");
            BigDecimal cumOtherTuition  = new BigDecimal("0");

            /********** cumulative costs **/
            BigDecimal cumTrainingCosts = new BigDecimal("0"), cumTravelCosts = new BigDecimal("0"), cumConsCosts = new BigDecimal("0");
            BigDecimal cumResearchTotalDirectCosts = new BigDecimal("0"), cumTotalOtherDirectCosts = new BigDecimal("0"),
                       cumTotalDirectCosts = new BigDecimal("0");
            BigDecimal cumTotalIndCosts1 = new BigDecimal("0"), cumTotalIndCosts2 = new BigDecimal("0"),
                       cumTotalIndCosts = new BigDecimal("0");
            BigDecimal cumTotalDirectAndIndCosts = new BigDecimal("0");

            BigDecimal researchDirectCosts = new BigDecimal("0");
            BigDecimal totalOtherDirectCostsRequested = new BigDecimal("0");
            /********************************
             * get budget periods
             *********************************/
            for (int period=1; period <= numPeriods; period++) {
              hmbudgetinfo = new HashMap();
              hmbudgetinfo = s2SPHS398TrainingBudgTxnBean.getBudgetInfo(propNumber,period);
              
              PHS398TrainingBudgetYearDataType  phs398TrainingBudgetYearDataType = objFactory.createPHS398TrainingBudgetYearDataType();
              phs398TrainingBudgetYearDataType.setTraineeTravelRequested(new BigDecimal(hmbudgetinfo.get("TRAINEE_TRAVEL_COST").toString()));
              phs398TrainingBudgetYearDataType.setTrainingRelatedExpensesRequested(new BigDecimal(hmbudgetinfo.get("TRAINING_RELATED_COST").toString()));


         //     phs398TrainingBudgetYearDataType.setResearchDirectCostsRequested(new BigDecimal(hmbudgetinfo.get("RESEARCH_DIRECT_COST").toString()));
              phs398TrainingBudgetYearDataType.setConsortiumTrainingCostsRequested(new BigDecimal(hmbudgetinfo.get("CONSORTIUM_TRAINING_COST").toString()));

        //      phs398TrainingBudgetYearDataType.setTotalOtherDirectCostsRequested(new BigDecimal(hmbudgetinfo.get("TOTAL_OTHER_DIRECT_COST").toString()));
             //coeusqa-3900 start
//              phs398TrainingBudgetYearDataType.setPeriodEndDate( convertDateStringToCalendar(hmbudgetinfo.get("END_DATE").toString()));
//              phs398TrainingBudgetYearDataType.setPeriodStartDate(convertDateStringToCalendar(hmbudgetinfo.get("START_DATE").toString()));
              phs398TrainingBudgetYearDataType.setPeriodEndDate(getCal(new Date(((Timestamp) hmbudgetinfo.get("END_DATE")).getTime())));
              phs398TrainingBudgetYearDataType.setPeriodStartDate(getCal(new Date(((Timestamp) hmbudgetinfo.get("START_DATE")).getTime())));
              //coeusqa-3900 end
              phs398TrainingBudgetYearDataType.setPostdocNonDegreeTuitionAndFeesRequested(new BigDecimal(hmbudgetinfo.get("TUITION_POSTNONDEG").toString()));
              phs398TrainingBudgetYearDataType.setPostdocDegreeTuitionAndFeesRequested(new BigDecimal(hmbudgetinfo.get("TUITION_POSTDEG").toString()));
              phs398TrainingBudgetYearDataType.setUndergraduateTuitionAndFeesRequested(new BigDecimal(hmbudgetinfo.get("TUITION_UND").toString()));
              phs398TrainingBudgetYearDataType.setPredocDualDegreeTuitionAndFeesRequested(new BigDecimal(hmbudgetinfo.get("TUITION_PREDUAL").toString()));
              phs398TrainingBudgetYearDataType.setPredocSingleDegreeTuitionAndFeesRequested(new BigDecimal(hmbudgetinfo.get("TUITION_PRESING").toString()));
              phs398TrainingBudgetYearDataType.setOtherTuitionAndFeesRequested(new BigDecimal(hmbudgetinfo.get("TUITION_OTHER").toString()));


              /******************************
               * add to cumulative amounts for tuition and costs
               ******************************/
              cumUndergradTuition = cumUndergradTuition.add(phs398TrainingBudgetYearDataType.getUndergraduateTuitionAndFeesRequested());
              cumPreDocSingleTuition = cumPreDocSingleTuition.add(phs398TrainingBudgetYearDataType.getPredocSingleDegreeTuitionAndFeesRequested());
              cumPreDocDualTuition = cumPreDocDualTuition.add(phs398TrainingBudgetYearDataType.getPredocDualDegreeTuitionAndFeesRequested());

              cumPostDocNonDegTuition =cumPostDocNonDegTuition.add( phs398TrainingBudgetYearDataType.getPostdocNonDegreeTuitionAndFeesRequested());
              cumPostDocDegTuition  = cumPostDocDegTuition.add(phs398TrainingBudgetYearDataType.getPostdocDegreeTuitionAndFeesRequested());
              cumPostDocTotalTuition  = cumPostDocNonDegTuition.add(cumPostDocDegTuition);
              cumOtherTuition =cumOtherTuition.add(phs398TrainingBudgetYearDataType.getOtherTuitionAndFeesRequested());
              cumTrainingCosts = cumTrainingCosts.add(phs398TrainingBudgetYearDataType.getTrainingRelatedExpensesRequested());
              cumTravelCosts =  cumTravelCosts.add(phs398TrainingBudgetYearDataType.getTraineeTravelRequested());
              cumConsCosts = cumConsCosts.add(phs398TrainingBudgetYearDataType.getConsortiumTrainingCostsRequested());
  //            cumResearchTotalDirectCosts = cumResearchTotalDirectCosts.add(phs398TrainingBudgetYearDataType.getResearchDirectCostsRequested());
 //             cumTotalOtherDirectCosts = cumTotalOtherDirectCosts.add(phs398TrainingBudgetYearDataType.getTotalOtherDirectCostsRequested());



              /************************
               *  getting first two indirect cost type
               ************************/
              cvIndirectCost = s2SPHS398TrainingBudgTxnBean.getIndirectCosts(propNumber,version,period);
              int numIndCostRates = cvIndirectCost.size();
              BigDecimal totIndCosts = new BigDecimal("0");
              if (numIndCostRates >0) {
                for(int i=0; i< numIndCostRates; i++){
                    if (i == 2) { break;} 
                    hmIndirectCost = (HashMap) cvIndirectCost.get(i);
                    phs398TrainingBudgetYearDataType.setIndirectCostType1(hmIndirectCost.get("DESCRIPTION").toString());
                    if (i == 0){
                      phs398TrainingBudgetYearDataType.setIndirectCostBase1(new BigDecimal(hmIndirectCost.get("BASE_COST").toString()));
                      phs398TrainingBudgetYearDataType.setIndirectCostFundsRequested1(new BigDecimal(hmIndirectCost.get("CALCULATED_COST").toString()));
                      phs398TrainingBudgetYearDataType.setIndirectCostRate1(new BigDecimal(hmIndirectCost.get("APPLIED_RATE").toString()));
                      totIndCosts = totIndCosts.add(phs398TrainingBudgetYearDataType.getIndirectCostFundsRequested1());
                      cumTotalIndCosts1 =  cumTotalIndCosts1.add(phs398TrainingBudgetYearDataType.getIndirectCostFundsRequested1());
                    } else {
                      phs398TrainingBudgetYearDataType.setIndirectCostBase2(new BigDecimal(hmIndirectCost.get("BASE_COST").toString()));
                      phs398TrainingBudgetYearDataType.setIndirectCostFundsRequested2(new BigDecimal(hmIndirectCost.get("CALCULATED_COST").toString()));
                      phs398TrainingBudgetYearDataType.setIndirectCostRate2(new BigDecimal(hmIndirectCost.get("APPLIED_RATE").toString()));
                      totIndCosts = totIndCosts.add(phs398TrainingBudgetYearDataType.getIndirectCostFundsRequested2());
                      cumTotalIndCosts2 =  cumTotalIndCosts2.add(phs398TrainingBudgetYearDataType.getIndirectCostFundsRequested2());
                    }
                }

      //        phs398TrainingBudgetYearDataType.setTotalIndirectCostsRequested(totIndCosts);
              }
                phs398TrainingBudgetYearDataType.setTotalIndirectCostsRequested(totIndCosts);
            /********************************************************
            * get questionnaire answers for undergrads and predocs and others
             ********************************************************/
           CoeusVector cvQuestions = null;
  
           cvQuestions = s2SPHS398TrainingBudgTxnBean.getPreDocAnswers(propNumber,period);
           HashMap hmQuestions = new HashMap();

           int numQuestions = cvQuestions.size();
           int questionId = 0;
           int questionNumber = 0;
           String answer = null;
           int preDocCountFull = 0, preDocCountShort=0;
           int undergradFirstYearNum = 0, undergradJrNum = 0;
           BigDecimal otherShortStipends = new BigDecimal("0"), otherFullStipends = new BigDecimal("0");
           Calendar dateString = null;

           if (numQuestions >0){
            for(int i=0;i<numQuestions;i++){
                hmQuestions = (HashMap) cvQuestions.get(i);
                questionId = Integer.parseInt(hmQuestions.get("QUESTION_ID").toString());
                answer = hmQuestions.get("ANSWER").toString();
                questionNumber = Integer.parseInt(hmQuestions.get("QUESTION_NUMBER").toString());

              switch (questionId) {
                  case 72:
                      //full term undergrad
                      if (answer != null)
                          phs398TrainingBudgetYearDataType.setUndergraduateNumFullTime(Integer.parseInt(answer.toString()));

                      break;
                  case 73:
                        //short term undergrad
                      if (answer != null)
                          phs398TrainingBudgetYearDataType.setUndergraduateNumShortTerm(Integer.parseInt(answer.toString()));
                      break;
                  case 74:
                        //stipends first year
                      if (answer != null)
                          undergradFirstYearNum = undergradFirstYearNum + Integer.parseInt(answer.toString());
                        
                      break;
                  case 75:
                        //stipends junior
                      if (answer != null)
                          undergradJrNum = undergradJrNum + Integer.parseInt(answer.toString());
                         
                      break;
                  case 77:
                       //full time single degree predoc
                       if (answer != null) {
                            phs398TrainingBudgetYearDataType.setPredocSingleDegreeNumFullTime(Integer.parseInt(answer.toString()));
                            preDocCountFull = preDocCountFull + phs398TrainingBudgetYearDataType.getPredocSingleDegreeNumFullTime();
                       }
                            break;
                  case 78:
                       //short term single degree predoc
                       if (answer != null){
                            phs398TrainingBudgetYearDataType.setPredocSingleDegreeNumShortTerm(Integer.parseInt(answer.toString()));
                            preDocCountShort = preDocCountShort + phs398TrainingBudgetYearDataType.getPredocSingleDegreeNumShortTerm();
                       }
                            break;
                  case 79:
                       //full term dual degree predoc
                       if (answer != null){
                            phs398TrainingBudgetYearDataType.setPredocDualDegreeNumFullTime(Integer.parseInt(answer.toString()));
                             preDocCountFull = preDocCountFull + phs398TrainingBudgetYearDataType.getPredocDualDegreeNumFullTime();
                       } break;
                  case 80:
                       //short term dual degree predoc
                       if (answer != null){
                            phs398TrainingBudgetYearDataType.setPredocDualDegreeNumShortTerm(Integer.parseInt(answer.toString()));
                            preDocCountShort = preDocCountShort + phs398TrainingBudgetYearDataType.getPredocDualDegreeNumShortTerm();
                       }break;
                  case 95:
                         //others full term
                        if (answer != null)
                            phs398TrainingBudgetYearDataType.setOtherNumFullTime(Integer.parseInt(answer.toString()));
                        break;
                  case 97:
                      //others short term
                        if (answer != null)
                            phs398TrainingBudgetYearDataType.setOtherNumShortTerm(Integer.parseInt(answer.toString()));
                        break;
                 case 96:
                         //others full term stipend
                        if (answer != null)
                            otherFullStipends = new BigDecimal(answer.toString());
                        break;
                 case 98:
                      //others short term stipend
                        if (answer != null)
                             otherShortStipends =new BigDecimal(answer.toString());
                        break;

                  default:
                        break;

                 }  //switch question id
              }   //for num questions
                phs398TrainingBudgetYearDataType.setOtherStipendsRequested(otherFullStipends.add(otherShortStipends));
                cumOtherStipends = cumOtherStipends.add(phs398TrainingBudgetYearDataType.getOtherStipendsRequested());
           }  //if question

           phs398TrainingBudgetYearDataType.setUndergraduateNumFirstYearSophomoreStipends(undergradFirstYearNum);
           phs398TrainingBudgetYearDataType.setUndergraduateNumJuniorSeniorStipends(undergradJrNum);
           phs398TrainingBudgetYearDataType.setOtherStipendsRequested(otherShortStipends.add(otherFullStipends));
           phs398TrainingBudgetYearDataType.setPredocTotalNumShortTerm(preDocCountShort);
           phs398TrainingBudgetYearDataType.setPredocTotalNumFullTime(preDocCountFull);

          /*******************************************
          * get questionnaire answers for POSTDOCS
          *******************************************/
        
           int numPostDocLevel0 ,numPostDocLevel1,numPostDocLevel2,numPostDocLevel3,numPostDocLevel4 = 0;
           int numPostDocLevel5 ,numPostDocLevel6,numPostDocLevel7 = 0;
           BigDecimal bdTemp = new BigDecimal("0");

           HashMap hmNonDegree = new HashMap();
           HashMap hmDegree = new HashMap();

          
           CoeusVector cvPostDocAnswers = null;
           /*****************************
            *   full time non degree
            * ***************************/
           cvPostDocAnswers =  s2SPHS398TrainingBudgTxnBean.getPostDocAnswers(propNumber,"FN", period);
           HashMap hmPostDocAnswers = new HashMap();
           int numAnswers = cvPostDocAnswers.size();
           questionId = 0;
           questionNumber = 0;

           answer = null;

            hmNonDegree.put("fulllevel0", "0");
            hmNonDegree.put("fulllevel1", "0");
            hmNonDegree.put("fulllevel2", "0");
            hmNonDegree.put("fulllevel3", "0");
            hmNonDegree.put("fulllevel4", "0");
            hmNonDegree.put("fulllevel5", "0");
            hmNonDegree.put("fulllevel6", "0");
            hmNonDegree.put("fulllevel7", "0");
            hmNonDegree.put("shortlevel0", "0");
            hmNonDegree.put("shortlevel1", "0");
            hmNonDegree.put("shortlevel2", "0");
            hmNonDegree.put("shortlevel3", "0");
            hmNonDegree.put("shortlevel4", "0");
            hmNonDegree.put("shortlevel5", "0");
            hmNonDegree.put("shortlevel6", "0");
            hmNonDegree.put("shortlevel7", "0");

           if (numAnswers >0){
            for(int i=0;i<numAnswers;i++){
                hmPostDocAnswers = (HashMap) cvPostDocAnswers.get(i);
                questionId = Integer.parseInt(hmPostDocAnswers.get("QUESTION_ID").toString());
                answer = hmPostDocAnswers.get("ANSWER").toString();
                questionNumber = Integer.parseInt(hmPostDocAnswers.get("QUESTION_NUMBER").toString());

              switch (questionId) {
                  case 86:
                      //trainees at stipend level 0
                      if (answer != null)
                          hmNonDegree.put("fulllevel0", answer);
                         
                      break;
                  case 87:
                      //trainees at stipend level 1
                      if (answer != null)
                           hmNonDegree.put("fulllevel1", answer);

                      break;
                  case 88:
                      //trainees at stipend level 2
                      if (answer != null)
                           hmNonDegree.put("fulllevel2", answer);
                      break;
                  case 89:
                      //trainees at stipend level 3
                      if (answer != null)
                           hmNonDegree.put("fulllevel3", answer);
                      break;
                  case 90:
                      //trainees at stipend level 4
                      if (answer != null)
                           hmNonDegree.put("fulllevel4", answer);
                      break;
                  case 91:
                      //trainees at stipend level 5
                      if (answer != null)
                           hmNonDegree.put("fulllevel5", answer);
                      break;
                  case 92:
                      //trainees at stipend level 6
                      if (answer != null)
                           hmNonDegree.put("fulllevel6", answer);
                      break;
                  case 93:
                      //trainees at stipend level 7
                      if (answer != null)
                           hmNonDegree.put("fulllevel7", answer);
                      break;
                  default:
                        break;

                 }  //switch question id
              }   //for num questions
           }  //if numanswers


           /*****************************
            *   short term non degree
            * ***************************/
           cvPostDocAnswers =  s2SPHS398TrainingBudgTxnBean.getPostDocAnswers(propNumber,"SN", period);
           hmPostDocAnswers = new HashMap();
           numAnswers = cvPostDocAnswers.size();
           questionId = 0;
           questionNumber = 0;

           answer = null;

           if (numAnswers >0){
            for(int i=0;i<numAnswers;i++){
                hmPostDocAnswers = (HashMap) cvPostDocAnswers.get(i);
                questionId = Integer.parseInt(hmPostDocAnswers.get("QUESTION_ID").toString());
                answer = hmPostDocAnswers.get("ANSWER").toString();
                questionNumber = Integer.parseInt(hmPostDocAnswers.get("QUESTION_NUMBER").toString());

              switch (questionId) {
                  case 86:
                      //trainees at stipend level 0
                      if (answer != null)
                          hmNonDegree.put("shortlevel0", answer);
                       break;
                  case 87:
                      //trainees at stipend level 1
                      if (answer != null)
                           hmNonDegree.put("shortlevel1", answer);
                      break;
                  case 88:
                      //trainees at stipend level 2
                      if (answer != null)
                           hmNonDegree.put("shortlevel2", answer);
                       break;
                  case 89:
                      //trainees at stipend level 3
                      if (answer != null)
                           hmNonDegree.put("shortlevel3", answer);
                      break;
                  case 90:
                      //trainees at stipend level 4
                      if (answer != null)
                           hmNonDegree.put("shortlevel4", answer);
                       break;
                  case 91:
                      //trainees at stipend level 5
                      if (answer != null)
                           hmNonDegree.put("shortlevel5", answer);
                      break;
                  case 92:
                      //trainees at stipend level 6
                      if (answer != null)
                           hmNonDegree.put("shortlevel6", answer);
                      break;
                  case 93:
                      //trainees at stipend level 7
                      if (answer != null)
                           hmNonDegree.put("shortlevel7", answer);
                      break;
                  default:
                        break;

                 }  //switch question id
              }   //for num questions
           }  //if numanswers

            /***********************************************************
            * set post doc non degree full time total number
            ***********************************************************/


            int postDocNumNonDegreeFullTime =
                    Integer.parseInt(hmNonDegree.get("fulllevel0").toString()) +
                    Integer.parseInt(hmNonDegree.get("fulllevel1").toString()) +
                    Integer.parseInt(hmNonDegree.get("fulllevel2").toString()) +
                    Integer.parseInt(hmNonDegree.get("fulllevel3").toString()) +
                    Integer.parseInt(hmNonDegree.get("fulllevel4").toString()) +
                    Integer.parseInt(hmNonDegree.get("fulllevel5").toString()) +
                    Integer.parseInt(hmNonDegree.get("fulllevel6").toString()) +
                    Integer.parseInt(hmNonDegree.get("fulllevel7").toString());

            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeFullTime(postDocNumNonDegreeFullTime);

            /***********************************************************
            * set post doc non degree short term total number
            ***********************************************************/

           int postDocNumNonDegreeShortTerm =
               Integer.parseInt(hmNonDegree.get("shortlevel0").toString())+
               Integer.parseInt(hmNonDegree.get("shortlevel1").toString()) +
               Integer.parseInt(hmNonDegree.get("shortlevel2").toString()) +
               Integer.parseInt(hmNonDegree.get("shortlevel3").toString()) +
               Integer.parseInt(hmNonDegree.get("shortlevel4").toString()) +
               Integer.parseInt(hmNonDegree.get("shortlevel5").toString()) +
               Integer.parseInt(hmNonDegree.get("shortlevel6").toString()) +
               Integer.parseInt(hmNonDegree.get("shortlevel7").toString()) ;

            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeShortTerm(postDocNumNonDegreeShortTerm);



           /************************************************
            * set post doc non degree level numbers
           *************************************************/
            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeStipendLevel0(
              Integer.parseInt(hmNonDegree.get("fulllevel0").toString()) +
              Integer.parseInt(hmNonDegree.get("shortlevel0").toString()));
         
            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeStipendLevel1(
              Integer.parseInt(hmNonDegree.get("fulllevel1").toString()) +
              Integer.parseInt(hmNonDegree.get("shortlevel1").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeStipendLevel2(
              Integer.parseInt(hmNonDegree.get("fulllevel2").toString()) +
              Integer.parseInt(hmNonDegree.get("shortlevel2").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeStipendLevel3(
              Integer.parseInt(hmNonDegree.get("fulllevel3").toString()) +
              Integer.parseInt(hmNonDegree.get("shortlevel3").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeStipendLevel4(
              Integer.parseInt(hmNonDegree.get("fulllevel4").toString()) +
              Integer.parseInt(hmNonDegree.get("shortlevel4").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeStipendLevel5(
              Integer.parseInt(hmNonDegree.get("fulllevel5").toString()) +
              Integer.parseInt(hmNonDegree.get("shortlevel5").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeStipendLevel6(
              Integer.parseInt(hmNonDegree.get("fulllevel6").toString()) +
              Integer.parseInt(hmNonDegree.get("shortlevel6").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumNonDegreeStipendLevel7(
              Integer.parseInt(hmNonDegree.get("fulllevel7").toString()) +
              Integer.parseInt(hmNonDegree.get("shortlevel7").toString()));

         


           /*****************************
            *   full time degree seeking
            * ***************************/
           cvPostDocAnswers =  s2SPHS398TrainingBudgTxnBean.getPostDocAnswers(propNumber,"FD",period);
           hmPostDocAnswers = new HashMap();
           numAnswers = cvPostDocAnswers.size();
           questionId = 0;
           questionNumber = 0;

           answer = null;

             hmDegree.put("fulllevel0", "0");
            hmDegree.put("fulllevel1", "0");
            hmDegree.put("fulllevel2", "0");
            hmDegree.put("fulllevel3", "0");
            hmDegree.put("fulllevel4", "0");
            hmDegree.put("fulllevel5", "0");
            hmDegree.put("fulllevel6", "0");
            hmDegree.put("fulllevel7", "0");
            hmDegree.put("shortlevel0", "0");
            hmDegree.put("shortlevel1", "0");
            hmDegree.put("shortlevel2", "0");
            hmDegree.put("shortlevel3", "0");
            hmDegree.put("shortlevel4", "0");
            hmDegree.put("shortlevel5", "0");
            hmDegree.put("shortlevel6", "0");
            hmDegree.put("shortlevel7", "0");

           if (numAnswers >0){
            for(int i=0;i<numAnswers;i++){
                hmPostDocAnswers = (HashMap) cvPostDocAnswers.get(i);
                questionId = Integer.parseInt(hmPostDocAnswers.get("QUESTION_ID").toString());
                answer = hmPostDocAnswers.get("ANSWER").toString();
                questionNumber = Integer.parseInt(hmPostDocAnswers.get("QUESTION_NUMBER").toString());

              switch (questionId) {
                  case 86:
                      //trainees at stipend level 0
                      if (answer != null)
                          hmDegree.put("fulllevel0", answer);

                      break;
                  case 87:
                      //trainees at stipend level 1
                      if (answer != null)
                           hmDegree.put("fulllevel1", answer);

                      break;
                  case 88:
                      //trainees at stipend level 2
                      if (answer != null)
                           hmDegree.put("fulllevel2", answer);
                      break;
                  case 89:
                      //trainees at stipend level 3
                      if (answer != null)
                           hmDegree.put("fulllevel3", answer);
                      break;
                  case 90:
                      //trainees at stipend level 4
                      if (answer != null)
                           hmDegree.put("fulllevel4", answer);
                      break;
                  case 91:
                      //trainees at stipend level 5
                      if (answer != null)
                           hmDegree.put("fulllevel5", answer);
                      break;
                  case 92:
                      //trainees at stipend level 6
                      if (answer != null)
                           hmDegree.put("fulllevel6", answer);
                      break;
                  case 93:
                      //trainees at stipend level 7
                      if (answer != null)
                           hmDegree.put("fulllevel7", answer);
                      break;
                  default:
                        break;

                 }  //switch question id
              }   //for num questions
           }  //if numanswers


           /*****************************
            *   short term  degree seeking
            * ***************************/
           cvPostDocAnswers =  s2SPHS398TrainingBudgTxnBean.getPostDocAnswers(propNumber,"SD",period);
           hmPostDocAnswers = new HashMap();
           numAnswers = cvPostDocAnswers.size();
           questionId = 0;
           questionNumber = 0;

           answer = null;

           if (numAnswers >0){
            for(int i=0;i<numAnswers;i++){
                hmPostDocAnswers = (HashMap) cvPostDocAnswers.get(i);
                questionId = Integer.parseInt(hmPostDocAnswers.get("QUESTION_ID").toString());
                answer = hmPostDocAnswers.get("ANSWER").toString();
                questionNumber = Integer.parseInt(hmPostDocAnswers.get("QUESTION_NUMBER").toString());

              switch (questionId) {
                  case 86:
                      //trainees at stipend level 0
                      if (answer != null)
                          hmDegree.put("shortlevel0", answer);
                       break;
                  case 87:
                      //trainees at stipend level 1
                      if (answer != null)
                           hmDegree.put("shortlevel1", answer);
                      break;
                  case 88:
                      //trainees at stipend level 2
                      if (answer != null)
                           hmDegree.put("shortlevel2", answer);
                       break;
                  case 89:
                      //trainees at stipend level 3
                      if (answer != null)
                           hmDegree.put("shortlevel3", answer);
                      break;
                  case 90:
                      //trainees at stipend level 4
                      if (answer != null)
                           hmDegree.put("shortlevel4", answer);
                       break;
                  case 91:
                      //trainees at stipend level 5
                      if (answer != null)
                           hmDegree.put("shortlevel5", answer);
                      break;
                  case 92:
                      //trainees at stipend level 6
                      if (answer != null)
                           hmDegree.put("shortlevel6", answer);
                      break;
                  case 93:
                      //trainees at stipend level 7
                      if (answer != null)
                           hmDegree.put("shortlevel7", answer);
                      break;
                  default:
                        break;

                 }  //switch question id
              }   //for num questions
           }  //if numanswers

           /******************************************************
            * set post doc degree seeking numbers for each level
            ******************************************************/
            phs398TrainingBudgetYearDataType.setPostdocNumDegreeStipendLevel0(
              Integer.parseInt(hmDegree.get("fulllevel0").toString()) +
              Integer.parseInt(hmDegree.get("shortlevel0").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumDegreeStipendLevel1(
              Integer.parseInt(hmDegree.get("fulllevel1").toString()) +
              Integer.parseInt(hmDegree.get("shortlevel1").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumDegreeStipendLevel2(
              Integer.parseInt(hmDegree.get("fulllevel2").toString()) +
              Integer.parseInt(hmDegree.get("shortlevel2").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumDegreeStipendLevel3(
              Integer.parseInt(hmDegree.get("fulllevel3").toString()) +
              Integer.parseInt(hmDegree.get("shortlevel3").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumDegreeStipendLevel4(
              Integer.parseInt(hmDegree.get("fulllevel4").toString()) +
              Integer.parseInt(hmDegree.get("shortlevel4").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumDegreeStipendLevel5(
              Integer.parseInt(hmDegree.get("fulllevel5").toString()) +
              Integer.parseInt(hmDegree.get("shortlevel5").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumDegreeStipendLevel6(
              Integer.parseInt(hmDegree.get("fulllevel6").toString()) +
              Integer.parseInt(hmDegree.get("shortlevel6").toString()));
            phs398TrainingBudgetYearDataType.setPostdocNumDegreeStipendLevel7(
              Integer.parseInt(hmDegree.get("fulllevel7").toString()) +
              Integer.parseInt(hmDegree.get("shortlevel7").toString()));

            /************************************************
             * set post doc degree seeking full time number
             **********************************************/

            int postDocNumDegreeFulltime =
                    Integer.parseInt(hmDegree.get("fulllevel0").toString()) +
                    Integer.parseInt(hmDegree.get("fulllevel1").toString()) +
                    Integer.parseInt(hmDegree.get("fulllevel2").toString()) +
                    Integer.parseInt(hmDegree.get("fulllevel3").toString()) +
                    Integer.parseInt(hmDegree.get("fulllevel4").toString()) +
                    Integer.parseInt(hmDegree.get("fulllevel5").toString()) +
                    Integer.parseInt(hmDegree.get("fulllevel6").toString()) +
                    Integer.parseInt(hmDegree.get("fulllevel7").toString());

              phs398TrainingBudgetYearDataType.setPostdocNumDegreeFullTime(postDocNumDegreeFulltime);

             /***********************************************
              *set post doc degree seeking short term number
              * ************************************************/

             int postDocNumDegreeShortTerm =
                    Integer.parseInt(hmDegree.get("shortlevel0").toString()) +
                    Integer.parseInt(hmDegree.get("shortlevel1").toString()) +
                    Integer.parseInt(hmDegree.get("shortlevel2").toString()) +
                    Integer.parseInt(hmDegree.get("shortlevel3").toString()) +
                    Integer.parseInt(hmDegree.get("shortlevel4").toString()) +
                    Integer.parseInt(hmDegree.get("shortlevel5").toString()) +
                    Integer.parseInt(hmDegree.get("shortlevel6").toString()) +
                    Integer.parseInt(hmDegree.get("shortlevel7").toString());

             phs398TrainingBudgetYearDataType.setPostdocNumDegreeShortTerm(postDocNumDegreeShortTerm);

             //Total numbers of  post docs
             phs398TrainingBudgetYearDataType.setPostdocTotalShortTerm(
                      phs398TrainingBudgetYearDataType.getPostdocNumDegreeShortTerm() +
                      phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeShortTerm());

             phs398TrainingBudgetYearDataType.setPostdocTotalFullTime(
                      phs398TrainingBudgetYearDataType.getPostdocNumDegreeFullTime() +
                      phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeFullTime());

             //total numbers of post docs for each level
                phs398TrainingBudgetYearDataType.setPostdocTotalStipendLevel0(
                  phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel0() +
                  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel0());

                phs398TrainingBudgetYearDataType.setPostdocTotalStipendLevel1(
                  phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel1() +
                  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel1());
                phs398TrainingBudgetYearDataType.setPostdocTotalStipendLevel2(
                  phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel2() +
                  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel2());
                phs398TrainingBudgetYearDataType.setPostdocTotalStipendLevel3(
                  phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel3() +
                  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel3());
                phs398TrainingBudgetYearDataType.setPostdocTotalStipendLevel4(
                  phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel4() +
                  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel4());
                phs398TrainingBudgetYearDataType.setPostdocTotalStipendLevel5(
                  phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel5() +
                  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel5());
                phs398TrainingBudgetYearDataType.setPostdocTotalStipendLevel6(
                  phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel6() +
                  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel6());
                phs398TrainingBudgetYearDataType.setPostdocTotalStipendLevel7(
                  phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel7() +
                  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel7());


             /******************************************************
              * get stipend amounts
              ******************************************************/

                //undergrad
                numPeople = phs398TrainingBudgetYearDataType.getUndergraduateNumFirstYearSophomoreStipends() ;
                stipendAmountF = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(UNDERGRADS,0,numPeople,propNumber)));
                numPeople =  phs398TrainingBudgetYearDataType.getUndergraduateNumJuniorSeniorStipends();
                stipendAmountJ = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(UNDERGRADS,1,numPeople,propNumber)));
                phs398TrainingBudgetYearDataType.setUndergraduateStipendsRequested(stipendAmountF.add(stipendAmountJ));

                cumUndergradStipends = cumUndergradStipends.add(phs398TrainingBudgetYearDataType.getUndergraduateStipendsRequested());

                //predoc
                numPeople = phs398TrainingBudgetYearDataType.getPredocSingleDegreeNumFullTime();
                stipendAmountPreSingFull = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(PREDOC,0,numPeople,propNumber)));
                numPeople = phs398TrainingBudgetYearDataType.getPredocDualDegreeNumFullTime();
                stipendAmountPreDualFull = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(PREDOC,0,numPeople,propNumber)));


                numPeople = phs398TrainingBudgetYearDataType.getPredocSingleDegreeNumShortTerm();
                stipendAmountPreSingShort = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(PREDOC,0,numPeople,propNumber)));
                numPeople = phs398TrainingBudgetYearDataType.getPredocDualDegreeNumShortTerm();
                stipendAmountPreDualShort = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(PREDOC,0,numPeople,propNumber)));
                
                phs398TrainingBudgetYearDataType.setPredocSingleDegreeStipendsRequested( stipendAmountPreSingFull.add( stipendAmountPreSingShort));
                phs398TrainingBudgetYearDataType.setPredocDualDegreeStipendsRequested(stipendAmountPreDualFull.add( stipendAmountPreDualShort));
                phs398TrainingBudgetYearDataType.setPredocTotalStipendsRequested(stipendAmountPreSingFull.add(stipendAmountPreDualFull.
                           add(stipendAmountPreSingShort).add(stipendAmountPreDualShort)));

                //cumulative amounts
                cumPreDocSingleStipends=cumPreDocSingleStipends.add(stipendAmountPreSingFull).add(stipendAmountPreSingShort);
                cumPreDocDualStipends=cumPreDocDualStipends.add(stipendAmountPreDualFull).add(stipendAmountPreDualShort)  ;
                cumPreDocTotalStipends = cumPreDocSingleStipends.add(cumPreDocDualStipends);
                cumPreDocTotalTuition  = cumPreDocDualTuition.add(cumPreDocSingleTuition);

                //postdoc

                numPostDocLevel0 = phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel0();
                stipendAmountNonDeg0 = new BigDecimal(Integer.toString( s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,0,numPostDocLevel0,propNumber)));
                numPostDocLevel0 = phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel0();
                stipendAmountDeg0 = new BigDecimal(Integer.toString( s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,0,numPostDocLevel0,propNumber)));
                
                numPostDocLevel1 = phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel1() ;
                stipendAmountNonDeg1 = new BigDecimal(Integer.toString( s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,1,numPostDocLevel1,propNumber)));
                numPostDocLevel1 =  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel1();
                stipendAmountDeg1 = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,1,numPostDocLevel1,propNumber)));
               
                numPostDocLevel2 = phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel2() ;
                stipendAmountNonDeg2 = new BigDecimal(Integer.toString( s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,2,numPostDocLevel2,propNumber)));
                numPostDocLevel2 =  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel2();
                stipendAmountDeg2 = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,2,numPostDocLevel2,propNumber)));

                numPostDocLevel3 = phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel3() ;
                stipendAmountNonDeg3 = new BigDecimal(Integer.toString( s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,3,numPostDocLevel3,propNumber)));
                numPostDocLevel3 =  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel3();
                stipendAmountDeg3 = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,3,numPostDocLevel3,propNumber)));

                numPostDocLevel4 = phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel4() ;
                stipendAmountNonDeg4 = new BigDecimal(Integer.toString( s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,4,numPostDocLevel4,propNumber)));
                numPostDocLevel4 =  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel4();
                stipendAmountDeg4 = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,4,numPostDocLevel4,propNumber)));

                numPostDocLevel5 = phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel5() ;
                stipendAmountNonDeg5 = new BigDecimal(Integer.toString( s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,5,numPostDocLevel5,propNumber)));
                numPostDocLevel5 =  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel5();
                stipendAmountDeg5 = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,5,numPostDocLevel5,propNumber)));

                numPostDocLevel6 = phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel6() ;
                stipendAmountNonDeg6 = new BigDecimal(Integer.toString( s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,6,numPostDocLevel6,propNumber)));
                numPostDocLevel6 =  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel6();
                stipendAmountDeg6 = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,6,numPostDocLevel6,propNumber)));

                numPostDocLevel7= phs398TrainingBudgetYearDataType.getPostdocNumNonDegreeStipendLevel7() ;
                stipendAmountNonDeg7 = new BigDecimal(Integer.toString( s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,7,numPostDocLevel7,propNumber)));
                numPostDocLevel7 =  phs398TrainingBudgetYearDataType.getPostdocNumDegreeStipendLevel7();
                stipendAmountDeg7 = new BigDecimal(Integer.toString(s2SPHS398TrainingBudgTxnBean.getStipendAmount(POSTDOC,7,numPostDocLevel7,propNumber)));

                phs398TrainingBudgetYearDataType.setPostdocDegreeStipendsRequested(stipendAmountDeg0.add(stipendAmountDeg1).add(stipendAmountDeg2).add(
                        stipendAmountDeg3).add(stipendAmountDeg4).add(stipendAmountDeg5).add(stipendAmountDeg6).add(stipendAmountDeg7));

                phs398TrainingBudgetYearDataType.setPostdocNonDegreeStipendsRequested(stipendAmountNonDeg0.add(stipendAmountNonDeg1).add(stipendAmountNonDeg2).add(
                        stipendAmountNonDeg3).add(stipendAmountNonDeg4).add(stipendAmountNonDeg5).add(stipendAmountNonDeg6).add(stipendAmountNonDeg7));

                phs398TrainingBudgetYearDataType.setPostdocTotalStipendsRequested(phs398TrainingBudgetYearDataType.getPostdocNonDegreeStipendsRequested()
                            .add( phs398TrainingBudgetYearDataType.getPostdocDegreeStipendsRequested()));


              /******************************************************
              * set total amounts
              ******************************************************/
              
                phs398TrainingBudgetYearDataType.setPostdocTotalTuitionAndFeesRequested(
                        phs398TrainingBudgetYearDataType.getPostdocDegreeTuitionAndFeesRequested().add(
                        phs398TrainingBudgetYearDataType.getPostdocNonDegreeTuitionAndFeesRequested()));
                phs398TrainingBudgetYearDataType.setPredocTotalTuitionAndFeesRequested(
                        phs398TrainingBudgetYearDataType.getPredocDualDegreeTuitionAndFeesRequested().add(
                        phs398TrainingBudgetYearDataType.getPredocSingleDegreeTuitionAndFeesRequested()));
                phs398TrainingBudgetYearDataType.setTotalTuitionAndFeesRequested(
                        phs398TrainingBudgetYearDataType.getPredocTotalTuitionAndFeesRequested().add(
                        phs398TrainingBudgetYearDataType.getPostdocTotalTuitionAndFeesRequested().add(
                        phs398TrainingBudgetYearDataType.getUndergraduateTuitionAndFeesRequested())).add(
                        phs398TrainingBudgetYearDataType.getOtherTuitionAndFeesRequested()));
                phs398TrainingBudgetYearDataType.setTotalStipendsRequested(
                        phs398TrainingBudgetYearDataType.getPostdocTotalStipendsRequested().add(
                        phs398TrainingBudgetYearDataType.getPredocTotalStipendsRequested().add(
                        phs398TrainingBudgetYearDataType.getUndergraduateStipendsRequested())  ).add(
                        phs398TrainingBudgetYearDataType.getOtherStipendsRequested()));
                phs398TrainingBudgetYearDataType.setTotalStipendsAndTuitionFeesRequested(
                         phs398TrainingBudgetYearDataType.getTotalStipendsRequested().add(
                         phs398TrainingBudgetYearDataType.getPostdocTotalTuitionAndFeesRequested().add(
                         phs398TrainingBudgetYearDataType.getPredocTotalTuitionAndFeesRequested().add(
                         phs398TrainingBudgetYearDataType.getUndergraduateTuitionAndFeesRequested().add(
                         phs398TrainingBudgetYearDataType.getOtherTuitionAndFeesRequested())))));

              //the total tdirect costs from r and r budget line, which is RESEARCH_DIRECT_COST, has to have the
              //total stipends and tuition subtracted from it.

              researchDirectCosts =   new BigDecimal(hmbudgetinfo.get("RESEARCH_DIRECT_COST").toString());
              researchDirectCosts = researchDirectCosts.subtract(phs398TrainingBudgetYearDataType.getTotalStipendsAndTuitionFeesRequested());
              phs398TrainingBudgetYearDataType.setResearchDirectCostsRequested(researchDirectCosts);

              totalOtherDirectCostsRequested = new BigDecimal(hmbudgetinfo.get("TOTAL_OTHER_DIRECT_COST").toString());
              totalOtherDirectCostsRequested = totalOtherDirectCostsRequested.subtract(phs398TrainingBudgetYearDataType.getTotalStipendsAndTuitionFeesRequested());
              phs398TrainingBudgetYearDataType.setTotalOtherDirectCostsRequested(totalOtherDirectCostsRequested);

              phs398TrainingBudgetYearDataType.setTotalDirectCostsRequested(phs398TrainingBudgetYearDataType.getTotalOtherDirectCostsRequested().add(
                                            phs398TrainingBudgetYearDataType.getTotalStipendsAndTuitionFeesRequested()) );

              phs398TrainingBudgetYearDataType.setTotalDirectAndIndirectCostsRequested(phs398TrainingBudgetYearDataType.getTotalDirectCostsRequested().add(
                                            phs398TrainingBudgetYearDataType.getTotalIndirectCostsRequested()));


            
                /******************************************************
                * add to cumulative amounts
                ******************************************************/

                cumPostDocNonDegStipends =cumPostDocNonDegStipends.add(phs398TrainingBudgetYearDataType.getPostdocNonDegreeStipendsRequested());
                cumPostDocDegStipends = cumPostDocDegStipends.add(phs398TrainingBudgetYearDataType.getPostdocDegreeStipendsRequested());
                cumPostDocTotalStipends = cumPostDocNonDegStipends.add(cumPostDocDegStipends);

                cumResearchTotalDirectCosts = cumResearchTotalDirectCosts.add(phs398TrainingBudgetYearDataType.getResearchDirectCostsRequested());
                cumTotalOtherDirectCosts = cumTotalOtherDirectCosts.add(phs398TrainingBudgetYearDataType.getTotalOtherDirectCostsRequested());

              trainingBudgetType.getBudgetYear().add(phs398TrainingBudgetYearDataType);
        }
              
            //cumulative amounts
            trainingBudgetType.setCumulativeUndergraduateStipendsRequested(cumUndergradStipends);
            trainingBudgetType.setCumulativeUndergraduateTuitionAndFeesRequested(cumUndergradTuition);
           
            trainingBudgetType.setCumulativeOtherStipendsRequested(cumOtherStipends);
            trainingBudgetType.setCumulativeOtherTuitionAndFeesRequested(cumOtherTuition);
            trainingBudgetType.setCumulativePostdocDegreeStipendsRequested(cumPostDocDegStipends);
            trainingBudgetType.setCumulativePostdocDegreeTuitionAndFeesRequested(cumPostDocDegTuition);
            trainingBudgetType.setCumulativePostdocNonDegreeStipendsRequested(cumPostDocNonDegStipends);
            trainingBudgetType.setCumulativePostdocNonDegreeTuitionAndFeesRequested(cumPostDocNonDegTuition);
            trainingBudgetType.setCumulativePostdocTotalStipendsRequested(cumPostDocTotalStipends);
            trainingBudgetType.setCumulativePostdocTotalTuitionAndFeesRequested(cumPostDocTotalTuition);

            trainingBudgetType.setCumulativePredocDualDegreeStipendsRequested(cumPreDocDualStipends);
            trainingBudgetType.setCumulativePredocDualDegreeTuitionAndFeesRequested(cumPreDocDualTuition);
            trainingBudgetType.setCumulativePredocSingleDegreeStipendsRequested(cumPreDocSingleStipends);
            trainingBudgetType.setCumulativePredocSingleDegreeTuitionAndFeesRequested(cumPreDocSingleTuition);
            trainingBudgetType.setCumulativePredocTotalStipendsRequested(cumPreDocTotalStipends);
            trainingBudgetType.setCumulativePredocTotalTuitionAndFeesRequested(cumPreDocTotalTuition);

            trainingBudgetType.setCumulativeTotalStipendsRequested(cumPostDocTotalStipends.add(cumPreDocTotalStipends).add(
                    cumOtherStipends).add(cumUndergradStipends));

            trainingBudgetType.setCumulativeTuitionAndFeesRequested(cumPostDocTotalTuition.add(cumPreDocTotalTuition).add(
                    cumOtherTuition).add(cumUndergradTuition));
            trainingBudgetType.setCumulativeTotalStipendsAndTuitionFeesRequested( trainingBudgetType.getCumulativeTotalStipendsRequested().add(
                       trainingBudgetType.getCumulativeTuitionAndFeesRequested()));


        
            trainingBudgetType.setCumulativeConsortiumTrainingCostsRequested(cumConsCosts);
            trainingBudgetType.setCumulativeResearchDirectCostsRequested(cumResearchTotalDirectCosts);
          
            trainingBudgetType.setCumulativeTotalDirectCostsRequested(trainingBudgetType.getCumulativeTotalStipendsAndTuitionFeesRequested().add(
                                 cumTotalOtherDirectCosts ));
            trainingBudgetType.setCumulativeTotalIndirectCostsRequested(cumTotalIndCosts1.add(cumTotalIndCosts2));
            trainingBudgetType.setCumulativeTotalOtherDirectCostsRequested(cumTotalOtherDirectCosts);
             trainingBudgetType.setCumulativeTotalDirectAndIndirectCostsRequested(trainingBudgetType.getCumulativeTotalDirectCostsRequested().add(cumTotalIndCosts1.add(cumTotalIndCosts2)));
            trainingBudgetType.setCumulativeTraineeTravelRequested(cumTravelCosts);
            trainingBudgetType.setCumulativeTrainingRelatedExpensesRequested(cumTrainingCosts);
          


            /**
            * Attachment
            */


            int narrativeType;
            int moduleNum;
            ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
            ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;

            Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);

           s2sTxnBean = new S2STxnBean();
           LinkedHashMap hmArg = new LinkedHashMap();

           HashMap hmNarrative = new HashMap();
           String description= null;
           GetNarrativeDocumentBean narrativeDocBean;
            Attachment attachment = null;
           String contentId;
           String contentType;

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

               if (narrativeType == 130){
                   //Budget Justification
                   if (attachment == null) {
                     //attachment does not already exist - we need to get it and add it
                     proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     Attachment budgJustAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                      if (budgJustAttachment.getContent() != null){

                        attachedFileType = getAttachedFileType(budgJustAttachment);
                        trainingBudgetType.setBudgetJustification(attachedFileType);
                      }
                   }
               }
           }



         
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398TrainingBudgStream","getPHS398TrainingBudget()");
            throw new CoeusXMLException(jaxbEx.getMessage());
     
        }
        
        return trainingBudgetType;
    }



   /*****************************************************
       get simple Info
    *****************************************************/
     public HashMap getSimpleInfo() 
        throws CoeusXMLException, CoeusException, DBException, JAXBException{
            
       HashMap hm2Info = new HashMap();
       hm2Info = s2SPHS398TrainingBudgTxnBean.getTrainingBudgInfo(propNumber);
 
       return hm2Info;
     }
     

    

      
private gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachedFileType(Attachment attachment) 
     throws JAXBException {
    
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
    gov.grants.apply.system.global_v1.HashValueType hashValueType;

    attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
    fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
    hashValueType = globalObjFactory.createHashValueType();
    
    fileLocation.setHref(attachment.getContentId());
    attachedFileType.setFileLocation(fileLocation);
    attachedFileType.setFileName(AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
    attachedFileType.setMimeType("application/octet-stream");
    try{
        attachedFileType.setHashValue(S2SHashValue.getValue(attachment.getContent()));
    }catch(Exception ex){
        ex.printStackTrace();
        UtilFactory.log(ex.getMessage(),ex, "PHSModularBudgetStream", "getAttachedFile");
        throw new JAXBException(ex);
    }

    return attachedFileType;
           
}

     
   //coeusqa-3900 start
//    public Calendar convertDateStringToCalendar(String dateStr)
//    {
//        try {
//            java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
//
//        DateUtils dtUtils = new DateUtils();
//        if (dateStr != null)
//        {
//            if (dateStr.indexOf('-')!= -1)
//            { // if the format obtd is YYYY-MM-DD
//              dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
//            }
//            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
//                        Integer.parseInt(dateStr.substring(0,2)) - 1,
//                        Integer.parseInt(dateStr.substring(3,5))) ;
//
//            return calDate ;
//        }
//        }catch(Exception ex){
//
//        }
//        return null ;
//     }

    //coeusqa-3900 end
    

 
        
    private static BigDecimal convDoubleToBigDec(double d){
        return new BigDecimal(d);
    }
        //coeusqa-3900 start
//    private Calendar getCal(Date date){
//        if(date==null)
//            return null;
//        String localTimeZone;
//         try {
//             localTimeZone = CoeusProperties.getProperty(CoeusPropertyKeys.LOCAL_TIMEZONE_ID,"America/New_York");
//         } catch (IOException ex) {
//             localTimeZone = "America/New_York";
//         }
//      Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(localTimeZone));
//      cal.setTime(date);
//      return cal;
//    }
    private Calendar getCal(Date date){
        return DateUtils.getCal(date);
    }
    
//    private Calendar getTodayDate() {
//      Calendar cal = Calendar.getInstance(TimeZone.getDefault());
//      java.util.Date today = cal.getTime();
//      cal.setTime(today);
//      return cal;
//    }
    //coeusqa-3900 end
 
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398TrainingBudget();
    }    
     
 }
    

