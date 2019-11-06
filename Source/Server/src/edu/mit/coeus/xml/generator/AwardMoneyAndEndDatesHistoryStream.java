/*
 * AwardMoneyAndEndDatesHistoryStream.java
 *
 * Created on December 29, 2004, 3:17 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.award.*;
import edu.mit.coeus.award.bean.*;
import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import java.util.*;
//import java.util.Calendar;
//import java.sql.Date;
//import java.util.Hashtable;
//import java.util.TimeZone;
//import java.util.Vector;


/**
 *
 * @author  jenlu
 */
public class AwardMoneyAndEndDatesHistoryStream extends ReportBaseStream{
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;  
    private String mitAwardNumber;
    private AwardTxnBean awardTxnBean;
    private CoeusVector cvAwardAmount;
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.award";
    private String directIndirectParamValue = null;
    /** Creates a new instance of AwardMoneyAndEndDatesHistoryStream */
    public AwardMoneyAndEndDatesHistoryStream() {        
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
    }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        this.mitAwardNumber = (String)params.get("MIT_AWARD_NUMBER");
        awardTxnBean = new AwardTxnBean();
         //Added for Case 4156 - direct/indirect amounts don't appear on NOA -Start
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        directIndirectParamValue = coeusFunctions.getParameterValue("ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST");
        //Added for Case 4156 - direct/indirect amounts don't appear on NOA -End
        cvAwardAmount = awardTxnBean.getAwardAmountHistory(mitAwardNumber);
        AwardNoticeType awardNoticeType = getMoneyAndEndDatesHistory();
        return awardNoticeType;
    }
    /*
    public org.w3c.dom.Document getStream(String mitAwardNum) throws Exception {
        
        
        return xmlGenerator.marshelObject(awardNoticeType,packageName);
    }*/
    
    private AwardNoticeType getMoneyAndEndDatesHistory() throws CoeusXMLException,CoeusException,DBException{
        AwardNoticeType awardNotice = null;
        AwardType awardType = null;
        try{
            awardNotice = objFactory.createAwardNotice();
            awardNotice.getMoneyHistoryReport().addAll(getMoneyHistoryReportTypes());
            awardNotice.setSchoolInfo(getSchoolInfoType());
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            AwardNoticeType.PrintRequirementType printRequirementType = objFactory.createAwardNoticeTypePrintRequirementType();
            awardNotice.setPrintRequirement(printRequirementType);
            awardNotice.getPrintRequirement().setCurrentDate(currentDate);
            awardType = objFactory.createAwardType();
            AwardHeaderType awardHeaderType = objFactory.createAwardHeaderType();
            AwardType.AwardDetailsType awardDetailsType = objFactory.createAwardTypeAwardDetailsType();
            awardHeaderType.setAwardNumber(mitAwardNumber);
            awardDetailsType.setAwardHeader(awardHeaderType);
            awardType.setAwardDetails(awardDetailsType);
            awardNotice.setAward(awardType);
//            awardNotice.set
        }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"AwardBudgetHierarchyStream","getStream()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return awardNotice;
        

    }
    
     private CoeusVector getMoneyHistoryReportTypes() throws JAXBException{
        CoeusVector cvMoneyHistoryReport = new CoeusVector();
        CoeusVector cvAmountInfo = null;
        // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - Start
        CoeusVector cvTransactionInfo = null;
        // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - End
        MoneyHistoryReportType moneyHistoryReportType = null;
        
        int historySize = cvAwardAmount==null?0:cvAwardAmount.size();
        int seqNo = -1;
        int rowNo = 0;;
        
        for(int historyIndex = 0;historyIndex<historySize; historyIndex++){
            AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvAwardAmount.get(historyIndex);
            if( seqNo != awardAmountInfoBean.getSequenceNumber()){//new seq row
                if (seqNo != -1 ){
                    moneyHistoryReportType.getMoneyHistoryInfo().addAll(cvAmountInfo);
                    // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - Start
                    moneyHistoryReportType.getMoneyHistoryTransactionInfo().addAll(cvTransactionInfo); 
                    // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - End
                    cvMoneyHistoryReport.add(moneyHistoryReportType);
                }
                moneyHistoryReportType = objFactory.createMoneyHistoryReportType(); 
                seqNo = awardAmountInfoBean.getSequenceNumber();
                rowNo = 1;
                cvAmountInfo = new CoeusVector();
                // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - Start
                cvTransactionInfo = new CoeusVector();
                // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - End
                moneyHistoryReportType.setMoneyHistorySeq(seqNo);
                
            }else{//same seq row
                rowNo++;
                
            }
            cvAmountInfo.add(setAmountInfo(awardAmountInfoBean, rowNo));
            // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - Start
            cvTransactionInfo.add(setTransactionInfo(awardAmountInfoBean, rowNo)); 
            // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - End
        }
        //case 1696 begin
        //for last sequece info
         moneyHistoryReportType.getMoneyHistoryInfo().addAll(cvAmountInfo);
         // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - Start
        moneyHistoryReportType.getMoneyHistoryTransactionInfo().addAll(cvTransactionInfo);
        // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - End
         cvMoneyHistoryReport.add(moneyHistoryReportType);
        //case 1696 end 
        
        return cvMoneyHistoryReport;
    }
     
     private AmountInfoType setAmountInfo(AwardAmountInfoBean awardAmountInfoBean, int rowNo)throws JAXBException{
     	AmountInfoType amountInfoType = objFactory.createAmountInfoType();
//        amountInfoType.setAwardNumber(awardAmountInfoBean.getMitAwardNumber());
//        amountInfoType.setAccountNumber(awardAmountInfoBean.getAccountNumber());
        amountInfoType.setObligatedChange(convDoubleToBigDec(awardAmountInfoBean.getObligatedChange()));
        amountInfoType.setAnticipatedChange(convDoubleToBigDec(awardAmountInfoBean.getAnticipatedChange()));
        amountInfoType.setAmtObligatedToDate(convDoubleToBigDec(awardAmountInfoBean.getAmountObligatedToDate()));
        amountInfoType.setObligatedDistributableAmt(convDoubleToBigDec(awardAmountInfoBean.getObliDistributableAmount()));
        amountInfoType.setAnticipatedTotalAmt(convDoubleToBigDec(awardAmountInfoBean.getAnticipatedTotalAmount()));
        amountInfoType.setAnticipatedDistributableAmt(convDoubleToBigDec(awardAmountInfoBean.getAnticipatedDistributableAmount()));
        amountInfoType.setObligationExpirationDate(getCal(awardAmountInfoBean.getObligationExpirationDate()));
        amountInfoType.setCurrentFundEffectiveDate(getCal(awardAmountInfoBean.getCurrentFundEffectiveDate()));
        amountInfoType.setFinalExpirationDate(getCal(awardAmountInfoBean.getFinalExpirationDate()));
        
        //Added for Case 4156 - direct/indirect amounts don't appear on NOA -Start
        amountInfoType.setObligatedChangeDirect(convDoubleToBigDec(awardAmountInfoBean.getDirectObligatedChange()));
        amountInfoType.setObligatedChangeIndirect(convDoubleToBigDec(awardAmountInfoBean.getIndirectObligatedChange()));
        amountInfoType.setAnticipatedChangeDirect(convDoubleToBigDec(awardAmountInfoBean.getDirectAnticipatedChange()));
        amountInfoType.setAnticipatedChangeIndirect(convDoubleToBigDec(awardAmountInfoBean.getIndirectAnticipatedChange()));
        amountInfoType.setEnableAwdAntOblDirectIndirectCost(directIndirectParamValue);
        //Added for Case 4156 - direct/indirect amounts don't appear on NOA -End

        amountInfoType.setTreeLevel(rowNo);//use TreeLevel to set history row num in one seq.
        
        return amountInfoType;
    }
      // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - Start 
    private AwardTransactionType setTransactionInfo(AwardAmountInfoBean awardAmountInfoBean, int rowNo)throws JAXBException{
            AwardAmountTransactionBean awardAmountTransactionBean = awardAmountInfoBean.getAwardAmountTransaction();
            AwardTransactionType awardTransactionType = objFactory.createAwardTransactionType();
            awardTransactionType.setAwardNumber(awardAmountTransactionBean.getMitAwardNumber());
            awardTransactionType.setTransactionTypeCode(awardAmountTransactionBean.getTransactionTypeCode());
            awardTransactionType.setTransactionTypeDesc(awardAmountTransactionBean.getTransactionTypeDescription());
            awardTransactionType.setComments(awardAmountTransactionBean.getComments());
            awardTransactionType.setNoticeDate(getCal(awardAmountTransactionBean.getNoticeDate()));
            awardTransactionType.setTreeLevel(rowNo);//use TreeLevel to set history row num in one seq.
            return awardTransactionType;         
        }
        
     // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - End
     
      private static BigDecimal convDoubleToBigDec(double d){
        return (new BigDecimal(d)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
    }
      private Calendar getCal(Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
      
      private SchoolInfoType getSchoolInfoType() throws JAXBException,
                                        CoeusException,DBException{
        try{
        SchoolInfoType schoolInfoType = objFactory.createSchoolInfoType();
        String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
        String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
        // SchoolInfoType
        schoolInfoType.setSchoolName(schoolName);
        schoolInfoType.setAcronym(schoolAcronym);
        return schoolInfoType;
        }catch (Exception ex) {
            UtilFactory.log(ex.getMessage(),ex,"AwardMoneyAndEndDatesHistoryStream","getSchoolInfoType()");
            throw new CoeusException(ex.getMessage());
        }
    }
}
