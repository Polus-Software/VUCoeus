/*
 * @(#)AuthorizationOperator.java 1.0 8/13/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.query;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;

/** implements Operator */
public class AuthorizationOperator implements Operator, java.io.Serializable{
    
    private BaseBean baseBean;
    
    /** Creates new AuthorizationOperator
     * @param baseBean
     */    
    public AuthorizationOperator(BaseBean baseBean) {
        this.baseBean = baseBean;        
    }
    
    /**
     * This is an overiden method
     *
     * @param baseBean
     * @return boolean true if authorization is success else false
     */    
    public boolean getResult(BaseBean baseBean) {        
        return false;
    }
    
    /** Getter for property baseBean.
     * @return Value of property baseBean.
     *
     */
    public edu.mit.coeus.bean.BaseBean getBaseBean() {
        return baseBean;
    }
    
    /** Setter for property baseBean.
     * @param baseBean New value of property baseBean.
     *
     */
    public void setBaseBean(edu.mit.coeus.bean.BaseBean baseBean) {
        this.baseBean = baseBean;
    }
} // end LogicalOperator

