/*
 * RarObjFactoryGenerator.java
 *
 * Created on March 3, 2004, 4:39 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

import edu.mit.coeus.utils.xml.bean.proposal.rar.*;

/**
 *
 * @author  ele
 */
public class RarObjFactoryGenerator {
    
    ObjectFactory rarObjFactory;
    
    public RarObjFactoryGenerator() 
    {
         rarObjFactory = new ObjectFactory();
    }
    
    public ObjectFactory getInstance()
    {
        return rarObjFactory ;
    }
    
}
