/*
 * @(#)OtherPersonnelStream.java November 17, 2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator.stream;

import edu.mit.coeus.exception.CoeusException;
import javax.xml.bind.JAXBException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.generator.AttachmentValidator;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.forms.rr_budget_v1.*;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @author  Eleanor Shavell
 * @Created on November 17, 2004, 10:12 AM
 */

 public class OtherPersonnelStream extends AttachmentValidator{ 
    private gov.grants.apply.forms.rr_budget_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalObjFactory;

    private OtherPersonnelBean otherPersonnelBean;
  
    private UtilFactory utilFactory;

   
   
    /** Creates a new instance of OtherPersonnelStream */
    public OtherPersonnelStream(){
        objFactory = new gov.grants.apply.forms.rr_budget_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
      
      //   utilFactory = new UtilFactory();
    } 
   
    public BudgetYearDataType.OtherPersonnelType getOtherPersonnel(BudgetPeriodDataBean periodBean)
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
   
        OtherPersonnelBean otherPersonnelBean;
        CompensationBean compensationBean;
        String personnelType;
        SectBCompensationDataType sectBCompType;
        OtherPersonnelDataType otherOtherPersonnelType;
         
        BudgetYearDataType.OtherPersonnelType otherPersonnelType =
                     objFactory.createBudgetYearDataTypeOtherPersonnelType();
        
        otherPersonnelType.setOtherPersonnelTotalNumber(periodBean.getOtherPersonnelTotalNumber());
        otherPersonnelType.setTotalOtherPersonnelFund(periodBean.getTotalOtherPersonnelFunds());
    
        CoeusVector cvOtherPersonnel = periodBean.getOtherPersonnel();
        //cvOtherPersonnel is a vector of OtherPersonnelBeans
        
        BudgetYearDataType.OtherPersonnelType.PostDocAssociatesType postDocType =
                objFactory.createBudgetYearDataTypeOtherPersonnelTypePostDocAssociatesType();
        BudgetYearDataType.OtherPersonnelType.GraduateStudentsType gradStudentType =
                objFactory.createBudgetYearDataTypeOtherPersonnelTypeGraduateStudentsType();
        BudgetYearDataType.OtherPersonnelType.SecretarialClericalType secType =
                objFactory.createBudgetYearDataTypeOtherPersonnelTypeSecretarialClericalType();
        BudgetYearDataType.OtherPersonnelType.UndergraduateStudentsType underGradType =
                objFactory.createBudgetYearDataTypeOtherPersonnelTypeUndergraduateStudentsType();
  
        CompensationStream compensationStream = new CompensationStream();
        
     
        for (int i=0; i < cvOtherPersonnel.size();i++){
           //cvOtherPersonnel is a vector of OtherPersonnelBeans. there may be a bean for 
           // gradstudents, one for postdocs, one for secs, one for undergragds, and up to 6
           // for other types
    
           otherPersonnelBean = (OtherPersonnelBean) cvOtherPersonnel.elementAt(i);
           personnelType = otherPersonnelBean.getPersonnelType();
           if (personnelType == "PostDoc"){ 
            postDocType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
            postDocType.setProjectRole(otherPersonnelBean.getRole());
            sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
            postDocType.setCompensation(sectBCompType);
            
            otherPersonnelType.setPostDocAssociates(postDocType);
            }
           else if (personnelType == "Grad"){
            gradStudentType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
            gradStudentType.setProjectRole(otherPersonnelBean.getRole());
            sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
            gradStudentType.setCompensation(sectBCompType);
            
            otherPersonnelType.setGraduateStudents(gradStudentType);
           }
           else if (personnelType == "UnderGrad") {
            underGradType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
            underGradType.setProjectRole(otherPersonnelBean.getRole());
            sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
            underGradType.setCompensation(sectBCompType);
            
            otherPersonnelType.setUndergraduateStudents(underGradType);
            }
           else if (personnelType == "Sec") {
            secType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
            secType.setProjectRole(otherPersonnelBean.getRole());
            sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
            secType.setCompensation(sectBCompType);
            
            otherPersonnelType.setSecretarialClerical(secType);
             }
           else {
              otherOtherPersonnelType = objFactory.createOtherPersonnelDataType();
              otherOtherPersonnelType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
              otherOtherPersonnelType.setProjectRole(otherPersonnelBean.getRole());
              sectBCompType = compensationStream.getSectBComp(otherPersonnelBean.getCompensation());
              otherOtherPersonnelType.setCompensation(sectBCompType);
              
              otherPersonnelType.getOther().add(otherOtherPersonnelType); 
           }
         }
             
       
  
        return otherPersonnelType;
    }
   

    
}
