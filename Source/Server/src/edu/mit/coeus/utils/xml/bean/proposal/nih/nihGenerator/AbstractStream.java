/*
 * AbstractStream.java
 *
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

/**
 *
 * @author  ele
 */

import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.bean.AbstractBean;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import java.math.BigInteger;

public class AbstractStream {
    
    ObjectFactory objFactory ;
    
    AbstractType abstractType;
  
    public AbstractStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;  
             
    }
   
  
       public AbstractType getAbstract(String proposalNumber, AbstractBean abstractBean) 
          throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
         abstractType = objFactory.createAbstractType();
         
         
        /** abstractTypeCode
         */
        abstractType.setAbstractTypeCode(new BigInteger (Integer.toString(abstractBean.getAbstractType())));
         
        /** abstractTypeDesc
         */
        abstractType.setAbstractTypeDesc(abstractBean.getAbstractDescription());
         
         
        /** abstractText
         */
        abstractType.setAbstractText(abstractBean.getAbstractText()); 
     
        return abstractType;
    }
       
}
    
   