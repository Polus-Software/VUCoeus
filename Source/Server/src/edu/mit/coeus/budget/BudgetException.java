/*
 * BudgetException.java
 *
 * Created on November 18, 2003, 11:17 AM
 */

package edu.mit.coeus.budget;

import edu.mit.coeus.exception.CoeusException;
/**
 *
 * @author  geo
 */
public class BudgetException extends CoeusException {
    
    /**
     * Creates a new instance of <code>BudgetException</code> without detail message.
     */
    public BudgetException() {
    }
    
    
    /**
     * Constructs an instance of <code>BudgetException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BudgetException(String msg) {
        super(msg);
    }
}
