/*
 * EventManagerImpl.java
 *
 * Created on October 9, 2003, 12:38 PM
 */

package edu.mit.coeus.gui.event;

import java.util.*;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  geo
 */

public class EventManagerImpl implements EventManager, EventTrigger {
    
    private static EventManagerImpl eventManager;
    private HashMap beanAddedListeners;
    private HashMap beanDeletedListeners;
    private HashMap beanUpdatedListeners;
    
    /** Creates a new instance of EventManagerImpl */
    private EventManagerImpl() {
        init();
    }
    
    private void init(){
        beanAddedListeners = new HashMap();
        beanDeletedListeners = new HashMap();
        beanUpdatedListeners = new HashMap();
    }
    
    public static synchronized EventManagerImpl getInstance(){
        if(eventManager==null)
            eventManager = new EventManagerImpl();
        return eventManager;
    }
    
    /** adds a listener to listen to Bean Added Event.
     * @param addedListener the listener to be added
     * @param beanClass beanClass whose addition has to be listened.
     */
    public void addBeanAddedListener(BeanAddedListener addedListener, Class beanClass) {
        Class key = beanClass;
        List specBeanListeners;
        if(beanAddedListeners.containsKey(key)){
            specBeanListeners = (List)beanAddedListeners.get(key);
            specBeanListeners.add(addedListener);
            return;
        }
        specBeanListeners = Collections.synchronizedList(new ArrayList());
        specBeanListeners.add(addedListener);
        beanAddedListeners.put(key,specBeanListeners);
    }
    
    /** adds a listener to listen to Bean Deleted Event.
     * @param deletedListener the listener to be added
     * @param beanClass beanClass whose Deletion has to be listened.
     */
    public void addBeanDeletedListener(BeanDeletedListener deletedListener, Class beanClass) {
        Class key = beanClass;
        List specBeanListeners;
        if(beanDeletedListeners.containsKey(key)){
            specBeanListeners = (List)beanDeletedListeners.get(key);
            specBeanListeners.add(deletedListener);
            return;
        }
        specBeanListeners = Collections.synchronizedList(new ArrayList());
        specBeanListeners.add(deletedListener);
        beanDeletedListeners.put(key,specBeanListeners);
    }
    
    /** adds a listener to listen to Bean Updated Event.
     * @param updatedListener the listener to be added
     * @param beanClass beanClass whose Updation has to be listened.
     */
    public void addBeanUpdatedListener(BeanUpdatedListener updatedListener, Class beanClass) {
        Class key = beanClass;
        List specBeanListeners;
        if(beanUpdatedListeners.containsKey(key)){
            specBeanListeners = (List)beanUpdatedListeners.get(key);
            if(!specBeanListeners.contains(updatedListener)) {
                specBeanListeners.add(updatedListener);
            }
            return;
        }
        specBeanListeners = Collections.synchronizedList(new ArrayList());
        specBeanListeners.add(updatedListener);
        beanUpdatedListeners.put(key,specBeanListeners);
    }
    
    /** Notifies all listeners that have registered interest for notification on this event type.
     * @param beanEvent the BeanEvent object
     */
    public void fireBeanAdded(BeanEvent beanEvent) {
        Class key = beanEvent.getBean().getClass();
        List specBeanListeners = (List)beanAddedListeners.get(key);
        
        //Bug Fix - No Listeners to Fire - Start
        if(specBeanListeners == null || specBeanListeners.isEmpty()) {
            return ;
        }
        //Bug Fix - No Listeners to Fire - End
        
        /**
         * Copy Listeners to temp List.
         * to avoid getting concurrent modification exception.
         */
        List temp = new ArrayList(specBeanListeners);
        
        synchronized(temp) {
            if(temp == null || temp.isEmpty())
                return;
            for(Iterator listener = temp.iterator();listener.hasNext();){
                ((BeanAddedListener)listener.next()).beanAdded(beanEvent);
            }
        }
    }
    
    /** Notifies all listeners that have registered interest for notification on this event type.
     * @param beanEvent the BeanEvent object
     */
    public void fireBeanDeleted(BeanEvent beanEvent) {
        Class key = beanEvent.getBean().getClass();
        List specBeanListeners = (List)beanDeletedListeners.get(key);
        
        //Bug Fix - No Listeners to Fire - Start
        if(specBeanListeners == null || specBeanListeners.isEmpty()) {
            return ;
        }
        //Bug Fix - No Listeners to Fire - End
        
        /**
         * Copy Listeners to temp List.
         * to avoid getting concurrent modification exception.
         */
        List temp = new ArrayList(specBeanListeners);
        
        synchronized(temp) {
            if(temp==null || temp.isEmpty())
                return;
            for(Iterator listener = temp.iterator();listener.hasNext();){
                ((BeanDeletedListener)listener.next()).beanDeleted(beanEvent);
            }
        }
    }
    
    /** Notifies all listeners that have registered interest for notification on this event type.
     * @param beanEvent the BeanEvent object
     */
    public synchronized void fireBeanUpdated(BeanEvent beanEvent) {
        Class key =beanEvent.getBean().getClass();
        List specBeanListeners = (List)beanUpdatedListeners.get(key);
        
        //Bug Fix - No Listeners to Fire - Start
        if(specBeanListeners == null || specBeanListeners.isEmpty()) {
            return ;
        }
        //Bug Fix - No Listeners to Fire - End
        
        /**
         * Copy Listeners to temp List.
         * to avoid getting concurrent modification exception.
         */
        List temp = new ArrayList(specBeanListeners);
        
        synchronized(temp) {
            if(temp==null || temp.isEmpty())
                return;
            for(Iterator listener = temp.iterator();listener.hasNext();){
                ((BeanUpdatedListener)listener.next()).beanUpdated(beanEvent);
            }//End For
        }
    }
    
    /** removes the listener.
     * @param addedListener the listener to be removed.
     * @param beanClass beanClass whose Addition was being listened.
     */
    public void removeBeanAddedListener(BeanAddedListener addedListener, Class beanClass) {
        Class key = beanClass;
        List specBeanListeners;
        if(beanAddedListeners.containsKey(key)){
            specBeanListeners = (List)beanAddedListeners.get(key);
            specBeanListeners.remove(addedListener);
        }
    }
    
    /** removes the listener.
     * @param deletedListener the listener to be removed.
     * @param beanClass beanClass whose Deletion was being listened.
     */
    public void removeBeanDeletedListener(BeanDeletedListener deletedListener,  Class beanClass) {
        Class key = beanClass;
        List specBeanListeners;
        if(beanDeletedListeners.containsKey(key)){
            specBeanListeners = (List)beanDeletedListeners.get(key);
            specBeanListeners.remove(deletedListener);
        }
    }
    
    /** removes the listener.
     * @param updatedListener the listener to be removed.
     * @param beanClass beanClass whose Updation was being listened.
     */
    public void removeBeanUpdatedListener(BeanUpdatedListener updatedListener,  Class beanClass) {
        Class key = beanClass;
        List specBeanListeners;
        if(beanUpdatedListeners.containsKey(key)){
            specBeanListeners = (List)beanUpdatedListeners.get(key);
            specBeanListeners.remove(updatedListener);
        }
    }
    
}
