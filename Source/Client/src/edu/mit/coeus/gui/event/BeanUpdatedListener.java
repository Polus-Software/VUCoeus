/*
 * UpdateListener.java
 *
 * Created on September 29, 2003, 12:30 PM
 */

package edu.mit.coeus.gui.event;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  geo
 */
public interface BeanUpdatedListener {
    /** invoked when bean is Updated
     * @param beanEvent encapsulates event information.
     */   
    void beanUpdated(BeanEvent beanEvent);
    
}
