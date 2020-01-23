/*
 * BudgetTotalsTypeStream.java
 *
 * Created on March 12, 2004 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.xml.bean.proposal.bean.*;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import java.lang.Double;
import java.util.* ;
import java.math.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BudgetTotalsTypeStream {
    ObjectFactory objFactory ;
    BudgetTotalsType BudgetTotalsType;
    BudgetPeriodPrintingBean budgetPeriodBean;
    QueryEngine queryEngine ;
    String key;
    
    /** Creates a new instance of BudgetTotalsTypeStream */
    public BudgetTotalsTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
        queryEngine = QueryEngine.getInstance();
    }
    
 //    public BudgetTotalsType getInitBudgetTotals(CoeusVector vecBudgetBeans)
      public BudgetTotalsType getInitBudgetTotals(String propNumber)
  
           throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
        System.out.println("in budgetTotalsTypeStream, getInitBudgetTtoals");
        
         BudgetTotalsType budgetTotalsType = objFactory.createBudgetTotalsType();
          
         budgetPeriodBean = getInitialBudgetPeriod(propNumber);
         //now first period is in budgetPeriodBean
          
         //set initial applicantCost to cost sharing amount for period 1
       
         budgetTotalsType.setApplicantCost(formatCost(budgetPeriodBean.getCostSharingAmt())) ;
         //set initial FederalCost to total cost for period 1
         budgetTotalsType.setFederalCost( formatCost(budgetPeriodBean.getPeriodCostsTotal())) ;
         //hard coding period 1 local cost, other cost, program income, state cost to 0
         budgetTotalsType.setLocalCost(new BigDecimal("0")) ;       
         budgetTotalsType.setOtherCost(new BigDecimal("0")) ;
         budgetTotalsType.setProgramIncome(new BigDecimal("0")) ;
         budgetTotalsType.setStateCost(new BigDecimal("0")) ;
          
         return budgetTotalsType;
    }
    
    //   public BudgetTotalsType getTotalBudgetTotals(BudgetInfoBean budgetInfoBean) 
       public BudgetTotalsType getTotalBudgetTotals(String propNumber) 
       throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         System.out.println("in budgetTotalsTypeStream, getTotalBudgetTotals");
     
         BudgetTotalsType budgetTotalsType = objFactory.createBudgetTotalsType();
         //queryEngine has all the budget info
         // first filter to get final version
         // then sum up the costs over all the periods
         BudgetPeriodPrintingBean budgetPeriodBean = new BudgetPeriodPrintingBean();
         key = propNumber;
         Equals equalsVersion = new Equals("finalVersionFlag", "Y");
         CoeusVector cvTotal = queryEngine.getActiveData(key,BudgetPeriodPrintingBean.class, equalsVersion);
         //cvTotal is vector containing BudgetPeriodPrintingBeans for all periods of final version
         //sum up the necessary costs
         double totalCost = cvTotal.sum("periodCostsTotal");
         double costSharing = cvTotal.sum("costSharingAmt");
         
         //set total applicantCost to cost sharing amount
         budgetTotalsType.setApplicantCost(formatCost(costSharing)) ;  
         //set total FederalCost to total cost
         budgetTotalsType.setFederalCost( formatCost(totalCost)) ;
         //hard coding budget totals local cost, other cost, program income and state cost to 0
         budgetTotalsType.setLocalCost(new BigDecimal("0")) ;       
         budgetTotalsType.setOtherCost(new BigDecimal("0")) ;
         budgetTotalsType.setProgramIncome(new BigDecimal("0")) ;
         budgetTotalsType.setStateCost(new BigDecimal("0")) ;
        
         System.out.println("---- returning from getTotalBudgetTotals");
     
         return budgetTotalsType;
    } 
    
     private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
         return bdCost;   
     }
    
     private  BudgetPeriodPrintingBean  getInitialBudgetPeriod(String propNumber) 
         throws CoeusException {
       // vecBudgetBeans is vector of BudgetPeriodPrintingBeans containing budget detail info
       // for each period in each version.  it is in the queryEngine.
       // filter for budget period 1 and final version 
             
     System.out.println("-----enter getInitBudgetperiod");
    
        BudgetPeriodPrintingBean budgetPeriodBean = new BudgetPeriodPrintingBean();
        key = propNumber;
  //      QueryEngine queryEngine = QueryEngine.getInstance();
        Equals equalsPeriod = new Equals("budgetPeriod", new Integer(1));
        Equals equalsVersion = new Equals("finalVersionFlag", "Y");
        And periodAndVersion = new And( equalsPeriod,equalsVersion);
        CoeusVector cvBudget = queryEngine.getActiveData(key,BudgetPeriodPrintingBean.class, periodAndVersion);
        //cvBudget is a vector of BudgetPeriodPrintingBeans - should just be one bean for this
        // version and period
        System.out.println("---before getting bean from budget vector, vector size is " + cvBudget.size());
        if (cvBudget.size() > 0) {
            budgetPeriodBean = (BudgetPeriodPrintingBean) cvBudget.get(0);
        }
        System.out.println("---returning budgetperiodbean");
        return budgetPeriodBean;
                
        /*
         if (vecPeriodBeans != null)
        {    
            for (int vecCount = 0 ; vecCount < vecPeriodBeans.size() ; vecCount++)
               {
                   budgetPeriodBean = (BudgetPeriodBean) vecPeriodBeans.get(vecCount) ;
                   if (budgetPeriodBean.getAw_BudgetPeriod() == 1) {
                       return budgetPeriodBean;
                   }                                     
               }   
        }// end if  vecPeriodBeans   
        else
        {
               System.out.println("** BudgetTotals : vecBudgetPeriods is null **") ;
        }    
         return budgetPeriodBean;
         **/
    }
}
