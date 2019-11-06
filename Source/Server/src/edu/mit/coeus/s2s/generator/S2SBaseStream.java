/*
 * S2SBaseStream.java
 *
 * Created on October 28, 2004, 1:58 PM
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import gov.grants.apply.system.metagrantapplication.GrantApplicationType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.bind.JAXBException;
 
/**
 *
 * @author  geot
 */
public abstract class S2SBaseStream extends AttachmentValidator{
    
    public abstract Object getStream(HashMap ht) throws JAXBException,  
                            CoeusException,DBException;
    
}
