/*
 * CoreBudgetTotalsTypeStream.java
 *
 * Created on March 2, 2004, 6:05 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import java.lang.Double;
import java.util.* ;
import java.math.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CoreBudgetTotalsTypeStream {
    ObjectFactory objFactory ;
    CoreBudgetTotalsType coreBudgetTotalsType;
    
    /** Creates a new instance of CoreBudgetTotalsTypeStream */
    public CoreBudgetTotalsTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
           
    }
    
     public CoreBudgetTotalsType getBudgetTotals(String propNumber,int version)
         throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         coreBudgetTotalsType = objFactory.createCoreBudgetTotalsType();
         
         if (version > 0) {
             
            ProposalPrintingTxnBean propPrintingTxnBean = new ProposalPrintingTxnBean();
            HashMap hashTotal = propPrintingTxnBean.getCoreBudgetTotal(propNumber,  version) ;      
              
            BigDecimal costSharing = (BigDecimal) hashTotal.get("COST_SHARING_AMOUNT");
            BigDecimal totalCost = (BigDecimal) hashTotal.get("TOTAL_COST");
            BigDecimal totalIndirect = (BigDecimal) hashTotal.get("TOTAL_INDIRECT_COST");
    //      BigDecimal totalDirect = (BigDecimal) hashTotal.get("TOTAL_DIRECT_COST");
       
            coreBudgetTotalsType.setApplicantCost(costSharing) ;
            coreBudgetTotalsType.setFederalCost(totalCost) ;
            coreBudgetTotalsType.setLocalCost(new BigDecimal("0")) ;
            coreBudgetTotalsType.setOtherCost(totalIndirect) ;
            coreBudgetTotalsType.setProgramIncome(new BigDecimal("0")) ;
            coreBudgetTotalsType.setStateCost(new BigDecimal("0")) ;
         } else {
             //no budget
            coreBudgetTotalsType.setApplicantCost(new BigDecimal("0")) ;
            coreBudgetTotalsType.setFederalCost(new BigDecimal("0")) ;
            coreBudgetTotalsType.setLocalCost(new BigDecimal("0")) ;
            coreBudgetTotalsType.setOtherCost(new BigDecimal("0")) ;
            coreBudgetTotalsType.setProgramIncome(new BigDecimal("0")) ;
            coreBudgetTotalsType.setStateCost(new BigDecimal("0")) ;
         }
         
         return coreBudgetTotalsType;
    }
   
}
