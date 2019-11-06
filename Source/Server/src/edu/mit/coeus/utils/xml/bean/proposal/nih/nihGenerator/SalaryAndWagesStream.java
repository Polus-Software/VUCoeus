/*
 * SalaryAndWagesStream.java
 *
 * Created on April 28, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

import edu.mit.coeus.utils.xml.bean.proposal.bean.SalaryAndWagesBean;  
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.PersonFullNameTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.RarObjFactoryGenerator;
import java.math.*;
import java.text.*;
import java.text.DecimalFormat;

public class SalaryAndWagesStream
{
    ObjectFactory objFactory;     
    SalariesAndWagesType salariesAndWagesType;
      
    /** Creates a new instance of SalaryAndWagesStream */
    public SalaryAndWagesStream(ObjectFactory objFactory)
    {  
        this.objFactory = objFactory ;            
    }
     
    public SalariesAndWagesType getSalaryAndWages(SalaryAndWagesBean salaryAndWagesBean)
      throws CoeusException,  javax.xml.bind.JAXBException

    {        
       salariesAndWagesType = objFactory.createSalariesAndWagesType(); 
       /**AccountIdentifier - this should be the era commons name, which we don't have here.
       */   
 //      salariesAndWagesType.setAccountIdentifier(salaryAndWagesBean.getPersonId());
     
       /**AppointmentMonths
       */  
       salariesAndWagesType.setAppointmentMonths(new BigDecimal(salaryAndWagesBean.getAppointmentMonths()));
    
       /**AppointmentType
       */  
       salariesAndWagesType.setAppointmentType(salaryAndWagesBean.getAppointmentType());
     
       /**BaseSalary
       */  
       salariesAndWagesType.setBaseSalary(formatCost(salaryAndWagesBean.getCalculationBase()));
      
       /**FringeCost
       */  
       salariesAndWagesType.setFringeCost(formatCost(salaryAndWagesBean.getFringe()));
     
       /**FullTimeQuestion
       */  
       salariesAndWagesType.setFullTimeQuestion(salaryAndWagesBean.getFullTime());
       
       /**FundingMonths
       */  
       //changes made on may 2 2006 for new nih requirements of summer,calendar, academic months.
       salariesAndWagesType.setSummerFundingMonths(formatCost(salaryAndWagesBean.getSummerFundingMonths()));
       salariesAndWagesType.setAcademicFundingMonths(formatCost(salaryAndWagesBean.getAcademicFundingMonths()));
       salariesAndWagesType.setFundingMonths(formatCost(salaryAndWagesBean.getFundingMonths()));
        
       /**Name
       */  
       RarObjFactoryGenerator rarObjFactoryGenerator = new RarObjFactoryGenerator();
       edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory =
           rarObjFactoryGenerator.getInstance();
       
       PersonFullNameTypeStream personFullNameStream = new PersonFullNameTypeStream(rarObjFactory);
       
       salariesAndWagesType.setName(personFullNameStream.getPersonFullNameTypeInfo(salaryAndWagesBean));
     
       
       /**PercentEffort 
       */  
       // fixed on feb 9,2006
   //  salariesAndWagesType.setPercentEffort(new BigInteger(Integer.toString(salaryAndWagesBean.getPercentEffort())));
   //   salariesAndWagesType.setPercentEffort(Integer.toString(salaryAndWagesBean.getPercentEffort()));
        
       //note: changes for may 2 - percent effort is obsolete. 
       /*
       double dblEffort = (salaryAndWagesBean.getFundingMonths() / 12) * 100;
       String strEffort = Double.toString(dblEffort);
       //truncate to two decimal places
       int stop;
       int len=  strEffort.length();
       int dot = strEffort.indexOf(".");
       if (dot < 0){
           stop = len;
       }else{
          stop = dot + 3;
          if (stop > len) {
           stop = len;
          }
       }
       strEffort = strEffort.substring(0,stop);  
       salariesAndWagesType.setPercentEffort(strEffort);
       */
       
      
 
       /**ProjectRole
       */  
       salariesAndWagesType.setProjectRole(salaryAndWagesBean.getRole());
       
       /**ProjectRoleDescription
       */  
       salariesAndWagesType.setProjectRoleDescription(salaryAndWagesBean.getRole());
    
       salariesAndWagesType.setRequestedCost(new BigDecimal(salaryAndWagesBean.getSalaryRequested()));
     return salariesAndWagesType;
    }
 
    
   
     private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
     return bdCost;   
     }
         
      
}
