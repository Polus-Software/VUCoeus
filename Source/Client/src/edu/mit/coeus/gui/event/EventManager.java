/*
 * IEventManager.java
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

public interface EventManager {
    
    /** adds a listener to listen to Bean Added Event.
     * @param addedListener the listener to be added
     * @param beanClass beanClass whose addition has to be listened.
     */    
    public void addBeanAddedListener(BeanAddedListener addedListener, Class beanClass);
    
    /** adds a listener to listen to Bean Deleted Event.
     * @param deletedListener the listener to be added
     * @param beanClass beanClass whose Deletion has to be listened.
     */    
    public void addBeanDeletedListener(BeanDeletedListener deletedListener, Class beanClass);
    
    /** adds a listener to listen to Bean Updated Event.
     * @param updatedListener the listener to be added
     * @param beanClass beanClass whose Updation has to be listened.
     */    
    public void addBeanUpdatedListener(BeanUpdatedListener updatedListener, Class beanClass);
    
    /** removes the listener.
     * @param addedListener the listener to be removed.
     * @param beanClass beanClass whose Addition was being listened.
     */    
    public void removeBeanAddedListener(BeanAddedListener addedListener, Class beanClass);

    /** removes the listener.
     * @param deletedListener the listener to be removed.
     * @param beanClass beanClass whose Deletion was being listened.
     */    
    public void removeBeanDeletedListener(BeanDeletedListener deletedListener, Class beanClass);

    /** removes the listener.
     * @param updatedListener the listener to be removed.
     * @param beanClass beanClass whose Updation was being listened.
     */    
    public void removeBeanUpdatedListener(BeanUpdatedListener updatedListener, Class beanClass);
    
}
