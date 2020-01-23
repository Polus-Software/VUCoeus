/*
 * OtherDirectCostsTypeStream.java
 *
 * Created on April 30, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import edu.mit.coeus.utils.xml.bean.proposal.bean.CostBean;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.* ;

public class OtherDirectCostsTypeStream {
    ObjectFactory objFactory ;
    OtherDirectCostsType otherDirectCostsType;
    
    /** Creates a new instance of OtherDirectCostsTypeStream */
    public OtherDirectCostsTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public OtherDirectCostsType getOtherDirectCosts(CostBean costBean)
        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         otherDirectCostsType = objFactory.createOtherDirectCostsType();
         otherDirectCostsType.setCost(formatCost(costBean.getCost()));
         otherDirectCostsType.setDescription(costBean.getDescription());
         otherDirectCostsType.setType(costBean.getBudgetCategoryDesc());
         
         return otherDirectCostsType;
    }
    
      private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
         return bdCost;
      }
   
}
