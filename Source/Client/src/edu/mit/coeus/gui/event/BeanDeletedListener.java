/*
 * DeleteListener.java
 *
 * Created on September 29, 2003, 12:35 PM
 */

package edu.mit.coeus.gui.event;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  geo
 */
public interface BeanDeletedListener {
    /** invoked when bean is Deleted
     * @param beanEvent encapsulates event information.
     */   
    void beanDeleted(BeanEvent beanEvent);
    
}
