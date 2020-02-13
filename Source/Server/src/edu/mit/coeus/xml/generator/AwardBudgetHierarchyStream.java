/*
 * @(#)AwardBudgetHierarchyStream.java December 17, 2004, 10:13 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
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
 * Created on December 17, 2004, 10:13 AM
 */
public class AwardBudgetHierarchyStream extends ReportBaseStream{
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private String mitAwardNumber;
    private AwardTxnBean awardTxnBean;
    private CoeusVector cvAwardAmount;
    private CoeusVector cvAmountTree = new CoeusVector();
    private CoeusVector cvTransactionData = new CoeusVector();
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.award";
    private String directIndirectParamValue = null;
    /** Creates a new instance of AwardBudgetHierarchyStream */
    public AwardBudgetHierarchyStream() {
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
    }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        this.mitAwardNumber = (String)params.get("MIT_AWARD_NUMBER");
        awardTxnBean = new AwardTxnBean();        
	cvAwardAmount = awardTxnBean.getMoneyAndEndDatesTree(mitAwardNumber);
        //Added for Case 4156 - direct/indirect amounts don't appear on NOA -Start
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        directIndirectParamValue = coeusFunctions.getParameterValue("ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST");
        //Added for Case 4156 - direct/indirect amounts don't appear on NOA -End
        buildTreeLevel("000000-000",1);
        AwardNoticeType awardNoticeType = getBudgetHierarchy();        
        return awardNoticeType;
    }
    
    public org.w3c.dom.Document getStream(Hashtable params) throws DBException, CoeusException {
        AwardNoticeType awardNoticeType = (AwardNoticeType)getObjectStream(params);       
        return xmlGenerator.marshelObject(awardNoticeType,packageName);
    }
    
     private AwardNoticeType getBudgetHierarchy() throws CoeusXMLException,CoeusException,DBException{
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
        CoeusVector cvTransactionForXml = new CoeusVector();   
        CoeusVector cvTransactionForXml2 = new CoeusVector();     
        CoeusVector cvTempTransactionForXml = new CoeusVector();                           
        int amountInfoSize = cvAmountTree == null?0:cvAmountTree.size();
        for (int index =0; index < amountInfoSize; index++){
            AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvAmountTree.get(index);
            AwardAmountTransactionBean awardAmountTransactionBean = awardAmountInfoBean.getAwardAmountTransaction();
            AwardTransactionType awardTransactionType = objFactory.createAwardTransactionType();
            awardTransactionType.setAwardNumber(awardAmountTransactionBean.getMitAwardNumber());
            awardTransactionType.setTransactionTypeCode(awardAmountTransactionBean.getTransactionTypeCode());
            awardTransactionType.setTransactionTypeDesc(awardAmountTransactionBean.getTransactionTypeDescription());
            awardTransactionType.setComments(awardAmountTransactionBean.getComments());
            awardTransactionType.setNoticeDate(getCal(awardAmountTransactionBean.getNoticeDate()));
            cvTransactionForXml.add(awardTransactionType);
        }       

            return cvTransactionForXml;         
        }
        
     // Added for Case 2269 -Money & End Dates Tab/Panel in Awards Module - End
    
    private CoeusVector getAmountInfo()throws JAXBException,
                                        CoeusException,DBException{
        CoeusVector cvAmountForXml = new CoeusVector();                           
        int amountInfoSize = cvAmountTree == null?0:cvAmountTree.size();
        for (int index =0; index < amountInfoSize; index++){
            AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvAmountTree.get(index);
            AmountInfoType amountInfoType = objFactory.createAmountInfoType();
            amountInfoType.setAwardNumber(awardAmountInfoBean.getMitAwardNumber());
            amountInfoType.setAccountNumber(awardAmountInfoBean.getAccountNumber());
            amountInfoType.setAmtObligatedToDate(convDoubleToBigDec(awardAmountInfoBean.getAmountObligatedToDate()));
            amountInfoType.setObligatedDistributableAmt(convDoubleToBigDec(awardAmountInfoBean.getObliDistributableAmount()));
            amountInfoType.setAnticipatedTotalAmt(convDoubleToBigDec(awardAmountInfoBean.getAnticipatedTotalAmount()));
            amountInfoType.setAnticipatedDistributableAmt(convDoubleToBigDec(awardAmountInfoBean.getAnticipatedDistributableAmount()));
            amountInfoType.setObligationExpirationDate(getCal(awardAmountInfoBean.getObligationExpirationDate()));
            amountInfoType.setCurrentFundEffectiveDate(getCal(awardAmountInfoBean.getCurrentFundEffectiveDate()));
            amountInfoType.setFinalExpirationDate(getCal(awardAmountInfoBean.getFinalExpirationDate()));
             //Added for Case 4156 - direct/indirect amounts don't appear on NOA -Start
            amountInfoType.setObligatedTotalDirect(convDoubleToBigDec(awardAmountInfoBean.getDirectObligatedTotal()));
            amountInfoType.setObligatedTotalIndirect(convDoubleToBigDec(awardAmountInfoBean.getIndirectObligatedTotal()));
            amountInfoType.setAnticipatedTotalDirect(convDoubleToBigDec(awardAmountInfoBean.getDirectAnticipatedTotal()));
            amountInfoType.setAnticipatedTotalIndirect(convDoubleToBigDec(awardAmountInfoBean.getIndirectAnticipatedTotal()));
            amountInfoType.setEnableAwdAntOblDirectIndirectCost(directIndirectParamValue);
            //Added for Case 4156 - direct/indirect amounts don't appear on NOA -End

            amountInfoType.setTreeLevel(awardAmountInfoBean.getChildCount());//just use childCount field to set level num
            cvAmountForXml.add(amountInfoType);
        }
        
        return cvAmountForXml;   
    }
    
    private void buildTreeLevel(String awardNum, int level){
        Equals eqNode = new Equals("parentMitAwardNumber", awardNum);
        NotEquals notEqNode = new NotEquals("parentMitAwardNumber", awardNum);
        CoeusVector cvAmountToBeBuild = cvAwardAmount.filter(eqNode);
        cvAwardAmount = cvAwardAmount.filter(notEqNode);
        int size = cvAmountToBeBuild==null?0:cvAmountToBeBuild.size();
        for (int index = 0; index < size; index++)
        {
            AwardAmountInfoBean root = (AwardAmountInfoBean)cvAmountToBeBuild.filter(eqNode).get(index);//should always return a vector with 1 item 
            //use exist childCount field to keep tree level number.
            root.setChildCount(level);
            cvAmountTree.add(root);
            buildTreeLevel(root.getMitAwardNumber(), level+1);
        }
    }

     private static BigDecimal convDoubleToBigDec(double d){
//        return new BigDecimal(d);
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
            UtilFactory.log(ex.getMessage(),ex,"AwardBudgetHierarchyStream","getSchoolInfoType()");
            throw new CoeusException(ex.getMessage());
        }
    }
}
