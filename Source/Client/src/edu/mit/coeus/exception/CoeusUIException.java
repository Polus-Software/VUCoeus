/*
 * CoeusUIException.java
 *
 * Created on August 20, 2003, 2:01 PM
 */

package edu.mit.coeus.exception;

import java.awt.Component;

/** This Exception is useful to the GUI.
 * to set Focus to a component ,
 * to set the tab index.
 *
 * after an exception has occured.
 * @author sharathk
 */
public class CoeusUIException extends CoeusClientException {
    
    /** stores tab Index.
     * Used when exception occures in one of the Tabs in TabbedPane.
     */    
    private int tabIndex;
    /** stores Component.
     * used if user has to set Focus on to the component
     * or to change the state of the component.
     */    
    private Component focusComponent;
    
    /**
     * Creates a new instance of <code>CoeusUIException</code> without detail message.
     */
    public CoeusUIException() {
    }
    
    /** Constructs an instance of <code>CoeusUIException</code> with the specified detail message.
     * @param messageKey message Key.
     */
    public CoeusUIException(String messageKey) {
        super(messageKey);
    }
    
    /** creates a new instance of <CODE>CoeusClientException</CODE>
      * @param messageKey message key.
      * @param messageType Possible values are:
      * ERROR_MESSAGE
      * INFORMATION_MESSAGE
      * WARNING_MESSAGE
      */     
    public CoeusUIException(String messageKey, int messageType) {
        super(messageKey, messageType);
    }
    
    /** Getter for property tabIndex.
     * @return Value of property tabIndex.
     */
    public int getTabIndex() {
        return tabIndex;
    }
    
    /** Setter for property tabIndex.
     * @param tabIndex New value of property tabIndex.
     */
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
    
    /** Getter for property focusComponent.
     * @return Value of property focusComponent.
     */
    public Component getFocusComponent() {
        return focusComponent;
    }
    
    /** Setter for property focusComponent.
     * @param focusComponent New value of property focusComponent.
     */
    public void setFocusComponent(Component focusComponent) {
        this.focusComponent = focusComponent;
    }
    
}
