
/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */
package edu.mit.coeus.utils.query;

/** implements Operator and holds left hand and right hand values
 * which are instances of Opertors(Since Logical Operators operate on two Relational, Logical Operators).
 */
public abstract class LogicalOperator implements Operator {
    
    /** holds left hand Operator.
     */    
    protected Operator lhsOperator;
    /** holds right hand operator.
     */    
    protected Operator rhsOperator;
    
    /**
     * @param lhsOperator left hand Operator.
     * @param rhsOperator right hand operator.
     */    
    public  LogicalOperator(Operator lhsOperator, Operator rhsOperator) {
        this.lhsOperator = lhsOperator;
        this.rhsOperator = rhsOperator;
    } // end LogicalOperator
    
} // end LogicalOperator



