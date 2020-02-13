/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */

package edu.mit.coeus.utils.query;

import java.lang.reflect.*;

import edu.mit.coeus.bean.BaseBean;

/** This class is a wrapper for and operator ( & ).
 * Takes two Operators as parameters and returs true
 * if both the operators return true as result, else returns false.
 */
public class And extends LogicalOperator {
    
    /** creates new instance of And.
     * @param lhsOperator left hand operator.
     * @param rhsOperator right hand operator.
     */    
    public  And(Operator lhsOperator, Operator rhsOperator) {
        super(lhsOperator, rhsOperator);
        // your code here
    } // end And
    
    /** returs true if both the operators return true as result,
     * else returns false.
     * @param baseBean BaseBean
     * @return returs true if both the operators return true as result,
     * else returns false.
     */    
    public boolean getResult(BaseBean baseBean) {
        return (lhsOperator.getResult(baseBean) && rhsOperator.getResult(baseBean));
    }
    
    /** 
     * returns the logical AND condition being checked using left-hand operator(lhsOperator)
     * and right-hand operator(rhsOperator)
     * @return String - AND condition
     */ 
    public String toString() {
        return "( " + lhsOperator.toString() + " && " + rhsOperator + " )";
    }
    
} // end And



