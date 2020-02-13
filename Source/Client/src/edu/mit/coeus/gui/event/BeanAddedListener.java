/*
 * InsertListener.java
 *
 * Created on September 29, 2003, 12:29 PM
 */

package edu.mit.coeus.gui.event;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  geo
 */
public interface BeanAddedListener {
    
    /** invoked when bean is Added
     * @param beanEvent encapsulates event information.
     */    
    void beanAdded(BeanEvent beanEvent);
    
}
 