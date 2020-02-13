/*
 * BeanEvent.java
 *
 * Created on October 29, 2003, 11:37 AM
 */

package edu.mit.coeus.gui.event;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */

import edu.mit.coeus.bean.BaseBean;

/** Holds the event information. */
public class BeanEvent {
    
    private Controller source;
    private BaseBean bean;
    private int messageId;
    private Object object;
    
    /** Creates a new instance of BeanEvent */
    public BeanEvent() {
    }
    
    /** Getter for property source.
     * @return Value of property source.
     *
     */
    public Controller getSource() {
        return source;
    }
    
    /** Setter for property source.
     * @param source New value of property source.
     *
     */
    public void setSource(Controller source) {
        this.source = source;
    }
    
    /** Getter for property bean.
     * @return Value of property bean.
     *
     */
    public BaseBean getBean() {
        return bean;
    }
    
    /** Setter for property bean.
     * @param bean New value of property bean.
     *
     */
    public void setBean(BaseBean bean) {
        this.bean = bean;
    }
    
    /** Getter for property messageId.
     * @return Value of property messageId.
     *
     */
    public int getMessageId() {
        return messageId;
    }    
    
    /** Setter for property messageId.
     * @param messageId New value of property messageId.
     *
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    
    /**
     * Getter for property object.
     * @return Value of property object.
     */
    public java.lang.Object getObject() {
        return object;
    }
    
    /**
     * Setter for property object.
     * @param object New value of property object.
     */
    public void setObject(java.lang.Object object) {
        this.object = object;
    }
    
}
