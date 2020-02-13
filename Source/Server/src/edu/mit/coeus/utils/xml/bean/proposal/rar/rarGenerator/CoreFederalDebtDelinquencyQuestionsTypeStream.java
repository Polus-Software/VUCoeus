/*
 * CoreFederalDebtDelinquencyQuestionsTypeStream.java
 *
 * Created on March 2, 2004, 6:04 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 

public class CoreFederalDebtDelinquencyQuestionsTypeStream {
    
    ObjectFactory objFactory ;
    CoreFederalDebtDelinquencyQuestionsType coreFedDebtQuestionsType;
    /** Creates a new instance of CoreFederalDebtDelinquencyQuestionsTypeStream */
    public CoreFederalDebtDelinquencyQuestionsTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public CoreFederalDebtDelinquencyQuestionsType getCoreFedDebtQuestions(String propNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
        coreFedDebtQuestionsType = objFactory.createCoreFederalDebtDelinquencyQuestionsType();
        coreFedDebtQuestionsType.setApplicantDelinquentIndicator(false);
        return coreFedDebtQuestionsType;
    }
}
