/*
 * @(#)CompensationStream.java November 9, 2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator.stream;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.CompensationBean;
import edu.mit.coeus.s2s.generator.AttachmentValidator;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
 
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;

import gov.grants.apply.forms.rr_budget_v1.RRBudgetType;
import gov.grants.apply.forms.rr_budget_v1.SectBCompensationDataType;
import gov.grants.apply.forms.rr_budget_v1.SectACompensationDataType;
import gov.grants.apply.system.globallibrary_v1.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  Eleanor Shavell
 * @Created on November 8, 2004, 10:12 AM
 */

 public class CompensationStream extends AttachmentValidator{ 
    private gov.grants.apply.forms.rr_budget_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalObjFactory;

    private CompensationBean compensationBean;
    
    private Hashtable propData;
    private String propNumber;
    private UtilFactory utilFactory;
    private Calendar calendar;
   
   
    /** Creates a new instance of CompensationStream */
    public CompensationStream(){
        objFactory = new gov.grants.apply.forms.rr_budget_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
      
      //   utilFactory = new UtilFactory();
    } 
   
    public SectACompensationDataType getSectAComp(CompensationBean compensationBean)
        throws CoeusXMLException,CoeusException,DBException{
        SectACompensationDataType sectAcomp = null;
        try{
           sectAcomp = objFactory.createSectACompensationDataType();
           sectAcomp.setAcademicMonths(compensationBean.getAcademicMonths());
           sectAcomp.setCalendarMonths(compensationBean.getCalendarMonths());
           sectAcomp.setFringeBenefits(compensationBean.getFringe());
           sectAcomp.setFundsRequested(compensationBean.getFundsRequested());
           sectAcomp.setRequestedSalary(compensationBean.getRequestedSalary());
           sectAcomp.setSummerMonths(compensationBean.getSummerMonths());
              
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"CompensationStream","getSectAComp()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return sectAcomp;
    }
   
   public SectBCompensationDataType getSectBComp(CompensationBean compensationBean)
      throws CoeusXMLException,CoeusException,DBException{
        SectBCompensationDataType sectBcomp = null;
        try{
           sectBcomp = objFactory.createSectBCompensationDataType();
           sectBcomp.setAcademicMonths(compensationBean.getAcademicMonths());
           sectBcomp.setCalendarMonths(compensationBean.getCalendarMonths());
           sectBcomp.setFringeBenefits(compensationBean.getFringe());
           sectBcomp.setFundsRequested(compensationBean.getFundsRequested());
           sectBcomp.setRequestedSalary(compensationBean.getRequestedSalary());
           sectBcomp.setSummerMonths(compensationBean.getSummerMonths());
              
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"CompensationStream","getSectBComp()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return sectBcomp;
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

}
