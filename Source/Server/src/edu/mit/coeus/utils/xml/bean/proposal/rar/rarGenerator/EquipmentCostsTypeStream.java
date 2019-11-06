/*
 * EquipmentCostsTypeStream.java
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

public class EquipmentCostsTypeStream {
    ObjectFactory objFactory ;
    EquipmentCostsType equipmentCostsType;
    
    /** Creates a new instance of EquipmentCostsTypeStream */
    public EquipmentCostsTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public EquipmentCostsType getEquipmentCosts(CostBean equipCostBean )
        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         equipmentCostsType = objFactory.createEquipmentCostsType();
         equipmentCostsType.setCost(formatCost(equipCostBean.getCost()));
         equipmentCostsType.setDescription(equipCostBean.getBudgetCategoryDesc());
         //NSF extension
         equipmentCostsType.setEquipmentDescription(equipCostBean.getDescription());
         
         
         return equipmentCostsType;
    }
    
      private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
         return bdCost;
      }
   
}
