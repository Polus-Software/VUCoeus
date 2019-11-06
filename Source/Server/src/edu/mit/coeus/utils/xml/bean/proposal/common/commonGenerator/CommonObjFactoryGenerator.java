/*
 * CommonObjFactoryGenerator.java
 *
 * Created on March 3, 2004, 4:39 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator;

import edu.mit.coeus.utils.xml.bean.proposal.common.*;

/**
 *
 * @author  ele
 */
public class CommonObjFactoryGenerator {
    
    ObjectFactory commonObjFactory;
    
    public CommonObjFactoryGenerator() 
    {
         commonObjFactory = new ObjectFactory();
    }
    
    public ObjectFactory getInstance()
    {
        return commonObjFactory ;
    }
    
}
