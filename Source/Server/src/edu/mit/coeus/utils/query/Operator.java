/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */
package edu.mit.coeus.utils.query;

import edu.mit.coeus.bean.BaseBean;

/** The Operator interface should be implemented by any class whose instances are intended to be executed as an Operator. The class must define a method of boolean argument called getResult.
 *
 * This interface is designed to provide a common protocol for objects that wish be recognised as Operators. For example, Operator is implemented by class GreaterThan which is a wrapper for greater than ( > ) operator.
 *
 */
public interface Operator {

    /** true if operation succeeds, else return false.
     * @param baseBean BaseBean
     * @return true if operation succeeds, else return false.
     */    
    public boolean getResult(BaseBean baseBean);

} // end Operator





