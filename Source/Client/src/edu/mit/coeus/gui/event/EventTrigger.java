/*
 * IEventTrigger.java
 *
 * Created on October 9, 2003, 11:18 AM
 */

package edu.mit.coeus.gui.event;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  geo
 */

public interface EventTrigger {
    
    /** Notifies all listeners that have registered interest for notification on this event type.
     * @param beanEvent the BeanEvent object
     */    
    public void fireBeanAdded(BeanEvent beanEvent);
    
    /** Notifies all listeners that have registered interest for notification on this event type.
     * @param beanEvent the BeanEvent object
     */
    public void fireBeanDeleted(BeanEvent beanEvent);
    
    /** Notifies all listeners that have registered interest for notification on this event type.
     * @param beanEvent the BeanEvent object
     */
    public void fireBeanUpdated(BeanEvent beanEvent);
    
}
