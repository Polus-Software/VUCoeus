/*
 * ParticipantPatientCostsTypeStream.java
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

public class ParticipantPatientCostsTypeStream {
    ObjectFactory objFactory ;
    ParticipantPatientCostsType participantPatientCostsType;
    
    /** Creates a new instance of ParticipantPatientCostsTypeStream */
    public ParticipantPatientCostsTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public ParticipantPatientCostsType getPartPatientCosts(CostBean costBean)
        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         participantPatientCostsType = objFactory.createParticipantPatientCostsType();
         participantPatientCostsType.setCost(formatCost(costBean.getCost()));
         participantPatientCostsType.setDescription(costBean.getDescription());
         participantPatientCostsType.setType(costBean.getBudgetCategoryDesc());
         
         return participantPatientCostsType;
    }
    
      private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
         return bdCost;
      }
   
}
