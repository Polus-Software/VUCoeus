/*
 * KeyPersonBioSketchStream.java
 *
 * Created on April 1, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

/**
 *
 * @author  ele
 */

import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;

public class KeyPersonBiosketchStream {
    
    ObjectFactory objFactory ;   
    KeyPersonBiosketchType keyPersonBiosketchType;
  
    public KeyPersonBiosketchStream(ObjectFactory objFactory)
    {
        this.objFactory = objFactory ;            
    }
    
       public KeyPersonBiosketchType getKeyBiosketch(ProposalKeyPersonFormBean keyPersonBean)
            throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
        keyPersonBiosketchType = objFactory.createKeyPersonBiosketchType();
         //hard coding file id
         
         keyPersonBiosketchType.setResearchSupportFileIdentifier("researchfilename");
        //hard coding file id
         keyPersonBiosketchType.setPositionsHonorsCitationsFileIdentifier("honorsfilename");
        
        
         return keyPersonBiosketchType;
    }
       
       //overloading for PI as key person
          public KeyPersonBiosketchType getKeyBiosketch(ProposalPersonFormBean personBean)
            throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
        keyPersonBiosketchType = objFactory.createKeyPersonBiosketchType();
         //hard coding file id
         
         keyPersonBiosketchType.setResearchSupportFileIdentifier("researchfilename");
        //hard coding file id
         keyPersonBiosketchType.setPositionsHonorsCitationsFileIdentifier("honorsfilename");
        
        
         return keyPersonBiosketchType;
    }
}
    
   