/*
 * ComparableBean.java
 *
 * Created on October 30, 2003, 9:30 AM
 */

package edu.mit.coeus.bean;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */

import edu.mit.coeus.exception.CoeusException;

public interface ComparableBean extends BaseBean{
    
    public boolean isLike(ComparableBean comparableBean)throws CoeusException;
    
}
