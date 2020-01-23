/*
 * Controller.java
 *
 * Created on September 29, 2003, 2:29 PM
 */

package edu.mit.coeus.gui.event;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */

import java.awt.Component;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.bean.BaseBean;

/** This is the Base Class for GUI Controller.
 * All the operations/Functionalities for the GUI
 * should contain within this class.
 */

public abstract class Controller implements EventTrigger, EventManager{
    
    private static EventManagerImpl EVENT_MANAGER_IMPL;
    
    private boolean refreshRequired;
    
    private static BlockingGlassPane blockingGlassPane;
    
    /** creates a new instance of Controller.
     * @param budgetBean BudgetBean
     */
    public Controller(BaseBean baseBean){
    }
    
    /** creates a new instance of Controller.
     */
    public Controller() {
        CoeusGuiConstants.getMDIForm().putController(this);
    }
    
    /** This is used to notify whether Save is required */
    private boolean saveRequired;
    
    /** This is used to hold the mode .
     *D for Display, I for Add, U for Modify
     */
    private char functionType;
    
    static {
        //Here the Event Manager and Event Trigger Objects will be instantiated.
        EVENT_MANAGER_IMPL = EventManagerImpl.getInstance();
    }
    
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public abstract Component getControlledUI();
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data to set to the form
     */
    public abstract void setFormData(Object data)throws CoeusException;
    
    
    /** returns the form data
     * @return the form data
     */
    public abstract Object getFormData();
    
    /** return true if some data has been modified.
     * else return false
     * @return true if some data has been modified.
     * else return false
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** sets saveRequired flag.
     * @param saveRequired saveRequired
     */
    public final void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /** sets the form view mode.
     * @param functionType Possible Values:
     * Display Mode = 'D'
     * Modify Mode = 'M'
     * New Mode = 'N'
     */
    public final void setFunctionType(char functionType) {
        this.functionType = functionType;
        formatFields();
    }
    
    /** returns the function Type
     * @return function Type
     */
    public final char getFunctionType() {
        return functionType;
    }
    
    /** perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public abstract void formatFields();
    
    /** validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public abstract boolean validate() throws CoeusUIException;
    
    /** registers GUI Components with event Listeners.
     */
    public abstract void registerComponents();
    
    /** saves the Form Data.
     */
    public abstract void saveFormData()throws CoeusException;
    
    /** displays the Form which is being controlled.
     */
    public abstract void display();
    
    public void addBeanDeletedListener(BeanDeletedListener deletedListener, Class beanClass) {
        EVENT_MANAGER_IMPL.addBeanDeletedListener(deletedListener, beanClass);
    }
    
    public void addBeanUpdatedListener(BeanUpdatedListener updatedListener, Class beanClass) {
        EVENT_MANAGER_IMPL.addBeanUpdatedListener(updatedListener, beanClass);
    }
    
    public void addBeanAddedListener(BeanAddedListener addedListener, Class beanClass) {
        EVENT_MANAGER_IMPL.addBeanAddedListener(addedListener, beanClass);
    }
    
    public void removeBeanAddedListener(BeanAddedListener addedListener, Class beanClass) {
        EVENT_MANAGER_IMPL.removeBeanAddedListener(addedListener, beanClass);
    }
    
    public void removeBeanUpdatedListener(BeanUpdatedListener updatedListener, Class beanClass) {
        EVENT_MANAGER_IMPL.removeBeanUpdatedListener(updatedListener, beanClass);
    }
    
    public void removeBeanDeletedListener(BeanDeletedListener deletedListener, Class beanClass) {
        EVENT_MANAGER_IMPL.removeBeanDeletedListener(deletedListener, beanClass);
    }
    
    public void fireBeanDeleted(BeanEvent beanEvent) {
        EVENT_MANAGER_IMPL.fireBeanDeleted(beanEvent);
    }
    
    public void fireBeanAdded(BeanEvent beanEvent) {
        EVENT_MANAGER_IMPL.fireBeanAdded(beanEvent);
    }
    
    public void fireBeanUpdated(BeanEvent beanEvent) {
        EVENT_MANAGER_IMPL.fireBeanUpdated(beanEvent);
    }
    
    /** Use this method to refresh the GUI with new Data
     */
    public void refresh()throws CoeusException{}
    
    /** Getter for property refreshRequired.
     * @return Value of property refreshRequired.
     *
     */
    public boolean isRefreshRequired() {
        return refreshRequired;
    }
    
    /** Setter for property refreshRequired.
     * @param refreshRequired New value of property refreshRequired.
     *
     */
    public void setRefreshRequired(boolean refreshRequired) {
        this.refreshRequired = refreshRequired;
    }
    
    public void blockEvents(boolean block) {
        if(blockingGlassPane == null) {
            blockingGlassPane = new BlockingGlassPane();
            CoeusGuiConstants.getMDIForm().setGlassPane(blockingGlassPane);
        }
        blockingGlassPane.block(block);
    }
    
    /** will clean up the objects this controller is using.
     */
    protected void cleanUp(){}
    
}
