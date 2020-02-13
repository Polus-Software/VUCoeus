/*
 * @(#)KeyPersonStream_V1_1.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator.stream;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.s2s.Attachment;

import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.generator.AttachmentValidator;
import edu.mit.coeus.s2s.generator.ContentIdConstants;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
import edu.mit.coeus.s2s.generator.stream.bean.ExAttQueryParams;
import edu.mit.coeus.s2s.util.S2SHashValue;
 
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import java.math.BigInteger;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.coeus.extrakeyperson.ExtraKeyPersonList;
import gov.grants.apply.coeus.extrakeyperson.ExtraKeyPersonListType;
import gov.grants.apply.forms.rr_budget_v1_1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;

/**
 * @author  Eleanor Shavell
 * 
 */

 public class KeyPersonStream_V1_1 extends AttachmentValidator { 
    private gov.grants.apply.forms.rr_budget_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalObjFactory;
    private gov.grants.apply.coeus.extrakeyperson.ObjectFactory extraKeyPerObjFctory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory; 
    private gov.grants.apply.system.global_v1.ObjectFactory globObjFactory;

    private KeyPersonBean keyPersonBean;
  
    private UtilFactory utilFactory;
    private Calendar calendar;
    private S2STxnBean s2sTxnBean;
   
   
    /** Creates a new instance of KeyPersonStream */
    public KeyPersonStream_V1_1(){
        objFactory = new gov.grants.apply.forms.rr_budget_v1_1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        s2sTxnBean = new S2STxnBean();
        extraAttachments = new Vector();
    } 
   
    public BudgetYearDataType.KeyPersonsType getKeyPersons(BudgetPeriodDataBean periodBean)
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
        BudgetYearDataType.KeyPersonsType keyPersonType = objFactory.createBudgetYearDataTypeKeyPersonsType();
        keyPersonType.setTotalFundForKeyPersons(periodBean.getTotalFundsKeyPersons());
        
       
        //case 2229 - multi-pis functionality was added
        String role;
        int isNih = s2sTxnBean.getIsNIHSponsor(periodBean.getProposalNumber());
  
        
        CoeusVector cvKeyPersons = periodBean.getKeyPersons();
        //cvKeyPersons is vector of KeyPersonBeans
        for (int i=0; i < cvKeyPersons.size();i++){
            KeyPersonBean keyPersonBean = (KeyPersonBean) cvKeyPersons.elementAt(i);
            KeyPersonDataType keyPersonDataType = objFactory.createKeyPersonDataType();
// case 4256
            role = keyPersonBean.getRole();
            if (isNih == 1) {
                if (role.equals("Co-PD/PI"))    role = "Co-Investigator";
            }
            keyPersonDataType.setProjectRole(role);
 
            keyPersonDataType.setName(getName(keyPersonBean));            
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            //keyPersonDataType.setCompensation(getCompensation(keyPersonBean));
            String baseSalary = "0.00";
            //If budget period is greater than 10 then display the 10th period base salary
            if(periodBean.getBudgetPeriod()>10){
                baseSalary = s2sTxnBean.getBaseSalaryForPeriod(periodBean.getProposalNumber(), periodBean.getVersion(),
                    10, keyPersonBean.getPersonId());
            }else{
                baseSalary = s2sTxnBean.getBaseSalaryForPeriod(periodBean.getProposalNumber(), periodBean.getVersion(),
                    periodBean.getBudgetPeriod(), keyPersonBean.getPersonId());
            }
            if(baseSalary == null ){
                keyPersonDataType.setCompensation(getCompensation(keyPersonBean));
            }else{
                BigDecimal bdBaseSalary = new BigDecimal("0");
                bdBaseSalary = new BigDecimal(baseSalary);
                bdBaseSalary = bdBaseSalary.setScale(2,BigDecimal.ROUND_HALF_UP);
                keyPersonDataType.setCompensation(getCompensation(keyPersonBean, bdBaseSalary));
            }
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            keyPersonType.getKeyPerson().add(keyPersonDataType);
            
        }
        
        CoeusVector cvExtraPersons = periodBean.getExtraKeyPersons();
        if (cvExtraPersons != null ) {
            if (cvExtraPersons.size() > 0){
       
            BigDecimal extraFunds = new BigDecimal("0");
            BudgetYearDataType.KeyPersonsType.TotalFundForAttachedKeyPersonsType extraPersonType =
                    objFactory.createBudgetYearDataTypeKeyPersonsTypeTotalFundForAttachedKeyPersonsType();
            extraPersonType.setTotalFundForAttachedKeyPersonsExist("Y: Yes");
          
            extraKeyPerObjFctory = new gov.grants.apply.coeus.extrakeyperson.ObjectFactory();
            ExtraKeyPersonListType extraKeyPersonList = extraKeyPerObjFctory.createExtraKeyPersonList();
            extraKeyPersonList.setProposalNumber(periodBean.getProposalNumber());
            extraKeyPersonList.setBudgetPeriod(new BigInteger(Integer.toString(periodBean.getBudgetPeriod())));
       
            List extraPersonList = extraKeyPersonList.getKeyPersons();
          
            for (int i=0; i<cvExtraPersons.size();i++){
                KeyPersonBean keyPersonBean = (KeyPersonBean) cvExtraPersons.elementAt(i);
                extraFunds = extraFunds.add(keyPersonBean.getFundsRequested());
                 //prepare extra key persons attachment
                  ExtraKeyPersonListType.KeyPersonsType extraKeyPersonType= extraKeyPerObjFctory.createExtraKeyPersonListTypeKeyPersonsType();
                  ExtraKeyPersonListType.KeyPersonsType.CompensationType compType = extraKeyPerObjFctory.createExtraKeyPersonListTypeKeyPersonsTypeCompensationType();
                  extraKeyPersonType.setFirstName(keyPersonBean.getFirstName());
                  extraKeyPersonType.setMiddleName(keyPersonBean.getMiddleName());
                  extraKeyPersonType.setLastName(keyPersonBean.getLastName());
                 //case 2229 - multiple pis

                  role = keyPersonBean.getRole();
                   //coeusqa-2349 start
                  if (isNih == 1) {
                      if (role.equals("Co-PD/PI"))    role = "Co-Investigator";
                   }
              
                  extraKeyPersonType.setProjectRole(role);
              //    extraKeyPersonType.setProjectRole(keyPersonBean.getRole());
                  compType.setAcademicMonths(keyPersonBean.getAcademicMonths());
                  compType.setCalendarMonths(keyPersonBean.getCalendarMonths());
                  compType.setSummerMonths(keyPersonBean.getSummerMonths());
                  compType.setBaseSalary(keyPersonBean.getBaseSalary());
                  compType.setFringeBenefits(keyPersonBean.getFringe());
                  compType.setFundsRequested(keyPersonBean.getFundsRequested());
                  compType.setRequestedSalary(keyPersonBean.getRequestedSalary());
          
                  extraKeyPersonType.setCompensation(compType);
                  extraPersonList.add(extraKeyPersonType);
                //end preparing extra key persons
            }
       
            extraPersonType.setValue(extraFunds);
            keyPersonType.setTotalFundForAttachedKeyPersons(extraPersonType);
            
                //marshell it to XML doc
                CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
                Document extraKeyPerDoc = xmlGen.marshelObject( extraKeyPersonList,
                                "gov.grants.apply.coeus.extrakeyperson");
                byte[] pdfBytes = null;
                try{
                    InputStream templateIS = getClass().getResourceAsStream("/edu/mit/coeus/s2s/template/ExtraKeyPersonAttachment.xsl");
                    byte[] tmplBytes = new byte[templateIS.available()];
                    templateIS.read(tmplBytes);
                    pdfBytes = xmlGen.generatePdfBytes(extraKeyPerDoc,tmplBytes,null,
                        periodBean.getProposalNumber()+"_"+periodBean.getBudgetPeriod()+
                        "_ExKeyPerson");
                }catch(FOPException ex){
                    UtilFactory.log(ex.getMessage(),ex, "KeyPersonStream", "getKeyPersons");
                    throw new CoeusException(ex.getMessage());
                }catch(IOException ex){
                    UtilFactory.log(ex.getMessage(),ex, "KeyPersonStream", "getKeyPersons");
                    throw new CoeusException(ex.getMessage());
                }


                ProposalNarrativeFormBean propNarrBean = new ProposalNarrativeFormBean();
                propNarrBean.setProposalNumber(periodBean.getProposalNumber());
                propNarrBean.setModuleTitle("BudgetPeriod_"+periodBean.getBudgetPeriod());
                propNarrBean.setComments("Auto generated document for extra key persons");
                propNarrBean.setModuleStatusCode('C');
                propNarrBean.setNarrativeTypeCode(11);
                ProposalNarrativePDFSourceBean propNarrPDFBean = new ProposalNarrativePDFSourceBean();
                propNarrPDFBean.setProposalNumber(periodBean.getProposalNumber());
                propNarrPDFBean.setAcType("I");
                propNarrPDFBean.setFileBytes(pdfBytes);
                propNarrPDFBean.setFileName(AttachedFileDataTypeStream.addExtension(periodBean.getProposalNumber()+"_EXTRA_KEYPERSONS"));
                s2sTxnBean.insertAutoGenNarrativeDetails(propNarrBean, propNarrPDFBean,
                                periodBean.getBudgetPeriod()==1);

            }
        }
      
        return keyPersonType;
    }
    private Vector extraAttachments;
    
   
  private  HumanNameDataType getName(KeyPersonBean keyPersonBean)
	throws CoeusException, JAXBException {
	HumanNameDataType humanNameDataType = globalObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(keyPersonBean.getFirstName());
        humanNameDataType.setLastName(keyPersonBean.getLastName());
        humanNameDataType.setMiddleName(keyPersonBean.getMiddleName());
        
        return humanNameDataType;
  }
   
   private KeyPersonCompensationDataType getCompensation(KeyPersonBean keyPersonBean)
	throws CoeusException, JAXBException {
      KeyPersonCompensationDataType keyPersonCompDataType =
                objFactory.createKeyPersonCompensationDataType();
      keyPersonCompDataType.setAcademicMonths(keyPersonBean.getAcademicMonths());
      keyPersonCompDataType.setBaseSalary(keyPersonBean.getBaseSalary());
      keyPersonCompDataType.setCalendarMonths(keyPersonBean.getCalendarMonths());
      keyPersonCompDataType.setFringeBenefits(keyPersonBean.getFringe());
      keyPersonCompDataType.setFundsRequested(keyPersonBean.getFundsRequested());
      keyPersonCompDataType.setRequestedSalary(keyPersonBean.getRequestedSalary());
      keyPersonCompDataType.setSummerMonths(keyPersonBean.getSummerMonths());
   
      return keyPersonCompDataType;
   }

    private static BigDecimal convDoubleToBigDec(double d){
        return new BigDecimal(d);
    }
    private Calendar getCal(Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
    
    private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }

    /**
     * Getter for property extraAttachments.
     * @return Value of property extraAttachments.
     */
    public java.util.Vector getExtraAttachments() {
        return extraAttachments;
    }
    
    /**
     * Setter for property extraAttachments.
     * @param extraAttachments New value of property extraAttachments.
     */
    public void setExtraAttachments(java.util.Vector extraAttachments) {
        this.extraAttachments = extraAttachments;
    }
    
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    /**
     * Method to fetch the compensation details for the keyperson
     * @param KeyPersonBean
     * @param BigDecimal
     * @return KeyPersonCompensationDataType
     * @throws CoeusException if exception occur
     */
    private KeyPersonCompensationDataType getCompensation(KeyPersonBean keyPersonBean, BigDecimal bdBaseSalary)
	throws CoeusException, JAXBException {
      KeyPersonCompensationDataType keyPersonCompDataType =
                objFactory.createKeyPersonCompensationDataType();
      keyPersonCompDataType.setAcademicMonths(keyPersonBean.getAcademicMonths());
      //set the base salary for the respective period
      keyPersonCompDataType.setBaseSalary(bdBaseSalary);
      keyPersonCompDataType.setCalendarMonths(keyPersonBean.getCalendarMonths());
      keyPersonCompDataType.setFringeBenefits(keyPersonBean.getFringe());
      keyPersonCompDataType.setFundsRequested(keyPersonBean.getFundsRequested());
      keyPersonCompDataType.setRequestedSalary(keyPersonBean.getRequestedSalary());
      keyPersonCompDataType.setSummerMonths(keyPersonBean.getSummerMonths());
   
      return keyPersonCompDataType;
   }
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
}
