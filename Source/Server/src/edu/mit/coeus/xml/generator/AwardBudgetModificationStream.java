/*
 * AwardBudgetModificationStream.java
 *
 * Created on January 6, 2005, 3:08 PM
 ** Copyright (c) Massachusetts Institute of Technology
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


/**
 *
 * @author  jenlu
 */
public class AwardBudgetModificationStream extends ReportBaseStream{
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private String mitAwardNumber;
    private String transactionId;
    private AwardTxnBean awardTxnBean;
    private CoeusVector cvAwardAmount;
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.award";
    
    /** Creates a new instance of AwardBudgetModificationStream */
    public AwardBudgetModificationStream() {
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
    }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        String transactionId = (String)params.get("TRANSACTION_ID");
        AwardTxnBean awardTxnBean = new AwardTxnBean();
        if(awardTxnBean.isTranscationIdWithAwardAmount(transactionId)) {
            this.mitAwardNumber = (String)params.get("MIT_AWARD_NUMBER");
            this.transactionId = (String)params.get("TRANSACTION_ID");
            awardTxnBean = new AwardTxnBean();
            cvAwardAmount = awardTxnBean.getAwardAmountInfoTransactionDetails(transactionId, mitAwardNumber);
            AwardNoticeType awardNoticeType = getBudgetModification();
            return awardNoticeType;
        }else {
            throw new CoeusException("NoAwardAmountInfo");
        }
    }
    
     public org.w3c.dom.Document getStream(Hashtable params) throws DBException,CoeusException {
           AwardNoticeType awardNoticeType = (AwardNoticeType)getObjectStream(params);
        return xmlGenerator.marshelObject(awardNoticeType,packageName);
    }
    private AwardNoticeType getBudgetModification() throws CoeusXMLException,CoeusException,DBException{
        AwardNoticeType awardNotice = null;
        AwardType awardType = null;
        try{
            awardNotice = objFactory.createAwardNotice();                      
            awardNotice.setAward(getAwardType());
            awardNotice.setSchoolInfo(getSchoolInfoType());
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            AwardNoticeType.PrintRequirementType printRequirementType = objFactory.createAwardNoticeTypePrintRequirementType();
            awardNotice.setPrintRequirement(printRequirementType);
            awardNotice.getPrintRequirement().setCurrentDate(currentDate);
        }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"AwardBudgetHierarchyStream","getStream()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return awardNotice;
        

    }
    private AwardType getAwardType() throws JAXBException,
                                        CoeusException,DBException{
        AwardType awardType = objFactory.createAwardType();
        AwardType.AwardAmountInfoType awardTypeAwardAmountInfoType = objFactory.createAwardTypeAwardAmountInfoType();
        awardTypeAwardAmountInfoType.getAmountInfo().addAll(getAmountInfo());
        awardType.setAwardAmountInfo(awardTypeAwardAmountInfoType);     
         // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - Start
        AwardType.AwardTransactionInfoType awardTransactionInfoType = objFactory.createAwardTypeAwardTransactionInfoType();
        awardTransactionInfoType.getTransactionInfo().addAll(getTrasactionInfo());  
        awardType.setAwardTransactionInfo(awardTransactionInfoType);
         // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - End     
      return awardType;      
        
    }
    
    // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - Start
    private CoeusVector getTrasactionInfo()throws JAXBException,
            CoeusException,DBException{
        awardTxnBean = new AwardTxnBean();
        CoeusVector cvTransactionForXml = new CoeusVector();
        CoeusVector cvTransactionForXml2 = new CoeusVector();
        CoeusVector cvTempTransactionForXml = new CoeusVector();
        AwardAmountTransactionBean awardAmountTransactionBean = null;
        if(cvAwardAmount != null){
            for(int i=0; i<cvAwardAmount.size();i++){
                AwardAmountInfoBean  awardAmountInfoBean = (AwardAmountInfoBean)cvAwardAmount.get(i);
                if(awardAmountInfoBean.getTransactionId()!=null){
                    awardAmountTransactionBean = awardTxnBean.getAwardAmountTransaction(
                            awardAmountInfoBean.getMitAwardNumber(),awardAmountInfoBean.getTransactionId());
                    AwardTransactionType awardTransactionType = objFactory.createAwardTransactionType();
                    awardTransactionType.setAwardNumber(awardAmountTransactionBean.getMitAwardNumber());
                    awardTransactionType.setTransactionTypeCode(awardAmountTransactionBean.getTransactionTypeCode());
                    awardTransactionType.setTransactionTypeDesc(awardAmountTransactionBean.getTransactionTypeDescription());
                    awardTransactionType.setComments(awardAmountTransactionBean.getComments());
                    awardTransactionType.setNoticeDate(getCal(awardAmountTransactionBean.getNoticeDate()));
                    cvTransactionForXml.add(awardTransactionType);
                    break;
                }
            }
        }             
        return cvTransactionForXml;
    }        
     // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - End
    
              
    
    private CoeusVector getAmountInfo()throws JAXBException,
                                        CoeusException,DBException{
        CoeusVector cvAmountForXml = new CoeusVector();                           
        int amountInfoSize = cvAwardAmount == null?0:cvAwardAmount.size();
        String mitAwardNum ;
        int seqNum, amtSeqNum;
        AwardDeltaReportTxnBean awardDeltaReportTxnBean = new AwardDeltaReportTxnBean("userid");// this constructor will create a new dbEngine, we do need useid here.
        
        for (int index =0; index < amountInfoSize; index++){
            AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvAwardAmount.get(index);
            if (awardAmountInfoBean.getSequenceNumber() > 0 ){            
                AmountInfoType amountInfoType = objFactory.createAmountInfoType();
                if (index == 0 ){
                    amountInfoType.setTransactionDate(getCal(awardAmountInfoBean.getUpdateTimestamp()));
                }
                mitAwardNum = awardAmountInfoBean.getMitAwardNumber();
                seqNum = awardAmountInfoBean.getSequenceNumber();
                amtSeqNum = awardAmountInfoBean.getAmountSequenceNumber();
                amountInfoType.setAwardNumber(mitAwardNum);
                amountInfoType.setAccountNumber(awardAmountInfoBean.getAccountNumber());
                amountInfoType.setSequenceNumber(seqNum);            
                amountInfoType.setAmountSequenceNumber(amtSeqNum);
                amountInfoType.setObligatedChange(convDoubleToBigDec(awardAmountInfoBean.getObligatedChange()));
                amountInfoType.setAnticipatedChange(convDoubleToBigDec(awardAmountInfoBean.getAnticipatedChange()));
                amountInfoType.setAmtObligatedToDate(convDoubleToBigDec(awardAmountInfoBean.getAmountObligatedToDate()));
                amountInfoType.setObligatedDistributableAmt(convDoubleToBigDec(awardAmountInfoBean.getObliDistributableAmount()));
                amountInfoType.setAnticipatedTotalAmt(convDoubleToBigDec(awardAmountInfoBean.getAnticipatedTotalAmount()));
                amountInfoType.setAnticipatedDistributableAmt(convDoubleToBigDec(awardAmountInfoBean.getAnticipatedDistributableAmount()));
                amountInfoType.setObligationExpirationDate(getCal(awardAmountInfoBean.getObligationExpirationDate()));
                amountInfoType.setCurrentFundEffectiveDate(getCal(awardAmountInfoBean.getCurrentFundEffectiveDate()));
                amountInfoType.setFinalExpirationDate(getCal(awardAmountInfoBean.getFinalExpirationDate()));

                if (seqNum > 1 || amtSeqNum > 1){
                    CoeusVector cvPrevAmount = new CoeusVector();
                    int prevMaxAmtSeq;
                    if (amtSeqNum > 1){
                       cvPrevAmount =  awardDeltaReportTxnBean.getMoneyAndEndDatesForSeq(mitAwardNum,""+seqNum,""+(amtSeqNum-1), -1);
                    }else{
                        prevMaxAmtSeq = awardDeltaReportTxnBean.getMaxAmountSeq(mitAwardNum, ""+(seqNum-1));
                        cvPrevAmount =  awardDeltaReportTxnBean.getMoneyAndEndDatesForSeq(mitAwardNum,""+(seqNum-1),""+prevMaxAmtSeq, -1);
                    }
                    
                    if (cvPrevAmount != null && cvPrevAmount.size() > 0 ){
                        AwardAmountInfoBean prevAmountInfoBean = (AwardAmountInfoBean)cvPrevAmount.get(0);
                        if (prevAmountInfoBean.getAmountObligatedToDate() != awardAmountInfoBean.getAmountObligatedToDate()){
                            amountInfoType.setAmtObligatedToDateModified("1");
                        } 
                        if (prevAmountInfoBean.getObliDistributableAmount() != awardAmountInfoBean.getObliDistributableAmount()){
                            amountInfoType.setObligatedDistributableAmtModified("1");
                        } 
                        if (prevAmountInfoBean.getAnticipatedTotalAmount() != awardAmountInfoBean.getAnticipatedTotalAmount()){
                            amountInfoType.setAnticipatedTotalAmtModified("1");
                        } 
                        if (prevAmountInfoBean.getAnticipatedDistributableAmount() != awardAmountInfoBean.getAnticipatedDistributableAmount()){
                            amountInfoType.setAnticipatedDistributableAmtModified("1");
                        } 
                        if (!prevAmountInfoBean.getObligationExpirationDate().equals( awardAmountInfoBean.getObligationExpirationDate())){
                            amountInfoType.setObligationExpDateModified("1");
                        } 
                        if (!prevAmountInfoBean.getCurrentFundEffectiveDate().equals( awardAmountInfoBean.getCurrentFundEffectiveDate())){
                            amountInfoType.setCurrentFundEffectiveDateModified("1");
                        } 
                        if (!prevAmountInfoBean.getFinalExpirationDate().equals( awardAmountInfoBean.getFinalExpirationDate())){
                            amountInfoType.setFinalExpDateModified("1");
                        }                        
                                                
                    }
                }
    //            amountInfoType.setTreeLevel(awardAmountInfoBean.getChildCount());//just use childCount field to set level num
    //            amountInfoType.set
                cvAmountForXml.add(amountInfoType);
            }
        }
        
        return cvAmountForXml;   
    }
    
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
            UtilFactory.log(ex.getMessage(),ex,"AwardBudgetModificationStream","getSchoolInfoType()");
            throw new CoeusException(ex.getMessage());
        }
    }
}
