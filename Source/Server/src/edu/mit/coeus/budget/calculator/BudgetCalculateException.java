/*
 * BudgetCalculateException.java
 *
 * Created on November 18, 2003, 11:17 AM
 */

package edu.mit.coeus.budget.calculator;

import edu.mit.coeus.budget.BudgetException;
/**
 *
 * @author  geo
 */
public class BudgetCalculateException extends BudgetException {
    
    /**
     * Creates a new instance of <code>BudgetCalculateException</code> without detail message.
     */
    public BudgetCalculateException() {
    }
    
    
    /**
     * Constructs an instance of <code>BudgetCalculateException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BudgetCalculateException(String msg) {
        super(msg);
    }
}
