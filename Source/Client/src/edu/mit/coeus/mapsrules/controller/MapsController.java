/*
 * MapsController.java
 *
 * Created on October 14, 2005, 5:37 PM
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.event.Controller;
import java.awt.Component;

/**
 * Class for handle the controller
 * @author tarique
 */
public abstract class MapsController extends Controller{
    
    /** Creates a new instance of MapsController */
    public MapsController() {
    }
   
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public abstract Component getControlledUI();
    /**
     *Method to clean up the register components
     */ 
    public abstract void cleanUp();
    /**
     * This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data to set to the form
     * @throws CoeusException
     */
    public abstract void setFormData(Object data) throws CoeusException;
    
    
    /** returns the form data
     * @return the form data
     */
    public abstract Object getFormData();
    
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
    
    /**
     * saves the Form Data.
     * @throws CoeusException
     */
    public abstract void saveFormData() throws CoeusException;
    
    /** displays the Form which is being controlled.
     */
    public abstract void display();
  
}
