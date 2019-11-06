
/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */
package edu.mit.coeus.utils.query;

import java.lang.reflect.*;

import edu.mit.coeus.bean.BaseBean;

/** This class is a wrapper for greater than operator ( > ).
 * Takes field and Comparable object as parameters in the constructor and returs true
 * if the field of the BaseBean object is greater than the Comparable object, else returns false.
 */
public class GreaterThan extends RelationalOperator {

    /** creates a new GreaterThan object.
     * @param fieldName field which has to be compared.
     * @param fixedData compare value.
     */    
    public  GreaterThan(String fieldName, Comparable fixedData) {        
        super(fieldName, fixedData);
    } // end GreaterThan        

    /** returns true if the field of the BaseBean object is greater than the Comparable object, else returns false.
     * @param baseBean BaseBean
     * @return true if the field of the BaseBean object is greater than the Comparable object, else returns false.
     */    
    public boolean getResult(BaseBean baseBean) {
        if(fixedData == null) return false; //cannot query property > null. will always return false
        try{
            return compare(baseBean) > 0;
        }catch (Exception exception) {
            return false;
        }
    }
    
    /** 
     * returns the greater than condition being checked using fieldName and fixedData
     * @return String - Greater than condition
     */ 
    public String toString() {
        return "( " + fieldName + " > " + fixedData + " )";
    }

 } // end GreaterThan



