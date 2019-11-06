/*
 * TravelCostsTypeStream.java
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
import edu.mit.coeus.utils.xml.bean.proposal.bean.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.* ;

public class TravelCostsTypeStream {
    ObjectFactory objFactory ;
    TravelCostsType travelCostsType;
    
    /** Creates a new instance of TravelCostsTypeStream */
    public TravelCostsTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public TravelCostsType getTravelCosts(CostBean travelCostBean )
        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         travelCostsType = objFactory.createTravelCostsType();
         travelCostsType.setCost(formatCost(travelCostBean.getCost()));
         travelCostsType.setType(travelCostBean.getBudgetCategoryDesc());
         //nsf extension
         travelCostsType.setDescription(travelCostBean.getDescription());
         return travelCostsType;
    }
    
      private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
         return bdCost;
      }
   
}
