
/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */
package edu.mit.coeus.utils.query;

import edu.mit.coeus.bean.BaseBean;

/** This class is a wrapper for or operator ( || ).
 * Takes two Operators as parameters in the constructor and returs true
 * if any one of the operators return true as result, else returns false.
 */
public class Or extends LogicalOperator {

    /** creates new instance of Or
     * @param lhsOperator left hand operator.
     * @param rhsOperator right hand operator.
     */    
    public  Or(Operator lhsOperator, Operator rhsOperator) {        
        super(lhsOperator, rhsOperator);
    } // end Or        

    /** returs true
     * if any one of the operators return true as result, else returns false.
     * @param baseBean BaseBean
     * @return true if any one of the operators return true as result, else returns false.
     */    
    public boolean getResult(BaseBean baseBean) {        
        return (lhsOperator.getResult(baseBean) || rhsOperator.getResult(baseBean));
    }
    
    /** 
     * returns the logical OR condition being checked using left-hand operator(lhsOperator)
     * and right-hand operator(rhsOperator)
     * @return String - OR condition
     */ 
    public String toString() {
        return "( " + lhsOperator.toString() + " || " + rhsOperator + " )";
    }

 // end getResult        

 } // end Or



