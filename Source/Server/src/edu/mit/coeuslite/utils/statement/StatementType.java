/*
 * StatementType.java
 *
 * Created on April 22, 2005, 12:06 PM
 */

package edu.mit.coeuslite.utils.statement;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;

/**
 *
 * @author  sharathk
 */
public interface StatementType {
   
   public ProcReqParameter execute(Object values, String userId)throws CoeusException;
   
}
