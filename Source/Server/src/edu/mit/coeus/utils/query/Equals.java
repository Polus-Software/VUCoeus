
/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */
package edu.mit.coeus.utils.query;

import java.lang.reflect.*;

import edu.mit.coeus.bean.BaseBean;

/** This class is a wrapper for equals operator ( == ).
 * Takes field and Comparable objects as parameters in the constructor and returs true
 * if the field of the BaseBean object is equal to the Comparable object, else returns false.
 */

public class Equals extends RelationalOperator {
    
    /** creates a new Equals object.
     * @param fieldName field which has to be compared.
     * @param fixedData compare value.
     */  
    public  Equals(String fieldName, Comparable fixedData) {
        super(fieldName, fixedData);
    }
    
    /** creates a new Equals object.
     * Since Boolean object is not Comparable, use this constructor to check boolean values.
     * @param fieldName field which has to be compared.
     * @param booleanFixedData compare value.
     */  
    public  Equals(String fieldName, boolean booleanFixedData) {
        super(fieldName, booleanFixedData);
    }
    
    /** returns true if the field of the BaseBean object is equal to the Comparable object/boolean data, else returns false.
     * @param baseBean BaseBean
     * @return true if the field of the BaseBean object is equal to the Comparable object/boolean data, else returns false.
     */ 
    public boolean getResult(BaseBean baseBean) {
        try{
            return compare(baseBean) == 0;
        }catch (Exception exception) {
            return false;
        }
    } // end getResult
    
    /**
     * returns the equality condition being checked using fieldName and fixedData
     * @return String - Equality condition
     */
    public String toString() {
        if (! isBoolean) {
            return "( " + fieldName + " == " + fixedData + " )";
        } else {
            return "( " + fieldName + " == " + booleanFixedData + " )";
        }
    }
    
} // end Equals



